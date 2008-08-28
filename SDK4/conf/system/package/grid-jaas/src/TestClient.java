import gov.nih.nci.system.security.authentication.cagrid.GridAuthenticationHelper;
import org.globus.gsi.GlobusCredential;

public class TestClient
{
	
	public static void main(String args[]) throws Exception
	{
		String username = "dorian";
		String password = "DorianAdmin$1";
		GridAuthenticationHelper loginHelper = new GridAuthenticationHelper("@CSM_PROJECT_NAME@");
		GlobusCredential proxy = loginHelper.login(username,password);
		System.out.println(proxy);
	}
	
}