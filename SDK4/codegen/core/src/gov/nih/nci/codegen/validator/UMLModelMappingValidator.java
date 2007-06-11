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

public class UMLModelMappingValidator implements Validator
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
		String fqcn = TransformerUtils.getFQCN(klass);
		try 
		{
			UMLAttribute idAttr = TransformerUtils.getClassIdAttr(klass);
			String discriminatorColumnName = TransformerUtils.findDiscriminatingColumnName(fqcn, table);		
			if(discriminatorColumnName == null || "".equals(discriminatorColumnName))
			{
				for(UMLGeneralization gen:klass.getGeneralizations())
				{
					UMLClass subKlass = (UMLClass)gen.getSubtype();
					if(subKlass!=klass){
						UMLClass subTable = TransformerUtils.getTable(subKlass);
						String subFqcn = TransformerUtils.getFQCN(subKlass);
						String keyColumnName = TransformerUtils.getMappedColumnName(subTable,subFqcn+"."+idAttr.getName());
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
						String discriminatorValue = TransformerUtils.getDiscriminatorValue(klass);
						if(discriminatorValue == null || discriminatorValue.trim().length() ==0)
							errors.addError(new GeneratorError("Discriminator value not present for the "+subFqcn+" class"));
						if(discriminatorValues.get(discriminatorValue)!= null)
							errors.addError(new GeneratorError("Same discriminator value for "+subFqcn+" and "+discriminatorValues.get(discriminatorValue)+ " class"));
						discriminatorValues.put(discriminatorValue, subFqcn);
						validateClass(model,subKlass, errors);
					}
				}
			}
		} 
		catch (GenerationException e) 
		{
			errors.addError(new GeneratorError("Subclass validation failed for "+fqcn+" class"));
		}		
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
						UMLClass assocTable = TransformerUtils.getTable(assocKlass);
						String keyColumnName = TransformerUtils.findAssociatedColumn(assocTable,assocKlass,thisEnd);
					}else if(TransformerUtils.isMany2One(thisEnd,otherEnd)){
						String keyColumnName = TransformerUtils.findAssociatedColumn(table,klass,otherEnd);
					}else{
						String keyColumnName = TransformerUtils.findAssociatedColumn(table,klass,otherEnd, false);
						Boolean keyColumnPresent = (keyColumnName!=null && !"".equals(keyColumnName));
						if(!thisEnd.isNavigable() && !keyColumnPresent)
							errors.addError(new GeneratorError("One to one unidirectional mapping requires key column to be present in the source class"));
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