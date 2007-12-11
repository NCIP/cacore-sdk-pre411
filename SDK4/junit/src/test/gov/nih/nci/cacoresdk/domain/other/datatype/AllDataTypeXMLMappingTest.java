package test.gov.nih.nci.cacoresdk.domain.other.datatype;

import gov.nih.nci.cacoresdk.domain.other.datatype.AllDataType;

import org.jdom.Document;

import test.gov.nih.nci.cacoresdk.SDKXMLMappingTestBase;

public class AllDataTypeXMLMappingTest extends SDKXMLMappingTestBase
{
	
	private Document doc = null;
	
	public static String getTestCaseName()
	{
		return "AllDataType XML Mapping Test Case";
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		String xmlMappingFileName = "xml-mapping.xml";
		doc = getDocument(filepath + xmlMappingFileName);
	}

	public Document getDoc() {
		return doc;
	}
	
	/**
	 * Uses xpath to query XMLMapping
	 * Verifies that common XMLMapping elements are present 
	 * 
	 * @throws Exception
	 */
	public void testCommonSchemaElements() throws Exception
	{
		validateCommonSchemaElements();
	}
	
	/**
	 * Verifies that the 'element' and 'complexType' elements 
	 * corresponding to the Class are present in the XMLMapping
	 * Verifies that the Class attributes are present in the XMLMapping
	 * 
	 * @throws Exception
	 */
	public void testClassElement1() throws Exception
	{
		Class targetClass = AllDataType.class;

		validateClassElements(targetClass,"id");

		validateFieldElement(targetClass, "id", "Integer");
		validateFieldElement(targetClass, "booleanPrimitiveValue", "Boolean");	
		validateFieldElement(targetClass, "booleanValue", "Boolean");
		validateFieldElement(targetClass, "characterPrimitiveValue", "Char");
		validateFieldElement(targetClass, "characterValue", "Char");
		validateFieldElement(targetClass, "clobValue", "String");
		validateFieldElement(targetClass, "datePrimitiveValue", "java.util.Date");
		validateFieldElement(targetClass, "dateValue", "java.util.Date");
		validateFieldElement(targetClass, "doublePrimitiveValue", "Double");
		validateFieldElement(targetClass, "doubleValue", "Double");
		validateFieldElement(targetClass, "floatPrimitiveValue", "Float");
		validateFieldElement(targetClass, "floatValue", "Float");
		validateFieldElement(targetClass, "intValue", "Integer");
		validateFieldElement(targetClass, "intPrimitiveValue", "Integer");
		validateFieldElement(targetClass, "longValue", "Long");
		validateFieldElement(targetClass, "longPrimitiveValue", "Long");
		validateFieldElement(targetClass, "stringPrimitiveValue", "String");
		validateFieldElement(targetClass, "stringValue", "String");	
	}	
	
	/**
	 * Verifies that association elements 
	 * corresponding to the Class are present in the XMLMapping
	 * Verifies that the Class attributes are present in the XMLMapping
	 * 
	 * @throws Exception
	 */
	public void testAssociationElements1() throws Exception
	{
		Class targetClass = AllDataType.class;
		Class associatedClass = String.class;
		validateClassAssociationElements(targetClass, associatedClass, "stringCollection",true);

	}	
}
