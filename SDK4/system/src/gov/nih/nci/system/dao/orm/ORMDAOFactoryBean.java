package gov.nih.nci.system.dao.orm;

import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.acegi.authentication.CSMAuthenticationProvider;
import gov.nih.nci.security.authorization.instancelevel.InstanceLevelSecurityHelper;

import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

public class ORMDAOFactoryBean extends LocalSessionFactoryBean {
	
	private static Logger log = Logger.getLogger(ORMDAOFactoryBean.class.getName());
	
	private ORMDAOImpl ormDAO;
	
	private AuthorizationManager authorizationManager = null;
	private CSMAuthenticationProvider authenticationProvider = null;
	
	private boolean securityEnabledFlag = false;
	private boolean instanceLevelSecurity = false;
	private boolean attributeLevelSecurity = false;
	private boolean caseSensitive;
	private int resultCountPerQuery;
	
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
		createDAO();
	}
	
	protected void createDAO()
	{
		if (isSecurityEnabledFlag())
			setOrmDAO(new ORMDAOImpl((SessionFactory)this.getSessionFactory(), (Configuration)this.getConfiguration(), isCaseSensitive(), getResultCountPerQuery(), isInstanceLevelSecurity(), isAttributeLevelSecurity(), getAuthenticationProvider()));
		else
			setOrmDAO(new ORMDAOImpl((SessionFactory)this.getSessionFactory(), (Configuration)this.getConfiguration(), isCaseSensitive(), getResultCountPerQuery(), isInstanceLevelSecurity(), isAttributeLevelSecurity(), null));
	}
	
	protected void postProcessConfiguration(Configuration configuration)
	{
		if (instanceLevelSecurity)
			InstanceLevelSecurityHelper.addFilters(authorizationManager, configuration);
	}

	protected boolean isAttributeLevelSecurity() {
		return attributeLevelSecurity;
	}

	protected void setAttributeLevelSecurity(boolean attributeLevelSecurity) {
		this.attributeLevelSecurity = attributeLevelSecurity;
	}

	protected CSMAuthenticationProvider getAuthenticationProvider() {
		return authenticationProvider;
	}

	protected void setAuthenticationProvider(
			CSMAuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}

	protected AuthorizationManager getAuthorizationManager() {
		return authorizationManager;
	}

	protected void setAuthorizationManager(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}

	protected boolean isCaseSensitive() {
		return caseSensitive;
	}

	protected void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	protected boolean isInstanceLevelSecurity() {
		return instanceLevelSecurity;
	}

	protected void setInstanceLevelSecurity(boolean instanceLevelSecurity) {
		this.instanceLevelSecurity = instanceLevelSecurity;
	}

	protected ORMDAOImpl getOrmDAO() {
		return ormDAO;
	}

	protected void setOrmDAO(ORMDAOImpl ormDAO) {
		this.ormDAO = ormDAO;
	}

	protected int getResultCountPerQuery() {
		return resultCountPerQuery;
	}

	protected void setResultCountPerQuery(int resultCountPerQuery) {
		this.resultCountPerQuery = resultCountPerQuery;
	}

	protected boolean isSecurityEnabledFlag() {
		return securityEnabledFlag;
	}

	protected void setSecurityEnabledFlag(boolean securityEnabledFlag) {
		this.securityEnabledFlag = securityEnabledFlag;
	}
}
