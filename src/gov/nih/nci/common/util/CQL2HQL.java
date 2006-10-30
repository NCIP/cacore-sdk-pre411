package gov.nih.nci.common.util;


import gov.nih.nci.system.query.cql.CQLAssociation;
import gov.nih.nci.system.query.cql.CQLAttribute;
import gov.nih.nci.system.query.cql.CQLGroup;
import gov.nih.nci.system.query.cql.CQLObject;
import gov.nih.nci.system.query.cql.CQLPredicate;
import gov.nih.nci.system.query.cql.CQLQuery;
import gov.nih.nci.system.dao.QueryException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 
 *  CQL2HQL
 *  
 *  Translates a CQL query to Hibernate HQL
 * 
 * @author Satish Patel
 * 
 */
public class CQL2HQL {
	public static final String TARGET_ALIAS = "xxTargetAliasxx";
	private static Map predicateValues;
	private static Map classCache;
	private static Map fieldCache;
	private static Map setterMethodCache;

	/**
	 * Translates a CQL query into an HQL string.  This translation process assumes the
	 * CQL Query has passed validation.  Processing of invalid CQL may or may not procede
	 * with undefined results.
	 * 
	 * @param query
	 * 		The CQL Query to translate into HQL
	 * @param eliminateSubclasses
	 * 		A flag indicating that the query should be formulated to avoid
	 * 		returning subclass instances of the targeted class.
	 * @return
	 * @throws QueryException
	 */
	public static String translate(CQLQuery query, boolean eliminateSubclasses) throws QueryException {
		StringBuffer hql = new StringBuffer();
		processTarget(hql, query.getTarget(), eliminateSubclasses);
		return hql.toString();
	}
	
	private static void processTarget(StringBuffer hql, CQLObject target, boolean eliminateSubclasses) 
		throws QueryException {
		String objName = target.getName();
		hql.append("select distinct(").append(TARGET_ALIAS).append(") From ").append(objName);
		hql.append(" as ").append(TARGET_ALIAS);
		
		if(eliminateSubclasses==false && target.getAttribute() == null && target.getAssociation() == null && target.getGroup() == null)
			return;
		
		boolean andFlag = false;
		hql.append(" where ");
		if (eliminateSubclasses) {
			hql.append(TARGET_ALIAS).append(".class = ").append(objName);
			andFlag = true;
		}

		if (target.getAttribute() != null) {
			if (andFlag) 
				hql.append(" and ");
			processAttribute(hql, target.getAttribute(), true);
			andFlag = true;
		}
		if (target.getAssociation() != null) {
			if (andFlag) 
				hql.append(" and ");
			processAssociation(hql, objName, target.getAssociation(), true);
			andFlag = true;
		}
		if (target.getGroup() != null) {
			if (andFlag) 
				hql.append(" and ");
			processGroup(hql, objName, target.getGroup(), true);
		}
	}
	
	
	/**
	 * Processes an Object of a CQL Query.
	 * 
	 * @param hql
	 * 		The existing HQL query fragment
	 * @param obj
	 * 		The object to process into HQL
	 * @throws QueryException
	 */
	private static void processObject(StringBuffer hql, CQLObject obj) throws QueryException {
		String objName = obj.getName();
		hql.append("from ").append(objName);
		if (obj.getAttribute() != null) {
			hql.append(" where ");
			processAttribute(hql, obj.getAttribute(), false);
		}
		if (obj.getAssociation() != null) {
			hql.append(" where ");
			processAssociation(hql, objName, obj.getAssociation(), false);
		}
		if (obj.getGroup() != null) {
			hql.append(" where ");
			processGroup(hql, objName, obj.getGroup(), false);
		}
	}
	
	
	/**
	 * Proceses an Attribute of a CQL Query.
	 * 
	 * @param hql
	 * 		The existing HQL query fragment
	 * @param objAlias
	 * 		The alias of the object to which this attribute belongs
	 * @param attrib
	 * 		The attribute to process into HQL
	 * @throws QueryException
	 */
	private static void processAttribute(StringBuffer hql, CQLAttribute attrib, boolean useAlias) throws QueryException {
		if (useAlias) {
			hql.append(TARGET_ALIAS).append(".");
		}
		hql.append(attrib.getName());
		CQLPredicate predicate = attrib.getPredicate();
		// unary predicates
		if (predicate.equals(CQLPredicate.IS_NULL)) {
			hql.append(" is null");
		} else if (predicate.equals(CQLPredicate.IS_NOT_NULL)) {
			hql.append(" is not null");
		} else {
			// binary predicates
			String predValue = convertPredicate(predicate);
			hql.append(" ").append(predValue).append(" '").append(attrib.getValue()).append("'");
		}
	}
	
	
	/**
	 * Processes an Association of a CQL Query.
	 * 
	 * @param hql
	 * 		The existing HQL query fragment
	 * @param parentAlias
	 * 		The alias of the parent object
	 * @param parentName
	 * 		The class name of the parent object
	 * @param assoc
	 * 		The association to process into HQL
	 * @throws QueryException
	 */
	private static void processAssociation(StringBuffer hql, String parentName, CQLAssociation assoc, boolean useAlias) throws QueryException {
		// get the role name of the association
		String roleName = getRoleName(parentName, assoc);
		String sourceRoleName = assoc.getSourceRoleName();
		if (roleName == null && sourceRoleName ==null ) {
			// still null?? no association to the object!
			throw new QueryException("Association from type " + parentName + 
				" to type " + assoc.getName() + " does not exist.  Use only direct associations");
		}
		if(roleName!=null)
		{
			// make an HQL subquery for the object
			if (useAlias) {
				hql.append(TARGET_ALIAS).append(".");
			}
			hql.append(roleName).append(".id in ( select id ");
			processObject(hql, assoc);
			hql.append(")");
		}
		else
		{
			if (useAlias) {
				hql.append(TARGET_ALIAS).append(".");
			}
			hql.append("id in ( select ").append(sourceRoleName).append(".id ");
			processObject(hql, assoc);
			hql.append(")");
		}	
	}
	
	
	/**
	 * Processes a Group of a CQL Query.
	 * 
	 * @param hql
	 * 		The existing HQL query fragment
	 * @param parentName
	 * 		The type name of the parent object
	 * @param group
	 * 		The group to process into HQL
	 * @throws QueryException
	 */
	private static void processGroup(StringBuffer hql, String parentName, CQLGroup group, boolean useAlias) throws QueryException {
		String logic = group.getLogicOperator().getValue();
		
		// flag indicating a logic clause is needed before adding further query parts
		boolean logicClauseNeeded = false;
		
		// attributes
		if (group.getAttributeCollection()!= null) {
			Iterator iterator = group.getAttributeCollection().iterator();
			for (int i = 0; iterator.hasNext(); i++) {
				logicClauseNeeded = true;
				processAttribute(hql, (CQLAttribute)iterator.next(), useAlias);
				if (iterator.hasNext()) {
					hql.append(" ").append(logic).append(" ");
				}
			}
		}
		
		// associations
		if (group.getAssociationCollection() != null) {
			if (logicClauseNeeded) {
				hql.append(" ").append(logic).append(" ");
			}
			Iterator iterator = group.getAssociationCollection().iterator();
			for (int i = 0; iterator.hasNext(); i++) {
				logicClauseNeeded = true;
				processAssociation(hql, parentName, (CQLAssociation) iterator.next(), useAlias);
				if (iterator.hasNext()) {
					hql.append(" ").append(logic).append(" ");
				}
			}
		}
		
		// subgroups
		if (group.getGroupCollection() != null) {
			if (logicClauseNeeded) {
				hql.append(" ").append(logic).append(" ");
			}
			Iterator iterator = group.getGroupCollection().iterator();
			for (int i = 0; iterator.hasNext(); i++) {
				hql.append("( ");
				processGroup(hql, parentName, (CQLGroup) iterator.next(), useAlias);
				hql.append(" )");
				if (iterator.hasNext()) {
					hql.append(" ").append(logic).append(" ");
				}
			}
		}
	}
	
	
	/**
	 * Gets the role name of an association relative to its parent class.
	 * 
	 * @param parentName
	 * 		The class name of the parent of the association
	 * @param assoc
	 * 		The associated object restriction
	 * @return
	 * 		The role name of the associated object
	 * @throws QueryException
	 */
	private static String getRoleName(String parentName, CQLAssociation assoc) throws QueryException {
		String roleName = assoc.getTargetRoleName();
		if (roleName == null) {
			// determine role based on object's type
			Class parentClass = null;
			try {
				parentClass = getClassFromCache(parentName);
			} catch (Exception ex) {
				throw new QueryException("Could not load class: " + ex.getMessage(), ex);
			}
			String associationTypeName = assoc.getName();
			
			// search the fields of the right type
			Field[] typedFields = getFieldsOfTypeFromCache(parentClass, associationTypeName);
			if (typedFields.length == 1) {
				// found one and only one field
				roleName = typedFields[0].getName();
			} else if (typedFields.length > 1) {
				// more than one association found
				throw new QueryException("Association from " + parentClass.getName() + 
					" to " + associationTypeName + " is ambiguous: Specify a role name");
			}
			
			if (roleName == null) {
				// search for a setter method
				Method[] setters = getSettersForTypeFromCache(parentClass, associationTypeName);
				if (setters.length == 1) {
					String temp = setters[0].getName().substring(3);
					if (temp.length() == 1) {
						roleName = String.valueOf(Character.toLowerCase(temp.charAt(0)));
					} else {
						roleName = String.valueOf(Character.toLowerCase(temp.charAt(0))) 
							+ temp.substring(1);
					}
				} else if (setters.length > 1) {
					// more than one association found
					throw new QueryException("Association from " + parentClass.getName() + 
						" to " + associationTypeName + " is ambiguous: Specify a role name");
				}
			}
		}
		return roleName;
	}
	
	
	
	/**
	 * Converts a predicate to its HQL string equivalent.
	 * 
	 * @param p
	 * @return
	 */
	private static String convertPredicate(CQLPredicate p) {
		if (predicateValues == null) {
			predicateValues = new HashMap();
			predicateValues.put(CQLPredicate.EQUAL_TO, "=");
			predicateValues.put(CQLPredicate.GREATER_THAN, ">");
			predicateValues.put(CQLPredicate.GREATER_THAN_EQUAL_TO, ">=");
			predicateValues.put(CQLPredicate.LESS_THAN, "<");
			predicateValues.put(CQLPredicate.LESS_THAN_EQUAL_TO, "<=");
			predicateValues.put(CQLPredicate.LIKE, "LIKE");
			predicateValues.put(CQLPredicate.NOT_EQUAL_TO, "!=");
		}
		return (String) predicateValues.get(p);
	}
	
	private static Class getClassFromCache(String name) throws ClassNotFoundException
	{
		if(classCache == null) classCache = new HashMap();
		Class klass = (Class)classCache.get(name);
		if(klass==null)
		{
			klass = Class.forName(name);
			classCache.put(name, klass);
		}
		return klass;
	}

	private static Field[] getFieldsOfTypeFromCache(Class klass, String name) 
	{
		String key = klass.getName()+","+name;
		if(fieldCache == null) fieldCache = new HashMap();
		Field[] fieldCollection = (Field[])fieldCache.get(name);
		if(fieldCollection==null)
		{
			fieldCollection = getFieldsOfType(klass,name);
			fieldCache.put(key, fieldCollection);
		}
		return fieldCollection;
	}

	private static Method[] getSettersForTypeFromCache(Class klass, String name)
	{
		String key = klass.getName()+","+name;
		if(setterMethodCache == null) setterMethodCache = new HashMap();
		Method[] methodCollection = (Method[])setterMethodCache.get(name);
		if(methodCollection==null)
		{
			methodCollection = getSettersForType(klass,name);
			setterMethodCache.put(key, methodCollection);
		}
		return methodCollection;
	}
	
	/**
	 * Gets all fields from a class and it's superclasses of a given type
	 * 
	 * @param clazz
	 * 		The class to explore for typed fields
	 * @param typeName
	 * 		The name of the type to search for
	 * @return
	 */
	private static Field[] getFieldsOfType(Class clazz, String typeName) {
		Set allFields = new HashSet();
		Class checkClass = clazz;
		while (checkClass != null) {
			Field[] classFields = checkClass.getDeclaredFields();
			if(classFields!=null)
				for(int i=0;i<classFields.length;i++)
					allFields.add(classFields[i]);
			checkClass = checkClass.getSuperclass();
		}
		List namedFields = new ArrayList();
		Iterator fieldIter = allFields.iterator();
		while (fieldIter.hasNext()) {
			Field field = (Field) fieldIter.next();
			if (field.getType().getName().equals(typeName)) {
				namedFields.add(field);
			}
		}
		Field[] fieldArray = new Field[namedFields.size()];
		namedFields.toArray(fieldArray);
		return fieldArray;
	}
	
	
	private static Method[] getSettersForType(Class clazz, String typeName) {
		Set allMethods = new HashSet();
		Class checkClass = clazz;
		while (checkClass != null) {
			Method[] classMethods = checkClass.getDeclaredMethods();
			for (int i = 0; i < classMethods.length; i++) {
				Method current = classMethods[i];
				if (current.getName().startsWith("set")) {
					if (Modifier.isPublic(current.getModifiers())) {
						Class[] paramTypes = current.getParameterTypes();
						if (paramTypes.length == 1) {
							if (paramTypes[0].getName().equals(typeName)) {
								allMethods.add(current);
							}
						}
					}
				}
			}
			checkClass = checkClass.getSuperclass();
		}
		Method[] methodArray = new Method[allMethods.size()];
		allMethods.toArray(methodArray);
		return methodArray;
	}
}