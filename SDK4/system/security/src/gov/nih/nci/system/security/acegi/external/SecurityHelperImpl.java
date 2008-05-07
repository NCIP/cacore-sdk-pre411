package gov.nih.nci.system.security.acegi.external;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;

public class SecurityHelperImpl implements SecurityHelper
{
	private Collection<String> readRoleCollection;
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
		readRoleCollection = new ArrayList<String>();
		readRoleCollection.add("READ");

		allRoleCollection = new ArrayList<String>();
		allRoleCollection.add("*");
	}

	public Map<String, Collection<String>> getPostMethodInvocationSecurityMap(MethodInvocation invocation) {
		String domainObject = getDomainObjectName(invocation);
		
		Map<String, Collection<String>> requiredPrivilageMap = new HashMap<String, Collection<String>>();
		requiredPrivilageMap.put(domainObject, readRoleCollection);
		return requiredPrivilageMap;
	}

	private String getDomainObjectName(MethodInvocation invocation) {
		String domainObjectName="*";
		Method m = getApplicationServiceHelperMethod();
		try {
			if(m!=null)
				domainObjectName = (String)m.invoke(null, invocation);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
		return domainObjectName;
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
		String domainObject = getDomainObjectName(invocation);
		
		Map<String, Collection<String>> requiredPrivilageMap = new HashMap<String, Collection<String>>();
		requiredPrivilageMap.put(domainObject, readRoleCollection);
		return requiredPrivilageMap;
	}

	public Map<String, Collection<String>> getPostMethodInvocationSecurityMap(MethodInvocation arg0, Object arg1) {
		
		return getPreMethodInvocationSecurityMap(arg0);
	}
	
}
