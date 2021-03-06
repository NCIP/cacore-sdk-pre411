/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package gov.nih.nci.system.client.proxy;

import gov.nih.nci.system.applicationservice.ApplicationService;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class ApplicationServiceProxy implements MethodInterceptor
{
	private ProxyHelper proxyHelper;
	
	private ApplicationService as;
	
	private Authentication auth;
	
	public void setProxyFactory(ProxyHelper proxyHelper)
	{
		this.proxyHelper = proxyHelper;
	}
	
	public void setApplicationService (ApplicationService as)
	{
		this.as = as;
	}
	
	public void setAuthentication(Authentication auth) {
		this.auth=auth;
	}
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		if (as == null)
			return invocation.proceed();
		
		SecurityContextHolder.clearContext();
		SecurityContextHolder.getContext().setAuthentication(auth);
		Object value = invocation.proceed();
		SecurityContextHolder.clearContext();
		value = proxyHelper.convertToProxy(as,value);
		return value;
		
	}
}