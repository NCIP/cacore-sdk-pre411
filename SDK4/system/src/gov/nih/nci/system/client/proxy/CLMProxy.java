package gov.nih.nci.system.client.proxy;

import gov.nih.nci.logging.api.user.UserInfoHelper;

import org.acegisecurity.context.SecurityContextHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class CLMProxy implements MethodInterceptor
{
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if (userName == null)
			userName = "dummy";

		UserInfoHelper.setUserName(userName);
		Object value = invocation.proceed();
		UserInfoHelper.setUserName(null);
		
		return value;
	}
}