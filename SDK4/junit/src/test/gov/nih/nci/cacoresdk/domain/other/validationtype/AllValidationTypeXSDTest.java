package test.gov.nih.nci.cacoresdk.domain.other.validationtype;

import gov.nih.nci.cacoresdk.domain.other.validationtype.AllValidationType;

import org.jdom.Document;

import test.gov.nih.nci.cacoresdk.SDKXSDTestBase;

public class AllValidationTypeXSDTest extends SDKXSDTestBase
{
	
	private Document doc = null;
	
	public static String getTestCaseName()
	{
		return "AllValidationType XSD Test Case";
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		String schemaFileName = "gov.nih.nci.cacoresdk.domain.other.validationtype.xsd";
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
		Class<AllValidationType> targetClass = AllValidationType.class;

		validateClassElements(targetClass);

		validateAttributeWithRestriction(targetClass,"pattern", "String","DEPT");
		validateAttributeWithRestriction(targetClass,"pattern", "String","BLDG");
		validateAttributeWithRestriction(targetClass,"pattern", "String","FL");
		validateAttributeWithRestriction(targetClass,"pattern", "String","RM");
		validateAttributeWithRestriction(targetClass,"pattern", "String","APT");
		validateAttributeWithRestriction(targetClass,"pattern", "String","STE");

	}	
	
}
