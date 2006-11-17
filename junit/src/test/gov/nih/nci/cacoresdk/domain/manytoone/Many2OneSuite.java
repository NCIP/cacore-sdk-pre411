package test.gov.nih.nci.cacoresdk.domain.manytoone;

import junit.framework.Test;
import junit.framework.TestSuite;
import test.gov.nih.nci.cacoresdk.domain.manytoone.unidirectional.M2OUnidirectionalTest;

public class Many2OneSuite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for Many to One package");
		suite.addTest(new TestSuite(M2OUnidirectionalTest.class,M2OUnidirectionalTest.getTestCaseName()));
		return suite;
	}
}
