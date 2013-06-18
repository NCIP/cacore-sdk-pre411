/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk.domain.security;


import java.util.List;

import org.acegisecurity.AccessDeniedException;

import gov.nih.nci.cacoresdk.domain.other.levelassociation.Card;
import gov.nih.nci.cacoresdk.domain.other.levelassociation.Suit;
import gov.nih.nci.system.applicationservice.WritableApplicationService;
import gov.nih.nci.system.query.SDKQueryResult;
import gov.nih.nci.system.query.example.DeleteExampleQuery;
import gov.nih.nci.system.query.example.InsertExampleQuery;
import gov.nih.nci.system.query.example.UpdateExampleQuery;
import test.gov.nih.nci.cacoresdk.SDKSecurityTestBase;



public class WritableApiSecurityTest extends SDKSecurityTestBase{
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	
	public static String getTestCaseName() {
		return "WritableApi Security Test Case";
	}

	public void testCreateCardSuccess() throws Exception {
		WritableApplicationService applicationService = (WritableApplicationService) getApplicationService("user1", "password");
		
		Suit suit=new Suit();
		suit.setId(1);
		List<Suit> suits=applicationService.search(Suit.class, suit);
		Suit resultSuit = suits.get(0);
		Card card = new Card();
		card.setId(55);
		card.setName("card");
		card.setSuit(resultSuit);
		InsertExampleQuery exampleQuery=new InsertExampleQuery(card);
		SDKQueryResult queryResult=applicationService.executeQuery(exampleQuery);
		Card resultCard=(Card)queryResult.getObjectResult();
		assertEquals(card.getName(), resultCard.getName());
	}

	public void testCreateCardSuccessUser2() throws Exception {
		WritableApplicationService applicationService = (WritableApplicationService) getApplicationService("user2", "password");
		
		Suit suit=new Suit();
		suit.setId(1);
		List<Suit> suits=applicationService.search(Suit.class, suit);
		Suit resultSuit = suits.get(0);
		Card card = new Card();
		card.setId(56);
		card.setName("card");
		card.setSuit(resultSuit);
		InsertExampleQuery exampleQuery=new InsertExampleQuery(card);
		SDKQueryResult queryResult=applicationService.executeQuery(exampleQuery);
		Card resultCard=(Card)queryResult.getObjectResult();
		assertEquals(card.getName(), resultCard.getName());
	}

	public void testUpdateCardSuccess() throws Exception {
		WritableApplicationService applicationService = (WritableApplicationService) getApplicationService("user1", "password");
		
		Suit suit=new Suit();
		suit.setId(1);
		List<Suit> suits=applicationService.search(Suit.class, suit);
		Suit resultSuit = suits.get(0);
		Card card = new Card();
		card.setId(57);
		card.setName("card");
		card.setSuit(resultSuit);
		InsertExampleQuery exampleQuery=new InsertExampleQuery(card);
		SDKQueryResult queryResult=applicationService.executeQuery(exampleQuery);
		Card resultCard=(Card)queryResult.getObjectResult();
		assertEquals(card.getName(), resultCard.getName());

		card.setName("cardUpdate");
		UpdateExampleQuery updateExample=new UpdateExampleQuery(card);
		SDKQueryResult updateQueryResult=applicationService.executeQuery(updateExample);
		Card updateResultCard=(Card)updateQueryResult.getObjectResult();
		assertEquals(card.getName(), updateResultCard.getName());
	}

	public void testUpdateCardFailure() throws Exception {
		WritableApplicationService applicationService = (WritableApplicationService) getApplicationService("user2", "password");
		
		Suit suit=new Suit();
		suit.setId(1);
		List<Suit> suits=applicationService.search(Suit.class, suit);
		Suit resultSuit = suits.get(0);
		Card card = new Card();
		card.setId(58);
		card.setName("card");
		card.setSuit(resultSuit);
		InsertExampleQuery exampleQuery=new InsertExampleQuery(card);
		SDKQueryResult queryResult=applicationService.executeQuery(exampleQuery);
		Card resultCard=(Card)queryResult.getObjectResult();
		assertEquals(card.getName(), resultCard.getName());

		card.setName("cardUpdate");
		UpdateExampleQuery updateExample=new UpdateExampleQuery(card);
		try{
			applicationService.executeQuery(updateExample);
			fail("must through access denied exception");
		}catch (AccessDeniedException e) {
			assertEquals("Access is denied", e.getMessage());
		}
	}

	public void testDeleteCardSuccess() throws Exception {
		WritableApplicationService applicationService = (WritableApplicationService) getApplicationService("user1", "password");
		
		Suit suit=new Suit();
		suit.setId(1);
		List<Suit> suits=applicationService.search(Suit.class, suit);
		Suit resultSuit = suits.get(0);
		Card card = new Card();
		card.setId(59);
		card.setName("card");
		card.setSuit(resultSuit);
		InsertExampleQuery exampleQuery=new InsertExampleQuery(card);
		SDKQueryResult queryResult=applicationService.executeQuery(exampleQuery);
		Card resultCard=(Card)queryResult.getObjectResult();
		assertEquals(card.getName(), resultCard.getName());

		card.setName("cardUpdate");
		DeleteExampleQuery deleteExample=new DeleteExampleQuery(card);
		SDKQueryResult deleteQueryResult=applicationService.executeQuery(deleteExample);
		assertEquals(true,deleteQueryResult.getObjectResult());
	}

	public void testDeleteCardFailure() throws Exception {
		WritableApplicationService applicationService = (WritableApplicationService) getApplicationService("user2", "password");
		
		Suit suit=new Suit();
		suit.setId(1);
		List<Suit> suits=applicationService.search(Suit.class, suit);
		Suit resultSuit = suits.get(0);
		Card card = new Card();
		card.setId(59);
		card.setName("card");
		card.setSuit(resultSuit);
		InsertExampleQuery exampleQuery=new InsertExampleQuery(card);
		SDKQueryResult queryResult=applicationService.executeQuery(exampleQuery);
		Card resultCard=(Card)queryResult.getObjectResult();
		assertEquals(card.getName(), resultCard.getName());

		DeleteExampleQuery deleteExample=new DeleteExampleQuery(card);
		try {
			 applicationService.executeQuery(deleteExample);
			fail("must through access denied exception");
		} catch (AccessDeniedException e) {
			assertEquals("Access is denied", e.getMessage());
		}
	}
}
