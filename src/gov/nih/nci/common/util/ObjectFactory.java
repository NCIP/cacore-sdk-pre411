/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk/LICENSE.txt for details.
 */

package gov.nih.nci.common.util;

import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.impl.ApplicationServiceBusinessImpl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Dan Dumitru
 * 
 */

public class ObjectFactory

{
	private static XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("SDKSpringBeanConfig.xml"));
	private static Logger log = Logger.getLogger(ObjectFactory.class.getName());

	private ObjectFactory() {

	}

	/**
	 * get Object instance from the class name.
	 * 
	 * @param classname
	 * @return
	 * @throws ApplicationException 
	 */

	public static Object getObject(String classname) throws ApplicationException{
		try
		{
			return factory.getBean(classname);
		}
		catch(Exception e)
		{
			log.error("No bean found for key = "+classname +"\n",e);
			throw new ApplicationException("No bean found for key = "+classname +"\n",e);
		}
	}

}
