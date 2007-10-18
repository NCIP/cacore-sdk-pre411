package gov.nih.nci.system.client.util.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.exolab.castor.mapping.GeneralizedFieldHandler;

/**
 * The FieldHandler for the Date class
 *
 */
public abstract class BaseCastorFieldHandler
extends GeneralizedFieldHandler
{

	private static Logger log = Logger.getLogger(BaseCastorFieldHandler.class);

	static public Object convertObject(Object oldObj) throws Exception
	{
		String proxyClassName = oldObj.getClass().getName();
		String domainClassName;
		if (proxyClassName.indexOf('$') > 0) {
			domainClassName = proxyClassName.substring(0, proxyClassName.indexOf('$'));
		} else {
			domainClassName = proxyClassName;
		}

		log.debug("domainClassName: " + domainClassName);

		Object convertedObj =  convertObject(oldObj, Class.forName(domainClassName));

		return convertedObj;

	}

	private static Object convertObject(Object obj, Class klass) throws Exception {
		log.debug("***  Converting from proxy object: " + obj.getClass().getName());

		Object convertedObject = klass.newInstance();

		Method[] methods = klass.getMethods();
		for(Method method:methods)
		{
			if(method.getName().startsWith("get") && !method.getName().equals("getClass"))
			{
				try {
					Method setterMethod = convertedObject.getClass().getMethod("set" + method.getName().substring(3), method.getReturnType());
					log.debug("***  setterMethod Name: " + setterMethod.getName() + "; parameter type: " + method.getReturnType());
	
					Object value = method.invoke(obj, (Object[])null);
	
					Object[] parameters = new Object[1];
					parameters[0] = value;
	
					setterMethod.invoke(convertedObject, (Object[])parameters);
				} catch (NoSuchMethodException e){
					//ignore - E.g., Strings have getChars(), getBytes() methods with no corresponding Setters
				}
			}
		}

		return convertedObject;
	}   
}
