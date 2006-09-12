package gov.nih.nci.system.dao;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Dan Dumitru
 * 
 */

public class DAOObjectFactory

{
	private static XmlBeanFactory factory = new XmlBeanFactory(
			new ClassPathResource("orm1Dao.xml"));

	private DAOObjectFactory() {

	}

	/**
	 * get Object instance from the class name.
	 * 
	 * @param classname
	 * @return
	 */

	public Object getObject(String classname) {
		return factory.getBean(classname);
	}

}
