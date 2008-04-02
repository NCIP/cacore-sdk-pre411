package test.gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation;

import gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Bank;
import gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Credit;
import gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Payment;
import gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Cash;

import org.jdom.Document;

import test.gov.nih.nci.cacoresdk.SDKXSDTestBase;

public class ChildWithAssociationXSDTest extends SDKXSDTestBase
{
	
	private Document doc = null;
	
	public static String getTestCaseName()
	{
		return "Child With Association XSD Test Case";
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		String schemaFileName = "gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.xsd";
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
		Class targetClass = Payment.class;

		validateClassElements(targetClass);

		validateAttributeElement(targetClass, "id", "Integer");
		validateAttributeElement(targetClass, "amount", "Integer");	
	}	
	
	/**
	 * Verifies that the 'element' and 'complexType' elements 
	 * corresponding to the Subclass are present in the XSD
	 * Verifies that the Class attributes are present in the XSD
	 * 
	 * @throws Exception
	 */
	public void testClassElement2() throws Exception
	{
		Class targetClass = Cash.class;

		validateSubclassElements(targetClass);

	}	
	
	/**
	 * Verifies that the 'element' and 'complexType' elements 
	 * corresponding to the Class are present in the XSD
	 * Verifies that the Class attributes are present in the XSD
	 * 
	 * @throws Exception
	 */
	public void testClassElement3() throws Exception
	{
		Class targetClass = Credit.class;

		validateSubclassElements(targetClass);
		validateSubclassAttributeElement(targetClass, "cardNumber","String");
	}
	
	/**
	 * Verifies that the 'element' and 'complexType' elements 
	 * corresponding to the Class are present in the XSD
	 * Verifies that the Class attributes are present in the XSD
	 * 
	 * @throws Exception
	 */
	public void testClassElement4() throws Exception
	{
		Class targetClass = Bank.class;

		validateClassElements(targetClass);

		validateAttributeElement(targetClass, "id", "Integer");
		validateAttributeElement(targetClass, "name", "String");	
	}	
	
	/**
	 * Verifies that association elements 
	 * corresponding to the Class are present in the XSD
	 * Verifies that the Class attributes are present in the XSD
	 * 
	 * @throws Exception
	 */
	public void testAssociationElements() throws Exception
	{
		Class targetClass = Credit.class;
		Class associatedClass = Bank.class;

		validateSubclassAssociationElements(targetClass, associatedClass, "issuingBank","0","1");
	}	
}
