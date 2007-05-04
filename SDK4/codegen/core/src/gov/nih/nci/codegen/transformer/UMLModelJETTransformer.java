package gov.nih.nci.codegen.transformer;

import gov.nih.nci.codegen.Artifact;
import gov.nih.nci.codegen.ArtifactHandler;
import gov.nih.nci.codegen.GenerationException;
import gov.nih.nci.codegen.GeneratorErrors;
import gov.nih.nci.codegen.Transformer;
import gov.nih.nci.ncicb.xmiinout.domain.UMLModel;

public abstract class UMLModelJETTransformer implements Transformer
{

	private ArtifactHandler artifactHandler;
	
	/**
	 * @param artifactHandler the artifactHandler to set
	 */
	public void setArtifactHandler(ArtifactHandler artifactHandler)
	{
		this.artifactHandler = artifactHandler;
	}
	
	public GeneratorErrors execute(UMLModel model)
	{
		try {
			Artifact artifact = executeTemplate(model);
			artifactHandler.handleArtifact(artifact);
		} catch (GenerationException e) {
		}
		return null;
	}

	public GeneratorErrors validate(UMLModel model)
	{
		return null;
	}
	
	protected abstract Artifact executeTemplate(UMLModel model);	
	
}