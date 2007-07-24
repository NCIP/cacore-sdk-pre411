package gov.nih.nci.system.client.proxy;

import gov.nih.nci.system.applicationservice.ApplicationService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.Hibernate;

public class ProxyHelperImpl implements ProxyHelper 
{
	public Object convertToProxy(ApplicationService as, Object obj) 
	{
		if(obj == null) return null;
		
		if(obj instanceof ListProxy)
			((ListProxy)obj).setAppService(as);
		
		if(obj instanceof java.util.Collection)
			return convertCollectionToProxy(as,(Collection)obj);
		else if(obj instanceof Object[])
			return convertArrayToProxy(as,(Object[])obj);
		else
		   	return converObjectToProxy(as,obj);
	}

	protected Object converObjectToProxy(ApplicationService as, Object obj) 
	{
		if(null == obj) return null;
    	if(obj instanceof Integer || obj instanceof Float || obj instanceof Double
    			|| obj instanceof Character || obj instanceof Long || obj instanceof Boolean
    			|| obj instanceof String || obj instanceof Date || obj instanceof BeanProxy)
    		return obj;

    	org.springframework.aop.framework.ProxyFactory pf = new org.springframework.aop.framework.ProxyFactory();
        pf.addAdvice(new BeanProxy(as, this));
        pf.setTarget(obj);
	    
		return pf.getProxy();		
	}

	protected Object convertCollectionToProxy(ApplicationService as, Collection collection) {
		if(null == collection) return null;
		Collection<Object> modifiedCollection =  new ArrayList<Object>();
		for(Object obj:collection)
			modifiedCollection.add(convertToProxy(as, obj));
		return modifiedCollection;
	}

	protected Object convertArrayToProxy(ApplicationService as, Object[] objects) {
		if(null == objects) return null;
		if(objects.length==0) return objects;
		Object[] modifiedCollection =  new Object[objects.length];
		for(int i=0;i<objects.length;i++)
			modifiedCollection[i] = convertToProxy(as, objects[i]);
		return modifiedCollection;
	}
	
	public boolean isInitialized(MethodInvocation invocation) throws Throwable {
	    Object retVal = invocation.proceed();
		return Hibernate.isInitialized(retVal);
	}
	
	public Object lazyLoad(ApplicationService as, MethodInvocation invocation) throws Throwable{
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
    		
    		Field field = getField(bean, fieldName); 

    		if(obj instanceof ListProxy)
    			((ListProxy)obj).setAppService(as);
    			
    		if(!field.getType().getName().equalsIgnoreCase("java.util.Collection"))
    		{
    			Class<?> x = field.getType();
    			Collection results = (Collection)obj;
    			if(results.size() == 1)
    				value = results.iterator().next();
    			else if(results.size() == 0)
    				value = null;
    			else
    				throw new Exception("Invalid data obtained from the database for the "+fieldName+" attribute of the "+bean.getClass().getName());
    		}

    		Class[] params =  new Class[]{field.getType()};
	    	Method setter = getMethod(bean,"set"+method.getName().substring(3), params);
	    	if(setter != null && params!=null && params.length==1)
	    		setter.invoke(bean, new Object[]{value});

	    	return value;
	    }
    	
    	return null;
	}
	
	protected Method getMethod(Object bean, String methodName, Class[] params) {
		Method method = null;
		if(bean == null) return null;
		
		Class klass = bean.getClass();
		while(klass!=null && klass!= Object.class)
		{
			try {
				method = klass.getDeclaredMethod(methodName,params);
			} catch (SecurityException e) {
			} catch (NoSuchMethodException e) {
			}
			if(method!=null) 
				break;;
			klass = klass.getSuperclass();
		}
		if(method==null) 
			System.out.println("Error:");
		return method;
	}

	protected Field getField(Object bean, String fieldName) {
		Field field = null;
		if(bean == null) return null;
		
		Class klass = bean.getClass();
		while(klass!=null && klass!= Object.class)
		{
			try {
				field = klass.getDeclaredField(fieldName);
			} catch (SecurityException e) {
			} catch (NoSuchFieldException e) {
			}
			if(field!=null) 
				break;;
			klass = klass.getSuperclass();
		}
		if(field==null) 
			System.out.println("Error:");
		return field;
	}

	protected Object createClone(Object source)
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
	
	protected void getAllFields(Class klass, List<Field> fieldList){

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