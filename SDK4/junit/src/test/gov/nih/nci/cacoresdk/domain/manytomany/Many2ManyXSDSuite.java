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
import test.gov.nih.nci.cacoresdk.domain.manytomany.bidirectional.M2MBidirectionalXSDTest;
import test.gov.nih.nci.cacoresdk.domain.manytomany.unidirectional.M2MUnidirectionalXSDTest;

public class Many2ManyXSDSuite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for Many to Many XSD Package");
		suite.addTest(new TestSuite(M2MBidirectionalXSDTest.class,M2MBidirectionalXSDTest.getTestCaseName()));
		suite.addTest(new TestSuite(M2MUnidirectionalXSDTest.class,M2MUnidirectionalXSDTest.getTestCaseName()));
		return suite;
	}
}
