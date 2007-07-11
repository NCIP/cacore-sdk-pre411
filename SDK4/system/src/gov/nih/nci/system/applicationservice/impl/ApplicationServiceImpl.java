package gov.nih.nci.system.applicationservice.impl;

import gov.nih.nci.dao.orm.translator.Path2NestedCriteria;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.dao.DAO;
import gov.nih.nci.system.dao.DAOException;
import gov.nih.nci.system.dao.QueryException;
import gov.nih.nci.system.dao.Request;
import gov.nih.nci.system.dao.Response;
import gov.nih.nci.system.query.cql.CQLQuery;
import gov.nih.nci.system.query.hibernate.HQLCriteria;
import gov.nih.nci.system.query.nestedcriteria.NestedCriteria;
import gov.nih.nci.system.util.ClassCache;
import gov.nih.nci.system.util.ListProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;

/**
 * Implementation for the methods in the service layer
 * 
 * @author Satish Patel
 * @version 1.0
 */

public class ApplicationServiceImpl implements ApplicationService {

	private ClassCache classCache;	

	private static Logger log = Logger.getLogger(ApplicationServiceImpl.class.getName());

	/**
	 * Default constructor
	 * 
	 * @param model
	 * @param validators
	 * @param transformers
	 */
	public ApplicationServiceImpl(ClassCache classCache)
	{
		this.classCache = classCache;
	}


	/**
	 * @param criteria
	 * @return total count for the query
	 * @throws ApplicationException
	 * 
	 */
	public int getQueryRowCount(Object criteria, String targetClassName) throws ApplicationException {
		Integer count = null;
		Response response = new Response();
		Request request = new Request(criteria);
		request.setIsCount(Boolean.TRUE);
		request.setDomainObjectName(targetClassName);

		response = query(request);
		count = response.getRowCount();

		if (count != null)
			return count.intValue();
		else
			return 0;
	}
	
	public List<Object> query(DetachedCriteria detachedCriteria, String targetClassName) throws ApplicationException {
		return privateQuery((Object) detachedCriteria, targetClassName);
	}

	private List query(NestedCriteria nestedCriteria, String targetClassName) throws ApplicationException {
		return privateQuery((Object) nestedCriteria, targetClassName);
	}

	public List<Object> query(HQLCriteria hqlCriteria, String targetClassName) throws ApplicationException {
		return privateQuery((Object) hqlCriteria, targetClassName);
	}

	public List<Object> query(CQLQuery cqlQuery, String targetClassName) throws ApplicationException {
		return privateQuery((Object) cqlQuery, targetClassName);
	}
	/**
	 * Gets the result list for the specified Hibernate Criteria from the
	 * HTTPClient.
	 * 
	 * @param criteria
	 *            Specified Hibernate criteria
	 * @return gets the result list
	 * @throws ApplicationException
	 */

	private List<Object> privateQuery(Object criteria, String targetClassName) throws ApplicationException {

		List<Object> results = null;
		List<Object> resultList = new ListProxy();
		
		Response response = new Response();
		Request request = new Request(criteria);
		request.setIsCount(Boolean.FALSE);
		request.setFirstRow(0);

		request.setDomainObjectName(targetClassName);

		response = query(request);
		results = (List) response.getResponse();

		resultList.clear();

		// Set the value for ListProxy
		if (results != null) {
			resultList.addAll(results);
		}
		
		log.debug("resultList.size(): " + resultList.size());
		log.debug("response.getRowCount(): " + response.getRowCount());
		
		ListProxy myProxy = (ListProxy) resultList;
		myProxy.setOriginalStart(0);
		
		// TODO :: determine if the following assignment is accurate
		myProxy.setMaxRecordsPerQuery(response.getRowCount());
		myProxy.setOriginalCriteria(criteria);
		myProxy.setTargetClassName(targetClassName);

		return resultList;

	}

	/**
	 * @param criteria
	 * @param firstRow
	 * @param resultsPerQuery
	 * @param targetClassName
	 * @return List
	 * @throws ApplicationException
	 */
	public List<Object> query(Object criteria, int firstRow, int resultsPerQuery, String targetClassName) throws ApplicationException {
		List<Object> results = null;
		
		Response response = new Response();
		Request request = new Request(criteria);
		
		request.setIsCount(Boolean.valueOf(false));
		request.setFirstRow(new Integer(firstRow));
		request.setDomainObjectName(targetClassName);

		response = query(request);
		results = (List) response.getResponse();

		return results;
	}

	public List<Object> search(Class targetClass, Object obj) throws ApplicationException {
		return search(targetClass.getName(), obj);
	}

	public List<Object> search(Class targetClass, List objList) throws ApplicationException {
		return search(targetClass.getName(), objList);
	}

	public List<Object> search(String path, Object obj) throws ApplicationException {
		List<Object> list = new ArrayList<Object>();
		list.add(obj);
		return search(path, list);
	}

	public List search(String path, List objList) throws ApplicationException {

		try{
			List pathList = preparePathList(path);

			NestedCriteria crit = Path2NestedCriteria.createNestedCriteria(pathList, objList);
			List results = query(crit, crit.getTargetObjectName());

			return results;
		}
		catch(Exception e)
		{
			String msg = "Error while executing nested search criteria query";
			log.error(msg,e);
			throw new ApplicationException(msg,e); 
		}
	}

	private List preparePathList(String path)
	{
		List pathList = new ArrayList();

		StringTokenizer tokens = new StringTokenizer(path, ",");
		while (tokens.hasMoreTokens()) {
			pathList.add(tokens.nextToken().trim());
		}
		return pathList;
	}

	public static String getFullQName(String name) throws ApplicationException {
		try {
			Class.forName(name);
		} catch (ClassNotFoundException e) {
			log.error("ERROR: Class " + name + " does not exist.  Please check the package and class name.",e);
			throw new QueryException("ERROR: Class " + name + " does not exist.  Please check the package and class name.",e);
		}

		return name;
	}


	private Response query(Request request) throws ApplicationException
	{

		String domainObjectName = request.getDomainObjectName();
		if (domainObjectName == null || domainObjectName.equals("")){
			throw new ApplicationException("No Domain Object name specified in the request; unable to locate corresponding DAO");
		}
		
		// TODO :: make classCache instance based (i.e., non-static methods)
		DAO dao = classCache.getDAOForClass(request.getDomainObjectName());
		
		System.out.println("**** DAO: " + dao);
		
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

	public ClassCache getClassCache() {
		return classCache;
	}


	public void setClassCache(ClassCache classCache) {
		this.classCache = classCache;
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("\nApplicationServiceImpl[");
		sb.append("\n\tclassCache: " + classCache);
		sb.append("\n]");		

		return sb.toString();
	}

}

