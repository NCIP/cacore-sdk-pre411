/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.withjoin;

import gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.withjoin.Handle;
import gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.withjoin.Bag;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.query.cql.CQLAssociation;
import gov.nih.nci.system.query.cql.CQLAttribute;
import gov.nih.nci.system.query.cql.CQLObject;
import gov.nih.nci.system.query.cql.CQLPredicate;
import gov.nih.nci.system.query.cql.CQLQuery;

import java.util.Collection;
import java.util.Iterator;

import test.gov.nih.nci.cacoresdk.SDKTestBase;

public class O2OUnidirectionalWJoinTest extends SDKTestBase
{
	public static String getTestCaseName()
	{
		return "One to One Unidirectional With Join Test Case";
	}
	
	/**
	 * Uses Nested Search Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * 
	 * @throws ApplicationException
	 */
	public void testEntireObjectNestedSearch1() throws ApplicationException
	{
		Bag searchObject = new Bag();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.withjoin.Bag",searchObject );

		assertNotNull(results);
		assertEquals(11,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Bag result = (Bag)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getStyle());
		}
	}

	/**
	 * Uses Nested Search Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * 
	 * @throws ApplicationException
	 */
	public void testEntireObjectNestedSearch2() throws ApplicationException
	{
		Handle searchObject = new Handle();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.withjoin.Handle",searchObject );

		assertNotNull(results);
		assertEquals(12,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Handle result = (Handle)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getColor());
		}
	}

	/**
	 * Uses Nested Search Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * erifies that the associated object is null
	 * 
	 * @throws ApplicationException
	 */
	public void testZeroAssociatedObjectsNestedSearch1() throws ApplicationException
	{
		Bag searchObject = new Bag();
		searchObject.setId(new Integer(11));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.withjoin.Bag",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Iterator i = results.iterator();
		Bag result = (Bag)i.next();
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getStyle());
		
		Handle handle = result.getHandle();
		assertNull(handle);
	}

	/**
	 * Uses Nested Search Criteria for search to get associated object
	 * Verifies that the results are returned 
	 * Verifies size of the result set is 0
	 * 
	 * @throws ApplicationException
	 */
	public void testZeroAssociatedObjectsNestedSearch2() throws ApplicationException
	{
		Bag searchObject = new Bag();
		searchObject.setId(new Integer(11));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.withjoin.Handle",searchObject );

		assertNotNull(results);
		assertEquals(0,results.size());
	}	
	
	/**
	 * Uses Nested Search Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * Verifies that the associated object has required Id
	 * 
	 * @throws ApplicationException
	 */
	public void testOneAssociatedObjectNestedSearch1() throws ApplicationException
	{
		Bag searchObject = new Bag();
		searchObject.setId(new Integer(1));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.withjoin.Bag",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Iterator i = results.iterator();
		Bag result = (Bag)i.next();
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getStyle());
		
		Handle handle = result.getHandle();
		assertNotNull(handle);
		
		assertNotNull(handle);
		assertNotNull(handle.getId());
		assertNotNull(handle.getColor());
		assertEquals(new Integer(1),handle.getId());
	}

	/**
	 * Uses Nested Search Criteria for search to get associated object
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * Verified the Id attribute's value of the returned object 
	 * 
	 * @throws ApplicationException
	 */
	public void testOneAssociatedObjectNestedSearch2() throws ApplicationException
	{
		Bag searchObject = new Bag();
		searchObject.setId(new Integer(1));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.withjoin.Handle",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Iterator i = results.iterator();
		
		Handle handle = (Handle)i.next();
		assertNotNull(handle);
		
		assertNotNull(handle);
		assertNotNull(handle.getId());
		assertNotNull(handle.getColor());
		assertEquals(new Integer(1),handle.getId());
	}
	

	/**
	 * Uses CQL Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * Verifies that the associated object has required Id
	 * 
	 * @throws ApplicationException
	 */
	public void testNoAssociationCQL() throws ApplicationException
	{
		boolean flag = false;
		try
		{
			CQLQuery cqlQuery = new CQLQuery();
			CQLObject target = new CQLObject();
			
			CQLAssociation association = new CQLAssociation();
			association.setName("gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.withjoin.Bag");
			association.setAttribute(new CQLAttribute("id",CQLPredicate.EQUAL_TO,"1"));
			
			target.setName("gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.withjoin.Handle");
			target.setAssociation(association);
			cqlQuery.setTarget(target);
	
			Collection results = getApplicationService().query(cqlQuery);
			assertNotNull(results);
			
		}
		catch(ApplicationException e)
		{
			flag = true;
		}
		
		assertTrue(flag);
	}

	
	/**
	 * Uses CQL Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * Verifies that the associated object has required Id
	 * 
	 * @throws ApplicationException
	 */
	public void testOneAssociatedObjectCQL() throws ApplicationException
	{
		CQLQuery cqlQuery = new CQLQuery();
		CQLObject target = new CQLObject();
		
		CQLAssociation association = new CQLAssociation();
		association.setName("gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.withjoin.Bag");
		association.setAttribute(new CQLAttribute("id",CQLPredicate.EQUAL_TO,"1"));
		association.setSourceRoleName("handle");
		
		target.setName("gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.withjoin.Handle");
		target.setAssociation(association);
		cqlQuery.setTarget(target);

		Collection results = getApplicationService().query(cqlQuery);

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Iterator i = results.iterator();
		
		Handle handle = (Handle)i.next();
		assertNotNull(handle);
		
		assertNotNull(handle);
		assertNotNull(handle.getId());
		assertNotNull(handle.getColor());
		assertEquals(new Integer(1),handle.getId());
	}	
	
	public void testGetAssociation() throws ApplicationException
	{

		Bag searchObject = new Bag();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.unidirectional.withjoin.Bag",searchObject );

		assertNotNull(results);
		assertEquals(11,results.size());
		
		Handle handle;
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Bag result = (Bag)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getStyle());
			
			if (result.getId() < 11){
				handle = result.getHandle();
				assertNotNull(handle);
				assertNotNull(handle.getId());
				assertNotNull(handle.getColor());
			}
		}
	}	
}
