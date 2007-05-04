package gov.nih.nci.codegen.transformer;

import java.util.Collection;

import gov.nih.nci.codegen.Artifact;
import gov.nih.nci.codegen.ArtifactHandler;
import gov.nih.nci.codegen.GenerationException;
import gov.nih.nci.codegen.GeneratorErrors;
import gov.nih.nci.codegen.Transformer;
import gov.nih.nci.codegen.util.TransformerUtils;
import gov.nih.nci.ncicb.xmiinout.domain.UMLClass;
import gov.nih.nci.ncicb.xmiinout.domain.UMLModel;
import gov.nih.nci.ncicb.xmiinout.domain.UMLPackage;

public abstract class UMLClassJetTransformer implements Transformer
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
		processPackages(model.getPackages());
		return null;
	}
	

	private void processPackages(Collection<UMLPackage> pkgCollection)
	{
		for(UMLPackage pkg:pkgCollection)
			processPackage(pkg);
	}
	
	private void processPackage(UMLPackage rootPkg)
	{
		for(UMLClass klass:rootPkg.getClasses())
		{
			String pkgName = TransformerUtils.getFullPackageName(klass);
			if(!"table".equals(klass.getStereotype()) && !pkgName.startsWith("java.lang") && !pkgName.startsWith("java.util"))
			{
				try
				{
					Artifact artifact = executeTemplate(klass);
					artifactHandler.handleArtifact(artifact);
				}
				catch(GenerationException ge)
				{
					System.out.println("ERROR----------");
				}
			}
		}
		processPackages(rootPkg.getPackages());
	}
	
	public abstract Artifact executeTemplate(UMLClass klass) throws GenerationException;

	public GeneratorErrors validate(UMLModel model)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}