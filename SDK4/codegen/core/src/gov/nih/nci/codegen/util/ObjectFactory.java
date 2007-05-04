package gov.nih.nci.codegen.util;


import gov.nih.nci.codegen.GenerationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;


public class ObjectFactory
{
	private static Logger log = Logger.getLogger(ObjectFactory.class.getName());

	private static XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("CodegenConfig.xml"));

	private ObjectFactory() {}

	/**
	 * Get Object instance from the classname.
	 * 
	 * @param key Key representing the class (bean) to be retrieved as configured in the configuration file
	 * @return
	 * @throws ApplicationException 
	 */

	public static Object getObject(String key) throws GenerationException{
		try
		{
			return factory.getBean(key);
		}
		catch(Exception e)
		{
			log.info("No bean found for key = "+key +"\n");
			throw new GenerationException("No bean found for key = "+key +"\n",e);
		}
	}

}
