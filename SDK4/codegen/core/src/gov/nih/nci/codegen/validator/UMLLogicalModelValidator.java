package gov.nih.nci.codegen.validator;

import gov.nih.nci.codegen.GenerationException;
import gov.nih.nci.codegen.GeneratorError;
import gov.nih.nci.codegen.GeneratorErrors;
import gov.nih.nci.codegen.Validator;
import gov.nih.nci.codegen.util.TransformerUtils;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAssociation;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAssociationEnd;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAttribute;
import gov.nih.nci.ncicb.xmiinout.domain.UMLClass;
import gov.nih.nci.ncicb.xmiinout.domain.UMLDatatype;
import gov.nih.nci.ncicb.xmiinout.domain.UMLModel;

import java.util.Collection;
import java.util.List;

/**
 * Validates the logical model (Object model) of the <code>model</code> passed in as a parameter
 * Validation rules are as follows
 * 
 * <UL>
 * <LI>Package name should not contain spaces</LI>
 * <LI>Model should not contain duplicate classes</LI>
 * <LI>Class name should be present</LI>
 * <LI>Class name should not contain empty spaces</LI>
 * <LI>Class name should start with a character</LI>
 * <LI>Association end should have a role name</LI>
 * <LI>Association end should not contain spaces</LI>
 * <LI>Association end name should start with a character</LI> 
 * <LI>Associated class should be present in the included package and not excluded</LI>
 * <LI>Association can not be made to classes in java.lang and java.util packages</LI>
 * <LI>Class should not contain duplicate associations (association ends which are not navigable are not counted)</LI>
 * <LI>Attribute name should be present</LI>
 * <LI>Attribute name should not contain empty spaces</LI>
 * <LI>Attribute name should start with a character</LI>
 * <LI>Class should not have duplicate attributes</LI>
 * <LI>Attribute type should be present</LI>
 * <LI>Attribute type should not contain empty spaces</LI>
 * <LI>Attribute type should be one of the valid values</LI>
 * <LI>Parent class of any class should be in the included package and not excluded</LI>
 * </UL>
 * 
 * @author Satish Patel
 *
 */
public class UMLLogicalModelValidator implements Validator
{

	/**
	 * See description for the this class above
	 *  
	 * @see gov.nih.nci.codegen.Validator#validate(gov.nih.nci.ncicb.xmiinout.domain.UMLModel)
	 */
	public GeneratorErrors validate(UMLModel model)
	{
		GeneratorErrors errors = new GeneratorErrors();
		Collection<UMLClass> classes = TransformerUtils.getAllClasses(model);
		for(UMLClass klass1:classes)
		{
			String class1Name = TransformerUtils.getFQCN(klass1);
			if(class1Name ==null || class1Name.trim().length()==0)
				class1Name = "";
			else
				class1Name = class1Name.trim();
			
			if(class1Name.length()==0)
				errors.addError(new GeneratorError("Class name empty "+class1Name));
			if(class1Name.indexOf(' ')>0)
				errors.addError(new GeneratorError("Class name contains empty spaces "+class1Name));
			if(class1Name.indexOf("..")>0)
				errors.addError(new GeneratorError("Class name contains empty package name "+class1Name));
			if(class1Name.indexOf("..")>0)
				errors.addError(new GeneratorError("Class name contains empty package name "+class1Name));
			if(class1Name.length() >0 && !Character.isLetter(class1Name.charAt(0)))
				errors.addError(new GeneratorError("Class name starts with non-character value "+class1Name));
			for(UMLClass klass2:classes)
			{
				String class2Name = TransformerUtils.getFQCN(klass2);
				if(klass1!=klass2 && class1Name!=null && class1Name.equals(class2Name))
					errors.addError(new GeneratorError("Duplicate class found for the "+class1Name));					
			}
			validateClass(klass1, errors);
		}
		return errors;
	}

	private void validateClass(UMLClass klass,GeneratorErrors errors) {
		validateSuperClass(klass, errors);
		validateAttributes(klass, errors);
		validateAssociations(klass, errors);
	}

	private void validateAssociations(UMLClass klass, GeneratorErrors errors) 
	{
		for(UMLAssociation association: klass.getAssociations())
		{
			try 
			{
				List <UMLAssociationEnd>ends = association.getAssociationEnds();
				UMLAssociationEnd thisEnd = TransformerUtils.getThisEnd(klass, ends);
				UMLAssociationEnd otherEnd = TransformerUtils.getOtherEnd(klass, ends);
				String thisClassName = TransformerUtils.getFQCN(((UMLClass)thisEnd.getUMLElement()));
				String otherClassName = TransformerUtils.getFQCN(((UMLClass)otherEnd.getUMLElement()));
				if(!TransformerUtils.isIncluded((UMLClass)otherEnd.getUMLElement()))
					errors.addError(new GeneratorError("Association belongs to the not included or excluded package for association between "+thisClassName +" and "+ otherClassName));
				if(thisEnd.getRoleName()!=null && thisEnd.getRoleName().length()>0 && !Character.isLetter(thisEnd.getRoleName().charAt(0)))
					errors.addError(new GeneratorError("Association role name starts with non-character value for association between "+thisClassName +" and "+ otherClassName));
				if(otherEnd.getRoleName()!=null && otherEnd.getRoleName().length()>0 && !Character.isLetter(otherEnd.getRoleName().charAt(0)))
					errors.addError(new GeneratorError("Association role name starts with non-character value for association between "+thisClassName +" and "+ otherClassName));
				
				if(thisEnd.getRoleName()==null || thisEnd.getRoleName().trim().length()==0 || otherEnd.getRoleName()==null || otherEnd.getRoleName().trim().length()==0 )
					errors.addError(new GeneratorError("Association end name not specified for association between "+thisClassName +" and "+ otherClassName));
				if((thisEnd.getRoleName()!= null && thisEnd.getRoleName().indexOf(' ') > 0 ) || (otherEnd.getRoleName()!= null && otherEnd.getRoleName().indexOf(' ') > 0 ))
					errors.addError(new GeneratorError("Association end name contains empty spaces for association between "+thisClassName +" and "+ otherClassName));
				if(thisClassName.startsWith("java.lang") || thisClassName.startsWith("java.util"))
					errors.addError(new GeneratorError("Association to the wrapper class not allowed for association between "+thisClassName +" and "+ otherClassName));
				if(otherClassName.startsWith("java.lang") || otherClassName.startsWith("java.util"))
					errors.addError(new GeneratorError("Association to the wrapper class not allowed for association between "+thisClassName +" and "+ otherClassName));

				if (otherEnd.isNavigable())
				{
					for(UMLAssociation assoc: klass.getAssociations())
					{
						List <UMLAssociationEnd>ends2 = assoc.getAssociationEnds();
						if(ends!=ends2)
						{
							UMLAssociationEnd thisEnd2 = TransformerUtils.getThisEnd(klass, ends2);
							UMLAssociationEnd otherEnd2 = TransformerUtils.getOtherEnd(klass, ends2);
							String thisClassName2 = TransformerUtils.getFQCN(((UMLClass)thisEnd2.getUMLElement()));
							String otherClassName2 = TransformerUtils.getFQCN(((UMLClass)otherEnd2.getUMLElement()));				
							if(otherEnd.getRoleName()!=null && otherEnd.getRoleName().equals(otherEnd2.getRoleName()))
								errors.addError(new GeneratorError("Duplicate association between "+thisClassName2 +" and "+ otherClassName2));
						}
					}
				}
			
			}
			catch (GenerationException e) 
			{
				errors.addError(new GeneratorError("Association validation failed", e));
			}
		}
	}

	private void validateAttributes(UMLClass klass, GeneratorErrors errors) 
	{
		String thisClassName = TransformerUtils.getFQCN(klass);
		for(UMLAttribute attribute: klass.getAttributes())
		{
			if(attribute.getName()==null || attribute.getName().trim().length()==0)
				errors.addError(new GeneratorError("Attribute name empty in "+thisClassName+": "+attribute.getName()));
			if(attribute.getName()!=null && attribute.getName().indexOf(' ')>0)
				errors.addError(new GeneratorError("Attribute name contains empty spaces in "+thisClassName+": "+attribute.getName()));

			UMLDatatype dataType = attribute.getDatatype();
			String name = dataType.getName();
			if(dataType instanceof UMLClass)
				name = TransformerUtils.getFQCN((UMLClass)dataType);
			if(name == null) name = "";
			
			if(name.trim().length() == 0)
				errors.addError(new GeneratorError("Attribute type empty in "+thisClassName+": "+attribute.getName()));
			if(name.indexOf(' ')>0)
				errors.addError(new GeneratorError("Attribute type contains empty spaces in "+thisClassName+": "+attribute.getName()));
			
			if(name.startsWith("java.lang."))
				name = name.substring("java.lang.".length());

			if(!("int".equalsIgnoreCase(name) || "integer".equalsIgnoreCase(name) ||
					"double".equalsIgnoreCase(name) ||
					"float".equalsIgnoreCase(name) ||
					"long".equalsIgnoreCase(name) ||
					"string".equalsIgnoreCase(name) ||
					"char".equalsIgnoreCase(name) || "character".equalsIgnoreCase(name) ||
					"boolean".equalsIgnoreCase(name) ||
					"date".equalsIgnoreCase(name) || "java.util.date".equalsIgnoreCase(name)))
				errors.addError(new GeneratorError("Invalid datatype for the "+attribute.getName()+" attribute in the "+thisClassName+" class"));
			
			for(UMLAttribute attr: klass.getAttributes())
				if(attr!=attribute &&  attr.getName()!=null && attr.getName().equals(attribute.getName()))
					errors.addError(new GeneratorError("Duplicate attributes found in the "+thisClassName+" class :"+attr.getName()));
		}
	}

	private void validateSuperClass(UMLClass klass, GeneratorErrors errors) {
		try {
			UMLClass superKlass = TransformerUtils.getSuperClass(klass);
			if(superKlass!=null && !TransformerUtils.isIncluded(superKlass))
				errors.addError(new GeneratorError("Parent of the class "+TransformerUtils.getFQCN(klass)+" belongs to the not included or excluded package"));

		} catch (GenerationException e) {
			errors.addError(new GeneratorError("Superclass validation failed", e));
		}
	}	
}