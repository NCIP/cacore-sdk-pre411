package test.gov.nih.nci.cacoresdk.domain.onetomany;

import junit.framework.Test;
import junit.framework.TestSuite;
import test.gov.nih.nci.cacoresdk.domain.onetomany.bidirectional.O2MBidirectionalTest;
import test.gov.nih.nci.cacoresdk.domain.onetomany.unidirectional.O2MUnidirectionalTest;

public class One2ManySuite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for One to Many package");
		suite.addTest(new TestSuite(O2MBidirectionalTest.class,O2MBidirectionalTest.getTestCaseName()));
		suite.addTest(new TestSuite(O2MUnidirectionalTest.class,O2MUnidirectionalTest.getTestCaseName()));
		return suite;
	}
}
