/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk.domain.interfaze;

import junit.framework.Test;
import junit.framework.TestSuite;

public class InterfaceXSDSuite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for Interface XSD package");
		suite.addTest(new TestSuite(InterfaceXSDTest.class,InterfaceXSDTest.getTestCaseName()));
		return suite;
	}
}
