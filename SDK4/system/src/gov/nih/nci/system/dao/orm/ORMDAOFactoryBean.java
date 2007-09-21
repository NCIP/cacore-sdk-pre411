package gov.nih.nci.system.dao.orm;

import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.acegi.CSMAfterInvocationProvider;
import gov.nih.nci.security.acegi.authentication.CSMAuthenticationProvider;
import gov.nih.nci.security.authorization.instancelevel.InstanceLevelSecurityHelper;

import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

public class ORMDAOFactoryBean extends LocalSessionFactoryBean {
	
	private static Logger log = Logger.getLogger(ORMDAOFactoryBean.class.getName());
	
	private ORMDAOImpl ormDAO;
	
	private AuthorizationManager authorizationManager = null;
	private CSMAuthenticationProvider authenticationProvider = null;
	
	boolean securityEnabledFlag = false;
	boolean instanceLevelSecurity = false;
	boolean attributeLevelSecurity = false;
	boolean caseSensitive;
	int resultCountPerQuery;
	
	public ORMDAOFactoryBean(String configLocation, Properties systemProperties, Map systemPropertiesMap) throws Exception {	
	
		Resource resource = new ClassPathResource(configLocation);
		this.setConfigLocation(resource);
		
		try {
			this.caseSensitive = Boolean.parseBoolean(systemProperties.getProperty("caseSensitive"));
			this.resultCountPerQuery = Integer.parseInt(systemProperties.getProperty("resultCountPerQuery"));
			String securityEnabled = (String)systemProperties.getProperty("securityEnabled");
			if ("yes".equalsIgnoreCase(securityEnabled) || "true".equalsIgnoreCase(securityEnabled))
			{
				securityEnabledFlag = true;
				String instanceLevelSecurityEnabled = (String)systemProperties.getProperty("instanceLevelSecurityEnabled");
				String attributeLevelSecurityEnabled = (String)systemProperties.getProperty("attributeLevelSecurityEnabled");
				instanceLevelSecurity = "yes".equalsIgnoreCase(instanceLevelSecurityEnabled) || "true".equalsIgnoreCase(instanceLevelSecurityEnabled);
				attributeLevelSecurity = "yes".equalsIgnoreCase(attributeLevelSecurityEnabled) || "true".equalsIgnoreCase(attributeLevelSecurityEnabled);
				this.authenticationProvider = (CSMAuthenticationProvider)systemPropertiesMap.get("AUTHENTICATION_PROVIDER");
				this.authorizationManager = authenticationProvider.getUserDetailsService().authorizationManagerInstance();
			}		
		} catch (Exception e){
			log.error(e);
			throw e;
		}
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
	
	public void afterPropertiesSet() throws Exception
	{
		super.afterPropertiesSet();
		if (securityEnabledFlag)
			ormDAO = new ORMDAOImpl((SessionFactory)this.getSessionFactory(), (Configuration)this.getConfiguration(), caseSensitive, resultCountPerQuery, instanceLevelSecurity, attributeLevelSecurity, authenticationProvider);
		else
			ormDAO = new ORMDAOImpl((SessionFactory)this.getSessionFactory(), (Configuration)this.getConfiguration(), caseSensitive, resultCountPerQuery, instanceLevelSecurity, attributeLevelSecurity, null);			
	}
	protected void postProcessConfiguration(Configuration configuration)
	{
		if (instanceLevelSecurity)
			InstanceLevelSecurityHelper.addFilters(authorizationManager, configuration);
	}
}
