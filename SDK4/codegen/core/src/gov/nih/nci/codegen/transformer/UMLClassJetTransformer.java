package gov.nih.nci.codegen.transformer;

import gov.nih.nci.codegen.Artifact;
import gov.nih.nci.codegen.ArtifactHandler;
import gov.nih.nci.codegen.GenerationException;
import gov.nih.nci.codegen.GeneratorError;
import gov.nih.nci.codegen.GeneratorErrors;
import gov.nih.nci.codegen.Transformer;
import gov.nih.nci.codegen.util.TransformerUtils;
import gov.nih.nci.ncicb.xmiinout.domain.UMLClass;
import gov.nih.nci.ncicb.xmiinout.domain.UMLModel;

import java.util.Collection;

/**
 * @author Satish Patel
 *
 */
public abstract class UMLClassJetTransformer implements Transformer
{
	private ArtifactHandler artifactHandler;

	private boolean enabled = true;
	
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
		Collection<UMLClass> classes = getAllClasses(model);
		for(UMLClass klass:classes)
			try
		{
			Artifact artifact = executeTemplate(model, klass);
			artifactHandler.handleArtifact(artifact);
		}
		catch(GenerationException ge)
		{
			errors.addError(new GeneratorError("Error while generating artifact for the class "+klass.getName()+"\n\t",ge));
		}
		return errors;
	}
	
	public abstract Artifact executeTemplate(UMLModel model, UMLClass klass) throws GenerationException;

	public GeneratorErrors validate(UMLModel model)
	{
		return null;
	}
	
	protected Collection<UMLClass> getAllClasses(UMLModel model)
	{
		return TransformerUtils.getAllClasses(model);		
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	public Boolean isEnabled() {
		return enabled;
	}
	
	
}