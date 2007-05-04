package gov.nih.nci.codegen;

import java.io.IOException;

import org.apache.log4j.Logger;

import gov.nih.nci.codegen.util.DebugUtils;
import gov.nih.nci.ncicb.xmiinout.domain.UMLModel;
import gov.nih.nci.ncicb.xmiinout.handler.HandlerEnum;
import gov.nih.nci.ncicb.xmiinout.handler.XmiException;
import gov.nih.nci.ncicb.xmiinout.handler.XmiHandlerFactory;
import gov.nih.nci.ncicb.xmiinout.handler.XmiInOutHandler;

/**
 * This class reads the XMI file and loads the UML model.
 * The constructor should be called only once in the system. 
 * 
 * @author Satish Patel
 *
 */
public class UMLModelLoader
{
	private static Logger log = Logger.getLogger(UMLModelLoader.class);
			
	private UMLModel model;
	
	private boolean printModel;
	
	public UMLModelLoader(String xmiFileName)
	{
		if(model!=null) return;
		loadModel(xmiFileName);
		
		if(printModel)
			DebugUtils.printModel(model);			
	}
	
	public UMLModel getUMLModel()
	{
		return model;
	}

	private void loadModel(String xmiFileName)
	{
		
		log.debug("Reading XMI File");
		
		XmiInOutHandler xmiHandler = XmiHandlerFactory.getXmiHandler(HandlerEnum.EADefault);
		try
		{
			xmiHandler.load(xmiFileName);
		} 
		catch (XmiException e)
		{
			log.error("Error reading XMI file: Malformed XMI file",e);
		} 
		catch (IOException e)
		{
			log.error("Error reading XMI file",e);
		}
		log.debug("XMI Loaded in Memory");
		
		model = xmiHandler.getModel();
		
		log.debug("UML Model retrieved");
	}
	
}