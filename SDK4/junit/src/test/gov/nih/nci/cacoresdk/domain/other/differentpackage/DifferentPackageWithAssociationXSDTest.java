/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk.domain.other.differentpackage;

import gov.nih.nci.cacoresdk.domain.other.differentpackage.associations.IceCream;
import gov.nih.nci.cacoresdk.domain.other.differentpackage.associations.Pie;
import gov.nih.nci.cacoresdk.domain.other.differentpackage.associations.Utensil;
import gov.nih.nci.cacoresdk.domain.other.differentpackage.Dessert;

import org.jdom.Document;

import test.gov.nih.nci.cacoresdk.SDKXSDTestBase;

public class DifferentPackageWithAssociationXSDTest extends SDKXSDTestBase
{
	
	private Document doc = null;
	
	public static String getTestCaseName()
	{
		return "Abstract Parent With Association XSD Test Case";
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		String schemaFileName = "gov.nih.nci.cacoresdk.domain.other.differentpackage.xsd";
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
		Class targetClass = Dessert.class;

		validateClassElements(targetClass);

		validateAttributeElement(targetClass, "id", "Integer");
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
		String schemaFileName = "gov.nih.nci.cacoresdk.domain.other.differentpackage.associations.xsd";
		doc = getDocument(schemaFileName);
		
		Class targetClass = Pie.class;

		validateSubclassElements(targetClass);
		validateSubclassAttributeElement(targetClass, "filling","String");

	}
	
	/**
	 * Verifies that the 'element' and 'complexType' elements 
	 * corresponding to the Subclass are present in the XSD
	 * Verifies that the Class attributes are present in the XSD
	 * 
	 * @throws Exception
	 */
	public void testClassElement3() throws Exception
	{
		String schemaFileName = "gov.nih.nci.cacoresdk.domain.other.differentpackage.associations.xsd";
		doc = getDocument(schemaFileName);
		
		Class targetClass = IceCream.class;

		validateSubclassElements(targetClass);
		validateSubclassAttributeElement(targetClass, "topping","String");

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
		String schemaFileName = "gov.nih.nci.cacoresdk.domain.other.differentpackage.associations.xsd";
		doc = getDocument(schemaFileName);
		
		Class targetClass = Utensil.class;

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
	public void testAssociationElements1() throws Exception
	{
		String schemaFileName = "gov.nih.nci.cacoresdk.domain.other.differentpackage.xsd";
		doc = getDocument(schemaFileName);
		
		Class targetClass = Dessert.class;
		Class associatedClass = Utensil.class;

		validateClassAssociationElements(targetClass, associatedClass, "utensilCollection","0","unbounded");
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
		String schemaFileName = "gov.nih.nci.cacoresdk.domain.other.differentpackage.associations.xsd";
		doc = getDocument(schemaFileName);
		
		Class targetClass = Utensil.class;
		Class associatedClass = Dessert.class;

		validateClassAssociationElements(targetClass, associatedClass, "dessertCollection","0","unbounded");
	}	
}
