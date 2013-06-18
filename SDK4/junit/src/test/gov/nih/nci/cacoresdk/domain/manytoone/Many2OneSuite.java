/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk.domain.manytoone;

import junit.framework.Test;
import junit.framework.TestSuite;
import test.gov.nih.nci.cacoresdk.domain.manytoone.unidirectional.M2OUnidirectionalTest;
import test.gov.nih.nci.cacoresdk.domain.manytoone.unidirectional.withjoin.M2OUnidirectionalWJoinTest;

public class Many2OneSuite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for Many to One package");
		suite.addTest(new TestSuite(M2OUnidirectionalTest.class,M2OUnidirectionalTest.getTestCaseName()));
		suite.addTest(new TestSuite(M2OUnidirectionalWJoinTest.class,M2OUnidirectionalWJoinTest.getTestCaseName()));
		return suite;
	}
}
