package test.gov.nih.nci.cacoresdk.domain.inheritance;

import junit.framework.Test;
import junit.framework.TestSuite;
import test.gov.nih.nci.cacoresdk.domain.inheritance.childwithassociation.ChildWithAssociationTest;
import test.gov.nih.nci.cacoresdk.domain.inheritance.multiplechild.MultipleChildTest;
import test.gov.nih.nci.cacoresdk.domain.inheritance.onechild.OneChildTest;
import test.gov.nih.nci.cacoresdk.domain.inheritance.parentwithassociation.ParentWithAssociationTest;
import test.gov.nih.nci.cacoresdk.domain.inheritance.twolevelinheritance.TwoLevelInheritanceTest;

public class InheritanceSuite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for Inheritance package");
		suite.addTest(new TestSuite(OneChildTest.class,OneChildTest.getTestCaseName()));
		suite.addTest(new TestSuite(TwoLevelInheritanceTest.class,TwoLevelInheritanceTest.getTestCaseName()));
		suite.addTest(new TestSuite(MultipleChildTest.class,MultipleChildTest.getTestCaseName()));
		suite.addTest(new TestSuite(ChildWithAssociationTest.class,ChildWithAssociationTest.getTestCaseName()));
		suite.addTest(new TestSuite(ParentWithAssociationTest.class,ParentWithAssociationTest.getTestCaseName()));
		return suite;
	}
}
