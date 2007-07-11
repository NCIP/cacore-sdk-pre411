package gov.nih.nci.system.dao.orm;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

public class DAOSessionFactoryBean extends LocalSessionFactoryBean {

	private static Logger log = Logger.getLogger(DAOSessionFactoryBean.class.getName());
	private SessionFactory instance;
	public DAOSessionFactoryBean(String configLocation) {	
		super();
		
		Resource resource = new ClassPathResource(configLocation);
		setConfigLocation(resource);
		try {
			instance = buildSessionFactory();
			
			log.info("this.getObject(): " + this.getObject());
		} catch (Exception e){
			log.error("Error building SessionFactory: ", e);
		}
	}
	public SessionFactory getInstance() {
		return instance;
	}
}
