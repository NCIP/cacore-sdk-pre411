package gov.nih.nci.system.util;

import gov.nih.nci.system.dao.DAO;
import gov.nih.nci.system.dao.QueryException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/** 
 *  ClassCache
 *  
 *  A Class Cache (and related metadata) facade.  Gets initialized with a list of the classes
 *  obtained from each DAO class within the System.
 * 
 * @author Dan Dumitru
 * 
 */
public class ClassCache {

	private static Logger log = Logger.getLogger(ClassCache.class);

	private List<String> allPackageNamesCache = new ArrayList<String>();
	private Map<String,List<String>> pkgClassNamesCache = new HashMap<String,List<String>>();

	private Map<String,Class> classCache = new HashMap<String,Class>();
	private Map<String,DAO> daoCache = new HashMap<String,DAO>();	
	private List<String>allQualClassNames = new ArrayList<String>();	
	private List<String>allUnqualClassNames = new ArrayList<String>();
	private Map<String,String> pkgNameForClassCache = new HashMap<String,String>();
	private Map<String,List<String>> classAssociationsCache = new HashMap<String,List<String>>();

	private Map<String,List<String>> allFieldsCache = new HashMap<String,List<String>>();	
	private Map<String,List<Field>> nonPrimitiveFieldsCache = new HashMap<String,List<Field>>();

	private List<DAO> daoList;

	//TODO :: Redo Original Below 
	private Map<String,List<String>> fieldCache = new HashMap<String,List<String>>();	

	//TODO :: Complete Type Safety changes
	private Map setterMethodCache;

	public List<String>getPkgClassNames(String packageName){
		return (List<String>)pkgClassNamesCache.get(packageName);
	}


	public List<String>getAllQualClassNames(){
		return allQualClassNames;
	}		

	public List<String>getAllUnqualClassNames(){
		return allUnqualClassNames;
	}	

	public List<String>getAllPackageNames(){
		return (List<String>)allPackageNamesCache;
	}

	public Class getClassFromCache(String className) throws ClassNotFoundException
	{
		Class klass=null;

		klass = (Class)classCache.get(className);
		if(klass==null)
		{
			log.warn("Class " + className + " not found in ClassCache");
			throw new ClassNotFoundException();
		}

		return klass;
	}

	public String getPkgNameForClass(String className){
		return pkgNameForClassCache.get(className.toLowerCase());
	}

	public boolean isPackageNameValid(String packageName) {
		return ((getPkgClassNames(packageName) != null));
	}

	public boolean isClassNameValid(String className) {
		
		try {
			getClassFromCache(className);
		} catch(ClassNotFoundException e){
			return false;
		}
		
		return true;
	}	
	
	public List<String> getFieldsOfTypeFromCache(Class klass, String typeName) 
	{
		String key = klass.getName() + "," + typeName;

		List<String> fieldCollection = (List<String>)fieldCache.get(key);
		if(fieldCollection==null)
		{
			fieldCollection = getFieldsOfType(klass, typeName);
			fieldCache.put(key, fieldCollection);
		}
		return fieldCollection;
	}

	public Method[] getSettersForTypeFromCache(Class klass, String name)
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
	public List<String> getFieldsOfType(Class clazz, String typeName) {
		Set<Field> allFields = new HashSet<Field>();
		Class checkClass = clazz;
		while (checkClass != null) {
			Field[] classFields = checkClass.getDeclaredFields();
			if(classFields!=null)
				for(int i=0;i < classFields.length;i++)
					allFields.add(classFields[i]);
			checkClass = checkClass.getSuperclass();
		}
		List<String> namedFields = new ArrayList<String>();
		Iterator fieldIter = allFields.iterator();
		while (fieldIter.hasNext()) {
			Field field = (Field) fieldIter.next();
			if (field.getType().getName().equals(typeName)) {
				namedFields.add(field.getName());
			}
		}

		return namedFields;
	}


	private Method[] getSettersForType(Class clazz, String typeName) {
		Set<Method> allMethods = new HashSet<Method>();
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

	/**
	 * Gets all fields from a class and it's superclasses 
	 * 
	 * @param clazz
	 * 		The class to explore for fields
	 * @return
	 */
	public Field[] getFields(Class clazz) {
		Set<Field> allFields = new HashSet<Field>();
		Class checkClass = clazz;
		while (checkClass != null) {
			Field[] classFields = checkClass.getDeclaredFields();
			if(classFields!=null)
				for(int i=0;i<classFields.length;i++)
					allFields.add(classFields[i]);
			checkClass = checkClass.getSuperclass();
		}
		Field[] fieldArray = new Field[allFields.size()];
		allFields.toArray(fieldArray);
		return fieldArray;
	}	

	/**
	 * Gets the data type of a particular field of the class
	 * @param className
	 * @param attribName
	 * @return
	 * @throws ClassNotFoundException 
	 */
	public String getDataType(String className, String attribName) throws QueryException
	{
		Field[] classFields;
		try
		{
			classFields = getFields(getClassFromCache(className));
			for (int i=0; i<classFields.length;i++)
			{
				if(classFields[i].getName().equals(attribName))
					return classFields[i].getType().getName();
			}
			return "";
		} 
		catch (ClassNotFoundException e)
		{
			throw new QueryException("Could not determine type of attribute "+attribName+" in class "+className,e);
		}
	}

	public boolean isCollection(String className, String attribName) throws QueryException
	{
		Field[] classFields;
		try
		{
			classFields = getFields(getClassFromCache(className));
			for (int i=0; i<classFields.length;i++)
			{
				if(classFields[i].getName().equals(attribName))
				{
					Class type = classFields[i].getType();
					if("java.util.Collection".equals(type.getName()))
						return true;

					return false;
				}
			}
			return false;
		} 
		catch (ClassNotFoundException e)
		{
			throw new QueryException("Could not determine type of attribute "+attribName+" in class "+className,e);
		}
	}

	public List<String>getAllFieldNames(String className){

		List<String> tmpFieldCache = null;

		try {
			tmpFieldCache = allFieldsCache.get(getClassFromCache(className));
		} catch(ClassNotFoundException cnfe){
			log.error("Exception: Class not found: ", cnfe);
		}

		return tmpFieldCache;
	}

	/**
	 * Get the list of all fields for the class
	 * @param className
	 * @return List of all fields for the given class
	 */
	private List<String> cacheAllFieldNames(Class klass)
	{
		List<String> fieldNames = new ArrayList<String>();

		try
		{
			Field[] fields = getAllFields(klass);
			String fieldType;
			for(int i=0; i< fields.length; i++)
			{
				fields[i].setAccessible(true);
				fieldType = fields[i].getType().getName();
				log.debug("Class: " + klass.getName() + "; Field: " + fields[i].getName() + "; FieldType: " + fieldType + "; FieldGenericType: " + getReturnType(fields[i].getGenericType().toString()));

				if (isSearchable(fieldType))
				{
					fieldNames.add(fields[i].getName());    
				}
			}
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return fieldNames;

	}	

	protected List<Field>getNonPrimitiveFields(String className){

		List<Field> tmpFieldCache = nonPrimitiveFieldsCache.get(className);

		return tmpFieldCache;
	}

	/**
	 * Get the list of all non-Primitive fields for the class
	 * @param className
	 * @return List of all fields for the given class
	 */
	private List<Field> cacheNonPrimitiveFieldNames(Class klass)
	{
		List<Field> tmpFields = new ArrayList<Field>();

		try	{			
			Field[] fields = getAllFields(klass);
			if (fields != null) {
				log.debug("FieldNames cache size: " + fields.length);
			} else {
				log.debug("FieldNames cache size is null");
			}

			for (Field field : fields) {
				field.setAccessible(true);

				if (!field.getType().isPrimitive())
				{
					tmpFields.add(field);    
				}
			}
		} catch(Exception e) {
			log.error(e.getMessage());
		}

		log.debug("non-Primitive FieldNames cache size: " + tmpFields.size());
		return tmpFields;
	}	

	public List<String> getAssociations(String className) {

		String qualClassName = null;
		if(className.indexOf(".") < 1) {
			String packageName = getPkgNameForClass(className);
			qualClassName = packageName + "." + className;
		} else {
			qualClassName = className;
		}		
		return classAssociationsCache.get(qualClassName);
	}


	private List<String> cacheAssociations(String className) {
		String qualClassName = null;

		//Get the package name for the qualified classname
		String packageName = null;
		if(className.indexOf(".") < 1){
			packageName = getPkgNameForClass(className);
			qualClassName = packageName + "." + className;
		} else {
			qualClassName = className;
			packageName = className.substring(0, className.lastIndexOf("."));
		}
		log.debug("Qualified Class name: " + qualClassName);
		log.debug("packageName: " + packageName);	

		List<Field> fields = getNonPrimitiveFields(qualClassName);

		HashSet<String> roleNames = new HashSet<String>();
		roleNames.add(qualClassName);

		for(Field field : fields) {
			field.setAccessible(true); 
			String type = field.getType().getName();
			log.debug("fieldType: " + type);
			String fieldName = field.getName();
			log.debug("fieldName: " + fieldName);
			if(!field.getType().isPrimitive()){
				if(fieldName.endsWith("Collection") || (type.startsWith("java") && type.endsWith("Collection"))){
					String roleClassName;
					String beanName;
					if(fieldName.endsWith("Collection")){
						beanName = fieldName.substring(0, fieldName.lastIndexOf("Collection"));
					} else {
						beanName = fieldName;
					} 

					roleClassName = locateClass(beanName, packageName);
					log.debug("roleClassName: " + roleClassName);
					if(roleClassName != null){
						roleNames.add(roleClassName);
					}                   
				} else if(!type.startsWith("java")) {
					if(type.startsWith(packageName)) {
						roleNames.add(type);
					} else {
						int counter = 0;
						for(int x=0; x<packageName.length(); x++){
							if(packageName.charAt(x)== '.'){
								counter++;
							}
						}
						String pkg = packageName.substring(0, packageName.lastIndexOf("."));
						for(int x=counter; x>1; x--){
							if(type.startsWith("pkg")){
								roleNames.add(type);
								break;
							}
							pkg = pkg.substring(0, pkg.lastIndexOf("."));
						}
					}
				} 
			}
		}

		try {	
			if(!(Class.forName(qualClassName).getSuperclass().getName().equalsIgnoreCase("java.lang.Object"))){
				String superClassName = Class.forName(qualClassName).getSuperclass().getName();
				List<String> associations = cacheAssociations(superClassName);
				for(Object roleName : associations) {
					if(!(superClassName.equals((String)roleName))){
						roleNames.add((String)roleName);
					}               
				}
			}
		} catch (ClassNotFoundException e){
			log.error("Exception caught: ", e);
		}


		ArrayList<String> roles = new ArrayList<String>();
		for(String role : roleNames) {
			roles.add(role);
		}     

		return roles;
	}

	public boolean isSearchable(String fieldType){
		boolean isSearchable=false;

		if(fieldType.equals("java.lang.Long") || 
				fieldType.equals("java.lang.String") || 
				fieldType.equals("java.lang.Integer") || 
				fieldType.equals("java.lang.Float") || 
				fieldType.equals("java.lang.Double") || 
				fieldType.equals("java.lang.Boolean") || 
				fieldType.equals("java.util.Date")
		){
			isSearchable = true;
		}

		return isSearchable;
	}


	private String locateClass(String beanName, String packageName){
		String className = null;

		try {
			Class klass = getClassFromCache(beanName);
			if (klass != null){
				className = klass.getName();
				log.debug("Found Class " + className + " for bean " + beanName);
			}
		} catch (ClassNotFoundException e){
			return null;
		}

		String packageNameFromCache = getPkgNameForClass(beanName);
		log.debug("packageNameFromCache: " + packageNameFromCache + " for bean name: " + beanName);
		if (!packageName.equalsIgnoreCase(packageNameFromCache)){
			return null;
		} 

		return packageName + "." + className;
	}

	/**
	 * Gets all the fields for a given class
	 * @param resultClass - Specifies the class name
	 * @return - returns all the fields of a class
	 */
	public Field[] getAllFields(Class klass){
		List<Field> fieldList = new ArrayList<Field>();
		try{
			getAllFields(klass, fieldList);
		}catch(Exception ex){
			log.error("Exception: ", ex);
		}

		Field[] fields = new Field[fieldList.size()];
		for(int i=0;i<fieldList.size(); i++){
			fields[i]= (Field)fieldList.get(i);
		}
		return fields;
	}	

	private void getAllFields(Class klass, List<Field> fieldList){

		if ( klass == null || 
				klass.getName().equalsIgnoreCase("java.lang.Object") ||
				klass.isInterface() ||
				klass.isPrimitive()) {
			return; // end of processing
		} 

		getAllFields(klass.getSuperclass(), fieldList);

		Field[] fields = klass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			String fieldName = fields[i].getName();
			if(fieldName.indexOf('$')==-1)
				fieldList.add(fields[i]);
		}	
	}

	public List<DAO> getDaoList() {
		return daoList;
	}


	/**
	 * @param daoList 	A list of DAO's for which Class metadata should be generated and cached.
	 * 					Called by the Spring Framework.  See ApplicationConfig for more details.
	 */
	public void setDaoList(List<DAO> daoList) {
		this.daoList = daoList;
		initialize();
	}	

	public DAO getDAOForClass(String qualClassName){
		
		return daoCache.get(qualClassName);
	}

	
	public static String getReturnType(String fieldGenericType){
		log.debug("fieldGenericType: " + fieldGenericType);
		int begin = fieldGenericType.indexOf('<');
		int end = fieldGenericType.indexOf('>'); 

		if (begin > -1 && end > -1)
			return fieldGenericType.substring(begin+1,end);

		return fieldGenericType;
	}

	private void initialize() { // initialize with a list of the classes obtained from each DAO class within the System

		String unqualifiedClassName = null;
		Class klass = null;

		HashSet<String> tmpPackageNames = new HashSet<String>();

		List<String> allClassNames;

		for(DAO dao:daoList){

			allClassNames = dao.getAllClassNames();

			// Certain metadata needs to be generated prior to caching the rest of the info
			for(String klassName:allClassNames){

				try {
					klass = Class.forName(klassName);
				} catch (ClassNotFoundException e){
					log.error("ClassNotFoundException caught: ", e);
				}
				String packageName = klass.getPackage().getName();
				unqualifiedClassName = klassName.substring(klassName.lastIndexOf(".") + 1);
				log.debug("Unqualified class name: " + unqualifiedClassName);

				// Cache the package name for each klass
				pkgNameForClassCache.put(klassName.toLowerCase(), packageName);			
				pkgNameForClassCache.put(unqualifiedClassName.toLowerCase(), packageName);
				nonPrimitiveFieldsCache.put(klassName, cacheNonPrimitiveFieldNames(klass));			
			}

			// Certain metadata needs to be cached prior to caching the rest, 
			// so here we loop through the second time now that we have the data 
			// we need
			for(String klassName:allClassNames){
				log.debug("Adding class " + klassName + " to allClassNames List");		

				allQualClassNames.add(klassName);
				unqualifiedClassName = klassName.substring(klassName.lastIndexOf(".") + 1);
				log.debug("Unqualified class name: " + unqualifiedClassName);
				allUnqualClassNames.add(unqualifiedClassName);

				List<String>pkgClassNames  = new ArrayList<String>(); 
				try {
					klass = Class.forName(klassName);

					allFieldsCache.put(klassName, cacheAllFieldNames(klass));

// TODO :: Implement Error handling in case of duplicate class/package entries
					log.debug("Adding class " + klass.getName() + " to Class Cache.");
					classCache.put(klassName, klass);
					classCache.put(klassName.toLowerCase(), klass);
					classCache.put(unqualifiedClassName, klass);
					classCache.put(unqualifiedClassName.toLowerCase(), klass);
					
					log.debug("Adding class " + klass.getName() + " to DAO Cache for DAO: " + dao.getClass().getName());
					daoCache.put(klassName, dao);
					daoCache.put(klassName.toLowerCase(), dao);
					daoCache.put(unqualifiedClassName, dao);
					daoCache.put(unqualifiedClassName.toLowerCase(), dao);					

					String packageName = klass.getPackage().getName();

					// Cache all package names
					tmpPackageNames.add(packageName);

					// Cache associations for klass
					classAssociationsCache.put(klassName, cacheAssociations(klassName));

					// Collect all class names within a package
					if(!pkgClassNamesCache.containsKey(packageName)) {
						pkgClassNames.add(klassName);
						pkgClassNamesCache.put(packageName, pkgClassNames);
					} else {
						List<String> existingCollection = (List<String>)pkgClassNamesCache.get(packageName);
						existingCollection.add(klassName);
					}	

				} catch(ClassNotFoundException cnfe) {
					log.error("Exception caught while initializing ClassCache for class: " + klassName, cnfe);
				}
			}
		}

		allPackageNamesCache = new ArrayList<String>(tmpPackageNames);

		Collections.sort(allPackageNamesCache);		
		Collections.sort(allQualClassNames);		
		Collections.sort(allUnqualClassNames);
	}	

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("[\n" + ClassCache.class.getName()+"[\n");


		sb.append("\tDAO Cache: [");
		String daoName;
		for(DAO dao:daoList){
			daoName = (String)dao.getClass().getName();
			sb.append("\n\t\t" + daoName );
		}		
		sb.append("\n\t]\n\n");

		sb.append("\tClass Cache: [");

		Class klass;
		for(String klassName:allUnqualClassNames){
			klass = (Class)classCache.get(klassName);
			sb.append("\n\t\t" + klassName + ": " + klass.getName());
		}
		sb.append("\n\t]\n");

		sb.append("]\n");		

		return sb.toString();
	}	
}