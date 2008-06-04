package gov.nih.nci.codegen.transformer;

import gov.nih.nci.cadsr.domain.PermissibleValue;
import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata;
import gov.nih.nci.codegen.Artifact;
import gov.nih.nci.codegen.ArtifactHandler;
import gov.nih.nci.codegen.GenerationException;
import gov.nih.nci.codegen.GeneratorError;
import gov.nih.nci.codegen.GeneratorErrors;
import gov.nih.nci.codegen.Transformer;
import gov.nih.nci.codegen.util.TransformerUtils;
import gov.nih.nci.ncicb.xmiinout.domain.UMLModel;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author Daniel Dumitru
 *
 */
public abstract class UMLHibernateValidatorJETTransformer implements Transformer
{
	private static Logger log = Logger.getLogger(UMLHibernateValidatorJETTransformer.class);
	
	private ArtifactHandler artifactHandler;
	
	private Map<String, Object> configurationParams;
	
	private boolean enabled = true;
	
	private String namespacePrefix;
	
	private String serviceURL;
	
	private String name = UMLModelJETTransformer.class.getName();
	
	protected TransformerUtils transformerUtils;
	
	private Map<String,String> caDSREnumMap = new HashMap<String,String>();

	public void setTransformerUtils(TransformerUtils transformerUtils) {
		this.transformerUtils = transformerUtils;
	}
	
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
			errors.addError(new GeneratorError(getName() + ": Error while generating artifact for the model",e));
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
	
	public String getNamespacePrefix() {
		return namespacePrefix;
	}

	public void setNamespacePrefix(String namespacePrefix) {
		this.namespacePrefix = namespacePrefix;
	}
	
	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}
	
	protected void initCaDSREnumMap() throws GenerationException {
		try
		{
			//Application Service retrieval for secured system
			//ApplicationService appService = ApplicationServiceProvider.getApplicationService("userId","password");
			ApplicationService appService;
			if (serviceURL == null || serviceURL.length() == 0){
				appService = ApplicationServiceProvider.getApplicationService();
			} else {
				appService = ApplicationServiceProvider.getApplicationServiceFromUrl(serviceURL);
			}
			
			Project proj = new Project();  
			proj.setShortName("caTISSUE Core");//TODO :: Externalize 
			log.debug("Searching for " + Project.class.getName());

			Collection<Object> results = appService.search("gov.nih.nci.cadsr.umlproject.domain.Project", proj);
			Collection<UMLAttributeMetadata> attrResults;
			Collection<Object> permValueResults;
			
			String path = 
				"gov.nih.nci.cadsr.domain.PermissibleValue," +
				"gov.nih.nci.cadsr.domain.ValueDomainPermissibleValue," +
				"gov.nih.nci.cadsr.domain.EnumeratedValueDomain," +
				"gov.nih.nci.cadsr.domain.ValueDomain," +
				"gov.nih.nci.cadsr.domain.DataElement," + 
				"gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata";
			
			for(Object obj : results)
			{
				Project project = (Project)obj;
				
				log.debug("Printing gov.nih.nci.cadsr.umlproject.domain.Project");
				log.debug("\tId: "+project.getId());
				log.debug("\tVersion: "+project.getVersion());
				log.debug("\tShortName: "+project.getShortName());
				log.debug("\tLongName: "+project.getLongName());
				log.debug("\tGmeNamespace: "+project.getGmeNamespace());
				
				//Get associated attributes
				attrResults = project.getUMLAttributeMetadataCollection();
				for(Object attr : attrResults)
				{
					log.debug("*******\n ");
					UMLAttributeMetadata attribute = (UMLAttributeMetadata)attr;
					
					log.debug("Printing gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata");
					log.debug("\tId: "+attribute.getId());
					log.debug("\tName: "+attribute.getName());
					
					//Perform NestedCriteria query for PermissibleValue
					log.debug("=======\n ");
					UMLAttributeMetadata attrCriteria = new UMLAttributeMetadata();
					attrCriteria.setId(attribute.getId());
					permValueResults = appService.search(path, attrCriteria);
					log.debug("permValueResults size(): " + permValueResults.size());
					
					if (permValueResults.size() > 0){
						//Build enumerated value string, which delimits values using a "|"
						log.debug("-------\n ");
						
						StringBuilder sb = new StringBuilder();
						Iterator<Object> iter = permValueResults.iterator();
						PermissibleValue value = (PermissibleValue)iter.next();
						
						log.debug("Printing gov.nih.nci.cadsr.domain.PermissibleValue");
						log.debug("\tId: "+value.getId());
						log.debug("\tValue: "+value.getValue());

						sb.append(value.getValue());
						
						while (iter.hasNext()){
							value = (PermissibleValue)iter.next();
							
							log.debug("Printing gov.nih.nci.cadsr.domain.PermissibleValue");
							log.debug("\tId: "+value.getId());
							log.debug("\tValue: "+value.getValue());
							
							sb.append("|").append(value.getValue());
						}
						log.debug("Adding caDSREnumMap entry for attribute " + attribute.getFullyQualifiedName() + ".  Enum value string is: " + sb.toString());
						caDSREnumMap.put(attribute.getFullyQualifiedName(), sb.toString());
					}
				}
			}
			
		}catch(Exception e)
		{
			log.error("Error retrieving caDSR Permissible Value Enumerations: " + e.getMessage(),e);
			throw new GenerationException("Error retrieving caDSR Permissible Value Enumerations: " + e.getMessage(),e);
		}
	}
	
	protected String getCaDSREnumPattern(String fqcnAttributeName){
		return caDSREnumMap.get(fqcnAttributeName);
	}

}