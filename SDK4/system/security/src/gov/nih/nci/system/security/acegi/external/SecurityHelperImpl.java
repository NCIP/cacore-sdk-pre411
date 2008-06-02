package gov.nih.nci.system.security.acegi.external;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;

public class SecurityHelperImpl implements SecurityHelper
{
	private Collection<String> allRoleCollection;
	private Class applicationServiceMethodHelperClass;
	private Method applicationServiceHelperMethod;
	
	public Class getApplicationServiceMethodHelperClass() {
		return applicationServiceMethodHelperClass;
	}

	public void setApplicationServiceMethodHelperClass(
			Class applicationServiceMethodHelperClass) {
		this.applicationServiceMethodHelperClass = applicationServiceMethodHelperClass;
	}

	public SecurityHelperImpl()
	{
		allRoleCollection = new ArrayList<String>();
		allRoleCollection.add("*");
	}

	public Map<String, Collection<String>> getPostMethodInvocationSecurityMap(MethodInvocation invocation) {
		Map<String, Collection<String>> requiredPrivilageMap  = getDomainObjectName(invocation);
		return requiredPrivilageMap;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Collection<String>> getDomainObjectName(MethodInvocation invocation) {
		//@TODO String domainObjectName="*";  needs refactoring later ?
		Map<String, Collection<String>> requiredPrivilageMap = new HashMap<String, Collection<String>>();
		Method m = getApplicationServiceHelperMethod();
		try {
			if (m != null)
				requiredPrivilageMap = (Map) m.invoke(applicationServiceMethodHelperClass.newInstance(),invocation);
		} catch (Exception e) {
			throw new RuntimeException("error in getDomainObjectName reflection invocation ", e);
		}
		return requiredPrivilageMap;
	}

	private Method getApplicationServiceHelperMethod() {
		if(applicationServiceHelperMethod == null)
		{
			try 
			{
				Class[] params = new Class[] {MethodInvocation.class};
				applicationServiceHelperMethod =  applicationServiceMethodHelperClass.getMethod("getDomainObjectName", params);
			} 
			catch (SecurityException e) {} 
			catch (NoSuchMethodException e){}
		}
		return applicationServiceHelperMethod;
	}

	public Map<String, Collection<String>> getPreMethodInvocationSecurityMap(MethodInvocation invocation) {
		Map<String, Collection<String>> requiredPrivilageMap  = getDomainObjectName(invocation);
		return requiredPrivilageMap;
	}

	public Map<String, Collection<String>> getPostMethodInvocationSecurityMap(MethodInvocation arg0, Object arg1) {
		
		return getPreMethodInvocationSecurityMap(arg0);
	}
	
}
