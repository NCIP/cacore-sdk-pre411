package test.gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation;

import java.util.Collection;
import java.util.Iterator;

import gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Bank;
import gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Credit;
import gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Payment;
import gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Cash;
import gov.nih.nci.cacoresdk.domain.other.datatype.AllDataType;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.query.cql.CQLAssociation;
import gov.nih.nci.system.query.cql.CQLAttribute;
import gov.nih.nci.system.query.cql.CQLObject;
import gov.nih.nci.system.query.cql.CQLPredicate;
import gov.nih.nci.system.query.cql.CQLQuery;
import test.gov.nih.nci.cacoresdk.SDKTestBase;

public class ChildWithAssociationTest extends SDKTestBase
{
	public static String getTestCaseName()
	{
		return "Child With Association Test Case";
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
		Payment searchObject = new Payment();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Payment",searchObject );

		assertNotNull(results);
		assertEquals(5,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Payment result = (Payment)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getAmount());
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
		Cash searchObject = new Cash();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Cash",searchObject );

		assertNotNull(results);
		assertEquals(2,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Cash result = (Cash)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getAmount());
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
	public void testEntireObjectNestedSearch3() throws ApplicationException
	{
		Credit searchObject = new Credit();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Credit",searchObject );

		assertNotNull(results);
		assertEquals(2,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Credit result = (Credit)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getAmount());
			assertNotNull(result.getCardNumber());
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
	public void testEntireObjectNestedSearch4() throws ApplicationException
	{
		Bank searchObject = new Bank();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Bank",searchObject );

		assertNotNull(results);
		assertEquals(4,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Bank result = (Bank)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getName());
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
		
		target.setName("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Cash");
		cqlQuery.setTarget(target);

		Collection results = getApplicationService().query(cqlQuery,"gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Cash");

		assertNotNull(results);
		assertEquals(2,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Cash result = (Cash)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getAmount());
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
		
		target.setName("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Payment");
		cqlQuery.setTarget(target);

		Collection results = getApplicationService().query(cqlQuery,"gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Payment");

		assertNotNull(results);
		assertEquals(5,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Payment result = (Payment)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getAmount());
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
	public void testEntireObjectCQL3() throws ApplicationException
	{
		CQLQuery cqlQuery = new CQLQuery();
		CQLObject target = new CQLObject();
		
		target.setName("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Credit");
		cqlQuery.setTarget(target);

		Collection results = getApplicationService().query(cqlQuery,"gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Credit");

		assertNotNull(results);
		assertEquals(2,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Credit result = (Credit)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getAmount());
			assertNotNull(result.getCardNumber());
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
	public void testEntireObjectCQL4() throws ApplicationException
	{
		CQLQuery cqlQuery = new CQLQuery();
		CQLObject target = new CQLObject();
		
		target.setName("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Bank");
		cqlQuery.setTarget(target);

		Collection results = getApplicationService().query(cqlQuery,"gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Bank");

		assertNotNull(results);
		assertEquals(4,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Bank result = (Bank)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getName());
		}
	}
	
	/**
	 * Uses Nested Search Criteria for inheritance as association in search
	 * Verifies that the result set is empty
	 * 
	 * @throws ApplicationException
	 */
	public void testZeroAssociationNestedSearch() throws ApplicationException
	{
		Cash searchObject = new Cash();
		searchObject.setAmount(new Integer(6));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Payment",searchObject );

		assertNotNull(results);
		assertEquals(0,results.size());
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
		Payment searchObject = new Payment();
		searchObject.setAmount(new Integer(1));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Cash",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Cash result = (Cash)results.iterator().next();
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(new Integer(1), result.getId());
	}

	/**
	 * Uses CQL Search Criteria for inheritance as association in search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * 
	 * @throws ApplicationException
	 */
	public void testZeroAssociationCQL() throws ApplicationException
	{
		CQLQuery cqlQuery = new CQLQuery();
		CQLObject target = new CQLObject();

		CQLAssociation association = new CQLAssociation();
		association.setName("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Cash");
		association.setAttribute(new CQLAttribute("amount", CQLPredicate.EQUAL_TO,"6"));
		
		target.setName("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Payment");
		target.setAssociation(association);
		cqlQuery.setTarget(target);
		
		Collection results = getApplicationService().query(cqlQuery,"gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Payment");

		assertNotNull(results);
		assertEquals(0,results.size());
		
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
		Cash searchObject = new Cash();
		searchObject.setAmount(new Integer(2));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Payment",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Payment result = (Payment)results.iterator().next();
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(new Integer(2),result.getId());
	}

	/**
	 * Uses Nested Search Criteria for inheritance as association in search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * 
	 * @throws ApplicationException
	 */
	public void testAssociationNestedSearch3() throws ApplicationException
	{
		Payment searchObject = new Payment();
		searchObject.setAmount(new Integer(4));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Credit",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Credit result = (Credit)results.iterator().next();
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(new Integer(4), result.getId());
	}

	/**
	 * Uses Nested Search Criteria for inheritance as association in search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * 
	 * @throws ApplicationException
	 */
	public void testAssociationNestedSearch4() throws ApplicationException
	{
		Credit searchObject = new Credit();
		searchObject.setAmount(new Integer(3));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Payment",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Payment result = (Payment)results.iterator().next();
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(new Integer(3), result.getId());
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
		association.setName("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Cash");
		association.setAttribute(new CQLAttribute("id", CQLPredicate.EQUAL_TO,"1"));
		
		target.setName("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Payment");
		target.setAssociation(association);
		cqlQuery.setTarget(target);
		
		Collection results = getApplicationService().query(cqlQuery,"gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Payment");

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Payment result = (Payment)results.iterator().next();
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(new Integer(1), result.getId());
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
		association.setName("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Payment");
		association.setAttribute(new CQLAttribute("id", CQLPredicate.EQUAL_TO,"2"));
		
		target.setName("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Cash");
		target.setAssociation(association);
		cqlQuery.setTarget(target);
		
		Collection results = getApplicationService().query(cqlQuery,"gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Cash");

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Cash result = (Cash)results.iterator().next();
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(new Integer(2), result.getId());
	}

	/**
	 * Uses CQL Criteria for inheritance as association in search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * 
	 * @throws ApplicationException
	 */
	public void testAssociationCQL3() throws ApplicationException
	{
		CQLQuery cqlQuery = new CQLQuery();
		CQLObject target = new CQLObject();

		CQLAssociation association = new CQLAssociation();
		association.setName("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Bank");
		association.setAttribute(new CQLAttribute("name", CQLPredicate.EQUAL_TO,"Bank4"));
		
		target.setName("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Credit");
		target.setAssociation(association);
		cqlQuery.setTarget(target);
		
		Collection results = getApplicationService().query(cqlQuery,"gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Credit");

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Payment result = (Payment)results.iterator().next();
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(new Integer(4), result.getId());
	}
	

	/**
	 * Uses CQL Criteria for inheritance as association in search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attribute is null
	 * 
	 * @throws ApplicationException
	 */
	public void testAssociationCQL4() throws ApplicationException
	{
		CQLQuery cqlQuery = new CQLQuery();
		CQLObject target = new CQLObject();

		CQLAssociation association = new CQLAssociation();
		association.setName("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Bank");
		association.setAttribute(new CQLAttribute("name", CQLPredicate.EQUAL_TO,"Bank3"));
		
		target.setName("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Credit");
		target.setAssociation(association);
		cqlQuery.setTarget(target);
		
		Collection results = getApplicationService().query(cqlQuery,"gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Credit");

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Credit result = (Credit)results.iterator().next();
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals(new Integer(3), result.getId());
	}
}
