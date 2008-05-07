package gov.nih.nci.system.dao.orm;

import gov.nih.nci.system.security.helper.SecurityInitializationHelper;

import org.acegisecurity.context.SecurityContextHolder;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class CSMFilterParameterSetter implements FilterParameterSetter
{
	private Long applicationId ;
	private SecurityInitializationHelper securityHelper;
	public CSMFilterParameterSetter(SecurityInitializationHelper securityHelper)
	{
		this.securityHelper = securityHelper;
		applicationId = securityHelper.getAuthorizationManager().getApplicationContext().getApplicationId();
	}
	
	public void setParameters(Filter filter) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		filter.setParameter("USER_NAME", userName);
		filter.setParameter("APPLICATION_ID", applicationId);
	}

	public void performPostProcessing(SessionFactory sf, Session s) {
		//NOOP.
	}

	public void performPreProcessing(SessionFactory sf, Session s) {
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		securityHelper.enableAttributeLevelSecurity(userName,sf);
	}

	public boolean getApplyFilters() {
		return securityHelper.isSecurityEnabled() && securityHelper.getAuthorizationManager()!=null && securityHelper.isInstanceLevelSecurityEnabled();
	}
}
