package gov.nih.nci.system.security.authentication.cagrid;
import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class GridJAASCallbackHandler implements CallbackHandler 
{
	private String _userID;
	private String _password;
	private String _authenticationServiceURL;

	public GridJAASCallbackHandler(String userID, String password) {
		super();
		_userID = userID;
		_password = password;
	}

	public GridJAASCallbackHandler(String userID, String password, String authenticationServiceURL) {
		super();
		_userID = userID;
		_password = password;
		_authenticationServiceURL = authenticationServiceURL;
	}
	
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {

		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof NameCallback) {
				NameCallback nameCallback = (NameCallback) callbacks[i];
				nameCallback.setName(_userID);
			} else if (callbacks[i] instanceof PasswordCallback) {
				PasswordCallback passwordCallback = (PasswordCallback) callbacks[i];
				passwordCallback.setPassword(_password.toCharArray());
			} else if (callbacks[i] instanceof TextInputCallback) {
				TextInputCallback textCallback = (TextInputCallback) callbacks[i];
				textCallback.setText(_authenticationServiceURL);
			} else {
				throw new UnsupportedCallbackException(callbacks[i],
						"Error in initializing the CallBack Handler");
			}
		}
	}
}
