import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;


import java.net.URL;
import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.utils.Options;

public class TestClient
{
	public static void main(String args[])
	{
		TestClient client = new TestClient();
		try
		{
			client.testSearch();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void testSearch() throws Exception
	{
		Collection<Class> classList = getClasses();
		
		String url = "http://localhost:8080/example/services/exampleService";
		Service  service = new Service();
		Call call = null;
		
			
		for(Class klass:classList)
		{
			try 
			{
				call = (Call) service.createCall();
				QName searchClassQName = new QName("urn:"+getInversePackageName(klass), klass.getSimpleName());
				call.registerTypeMapping(klass, searchClassQName,
						new org.apache.axis.encoding.ser.BeanSerializerFactory(klass, searchClassQName),
						new org.apache.axis.encoding.ser.BeanDeserializerFactory(klass, searchClassQName));
	
				call.setTargetEndpointAddress(new java.net.URL(url));
				call.setOperationName(new QName("exampleService", "queryObject"));
				call.addParameter("arg1", org.apache.axis.encoding.XMLType.XSD_STRING, ParameterMode.IN);
				call.addParameter("arg2", searchClassQName, ParameterMode.IN);
				call.setReturnType(org.apache.axis.encoding.XMLType.SOAP_ARRAY);
	
				Object o = klass.newInstance();
	
				System.out.println("Searching for "+klass.getName());
				Object[] results = (Object[])call.invoke(new Object[] { klass.getName(), o });
	
				if (results!=null &&  results.length> 0) {
					for(Object obj : results)
					{
						printObject(obj, klass);
						break;
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	
	private String getInversePackageName(Class klass)
	{
		String name1 = klass.getPackage().getName();
		String[] tokens = name1.split("[.]");
		String newName = "";
		for(int i=0;i<tokens.length;i++)
			newName+="."+tokens[tokens.length-i-1];
		newName = newName.substring(1);
		return newName;
	}
	
	private void printObject(Object obj, Class klass) throws Exception {
		System.out.println("Printing "+ obj.getClass().getName());
		Method[] methods = klass.getMethods();
		for(Method method:methods)
		{
			if(method.getName().startsWith("get") && !method.getName().equals("getClass"))
			{
				System.out.print("\t"+method.getName().substring(3)+":");
				Object val = method.invoke(obj, (Object[])null);
				if(val instanceof java.util.Set)
					System.out.println("size="+((Collection)val).size());
				else
					System.out.println(val);
			}
		}
	}
	

	protected Collection<Class> getClasses() throws Exception
	{
		Collection<Class> list = new ArrayList<Class>();
		JarFile file = null;
		int count = 0;
		for(File f:new File("lib").listFiles())
		{
			if(f.getName().endsWith("-beans.jar"))
			{
				file = new JarFile(f);
				count++;
			}
		}
		if(file == null) throw new Exception("Could not locate the bean jar");
		if(count>1) throw new Exception("Found more than one bean jar");
		
		Enumeration e = file.entries();
		while(e.hasMoreElements())
		{
			JarEntry o = (JarEntry) e.nextElement();
			if(!o.isDirectory())
			{
				String name = o.getName();
				if(name.endsWith(".class"))
				{
					String klassName = name.replace('/', '.').substring(0, name.lastIndexOf('.'));
					list.add(Class.forName(klassName));
				}
			}
		}
		return list;
	}	
}