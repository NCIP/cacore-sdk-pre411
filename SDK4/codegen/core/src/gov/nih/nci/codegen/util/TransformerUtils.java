/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package gov.nih.nci.codegen.util;

import gov.nih.nci.codegen.GenerationException;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAssociation;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAssociationEnd;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAttribute;
import gov.nih.nci.ncicb.xmiinout.domain.UMLClass;
import gov.nih.nci.ncicb.xmiinout.domain.UMLDatatype;
import gov.nih.nci.ncicb.xmiinout.domain.UMLDependency;
import gov.nih.nci.ncicb.xmiinout.domain.UMLGeneralization;
import gov.nih.nci.ncicb.xmiinout.domain.UMLModel;
import gov.nih.nci.ncicb.xmiinout.domain.UMLPackage;
import gov.nih.nci.ncicb.xmiinout.domain.UMLTaggableElement;
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
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

public class TransformerUtils
{
	private static Logger log = Logger.getLogger(TransformerUtils.class);
	private static String BASE_PKG_LOGICAL_MODEL;
	private static String BASE_PKG_DATA_MODEL;
	private static String INCLUDE_PACKAGE;
	private static String EXCLUDE_PACKAGE;
	private static String EXCLUDE_NAME;
	private static Set<String> INCLUDE_PACKAGE_NAMES = new HashSet<String>();
	private static Set<String> EXCLUDE_PACKAGE_NAMES = new HashSet<String>();
	private static Set<String> EXCLUDE_NAMES = new HashSet<String>();
	
	private static String TV_ID_ATTR_COLUMN = "id-attribute";
	private static String TV_MAPPED_ATTR_COLUMN = "mapped-attributes";
	private static String TV_ASSOC_COLUMN = "implements-association";
	private static String TV_INVERSE_ASSOC_COLUMN = "inverse-of";
	private static String TV_DISCR_COLUMN = "discriminator";
	private static String TV_CORRELATION_TABLE = "correlation-table";
	private static String TV_DOCUMENTATION = "documentation";
	private static String TV_DESCRIPTION = "description";
	private static String TV_LAZY_LOAD = "lazy-load";
	private static String TV_TYPE="type";
	private static String TV_MAPPED_COLLECTION_TABLE = "mapped-collection-table";
	private static String TV_MAPPED_ELEMENT_COLUMN = "mapped-element";
	private static String TV_CADSR_PUBLICID = "CADSR_ConceptualDomainPublicID";
	private static String TV_CADSR_VERSION = "CADSR_ConceptualDomainVersion";
	
	private static String STEREO_TYPE_TABLE = "table";
	private static String STEREO_TYPE_DATASOURCE_DEPENDENCY = "DataSource";

	//private static String TV_TYPE = "type"; -- used to recognize CLOB
	
	static
	{
		try 
		{
			Properties prop = ((Properties)ObjectFactory.getObject("UMLModelFileProperties"));
			BASE_PKG_LOGICAL_MODEL = prop.getProperty("Logical Model") == null ? "" :prop.getProperty("Logical Model").trim();
			BASE_PKG_DATA_MODEL = prop.getProperty("Data Model")==null ? "" : prop.getProperty("Data Model").trim();
			
			EXCLUDE_PACKAGE = prop.getProperty("Exclude Package")==null ? "" : prop.getProperty("Exclude Package").trim();
			INCLUDE_PACKAGE = prop.getProperty("Include Package")==null ? "" : prop.getProperty("Include Package").trim();
			EXCLUDE_NAME = prop.getProperty("Exclude Name")==null ? "" : prop.getProperty("Exclude Name").trim();
			
			for(String excludeToken:EXCLUDE_PACKAGE.split(","))
				EXCLUDE_PACKAGE_NAMES.add(excludeToken.trim());
			for(String includeToken:INCLUDE_PACKAGE.split(","))
				INCLUDE_PACKAGE_NAMES.add(includeToken.trim());
			for(String excludeToken:EXCLUDE_NAME.split(","))
				EXCLUDE_NAMES.add(excludeToken.trim());
		}
		catch (GenerationException e) 
		{
			log.fatal(e);
		}
	}
	
	public static boolean isIncluded(UMLClass klass)
	{
		String fqcn = getFQCN(klass);
		String pkgName = getFullPackageName(klass);
		
		Set<String> FQCN_PACKAGE_NAMES = new HashSet<String>();		
		for(String packageToken:fqcn.split("\\.")){
			FQCN_PACKAGE_NAMES.add(packageToken.trim());
		}

		if(INCLUDE_PACKAGE_NAMES.contains(pkgName) && !EXCLUDE_PACKAGE_NAMES.contains(pkgName) && !EXCLUDE_NAMES.contains(fqcn))
			return true;

		boolean included = false;

		if(pkgName.length()==0)
			included = true;

		for(String includePkg: INCLUDE_PACKAGE_NAMES)
			if(includePkg!=null && includePkg.length()>0)
				included |= (FQCN_PACKAGE_NAMES.contains(includePkg));
		for(String excludePkg: EXCLUDE_PACKAGE_NAMES)
			if(excludePkg!=null && excludePkg.length()>0) {
				included &= !(FQCN_PACKAGE_NAMES.contains(excludePkg));
			}
		included &= !EXCLUDE_NAMES.contains(fqcn);
		
		return included;
	}

	public static boolean isIncluded(UMLPackage pkg)
	{
		String pkgName = getFullPackageName(pkg);
		
		Set<String> PACKAGE_NAMES = new HashSet<String>();		
		for(String packageToken:pkgName.split("\\.")){
			PACKAGE_NAMES.add(packageToken.trim());
		}
		
		boolean included = true;
		if(pkgName.length()>0)
			for(String excludePkg: EXCLUDE_PACKAGE_NAMES)
				if(excludePkg!=null && excludePkg.length()>0){
					included &= !(PACKAGE_NAMES.contains(excludePkg));
				}

		return included;
	}
	
	public static String getEmptySpace(Integer count)
	{
		String spaces = "";
		
		for(Integer i=0;i<count;i++)
			spaces +="\t";
		
		return spaces;
	}

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
		if(path.startsWith(BASE_PKG_LOGICAL_MODEL+"."))
			return path.substring(BASE_PKG_LOGICAL_MODEL.length()+1);
		else if(path.startsWith(BASE_PKG_DATA_MODEL+"."))
			return path.substring(BASE_PKG_DATA_MODEL.length()+1);
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
		UMLClass superClass = getSuperClass(klass);
		if(superClass == null) 
			return "";
		else 
			return "extends " + superClass.getName();
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
				importList.add(getFQCN(superClasses[0]));
		}
		
		for(UMLAttribute attr: klass.getAttributes())
		{
			if(getDataType(attr).startsWith("Collection") && !importList.contains("java.util.Collection"))
			{
				importList.add("java.util.Collection");
				break;
			}
		}
		for(UMLAssociation association: klass.getAssociations())
		{
			List<UMLAssociationEnd> assocEnds = association.getAssociationEnds();
			UMLAssociationEnd otherEnd = TransformerUtils.getOtherEnd(klass,assocEnds);
			
			String assocKlass = getFQCN ((UMLClass)otherEnd.getUMLElement());
			if(!pkgName.equals(getFullPackageName ((UMLClass)otherEnd.getUMLElement())) && !importList.contains(assocKlass))
				importList.add(assocKlass);
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
		if("byte".equalsIgnoreCase(name) )
			return "Byte";
		if("short".equalsIgnoreCase(name) )
			return "Short";

		if("date".equalsIgnoreCase(name) || "java.util.date".equalsIgnoreCase(name))
			return "java.util.Date";

		
		if("collection<int>".equalsIgnoreCase(name) || "collection<integer>".equalsIgnoreCase(name))
			return "Collection<Integer>";
		if("collection<double>".equalsIgnoreCase(name))
			return "Collection<Double>";
		if("collection<float>".equalsIgnoreCase(name))
			return "Collection<Float>";
		if("collection<long>".equalsIgnoreCase(name))
			return "Collection<Long>";
		if("collection<string>".equalsIgnoreCase(name))
			return "Collection<String>";
		if("collection<boolean>".equalsIgnoreCase(name))
			return "Collection<Boolean>";
		if("collection<byte>".equalsIgnoreCase(name))
			return "Collection<Byte>";
		if("collection<short>".equalsIgnoreCase(name))
			return "Collection<Short>";
		if("collection<char>".equalsIgnoreCase(name) || "collection<character>".equalsIgnoreCase(name))
			return "Collection<Character>";
		
		log.error("Unknown data type = "+name);
		
		return name;
	}


	public static String getHibernateDataType(UMLClass klass, UMLAttribute attr) throws GenerationException
	{
		String fqcn = getFQCN(klass);
		UMLClass table = getTable(klass);
		UMLAttribute col = getMappedColumn(table,fqcn+"."+attr.getName());
		
		Boolean isClob = "CLOB".equalsIgnoreCase(getTagValue(col.getTaggedValues(),TV_TYPE, 1));
		
		UMLDatatype dataType = attr.getDatatype();
		String name = dataType.getName();
		if(dataType instanceof UMLClass)
			name = getFQCN((UMLClass)dataType);
		
		if(name.startsWith("java.lang."))
			name = name.substring("java.lang.".length());

		if(isClob && "string".equalsIgnoreCase(name))
			return "gov.nih.nci.system.util.StringClobType";
		if(isClob && !"string".equalsIgnoreCase(name))
			throw new GenerationException("Can not map CLOB to anything other than String");
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
		if("byte".equalsIgnoreCase(name) )
			return "byte";
		if("short".equalsIgnoreCase(name) )
			return "short";

		if("date".equalsIgnoreCase(name) || "java.util.date".equalsIgnoreCase(name))
			return "java.util.Date";

		log.info("Type = "+name);
		
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
		if((assocEnd.getHighMultiplicity()<0)||(assocEnd.getLowMultiplicity()<0)) 
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
		String idAttrName = getClassIdAttrName(klass);
		if (idAttrName == null) return null;
		return "get"+firstCharUpper(getClassIdAttrName(klass));
	}

	private static String firstCharUpper(String data)
	{
		if(data == null || data.length() == 0) return data;
		return Character.toUpperCase(data.charAt(0)) + data.substring(1); 
	}
	
	public static String getClassIdAttrName(UMLClass klass) throws GenerationException
	{
		UMLAttribute idAttr = getClassIdAttr(klass);
		if (idAttr == null) return null;
		
		return getClassIdAttr(klass).getName();
	}

	public static UMLAttribute getClassIdAttr(UMLClass klass) throws GenerationException
	{
		String fqcn = getFQCN(klass);

		UMLAttribute idAttr = getColumn(klass,TV_ID_ATTR_COLUMN, fqcn,true,0,1);
		
		if(idAttr !=null) return idAttr;
		
		String idAttrName = "id";
		for(UMLAttribute attribute:klass.getAttributes())
			if(idAttrName.equals(attribute.getName()))
				return attribute;

		for(UMLGeneralization gen: klass.getGeneralizations())
		{
			if(gen.getSubtype() == klass && gen.getSupertype() != klass)
			{
				UMLAttribute superId = getClassIdAttr(gen.getSupertype());
				if(superId != null)
					return superId;
			}
		}
		return null;
		//throw new GenerationException("No attribute found that maps to the primary key identifier for class : "+fqcn);
	}
	
	public static Boolean isCollection(UMLClass klass, UMLAttribute attr ) throws GenerationException
	{
		if(getDataType(attr).startsWith("Collection")) 
			return true;
		return false;
	}
	
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
	public static boolean isMany2One(UMLAssociationEnd thisEnd, UMLAssociationEnd otherEnd) {
		if((thisEnd.getRoleName().equals("buttonCollection")) || otherEnd.getRoleName().equals("buttonCollection"))
				System.out.println("");
		return isAssociationEndMany(thisEnd) && !isAssociationEndMany(otherEnd);
	}	
	/**
	 * @param thisEnd
	 * @param otherEnd
	 * @return
	 */
	public static boolean isOne2Many(UMLAssociationEnd thisEnd,UMLAssociationEnd otherEnd) {
		if((thisEnd.getRoleName().equals("buttonCollection")) || otherEnd.getRoleName().equals("buttonCollection"))
			System.out.println("");
		return !isAssociationEndMany(thisEnd) && isAssociationEndMany(otherEnd);
	}
	
	/**
	 * @param thisEnd
	 * @param otherEnd
	 * @return
	 */
	public static boolean isMany2Many(UMLAssociationEnd thisEnd,UMLAssociationEnd otherEnd) {
		return isAssociationEndMany(thisEnd) && isAssociationEndMany(otherEnd);
	}	
	
	public static Collection getAssociationEnds(UMLClass klass) {
		return getAssociationEnds(klass, false);
	}

	public static Collection getAssociationEnds(UMLClass klass,
			boolean includeInherited) {
		log.debug("class = " + klass.getName() + ", includeInherited = "
				+ includeInherited);
		Map<String, UMLAssociationEnd> assocEndsMap = new HashMap<String, UMLAssociationEnd>();
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

			if (isIncluded(pkg)){
				log.debug("including package: " + pkg.getName());
			
				Collection<UMLClass> pkgClasses = pkg.getClasses();

				if (pkgClasses != null && pkgClasses.size() > 0){
					for (UMLClass klass:pkgClasses){
						String pkgName = TransformerUtils.getFullPackageName(klass);
						if(!STEREO_TYPE_TABLE.equalsIgnoreCase(klass.getStereotype()) && isIncluded(klass)) { 
							classColl.add(klass);

							if(!pkgColl.containsKey(pkg)) {
								List<UMLClass> classes = new ArrayList<UMLClass>();
								classes.add(klass);
								pkgColl.put(pkg, classes);
							} else {
								Collection<UMLClass> existingCollection = pkgColl.get(pkg);
								existingCollection.add(klass);
							}					
						}
					}
				}
			} else{
				log.debug("excluding package: " + pkg.getName());
			}
			collectPackages(pkg.getPackages(), pkgColl, classColl);	
		}
	}	
	
	/**
	 * Returns all the classes (not the tables) in the XMI file which do not belong to java.lang or java.util package 
	 * @param model
	 * @return
	 */
	public static Collection<UMLClass> getAllClasses(UMLModel model) throws GenerationException
	{
		Collection<UMLClass> classes = null;
		try {
			classes = new HashSet<UMLClass>();
			getAllClasses(model.getPackages(),classes);
		} catch(Exception e){
			log.error("Unable to retrieve classes from model: ", e);
			throw new GenerationException("Unable to retrieve classes from model: ", e);
		}
		return classes;
	}
	
	private static void getAllClasses(Collection<UMLPackage> pkgCollection,Collection<UMLClass> classes)
	{
		for(UMLPackage pkg:pkgCollection)
			getAllClasses(pkg,classes);
	}
	
	private static void getAllClasses(UMLPackage rootPkg,Collection<UMLClass> classes)
	{
		if(isIncluded(rootPkg))
		{
			for(UMLClass klass:rootPkg.getClasses())
			{
				if(!STEREO_TYPE_TABLE.equalsIgnoreCase(klass.getStereotype()) && isIncluded(klass))
					classes.add(klass);
			}
		}
		getAllClasses(rootPkg.getPackages(),classes);
	}	
	

	
	/**
	 * Returns all the classes (not the tables) in the XMI file which do not belong to java.lang or java.util package.
	 * The class also have to be the root class in the inheritnace hierarchy to be included in the final list 
	 * @param model
	 * @return
	 */
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
		if(isIncluded(rootPkg))
		{
			for(UMLClass klass:rootPkg.getClasses())
			{
				if(!STEREO_TYPE_TABLE.equalsIgnoreCase(klass.getStereotype()) && isIncluded(klass) && ModelUtil.getSuperclasses(klass).length == 0)
					classes.add(klass);
			}
		}
		getAllParentClasses(rootPkg.getPackages(),classes);
	}

	/**
	 * Retrieves the table corresponding to the Dependency link between class and a table. 
	 * If there are no Dependencies that links the class to table or there are more than
	 * one Dependencies then in that case the method throws an exception
	 *  
	 * @param klass
	 * @return
	 * @throws GenerationException
	 */
	public static UMLClass getTable(UMLClass klass) throws GenerationException
	{
		Set<UMLDependency> dependencies = klass.getDependencies();
		int count = 0;
		UMLClass result = null; 
		for(UMLDependency dependency:dependencies)
		{
			UMLClass client = (UMLClass) dependency.getClient();
			if(STEREO_TYPE_TABLE.equalsIgnoreCase(client.getStereotype()) && STEREO_TYPE_DATASOURCE_DEPENDENCY.equalsIgnoreCase(dependency.getStereotype()))
			{
				count++;
				result = client;
			}
		}

		if(count!=1)
			throw new GenerationException("No table found for : "+getFQCN(klass));
		
		return result;
	}
	
	/**
	 * Scans the tag values of the association to determine which JOIN table the association is using. 
	 * 
	 * @param association
	 * @param model
	 * @param klass
	 * @return
	 * @throws GenerationException
	 */
	public static UMLClass findCorrelationTable(UMLAssociation association, UMLModel model, UMLClass klass) throws GenerationException
	{
		return findCorrelationTable(association, model, klass, true);
	}

	public static UMLClass findCorrelationTable(UMLAssociation association, UMLModel model, UMLClass klass, boolean throwException) throws GenerationException
	{
		int minReq = throwException ? 1:0;
		String tableName = getTagValue(klass, association,TV_CORRELATION_TABLE, null,minReq,1);

		if(!throwException && (tableName == null || tableName.length() ==0)) return null;
		
		UMLClass correlationTable = ModelUtil.findClass(model,BASE_PKG_DATA_MODEL+"."+tableName);
		if(correlationTable == null) throw new GenerationException("No correlation table found named : \""+tableName+"\"");
		
		return correlationTable;
	}

	public static String getMappedColumnName(UMLClass table, String fullyQualifiedAttrName) throws GenerationException
	{
		return getColumnName(table,TV_MAPPED_ATTR_COLUMN,fullyQualifiedAttrName,false,1,1);
	}
	
	public static UMLAttribute getMappedColumn(UMLClass table, String fullyQualifiedAttrName) throws GenerationException
	{
		return getColumn(table,TV_MAPPED_ATTR_COLUMN,fullyQualifiedAttrName,false,1,1);
	}

	/**
	 * @param tgElt The TaggableElement (UMLClass, UMLAttribute)
	 * @return		String containing a concatenation of any, all caDSR 
	 * 				tag values
	 */
	public static String getCaDSRAnnotationContent(UMLTaggableElement tgElt)
	{
		String publicID = getTagValue(tgElt, TV_CADSR_PUBLICID);
		String version = getTagValue(tgElt, TV_CADSR_VERSION);		

		if (publicID == null && version == null) {
			return null;
		}

		StringBuffer sb = new StringBuffer();
		
		if (publicID != null)
			sb.append(TV_CADSR_PUBLICID).append("=\"").append(publicID).append("\"; ");
		
		if (version != null)
			sb.append(TV_CADSR_VERSION).append("=\"").append(version).append("\"");
	
		return sb.toString();

	}	
	
	public static String findAssociatedColumn(UMLClass table,UMLClass klass, UMLAssociationEnd otherEnd, UMLClass assocKlass, UMLAssociationEnd thisEnd, Boolean throwException, Boolean isJoin) throws GenerationException
	{
		String col1 = getColumnName(table,TV_ASSOC_COLUMN,getFQCN(klass) +"."+ otherEnd.getRoleName(),false,0,1);
		String col2 = getColumnName(table,TV_ASSOC_COLUMN,getFQCN(assocKlass) +"."+ thisEnd.getRoleName(),false,0,1);
		String col3 = getColumnName(table,TV_INVERSE_ASSOC_COLUMN,getFQCN(assocKlass) +"."+ thisEnd.getRoleName(),false,0,1);
		
		if("".equals(col1)) col1=null;
		if("".equals(col2)) col2=null;
		if("".equals(col3)) col3=null;
		
		if((col1==null && col3==null && isJoin && throwException) || (col1==null && col2==null && !isJoin && throwException))
			throw new GenerationException("Could not determine the column for the association between "+getFQCN(klass)+" and "+getFQCN(assocKlass));
		/*if(col1!=null && col2!=null && !col1.equals(col2))
			throw new GenerationException("More than one column found for the association between "+getFQCN(klass)+" and "+getFQCN(assocKlass));
		if(col1!=null && col3!=null && !col1.equals(col3))
			throw new GenerationException("More than one column found for the association between "+getFQCN(klass)+" and "+getFQCN(assocKlass));
		if(col2!=null && col3!=null && !col2.equals(col3))
			throw new GenerationException("More than one column found for the association between "+getFQCN(klass)+" and "+getFQCN(assocKlass));
		*/
		if(isJoin)
		{
			return col1==null ? col3 : col1;
		}
		else
		{
			return col1==null ? col2 : col1;
			
		}
/*		if(col1!=null) return col1;
		else if (col3!=null) return col3;
		else return col2;
*/	}

	public static String findAssociatedColumn(UMLClass table,UMLClass klass, UMLAssociationEnd otherEnd, UMLClass assocKlass, UMLAssociationEnd thisEnd, Boolean isJoin) throws GenerationException
	{
		return findAssociatedColumn(table,klass, otherEnd, assocKlass, thisEnd, true, isJoin);
	}

	public static String findInverseColumnValue(UMLClass table,UMLClass klass, UMLAssociationEnd thisEnd) throws GenerationException
	{
		return getColumnName(table,TV_INVERSE_ASSOC_COLUMN,getFQCN(klass) +"."+ thisEnd.getRoleName(),false,0,1);
	}
	
	public static String findDiscriminatingColumnName(UMLClass klass) throws GenerationException
	{
		UMLClass superKlass = klass;
		UMLClass temp = klass;
		while ((temp = getSuperClass(temp))!=null)
			superKlass = temp;
		UMLClass table = getTable(superKlass);
		String fqcn = getFQCN(superKlass);
		return getColumnName(table,TV_DISCR_COLUMN,fqcn,false,0,1);
	}

	public static String getDiscriminatorValue(UMLClass klass) throws GenerationException
	{
		return getTagValue(klass,TV_DISCR_COLUMN,null, 1,1);
	}

	public static boolean isLazyLoad(UMLClass klass, UMLAssociation association) throws GenerationException
	{
		String temp = getTagValue(klass,association, TV_LAZY_LOAD,null, 0,1);
		temp = (temp == null || temp.trim().length()==0) ? "yes" : temp;
		return "yes".equalsIgnoreCase(temp)? true : false;
	}
	
	private static String getTagValue(UMLClass klass, String key, String value, int minOccurence, int maxOccurence) throws GenerationException
	{
		String result = null;
		int count = 0;
		for(UMLTaggedValue tv: klass.getTaggedValues())
		{
			if (key.equals(tv.getName()))
			{
				String tvValue = tv.getValue();
				String[] tvValues = tvValue.split(",");
				for(String val:tvValues)
				{
					if(value==null)
					{
						count++;
						result = val;
					}
					else if(value.equals(val))
					{
						count++;
						result = val;
					}
				}
			}
		}
		
		if(count < minOccurence || (minOccurence>0 && (result == null || result.trim().length() == 0))) throw new GenerationException("No value found for "+key+" tag in class : "+getFQCN(klass));
		if(count > maxOccurence) throw new GenerationException("More than one value found for "+key+" tag in class : "+getFQCN(klass));
		
		return result;
	}
	
	private static String getTagValue(UMLTaggableElement tgElt, String key) 
	{

		for(UMLTaggedValue tv: tgElt.getTaggedValues())
		{
			if (key.equals(tv.getName()))
			{
				return tv.getValue();
			}
		}
		
		return null;
	}	
	

	private static String getColumnName(UMLClass klass, String key, String value,  boolean isValuePrefix, int minOccurence, int maxOccurence) throws GenerationException
	{
		UMLAttribute attr = getColumn(klass,key,value,isValuePrefix,minOccurence,maxOccurence);
		return (attr==null) ? "" : attr.getName();
	}

	private static UMLAttribute getColumn(UMLClass klass, String key, String value, boolean isValuePrefix, int minOccurence, int maxOccurence) throws GenerationException
	{
		UMLAttribute result = null;
		int count = 0;
		for(UMLAttribute attr: klass.getAttributes())
		{
			for(UMLTaggedValue tv: attr.getTaggedValues())
			{
				if (key.equals(tv.getName()))
				{
					String tvValue = tv.getValue();
					String[] tvValues = tvValue.split(",");
					for(String val:tvValues)
					{
						if(value==null)
						{
							count++;
							result = attr;
						}
						else if(isValuePrefix && val.startsWith(value))
						{
							count++;
							result = attr;
						}
						else if(!isValuePrefix && val.equals(value))
						{
							count++;
							result = attr;
						}
					}
				}
			}
		}
		if(count < minOccurence) throw new GenerationException("No value of "+value+" found for "+key+" tag in class : "+getFQCN(klass));
		if(count > maxOccurence) throw new GenerationException("More than one values found for "+key+" tag in class : "+getFQCN(klass));
		
		return result;
	}
	
	
	private static String getTagValue(UMLClass klass, UMLAttribute attribute, String key, String value, Boolean isValuePrefix, int minOccurence, int maxOccurence) throws GenerationException
	{
		String result = null;
		int count = 0;
		for(UMLTaggedValue tv: attribute.getTaggedValues())
		{
			if (key.equals(tv.getName()))
			{
				String tvValue = tv.getValue();
				String[] tvValues = tvValue.split(",");
				for(String val:tvValues)
				{
					if(value==null)
					{
						count++;
						result = val;
					}
					else if(isValuePrefix && val.startsWith(value))
					{
						count++;
						result = val;
					}
					else if(!isValuePrefix && val.equals(value))
					{
						count++;
						result = val;
					}
				}
			}
		}
		
		if(count < minOccurence) throw new GenerationException("No value of "+value+" found for "+key+" tag in class : "+getFQCN(klass));
		if(count > maxOccurence) throw new GenerationException("More than one values found for "+key+" tag in class : "+getFQCN(klass));
		
		return result;
	}	
		
	private static String getTagValue(UMLClass klass, UMLAssociation association, String key, String value, Boolean isValuePrefix, int minOccurence, int maxOccurence) throws GenerationException
	{
		
		List <UMLAssociationEnd>ends = association.getAssociationEnds();
		UMLAssociationEnd thisEnd = TransformerUtils.getThisEnd(klass, ends);
		UMLAssociationEnd otherEnd = TransformerUtils.getOtherEnd(klass, ends);
		String thisClassName = TransformerUtils.getFQCN(((UMLClass)thisEnd.getUMLElement()));
		String otherClassName = TransformerUtils.getFQCN(((UMLClass)otherEnd.getUMLElement()));
			
		
		String result = null;
		int count = 0;
		for(UMLTaggedValue tv: association.getTaggedValues())
		{
			if (key.equals(tv.getName()))
			{
				String tvValue = tv.getValue();
				String[] tvValues = tvValue.split(",");
				for(String val:tvValues)
				{
					if(value==null)
					{
						count++;
						result = val;
					}
					else if(isValuePrefix && val.startsWith(value))
					{
						count++;
						result = val;
					}
					else if(!isValuePrefix && val.equals(value))
					{
						count++;
						result = val;
				}
			}
		}
		}
		
		
		if(count < minOccurence || (minOccurence >0 && (result == null || result.trim().length() == 0))) throw new GenerationException("No tag value of "+key+" found for association between "+thisClassName +" and "+ otherClassName +":"+count+":"+result);
		
		if(count > maxOccurence) throw new GenerationException("More than one tag values of "+key+" found for association between "+thisClassName +" and "+ otherClassName);
		
		return result;
	}	
	
	private static String getTagValue(UMLClass klass, UMLAssociation association, String key, String value, int minOccurence, int maxOccurence) throws GenerationException
	{
		return getTagValue(klass, association, key, value,false, minOccurence, maxOccurence);
	}	
	
	private static String getTagValue(Collection<UMLTaggedValue> tagValues, String key, int maxOccurence) throws GenerationException
	{
		StringBuilder temp = new StringBuilder();
		
		for(int i=0;i<maxOccurence;i++)
		{
			String searchKey = i==0 ? key : key + (i+1);
			for(UMLTaggedValue tv:tagValues)
			{
				if(searchKey.equals(tv.getName()))
				{
					temp.append(tv.getValue());
				}
			}
			
		}
		return temp.toString();
	}
	
	private static String getJavaDocs(Collection<UMLTaggedValue> tagValues) throws GenerationException
	{
		String documentation = getTagValue(tagValues, TV_DOCUMENTATION, 8);
		String description = getTagValue(tagValues, TV_DESCRIPTION, 8);		
		
		String temp = documentation == null || documentation.trim().length()==0 ? description : documentation; 
		StringBuilder doc = new StringBuilder();
		doc.append("	/**");
		doc.append("\n	* ").append(temp);
		doc.append("	**/");
		return doc.toString();

	}	
	
	public static String getJavaDocs(UMLClass klass) throws GenerationException 
	{
		return getJavaDocs(klass.getTaggedValues());
	}

	public static String getJavaDocs(UMLAttribute attr) throws GenerationException 
	{
		return getJavaDocs(attr.getTaggedValues());
	}

	public static String getJavaDocs(UMLClass klass, UMLAssociation assoc) throws GenerationException 
	{
		UMLAssociationEnd otherEnd = getOtherEnd(klass, assoc.getAssociationEnds());
		StringBuilder doc = new StringBuilder();
		doc.append("/**");
		doc.append("\n	* An associated "+getFQCN(((UMLClass)otherEnd.getUMLElement()))+" object");
		if(isAssociationEndMany(otherEnd))
			doc.append("'s collection ");
		doc.append("\n	**/\n");
		return doc.toString();
	}

	public static String getGetterMethodJavaDocs(UMLAttribute attr) {
		StringBuilder doc = new StringBuilder();
		doc.append("/**");
		doc.append("\n	* Retreives the value of "+attr.getName()+" attribue");
		doc.append("\n	* @return ").append(attr.getName());
		doc.append("\n	**/\n");
		return doc.toString();
	}

	public static String getSetterMethodJavaDocs(UMLAttribute attr) {
		StringBuilder doc = new StringBuilder();
		doc.append("/**");
		doc.append("\n	* Sets the value of "+attr.getName()+" attribue");
		doc.append("\n	**/\n");
		return doc.toString();
	}

	public static String getGetterMethodJavaDocs(UMLClass klass, UMLAssociation assoc) throws GenerationException {
		UMLAssociationEnd otherEnd = getOtherEnd(klass, assoc.getAssociationEnds());
		StringBuilder doc = new StringBuilder();
		doc.append("/**");
		doc.append("\n	* Retreives the value of "+otherEnd.getRoleName()+" attribue");
		doc.append("\n	* @return ").append(otherEnd.getRoleName());
		doc.append("\n	**/\n");
		return doc.toString();
	}

	public static String getSetterMethodJavaDocs(UMLClass klass, UMLAssociation assoc) throws GenerationException {
		UMLAssociationEnd otherEnd = getOtherEnd(klass, assoc.getAssociationEnds());
		StringBuilder doc = new StringBuilder();
		doc.append("/**");
		doc.append("\n	* Sets the value of "+otherEnd.getRoleName()+" attribue");
		doc.append("\n	**/\n");
		return doc.toString();
	}	
	
	public static String reversePackageName(String s) {
		StringTokenizer st = new StringTokenizer(s,".");
		Vector<String> myVector = new Vector<String>();
		StringBuffer myStringBuffer = new StringBuffer();
		while (st.hasMoreTokens()) {
			     String t = st.nextToken();
			     myVector.add(t);

	    }

        for (int i = myVector.size(); i>0; i--) {
			  myStringBuffer.append(myVector.elementAt(i-1));
			  myStringBuffer.append(Constant.DOT);

	    }
	    int length1 = myStringBuffer.length();
	    String finalString1 = myStringBuffer.substring(0,length1-1);
        return finalString1;
    }
	
	
	
	public static String getWSDDServiceValue(Collection<UMLClass> classColl){
        StringBuffer nn1 = new StringBuffer();
        for(UMLClass klass:classColl){
			String pkgName = TransformerUtils.getFullPackageName(klass);
			nn1.append(pkgName)
			   .append(Constant.DOT)
			   .append(klass.getName())
			   .append(Constant.COMMA);
		}
        
        // remove last Comma
        return nn1.substring(0, nn1.length()-1);

	}
	

	public static UMLClass findCollectionTable(UMLAttribute attr, UMLModel model) throws GenerationException
	{
		String tableName = getTagValue(attr.getTaggedValues(),TV_MAPPED_COLLECTION_TABLE, 1);

		UMLClass collectionTable = ModelUtil.findClass(model,BASE_PKG_DATA_MODEL+"."+tableName);
		if(collectionTable == null) throw new GenerationException("No collection table found named : \""+tableName+"\"");
		
		return collectionTable;
}	
	
	
	public static String getCollectionKeyColumnName(UMLClass table,UMLClass klass, UMLAttribute attr) throws GenerationException
	{
		return getColumnName(table,TV_MAPPED_ATTR_COLUMN,getFQCN(klass) +"."+ attr.getName(),false,1,1);
	}

	public static String getCollectionElementColumnName(UMLClass table,UMLClass klass, UMLAttribute attr) throws GenerationException
	{
		return getColumnName(table,TV_MAPPED_ELEMENT_COLUMN,getFQCN(klass) +"."+ attr.getName(),false,1,1);
	}

	public static String getCollectionElementHibernateType(UMLClass klass, UMLAttribute attr) throws GenerationException
	{
		String name = getDataType(attr);
		if(name.startsWith("Collection<"))
		{
			name = name.substring("Collection<".length());
			name = name.substring(0,name.length()-1);
			
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
			if("byte".equalsIgnoreCase(name) )
				return "byte";
			if("short".equalsIgnoreCase(name) )
				return "short";	
		}
		return name;
	}

}