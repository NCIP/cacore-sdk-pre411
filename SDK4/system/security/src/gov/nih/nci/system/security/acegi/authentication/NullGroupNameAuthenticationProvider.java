/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package gov.nih.nci.system.security.acegi.authentication;

import gov.nih.nci.system.security.acegi.providers.GroupNameAuthenticationToken;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.providers.AuthenticationProvider;

public class NullGroupNameAuthenticationProvider implements AuthenticationProvider
{

	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		return authentication;
	}

	public boolean supports(Class authentication) {
		
		return GroupNameAuthenticationToken.class.isAssignableFrom(authentication);
	}

}