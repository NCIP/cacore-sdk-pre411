package gov.nih.nci.system.security.helper;

import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.authorization.attributeLevel.UserClassAttributeMapCache;
import gov.nih.nci.security.authorization.instancelevel.InstanceLevelSecurityHelper;
import gov.nih.nci.system.security.acegi.GroupNameAuthenticationToken;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SecurityInitializationHelper 
{
	private AuthorizationManager authorizationManager ;
	private boolean groupLevelSecurityEnabled;
	private boolean instanceLevelSecurityEnabled;
	private boolean attributeLevelSecurityEnabled;
	private boolean securityEnabled;

	public void addFilters(Configuration cfg) {
		if(securityEnabled && instanceLevelSecurityEnabled && authorizationManager!=null)
		{
			if(groupLevelSecurityEnabled)
				InstanceLevelSecurityHelper.addFiltersForGroups(authorizationManager, cfg);
			else
				InstanceLevelSecurityHelper.addFilters(authorizationManager, cfg);
		}
	}

	public void initializeFilters(Session session) {
		if(securityEnabled && instanceLevelSecurityEnabled && authorizationManager!=null)
		{
			if(SecurityContextHolder.getContext()!=null)
			{
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if(auth !=null && auth instanceof GroupNameAuthenticationToken)
				{
					GroupNameAuthenticationToken groupAuth = (GroupNameAuthenticationToken)auth;
					InstanceLevelSecurityHelper.initializeFiltersForGroups((String[])(groupAuth.getGroups().toArray()),session,authorizationManager);					
				}
				else if (auth!=null)
				{
					InstanceLevelSecurityHelper.initializeFilters(auth.getName(), session, authorizationManager);					
				}
			}
		}
	}
	
	public void enableAttributeLevelSecurity(SessionFactory sessionFactory) 
	{
		if(securityEnabled && attributeLevelSecurityEnabled && authorizationManager != null)
		{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth !=null && auth instanceof GroupNameAuthenticationToken)
			{
				GroupNameAuthenticationToken groupAuth = (GroupNameAuthenticationToken)auth;		
				UserClassAttributeMapCache.setAttributeMapForGroup((String[])(groupAuth.getGroups().toArray()), sessionFactory, authorizationManager);	
			}
			else if (auth!=null)
			{
				UserClassAttributeMapCache.setAttributeMap(auth.getName(),sessionFactory, authorizationManager);
			}
		}
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

	public void setAttributeLevelSecurityEnabled(boolean attributeLevelSecurityEnabled) {
		this.attributeLevelSecurityEnabled = attributeLevelSecurityEnabled;
	}

	public boolean isSecurityEnabled() {
		return securityEnabled;
	}

	public void setSecurityEnabled(boolean securityEnabled) {
		this.securityEnabled = securityEnabled;
	}

	public boolean isGroupLevelSecurityEnabled() {
		return groupLevelSecurityEnabled;
	}

	public void setGroupLevelSecurityEnabled(boolean groupLevelSecurityEnabled) {
		this.groupLevelSecurityEnabled = groupLevelSecurityEnabled;
	}
	
}