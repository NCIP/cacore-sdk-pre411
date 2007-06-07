package gov.nih.nci.codegen.util;

import gov.nih.nci.codegen.GenerationException;
import gov.nih.nci.common.util.Constant;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAssociation;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAssociationEnd;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAttribute;
import gov.nih.nci.ncicb.xmiinout.domain.UMLClass;
import gov.nih.nci.ncicb.xmiinout.domain.UMLDatatype;
import gov.nih.nci.ncicb.xmiinout.domain.UMLDependency;
import gov.nih.nci.ncicb.xmiinout.domain.UMLGeneralization;
import gov.nih.nci.ncicb.xmiinout.domain.UMLModel;
import gov.nih.nci.ncicb.xmiinout.domain.UMLPackage;
import gov.nih.nci.ncicb.xmiinout.domain.UMLTaggedValue;
import gov.nih.nci.ncicb.xmiinout.util.ModelUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class TransformerUtils
{
	
	private static Logger log = Logger.getLogger(TransformerUtils.class);
	
	public static String getFQCN(UMLClass klass)
	{
		return removeBasePackage(ModelUtil.getFullName(klass));
	}

	public static String getFullPackageName(UMLClass klass)
	{
		return removeBasePackage(ModelUtil.getFullPackageName(klass));
	}

	public static String getFullPackageName(UMLPackage pkg)
	{
		return removeBasePackage(ModelUtil.getFullPackageName(pkg));
	}


	private static String removeBasePackage(String path)
	{
		//TODO - Get the base package from else where
		String basePkg = "Logical View.Logical Model";
		if(path.startsWith(basePkg))
			return path.substring(basePkg.length()+1);
		else
			return path;
	}

	public static UMLClass getSuperClass(UMLClass klass) throws GenerationException
	{
		UMLClass[] superClasses = ModelUtil.getSuperclasses(klass);

		if(superClasses.length == 0) {
			log.debug("*** Getting superclass for class " + klass.getName() + ": " + null);
			
			return null;
		}

		if(superClasses.length > 1)
			throw new GenerationException("Class can not have more than one super class");
		
		log.debug("*** Getting superclass for class " + klass.getName() + ": " + superClasses[0].getName());
		
		return superClasses[0];
	}

	public static String getSuperClassString(UMLClass klass) throws GenerationException
	{
		UMLClass[] superClasses = ModelUtil.getSuperclasses(klass);

		if(superClasses.length == 0) return "";

		if(superClasses.length>1)
			throw new GenerationException("Class can not have more than one super classes");

		return "extends " + superClasses[0].getName();
	}

	public static String getImports(UMLClass klass) throws GenerationException
	{
		StringBuilder sb = new StringBuilder();
		Set<String> importList = new HashSet<String>();
		UMLClass[] superClasses = ModelUtil.getSuperclasses(klass);

		if(superClasses.length>1)
			throw new GenerationException("Class can not have more than one super classes");

		String pkgName = getFullPackageName(klass);
		if(superClasses.length == 1)
		{ 
			String superPkg = getFullPackageName(superClasses[0]);
			if(!pkgName.equals(superPkg))
				importList.add(getFullPackageName(superClasses[0]));
		}
		
		for(UMLAssociation association: klass.getAssociations())
		{
			List<UMLAssociationEnd> assocEnds = association.getAssociationEnds();
			UMLAssociationEnd otherEnd = TransformerUtils.getOtherEnd(klass,assocEnds);
			
			String assocPkg = getFullPackageName ((UMLClass)otherEnd.getUMLElement());
			if(!pkgName.equals(assocPkg) && !importList.contains(assocPkg))
				importList.add(assocPkg);
			if(isAssociationEndMany(otherEnd) && otherEnd.isNavigable()&& !importList.contains("java.util.Collection"))
				importList.add("java.util.Collection");
		}
		
		for(String importClass:importList)
			sb.append("import ").append(importClass).append(";");

		return sb.toString();
	}

	public static String getDataType(UMLAttribute attr)
	{
		UMLDatatype dataType = attr.getDatatype();
		String name = dataType.getName();
		if(dataType instanceof UMLClass)
			name = getFQCN((UMLClass)dataType);
		
		if(name.startsWith("java.lang."))
			name = name.substring("java.lang.".length());

		if("int".equalsIgnoreCase(name) || "integer".equalsIgnoreCase(name))
			return "Integer";
		if("double".equalsIgnoreCase(name))
			return "Double";
		if("float".equalsIgnoreCase(name))
			return "Float";
		if("long".equalsIgnoreCase(name))
			return "Long";
		if("string".equalsIgnoreCase(name))
			return "String";
		if("char".equalsIgnoreCase(name) || "character".equalsIgnoreCase(name))
			return "Character";
		if("boolean".equalsIgnoreCase(name) )
			return "Boolean";

		if("date".equalsIgnoreCase(name) || "java.util.date".equalsIgnoreCase(name))
			return "java.util.Date";

		System.out.println("Type = "+name);
		
		return name;
	}


	public static String getHibernateDataType(UMLAttribute attr)
	{
		UMLDatatype dataType = attr.getDatatype();
		String name = dataType.getName();
		if(dataType instanceof UMLClass)
			name = getFQCN((UMLClass)dataType);
		
		if(name.startsWith("java.lang."))
			name = name.substring("java.lang.".length());

		if("int".equalsIgnoreCase(name) || "integer".equalsIgnoreCase(name))
			return "integer";
		if("double".equalsIgnoreCase(name))
			return "double";
		if("float".equalsIgnoreCase(name))
			return "float";
		if("long".equalsIgnoreCase(name))
			return "long";
		if("string".equalsIgnoreCase(name))
			return "string";
		if("char".equalsIgnoreCase(name) || "character".equalsIgnoreCase(name))
			return "character";
		if("boolean".equalsIgnoreCase(name) )
			return "boolean";

		if("date".equalsIgnoreCase(name) || "java.util.date".equalsIgnoreCase(name))
			return "java.util.Date";

		System.out.println("Type = "+name);
		
		return name;
	}	
	
	public static String getGetterMethodName(UMLAttribute attr)
	{
		String name = attr.getName(); 
		return "get"+name.substring(0,1).toUpperCase()+name.substring(1,name.length());
	}	

	public static String getSetterMethodName(UMLAttribute attr)
	{
		String name = attr.getName(); 
		return "set"+name.substring(0,1).toUpperCase()+name.substring(1,name.length());
	}

	public static UMLAssociationEnd getThisEnd(UMLClass klass, List<UMLAssociationEnd>assocEnds) throws GenerationException
	{
		UMLAssociationEnd end1 = assocEnds.get(0);
		UMLAssociationEnd end2 = assocEnds.get(1);
		if(end1.getUMLElement().equals(klass))
			return end1;
		else if(end2.getUMLElement().equals(klass))
			return end2;
		else
			throw new GenerationException("Could not figureout this end");
	}

	public static UMLAssociationEnd getOtherEnd(UMLClass klass, List<UMLAssociationEnd>assocEnds) throws GenerationException
	{
		UMLAssociationEnd end1 = assocEnds.get(0);
		UMLAssociationEnd end2 = assocEnds.get(1);
		if(end1.getUMLElement().equals(klass))
			return end2;
		else if(end2.getUMLElement().equals(klass))
			return end1;
		else
			throw new GenerationException("Could not figureout this end");
	}

	public static Boolean isAssociationEndMany(UMLAssociationEnd assocEnd)
	{
		if((assocEnd.getHighMultiplicity()<1)||(assocEnd.getLowMultiplicity()<0)) 
			return true;
		else
			return false;
	}

	public static String getGetterMethodName(UMLAssociationEnd assocEnd)
	{
		String name = assocEnd.getRoleName(); 
		return "get"+name.substring(0,1).toUpperCase()+name.substring(1,name.length());
	}	

	public static String getSetterMethodName(UMLAssociationEnd assocEnd)
	{
		String name = assocEnd.getRoleName(); 
		return "set"+name.substring(0,1).toUpperCase()+name.substring(1,name.length());
	}

	public static Boolean isSelfAssociation(UMLAssociationEnd assocEnd1,UMLAssociationEnd assocEnd2)
	{
		return assocEnd1.getUMLElement().equals(assocEnd2.getUMLElement());
	}

	public static String getClassIdGetterMthod(UMLClass klass) throws GenerationException
	{
		return "get"+firstCharUpper(getClassIdAttrName(klass));
	}

	private static String firstCharUpper(String data)
	{
		if(data == null || data.length() == 0) return data;
		return Character.toUpperCase(data.charAt(0)) + data.substring(1); 
	}
	
	public static String getClassIdAttrName(UMLClass klass) throws GenerationException
	{
		return getClassIdAttr(klass).getName();
	}

	public static UMLAttribute getClassIdAttr(UMLClass klass) throws GenerationException
	{
		UMLClass table = getTable(klass);
		String fqcn = getFQCN(klass);
		String idAttr = "id";
		int count = 0;
		for(UMLAttribute column:table.getAttributes())
		{
			for(UMLTaggedValue tv: column.getTaggedValues())
			{
				if ("id-attribute".equals(tv.getName()))
				{
					String tvValue = tv.getValue();
					if(tvValue.startsWith(fqcn+"."))
					{
						String attrName = tvValue.substring((fqcn+".").length());
						count++;
					}
				}
			}
		}
		
		if(count > 1)
			throw new GenerationException("More than one column found that maps to the primary key identifier for class : "+fqcn);
		
		for(UMLAttribute attribute:klass.getAttributes())
		{
			if(idAttr.equals(attribute.getName()))
				return attribute;
		}
		for(UMLGeneralization gen: klass.getGeneralizations())
		{
			if(gen.getSubtype() == klass)
			{
				UMLAttribute superId = getClassIdAttr(gen.getSupertype());
				if(superId != null)
					return superId;
			}
		}
		
		throw new GenerationException("No column found that maps to the primary key identifier for class : "+fqcn);
	}
	
	// TODO :: add method to UMLAttribute instead?  However, would alter existing interface...
	public static boolean isStatic(UMLAttribute att){
		
		UMLTaggedValue tValue = att.getTaggedValue("static");
		
		if (tValue == null) {
			return false;
		}
		
		log.debug("UMLAttribute 'static' Tagged Value: " + tValue.getValue());
		return ("1".equalsIgnoreCase(tValue.getValue()));
	}
	
	public static String getType(UMLAttribute att){
		
		UMLTaggedValue tValue = att.getTaggedValue("type");
		
		if (tValue == null) {
			return "";
		}
		
		log.debug("UMLAttribute Type Tagged Value: " + tValue.getValue());
		return tValue.getValue();
	}	
	
	public static String getType(UMLAssociationEnd assocEnd){
		
		UMLTaggedValue tValue = assocEnd.getTaggedValue("type");
		
		if (tValue == null) {
			return "";
		}
		
		log.debug("UMLAttribute Type Tagged Value: " + tValue.getValue());
		return tValue.getValue();
	}		
	
	public static UMLAssociationEnd getOtherAssociationEnd(UMLAssociationEnd assocEnd) {
		UMLAssociationEnd otherAssocEnd = null;
		for (Iterator i = assocEnd.getOwningAssociation().getAssociationEnds().iterator(); i
				.hasNext();) {
			UMLAssociationEnd ae = (UMLAssociationEnd) i.next();
			if (ae != assocEnd) {
				otherAssocEnd = ae;
				break;
			}
		}
		return otherAssocEnd;
	}	
	
	public static String getUpperBound(UMLAssociationEnd otherEnd) {

		int multiplicity = otherEnd.getHighMultiplicity();
		String finalMultiplicity = new String();
		if (multiplicity == -1) {
			finalMultiplicity = "unbounded";
		} else {
			Integer x = new Integer(multiplicity);
			finalMultiplicity = x.toString();
		}
		return finalMultiplicity;
	}	
	
	public static String getLowerBound(UMLAssociationEnd otherEnd) {

		int multiplicity = otherEnd.getLowMultiplicity();
		String finalMultiplicity = new String();
		if (multiplicity == -1) {
			finalMultiplicity = "unbounded";
		} else {
			Integer x = new Integer(multiplicity);
			
			finalMultiplicity = x.toString();
		}
		return finalMultiplicity;
	}	
	
	/**
	 * @param thisEnd
	 * @param otherEnd
	 * @return
	 */
	public static boolean isMany2One(UMLAssociationEnd thisEnd,
			UMLAssociationEnd otherEnd) {

		String thisEndUpperBound = getUpperBound(thisEnd);
		log.debug("thisEnd upper bound: " + thisEndUpperBound);
		
		String otherEndUpperBound = getUpperBound(otherEnd);
		log.debug("otherEnd upper bound: " + otherEndUpperBound);
		
		if (thisEndUpperBound.equalsIgnoreCase("unbounded")) {
			thisEndUpperBound = "-1";
		}
		
		if (otherEndUpperBound.equalsIgnoreCase("unbounded")) {
			otherEndUpperBound = "-1";
		}

		return ((Integer.parseInt(thisEndUpperBound) == -1 || Integer.parseInt(thisEndUpperBound) > 1) && 
				 Integer.parseInt(otherEndUpperBound) == 1);
		// return ((5 > 1) && 1 == 1);
	}	
	/**
	 * @param thisEnd
	 * @param otherEnd
	 * @return
	 */
	public static boolean isOne2Many(UMLAssociationEnd thisEnd,
			UMLAssociationEnd otherEnd) {

		String thisEndUpperBound = getUpperBound(thisEnd);
		log.debug("thisEnd upper bound: " + thisEndUpperBound);
		
		String otherEndUpperBound = getUpperBound(otherEnd);
		log.debug("otherEnd upper bound: " + otherEndUpperBound);
		
		if (thisEndUpperBound.equalsIgnoreCase("unbounded")) {
			thisEndUpperBound = "-1";
		}
		
		if (otherEndUpperBound.equalsIgnoreCase("unbounded")) {
			otherEndUpperBound = "-1";
		}		
		
		return ((Integer.parseInt(thisEndUpperBound) == 1) && 
				(Integer.parseInt(otherEndUpperBound) == -1 || Integer.parseInt(otherEndUpperBound) > 1));

	}
	
	/**
	 * @param thisEnd
	 * @param otherEnd
	 * @return
	 */
	public static boolean isMany2Many(UMLAssociationEnd thisEnd,
			UMLAssociationEnd otherEnd) {

		String thisEndUpperBound = getUpperBound(thisEnd);
		log.debug("thisEnd upper bound: " + thisEndUpperBound);
		
		String otherEndUpperBound = getUpperBound(otherEnd);
		log.debug("otherEnd upper bound: " + otherEndUpperBound);
		
		if (thisEndUpperBound.equalsIgnoreCase("unbounded")) {
			thisEndUpperBound = "-1";
		}
		
		if (otherEndUpperBound.equalsIgnoreCase("unbounded")) {
			otherEndUpperBound = "-1";
		}

		return ((Integer.parseInt(thisEndUpperBound) == -1 || Integer.parseInt(thisEndUpperBound) > 1) && 
				(Integer.parseInt(otherEndUpperBound) == -1 || Integer.parseInt(otherEndUpperBound) > 1) );
	}	
	
	public static Collection getAssociationEnds(UMLClass klass) {
		return getAssociationEnds(klass, false);
	}

	public static Collection getAssociationEnds(UMLClass klass,
			boolean includeInherited) {
		log.debug("class = " + klass.getName() + ", includeInherited = "
				+ includeInherited);
		Map assocEndsMap = new HashMap();
		UMLClass superClass = klass;
		while (superClass != null) {
			Collection assocs = superClass.getAssociations();
			for (Iterator i = assocs.iterator(); i.hasNext();) {
				UMLAssociation assoc = (UMLAssociation) i.next();
				
			
				for (UMLAssociationEnd ae:assoc.getAssociationEnds()){
					UMLAssociationEnd otherEnd = getOtherAssociationEnd(ae);
					String id = ((UMLClass)(otherEnd.getUMLElement())).getName() + Constant.LEFT_BRACKET
							+ getFQCN((UMLClass)(otherEnd.getUMLElement())) + Constant.RIGHT_BRACKET;
					
					log.debug("id: " + id);
					log.debug("assocEndsMap.get(id) == null ? " + (assocEndsMap.get(id) == null));
					log.debug("ae.getType(): " + (UMLClass)(ae.getUMLElement()));
					log.debug("superClass: " + superClass);
					
					if ((UMLClass)ae.getUMLElement() == superClass && assocEndsMap.get(id) == null) {
						log.debug("adding assoc: " + id);
						log.debug("adding assoc: " + id);
						assocEndsMap.put(id, ae);
					}
				}
			}
			if (includeInherited) {

				// TODO :: Implement includeInherited
//				Collection gens = superClass.getGeneralization();
//				if (gens.size() > 0) {
//					superClass = (Classifier) ((Generalization) gens.iterator()
//							.next()).getParent();
//				} else {
//					superClass = null;
//				}
				log.debug("Need to implement includeInherited");

			} else {
				superClass = null;
			}
		}	
		
		return assocEndsMap.values();
	}
	
	public static void collectPackages(Collection<UMLPackage> nextLevelPackages, Hashtable<UMLPackage, Collection<UMLClass>> pkgColl, Collection<UMLClass> classColl)
	{
		for(UMLPackage pkg:nextLevelPackages){
			Collection<UMLClass> pkgClasses = pkg.getClasses();

			if (pkgClasses != null && pkgClasses.size() > 0){
				for (UMLClass klass:pkgClasses){
					String pkgName = TransformerUtils.getFullPackageName(klass);
					if(!"table".equals(klass.getStereotype()) && !pkgName.startsWith("java.lang") && !pkgName.startsWith("java.util")) {
						classColl.add(klass);

						if(!pkgColl.containsKey(pkg)) {
							List<UMLClass> classes = new ArrayList();
							classes.add(klass);
							pkgColl.put(pkg, classes);
						} else {
							Collection<UMLClass> existingCollection = pkgColl.get(pkg);
							existingCollection.add(klass);
						}					
					}
				}
			}
			collectPackages(pkg.getPackages(), pkgColl, classColl);	
		}
	}	
	
	public static Collection<UMLClass> getAllClasses(UMLModel model)
	{
		Collection<UMLClass> classes = new HashSet<UMLClass>();
		getAllClasses(model.getPackages(),classes);
		return classes;
	}
	
	private static void getAllClasses(Collection<UMLPackage> pkgCollection,Collection<UMLClass> classes)
	{
		for(UMLPackage pkg:pkgCollection)
			getAllClasses(pkg,classes);
	}
	
	private static void getAllClasses(UMLPackage rootPkg,Collection<UMLClass> classes)
	{
		for(UMLClass klass:rootPkg.getClasses())
		{
			String pkgName = TransformerUtils.getFullPackageName(klass);
			if(!"table".equals(klass.getStereotype()) && !pkgName.startsWith("java.lang") && !pkgName.startsWith("java.util"))
				classes.add(klass);
		}
		getAllClasses(rootPkg.getPackages(),classes);
	}	
	

	
	public static Collection<UMLClass> getAllParentClasses(UMLModel model)
	{
		Collection<UMLClass> classes = new ArrayList<UMLClass>();
		getAllParentClasses(model.getPackages(),classes);
		return classes;
	}
	
	private static void getAllParentClasses(Collection<UMLPackage> pkgCollection,Collection<UMLClass> classes)
	{
		for(UMLPackage pkg:pkgCollection)
			getAllParentClasses(pkg,classes);
	}
	
	private static void getAllParentClasses(UMLPackage rootPkg,Collection<UMLClass> classes)
	{
		for(UMLClass klass:rootPkg.getClasses())
		{
			String pkgName = TransformerUtils.getFullPackageName(klass);
			if(!"table".equals(klass.getStereotype()) && !pkgName.startsWith("java.lang") && !pkgName.startsWith("java.util") && ModelUtil.getSuperclasses(klass).length == 0)
				classes.add(klass);
		}
		getAllParentClasses(rootPkg.getPackages(),classes);
	}

	public static UMLClass getTable(UMLClass klass) throws GenerationException
	{
		Set<UMLDependency> dependencies = klass.getDependencies();
		int count = 0;
		for(UMLDependency dependency:dependencies)
		{
			//TODO Cortrect the condition for the UMLDependency DataSource check
			count++;
			if(count>1)
				throw new GenerationException("No table found for : "+getFQCN(klass));
		}
		
		for(UMLDependency dependency:dependencies)
		{
			UMLClass client = (UMLClass) dependency.getClient();
			if(!"table".equalsIgnoreCase(client.getStereotype()))
				throw new GenerationException("Invalid stereotyoe for the dependency of : "+getFQCN(klass));
			
			return client;
		}
		return null;
	}
	
	public static String getMappedColumnName(UMLClass table, String attrName) throws GenerationException
	{
		int count = 0;
		UMLAttribute targetColumn = null; 
		for(UMLAttribute column: table.getAttributes())
		{
			for(UMLTaggedValue tv: column.getTaggedValues())
			{
				if ("mapped-attributes".equals(tv.getName()))
				{
					String tvValue = tv.getValue();
					String[] tvValues = tvValue.split(",");
					for(String val:tvValues)
					{
						if(attrName.equals(val))
						{
							count++;
							targetColumn = column;
						}
					}
				}
			}
		}
		
		if(count == 0) throw new GenerationException("No column found for : "+attrName);
		if(count > 1) throw new GenerationException("More than one column found for : "+attrName);
	
		return targetColumn.getName();
	}
	
	public static String getEmptySpace(Integer count)
	{
		String spaces = "";
		
		for(Integer i=0;i<count;i++)
			spaces +="\t";
		
		return spaces;
	}
	
	
	public static UMLClass findCorrelationTable(UMLAssociation association, UMLModel model, UMLClass klass) throws GenerationException
	{
		String tvValue = null;
		int count = 0;
		for(UMLTaggedValue tv: association.getTaggedValues())
		{
			if ("correlation-table".equals(tv.getName()))
			{
				tvValue = tv.getValue();
				count++;
			}
		}
		List<UMLAssociationEnd> ends = association.getAssociationEnds();
		UMLAssociationEnd thisEnd = getThisEnd(klass, ends);
		UMLAssociationEnd otherEnd = getOtherEnd(klass, ends);
		
		String temp = ((UMLClass)(thisEnd.getUMLElement())).getName()+"."+otherEnd.getRoleName();
		
		temp += "-->" +((UMLClass)(otherEnd.getUMLElement())).getName()+"."+thisEnd.getRoleName();
		
		if(count == 0 || tvValue == null || tvValue.trim().length() == 0) throw new GenerationException("No correlation table found for "+temp +" association");
		if(count > 1) throw new GenerationException("More than one correlation table found for "+temp+" association");
		
		UMLClass correlationTable = ModelUtil.findClass(model,"Logical View.Data Model."+tvValue);
		
		if(correlationTable == null) throw new GenerationException("No correlation table found named : \""+tvValue+"\"");
		
		return correlationTable;
	}
	
	public static String findAssociatedColumn(UMLClass table,UMLClass klass, UMLAssociationEnd otherEnd, Boolean throwException) throws GenerationException
	{
		String tvVal = getFQCN(klass) +"."+ otherEnd.getRoleName();
		String tvKey = "implements-association";
		
		UMLAttribute resultColumn = null;
		int count = 0;
		
		for(UMLAttribute column: table.getAttributes())
		{
			for(UMLTaggedValue tv: column.getTaggedValues())
			{
				if (tvKey.equals(tv.getName()))
				{
					String tvValue = tv.getValue();
					String[] tvValues = tvValue.split(",");
					for(String val:tvValues)
					{
						if(tvVal.equals(val))
						{
							count++;
							resultColumn = column;
						}
					}
				}
			}
		}
		
		if(count == 0 && throwException) throw new GenerationException("No column found for : "+tvVal);
		if(count > 1) throw new GenerationException("More than one column found for : "+tvVal);
		
		if(resultColumn == null ) return "";
		return resultColumn.getName();
	}

	public static String findAssociatedColumn(UMLClass table,UMLClass klass, UMLAssociationEnd otherEnd) throws GenerationException
	{
		return findAssociatedColumn(table,klass, otherEnd, true);
	}

	public static String findInverseColumnValue(UMLClass table,UMLClass klass, UMLAssociationEnd thisEnd) throws GenerationException
	{
		String tvVal = getFQCN(klass) +"."+ thisEnd.getRoleName();
		String tvKey = "inverse-of";
		
		UMLAttribute resultColumn = null;
		int count = 0;
		
		for(UMLAttribute column: table.getAttributes())
		{
			for(UMLTaggedValue tv: column.getTaggedValues())
			{
				if (tvKey.equals(tv.getName()))
				{
					String tvValue = tv.getValue();
					String[] tvValues = tvValue.split(",");
					for(String val:tvValues)
					{
						if(tvVal.equals(val))
						{
							count++;
							resultColumn = column;
						}
					}
				}
			}
		}
		
		if(count > 1) throw new GenerationException("More than one column found for inverse-of mapping of : "+tvVal);
		
		if(resultColumn!=null)
			return resultColumn.getName();
		else
			return "";
	}
	
	public static String findDiscriminatingColumnName(String fqcn, UMLClass table) throws GenerationException
	{
		String tvVal = fqcn;
		String tvKey = "discriminator";
		
		UMLAttribute resultColumn = null;
		int count = 0;
		
		for(UMLAttribute column: table.getAttributes())
		{
			for(UMLTaggedValue tv: column.getTaggedValues())
			{
				if (tvKey.equals(tv.getName()))
				{
					String tvValue = tv.getValue();
					String[] tvValues = tvValue.split(",");
					for(String val:tvValues)
					{
						if(tvVal.equals(val))
						{
							count++;
							resultColumn = column;
						}
					}
				}
			}
		}
		
		if(count > 1) throw new GenerationException("More than one discriminating columns found for : "+fqcn);
		
		if(resultColumn!=null)
			return resultColumn.getName();
		else
			return null;
	}

	public static String getDiscriminatorValue(UMLClass klass) throws GenerationException
	{
		String tvKey = "discriminator";
		
		String result = null;
		int count = 0;
		
		for(UMLTaggedValue tv: klass.getTaggedValues())
		{
			if (tvKey.equals(tv.getName()))
			{
				String tvValue = tv.getValue();
				String[] tvValues = tvValue.split(",");
				for(String val:tvValues)
				{
					count++;
					result = val;
				}
			}
		}
		
		if(count == 0 || result == null || result.trim().length() == 0) throw new GenerationException("No discriminating values found for : "+getFQCN(klass));
		if(count > 1) throw new GenerationException("More than one discriminating values found for : "+getFQCN(klass));
		
		return result;
	}
	
}