package test.gov.nih.nci.cacoresdk;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import junit.framework.TestCase;



/**
 * @author Satish Patel
 * 
 */
public abstract class SDKTestBase extends TestCase {

	private ApplicationService appService ;

	protected void setUp() throws Exception {
		super.setUp();
		appService = ApplicationServiceProvider.getApplicationService();
		//String url = "http://localhost:8080/example/http/remoteService";
		//appService = ApplicationServiceProvider.getRemoteInstance(url);
	}


	protected void tearDown() throws Exception 
	{
		appService = null;
		super.tearDown();
	}
	
	protected ApplicationService getApplicationService()
	{
		return appService;
	}
	
	public static String getTestCaseName()
	{
		return "SDK Base Test Case";
	}

}
