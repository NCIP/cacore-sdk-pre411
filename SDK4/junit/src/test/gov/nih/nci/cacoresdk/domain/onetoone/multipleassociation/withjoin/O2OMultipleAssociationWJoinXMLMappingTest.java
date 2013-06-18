/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk.domain.onetoone.multipleassociation.withjoin;

import gov.nih.nci.cacoresdk.domain.onetoone.multipleassociation.withjoin.Bride;
import gov.nih.nci.cacoresdk.domain.onetoone.multipleassociation.withjoin.InLaw;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import test.gov.nih.nci.cacoresdk.SDKXMLMappingTestBase;

public class O2OMultipleAssociationWJoinXMLMappingTest extends SDKXMLMappingTestBase
{
	
	private Document doc = null;
	
	public static String getTestCaseName()
	{
		return "One to One Multiple Association XML Mapping Test Case";
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
		Class targetClass = InLaw.class;

		validateClassElements(targetClass,"id");

		validateFieldElement(targetClass, "id", "Integer");
		validateFieldElement(targetClass, "name", "String");	
	}	

	
	/**
	 * Verifies that the 'element' and 'complexType' elements 
	 * corresponding to the Class are present in the XMLMapping
	 * Verifies that the Class attributes are present in the XMLMapping
	 * 
	 * @throws Exception
	 */
	public void testClassElement2() throws Exception
	{
		Class targetClass = Bride.class;	

		validateFieldElement(targetClass, "id", "Integer");
		validateFieldElement(targetClass, "name", "String");		
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
		Class targetClass = Bride.class;
		Class associatedClass = InLaw.class;

		validateClassAssociationElements(targetClass, associatedClass, "father",false);
	}	
	
	
	/**
	 * Verifies that association elements 
	 * corresponding to the Class are present in the XMLMapping
	 * Verifies that the Class attributes are present in the XMLMapping
	 * 
	 * @throws Exception
	 */
	public void testAssociationElements2() throws Exception
	{
		Class targetClass = Bride.class;
		Class associatedClass = InLaw.class;

		validateClassAssociationElements(targetClass, associatedClass, "mother",false);
	}	
		
	/**
	 * Verifies that association elements 
	 * corresponding to the Class are present in the XMLMapping
	 * Verifies that the Class attributes are present in the XMLMapping
	 * 
	 * @throws Exception
	 */
	public void testAssociationElements4() throws Exception
	{
		Class targetClass = InLaw.class;
		Class associatedClass = Bride.class;

		//No Association from InLaw to Bride
		String xpath = "/mapping/class[@name='" + targetClass.getName() + "']"
		+ "/field[@type='" + associatedClass.getName() + "']";

		List<Element> attributeElts = queryXMLMapping(doc, xpath);
		assertEquals(0, attributeElts.size());
	}	
}
