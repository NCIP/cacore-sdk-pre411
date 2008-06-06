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
		
		Method method = invocation.getMethod();
		Object[] arguments = invocation.getArguments();
		Map<String, String> securityMap = new HashMap<String, String>();

		if ("executeQuery".equals(method.getName()) && (arguments[0] instanceof SDKQuery)) {
			SDKQuery sdkQuery = (SDKQuery) arguments[0];

			if (sdkQuery instanceof InsertExampleQuery) {
				privilege=SecurityConstants.CREATE;
				InsertExampleQuery insertExampleQuery = (InsertExampleQuery) sdkQuery;
				securityMap.put(insertExampleQuery.getExample().getClass().getName(), privilege);
			} else if (sdkQuery instanceof DeleteExampleQuery) {
				privilege=SecurityConstants.DELETE;
				DeleteExampleQuery deleteExampleQuery = (DeleteExampleQuery) sdkQuery;
				securityMap.put(deleteExampleQuery.getExample().getClass().getName(), privilege);
			} else if (sdkQuery instanceof UpdateExampleQuery) {
				privilege=SecurityConstants.UPDATE;
				UpdateExampleQuery updateExampleQuery = (UpdateExampleQuery) sdkQuery;
				securityMap.put(updateExampleQuery.getExample().getClass().getName(), privilege);
			}
		} else {
			securityMap = super.getDomainObjectName(invocation);
		}
		return securityMap;	
	}
}