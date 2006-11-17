package test.gov.nih.nci.cacoresdk.domain.inheritnance;

import junit.framework.Test;
import junit.framework.TestSuite;
import test.gov.nih.nci.cacoresdk.domain.inheritnance.childwithassociation.ChildWithAssociationTest;
import test.gov.nih.nci.cacoresdk.domain.inheritnance.multiplechild.MultipleChildTest;
import test.gov.nih.nci.cacoresdk.domain.inheritnance.onechild.OneChildTest;
import test.gov.nih.nci.cacoresdk.domain.inheritnance.parentwithassociation.ParentWithAssociationTest;
import test.gov.nih.nci.cacoresdk.domain.inheritnance.twolevelinheritance.TwoLevelInheritanceTest;

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
