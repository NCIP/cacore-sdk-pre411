package gov.nih.nci.system.security.acegi;

import gov.nih.nci.system.security.acegi.authorization.CSMMethodDefinitionSource;
import gov.nih.nci.system.security.acegi.external.SecurityHelper;

import org.acegisecurity.intercept.AbstractSecurityInterceptor;
import org.acegisecurity.intercept.InterceptorStatusToken;
import org.acegisecurity.intercept.ObjectDefinitionSource;
import org.acegisecurity.intercept.method.MethodDefinitionSource;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


/**
 * Custom MethodSecuyrityInterceptor for CSM.
 * 
 * 
 * 
 * 
 * Provides security interception of AOP Alliance based method invocations.<p>The
 * <code>ObjectDefinitionSource</code> required by this security interceptor is of type {@link
 * MethodDefinitionSource}. This is shared with the AspectJ based security interceptor
 * (<code>AspectJSecurityInterceptor</code>), since both work with Java <code>Method</code>s.</p>
 *  <P>Refer to {@link AbstractSecurityInterceptor} for details on the workflow.</p>
 */
public class CSMMethodSecurityInterceptor extends AbstractSecurityInterceptor implements MethodInterceptor {

	private SecurityHelper securityHelper;

    private MethodDefinitionSource objectDefinitionSource;


    public MethodDefinitionSource getObjectDefinitionSource() {
        return this.objectDefinitionSource;
    }

    public Class getSecureObjectClass() {
        return MethodInvocation.class;
    }

    /**
     * This method should be used to enforce security on a <code>MethodInvocation</code>.
     *
     * @param mi The method being invoked which requires a security decision
     *
     * @return The returned value from the method invocation
     *
     * @throws Throwable if any error occurs
     */
    public Object invoke(MethodInvocation mi) throws Throwable {
        Object result = null;
        
        
        
        // Update the MethodDefinitionSource according to SDK's SecurityHelper Map
        CSMMethodDefinitionSource csmMethodDefinitionSource = (CSMMethodDefinitionSource) this.objectDefinitionSource;
        // Get PreMethodInvocationSecurityMap and set it in MethodDefinitionSource.
        //securityHelper.setMethodInvocation(mi);
        csmMethodDefinitionSource.setSecurityMap(securityHelper.getPreMethodInvocationSecurityMap(mi));
        // Rebuild MethodDefinitionSource
        csmMethodDefinitionSource.buildMethodMap(mi.getMethod());
        
       
        InterceptorStatusToken token = super.beforeInvocation(mi);
        
        try {
            result = mi.proceed();
        } finally {
        	/* 
        	 * Summary: Need to provide SecurityMap from SDK's SecurityHelper to the AfterInvocationProvider so it 
        	 * 			can filter out restricted objects from 'result'.
        	 * 
        	 * Logic: 
        	 * 	- MethodInvocation is required by SecurityHelper to determine the SecurityMap
        	 *  - MethodInvocation is NOT available in AfterInvocationProvider. 
        	 *  - Hence the following statement provide the SecurityMap to AfterInvocationProvider.
        	 *  NOTE: CSM implementation of variuos acegi classes are required and changes 
        	 *  	to acegi-security.xml cannot be made without incidents.
        	 * 
        	 */
        	CSMAfterInvocationProviderManager csmAfterInvocationProviderManager = (CSMAfterInvocationProviderManager) this.getAfterInvocationManager();
        	CSMAfterInvocationProvider cip = (CSMAfterInvocationProvider) csmAfterInvocationProviderManager.getProviders().get(0);
        	cip.setSecurityHelper(securityHelper);
        	cip.setMethodInvocation(mi);
        	        	
            result = super.afterInvocation(token, result);
        }

        return result;
    }

    public ObjectDefinitionSource obtainObjectDefinitionSource() {
        return this.objectDefinitionSource;
    }

    public void setObjectDefinitionSource(MethodDefinitionSource newSource) {
        this.objectDefinitionSource = newSource;
    }

	public SecurityHelper getSecurityHelper() {
		return securityHelper;
	}

	public void setSecurityHelper(SecurityHelper securityHelper) {
		this.securityHelper = securityHelper;
	}
    
    
  
}
