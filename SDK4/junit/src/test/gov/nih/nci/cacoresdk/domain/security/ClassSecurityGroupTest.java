/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk.domain.security;

import gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Bank;
import gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Cash;
import gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Credit;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.query.cql.CQLObject;
import gov.nih.nci.system.query.cql.CQLQuery;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.acegisecurity.AccessDeniedException;
import org.apache.commons.codec.binary.Base64;
import org.hibernate.criterion.DetachedCriteria;

import test.gov.nih.nci.cacoresdk.SDKSecurityTestBase;

public class ClassSecurityGroupTest extends SDKSecurityTestBase
{
	public static String getTestCaseName()
	{
		return "Class Security Group Test Case";
	}
	
	Collection<String> groups = new ArrayList<String>();
	
	/**
	 * Uses Query by Example search API, which takes the 
	 * target class and a list of criteria objects
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attributes are null 
	 * since user1 has access to all target class attributes
	 * 
	 * @throws Exception
	 */
	public void testClassListSearch() throws Exception
	{
		Bank bank1 = new Bank();
		Bank bank2 = new Bank();
		Bank bank3 = new Bank();
		Bank bank4 = new Bank();
		
		bank1.setId(1);
		bank2.setId(2);
		bank3.setId(3);
		bank4.setId(4);
		
		List<Bank> objList = new ArrayList<Bank>();
		
		objList.add(bank1);
		objList.add(bank2);
		objList.add(bank3);
		objList.add(bank4);
		
		groups.clear();
		groups.add("Group1");
		
		Collection results = getApplicationService(groups).search(Bank.class,objList);

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
	 * Uses Query by Example search API, which takes the 
	 * target class and a list of criteria objects
	 * Verifies that an "AccessDeniedException" is thrown 
	 * 
	 * @throws Exception
	 */
	public void testAccessDeniedClassListSearch() throws Exception
	{	
		Cash cash1 = new Cash();
		Cash cash2 = new Cash();
		
		cash1.setId(1);
		cash2.setId(2);
		
		List<Cash> objList = new ArrayList<Cash>();
		
		objList.add(cash1);
		objList.add(cash2);
		
		// Test Access Denied check - user2 does not have access to Cash class
		boolean flag = false;
		try {
			groups.clear();
			groups.add("Group2");
			getApplicationService(groups).search(Cash.class,objList);
		} catch(AccessDeniedException e){
			flag = true;
		}
		assertTrue(flag);
	}
	
	/**
	 * Uses Query by Example search API, which takes the 
	 * target class and a list of criteria objects
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attributes are null 
	 * since user1 has access to all target class attributes
	 * 
	 * @throws Exception
	 */
	public void testPathListSearch() throws Exception
	{
		Bank bank1 = new Bank();
		Bank bank2 = new Bank();
		Bank bank3 = new Bank();
		Bank bank4 = new Bank();
		
		bank1.setId(1);
		bank2.setId(2);
		bank3.setId(3);
		bank4.setId(4);
		
		List<Bank> objList = new ArrayList<Bank>();
		
		objList.add(bank1);
		objList.add(bank2);
		objList.add(bank3);
		objList.add(bank4);
		
		groups.clear();
		groups.add("Group1");
		Collection results = getApplicationService(groups).search(Bank.class.getName(),objList);

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
	 * Uses Query by Example search API, which takes the 
	 * target class and a list of criteria objects
	 * Verifies that an "AccessDeniedException" is thrown 
	 * 
	 * @throws Exception
	 */
	public void testAccessDeniedPathListSearch() throws Exception
	{
		Cash cash1 = new Cash();
		Cash cash2 = new Cash();
		
		cash1.setId(1);
		cash2.setId(2);
		
		List<Cash> objList = new ArrayList<Cash>();
		
		objList.add(cash1);
		objList.add(cash2);
		
		// Test Access Denied check - user2 does not have access to Cash class
		boolean flag = false;
		try {
			groups.clear();
			groups.add("Group2");
			getApplicationService(groups).search(Cash.class.getName(),objList);
		} catch(AccessDeniedException e){
			flag = true;
		}
		assertTrue(flag);
	}	

	/**
	 * Uses Query by Example search API, which takes the 
	 * target class and a search object
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attributes are null 
	 * since user1 has access to all target class attributes
	 * 
	 * @throws Exception
	 */
	public void testClassObjectSearch() throws Exception
	{
		Bank bank1 = new Bank();
		bank1.setId(1);

		groups.clear();
		groups.add("Group1");
		Collection results = getApplicationService(groups).search(Bank.class,bank1);

		assertNotNull(results);
		assertEquals(1,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Bank result = (Bank)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getName());
		}
	}	

	/**
	 * Uses Query by Example search API, which takes the 
	 * target class and a search object
	 * Verifies that an "AccessDeniedException" is thrown 
	 * 
	 * @throws Exception
	 */
	public void testAccessDeniedClassObjectSearch() throws Exception
	{
		Cash credit = new Cash();
		credit.setId(3);
		
		// Test Access Denied check - user2 does not have access to Cash class
		boolean flag = false;
		try {
			groups.clear();
			groups.add("Group2");
			getApplicationService(groups).search(Cash.class,credit);
		} catch(AccessDeniedException e){
			flag = true;
		}
		assertTrue(flag);
	}
	

	/**
	 * Uses Query by Example search API, which takes the 
	 * target class name and a search object
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attributes are null 
	 * since user1 has access to all target class attributes
	 * 
	 * @throws Exception
	 */
	public void testPathObjectSearch() throws Exception
	{
		Bank searchObject = new Bank();
		groups.clear();
		groups.add("Group1");
		Collection results = getApplicationService(groups).search(Bank.class.getName(),searchObject );

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
	 * Uses Query by Example search API, which takes the 
	 * target class name and a search object
	 * Verifies that an "AccessDeniedException" is thrown 
	 * 
	 * @throws Exception
	 */
	public void testAccessDeniedPathObjectSearch() throws Exception
	{
		Cash searchObject = new Cash();
		
		// Test Access Denied check - user2 does not have access to Cash class
		boolean flag = false;
		try {
			groups.clear();
			groups.add("Group2");
			getApplicationService(groups).search(Cash.class.getName(),searchObject );
		} catch(AccessDeniedException e){
			flag = true;
		}
		assertTrue(flag);
	}
	
	/**
	 * Uses Query by Example query API, which takes the 
	 * detached criteria instance and the target class name
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attributes are null 
	 * since user1 has access to all target class attributes
	 * 
	 * @throws Exception
	 */
	public void testCritieriaFirstRowPathQuery() throws Exception
	{
		
		DetachedCriteria detachedCrit = DetachedCriteria.forClass(Bank.class);

		groups.clear();
		groups.add("Group1");
		Collection results = getApplicationService(groups).query(detachedCrit, 1, Bank.class.getName());

		assertNotNull(results);
		assertEquals(3,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Bank result = (Bank)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
			assertNotNull(result.getName());
		}
	}	
	
	/**
	 * Uses Query by Example query API, which takes the 
	 * detached criteria instance and the target class name
	 * Verifies that an "AccessDeniedException" is thrown 
	 * 
	 * @throws Exception
	 */
	public void testAccessDeniedCritieriaFirstRowPathQuery() throws Exception
	{
		
		DetachedCriteria detachedCrit = DetachedCriteria.forClass(Cash.class);
		
		// Test Access Denied check - user2 does not have access to Cash class
		boolean flag = false;
		try {
			groups.clear();
			groups.add("Group2");
			getApplicationService(groups).query(detachedCrit, 1, Cash.class.getName());
		} catch(AccessDeniedException e){
			flag = true;
		}
		assertTrue(flag);
	}
	
	/**
	 * Uses Query by Example Row Count query, which takes a 
	 * detached criteria instance and the target class name
	 * Verifies that the count is returned 
	 * Verifies that the count value is accurate
	 * 
	 * @throws Exception
	 */
	public void testRowCountQuery() throws Exception
	{
		
		DetachedCriteria detachedCrit = DetachedCriteria.forClass(Bank.class);

		groups.clear();
		groups.add("Group1");
		int count = getApplicationService(groups).getQueryRowCount(detachedCrit, Bank.class.getName());

		assertNotNull(count);
		assertEquals(4,count);
	}		
	
	/**
	 * Uses Query by Example Row Count query, which takes a 
	 * detached criteria instance and the target class name
	 * Verifies that an "AccessDeniedException" is thrown 
	 * 
	 * @throws Exception
	 */
	public void testAccessDeniedRowCountQuery() throws Exception
	{
		
		DetachedCriteria detachedCrit = DetachedCriteria.forClass(Cash.class);
		
		// Test Access Denied check - user2 does not have access to Cash class
		boolean flag = false;
		try {
			groups.clear();
			groups.add("Group2");
			getApplicationService(groups).getQueryRowCount(detachedCrit, Cash.class.getName());
		} catch(AccessDeniedException e){
			flag = true;
		}
		assertTrue(flag);
	}
	
	/**
	 * Uses Query by Example Get Max Records query, which takes a 
	 * detached criteria instance and the target class name
	 * Verifies that the count is returned 
	 * Verifies that the count value is accurate
	 * 
	 * @throws Exception
	 */
	public void testGetMaxRecordsQuery() throws Exception
	{
		groups.clear();
		groups.add("Group1");
		int count = getApplicationService(groups).getMaxRecordsCount();

		assertNotNull(count);
		assertEquals(1000,count);
	}

	/**
	 * Uses Query by Example search API, which takes a search 
	 * path and search object.
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attributes are null 
	 * since user1 has access to all target class attributes
	 * Verifies that the Get Association query (called internally
	 * by the ApplicationService framework) returns the expected
	 * associated class results
	 * 
	 * @throws Exception
	 */
	public void testGetAssociationQuery() throws Exception
	{
		Credit searchObject = new Credit();
		groups.clear();
		groups.add("Group1");
		Collection results = getApplicationService(groups).search("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Credit",searchObject );

		assertNotNull(results);
		assertEquals(2,results.size());
		
		Bank bank;
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Credit credit = (Credit)i.next();
			assertNotNull(credit);
			assertNotNull(credit.getId());
			assertNotNull(credit.getAmount());
			assertNotNull(credit.getCardNumber());

			bank = credit.getIssuingBank();
			assertNotNull(bank);
			assertNotNull(bank.getId());
			assertNotNull(bank.getName());
		}
	}	
	
	/**
	 * Uses Query by Example query API, which takes a CQL query 
	 * object parameter.
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attributes are null 
	 * since user1 has access to all target class attributes
	 * 
	 * @throws Exception
	 */
	public void testCQLQuery() throws Exception
	{
		CQLQuery cqlQuery = new CQLQuery();
		CQLObject target = new CQLObject();
		
		target.setName("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Bank");
		cqlQuery.setTarget(target);

		groups.clear();
		groups.add("Group1");
		Collection results = getApplicationService(groups).query(cqlQuery);

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
	 * Uses Query by Example query API, which takes a CQL query 
	 * object parameter.
	 * Verifies that an "AccessDeniedException" is thrown 
	 * 
	 * @throws Exception
	 */
	public void testAccessDeniedCQLQuery() throws Exception
	{
		CQLQuery cqlQuery = new CQLQuery();
		CQLObject target = new CQLObject();
		
		target.setName("gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Cash");
		cqlQuery.setTarget(target);
		
		// Test Access Denied check - user2 does not have access to Cash class
		boolean flag = false;
		try {
			groups.clear();
			groups.add("Group2");
			getApplicationService(groups).query(cqlQuery);
		} catch(AccessDeniedException e){
			flag = true;
		}
		assertTrue(flag);
	}
	
	/**
	 * Uses Query by Example query API, which takes a HQL Criteria 
	 * object parameter.
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attributes are null 
	 * since user1 has access to all target class attributes
	 * 
	 * @throws Exception
	 */	
	public void testHQLQuery() throws Exception
	{
		String hql = "from gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Bank";
		HQLCriteria hqlCrit = new HQLCriteria(hql);

		groups.clear();
		groups.add("Group1");
		Collection results = getApplicationService(groups).query(hqlCrit);

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
	 * Uses Query by Example query API, which takes a HQL Criteria 
	 * object parameter.
	 * Verifies that an "AccessDeniedException" is thrown 
	 * 
	 * @throws Exception
	 */
	public void testAccessDeniedHQLQuery() throws Exception
	{
		String hql = "from gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.Cash";
		HQLCriteria hqlCrit = new HQLCriteria(hql);
		
		// Test Access Denied check - user2 does not have access to Cash class
		boolean flag = false;
		try {
			groups.clear();
			groups.add("Group2");
			getApplicationService(groups).query(hqlCrit);
		} catch(AccessDeniedException e){
			flag = true;
		}
		assertTrue(flag);
	}
	
	/**
	 * Uses Query by Example query API, which takes a Detached  
	 * Criteria object parameter.
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * Verifies that none of the attributes are null 
	 * since user1 has access to all target class attributes
	 * 
	 * @throws Exception
	 */		
	public void testDetachedCriteriaQuery() throws Exception
	{
		DetachedCriteria detachedCrit = DetachedCriteria.forClass(Bank.class);

		groups.clear();
		groups.add("Group1");
		Collection results = getApplicationService(groups).query(detachedCrit);

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
	 * Uses Query by Example query API, which takes a Detached  
	 * Criteria object parameter.
	 * Verifies that an "AccessDeniedException" is thrown 
	 * 
	 * @throws Exception
	 */	
	public void testAccessDeniedDetachedCriteriaQuery() throws Exception
	{
		DetachedCriteria detachedCrit = DetachedCriteria.forClass(Cash.class);

		// Test Access Denied check - user2 does not have access to Cash class
		boolean flag = false;
		try {
			groups.clear();
			groups.add("Group2");
			getApplicationService(groups).query(detachedCrit);
		} catch(AccessDeniedException e){
			flag = true;
		}
		assertTrue(flag);
	}
	
	public void testBadCredentials() throws Exception
	{	
		Cash cash1 = new Cash();
		Cash cash2 = new Cash();
		
		cash1.setId(1);
		cash2.setId(2);
		
		List<Cash> objList = new ArrayList<Cash>();
		
		objList.add(cash1);
		objList.add(cash2);
		
		boolean flag = false;
		
		// Test for bad credentials using a non-existent group
		try {
			groups.clear();
			groups.add("badGroup");
			getApplicationService(groups).search(Cash.class.getName(), new Cash());
		} catch(AccessDeniedException e){
			flag = true;
		}
		assertTrue(flag);
	}
}
