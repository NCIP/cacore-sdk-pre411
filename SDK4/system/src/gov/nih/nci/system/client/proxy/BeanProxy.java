package gov.nih.nci.system.client.proxy;

import java.lang.reflect.Method;

import gov.nih.nci.system.applicationservice.ApplicationService;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

public class BeanProxy implements MethodInterceptor
{
	protected static Logger log = Logger.getLogger(BeanProxy.class.getName());
	
	ApplicationService as;
	ProxyHelper proxyHelper;
	Object domainObj;
	
	public BeanProxy(ApplicationService as, ProxyHelper proxyHelper, Object obj)
	{
		this.as = as;
		this.proxyHelper = proxyHelper;
		this.domainObj = obj;
	}
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
	    if(!proxyHelper.isInitialized(invocation))
	    	return proxyHelper.lazyLoad(as,invocation);
	    else
	    	return proxyHelper.convertToProxy(as,invocation.proceed());
	}
	
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object comparisonObj)
	{
		String thisClassName=this.domainObj.getClass().getName();
		String objClassName=((BeanProxy)comparisonObj).getClassName();

		if(thisClassName.equals(objClassName)){
			if(this.domainObj != null && getId().equals(((BeanProxy)comparisonObj).getId())){
				return true;
			}
		}

		return false;
	}
	
	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		String id=getId();
		if(id != null)
			return id.hashCode();
		return 0;
	}
	
	public String getId()
	{
		try {
			Method getIdMethod = this.domainObj.getClass().getMethod("getId", null);
			String id = ""+(getIdMethod.invoke(this.domainObj, (Object[])null));
			return id;
		} catch (Exception e){
			log.error("Error getting id from object "+this.domainObj.getClass().getName(),e);
			return null;
		}
	}
	
	protected String getClassName(){
		return this.domainObj.getClass().getName();
	}
}