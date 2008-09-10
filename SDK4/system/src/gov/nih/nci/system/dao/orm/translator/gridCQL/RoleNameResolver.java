package gov.nih.nci.system.dao.orm.translator.gridCQL;

import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.system.util.ClassCache;


public class RoleNameResolver extends gov.nih.nci.cagrid.sdkquery4.processor.RoleNameResolver
{

	ClassCache cache;
	
	public RoleNameResolver(ClassCache cache)
	{
		super(null);
		this.cache = cache;
	}
	
	public String getRoleName(String parentName, Association assoc) throws QueryProcessingException 
	{
		try
		{
			return cache.getAssociationType(cache.getClassFromCache(parentName), assoc.getName());
		} 
		catch (Exception e)
		{
			throw new QueryProcessingException(e);
		}
	}
}