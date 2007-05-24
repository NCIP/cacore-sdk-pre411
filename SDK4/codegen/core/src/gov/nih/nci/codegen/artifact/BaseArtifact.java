package gov.nih.nci.codegen.artifact;

import gov.nih.nci.codegen.Artifact;
import gov.nih.nci.codegen.util.TransformerUtils;
import gov.nih.nci.ncicb.xmiinout.domain.UMLClass;
import gov.nih.nci.ncicb.xmiinout.domain.UMLPackage;
import gov.nih.nci.ncicb.xmiinout.domain.bean.JDomDomainObject;

public class BaseArtifact implements Artifact
{
	private String content;
	private String sourceName;
	
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

	public String getSourceName() {
		return sourceName;
	}
	
	public void createSourceName(UMLClass klass) {
		sourceName = TransformerUtils.getFQCN(klass).replace('.','/');
	}	
	
	
	public void createSourceName(UMLPackage pkg) {
		sourceName = TransformerUtils.getFullPackageName(pkg);
	}		
		
}