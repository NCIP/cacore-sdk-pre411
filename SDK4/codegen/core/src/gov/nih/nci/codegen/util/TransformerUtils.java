package gov.nih.nci.codegen.util;

import gov.nih.nci.codegen.GenerationException;
import gov.nih.nci.common.util.Constant;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAssociation;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAssociationEnd;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAttribute;
import gov.nih.nci.ncicb.xmiinout.domain.UMLClass;
import gov.nih.nci.ncicb.xmiinout.domain.UMLDatatype;
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
		UMLClass[] superClasses = ModelUtil.getSuperclasses(klass);

		if(superClasses.length == 0) return "";

		if(superClasses.length>1)
			throw new GenerationException("Class can not have more than one super classes");

		String pkgName = ModelUtil.getFullPackageName(klass);
		String superPkg = ModelUtil.getFullPackageName(superClasses[0]);

		StringBuilder sb = new StringBuilder();
		if(!pkgName.equals(superPkg))
			sb.append("import ").append(removeBasePackage(ModelUtil.getFullName(superClasses[0]))).append(";");

		//TODO - Imports for the data types e.g. List, Date etc
		return sb.toString();
	}

	public static String getDataType(UMLAttribute attr)
	{
		UMLDatatype dataType = attr.getDatatype();
		return dataType.getName();
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
		if((assocEnd.getHighMultiplicity()<1)||(assocEnd.getLowMultiplicity()<1)) 
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

	public static String getClassIdGetterMthod(UMLClass klass)
	{
		//TODO Figureout a way to get the ID attribute's getter method
		return "getId";
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
					
					System.out.println("id: " + id);
					System.out.println("assocEndsMap.get(id) == null ? " + (assocEndsMap.get(id) == null));
					System.out.println("ae.getType(): " + (UMLClass)(ae.getUMLElement()));
					System.out.println("superClass: " + superClass);
					
					if ((UMLClass)ae.getUMLElement() == superClass && assocEndsMap.get(id) == null) {
						log.debug("adding assoc: " + id);
						System.out.println("adding assoc: " + id);
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

	
}