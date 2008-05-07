package gov.nih.nci.system.util;

import gov.nih.nci.system.query.cql.CQLQuery;
import gov.nih.nci.system.query.hibernate.HQLCriteria;
import gov.nih.nci.system.query.nestedcriteria.NestedCriteriaPath;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.impl.CriteriaImpl;

public class ApplicationServiceMethodHelper
{
	public static String getDomainObjectName(MethodInvocation invocation) {
		String domainObjectName="*";
		Method method = invocation.getMethod();
		Object[] arguments = invocation.getArguments();
		
		if("getAssociation".equals(method.getName()))
		{
			Class klass = arguments[0].getClass();
			
			while (klass!=Object.class)
			{
			try {
				Field field = klass.getDeclaredField((String)arguments[1]);
				
				Type type = field.getGenericType();
				if (type instanceof ParameterizedType)
				{
					Type t = ((ParameterizedType)type).getActualTypeArguments()[0];
					domainObjectName = ((Class)t).getName();
				}
				else
				{
					domainObjectName = field.getType().getName();
				}
				break;
			} catch (SecurityException e) {
			} catch (NoSuchFieldException e) {
			}
				klass= klass.getSuperclass();
			}
		}
		else if("query".equals(method.getName()) || "getQueryRowCount".equals(method.getName()))
		{
			if(arguments.length == 3)
			{
				domainObjectName = (String)arguments[2];
			}
			else if(arguments[0] instanceof CQLQuery)
			{
				domainObjectName = ((CQLQuery)arguments[0]).getTarget().getName();
			}
			else if(arguments[0] instanceof DetachedCriteria)
			{
				CriteriaImpl crit = (CriteriaImpl)((DetachedCriteria)arguments[0]).getExecutableCriteria(null);
				domainObjectName = crit.getEntityOrClassName();
			}
			else if(arguments[0] instanceof HQLCriteria)
			{
				String hql = ((HQLCriteria)arguments[0]).getHqlString();
				
				String upperHQL = hql.toUpperCase();
				int index = upperHQL.indexOf(" FROM ");
				
				hql = hql.substring(index+" FROM ".length()).trim()+" ";
				domainObjectName = hql.substring(0,hql.indexOf(' ')).trim();
			}
			else if(arguments[0] instanceof NestedCriteriaPath)
			{
				String path = ((NestedCriteriaPath)arguments[0]).getpathString();
				String[] tokens=path.split(",");
				domainObjectName = tokens[0];
			}
		}
		else if("search".equals(method.getName()))
		{
			if(arguments[0] instanceof Class)
			{
				domainObjectName = ((Class)arguments[0]).getName();
			}
			else if(arguments[0] instanceof String)
			{
				String path = (String)arguments[0];
				String[] tokens=path.split(",");
				domainObjectName = tokens[0];
			}
		}
		return domainObjectName;
		
	}
	
}