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

import org.apache.log4j.Logger;

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
 * <P> When it is a one to many implicit polymorphic relationship (Hibernate any relationship) between classes and other end of the association is navigable
 * 	<UL>
 * 	<LI>Associated object table should be present</LI>
 * 	<LI>Key column to fetch the associated objects should be present in the associated object table</LI>
 * 	<LI>Key column to fetch itself from the associated class should be present in the associated object table</LI>
 * 	<LI>Subclasses of non-implicit parent object(s) should each be marked with a discriminating value</LI>
 * 	</UL>
 * </P>
 * <P> When it is a many to many implicit polymorphic relationship (Hibernate many-to-any relationship) between classes and other end of the association is navigable
 * 	<UL>
 * 	<LI>Join table should be present</LI>
 * 	<LI>Key column to fetch the associated objects should be present in the join table</LI>
 * 	<LI>Key column to fetch itself from the associated class should be present in the join table</LI>
 * 	<LI>It should be clearly indicated that one of the two columns in the joint table is inverse of the second column</LI>
 *  <LI>Subclasses of non-implicit parent object(s) should each be marked with a discriminating value</LI>
 * 	</UL>
 * </P> 
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
 */
public class UMLModelMappingValidator implements Validator
{
	private static Logger log = Logger.getLogger(UMLModelMappingValidator.class);
	
	private boolean enabled = true;
	
	private String name = UMLModelMappingValidator.class.getName();	
	
	public GeneratorErrors validate(UMLModel model)
	{
		GeneratorErrors errors = new GeneratorErrors();
		
		if (model == null){
			errors.addError(new GeneratorError(getName() + ": No Model element found within XMI file"));
			return errors;
		}		
		
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
			if (!TransformerUtils.isImplicitParent(klass))
				table = TransformerUtils.getTable(klass);
		} catch (GenerationException e) {
				errors.addError(new GeneratorError(getName() + ": Table search failed for "+fqcn+" ", e));
		}
		if(table != null)
			validateClass(model, klass, table, errors);
	}

	private void validateClass(UMLModel model, UMLClass klass, UMLClass table, GeneratorErrors errors) {
		validateIdAttributeMapping(klass, table, errors);
		validateAttributesMapping(model, klass, table, errors);
		validateAssociations(model, klass, table, errors);
		validateSubClass(model, klass, table, errors);
	}

	private void validateSubClass(UMLModel model, UMLClass klass, UMLClass table, GeneratorErrors errors) {
		if(!hasSubClass(klass)) return;
		log.debug("Validating subClass for klass: " +klass.getName());
		String fqcn = TransformerUtils.getFQCN(klass);
		log.debug("fqcn: " + fqcn);
		try 
		{
			UMLAttribute idAttr = TransformerUtils.getClassIdAttr(klass);
			log.debug("idAttr: " + idAttr.getName());
			String discriminatorColumnName = TransformerUtils.findDiscriminatingColumnName(klass);		
			log.debug("discriminatorColumnName: " + discriminatorColumnName);
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
							errors.addError(new GeneratorError(getName() + ": When the discriminating column is absent, the subclass and the parent class can not be persisted in the same table : "+subFqcn));
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
					log.debug("subKlass: " + subKlass.getName());
					if(subKlass!=klass)
					{
						String subFqcn = TransformerUtils.getFQCN(subKlass);
						log.debug("subFqcn: " + subFqcn);
						String discriminatorValue = TransformerUtils.getDiscriminatorValue(subKlass);
						log.debug("discriminatorValue: " + discriminatorValue);
						if(discriminatorValue == null || discriminatorValue.trim().length() ==0)
							errors.addError(new GeneratorError(getName() + ": Discriminator value not present for the "+subFqcn+" class"));
						if(discriminatorValues.get(discriminatorValue)!= null)
							errors.addError(new GeneratorError(getName() + ": Same discriminator value for "+subFqcn+" and "+discriminatorValues.get(discriminatorValue)+ " class"));
						if(TransformerUtils.getTable(subKlass) != table)
						{
							log.debug("klass table: " + table.getName() + "; subKlass table: " + TransformerUtils.getTable(subKlass).getName());
							for(UMLGeneralization subgen:subKlass.getGeneralizations())
							{
								UMLClass subSubKlass = (UMLClass)subgen.getSubtype();
								log.debug("subSubKlass: " + subSubKlass.getName());
								if(subKlass!=subSubKlass && subSubKlass!=TransformerUtils.getSuperClass(subKlass))
								{
									errors.addError(new GeneratorError(getName() + ": When the discriminating column is present, the subclass and the parent class should be persisted in the same table unless the subclass does not have any subclasses : "+subFqcn));
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
			errors.addError(new GeneratorError(getName() + ": Subclass validation failed for "+fqcn+" class", e));
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
		if (TransformerUtils.isImplicitParent(klass))
			return;
		
		String thisClassName = TransformerUtils.getFQCN(klass);

		boolean error = false;
		try {
			
			UMLAttribute idAttr = TransformerUtils.getClassIdAttr(klass);
			if(idAttr == null) error = true;				
		} catch (GenerationException e) {
			error =  true;
		}
		if(error) 
			errors.addError(new GeneratorError(getName() + ": ID Attribute mapping validation failed for "+thisClassName+" "));
	}

	private void validateAssociations(UMLModel model, UMLClass klass, UMLClass table, GeneratorErrors errors) 
	{
		UMLClass currentKlass = klass;
		do{

			for(UMLAssociation association: currentKlass.getAssociations())
			{
				try 
				{
					List <UMLAssociationEnd>ends = association.getAssociationEnds();
					UMLAssociationEnd thisEnd = TransformerUtils.getThisEnd(currentKlass, ends);
					UMLAssociationEnd otherEnd = TransformerUtils.getOtherEnd(currentKlass, ends);

					if(otherEnd.isNavigable())
					{
						UMLClass assocKlass = (UMLClass)otherEnd.getUMLElement();
						if(TransformerUtils.isAny(thisEnd,otherEnd)){
							
							//implicit polymorphic validations for Any associations
							Map<String, String> discriminatorValues = new HashMap<String, String>();

							UMLClass implicitClass = (UMLClass)otherEnd.getUMLElement();
							String discriminatorValue = null;
							String nonImplicitSubclassFqcn = null;
							
							for (UMLClass nonImplicitSubclass:TransformerUtils.getNonImplicitSubclasses(implicitClass)){
								discriminatorValue = TransformerUtils.getDiscriminatorValue(nonImplicitSubclass);
								nonImplicitSubclassFqcn = TransformerUtils.getFQCN(nonImplicitSubclass);
								
								log.debug("discriminatorValue: " + discriminatorValue);
								if(discriminatorValue == null || discriminatorValue.trim().length() ==0)
									errors.addError(new GeneratorError(getName() + ": Discriminator value not present for the "+implicitClass+" class"));
								if(discriminatorValues.get(discriminatorValue)!= null)
									errors.addError(new GeneratorError(getName() + ": Same discriminator value for "+implicitClass+" and "+discriminatorValues.get(discriminatorValue)+ " class"));	
								
								discriminatorValues.put(discriminatorValue, nonImplicitSubclassFqcn);
							}

							UMLClass anyTable = TransformerUtils.getTable(currentKlass);
							TransformerUtils.getImplicitDiscriminatorColumn(anyTable,currentKlass,otherEnd.getRoleName());
							TransformerUtils.getImplicitIdColumn(anyTable,currentKlass,otherEnd.getRoleName());
						} else if(TransformerUtils.isMany2Any(thisEnd,otherEnd)){
							
							//implicit polymorphic validations for Many-To-Any associations
							
							UMLClass correlationTable = TransformerUtils.findCorrelationTable(association, model, assocKlass);
							String keyColumnName = TransformerUtils.findAssociatedColumn(correlationTable,assocKlass,thisEnd, currentKlass,otherEnd, true);
							String assocColumnName = TransformerUtils.findAssociatedColumn(correlationTable,currentKlass,otherEnd, assocKlass, thisEnd, true);
							String inverseColumnName =  TransformerUtils.findInverseColumnValue(correlationTable,assocKlass,thisEnd);
							
							if(!"".equals(inverseColumnName) && !assocColumnName.equals(inverseColumnName))
								errors.addError(new GeneratorError(getName() + "Different columns used for implements-association and inverse-of of the same association"));
							
							String inverseValue = assocColumnName.equals(inverseColumnName) ?"true":"false";
							String joinTableName = correlationTable.getName();
							
							Map<String, String> discriminatorValues = new HashMap<String, String>();

							UMLClass implicitClass = (UMLClass)otherEnd.getUMLElement();
							String discriminatorValue = null;
							String nonImplicitSubclassFqcn = null;
							
							for (UMLClass nonImplicitSubclass:TransformerUtils.getNonImplicitSubclasses(implicitClass)){
								discriminatorValue = TransformerUtils.getDiscriminatorValue(nonImplicitSubclass);
								nonImplicitSubclassFqcn = TransformerUtils.getFQCN(nonImplicitSubclass);
								
								log.debug("discriminatorValue: " + discriminatorValue);
								if(discriminatorValue == null || discriminatorValue.trim().length() ==0)
									errors.addError(new GeneratorError(getName() + ": Discriminator value not present for the "+implicitClass+" class"));
								if(discriminatorValues.get(discriminatorValue)!= null)
									errors.addError(new GeneratorError(getName() + ": Same discriminator value for "+implicitClass+" and "+discriminatorValues.get(discriminatorValue)+ " class"));	
								
								discriminatorValues.put(discriminatorValue, nonImplicitSubclassFqcn);
							}

							TransformerUtils.getImplicitDiscriminatorColumn(correlationTable,currentKlass,otherEnd.getRoleName());
							TransformerUtils.getImplicitIdColumn(correlationTable,currentKlass,otherEnd.getRoleName());

						} else if(TransformerUtils.isMany2Many(thisEnd,otherEnd)){
							UMLClass correlationTable = TransformerUtils.findCorrelationTable(association, model, assocKlass);
							String keyColumnName = TransformerUtils.findAssociatedColumn(correlationTable,assocKlass,thisEnd,currentKlass,otherEnd,true);
							String assocColumnName = TransformerUtils.findAssociatedColumn(correlationTable,currentKlass,otherEnd,assocKlass,thisEnd, true);
							String inverseColumnName =  TransformerUtils.findInverseColumnValue(correlationTable,assocKlass,thisEnd);
							if(!"".equals(inverseColumnName) && !assocColumnName.equals(inverseColumnName))
								errors.addError(new GeneratorError(getName() + ": Different columns used for implements-association and inverse-of of the same association"));
						}else if(TransformerUtils.isOne2Many(thisEnd,otherEnd)){
							if(TransformerUtils.isImplicitParent(assocKlass)) {
								errors.addError(new GeneratorError(getName() + ": Implicit polymorphic one-to-many association between class " + currentKlass + " and implicit parent class " + assocKlass +" is not supported"));
							} else {
								
								UMLClass correlationTable = TransformerUtils.findCorrelationTable(association, model, assocKlass, false);
								if (correlationTable == null) //One to Many - No Join Table
								{
									UMLClass assocTable = null;
									if (!TransformerUtils.isImplicitParent(assocKlass))
										assocTable = TransformerUtils.getTable(assocKlass);
	
									if (assocTable != null){
										String keyColumnName = TransformerUtils.findAssociatedColumn(assocTable,assocKlass,thisEnd,currentKlass,otherEnd, false);
									}
								}else{ //One to Many - Join Table
									String keyColumnName = TransformerUtils.findAssociatedColumn(correlationTable,assocKlass,thisEnd,currentKlass,otherEnd, true);
									String assocColumnName = TransformerUtils.findAssociatedColumn(correlationTable,currentKlass,otherEnd,assocKlass,thisEnd, true);
									String inverseColumnName =  TransformerUtils.findInverseColumnValue(correlationTable,assocKlass,thisEnd);
									if(!"".equals(inverseColumnName) && !assocColumnName.equals(inverseColumnName))
										errors.addError(new GeneratorError(getName() + ": Different columns used for implements-association and inverse-of of the same association"));
								}
							}
						}else if(TransformerUtils.isMany2One(thisEnd,otherEnd)){
							UMLClass correlationTable = TransformerUtils.findCorrelationTable(association, model, assocKlass, false);
							if (correlationTable == null) //Many to One - No Join Table
							{
								String keyColumnName = TransformerUtils.findAssociatedColumn(table,currentKlass,otherEnd,assocKlass,thisEnd, false);
							}else{ // Many to One - Join Table
								String keyColumnName = TransformerUtils.findAssociatedColumn(correlationTable,assocKlass,thisEnd,currentKlass,otherEnd, true);
								String assocColumnName = TransformerUtils.findAssociatedColumn(correlationTable,currentKlass,otherEnd,assocKlass,thisEnd, true);
								String inverseColumnName =  TransformerUtils.findInverseColumnValue(correlationTable,assocKlass,thisEnd);
								if(!"".equals(inverseColumnName) && !assocColumnName.equals(inverseColumnName))
									errors.addError(new GeneratorError(getName() + ": Different columns used for implements-association and inverse-of of the same association"));
							}

						}else{// one-to-one
							if(TransformerUtils.isImplicitParent(assocKlass)) {
								errors.addError(new GeneratorError(getName() + ": Implicit polymorphic one-to-one association between class " + TransformerUtils.getFQCN(currentKlass) + " and implicit parent class " + TransformerUtils.getFQCN(assocKlass) +" is not supported"));
							} else {
								UMLClass correlationTable = TransformerUtils.findCorrelationTable(association, model, assocKlass, false);
								if (correlationTable == null) //One to One - No Join Table
								{
									String keyColumnName = TransformerUtils.findAssociatedColumn(table,currentKlass,otherEnd,assocKlass,thisEnd,false, false);
									Boolean keyColumnPresent = (keyColumnName!=null && !"".equals(keyColumnName));
									if(!thisEnd.isNavigable() && !keyColumnPresent)
										errors.addError(new GeneratorError(getName() + ": One to one unidirectional mapping requires key column to be present in the source class"+TransformerUtils.getFQCN(currentKlass)));
								}else{
									String keyColumnName = TransformerUtils.findAssociatedColumn(correlationTable,assocKlass,thisEnd,currentKlass,otherEnd, true);
									String assocColumnName = TransformerUtils.findAssociatedColumn(correlationTable,currentKlass,otherEnd,assocKlass,thisEnd, true);
									String inverseColumnName =  TransformerUtils.findInverseColumnValue(correlationTable,assocKlass,thisEnd);
									if(!"".equals(inverseColumnName) && !assocColumnName.equals(inverseColumnName))
										errors.addError(new GeneratorError(getName() + ": Different columns used for implements-association and inverse-of of the same association"));
								}
							}
						}
					}
				}
				catch (GenerationException e) 
				{
					errors.addError(new GeneratorError(getName() + ": Association validation failed ", e));
				}
			} // for
		} while(currentKlass!=null && TransformerUtils.isImplicitParent(currentKlass));
	}

	private void validateAttributesMapping(UMLModel model, UMLClass klass, UMLClass table, GeneratorErrors errors) 
	{
		String thisClassName = TransformerUtils.getFQCN(klass);
		for(UMLAttribute attribute: klass.getAttributes())
		{
			try {
				if(TransformerUtils.isCollection(klass, attribute))
				{
					UMLClass collectionTable = TransformerUtils.findCollectionTable(attribute, model);
					String keyColumnName = TransformerUtils.getCollectionKeyColumnName(collectionTable, klass, attribute);
					String elementColumnName = TransformerUtils.getCollectionElementColumnName(collectionTable, klass, attribute);
					String elementType = TransformerUtils.getCollectionElementHibernateType(klass, attribute);
					
				}
				else
				{
				TransformerUtils.getMappedColumnName(table,thisClassName+"."+attribute.getName());
				}
			} catch (GenerationException e) {
				errors.addError(new GeneratorError(getName() + ": Attribute mapping validation failed for "+thisClassName+"."+attribute.getName()+" ", e));
			}
		}
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	public Boolean isEnabled() {
		return enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}