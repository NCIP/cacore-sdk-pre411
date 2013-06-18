/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.withjoin;

import gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.withjoin.Bag;
import gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.withjoin.Handle;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import test.gov.nih.nci.cacoresdk.SDKXMLMappingTestBase;

public class O2OUnidirectionalWJoinXMLMappingTest extends SDKXMLMappingTestBase
{
	
	private Document doc = null;
	
	public static String getTestCaseName()
	{
		return "One to One Unidirectional With Join XML Mapping Test Case";
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
		Class targetClass = Handle.class;

		validateClassElements(targetClass,"id");

		validateFieldElement(targetClass, "id", "Integer");
		validateFieldElement(targetClass, "color", "String");	
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
		Class targetClass = Bag.class;	

		validateFieldElement(targetClass, "id", "Integer");
		validateFieldElement(targetClass, "style", "String");		
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
		Class targetClass = Bag.class;
		Class associatedClass = Handle.class;

		validateClassAssociationElements(targetClass, associatedClass, "handle",false);
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
		Class targetClass = Handle.class;
		Class associatedClass = Bag.class;

		//No Association from Handle to Bag
		String xpath = "/mapping/class[@name='" + targetClass.getName() + "']"
		+ "/field[@type='" + associatedClass.getName() + "']";

		List<Element> attributeElts = queryXMLMapping(doc, xpath);
		assertEquals(0, attributeElts.size());
	}	
}
