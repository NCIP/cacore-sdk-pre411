package test.gov.nih.nci.cacoresdk.domain.inheritnance.onechild;

import gov.nih.nci.cacoresdk.domain.other.datatype.AllDataType;
import gov.nih.nci.system.applicationservice.ApplicationException;
import test.gov.nih.nci.cacoresdk.SDKTestBase;

public class OneChildTest extends SDKTestBase
{
	public static String getTestCaseName()
	{
		return "One Child Test Case";
	}
	
	public void testOneChild1() throws ApplicationException
	{
		getApplicationService().search(AllDataType.class, new AllDataType());
	}
}
