/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

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
		String domainClassName = proxyClassName.substring(0, proxyClassName.indexOf('$'));
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
				Method setterMethod = convertedObject.getClass().getMethod("set" + method.getName().substring(3), method.getReturnType());
				log.debug("***  setterMethod Name: " + setterMethod.getName() + "; parameter type: " + method.getReturnType());

				Object value = method.invoke(obj, (Object[])null);
				String valueType = "";
				if (value != null){
					valueType = value.getClass().getName();
				}
				log.debug("***  " +  method.getName().substring(3)+": " + value + "; value type: " + valueType);

				Object[] parameters = new Object[1];
				parameters[0] = value;

				setterMethod.invoke(convertedObject, (Object[])parameters);
			}
		}

		return convertedObject;
	}   

	static public Object deepCopy(Object oldObj) throws Exception
	{

		log.debug("deepCopy called");
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try
		{
			ByteArrayOutputStream bos = 
				new ByteArrayOutputStream(); // A
				oos = new ObjectOutputStream(bos); // B
				// serialize and pass the object
				oos.writeObject(oldObj);   // C
				oos.flush();               // D
				ByteArrayInputStream bin = 
					new ByteArrayInputStream(bos.toByteArray()); // E
				ois = new ObjectInputStream(bin);                  // F
				// return the new object
				return ois.readObject(); // G
		}
		catch(Exception e)
		{
			log.debug("Exception in ObjectCloner = " + e);
			throw(e);
		}
		finally
		{
			oos.close();
			ois.close();
		}
	}
}
