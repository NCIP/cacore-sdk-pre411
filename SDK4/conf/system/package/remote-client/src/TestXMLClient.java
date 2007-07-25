import gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Bank;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.client.util.xml.Marshaller;
import gov.nih.nci.system.client.util.xml.Unmarshaller;
import gov.nih.nci.system.client.util.xml.XMLUtility;
import gov.nih.nci.system.client.util.xml.caCOREMarshaller;
import gov.nih.nci.system.client.util.xml.caCOREUnmarshaller;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.Collection;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;


public class TestXMLClient extends TestClient
{
	public static void main(String args[])
	{
		TestXMLClient client = new TestXMLClient();
		try
		{
			client.testXMLUtility();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void testXMLUtility() throws Exception
	{
		ApplicationService appService = ApplicationServiceProvider.getApplicationService();
		Collection<Class> classList = getClasses();
		
		Marshaller marshaller = new caCOREMarshaller("xml-mapping.xml", false);
		Unmarshaller unmarshaller = new caCOREUnmarshaller("unmarshaller-xml-mapping.xml", false);		
		XMLUtility myUtil = new XMLUtility(marshaller, unmarshaller);
		for(Class klass:classList)
		{
			Object o = klass.newInstance();
			System.out.println("Searching for "+klass.getName());
			try
			{
				Collection results = appService.search(klass, o);
				for(Object obj : results)
				{
					File myFile = new File("./output/" + klass.getName() + "_test.xml");						

					FileWriter myWriter = new FileWriter(myFile);
					myUtil.toXML(convertObject(obj, klass), myWriter);
					myWriter.close();
					printObject(obj, klass);					
					DocumentBuilder parser = DocumentBuilderFactory
							.newInstance().newDocumentBuilder();
					Document document = parser.parse(myFile);
					SchemaFactory factory = SchemaFactory
							.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
					
					Source schemaFile = new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream(klass.getPackage().getName() + ".xsd"));
					Schema schema = factory.newSchema(schemaFile);
					Validator validator = schema.newValidator();
					System.out.println("Validating " + klass.getName() + " against the schema......\n\n");
					validator.validate(new DOMSource(document));
					System.out.println(klass.getName() + " has been validated!!!\n\n");
					Object myObj = (Object) myUtil.fromXML(myFile);	
					
					printObject(myObj, klass);
					myWriter.close();
					break;
				}
			}catch(Exception e)
			{
				System.out.println("Exception caught: " + e.getMessage());
				e.printStackTrace();
			}
			break;
		}
	}
	
	
	private Object convertObject(Object obj, Class klass) throws Exception {
//		System.out.println("Converting from proxy object: "+ obj.getClass().getName());
		
		Object convertedObject = klass.newInstance();
		
		Method[] methods = klass.getMethods();
		for(Method method:methods)
		{
			if(method.getName().startsWith("get") && !method.getName().equals("getClass"))
			{
				Method setterMethod = convertedObject.getClass().getDeclaredMethod("set" + method.getName().substring(3), method.getReturnType());
//				System.out.println("setterMethod Name: " + setterMethod.getName() + "; parameter type: " + method.getReturnType());
				
				Object val = method.invoke(obj, (Object[])null);
//				System.out.println(method.getName().substring(3)+": " + val + "; value type: " + val.getClass().getName());

				Object[] parameters = new Object[1];
				parameters[0] = val;

				setterMethod.invoke(convertedObject, (Object[])parameters);
			}
		}
		
		return convertedObject;
	}
}