package gov.nih.nci.system.dao.orm;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

public class ORMDAOFactoryBean implements FactoryBean {
	
	private static Logger log = Logger.getLogger(ORMDAOFactoryBean.class.getName());
	
	private LocalSessionFactoryBean lsfb;
	
	private ORMDAOImpl ormDAO;

	public ORMDAOFactoryBean(String configLocation, Properties systemProperties) throws Exception {	
		lsfb = new LocalSessionFactoryBean();
		Resource resource = new ClassPathResource(configLocation);
		lsfb.setConfigLocation(resource);
		
		boolean caseSensitive;
		int resultCountPerQuery;
		
		try {
			caseSensitive = Boolean.parseBoolean(systemProperties.getProperty("caseSensitive"));
			resultCountPerQuery = Integer.parseInt(systemProperties.getProperty("resultCountPerQuery"));
			
			lsfb.afterPropertiesSet();
		} catch (Exception e){
			log.error(e);
			throw e;
		}
		
		ormDAO = new ORMDAOImpl((SessionFactory)lsfb.getObject(), (Configuration)lsfb.getConfiguration(), caseSensitive, resultCountPerQuery);
	}
	
	public Object getObject() {
		return ormDAO;
	}
	
	public Class getObjectType() {
		return ormDAO.getClass();
	}
	
	public boolean isSingleton(){
		return true;
	}
}
