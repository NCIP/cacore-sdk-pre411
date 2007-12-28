package test.gov.nih.nci.cacoresdk;

import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import junit.framework.TestCase;

/**
 * @author Satish Patel, Dan Dumitru
 * 
 */
public abstract class SDKSecurityTestBase extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected ApplicationService getApplicationService(String userid,
			String password) throws Exception {
		return ApplicationServiceProvider.getApplicationService(userid,
				password);
	}

	public static String getTestCaseName() {
		return "SDK Security Base Test Case";
	}

}
