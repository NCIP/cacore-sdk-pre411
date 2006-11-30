package test.gov.nih.nci.cacoresdk.domain.onetoone.bidirectional;

import gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.OrderLine;
import gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.Product;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.query.cql.CQLAssociation;
import gov.nih.nci.system.query.cql.CQLAttribute;
import gov.nih.nci.system.query.cql.CQLObject;
import gov.nih.nci.system.query.cql.CQLPredicate;
import gov.nih.nci.system.query.cql.CQLQuery;

import java.util.Collection;
import java.util.Iterator;

import test.gov.nih.nci.cacoresdk.SDKTestBase;

public class O2OBidirectionalTest extends SDKTestBase
{
	public static String getTestCaseName()
	{
		return "One to One Bidirectional Test Case";
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
		Product searchObject = new Product();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.Product",searchObject );

		assertNotNull(results);
		assertEquals(3,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Product result = (Product)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getName());
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
		OrderLine searchObject = new OrderLine();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.OrderLine",searchObject );

		assertNotNull(results);
		assertEquals(5,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			OrderLine result = (OrderLine)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getName());
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
		Product searchObject = new Product();
		searchObject.setId(new Integer(3));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.Product",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Iterator i = results.iterator();
		Product result = (Product)i.next();
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getName());
		
		OrderLine orederLine = result.getLine();
		assertNull(orederLine);
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
		Product searchObject = new Product();
		searchObject.setId(new Integer(3));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.OrderLine",searchObject );

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
		Product searchObject = new Product();
		searchObject.setId(new Integer(1));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.Product",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Iterator i = results.iterator();
		Product result = (Product)i.next();
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getName());
		
		OrderLine orderLine = result.getLine();
		assertNotNull(orderLine);
		
		assertNotNull(orderLine);
		assertNotNull(orderLine.getId());
		assertNotNull(orderLine.getName());
		assertEquals(new Integer(1),orderLine.getId());
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
		Product searchObject = new Product();
		searchObject.setId(new Integer(1));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.OrderLine",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Iterator i = results.iterator();
		
		OrderLine orderLine = (OrderLine)i.next();
		assertNotNull(orderLine);
		
		assertNotNull(orderLine);
		assertNotNull(orderLine.getId());
		assertNotNull(orderLine.getName());
		assertEquals(new Integer(1),orderLine.getId());
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
	public void testOneAssociatedObjectNestedSearch3() throws ApplicationException
	{
		OrderLine searchObject = new OrderLine();
		searchObject.setId(new Integer(1));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.Product",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Iterator i = results.iterator();
		
		Product product = (Product)i.next();
		assertNotNull(product);
		
		assertNotNull(product);
		assertNotNull(product.getId());
		assertNotNull(product.getName());
		assertEquals(new Integer(1),product.getId());
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
	public void testOneAssociatedObjectCQL1() throws ApplicationException
	{
		CQLQuery cqlQuery = new CQLQuery();
		CQLObject target = new CQLObject();
		
		CQLAssociation association = new CQLAssociation();
		association.setName("gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.Product");
		association.setAttribute(new CQLAttribute("id",CQLPredicate.EQUAL_TO,"1"));
		association.setTargetRoleName("product");
		
		target.setName("gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.OrderLine");
		target.setAssociation(association);
		cqlQuery.setTarget(target);

		Collection results = getApplicationService().query(cqlQuery,"gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.OrderLine");

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Iterator i = results.iterator();
		
		OrderLine orderLine = (OrderLine)i.next();
		assertNotNull(orderLine);
		
		assertNotNull(orderLine);
		assertNotNull(orderLine.getId());
		assertNotNull(orderLine.getName());
		assertEquals(new Integer(1),orderLine.getId());
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
	public void testOneAssociatedObjectCQL2() throws ApplicationException
	{
		CQLQuery cqlQuery = new CQLQuery();
		CQLObject target = new CQLObject();
		
		CQLAssociation association = new CQLAssociation();
		association.setName("gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.OrderLine");
		association.setAttribute(new CQLAttribute("id",CQLPredicate.EQUAL_TO,"1"));
		association.setTargetRoleName("line");
		
		target.setName("gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.Product");
		target.setAssociation(association);
		cqlQuery.setTarget(target);

		Collection results = getApplicationService().query(cqlQuery,"gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.Product");

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Iterator i = results.iterator();
		
		Product product = (Product)i.next();
		assertNotNull(product);
		
		assertNotNull(product);
		assertNotNull(product.getId());
		assertNotNull(product.getName());
		assertEquals(new Integer(1),product.getId());
	}	
	
	/**
	 * Uses CQL Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set is 0
	 * 
	 * @throws ApplicationException
	 */
	public void testZeroAssociatedObjectCQL() throws ApplicationException
	{
		CQLQuery cqlQuery = new CQLQuery();
		CQLObject target = new CQLObject();
		
		CQLAssociation association = new CQLAssociation();
		association.setName("gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.Product");
		association.setAttribute(new CQLAttribute("id",CQLPredicate.EQUAL_TO,"3"));
		association.setTargetRoleName("product");
		
		target.setName("gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.OrderLine");
		target.setAssociation(association);
		cqlQuery.setTarget(target);

		Collection results = getApplicationService().query(cqlQuery,"gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.OrderLine");

		assertNotNull(results);
		assertEquals(0,results.size());
	}
	
	public void testGetMethods1() throws ApplicationException
	{
		Product searchObject = new Product();
		searchObject.setId(new Integer(1));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.Product",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Product result = (Product)results.iterator().next();
		assertEquals(new Integer(1),result.getLine().getId());


		searchObject.setId(new Integer(2));
		results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.Product",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		result = (Product)results.iterator().next();
		assertEquals(new Integer(2),result.getLine().getId());

		searchObject.setId(new Integer(3));
		results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.Product",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		result = (Product)results.iterator().next();
		assertNull(result.getLine());
		
	}


	public void testGetMethods2() throws ApplicationException
	{
		OrderLine searchObject = new OrderLine();
		searchObject.setId(new Integer(1));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.OrderLine",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		OrderLine result = (OrderLine)results.iterator().next();
		assertEquals(new Integer(1),result.getProduct().getId());


		searchObject.setId(new Integer(2));
		results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.OrderLine",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		result = (OrderLine)results.iterator().next();
		assertEquals(new Integer(2),result.getProduct().getId());

		searchObject.setId(new Integer(3));
		results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.OrderLine",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		result = (OrderLine)results.iterator().next();
		assertNull(result.getProduct());
		
	}
}
