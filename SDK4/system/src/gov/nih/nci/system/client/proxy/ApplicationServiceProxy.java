package gov.nih.nci.system.client.proxy;

import gov.nih.nci.system.applicationservice.ApplicationService;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class ApplicationServiceProxy implements MethodInterceptor
{
	private ProxyHelper proxyHelper;
	
	public void setProxyFactory(ProxyHelper proxyHelper)
	{
		this.proxyHelper = proxyHelper;
	}
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		return proxyHelper.convertToProxy((ApplicationService)invocation.getThis(),invocation.proceed());

	}
}