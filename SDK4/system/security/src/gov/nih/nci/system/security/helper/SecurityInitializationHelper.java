package gov.nih.nci.system.security.helper;

import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.authorization.attributeLevel.AttributeSecuritySessionInterceptor;
import gov.nih.nci.security.authorization.attributeLevel.UserClassAttributeMapCache;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.engine.FilterDefinition;

public class SecurityInitializationHelper 
{
	private AuthorizationManager authorizationManager ;
	private boolean instanceLevelSecurityEnabled;
	private boolean attributeLevelSecurityEnabled;
	private boolean securityEnabled;
	
	public List<FilterDefinition> getFilters()
	{
		if(securityEnabled && (!instanceLevelSecurityEnabled || authorizationManager == null))
			return new ArrayList<FilterDefinition>();
		
		//TODO - Connecto to CSM to get the Filter definitions
		List<FilterDefinition> fdList = new ArrayList<FilterDefinition>();
		return fdList;
	}

	public Interceptor getInterceptor()
	{
		if(securityEnabled && attributeLevelSecurityEnabled)
			return new AttributeSecuritySessionInterceptor(); 
		else
			return EmptyInterceptor.INSTANCE;
	}

	public void enableAttributeLevelSecurity(String userName, SessionFactory sessionFactory) 
	{
		if(attributeLevelSecurityEnabled && authorizationManager != null)
			UserClassAttributeMapCache.setAttributeMap(userName,sessionFactory, authorizationManager);
	}
	
	public AuthorizationManager getAuthorizationManager() {
		return authorizationManager;
	}

	public void setAuthorizationManager(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}

	public boolean isInstanceLevelSecurityEnabled() {
		return instanceLevelSecurityEnabled;
	}

	public void setInstanceLevelSecurityEnabled(boolean instanceLevelSecurityEnabled) {
		this.instanceLevelSecurityEnabled = instanceLevelSecurityEnabled;
	}

	public boolean isAttributeLevelSecurityEnabled() {
		return attributeLevelSecurityEnabled;
	}

	public void setAttributeLevelSecurityEnabled(
			boolean attributeLevelSecurityEnabled) {
		this.attributeLevelSecurityEnabled = attributeLevelSecurityEnabled;
	}

	public boolean isSecurityEnabled() {
		return securityEnabled;
	}

	public void setSecurityEnabled(boolean securityEnabled) {
		this.securityEnabled = securityEnabled;
	}
	
}