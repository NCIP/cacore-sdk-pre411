package test.gov.nih.nci.cacoresdk;

import java.util.List;

import junit.framework.TestCase;

import org.jaxen.JaxenException;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * @author Dan Dumitru
 */
public abstract class SDKXSDTestBase extends TestCase {

	private   String namespace = "http://www.w3.org/2001/XMLSchema";
	protected String filepath  = "../output/example/package/remote-client/conf/";

	String prefix = "xs";

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected static String getTestCaseName() {
		return "SDK Base XSD Test Case";
	}

	public abstract Document getDoc();

	protected Element getRootElement(String filename) {
		Element rootElement;
		try {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(filename);
			rootElement = doc.getRootElement();

		} catch (Exception ex) {
			throw new RuntimeException("Error reading XSD file: " + filename,
					ex);
		}

		return rootElement;
	}

	protected Document getDocument(String filename) {
		Document doc;
		try {
			SAXBuilder builder = new SAXBuilder();
			doc = builder.build(filename);
		} catch (Exception ex) {
			throw new RuntimeException("Error reading XSD file: " + filename,ex);
		}

		return doc;
	}

	protected List<Element> queryXSD(Object obj, String xpath)
	throws JaxenException {

		// e.g.: xpath = "/*[local-name()='schema']";

		JDOMXPath path = new JDOMXPath(xpath);
		path.addNamespace(prefix, namespace);
		List<Element> elts = path.selectNodes(obj);

		// System.out.println("Elements Found: " + elts.size());

		return elts;

	}

	/**
	 * Uses xpath to query the generated XSD Verifies that common elements
	 * (schema, element, complexType) are present Verifies that the number of
	 * 'element' and 'complexType' elements match
	 * 
	 * @throws Exception
	 */
	public void validateCommonSchemaElements() throws Exception {

		Document doc = getDoc();

		assertNotNull(doc);
		assertNotNull(doc.getRootElement());
		String xpath = "/xs:schema";
		List<Element> schemaElts = queryXSD(doc, xpath);
		assertEquals(1, schemaElts.size());

		xpath = "/xs:schema/xs:element";
		List<Element> elts = queryXSD(doc, xpath);

		xpath = "/xs:schema/xs:complexType";
		List<Element> complexElts = queryXSD(doc, xpath);

		// Verifies that the number 'element' and 'complexType'  
		// Class elements match
		assertEquals(elts.size(), complexElts.size());

	}

	/**
	 * Uses xpath to query the generated XSD Verifies that common elements
	 * attributes(name, type) are present Verifies that the element 'name'
	 * attribute matches the class name
	 * 
	 * @throws Exception
	 */
	public void validateClassElements(Class klass)
	throws Exception {

		Document doc = getDoc();

		String klassName = klass.getSimpleName();
		String xpath = "/xs:schema/xs:element[@name='" + klassName + "']";

		List<Element> elts = queryXSD(doc, xpath);
		assertEquals(1, elts.size());
		Element klassElt = elts.get(0);
		
		if (!(null == klassElt.getAttribute("abstract")) && klassElt.getAttributeValue("abstract").equals("true")){
			assertEquals(3, klassElt.getAttributes().size());
		} else{
			assertEquals(2, klassElt.getAttributes().size());
		}
		
		assertTrue(klassElt.getAttributeValue("name").equals(klassName));
		assertTrue(klassElt.getAttributeValue("type").equals(klassName));

		xpath = "/xs:schema/xs:complexType[@name='" + klassName + "']";

		elts = queryXSD(doc, xpath);
		assertEquals(1, elts.size());
		Element complexTypeElt = elts.get(0);
		assertEquals(1, complexTypeElt.getAttributes().size());
		assertTrue(complexTypeElt.getAttributeValue("name").equals(klassName));
	}
	
	/**
	 * Uses xpath to query the generated XSD Verifies that common elements
	 * attributes(name, type) are present Verifies that the element 'name'
	 * attribute matches the class name
	 * 
	 * @throws Exception
	 */
	public void validateClassIsAbstract(Class klass)
	throws Exception {

		Document doc = getDoc();

		String klassName = klass.getSimpleName();
		String xpath = "/xs:schema/xs:element[@name='" + klassName + "']";

		List<Element> elts = queryXSD(doc, xpath);
		assertEquals(1, elts.size());
		Element klassElt = elts.get(0);
		
		assertNotNull(klassElt.getAttributeValue("abstract"));
		assertTrue(klassElt.getAttributeValue("abstract").equals("true"));
	}

	/**
	 * Uses xpath to query the generated XSD Verifies that common elements
	 * attributes(name, type) are present Verifies that the element 'name'
	 * attribute matches the class name
	 * 
	 * @throws Exception
	 */
	public void validateSubclassElements(Class klass)
	throws Exception {

		Document doc = getDoc();

		validateClassElements(klass);

		String superklassName = klass.getSuperclass().getSimpleName();
//		System.out.println("Superclass for class " + klass.getSimpleName()
//				+ ": " + superklassName);
		String xpath = "/xs:schema/xs:complexType[@name='"
			+ klass.getSimpleName()
			+ "']/xs:complexContent/xs:extension[@base='" + superklassName
			+ "']";

		List<Element> elts = queryXSD(doc, xpath);
		assertEquals(1, elts.size());
		
		Element klassElt = elts.get(0);
		assertEquals(1, klassElt.getAttributes().size());
		assertTrue(klassElt.getAttributeValue("base").equals(superklassName));

	}

	/**
	 * Uses xpath to query the generated XSD
	 * Verifies that Subclass Association elements
	 * (extension, sequence) and attributes (base, ref) are present
	 * 
	 * @throws Exception
	 */
	public void validateClassAssociationElements(Class klass, Class associatedKlass, String rolename, String minOccurs, String maxOccurs)
	throws Exception {

		Document doc = getDoc();

		validateClassElements(klass);
		
		String xpath = "/xs:schema/xs:complexType[@name='" + klass.getSimpleName() + "']" 
			+ "/xs:sequence/xs:element[@name='" + rolename + "']"
			+ "/xs:complexType/xs:sequence/xs:element[@ref='" + associatedKlass.getSimpleName() + "']";

//		System.out.println("xpath: " + xpath);
		
		List<Element> elts = queryXSD(doc, xpath);
		assertEquals(1, elts.size());
		
		Element klassElt = elts.get(0);
		assertEquals(3, klassElt.getAttributes().size()); // ref, minOccurs, maxOccurs
		assertEquals(klassElt.getAttributeValue("ref"),associatedKlass.getSimpleName());
		
		// TODO :: change eventually to honor minOccurs value passed in
		// ignore 'minOccurs' value for now, as the Code Generator always sets it to zero
		assertEquals(klassElt.getAttributeValue("minOccurs"),"0");
		assertEquals(klassElt.getAttributeValue("maxOccurs"),maxOccurs);

	}	
	
	/**
	 * Uses xpath to query the generated XSD
	 * Verifies that Subclass Association elements
	 * (extension, sequence) and attributes (base, ref) are present
	 * 
	 * @throws Exception
	 */
	public void validateSubclassAssociationElements(Class klass, Class associatedKlass, String rolename, String minOccurs, String maxOccurs)
	throws Exception {

		Document doc = getDoc();

		validateClassElements(klass);

		String superklassName = klass.getSuperclass().getSimpleName();
//		System.out.println("Superclass for class " + klass.getSimpleName()
//				+ ": " + superklassName);
		
		String xpath = "/xs:schema/xs:complexType[@name='" + klass.getSimpleName() + "']" 
			+ "/xs:complexContent/xs:extension[@base='" + superklassName + "']"
			+ "/xs:sequence/xs:element[@name='" + rolename + "']"
			+ "/xs:complexType/xs:sequence/xs:element[@ref='" + associatedKlass.getSimpleName() + "']";

//		System.out.println("xpath: " + xpath);
		
		List<Element> elts = queryXSD(doc, xpath);
		assertEquals(1, elts.size());
		
		Element klassElt = elts.get(0);
		assertEquals(3, klassElt.getAttributes().size()); // ref, minOccurs, maxOccurs
		assertEquals(klassElt.getAttributeValue("ref"),associatedKlass.getSimpleName());
		
		// TODO :: change eventually to honor minOccurs value passed in
		// ignore 'minOccurs' value for now, as the Code Generator always sets it to zero
		assertEquals(klassElt.getAttributeValue("minOccurs"),"0");
		assertEquals(klassElt.getAttributeValue("maxOccurs"),maxOccurs);

	}		

	protected void validateAttributeElement(Class klass,
			String attributeName, String attributeType) throws Exception {

		Document doc = getDoc();

		String xpath = "/xs:schema/xs:complexType[@name='"
			+ klass.getSimpleName() + "']/xs:attribute[@name='"
			+ attributeName + "']";

//		System.out.println("xpath: " + xpath);

		List<Element> attributeElts = queryXSD(doc, xpath);
		assertNotNull(attributeElts);
		assertEquals(1, attributeElts.size());

		Element elt = attributeElts.get(0);
		assertEquals(elt.getAttributeValue("type").toLowerCase(),"xs:"+attributeType.toLowerCase());
	}

	protected void validateSubclassAttributeElement(Class klass,
			String attributeName, String attributeType) throws Exception {

		Document doc = getDoc();

		String superklassName = klass.getSuperclass().getSimpleName();
		String xpath = "/xs:schema/xs:complexType[@name='"
			+ klass.getSimpleName()
			+ "']/xs:complexContent/xs:extension[@base='" + superklassName
			+ "']/xs:attribute[@name='" + attributeName + "']";

//		Element elt = queryXSD(doc, xpath).get(0);
//		xpath = "xs:attribute[@name='" + attributeName + "']";

		List<Element> attributeElts = queryXSD(doc, xpath);
		assertNotNull(attributeElts);
		assertEquals(1, attributeElts.size());

		Element elt = attributeElts.get(0);
		assertEquals(elt.getAttributeValue("type").toLowerCase(),"xs:"+attributeType.toLowerCase());
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	
	

}
