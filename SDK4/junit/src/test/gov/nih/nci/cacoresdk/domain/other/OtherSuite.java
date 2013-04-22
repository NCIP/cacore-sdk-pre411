/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk.domain.other;

import junit.framework.Test;
import junit.framework.TestSuite;
import test.gov.nih.nci.cacoresdk.domain.other.datatype.AllDataTypeTest;
import test.gov.nih.nci.cacoresdk.domain.other.levelassociation.LevelAssociationTest;
import test.gov.nih.nci.cacoresdk.domain.other.primarykey.DoubleKeyTest;
import test.gov.nih.nci.cacoresdk.domain.other.primarykey.FloatKeyTest;
import test.gov.nih.nci.cacoresdk.domain.other.primarykey.IntegerKeyTest;
import test.gov.nih.nci.cacoresdk.domain.other.primarykey.StringKeyTest;

public class OtherSuite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for Other package");
		suite.addTest(new TestSuite(AllDataTypeTest.class,AllDataTypeTest.getTestCaseName()));
		suite.addTest(new TestSuite(DoubleKeyTest.class,DoubleKeyTest.getTestCaseName()));
		suite.addTest(new TestSuite(IntegerKeyTest.class,IntegerKeyTest.getTestCaseName()));
		suite.addTest(new TestSuite(FloatKeyTest.class,FloatKeyTest.getTestCaseName()));
		suite.addTest(new TestSuite(StringKeyTest.class,StringKeyTest.getTestCaseName()));
		suite.addTest(new TestSuite(LevelAssociationTest.class,LevelAssociationTest.getTestCaseName()));
		return suite;
	}
}
