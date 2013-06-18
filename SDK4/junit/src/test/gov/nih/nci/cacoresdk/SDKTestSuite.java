/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk;
import test.gov.nih.nci.cacoresdk.domain.inheritance.InheritanceSuite;
import test.gov.nih.nci.cacoresdk.domain.manytomany.Many2ManySuite;
import test.gov.nih.nci.cacoresdk.domain.manytoone.Many2OneSuite;
import test.gov.nih.nci.cacoresdk.domain.onetomany.One2ManySuite;
import test.gov.nih.nci.cacoresdk.domain.onetoone.One2OneSuite;
import test.gov.nih.nci.cacoresdk.domain.other.OtherSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Satish Patel
 *
 */
public class SDKTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for caCORE SDK");
		suite.addTest(OtherSuite.suite());
		suite.addTest(InheritanceSuite.suite());
		suite.addTest(Many2ManySuite.suite());
		suite.addTest(One2ManySuite.suite());
		suite.addTest(Many2OneSuite.suite());
		suite.addTest(One2OneSuite.suite());
		return suite;
	}
}
