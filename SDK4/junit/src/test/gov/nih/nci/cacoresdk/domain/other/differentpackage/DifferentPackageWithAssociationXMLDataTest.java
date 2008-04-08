package test.gov.nih.nci.cacoresdk.domain.other.differentpackage;

import gov.nih.nci.cacoresdk.domain.other.differentpackage.associations.Pie;
import gov.nih.nci.cacoresdk.domain.other.differentpackage.associations.Utensil;
import gov.nih.nci.cacoresdk.domain.other.differentpackage.Dessert;

import java.util.Collection;
import java.util.Iterator;

import test.gov.nih.nci.cacoresdk.SDKXMLDataTestBase;

public class DifferentPackageWithAssociationXMLDataTest extends SDKXMLDataTestBase
{
	public static String getTestCaseName()
	{
		return "Different Package With Association XML Data Test Case";
	}
	
	/**
	 * Uses Nested Search Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * 
	 * @throws Exception
	 */
	public void testEntireObjectNestedSearch1() throws Exception
	{
		Dessert searchObject = new Dessert();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.other.differentpackage.Dessert",searchObject );

		assertNotNull(results);
		assertEquals(4,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Dessert result = (Dessert)i.next();
			toXML(result);
			
			validateClassElements(result);
			validateAttribute(result,"id",result.getId());
			
			//assertTrue(validateXMLData(result, searchObject.getClass()));

			Dessert result2 = (Dessert)fromXML(result);
			
			assertNotNull(result2);
			assertNotNull(result2.getId());
		}
	}

	/**
	 * Uses Nested Search Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * 
	 * @throws Exception
	 */
	public void testEntireObjectNestedSearch2() throws Exception
	{
		Pie searchObject = new Pie();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.other.differentpackage.associations.Pie",searchObject );

		assertNotNull(results);
		assertEquals(2,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Pie result = (Pie)i.next();
			toXML(result);
			
			validateClassElements(result);
			validateAttribute(result,"id",result.getId());
			validateAttribute(result,"filling",result.getFilling());
			
			//assertTrue(validateXMLData(result, searchObject.getClass()));

			Pie result2 = (Pie)fromXML(result);
			
			assertNotNull(result2);
			assertNotNull(result2.getId());
			assertNotNull(result2.getFilling());
		}
	}

	/**
	 * Uses Nested Search Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * 
	 * @throws Exception
	 */
	public void testEntireObjectNestedSearch3() throws Exception
	{
		Utensil searchObject = new Utensil();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.other.differentpackage.associations.Utensil",searchObject );

		assertNotNull(results);
		assertEquals(3,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Utensil result = (Utensil)i.next();
			toXML(result);
			
			validateClassElements(result);
			validateAttribute(result,"id",result.getId());
			validateAttribute(result,"name",result.getName());
			
			//assertTrue(validateXMLData(result, searchObject.getClass()));

			Utensil result2 = (Utensil)fromXML(result);
			
			assertNotNull(result2);
			assertNotNull(result2.getId());
			assertNotNull(result2.getName());			
		}
	}
	
	/**
	 * Uses Nested Search Criteria for inheritance as association in search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * 
	 * @throws Exception
	 */
	public void testAssociationNestedSearch1() throws Exception
	{
		Dessert searchObject = new Dessert();
		searchObject.setId(new Integer(3));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.other.differentpackage.associations.Pie",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Pie result = (Pie)results.iterator().next();
		toXML(result);
		Pie result2 = (Pie)fromXML(result);

		assertNotNull(result2);
		assertNotNull(result2.getId());
		assertNotNull(result2.getFilling());
		assertEquals(new Integer(3), result2.getId());
	}

	/**
	 * Uses Nested Search Criteria for inheritance as association in search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * 
	 * @throws Exception
	 */
	public void testAssociationNestedSearch2() throws Exception
	{
		Pie searchObject = new Pie();
		searchObject.setId(new Integer(3));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.other.differentpackage.Dessert",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Dessert result = (Dessert)results.iterator().next();
		toXML(result);
		Dessert result2 = (Dessert)fromXML(result);

		assertNotNull(result2);
		assertNotNull(result2.getId());
		assertEquals(new Integer(3), result2.getId());
	}

}
