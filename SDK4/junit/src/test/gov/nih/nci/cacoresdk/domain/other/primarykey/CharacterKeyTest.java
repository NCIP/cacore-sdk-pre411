/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk.domain.other.primarykey;

import gov.nih.nci.cacoresdk.domain.other.primarykey.CharacterKey;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.query.cql.CQLAttribute;
import gov.nih.nci.system.query.cql.CQLObject;
import gov.nih.nci.system.query.cql.CQLPredicate;
import gov.nih.nci.system.query.cql.CQLQuery;

import java.util.Collection;
import java.util.Iterator;

import test.gov.nih.nci.cacoresdk.SDKTestBase;

public class CharacterKeyTest extends SDKTestBase
{
	public static String getTestCaseName()
	{
		return "Character Key Test Case";
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
		CharacterKey searchObject = new CharacterKey();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.other.primarykey.CharacterKey",searchObject );

		assertNotNull(results);
		assertEquals(4,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			CharacterKey result = (CharacterKey)i.next();
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
		CharacterKey searchObject = new CharacterKey();
		searchObject.setId('9');
		Collection results = getApplicationService().search(CharacterKey.class,searchObject );

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
		object.setName("gov.nih.nci.cacoresdk.domain.other.primarykey.CharacterKey");
		object.setAttribute(new CQLAttribute("id",CQLPredicate.EQUAL_TO,"9"));
		criteria.setTarget(object);
		
		Collection results = getApplicationService().query(criteria);

		assertNotNull(results);
		assertEquals(1,results.size());
	}
	
}
