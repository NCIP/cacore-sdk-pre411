/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk;

import java.util.Collection;

import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import junit.framework.TestCase;

/**
 * @author Satish Patel, Dan Dumitru
 * 
 */
public abstract class SDKSecurityTestBase extends TestCase {

	protected boolean enableAttributeLevelSecurity=false;
	protected boolean enableInstanceLevelSecurity=false;
	
	protected void setUp() throws Exception {
		super.setUp();
		enableAttributeLevelSecurity = Boolean.parseBoolean(System.getProperty("enableAttributeLevelSecurity"));
		enableInstanceLevelSecurity = Boolean.parseBoolean(System.getProperty("enableInstanceLevelSecurity"));
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected ApplicationService getApplicationService(String userid,
			String password) throws Exception {
		return ApplicationServiceProvider.getApplicationService(userid,
				password);
	}
	
	protected ApplicationService getApplicationService(Collection<String> groups) throws Exception {
		return ApplicationServiceProvider.getApplicationService(groups);
	}

	public static String getTestCaseName() {
		return "SDK Security Base Test Case";
	}

}
