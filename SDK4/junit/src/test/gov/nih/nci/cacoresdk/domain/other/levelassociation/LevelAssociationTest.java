package test.gov.nih.nci.cacoresdk.domain.other.levelassociation;

import java.util.Collection;
import java.util.Iterator;

import gov.nih.nci.cacoresdk.domain.other.levelassociation.Card;
import gov.nih.nci.cacoresdk.domain.other.levelassociation.Deck;
import gov.nih.nci.cacoresdk.domain.other.levelassociation.Hand;
import gov.nih.nci.cacoresdk.domain.other.levelassociation.Suit;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.query.cql.CQLAssociation;
import gov.nih.nci.system.query.cql.CQLAttribute;
import gov.nih.nci.system.query.cql.CQLGroup;
import gov.nih.nci.system.query.cql.CQLLogicalOperator;
import gov.nih.nci.system.query.cql.CQLObject;
import gov.nih.nci.system.query.cql.CQLPredicate;
import gov.nih.nci.system.query.cql.CQLQuery;
import test.gov.nih.nci.cacoresdk.SDKTestBase;

public class LevelAssociationTest extends SDKTestBase
{
	public static String getTestCaseName()
	{
		return "Level association Test Case";
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
		Hand searchObject = new Hand();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.other.levelassociation.Hand",searchObject );

		assertNotNull(results);
		assertEquals(5,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Hand result = (Hand)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
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
		Card searchObject = new Card();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.other.levelassociation.Card",searchObject );

		assertNotNull(results);
		assertEquals(53,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Card result = (Card)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
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
		Suit searchObject = new Suit();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.other.levelassociation.Suit",searchObject );

		assertNotNull(results);
		assertEquals(4,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Suit result = (Suit)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
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
		Deck searchObject = new Deck();
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.other.levelassociation.Deck",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Deck result = (Deck)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
		}
	}


	/**
	 * Verifies all the associations in the object graph
	 * 
	 * @throws ApplicationException
	 */
	public void testNullAssociationsInBeans() throws ApplicationException
	{
		Card searchObject = new Card();
		searchObject.setId(new Integer(53));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.other.levelassociation.Card",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		Iterator i = results.iterator();
		Card card = (Card)i.next();
		
		assertNotNull(card);
		assertNotNull(card.getId());
		
		Suit suit = card.getSuit();
		
		assertNull(suit);
	}

	/**
	 * Verifies all the associations in the object graph
	 * 
	 * @throws ApplicationException
	 */
	public void testAssociationsInBeans() throws ApplicationException
	{
		Hand searchObject = new Hand();
		searchObject.setId(new Integer(1));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.other.levelassociation.Card",searchObject );

		assertNotNull(results);
		assertEquals(3,results.size());
		
		Iterator i = results.iterator();
		Card card = (Card)i.next();
		
		assertNotNull(card);
		assertNotNull(card.getId());
		
		Suit suit = card.getSuit();
		
		assertNotNull(suit);
		assertEquals(suit.getCardCollection().size(),13);
		
		Deck deck = suit.getDeck();
		
		assertNotNull(deck);
		assertEquals(deck.getSuitCollection().size(),4);
	}
	
	/**
	 * Uses Nested Search Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * 
	 * @throws ApplicationException
	 */
	public void testOneLevelAssociationNestedSearch1() throws ApplicationException
	{
		Hand searchObject = new Hand();
		searchObject.setId(new Integer(1));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.other.levelassociation.Card",searchObject );

		assertNotNull(results);
		assertEquals(3,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Card result = (Card)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
		}
	}

	/**
	 * Uses Nested Search Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * 
	 * @throws ApplicationException
	 */
	public void testOneLevelAssociationNestedSearch2() throws ApplicationException
	{
		Hand searchObject = new Hand();
		searchObject.setId(new Integer(1));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.other.levelassociation.Card,gov.nih.nci.cacoresdk.domain.other.levelassociation.Hand",searchObject );

		assertNotNull(results);
		assertEquals(3,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Card result = (Card)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
		}
	}

	/**
	 * Uses Nested Search Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * 
	 * @throws ApplicationException
	 */
	public void testTwoLevelAssociationNestedSearch() throws ApplicationException
	{
		Hand searchObject = new Hand();
		searchObject.setId(new Integer(1));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.other.levelassociation.Suit,gov.nih.nci.cacoresdk.domain.other.levelassociation.Card",searchObject );

		assertNotNull(results);
		assertEquals(3,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Suit result = (Suit)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
		}
	}

	/**
	 * Uses Nested Search Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * 
	 * @throws ApplicationException
	 */
	public void testThreeLevelAssociationNestedSearch() throws ApplicationException
	{
		Hand searchObject = new Hand();
		searchObject.setId(new Integer(1));
		Collection results = getApplicationService().search("gov.nih.nci.cacoresdk.domain.other.levelassociation.Deck,gov.nih.nci.cacoresdk.domain.other.levelassociation.Suit,gov.nih.nci.cacoresdk.domain.other.levelassociation.Card",searchObject );

		assertNotNull(results);
		assertEquals(1,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Deck result = (Deck)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
		}
	}

	/**
	 * Uses CQL Search Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * 
	 * @throws ApplicationException
	 */
	public void testOneLevelAssociationCQLSearch1() throws ApplicationException
	{
		CQLQuery query = new CQLQuery();
		CQLObject target = new CQLObject();
		target.setName("gov.nih.nci.cacoresdk.domain.other.levelassociation.Card");
		CQLAssociation association = new CQLAssociation();
		association.setName("gov.nih.nci.cacoresdk.domain.other.levelassociation.Hand");
		association.setSourceRoleName("cardCollection");
		association.setAttribute(new CQLAttribute("id",CQLPredicate.EQUAL_TO,"1"));
		target.setAssociation(association);
		query.setTarget(target);
		
		Collection results = getApplicationService().query(query,"gov.nih.nci.cacoresdk.domain.other.levelassociation.Card");

		assertNotNull(results);
		assertEquals(3,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Card result = (Card)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
		}
	}

	/**
	 * Uses CQL Search Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * 
	 * @throws ApplicationException
	 */
	public void testOneLevelAssociationCQLSearch2() throws ApplicationException
	{
		CQLQuery query = new CQLQuery();
		CQLObject target = new CQLObject();
		target.setName("gov.nih.nci.cacoresdk.domain.other.levelassociation.Card");
		
		CQLAssociation association1 = new CQLAssociation();
		association1.setName("gov.nih.nci.cacoresdk.domain.other.levelassociation.Hand");
		association1.setSourceRoleName("cardCollection");
		association1.setAttribute(new CQLAttribute("id",CQLPredicate.EQUAL_TO,"1"));
		
		CQLAssociation association2 = new CQLAssociation();
		association2.setName("gov.nih.nci.cacoresdk.domain.other.levelassociation.Hand");
		association2.setSourceRoleName("cardCollection");
		association2.setAttribute(new CQLAttribute("id",CQLPredicate.EQUAL_TO,"2"));
		
		CQLGroup group = new CQLGroup();
		group.setLogicOperator(CQLLogicalOperator.OR);
		group.addAssociation(association1);
		group.addAssociation(association2);
		
		target.setAttribute(new CQLAttribute("Name",CQLPredicate.EQUAL_TO,"Ace"));
		target.setGroup(group);
		query.setTarget(target);
		
		Collection results = getApplicationService().query(query,"gov.nih.nci.cacoresdk.domain.other.levelassociation.Card");

		assertNotNull(results);
		assertEquals(1,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Card result = (Card)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
		}
	}
	
	/**
	 * Uses CQL Search Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * 
	 * @throws ApplicationException
	 */
	public void testTwoLevelAssociationCQLSearch() throws ApplicationException
	{
		CQLQuery query = new CQLQuery();
		CQLObject target = new CQLObject();
		target.setName("gov.nih.nci.cacoresdk.domain.other.levelassociation.Suit");

		CQLAssociation association1 = new CQLAssociation();
		association1.setName("gov.nih.nci.cacoresdk.domain.other.levelassociation.Hand");
		association1.setSourceRoleName("cardCollection");
		association1.setAttribute(new CQLAttribute("id",CQLPredicate.EQUAL_TO,"1"));
		
		CQLAssociation association2 = new CQLAssociation();
		association2.setName("gov.nih.nci.cacoresdk.domain.other.levelassociation.Card");
		association2.setTargetRoleName("cardCollection");
		association2.setAssociation(association1);
		
		target.setAssociation(association2);
		query.setTarget(target);
		
		Collection results = getApplicationService().query(query,"gov.nih.nci.cacoresdk.domain.other.levelassociation.Suit");

		assertNotNull(results);
		assertEquals(3,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Suit result = (Suit)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
		}
	}

	/**
	 * Uses CQL Search Criteria for search
	 * Verifies that the results are returned 
	 * Verifies size of the result set
	 * 
	 * @throws ApplicationException
	 */
	public void testThreeLevelAssociationCQLSearch() throws ApplicationException
	{
		CQLQuery query = new CQLQuery();
		CQLObject target = new CQLObject();
		target.setName("gov.nih.nci.cacoresdk.domain.other.levelassociation.Deck");

		CQLAssociation association1 = new CQLAssociation();
		association1.setName("gov.nih.nci.cacoresdk.domain.other.levelassociation.Hand");
		association1.setSourceRoleName("cardCollection");
		association1.setAttribute(new CQLAttribute("id",CQLPredicate.EQUAL_TO,"1"));
		
		CQLAssociation association2 = new CQLAssociation();
		association2.setName("gov.nih.nci.cacoresdk.domain.other.levelassociation.Card");
		association2.setTargetRoleName("cardCollection");
		association2.setAssociation(association1);

		CQLAssociation association3 = new CQLAssociation();
		association3.setName("gov.nih.nci.cacoresdk.domain.other.levelassociation.Suit");
		association3.setTargetRoleName("suitCollection");
		association3.setAssociation(association2);
		
		target.setAssociation(association3);
		query.setTarget(target);
		
		Collection results = getApplicationService().query(query,"gov.nih.nci.cacoresdk.domain.other.levelassociation.Deck");

		assertNotNull(results);
		assertEquals(1,results.size());
		
		for(Iterator i = results.iterator();i.hasNext();)
		{
			Deck result = (Deck)i.next();
			assertNotNull(result);
			assertNotNull(result.getId());
		}
	}
}
