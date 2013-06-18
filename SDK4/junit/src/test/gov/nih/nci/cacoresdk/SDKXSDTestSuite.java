/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk;
import junit.framework.Test;
import junit.framework.TestSuite;
import test.gov.nih.nci.cacoresdk.domain.inheritance.InheritanceXSDSuite;
import test.gov.nih.nci.cacoresdk.domain.interfaze.InterfaceXSDSuite;
import test.gov.nih.nci.cacoresdk.domain.manytomany.Many2ManyXSDSuite;
import test.gov.nih.nci.cacoresdk.domain.manytoone.Many2OneXSDSuite;
import test.gov.nih.nci.cacoresdk.domain.onetomany.One2ManyXSDSuite;
import test.gov.nih.nci.cacoresdk.domain.onetoone.One2OneXSDSuite;
import test.gov.nih.nci.cacoresdk.domain.other.OtherXSDSuite;

/**
 * @author Dan Dumitru
 *
 */
public class SDKXSDTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for XSD caCORE SDK");
		suite.addTest(OtherXSDSuite.suite());
		suite.addTest(InheritanceXSDSuite.suite());
		suite.addTest(InterfaceXSDSuite.suite());
		suite.addTest(Many2ManyXSDSuite.suite());
		suite.addTest(One2ManyXSDSuite.suite());
		suite.addTest(Many2OneXSDSuite.suite());
		suite.addTest(One2OneXSDSuite.suite());
		return suite;
	}
}
