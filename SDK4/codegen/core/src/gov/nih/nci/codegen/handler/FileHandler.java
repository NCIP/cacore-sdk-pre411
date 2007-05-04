package gov.nih.nci.codegen.handler;

import gov.nih.nci.codegen.Artifact;
import gov.nih.nci.codegen.ArtifactHandler;
import gov.nih.nci.codegen.GenerationException;
import gov.nih.nci.codegen.util.TransformerUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler implements ArtifactHandler
{

	private String outputDir;
	private String fileName;
	private String prefix;
	private String suffix;
	private Boolean useArtifactSource;
	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	/**
	 * @return the outputDir
	 */
	public String getOutputDir()
	{
		return outputDir;
	}
	/**
	 * @param outputDir the outputDir to set
	 */
	public void setOutputDir(String outputDir)
	{
		this.outputDir = outputDir;
	}
	/**
	 * @return the prefix
	 */
	public String getPrefix()
	{
		return prefix;
	}
	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}
	/**
	 * @return the suffix
	 */
	public String getSuffix()
	{
		return suffix;
	}
	/**
	 * @param suffix the suffix to set
	 */
	public void setSuffix(String suffix)
	{
		this.suffix = suffix;
	}
	/**
	 * @return the useArtifactSource
	 */
	public Boolean getUseArtifactSource()
	{
		return useArtifactSource;
	}
	/**
	 * @param useArtifactSource the useArtifactSource to set
	 */
	public void setUseArtifactSource(Boolean useArtifactSource)
	{
		this.useArtifactSource = useArtifactSource;
	}
	/* (non-Javadoc)
	 * @see gov.nih.nci.codegen.ArtifactHandler#handleArtifact(gov.nih.nci.codegen.Artifact)
	 */
	public void handleArtifact(Artifact artifact) throws GenerationException
	{
		String fName = prepareFileName(artifact);
		try
		{
			File f = new File(outputDir,fName);
			File p = f.getParentFile();
			if (!p.exists())
				p.mkdirs();
			
			f.createNewFile();
			FileWriter out = new FileWriter(f);
			out.write(artifact.getContent());
			out.close();
		}
		catch (IOException e) 
		{
			throw new GenerationException("Error writing to file : "+fName,e);
		}
	}
	
	private String prepareFileName(Artifact artifact)
	{
		String fName = useArtifactSource == true ? TransformerUtils.getFQCN(artifact.getSource()).replace('.','/') : fileName ;
		String name = (prefix == null ? "" : prefix) + fName + (suffix == null ? "" : suffix);
		return name;
	}
}