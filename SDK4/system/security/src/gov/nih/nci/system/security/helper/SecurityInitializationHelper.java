package gov.nih.nci.system.security.helper;

import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.authorization.attributeLevel.UserClassAttributeMapCache;
import gov.nih.nci.security.authorization.instancelevel.InstanceLevelSecurityHelper;
import gov.nih.nci.security.exceptions.CSException;

import java.util.ArrayList;
import java.util.List;

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
		if(securityEnabled && instanceLevelSecurityEnabled && authorizationManager != null){
			 try {
				return InstanceLevelSecurityHelper.getFiltersForUser(authorizationManager);
			} catch (CSException e) {
				// TODO Add Error Handling
				e.printStackTrace();
			}
		}
		List<FilterDefinition> fdList = new ArrayList<FilterDefinition>();
		return fdList;
	}

	public void enableAttributeLevelSecurity(String userName, SessionFactory sessionFactory) 
	{
		if(securityEnabled && attributeLevelSecurityEnabled && authorizationManager != null)
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