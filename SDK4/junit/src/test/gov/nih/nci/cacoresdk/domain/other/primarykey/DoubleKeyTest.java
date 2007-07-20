package test.gov.nih.nci.cacoresdk.domain.other.primarykey;

import gov.nih.nci.cacoresdk.domain.other.primarykey.DoubleKey;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.query.cql.CQLAttribute;
import gov.nih.nci.system.query.cql.CQLObject;
import gov.nih.nci.system.query.cql.CQLPredicate;
import gov.nih.nci.system.query.cql.CQLQuery;

import java.util.Collection;
import java.util.Iterator;

import test.gov.nih.nci.cacoresdk.SDKTestBase;

public class DoubleKeyTest extends SDKTestBase
{
	public static String getTestCaseName()
	{
		return "Double Key Test Case";
	}
	 
	/**
	 * Uses Nested Search Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * 
	 * @throws ApplicationException
	 */
	public void testEntireObjectNestedSearch() throws ApplicationException
	{
		DoubleKey searchObject = new DoubleKey();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.other.primarykey.DoubleKey",searchObject );

		assertNotNull(results);
		assertEquals(5,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			DoubleKey result = (DoubleKey)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getName());
		}
	}

	/**
	 * Uses Class for search
	 * Searches by the primary key
	 * Verifies size of the result set
	 * 
	 * @throws ApplicationException
	 */
	public void testPrimaryKeyNestedSearch() throws ApplicationException
	{
		DoubleKey searchObject = new DoubleKey();
		searchObject.setId(new Double(1.1));
		Collection results = getApplicationService().search(DoubleKey.class,searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
	}

	/**
	 * Uses CQL for search
	 * Searches by the Double data type
	 * Verifies size of the result set
	 * 
	 * @throws ApplicationException
	 */
	public void testPrimaryKeyCQL() throws ApplicationException
	{
		CQLQuery criteria = new CQLQuery();

		CQLObject object = new CQLObject();
		object.setName("gov.nih.nci.cacoresdk.domain.other.primarykey.DoubleKey");
		object.setAttribute(new CQLAttribute("id",CQLPredicate.EQUAL_TO,"1.1"));
		criteria.setTarget(object);
		
		Collection results = getApplicationService().query(criteria, "gov.nih.nci.cacoresdk.domain.other.primarykey.DoubleKey");

		assertNotNull(results);
		assertEquals(1,results.size());
	}
	
}
