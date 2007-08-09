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
		
		boolean instanceLevelSecurity;
		boolean attributeLevelSecurity;
		try {
			caseSensitive = Boolean.parseBoolean(systemProperties.getProperty("caseSensitive"));
			resultCountPerQuery = Integer.parseInt(systemProperties.getProperty("resultCountPerQuery"));
			String instanceLevelSecurityEnabled = (String)systemProperties.getProperty("instanceLevelSecurityEnabled");
			String attributeLevelSecurityEnabled = (String)systemProperties.getProperty("attributeLevelSecurityEnabled");
			instanceLevelSecurity = "yes".equalsIgnoreCase(instanceLevelSecurityEnabled) || "true".equalsIgnoreCase(instanceLevelSecurityEnabled);
			attributeLevelSecurity = "yes".equalsIgnoreCase(attributeLevelSecurityEnabled) || "true".equalsIgnoreCase(attributeLevelSecurityEnabled);
			
			//TODO CSMTEAM
			//If InstanceLevel and or attribute level security is enabled then set corresponding filters
			//lsfb.setFilterDefinitions(org.hibernate.engine.FilterDefinition[]);
			
			lsfb.afterPropertiesSet();
		} catch (Exception e){
			log.error(e);
			throw e;
		}
		
		ormDAO = new ORMDAOImpl((SessionFactory)lsfb.getObject(), (Configuration)lsfb.getConfiguration(), caseSensitive, resultCountPerQuery, instanceLevelSecurity, attributeLevelSecurity);
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
