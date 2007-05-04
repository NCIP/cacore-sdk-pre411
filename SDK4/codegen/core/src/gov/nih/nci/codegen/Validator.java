package gov.nih.nci.codegen;

import gov.nih.nci.ncicb.xmiinout.domain.UMLModel;


/**
 * TODO Javadocs
 * @author Satish Patel
 *
 */
public interface Validator
{
	
	/**
	 * TODO Javadocs
	 * @param model
	 * @return
	 */
	public abstract GeneratorErrors validate(UMLModel model);
	
}