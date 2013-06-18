/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk.domain.inheritance.onechild.sametable;

import gov.nih.nci.cacoresdk.domain.inheritance.onechild.sametable.Note;
import gov.nih.nci.cacoresdk.domain.inheritance.onechild.sametable.Currency;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.query.cql.CQLAssociation;
import gov.nih.nci.system.query.cql.CQLAttribute;
import gov.nih.nci.system.query.cql.CQLObject;
import gov.nih.nci.system.query.cql.CQLPredicate;
import gov.nih.nci.system.query.cql.CQLQuery;

import java.util.Collection;
import java.util.Iterator;

import test.gov.nih.nci.cacoresdk.SDKTestBase;

public class OneChildSametableTest extends SDKTestBase
{
	public static String getTestCaseName()
	{
		return "One Child Same Table Test Case";
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
		Currency searchObject = new Currency();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.inheritance.onechild.sametable.Currency",searchObject );

		assertNotNull(results);
		assertEquals(3,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Currency result = (Currency)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getCountry());
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
		Note searchObject = new Note();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.inheritance.onechild.sametable.Note",searchObject );

		assertNotNull(results);
		assertEquals(3,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Note result = (Note)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getCountry());			
			assertNotNull(result.getValue());
		}
	}

	/**
	 * Uses CQL Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * 
	 * @throws ApplicationException
	 */
	public void testEntireObjectCQL1() throws ApplicationException
	{
		CQLQuery cqlQuery = new CQLQuery();
		CQLObject target = new CQLObject();
		
		target.setName("gov.nih.nci.cacoresdk.domain.inheritance.onechild.sametable.Note");
		cqlQuery.setTarget(target);

		Collection results = getApplicationService().query(cqlQuery);

		assertNotNull(results);
		assertEquals(3,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Note result = (Note)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getValue());
		}
	}

	/**
	 * Uses CQL Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * 
	 * @throws ApplicationException
	 */
	public void testEntireObjectCQL2() throws ApplicationException
	{
		CQLQuery cqlQuery = new CQLQuery();
		CQLObject target = new CQLObject();
		
		target.setName("gov.nih.nci.cacoresdk.domain.inheritance.onechild.sametable.Currency");
		cqlQuery.setTarget(target);

		Collection results = getApplicationService().query(cqlQuery);

		assertNotNull(results);
		assertEquals(3,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Currency result = (Currency)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getCountry());
		}
	}

	/**
	 * Uses Nested Search Criteria for inheritance as association in search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * 
	 * @throws ApplicationException
	 */
	public void testAssociationNestedSearch1() throws ApplicationException
	{
		Currency searchObject = new Currency();
		searchObject.setCountry("USA");
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.inheritance.onechild.sametable.Note",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Note result = (Note)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getValue());
		}
	}

	/**
	 * Uses Nested Search Criteria for inheritance as association in search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * 
	 * @throws ApplicationException
	 */
	public void testAssociationNestedSearch2() throws ApplicationException
	{
		Note searchObject = new Note();
		searchObject.setCountry("Germany");
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.inheritance.onechild.sametable.Currency",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Currency result = (Currency)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getCountry());
		}
	}

	/**
	 * Uses CQL Criteria for inheritance as association in search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * 
	 * @throws ApplicationException
	 */
	public void testAssociationCQL1() throws ApplicationException
	{
		CQLQuery cqlQuery = new CQLQuery();
		CQLObject target = new CQLObject();

		CQLAssociation association = new CQLAssociation();
		association.setName("gov.nih.nci.cacoresdk.domain.inheritance.onechild.sametable.Note");
		association.setAttribute(new CQLAttribute("country", CQLPredicate.EQUAL_TO,"USA"));
		
		target.setName("gov.nih.nci.cacoresdk.domain.inheritance.onechild.sametable.Currency");
		target.setAssociation(association);
		cqlQuery.setTarget(target);
		
		Collection results = getApplicationService().query(cqlQuery);

		assertNotNull(results);
		assertEquals(1,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Currency result = (Currency)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getCountry());
		}
	}
	

	/**
	 * Uses CQL Criteria for inheritance as association in search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * 
	 * @throws ApplicationException
	 */
	public void testAssociationCQL2() throws ApplicationException
	{
		CQLQuery cqlQuery = new CQLQuery();
		CQLObject target = new CQLObject();

		CQLAssociation association = new CQLAssociation();
		association.setName("gov.nih.nci.cacoresdk.domain.inheritance.onechild.sametable.Currency");
		association.setAttribute(new CQLAttribute("country", CQLPredicate.EQUAL_TO,"Germany"));
		
		target.setName("gov.nih.nci.cacoresdk.domain.inheritance.onechild.sametable.Note");
		target.setAssociation(association);
		cqlQuery.setTarget(target);
		
		Collection results = getApplicationService().query(cqlQuery);

		assertNotNull(results);
		assertEquals(1,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Note result = (Note)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getCountry());
		}
	}
}
