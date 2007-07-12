package gov.nih.nci.system.client.proxy;

import gov.nih.nci.system.applicationservice.ApplicationService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.Hibernate;

public class ProxyHelperImpl implements ProxyHelper 
{
	public Object convertToProxy(ApplicationService as, Object obj) 
	{
		  if(obj instanceof java.util.Collection)
		    	return convertToProxy(as,(Collection)obj);
		    else
		    	return converToProxy(as,obj);
	}

	protected Object converToProxy(ApplicationService as, Object obj) 
	{
		if(null == obj) return null;
    	if(obj instanceof Integer || obj instanceof Float || obj instanceof Double
    			|| obj instanceof Character || obj instanceof Long || obj instanceof Boolean
    			|| obj instanceof String || obj instanceof BeanProxy)
    		return obj;

    	org.springframework.aop.framework.ProxyFactory pf = new org.springframework.aop.framework.ProxyFactory();
        pf.addAdvice(new BeanProxy(as, this));
        pf.setTarget(obj);
	    
		return pf.getProxy();		
	}

	protected Object convertToProxy(ApplicationService as, Collection collection) {
		if(null == collection) return null;
		Collection<Object> modifiedCollection =  new ArrayList<Object>();
		for(Object obj:collection)
			modifiedCollection.add(converToProxy(as, obj));
		return modifiedCollection;
	}

	public boolean isInitialized(MethodInvocation invocation) throws Throwable {
	    Object retVal = invocation.proceed();
		return Hibernate.isInitialized(retVal);
	}
	
	public Object lazyLoad(ApplicationService as, MethodInvocation invocation) throws Throwable{
		System.out.println("Initializing Proxy");
	    Object bean = invocation.getThis();
	    Method method = invocation.getMethod();
	    String methodName = method.getName();
	    Object args[] = invocation.getArguments();
    	if(methodName.startsWith("get") && (args == null || args.length == 0))
    	{
        	String fieldName = methodName.substring(3);
    		fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
    		Object obj = as.getAssociation(bean,fieldName);
    		
    		Field field = bean.getClass().getDeclaredField(fieldName);

    		Class[] params =  new Class[]{field.getType()};
	    	Method setter = bean.getClass().getDeclaredMethod("set"+method.getName().substring(3),params);
	    	if(setter != null)
	    		setter.invoke(bean, new Object[]{obj});
	    	
	    	return obj;
	    }
    	
    	return null;
	}
	
}