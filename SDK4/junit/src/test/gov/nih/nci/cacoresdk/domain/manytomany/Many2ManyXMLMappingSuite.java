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
import test.gov.nih.nci.cacoresdk.domain.manytomany.bidirectional.M2MBidirectionalXMLMappingTest;
import test.gov.nih.nci.cacoresdk.domain.manytomany.unidirectional.M2MUnidirectionalXMLMappingTest;

public class Many2ManyXMLMappingSuite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for Many to Many XML Mapping Package");
		suite.addTest(new TestSuite(M2MBidirectionalXMLMappingTest.class,M2MBidirectionalXMLMappingTest.getTestCaseName()));
		suite.addTest(new TestSuite(M2MUnidirectionalXMLMappingTest.class,M2MUnidirectionalXMLMappingTest.getTestCaseName()));
		return suite;
	}
}
