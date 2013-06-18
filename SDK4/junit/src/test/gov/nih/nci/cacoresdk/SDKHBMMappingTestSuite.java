/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk;

import test.gov.nih.nci.cacoresdk.domain.other.OtherHBMMappingSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public  class SDKHBMMappingTestSuite extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite("Suite for HBM Mapping Xml Files");
		suite.addTest(new TestSuite(AllHBMMappingTest.class,AllHBMMappingTest.getTestCaseName()));
		suite.addTest(OtherHBMMappingSuite.suite());
		return suite;
	}
}