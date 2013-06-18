/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package gov.nih.nci.system.webservice;

import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.proxy.ListProxy;
import gov.nih.nci.system.query.hibernate.HQLCriteria;
import gov.nih.nci.system.query.nestedcriteria.NestedCriteriaPath;
import gov.nih.nci.system.util.ClassCache;
import gov.nih.nci.system.webservice.util.WSUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;


public class WSQueryImpl extends ServletEndpointSupport implements WSQuery{
	private static Logger log = Logger.getLogger(WSQueryImpl.class);

	private static ApplicationService applicationService;	
	private static ClassCache classCache;

	private static int resultCountPerQuery = 1000;
	private String version = "4.0";
	
	public void destroy() {
		applicationService = null;
		classCache = null;
		resultCountPerQuery = 0;
	}

	protected void onInit() throws ServiceException {
		classCache = (ClassCache)getWebApplicationContext().getBean("ClassCache");
		applicationService = (ApplicationService)getWebApplicationContext().getBean("ApplicationServiceImpl");
		
		Properties systemProperties = (Properties) getWebApplicationContext().getBean("SystemProperties");
		
		try {
			String count = systemProperties.getProperty("resultCountPerQuery");
			log.debug("resultCountPerQuery: " + count);
			if (count != null) {
				resultCountPerQuery = Integer.parseInt(count);
			}
		} catch (Exception ex) {
			log.error("Exception initializing resultCountPerQuery: ", ex);
			throw new ServiceException("Exception initializing resultCountPerQuery: ", ex);
		}
	}

	public String getVersion(){
        return version;
    }

    public int getRecordsPerQuery(){
        return resultCountPerQuery;
    }
    public int getMaximumRecordsPerQuery(){
        return resultCountPerQuery;
    }
	
	public int getTotalNumberOfRecords(String targetClassName, Object criteria) throws Exception{
		NestedCriteriaPath criteriaPath=getNestedCriteriaPath(targetClassName, criteria);
		Integer queryRowCount = applicationService.getQueryRowCount(criteriaPath, getTargetClassName(targetClassName));
		return queryRowCount;
	}

	public List queryObject(String targetClassName, Object criteria) throws Exception
	{
		return query(targetClassName,criteria,0);
	}

	public List query(String targetClassName, Object criteria, int startIndex) throws Exception
	{
		List results = new ArrayList();
		results = getNestedCriteriaResultSet(targetClassName, criteria, startIndex);
		List alteredResults = alterResultSet(results);
	
		return alteredResults;
	}

	private List getNestedCriteriaResultSet(String targetClassName, Object searchCriteria, int startIndex) throws Exception{
	
		
		List results = new ArrayList();
		String searchClassName = getSearchClassName(targetClassName);

		try
		{
			if(searchClassName != null && searchCriteria != null){
				NestedCriteriaPath pathCriteria = getNestedCriteriaPath(targetClassName, searchCriteria);				
				results = applicationService.query(pathCriteria, startIndex, getTargetClassName(targetClassName));			
			}
			else{
				throw new Exception("Invalid arguments passed over to the server");
			}

		}
		catch(Exception e)
		{
			log.error("WSQuery caught an exception: ", e);
			throw e;
		}
		return results;
	}

	private NestedCriteriaPath getNestedCriteriaPath(String targetClassName,Object searchCriteria) {
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(searchCriteria);
		NestedCriteriaPath pathCriteria = new NestedCriteriaPath(targetClassName, paramList);
		return pathCriteria;
	}
	
	public List getAssociation(Object source, String associationName, int startIndex) throws Exception
	{

		List results = new ArrayList();
		String targetClassName = source.getClass().getName();
		log.debug("targetClassName: " + targetClassName);
		
		String assocType = classCache.getAssociationType(source.getClass(),associationName);

		String hql = "";
		if(classCache.isCollection(source.getClass().getName(), associationName))
			//hql = "select dest from "+assocType+" as dest,"+source.getClass().getName()+" as src where dest in elements(src."+associationName+") and src=?";
			hql = "select dest from "+source.getClass().getName()+" as src inner join src."+associationName+" dest where src=?";
		else
			hql = "select dest from "+assocType+" as dest,"+source.getClass().getName()+" as src where dest.id=src."+associationName+".id and src=?";

		log.debug("hql: " + hql);
		
		List<Object> params = new ArrayList<Object>();
		params.add(source);
		HQLCriteria criteria = new HQLCriteria(hql,params);
		
		results = getHQLResultSet(targetClassName, criteria, startIndex);

		List alteredResults = alterResultSet(results);
		
		return alteredResults;
	}	

	private List getHQLResultSet(String targetClassName, Object searchCriteria, int startIndex) throws Exception{

		List results = new ArrayList();
		String searchClassName = getSearchClassName(targetClassName);

		try
		{
			if(searchClassName != null && searchCriteria != null){		
				results = applicationService.query(searchCriteria, startIndex, targetClassName);			
			}
			else{
				throw new Exception("Invalid arguments passed over to the server");
			}
		}
		catch(Exception e)
		{
			log.error("WSQuery caught an exception: ", e);
			throw e;
		}
		return results;
	}
	private String getSearchClassName(String targetClassName)throws Exception {
		String searchClassName = "";

		if(targetClassName.indexOf(",")>0){
			StringTokenizer st = new StringTokenizer(targetClassName, ",");
			while(st.hasMoreTokens()){
				String className = st.nextToken();

				String validClassName = classCache.getQualifiedClassName(className);
				log.debug("validClassName: " + validClassName);

				searchClassName += validClassName + ",";
			}
			searchClassName = searchClassName.substring(0,searchClassName.lastIndexOf(","));
		} else{
			searchClassName = classCache.getQualifiedClassName(targetClassName);
		}
		if(searchClassName == null){
			throw new Exception("Invalid class name: " + targetClassName);
		}
		return searchClassName;
	}
	
	
	private List alterResultSet(List results) {
		List objList;
		
		if (results instanceof ListProxy)
		{
			ListProxy listProxy = (ListProxy)results;
			objList = listProxy.getListChunk();
		}
		else
		{
			objList = results;
		}
		
		WSUtils util = new WSUtils();
		objList = (List)util.convertToProxy(null, objList);
		return objList;
	}
	
	private String getTargetClassName(String path){
		
		
		if (path.indexOf(',') <= 0){ 
			return path;
		}
		
		// We have a comma-delimited Nested Search criteria path
		String targetClassName = "";
		StringTokenizer tokens = new StringTokenizer(path, ",");
		targetClassName = tokens.nextToken().trim();
		
		return targetClassName;
	}
	
}
