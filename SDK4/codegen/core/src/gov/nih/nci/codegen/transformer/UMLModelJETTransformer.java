package gov.nih.nci.codegen.transformer;

import gov.nih.nci.codegen.Artifact;
import gov.nih.nci.codegen.ArtifactHandler;
import gov.nih.nci.codegen.GenerationException;
import gov.nih.nci.codegen.GeneratorError;
import gov.nih.nci.codegen.GeneratorErrors;
import gov.nih.nci.codegen.Transformer;
import gov.nih.nci.ncicb.xmiinout.domain.UMLModel;

import java.util.Map;

public abstract class UMLModelJETTransformer implements Transformer
{

	private ArtifactHandler artifactHandler;
	
	private Map<String, Object> configurationParams;
	
	/**
	 * @param artifactHandler the artifactHandler to set
	 */
	public void setArtifactHandler(ArtifactHandler artifactHandler)
	{
		this.artifactHandler = artifactHandler;
	}
	
	public GeneratorErrors execute(UMLModel model)
	{
		GeneratorErrors errors = new GeneratorErrors();
		try 
		{
			Artifact artifact = executeTemplate(model, configurationParams);
			artifactHandler.handleArtifact(artifact);
		} 
		catch (GenerationException e) 
		{
			errors.addError(new GeneratorError("Error while generating artifact for the model",e));
		}
		return errors;
	}

	public GeneratorErrors validate(UMLModel model)
	{
		return null;
	}
	
	protected abstract Artifact executeTemplate(UMLModel model, Map<String, Object> configurationParams) throws GenerationException;

	public void setConfigurationParams(Map<String, Object> configurationParams) {
		this.configurationParams = configurationParams;
	}	
	
	
}