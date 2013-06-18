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
	
	public int getTotalNumberOfRecords(String targetClassName, Object criteria) throws Exception{
		return getNestedCriteriaResultSet(targetClassName, criteria, 0).size();
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
				List<Object> paramList = new ArrayList<Object>();
				paramList.add(searchCriteria);
				NestedCriteriaPath pathCriteria = new NestedCriteriaPath(targetClassName,paramList);
				
				results = applicationService.query(pathCriteria, startIndex, targetClassName);			
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
	public List getAssociation(Object source, String associationName, int startIndex) throws Exception
	{

		List results = new ArrayList();
		String targetClassName = source.getClass().getName();
		log.debug("targetClassName: " + targetClassName);
		
		String hql = "select obj."+associationName+" from "+targetClassName+" obj where obj = ?";
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
	
}
