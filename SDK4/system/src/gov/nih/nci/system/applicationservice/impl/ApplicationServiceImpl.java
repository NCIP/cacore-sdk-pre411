/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package gov.nih.nci.system.applicationservice.impl;

import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.proxy.ListProxy;
import gov.nih.nci.system.dao.DAO;
import gov.nih.nci.system.dao.DAOException;
import gov.nih.nci.system.dao.Request;
import gov.nih.nci.system.dao.Response;
import gov.nih.nci.system.query.cql.CQLQuery;
import gov.nih.nci.system.query.hibernate.HQLCriteria;
import gov.nih.nci.system.query.nestedcriteria.NestedCriteriaPath;
import gov.nih.nci.system.util.ClassCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.impl.CriteriaImpl;

/**
 * Implementation for the methods in the service layer
 * 
 * @author Satish Patel
 * @version 1.0
 */

public class ApplicationServiceImpl implements ApplicationService {

	private ClassCache classCache;	

	private Properties systemProperties;
	
	private static Logger log = Logger.getLogger(ApplicationServiceImpl.class.getName());

	/**
	 * Default constructor. Cache is required and is expected to have a collection of DAO
	 * System properties is also a required parameter used to determine system specific parameters
	 * 
	 */
	public ApplicationServiceImpl(ClassCache classCache, Properties systemProperties)
	{
		this.classCache = classCache;
		this.systemProperties = systemProperties;
	}
	
	/**
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#query(org.hibernate.criterion.DetachedCriteria, java.lang.String)
	 */
	public List<Object> query(DetachedCriteria detachedCriteria, String targetClassName) throws ApplicationException {
		return query(detachedCriteria);
	}

	/**
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#query(org.hibernate.criterion.DetachedCriteria)
	 */
	public List<Object> query(DetachedCriteria detachedCriteria) throws ApplicationException {
		CriteriaImpl crit = (CriteriaImpl)detachedCriteria.getExecutableCriteria(null);
		String targetClassName = crit.getEntityOrClassName();
		return privateQuery(detachedCriteria, targetClassName);
	}

	/**
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#query(gov.nih.nci.system.query.hibernate.HQLCriteria, java.lang.String)
	 */
	public List<Object> query(HQLCriteria hqlCriteria, String targetClassName) throws ApplicationException {
		return query(hqlCriteria);
	}

	/**
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#query(gov.nih.nci.system.query.hibernate.HQLCriteria)
	 */
	public List<Object> query(HQLCriteria hqlCriteria) throws ApplicationException {
		String hql = hqlCriteria.getHqlString();
		int index = hql.indexOf(" from ");
		hql = hql.substring(index+" from ".length()).trim()+" ";
		String targetClassName = hql.substring(0,hql.indexOf(' ')).trim();
		return privateQuery(hqlCriteria, targetClassName);
	}
	
	/**
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#query(gov.nih.nci.system.query.cql.CQLQuery, java.lang.String)
	 */
	public List<Object> query(CQLQuery cqlQuery, String targetClassName) throws ApplicationException {
		return query(cqlQuery);
	}

	/**
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#query(gov.nih.nci.system.query.cql.CQLQuery)
	 */
	public List<Object> query(CQLQuery cqlQuery) throws ApplicationException {
		return privateQuery(cqlQuery, cqlQuery.getTarget().getName());
	}

	
	/** 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#search(java.lang.Class, java.lang.Object)
	 */
	public List<Object> search(Class targetClass, Object obj) throws ApplicationException {
		return search(targetClass.getName(), obj);
	}

	/** 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#search(java.lang.Class, java.util.List)
	 */
	public List<Object> search(Class targetClass, List<?> objList) throws ApplicationException {
		return search(targetClass.getName(), objList);
	}

	/**
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#search(java.lang.String, java.lang.Object)
	 */
	public List<Object> search(String path, Object obj) throws ApplicationException {
		List<Object> list = new ArrayList<Object>();
		list.add(obj);
		return search(path, list);
	}

	/** 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#query(java.lang.Object, java.lang.Integer, java.lang.String)
	 */
	public List<Object> query(Object criteria, Integer firstRow, String targetClassName) throws ApplicationException {
		Request request = new Request(criteria);
		
		request.setIsCount(Boolean.valueOf(false));
		request.setFirstRow(firstRow);
		request.setDomainObjectName(targetClassName);

		Response response = query(request);
		List<Object> results = (List<Object>) response.getResponse();

		return results;
	}

	/**
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#search(java.lang.String, java.util.List)
	 */
	public List<Object> search(String path, List<?> objList) throws ApplicationException {

		try{
			String targetClassName = "";
			StringTokenizer tokens = new StringTokenizer(path, ",");
			targetClassName = tokens.nextToken().trim();
			
			NestedCriteriaPath crit = new NestedCriteriaPath(path,objList);
			List<Object> results = privateQuery((Object)crit, targetClassName);

			return results;
		}
		catch(Exception e)
		{
			String msg = "Error while executing nested search criteria query";
			log.error(msg,e);
			throw new ApplicationException(msg,e); 
		}
	}

	/**
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getQueryRowCount(java.lang.Object, java.lang.String)
	 */
	public Integer getQueryRowCount(Object criteria, String targetClassName) throws ApplicationException {
		Integer count = null;
		Response response = new Response();
		Request request = new Request(criteria);
		request.setIsCount(Boolean.TRUE);
		request.setDomainObjectName(targetClassName);

		response = query(request);
		count = response.getRowCount();

		if (count != null)
			return count;
		else
			return 0;
	}	
	
	/**
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getAssociation(java.lang.Object, java.lang.String)
	 */
	public List<Object> getAssociation(Object source, String associationName) throws ApplicationException {
		
		String hql = "select obj."+associationName+" from "+source.getClass().getName()+" obj where obj = ?";
		
		List<Object> params = new ArrayList<Object>();
		params.add(source);
		HQLCriteria criteria = new HQLCriteria(hql,params);
		return query(criteria);
	}
	
	public Integer getMaxRecordsCount() throws ApplicationException {
		if(systemProperties == null) return 0;
		
		String temp = systemProperties.getProperty("resultCountPerQuery");
		if(temp == null || temp.trim().length() == 0) return 0;
		
		try{
			Integer count = Integer.parseInt(temp);
			return count;
		}catch(Exception e)
		{
			log.error("Could not determine the Max records count from the configuration");
		}
		return 0;
	}
	
	/**
	 * Prepares the {@link #gov.nih.nci.system.dao.Request} object and uses {@link #query(Request)} to retrieve results
	 * from the data source. The results are converted in the list which is only partially loaded upto the maximum number 
	 * of record that the system can support at a time. 
	 * 
	 * @param criteria
	 * @param targetClassName
	 * @return
	 * @throws ApplicationException
	 */
	protected List<Object> privateQuery(Object criteria, String targetClassName) throws ApplicationException 
	{
		
		Request request = new Request(criteria);
		request.setIsCount(Boolean.FALSE);
		request.setFirstRow(0);
		request.setDomainObjectName(targetClassName);

		Response response = query(request);
		List<Object> results = (List<Object>) response.getResponse();

		ListProxy resultList = new ListProxy();

		// Set the value for ListProxy
		if (results != null) {
			resultList.addAll(results);
		}
		
		resultList.setOriginalStart(0);
		resultList.setMaxRecordsPerQuery(response.getRowCount());
		resultList.setOriginalCriteria(criteria);
		resultList.setTargetClassName(targetClassName);
		
		log.debug("response.getRowCount(): " + response.getRowCount());
		
		return resultList;

	}	

	/**
	 * Sends the request to the DAO. The DAO is determined by the object that the query specifies to be queried. 
	 * DAO returns the result which is in the form of a {@link #gov.nih.nci.system.dao.Response} object.
	 * 
	 * @param request
	 * @return
	 * @throws ApplicationException
	 */
	protected Response query(Request request) throws ApplicationException
	{
		request.setClassCache(classCache);
		String domainObjectName = request.getDomainObjectName();
		if (domainObjectName == null || domainObjectName.equals("")){
			throw new ApplicationException("No Domain Object name specified in the request; unable to locate corresponding DAO");
		}
		
		DAO dao = classCache.getDAOForClass(request.getDomainObjectName());
		
		if(dao == null)
			throw new ApplicationException("Could not obtain DAO for: "+request.getDomainObjectName());
		
		try
		{
			return dao.query(request);
			
		} catch(DAOException daoException) {
			log.error("Error while getting and querying DAO",daoException);
			throw daoException;
		} catch (ApplicationException e1) {
			log.fatal("Unable to locate Service Locator :",e1);
			throw new ApplicationException("Unable to locate Service Locator :",e1);
		} catch(Exception exception) {
			log.error("Exception while getting datasource information "+ exception.getMessage());
			throw new ApplicationException("Exception in Base Delegate while getting datasource information: ", exception);
		}
	}
}