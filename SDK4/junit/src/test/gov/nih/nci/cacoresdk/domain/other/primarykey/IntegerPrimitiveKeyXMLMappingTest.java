/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk.domain.other.primarykey;

import gov.nih.nci.cacoresdk.domain.other.primarykey.IntegerPrimitiveKey;

import org.jdom.Document;

import test.gov.nih.nci.cacoresdk.SDKXMLMappingTestBase;

public class IntegerPrimitiveKeyXMLMappingTest extends SDKXMLMappingTestBase
{
	
	private Document doc = null;
	
	public static String getTestCaseName()
	{
		return "IntegerPrimitiveKey XML Mapping Test Case";
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		String xmlMappingFileName = "xml-mapping.xml";
		doc = getDocument(xmlMappingFileName);
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
		Class targetClass = IntegerPrimitiveKey.class;

		validateClassElements(targetClass,"id");

		validateFieldElement(targetClass, "id", "Integer");
		validateFieldElement(targetClass, "name", "String");	

	}	
}
