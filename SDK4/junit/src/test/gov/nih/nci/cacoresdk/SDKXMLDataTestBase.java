/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.client.util.xml.Marshaller;
import gov.nih.nci.system.client.util.xml.Unmarshaller;
import gov.nih.nci.system.client.util.xml.XMLUtility;
import gov.nih.nci.system.client.util.xml.caCOREMarshaller;
import gov.nih.nci.system.client.util.xml.caCOREUnmarshaller;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import junit.framework.TestCase;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Document;

/**
 * @author Dan Dumitru
 * 
 */
public abstract class SDKXMLDataTestBase extends TestCase {

	private String uriPrefix = "gme://caCORE.caCORE/3.2/";
	private String filepathPrefix  = "./output/";
	private String filepathSuffix  = "_test.xml";
	private ApplicationService appService;
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;
	private XMLUtility myUtil;

	protected void setUp() throws Exception {
		super.setUp();
		appService = ApplicationServiceProvider.getApplicationService();
//		String url = "http://localhost:8080/example";
//		appService = ApplicationServiceProvider.getApplicationServiceFromUrl(url);
		
		marshaller = new caCOREMarshaller("xml-mapping.xml", false);
		unmarshaller = new caCOREUnmarshaller("unmarshaller-xml-mapping.xml", false);		
		myUtil = new XMLUtility(marshaller, unmarshaller);
	}


	protected void tearDown() throws Exception 
	{
		appService = null;
		super.tearDown();
	}
	
	protected ApplicationService getApplicationService()
	{
		return appService;
	}
	
	public static String getTestCaseName()
	{
		return "SDK Base Test Case";
	}
	
	protected void toXML(Object resultObj) throws Exception {
		File myFile = new File(filepathPrefix + resultObj.getClass().getSimpleName() + filepathSuffix);						

		FileWriter myWriter = new FileWriter(myFile);
		
		myUtil.toXML(resultObj, myWriter);
		myWriter.close();
	}
	
	protected Object fromXML(Object resultObj) throws Exception {
		File myFile = new File(filepathPrefix + resultObj.getClass().getSimpleName() + filepathSuffix);
		
		Object myObj = (Object) myUtil.fromXML(myFile);
		
		return myObj;
	}
	
	protected org.jdom.Document getDocument(String filename) {
		org.jdom.Document doc;
		try {
			SAXBuilder builder = new SAXBuilder();
			doc = builder.build(filename);
		} catch (Exception ex) {
			throw new RuntimeException("Error reading XML Data file: " + filename,ex);
		}

		return doc;
	}
	
	protected boolean validateXMLData(Object resultObj, Class klass) throws Exception {
		File myFile = new File(filepathPrefix + resultObj.getClass().getSimpleName() + filepathSuffix);

		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = parser.parse(myFile);
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		try {
//			System.out.println("Validating " + klass.getName() + " against the schema......\n\n");						
			Source schemaFile = new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream(klass.getPackage().getName() + ".xsd"));
			Schema schema = factory.newSchema(schemaFile);
			Validator validator = schema.newValidator();

			validator.validate(new DOMSource(document));
//			System.out.println(klass.getName() + " has been validated!!!\n\n");
		} catch (Exception e) {
//			System.out.println(klass.getName() + " has failed validation!!!  Error reason is: \n\n" + e.getMessage());
			return false;
		}
		
		return true;
	}
	
//  TODO :: figure out how to get xpath to work when the XML Data file does not contain a Namespace prefix
//	protected List<Element> queryXMLData(Object obj, String xpath)
//	throws JaxenException {
//
//		// e.g.: xpath = "/*[local-name()='schema']";
//		JDOMXPath path = new JDOMXPath(xpath);
//		path.addNamespace("", "gme://caCORE.caCORE/3.2/gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation");
////		System.out.println("path: " + path.toString());
//		List<Element> elts = path.selectNodes(obj);
//
//		// System.out.println("Elements Found: " + elts.size());
//		return elts;
//
//	}
	
	/**
	 * Uses xpath to query the generated XSD Verifies that common elements
	 * attributes(name, type) are present Verifies that the element 'name'
	 * attribute matches the class name
	 * 
	 * @throws Exception
	 */
	protected void validateClassElements(Object resultObj)
	throws Exception {
		
//		System.out.println("Validating Class Elements from: " + filepathPrefix + resultObj.getClass().getSimpleName() + filepathSuffix);
		
		String proxyClassName = resultObj.getClass().getName();
		String klassName;
		if (proxyClassName.indexOf('$') > 0) {
			klassName = proxyClassName.substring(0, proxyClassName.indexOf('$'));
		} else {
			klassName = proxyClassName;
		}
		
		org.jdom.Document doc = getDocument(filepathPrefix + resultObj.getClass().getSimpleName() + filepathSuffix);
		
//		System.out.println(doc.toString());
//		System.out.println("doc.getBaseURI(): " + doc.getBaseURI());
//		System.out.println("doc.getName():" + doc.getRootElement().getName());
//		System.out.println("doc.getNamespace():" + doc.getRootElement().getNamespace());		
//		System.out.println("doc.getNamespacePrefix():" + doc.getRootElement().getNamespacePrefix());
//		System.out.println("doc.getNamespaceURI():" + doc.getRootElement().getNamespaceURI());
//
//		String xpath = "" + Class.forName(klassName).getSimpleName();
//		System.out.println("xpath: " + xpath);

//		List<Element> elts = queryXMLData(doc, xpath);
//		assertEquals(1, elts.size());
		Element klassElt = doc.getRootElement();
		
		assertEquals(Class.forName(klassName).getSimpleName(), klassElt.getName());
		assertEquals(klassElt.getNamespaceURI(),uriPrefix + Class.forName(klassName).getPackage().getName());
		
//		System.out.println("klassElt.getAttributeValue('id'):" + klassElt.getAttributeValue("id"));
//		System.out.println("klassElt.getAttributeValue('amount'):" + klassElt.getAttributeValue("amount"));

//		TODO :: figure out how to get xpath to work when the XML Data file does not contain a Namespace prefix
//		xpath = "/mapping/class[@name='" + klass.getName() + "']"
//			+ "/map-to[@xml='" + klass.getSimpleName() + "']";		
//
//		elts = queryXMLData(doc, xpath);
//		assertEquals(1, elts.size());
//		Element mapToElt = elts.get(0);
//
//		
//		assertEquals(3, mapToElt.getAttributes().size());//
//		assertTrue(mapToElt.getAttributeValue("xmlns").equals(uriPrefix + klass.getPackage().getName()));
	}
	
	/**
	 * Uses xpath to query the generated XSD Verifies that common elements
	 * attributes(name, type) are present Verifies that the element 'name'
	 * attribute matches the class name
	 * 
	 * @throws Exception
	 */
	protected void validateAttribute(Object resultObj, String attributeName, Object attributeValue)
	throws Exception {
		
//		System.out.println("Validating Class attributes from: " + filepathPrefix + resultObj.getClass().getSimpleName() + filepathSuffix);

		org.jdom.Document doc = getDocument(filepathPrefix + resultObj.getClass().getSimpleName() + filepathSuffix);
//		TODO :: figure out how to get xpath to work when the XML Data file does not contain a Namespace prefix
//		String xpath = "" + Class.forName(klassName).getSimpleName();
//		System.out.println("xpath: " + xpath);

//		List<Element> elts = queryXMLData(doc, xpath);
//		assertEquals(1, elts.size());
		Element klassElt = doc.getRootElement();
		
		assertEquals(klassElt.getAttributeValue(attributeName),attributeValue.toString());
	}
	
	/**
	 * Uses xpath to query the generated XSD Verifies that common elements
	 * attributes(name, type) are present Verifies that the element 'name'
	 * attribute matches the class name
	 * 
	 * @throws Exception
	 */
	protected void validateDateAttribute(Object resultObj, String attributeName, Date attributeValue)
	throws Exception {
		
//		System.out.println("Validating Class attributes from: " + filepathPrefix + resultObj.getClass().getSimpleName() + filepathSuffix);

		org.jdom.Document doc = getDocument(filepathPrefix + resultObj.getClass().getSimpleName() + filepathSuffix);
//		TODO :: figure out how to get xpath to work when the XML Data file does not contain a Namespace prefix
//		String xpath = "" + Class.forName(klassName).getSimpleName();
//		System.out.println("xpath: " + xpath);

//		List<Element> elts = queryXMLData(doc, xpath);
//		assertEquals(1, elts.size());
		Element klassElt = doc.getRootElement();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.S"); //e.g.:  2007-10-01T00:00:00.000-07:00
		String attributeDate = klassElt.getAttributeValue(attributeName).replace('T', ' ');
		
		Date xmlDate = sdf.parse(attributeDate.substring(0, 21));
		
		assertEquals(0,xmlDate.compareTo(attributeValue));
	}	
	
	/**
	 * Uses xpath to query the generated XSD Verifies that common elements
	 * attributes(name, type) are present Verifies that the element 'name'
	 * attribute matches the class name
	 * 
	 * @throws Exception
	 */
	protected void validateAssociation(Object resultObj, String associatedKlassName, String roleName)
	throws Exception {
		
//		System.out.println("Validating Class association from: " + filepathPrefix + resultObj.getClass().getSimpleName() + filepathSuffix);

		org.jdom.Document doc = getDocument(filepathPrefix + resultObj.getClass().getSimpleName() + filepathSuffix);

//		String xpath = "" + Class.forName(klassName).getSimpleName();
//		System.out.println("xpath: " + xpath);

//		List<Element> elts = queryXMLData(doc, xpath);
//		assertEquals(1, elts.size());
		Element klassElt = doc.getRootElement();
		
		List<Element> children = klassElt.getChildren();
		assertNotNull(children);
		assertEquals(1,countChildren(children,roleName));
		Element roleNameElt = locateChild(children,roleName);
		assertNotNull(roleNameElt);
		assertEquals(roleNameElt.getName(),roleName);
		
		children = roleNameElt.getChildren();
		assertNotNull(children);
//		assertEquals(1,countChildren(children,associatedKlassName)); // AllDataType.stringCollection > 1; i.e., = 3
		Element associatedKlassElt = locateChild(children,associatedKlassName);
		assertNotNull(associatedKlassElt);
	}
	
	private Element locateChild(List<Element> eltList, String roleName){
		
		for (Element elt:eltList){
			if (elt.getName().equals(roleName)) return elt;
		}
		
		return null;
	}
	
	
	private int countChildren(List<Element> eltList, String roleName){
		
		int counter=0;
		for (Element elt:eltList){
			if (elt.getName().equals(roleName)) counter++;
		}
		
		return counter;
	}

}
