package gov.nih.nci.system.util;

import gov.nih.nci.system.query.SDKQuery;
import gov.nih.nci.system.query.example.DeleteExampleQuery;
import gov.nih.nci.system.query.example.InsertExampleQuery;
import gov.nih.nci.system.query.example.UpdateExampleQuery;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;

public class WritableApiApplicationServiceMethodHelper extends ApplicationServiceMethodHelper{
	
	@SuppressWarnings("unchecked")
	public Map<String, Collection<String>> getDomainObjectName(MethodInvocation invocation) {
		Collection<String> privileges = new ArrayList<String>();
		
		String domainObjectName = "*";
		Method method = invocation.getMethod();
		Object[] arguments = invocation.getArguments();
		Map domainObjectMap=new HashMap<String, Collection<String>>();
		Map<String, Collection<String>> securityMap=new HashMap<String, Collection<String>>();

		if ("executeQuery".equals(method.getName()) && (arguments[0] instanceof SDKQuery)) {
			SDKQuery sdkQuery = (SDKQuery) arguments[0];

			if (sdkQuery instanceof InsertExampleQuery) {
				privileges.add(SecurityConstants.CREATE);
				InsertExampleQuery insertExampleQuery = (InsertExampleQuery) sdkQuery;
				domainObjectMap.put(insertExampleQuery.getExample().getClass().getName(), privileges);
				return domainObjectMap;
			} else if (sdkQuery instanceof DeleteExampleQuery) {
				privileges.add(SecurityConstants.DELETE);
				DeleteExampleQuery deleteExampleQuery = (DeleteExampleQuery) sdkQuery;
				domainObjectMap.put(deleteExampleQuery.getExample().getClass().getName(), privileges);
				return domainObjectMap;
			} else if (sdkQuery instanceof UpdateExampleQuery) {
				privileges.add(SecurityConstants.UPDATE);
				UpdateExampleQuery updateExampleQuery = (UpdateExampleQuery) sdkQuery;
				domainObjectMap.put(updateExampleQuery.getExample().getClass().getName(), privileges);
				return domainObjectMap;
			}
			securityMap.put(domainObjectName, privileges);
		} else {
			securityMap = super.getDomainObjectName(invocation);
		}
		return securityMap;	
	}
}