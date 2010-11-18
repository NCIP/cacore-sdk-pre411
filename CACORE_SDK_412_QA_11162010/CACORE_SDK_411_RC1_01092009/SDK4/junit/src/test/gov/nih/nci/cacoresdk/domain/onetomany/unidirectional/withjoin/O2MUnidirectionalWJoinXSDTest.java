package test.gov.nih.nci.cacoresdk.domain.onetomany.unidirectional.withjoin;

import gov.nih.nci.cacoresdk.domain.onetomany.unidirectional.withjoin.Button;
import gov.nih.nci.cacoresdk.domain.onetomany.unidirectional.withjoin.Shirt;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import test.gov.nih.nci.cacoresdk.SDKXSDTestBase;

public class O2MUnidirectionalWJoinXSDTest extends SDKXSDTestBase
{
	
	private Document doc = null;
	
	public static String getTestCaseName()
	{
		return "One to Many Unidirectional With Join XSD Test Case";
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		String schemaFileName = "gov.nih.nci.cacoresdk.domain.onetomany.unidirectional.withjoin.xsd";
		doc = getDocument(schemaFileName);
	}

	public Document getDoc() {
		return doc;
	}
	
	/**
	 * Uses xpath to query XSD
	 * Verifies that common XSD elements are present 
	 * 
	 * @throws Exception
	 */
	public void testCommonSchemaElements() throws Exception
	{
		validateCommonSchemaElements();
	}
	
	/**
	 * Verifies that the 'element' and 'complexType' elements 
	 * corresponding to the Class are present in the XSD
	 * Verifies that the Class attributes are present in the XSD
	 * 
	 * @throws Exception
	 */
	public void testClassElement1() throws Exception
	{
		Class targetClass = Button.class;

		validateClassElements(targetClass);
		validateAttributeElement(targetClass, "id", "Integer");
		validateAttributeElement(targetClass, "holes", "Integer");	
	}	

	
	/**
	 * Verifies that the 'element' and 'complexType' elements 
	 * corresponding to the Class are present in the XSD
	 * Verifies that the Class attributes are present in the XSD
	 * 
	 * @throws Exception
	 */
	public void testClassElement2() throws Exception
	{
		Class targetClass = Shirt.class;	

		validateClassElements(targetClass);
		validateAttributeElement(targetClass, "id", "Integer");
		validateAttributeElement(targetClass, "style", "String");		
	}
	
	
	/**
	 * Verifies that association elements 
	 * corresponding to the Class are present in the XSD
	 * Verifies that the Class attributes are present in the XSD
	 * 
	 * @throws Exception
	 */
	public void testAssociationElements1() throws Exception
	{
		Class targetClass = Shirt.class;
		Class associatedClass = Button.class;

		validateClassAssociationElements(targetClass, associatedClass, "buttonCollection","0","unbounded");
	}	
	
	
	/**
	 * Verifies that association elements 
	 * corresponding to the Class are present in the XSD
	 * Verifies that the Class attributes are present in the XSD
	 * 
	 * @throws Exception
	 */
	public void testAssociationElements2() throws Exception
	{
		Class targetClass = Button.class;
		Class associatedClass = Shirt.class;

		//No Association from Button to Shirt
		String xpath = "/xs:schema/xs:complexType[@name='" + targetClass.getSimpleName() + "']" 
		+ "/xs:sequence/xs:element"
		+ "/xs:complexType/xs:sequence/xs:element[@ref='" + associatedClass.getSimpleName() + "']";

		List<Element> elts = queryXSD(doc, xpath);
		assertEquals(0, elts.size());
	}	
}
