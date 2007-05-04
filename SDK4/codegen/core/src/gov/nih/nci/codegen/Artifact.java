package gov.nih.nci.codegen;

import gov.nih.nci.ncicb.xmiinout.domain.UMLClass;

public interface Artifact
{
	/**
	 * @return the content
	 */
	public String getContent();
	
	/**
	 * @return the source
	 */	
	public UMLClass getSource();
	
	
}