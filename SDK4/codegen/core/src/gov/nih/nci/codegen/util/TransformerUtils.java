package gov.nih.nci.codegen.util;

import java.util.List;

import gov.nih.nci.codegen.GenerationException;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAssociationEnd;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAttribute;
import gov.nih.nci.ncicb.xmiinout.domain.UMLClass;
import gov.nih.nci.ncicb.xmiinout.domain.UMLDatatype;
import gov.nih.nci.ncicb.xmiinout.util.ModelUtil;

public class TransformerUtils
{
	public static String getFQCN(UMLClass klass)
	{
		return removeBasePackage(ModelUtil.getFullName(klass));
	}

	public static String getFullPackageName(UMLClass klass)
	{
		return removeBasePackage(ModelUtil.getFullPackageName(klass));
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
		//System.out.println("Multiplicity = "+assocEnd.getLowMultiplicity()+","+assocEnd.getHighMultiplicity());
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
}