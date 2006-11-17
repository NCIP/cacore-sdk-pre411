package test.gov.nih.nci.cacoresdk.domain.inheritnance.multiplechild;

import gov.nih.nci.cacoresdk.domain.other.datatype.AllDataType;
import gov.nih.nci.system.applicationservice.ApplicationException;
import test.gov.nih.nci.cacoresdk.SDKTestBase;

public class MultipleChildTest extends SDKTestBase
{
	public static String getTestCaseName()
	{
		return "Multiple Child Test Case";
	}
	
	public void testMultipleChild1() throws ApplicationException
	{
		getApplicationService().search(AllDataType.class, new AllDataType());
	}
}
