/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk.domain.manytomany;

import junit.framework.Test;
import junit.framework.TestSuite;
import test.gov.nih.nci.cacoresdk.domain.manytomany.bidirectional.M2MBidirectionalWritableApiTest;
import test.gov.nih.nci.cacoresdk.domain.manytomany.unidirectional.M2MUnidirectionalWritableApiTest;

public class Many2ManyWritableApiSuite
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for Many to Many WritableApi package");
		suite.addTest(new TestSuite(M2MBidirectionalWritableApiTest.class,M2MBidirectionalWritableApiTest.getTestCaseName()));
		suite.addTest(new TestSuite(M2MUnidirectionalWritableApiTest.class,M2MUnidirectionalWritableApiTest.getTestCaseName()));
		return suite;
	}
}
