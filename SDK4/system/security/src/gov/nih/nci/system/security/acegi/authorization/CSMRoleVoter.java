package gov.nih.nci.system.security.acegi.authorization;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.vote.AccessDecisionVoter;
import org.apache.log4j.Logger;

import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.system.security.acegi.authentication.CSMUserDetailsService;

import java.util.Iterator;


/**
 * 
 * CSM Implementation of AcessDecisionVoter.
 * 
 * <p>Votes if any {@link ConfigAttribute#getAttribute()} starts with a prefix indicating that it is a role. 
 * RolePrefix isnt used which means that essentially any attribute will be voted on. 
 * 
 */
public class CSMRoleVoter implements AccessDecisionVoter {
    //~ Instance fields ================================================================================================

	static final Logger log = Logger.getLogger(CSMRoleVoter.class.getName());
	/**
     * NOTE: RolePrefix isnt used in CSMRoleVoter. 
     * It is desired to delegate this to SecurityHelper implemented by SDK. 
     * This avoids a security breach in case the User has access to CSM API's.
     * SDK SecurityHelper Impl can provide the RolePrefix in the Configuration file.
     */
    private String rolePrefix = "";
    
    private CSMUserDetailsService userDetailsService;

    
    public boolean supports(ConfigAttribute attribute) {
        if (attribute.getAttribute() != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This implementation supports any type of class, because it does not query the presented secure object.
     *
     * @param clazz the secure object
     *
     * @return always <code>true</code>
     */
    public boolean supports(Class clazz) {
        return true;
    }

    public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) {
        int result = ACCESS_ABSTAIN;
            Iterator iter = config.getConfigAttributes();
	        while (iter.hasNext()) {
				ConfigAttribute attribute = (ConfigAttribute) iter.next();
				if (this.supports(attribute)) {
					result = ACCESS_DENIED;
			        Boolean isProtectionElementsCached = userDetailsService.getCacheProtectionElementsFlag();
					if (isProtectionElementsCached) {
						GrantedAuthority[] grantedAuthorities = authentication.getAuthorities();
						// Attempt to find a matching granted authority
						for (int i = 0; i < grantedAuthorities.length; i++) {
							if ((attribute.getAttribute().equals(grantedAuthorities[i].getAuthority()))) {
								result = ACCESS_GRANTED;
							}
						}
			        } else {
					int index = attribute.getAttribute().lastIndexOf('_');
					String peName = attribute.getAttribute().substring(0, index);
					String privilege = attribute.getAttribute().substring(index + 1);
					try {
						final AuthorizationManager authorizationManager = userDetailsService.getAuthorizationManager();
						boolean accessAllowed = authorizationManager.checkPermission(authentication.getName(),peName, privilege);
						if(accessAllowed){
							result=ACCESS_GRANTED;
						}
						log.info("accessAllowed "+accessAllowed+" for user "+authentication.getName()+ "  protectedElementName  " + peName+ "  privilge " + privilege);
					} catch (CSException e) {
						e.printStackTrace();
					}
				}
			}
			if (result == ACCESS_DENIED) return result;
		}
		return result;
	}

    public void setUserDetailsService(CSMUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public CSMUserDetailsService getUserDetailsService() {
		return userDetailsService;
	}
}
