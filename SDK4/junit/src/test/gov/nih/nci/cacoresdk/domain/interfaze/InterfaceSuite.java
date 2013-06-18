/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk.domain.interfaze;

import junit.framework.Test;
import junit.framework.TestSuite;

public class InterfaceSuite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for Interface package");
		suite.addTest(new TestSuite(InterfaceTest.class,InterfaceTest.getTestCaseName()));
		return suite;
	}
}
