package gov.nih.nci.system.util;

import gov.nih.nci.system.query.SDKQuery;
import gov.nih.nci.system.query.example.DeleteExampleQuery;
import gov.nih.nci.system.query.example.InsertExampleQuery;
import gov.nih.nci.system.query.example.UpdateExampleQuery;
import gov.nih.nci.system.security.SecurityConstants;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;

public class WritableApiApplicationServiceMethodHelper extends ApplicationServiceMethodHelper{
	
	@SuppressWarnings("unchecked")
	public Map<String,String> getDomainObjectName(MethodInvocation invocation) {
		String privilege = null;
		
		String domainObjectName = "*";
		Method method = invocation.getMethod();
		Object[] arguments = invocation.getArguments();
		Map domainObjectMap=new HashMap<String, Collection<String>>();
		Map<String, String> securityMap = new HashMap<String, String>();

		if ("executeQuery".equals(method.getName()) && (arguments[0] instanceof SDKQuery)) {
			SDKQuery sdkQuery = (SDKQuery) arguments[0];

			if (sdkQuery instanceof InsertExampleQuery) {
				privilege=SecurityConstants.CREATE;
				InsertExampleQuery insertExampleQuery = (InsertExampleQuery) sdkQuery;
				domainObjectMap.put(insertExampleQuery.getExample().getClass().getName(), privilege);
				return domainObjectMap;
			} else if (sdkQuery instanceof DeleteExampleQuery) {
				privilege=SecurityConstants.DELETE;
				DeleteExampleQuery deleteExampleQuery = (DeleteExampleQuery) sdkQuery;
				domainObjectMap.put(deleteExampleQuery.getExample().getClass().getName(), privilege);
				return domainObjectMap;
			} else if (sdkQuery instanceof UpdateExampleQuery) {
				privilege=SecurityConstants.UPDATE;
				UpdateExampleQuery updateExampleQuery = (UpdateExampleQuery) sdkQuery;
				domainObjectMap.put(updateExampleQuery.getExample().getClass().getName(), privilege);
				return domainObjectMap;
			}
			securityMap.put(domainObjectName, privilege);
		} else {
			securityMap = super.getDomainObjectName(invocation);
		}
		return securityMap;	
	}
}