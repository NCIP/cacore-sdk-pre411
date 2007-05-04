package gov.nih.nci.codegen.artifact;

import gov.nih.nci.codegen.Artifact;
import gov.nih.nci.ncicb.xmiinout.domain.UMLClass;

public class BaseArtifact implements Artifact
{
	private String content;
	private UMLClass source;
	
	/**
	 * @return the content
	 */
	public String getContent()
	{
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content)
	{
		this.content = content;
	}
	/**
	 * @return the source
	 */
	public UMLClass getSource()
	{
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(UMLClass source)
	{
		this.source = source;
	}	
}