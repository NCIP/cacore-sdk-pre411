package test.gov.nih.nci.cacoresdk.domain.inheritnance.twolevelinheritance;

import gov.nih.nci.cacoresdk.domain.other.datatype.AllDataType;
import gov.nih.nci.system.applicationservice.ApplicationException;
import test.gov.nih.nci.cacoresdk.SDKTestBase;

public class TwoLevelInheritanceTest extends SDKTestBase
{
	public static String getTestCaseName()
	{
		return "Two Level Inheritnace Test Case";
	}
	
	public void testTwoLevelInheritnace1() throws ApplicationException
	{
		getApplicationService().search(AllDataType.class, new AllDataType());
	}
}
