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
import test.gov.nih.nci.cacoresdk.domain.manytoone.unidirectional.M2OUnidirectionalWSTest;
import test.gov.nih.nci.cacoresdk.domain.manytoone.unidirectional.withjoin.M2OUnidirectionalWJoinWSTest;

public class Many2OneWSSuite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for Many to One WS Package");
		suite.addTest(new TestSuite(M2OUnidirectionalWSTest.class,M2OUnidirectionalWSTest.getTestCaseName()));
		suite.addTest(new TestSuite(M2OUnidirectionalWJoinWSTest.class,M2OUnidirectionalWJoinWSTest.getTestCaseName()));
		return suite;
	}
}
