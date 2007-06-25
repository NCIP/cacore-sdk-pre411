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
import gov.nih.nci.ncicb.xmiinout.domain.UMLGeneralization;
import gov.nih.nci.ncicb.xmiinout.domain.UMLModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Validates the mapping between logical model (Object model) and data model of the <code>model</code> passed in as a parameter
 * Validation rules are as follows
 * 
 * <UL>
 * <LI>Only one table should be present for each of the root level classes</LI>
 * <LI>There should not be a duplicate entry between the table and a class</LI>
 * <LI>Every class should have an attribute which is mapped to the primary key column in the database</LI>
 * <LI>All the attributes in the class should be mapped to corresponding columns in the table</LI>
 * <LI>There should not be any duplicate tag values in the tables corresponding to attribute of association mapping</LI>
 * </UL>
 * 
 * <BR>Association validation rules are as follows
 * <P> When it is many to many relationship between classes and other end of the association is navigable
 * 	<UL>
 * 	<LI>Join table should be present</LI>
 * 	<LI>Key column to fetch the associated objects should be present in the join table</LI>
 * 	<LI>Key column to fetch itself from the associated class should be present in the join table</LI>
 * 	<LI>It should be clearly indicated that one of the two columns in the joint table is inverse of the second column</LI>
 * 	</UL>
 * </P>
 * <P> When it is one to many relationship between classes and other end of the association is navigable
 * 	<UL>
 * 	<LI>A table should be present corresponding to the associated class</LI>
 * 	<LI>Key column to fetch the associated objects should be present in the table for the associated class</LI>
 * 	</UL>
 * </P>
 * <P> When it is one to many relationship between classes with a join table and other end of the association is navigable
 * 	<UL>
 * 	<LI>Join table should be present</LI>
 * 	<LI>Key column to fetch the associated objects should be present in the join table</LI>
 * 	<LI>Key column to fetch itself from the associated class should be present in the join table</LI>
 * 	<LI>It should be clearly indicated that one of the two columns in the joint table is inverse of the second column</LI>
 * 	</UL>
 * </P>
 * <P> When it is many to one relationship between classes and other end of the association is navigable
 * 	<UL>
 * 	<LI>A table should be present corresponding to the associated class</LI>
 * 	<LI>Key column to fetch the associated objects should be present in the associated table</LI>
 * 	</UL>
 * </P>
 * <P> When it is many to one relationship between classes with a join table and other end of the association is navigable
 * 	<UL>
 * 	<LI>Join table should be present</LI>
 * 	<LI>Key column to fetch the associated objects should be present in the join table</LI>
 * 	<LI>Key column to fetch itself from the associated class should be present in the join table</LI>
 * 	<LI>It should be clearly indicated that one of the two columns in the joint table is inverse of the second column</LI>
 * 	</UL>
 * </P>
 * <P> When it is one to one relationship between classes and other end of the association is navigable
 * 	<UL>
 * 	<LI>A table should be present corresponding to the associated class</LI>
 * 	<LI>Key column to fetch the associated objects should be present in the associated table if this end of the association is not navigable</LI>
 * 	</UL>
 * </P>
 * <P> When it is one to one relationship between classes with a join table and other end of the association is navigable
 * 	<UL>
 * 	<LI>Join table should be present</LI>
 * 	<LI>Key column to fetch the associated objects should be present in the join table</LI>
 * 	<LI>Key column to fetch itself from the associated class should be present in the join table</LI>
 * 	<LI>It should be clearly indicated that one of the two columns in the joint table is inverse of the second column</LI>
 * 	</UL>
 * </P>
 * <BR>Subclass validation rules are as follows
 * <UL>
 * <LI>Every subclass should have a mapping to a table.</LI>
 * <LI>If all of the immidiate subclasses are to be persisted in the same table as the parent class then 
 * 	<UL>
 *		<LI>Each subclass should be marked with a distinct discriminating value</LI>
 *		<LI>Table corresponding to the root class should have a column indicating the discriminating values for the subclass</LI>
 *		<LI>The leaf level subclass can be persisted in a seperate table. However, the requirement to specify the discriminating value still holds</LI>
 * 	</UL>
 * </LI>
 * <LI>If all of the immidiate subclasses are to be persisted in the different table then the parent class then 
 * 	<UL>
 *		<LI>Table for each subclass should have a column that maps to the id attribute (ID attribute is present in the parent class)</LI>
 *		<LI>Table for the subclass should be different then the table for the parent class</LI>
 * 	</UL>
 * </LI>
 * </UL>
 * @author Satish Patel
 *
 */public class UMLModelMappingValidator implements Validator
{

	public GeneratorErrors validate(UMLModel model)
	{
		GeneratorErrors errors = new GeneratorErrors();
		Collection<UMLClass> classes = TransformerUtils.getAllParentClasses(model);
		for(UMLClass klass:classes)
			validateClass(model, klass, errors);
		return errors;
	}

	private void validateClass(UMLModel model, UMLClass klass, GeneratorErrors errors) {
		String fqcn = TransformerUtils.getFQCN(klass);
		UMLClass table = null;
		try 
		{
			table = TransformerUtils.getTable(klass);
		} catch (GenerationException e) {
			errors.addError(new GeneratorError("Table search failed for "+fqcn+" ", e));
		}
		if(table != null)
			validateClass(model, klass, table, errors);
	}

	private void validateClass(UMLModel model, UMLClass klass, UMLClass table, GeneratorErrors errors) {
		validateIdAttributeMapping(klass, table, errors);
		validateAttributesMapping(klass, table, errors);
		validateAssociations(model, klass, table, errors);
		validateSubClass(model, klass, table, errors);
	}

	private void validateSubClass(UMLModel model, UMLClass klass, UMLClass table, GeneratorErrors errors) {
		if(!hasSubClass(klass)) return;
		String fqcn = TransformerUtils.getFQCN(klass);
		try 
		{
			UMLAttribute idAttr = TransformerUtils.getClassIdAttr(klass);
			String discriminatorColumnName = TransformerUtils.findDiscriminatingColumnName(klass);		
			if(discriminatorColumnName == null || "".equals(discriminatorColumnName))
			{
				for(UMLGeneralization gen:klass.getGeneralizations())
				{
					UMLClass subKlass = (UMLClass)gen.getSubtype();
					if(subKlass!=klass){
						UMLClass subTable = TransformerUtils.getTable(subKlass);
						String subFqcn = TransformerUtils.getFQCN(subKlass);
						String keyColumnName = TransformerUtils.getMappedColumnName(subTable,subFqcn+"."+idAttr.getName());
						if(subTable == table)
							errors.addError(new GeneratorError("When the discriminating column is absent, the subclass and the parent class can not be presisted in the same table : "+subFqcn));
						validateClass(model,subKlass, errors);
					}
				}
			}
			else
			{
				Map<String, String> discriminatorValues = new HashMap<String, String>();
				
				for(UMLGeneralization gen:klass.getGeneralizations())
				{
					UMLClass subKlass = (UMLClass)gen.getSubtype();
					if(subKlass!=klass)
					{
						String subFqcn = TransformerUtils.getFQCN(subKlass);
						String discriminatorValue = TransformerUtils.getDiscriminatorValue(subKlass);
						if(discriminatorValue == null || discriminatorValue.trim().length() ==0)
							errors.addError(new GeneratorError("Discriminator value not present for the "+subFqcn+" class"));
						if(discriminatorValues.get(discriminatorValue)!= null)
							errors.addError(new GeneratorError("Same discriminator value for "+subFqcn+" and "+discriminatorValues.get(discriminatorValue)+ " class"));
						if(TransformerUtils.getTable(subKlass) != table)
						{
							for(UMLGeneralization subgen:klass.getGeneralizations())
							{
								UMLClass subSubKlass = (UMLClass)subgen.getSubtype();
								if(subKlass!=subSubKlass && subSubKlass!=TransformerUtils.getSuperClass(subKlass))
								{
									errors.addError(new GeneratorError("When the discriminating column is present, the subclass and the parent class should be presisted in the same table unless subclass do not have any subclasses : "+subFqcn));
								}
							}
							
						}
						discriminatorValues.put(discriminatorValue, subFqcn);
						validateClass(model,subKlass, errors);
					}
				}
			}
		} 
		catch (GenerationException e) 
		{
			errors.addError(new GeneratorError("Subclass validation failed for "+fqcn+" class", e));
		}		
	}

	private Boolean hasSubClass(UMLClass klass)
	{
		if(klass.getGeneralizations().size()==0)
			return false;
		for(UMLGeneralization gen:klass.getGeneralizations())
		{
			UMLClass subKlass = (UMLClass)gen.getSubtype();
			if(subKlass!=klass) return true;
		}
		return false;
	}
	
	private void validateIdAttributeMapping(UMLClass klass, UMLClass table, GeneratorErrors errors) {
		String thisClassName = TransformerUtils.getFQCN(klass);
		
		try {
			TransformerUtils.getClassIdAttr(klass);
		} catch (GenerationException e) {
			errors.addError(new GeneratorError("ID Attribute mapping validation failed for "+thisClassName+" ", e));
		}
	}

	private void validateAssociations(UMLModel model, UMLClass klass, UMLClass table, GeneratorErrors errors) 
	{
		for(UMLAssociation association: klass.getAssociations())
		{
			try 
			{
				List <UMLAssociationEnd>ends = association.getAssociationEnds();
				UMLAssociationEnd thisEnd = TransformerUtils.getThisEnd(klass, ends);
				UMLAssociationEnd otherEnd = TransformerUtils.getOtherEnd(klass, ends);

				if(otherEnd.isNavigable())
				{
					UMLClass assocKlass = (UMLClass)otherEnd.getUMLElement();
					if(TransformerUtils.isMany2Many(thisEnd,otherEnd))
					{
						UMLClass correlationTable = TransformerUtils.findCorrelationTable(association, model, assocKlass);
						String keyColumnName = TransformerUtils.findAssociatedColumn(correlationTable,assocKlass,thisEnd);
						String assocColumnName = TransformerUtils.findAssociatedColumn(correlationTable,klass,otherEnd);
						String inverseColumnName =  TransformerUtils.findInverseColumnValue(correlationTable,assocKlass,thisEnd);
						if(!"".equals(inverseColumnName) && !assocColumnName.equals(inverseColumnName))
							errors.addError(new GeneratorError("Different columns used for implements-association and inverse-of of the same association"));
					}else if(TransformerUtils.isOne2Many(thisEnd,otherEnd)){
						
						UMLClass correlationTable = TransformerUtils.findCorrelationTable(association, model, assocKlass, false);
						if (correlationTable == null) //One to Many - No Join Table
						{
							UMLClass assocTable = TransformerUtils.getTable(assocKlass);
							String keyColumnName = TransformerUtils.findAssociatedColumn(assocTable,assocKlass,thisEnd);
						}else{ //One to Many - Join Table
							String keyColumnName = TransformerUtils.findAssociatedColumn(correlationTable,assocKlass,thisEnd);
							String assocColumnName = TransformerUtils.findAssociatedColumn(correlationTable,klass,otherEnd);
							String inverseColumnName =  TransformerUtils.findInverseColumnValue(correlationTable,assocKlass,thisEnd);
							if(!"".equals(inverseColumnName) && !assocColumnName.equals(inverseColumnName))
								errors.addError(new GeneratorError("Different columns used for implements-association and inverse-of of the same association"));
						}
					}else if(TransformerUtils.isMany2One(thisEnd,otherEnd)){
						UMLClass correlationTable = TransformerUtils.findCorrelationTable(association, model, assocKlass, false);
						if (correlationTable == null) //Many to One - No Join Table
						{
							String keyColumnName = TransformerUtils.findAssociatedColumn(table,klass,otherEnd);
						}else{ // Many to One - Join Table
							String keyColumnName = TransformerUtils.findAssociatedColumn(correlationTable,assocKlass,thisEnd);
							String assocColumnName = TransformerUtils.findAssociatedColumn(correlationTable,klass,otherEnd);
							String inverseColumnName =  TransformerUtils.findInverseColumnValue(correlationTable,assocKlass,thisEnd);
							if(!"".equals(inverseColumnName) && !assocColumnName.equals(inverseColumnName))
								errors.addError(new GeneratorError("Different columns used for implements-association and inverse-of of the same association"));
						}
						
					}else{

						UMLClass correlationTable = TransformerUtils.findCorrelationTable(association, model, assocKlass, false);
						if (correlationTable == null) //One to One - No Join Table
						{
							String keyColumnName = TransformerUtils.findAssociatedColumn(table,klass,otherEnd, false);
							Boolean keyColumnPresent = (keyColumnName!=null && !"".equals(keyColumnName));
							if(!thisEnd.isNavigable() && !keyColumnPresent)
									errors.addError(new GeneratorError("One to one unidirectional mapping requires key column to be present in the source class"+TransformerUtils.getFQCN(klass)));
						}else{
							String keyColumnName = TransformerUtils.findAssociatedColumn(correlationTable,assocKlass,thisEnd);
							String assocColumnName = TransformerUtils.findAssociatedColumn(correlationTable,klass,otherEnd);
							String inverseColumnName =  TransformerUtils.findInverseColumnValue(correlationTable,assocKlass,thisEnd);
							if(!"".equals(inverseColumnName) && !assocColumnName.equals(inverseColumnName))
								errors.addError(new GeneratorError("Different columns used for implements-association and inverse-of of the same association"));
						}
					}
				}
			}
			catch (GenerationException e) 
			{
				errors.addError(new GeneratorError("Association validation failed ", e));
			}
		}
	}

	private void validateAttributesMapping(UMLClass klass, UMLClass table, GeneratorErrors errors) 
	{
		String thisClassName = TransformerUtils.getFQCN(klass);
		for(UMLAttribute attribute: klass.getAttributes())
		{
			try {
				TransformerUtils.getMappedColumnName(table,thisClassName+"."+attribute.getName());
			} catch (GenerationException e) {
				errors.addError(new GeneratorError("Attribute mapping validation failed for "+thisClassName+"."+attribute.getName()+" ", e));
			}
		}
	}
}