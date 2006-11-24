package test.gov.nih.nci.cacoresdk.domain.inheritnance.childwithassociation;

import gov.nih.nci.cacoresdk.domain.other.datatype.AllDataType;
import gov.nih.nci.system.applicationservice.ApplicationException;
import test.gov.nih.nci.cacoresdk.SDKTestBase;

public class ChildWithAssociationTest extends SDKTestBase
{
	public static String getTestCaseName()
	{
		return "Child With Association Test Case";
	}
	
	public void testChildWithAssociation1() throws ApplicationException
	{
		getApplicationService().search(AllDataType.class, new AllDataType());
	}
}
