package test.gov.nih.nci.cacoresdk.domain.inheritnance.parentwithassociation;

import gov.nih.nci.cacoresdk.domain.other.datatype.AllDataType;
import gov.nih.nci.system.applicationservice.ApplicationException;
import test.gov.nih.nci.cacoresdk.SDKTestBase;

public class ParentWithAssociationTest extends SDKTestBase
{
	public static String getTestCaseName()
	{
		return "Parent With Association Test Case";
	}
	
	public void testParentWithAssociation1() throws ApplicationException
	{
		getApplicationService().search(AllDataType.class, new AllDataType());
	}
}
