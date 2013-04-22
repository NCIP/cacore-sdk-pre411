/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk.domain.onetomany;

import junit.framework.Test;
import junit.framework.TestSuite;
import test.gov.nih.nci.cacoresdk.domain.onetomany.bidirectional.O2MBidirectionalXSDTest;
import test.gov.nih.nci.cacoresdk.domain.onetomany.bidirectional.withjoin.O2MBidirectionalWJoinXSDTest;
import test.gov.nih.nci.cacoresdk.domain.onetomany.unidirectional.O2MUnidirectionalXSDTest;
import test.gov.nih.nci.cacoresdk.domain.onetomany.unidirectional.withjoin.O2MUnidirectionalWJoinXSDTest;

public class One2ManyXSDSuite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for One to Many package");
		suite.addTest(new TestSuite(O2MBidirectionalXSDTest.class,O2MBidirectionalXSDTest.getTestCaseName()));
		suite.addTest(new TestSuite(O2MBidirectionalWJoinXSDTest.class,O2MBidirectionalWJoinXSDTest.getTestCaseName()));
		suite.addTest(new TestSuite(O2MUnidirectionalXSDTest.class,O2MUnidirectionalXSDTest.getTestCaseName()));
		suite.addTest(new TestSuite(O2MUnidirectionalWJoinXSDTest.class,O2MUnidirectionalWJoinXSDTest.getTestCaseName()));
		return suite;
	}
}
