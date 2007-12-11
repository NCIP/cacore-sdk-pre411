package test.gov.nih.nci.cacoresdk;

import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import junit.framework.TestCase;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

/**
 * @author Dan Dumitru
 */
public abstract class SDKWSTestBase extends TestCase {


	private String url = "http://localhost:8080/example/services/exampleService";

	protected void setUp() throws Exception {
		super.setUp();
	}

	
	protected abstract Collection<Class> getClasses() throws Exception;

	protected void tearDown() throws Exception 
	{
		super.tearDown();
	}
	
	protected Object[] getQueryObjectResults(Class targetClass, Object criteria) throws Exception
	{
		Service service = new Service();
		Call call = (Call) service.createCall();

		QName searchClassQName = new QName("urn:"+getInversePackageName(criteria.getClass()), criteria.getClass().getSimpleName());
		
		call.setTargetEndpointAddress(new java.net.URL(url));
		call.setOperationName(new QName("exampleService", "queryObject"));
		call.addParameter("arg1", org.apache.axis.encoding.XMLType.XSD_STRING, ParameterMode.IN);
		call.addParameter("arg2", searchClassQName, ParameterMode.IN);
		call.setReturnType(org.apache.axis.encoding.XMLType.SOAP_ARRAY);

		Collection<Class> classList = getClasses();

		for(Class klassToMap:classList)
		{
			QName searchClassQNameToMap = new QName("urn:"+getInversePackageName(klassToMap), klassToMap.getSimpleName());
			call.registerTypeMapping(klassToMap, searchClassQNameToMap,
					new org.apache.axis.encoding.ser.BeanSerializerFactory(klassToMap, searchClassQNameToMap),
					new org.apache.axis.encoding.ser.BeanDeserializerFactory(klassToMap, searchClassQNameToMap));
		}
		
		Object[] results = (Object[])call.invoke(new Object[] { targetClass.getName(), criteria });
		
		return results;
		
	}
	
	protected Object[] getAssociationResults(Object source, String associationName, int startIndex) throws Exception{
		
		Service service = new Service();
		Call call = (Call) service.createCall();

		QName searchClassQName = new QName("urn:"+getInversePackageName(source.getClass()), source.getClass().getSimpleName());
		
		call.setTargetEndpointAddress(new java.net.URL(url));
		call.setOperationName(new QName("exampleService", "getAssociation"));
		call.addParameter("arg1", searchClassQName, ParameterMode.IN);					
		call.addParameter("arg2", org.apache.axis.encoding.XMLType.XSD_STRING, ParameterMode.IN);
		call.addParameter("arg3", searchClassQName, ParameterMode.IN);
		call.setReturnType(org.apache.axis.encoding.XMLType.SOAP_ARRAY);

		Collection<Class> classList = getClasses();

		for(Class klassToMap:classList)
		{
			QName searchClassQNameToMap = new QName("urn:"+getInversePackageName(klassToMap), klassToMap.getSimpleName());
			call.registerTypeMapping(klassToMap, searchClassQNameToMap,
					new org.apache.axis.encoding.ser.BeanSerializerFactory(klassToMap, searchClassQNameToMap),
					new org.apache.axis.encoding.ser.BeanDeserializerFactory(klassToMap, searchClassQNameToMap));
		}

		Object[] results = (Object[])call.invoke(new Object[] { source, associationName, startIndex });
		
		return results;		
				
	}
	
	protected Object getTotalNumberOfRecords(String targetClassName, Object criteria) throws Exception{
		Service service = new Service();
		Call call = (Call) service.createCall();

		QName searchClassQName = new QName("urn:"+getInversePackageName(Class.forName(targetClassName)), targetClassName);
		
		call.setTargetEndpointAddress(new java.net.URL(url));
		call.setOperationName(new QName("exampleService", "getTotalNumberOfRecords"));
		call.addParameter("arg1", org.apache.axis.encoding.XMLType.XSD_STRING, ParameterMode.IN);
		call.addParameter("arg2", searchClassQName, ParameterMode.IN);
		call.setReturnType(org.apache.axis.encoding.XMLType.XSD_INT);
		
		Collection<Class> classList = getClasses();

		for(Class klassToMap:classList)
		{
			QName searchClassQNameToMap = new QName("urn:"+getInversePackageName(klassToMap), klassToMap.getSimpleName());
			call.registerTypeMapping(klassToMap, searchClassQNameToMap,
					new org.apache.axis.encoding.ser.BeanSerializerFactory(klassToMap, searchClassQNameToMap),
					new org.apache.axis.encoding.ser.BeanDeserializerFactory(klassToMap, searchClassQNameToMap));
		}	
		
		Object totalNumberOfRecords = (Object)call.invoke(new Object[] { targetClassName, criteria });
//		System.out.println("totalNumberOfRecords: " + totalNumberOfRecords);
		
		return totalNumberOfRecords;
	}

	protected String getVersion() throws Exception{
		Service service = new Service();
		
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new java.net.URL(url));
		call.setOperationName(new QName("exampleService", "getVersion"));
		call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);

		String version = (String)call.invoke(new Object[] {});
		
		return version;
    }
    
	protected Object getRecordsPerQuery() throws Exception {
		Service service = new Service();
		
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new java.net.URL(url));
		call.setOperationName(new QName("exampleService", "getRecordsPerQuery"));
		call.setReturnType(org.apache.axis.encoding.XMLType.XSD_INT);

		Object recordsPerQuery = (Object)call.invoke(new Object[] {});
//		System.out.println("recordsPerQuery: " + recordsPerQuery);
		
		return recordsPerQuery;
    }
    
	
    
	protected Object getMaximumRecordsPerQuery() throws Exception {
		Service service = new Service();
		
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new java.net.URL(url));
		call.setOperationName(new QName("exampleService", "getMaximumRecordsPerQuery"));
		call.setReturnType(org.apache.axis.encoding.XMLType.XSD_INT);

		Object getMaximumRecordsPerQuery = (Object)call.invoke(new Object[] {});
//		System.out.println("getMaximumRecordsPerQuery: " + getMaximumRecordsPerQuery);
		
		return getMaximumRecordsPerQuery;
    }
	
	protected static String getTestCaseName()
	{
		return "SDK Base WS Test Case";
	}
	
	protected String getInversePackageName(Class klass)
	{
		String name1 = klass.getPackage().getName();
		String[] tokens = name1.split("[.]");
		String newName = "";
		for(int i=0;i<tokens.length;i++)
			newName+="."+tokens[tokens.length-i-1];
		newName = newName.substring(1);
		
//		System.out.println("InversePackageName: " + newName);
		return newName;
	}

}
