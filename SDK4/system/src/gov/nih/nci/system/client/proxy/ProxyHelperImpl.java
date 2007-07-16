package gov.nih.nci.system.client.proxy;

import gov.nih.nci.system.applicationservice.ApplicationService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    		Object obj = as.getAssociation(createClone(bean),fieldName);
    		Object value = obj;
    		Field field = bean.getClass().getDeclaredField(fieldName);
    		
    		System.out.println("field.getType().getName()"+field.getType().getName());
    		if(!field.getType().getName().equalsIgnoreCase("java.lang.Collection"))
    		{
    			Collection results = (Collection)obj;
    			if(results.size() == 1)
    				value = results.iterator().next();
    			else if(results.size() == 0)
    				value = null;
    			else
    				throw new Exception("Invalid data obtained from the database for the "+fieldName+" attribute of the "+bean.getClass().getName());
    		}
    		

    		Class[] params =  new Class[]{field.getType()};
	    	Method setter = bean.getClass().getDeclaredMethod("set"+method.getName().substring(3),params);
	    	System.out.println("Setter = "+setter.getName());
	    	if(setter != null && params!=null && params.length==1)
	    	{
	    		System.out.println("vale type"+value.getClass().getName());
	    		setter.invoke(bean, new Object[]{value});
	    	}
	    	return value;
	    }
    	
    	return null;
	}
	
	private Object createClone(Object source)
	{
		try 
		{
			Object target = source.getClass().newInstance();
			List<Field> fieldList = new ArrayList<Field>();
			getAllFields(source.getClass(), fieldList);
			for(Field field:fieldList)
			{
				Object obj = field.get(source);
			    if(obj instanceof Integer || obj instanceof Float || obj instanceof Double
			    		|| obj instanceof Character || obj instanceof Long || obj instanceof Boolean
			    		|| obj instanceof String )
			    {
			    	if(!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()))
				    {
				    	field.setAccessible(true);
				    	field.set(target, obj);
			    	}
			    }
			}
			return target;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void getAllFields(Class klass, List<Field> fieldList){

		if ( klass == null || 
				klass.getName().equalsIgnoreCase("java.lang.Object") ||
				klass.isInterface() ||
				klass.isPrimitive()) {
			return; // end of processing
		} 

		getAllFields(klass.getSuperclass(), fieldList);

		Field[] fields = klass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			String fieldName = fields[i].getName();
			if(fieldName.indexOf('$')==-1)
				fieldList.add(fields[i]);
		}	
	}
	
}