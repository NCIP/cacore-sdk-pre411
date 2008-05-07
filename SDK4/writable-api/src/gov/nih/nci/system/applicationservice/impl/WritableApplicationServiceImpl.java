package gov.nih.nci.system.applicationservice.impl;

import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.WritableApplicationService;
import gov.nih.nci.system.dao.DAO;
import gov.nih.nci.system.dao.DAOException;
import gov.nih.nci.system.dao.Request;
import gov.nih.nci.system.dao.Response;
import gov.nih.nci.system.dao.WritableDAO;
import gov.nih.nci.system.query.SDKQuery;
import gov.nih.nci.system.query.example.ExampleQuery;
import gov.nih.nci.system.util.ClassCache;

import org.apache.log4j.Logger;


public class WritableApplicationServiceImpl extends ApplicationServiceImpl implements WritableApplicationService
{
	private static Logger log = Logger.getLogger(WritableApplicationServiceImpl.class.getName());
	public WritableApplicationServiceImpl(ClassCache classCache) {
		super(classCache);
	}

	public Object executeQuery(SDKQuery query) throws ApplicationException {
		String classname = null;
		if(query instanceof ExampleQuery)
			classname = ((ExampleQuery)query).getExample().getClass().getName();
		WritableDAO dao = getWritableDAO(classname);
		Request request = new Request();
		request.setIsCount(Boolean.FALSE);
		request.setClassCache(getClassCache());
		request.setRequest(query);
		try {
			Response resp =  dao.query(request);
			return resp.getResponse();
		}  catch(DAOException daoException) {
			log.error("Error while querying DAO ",daoException);
			throw daoException;
		} catch(Exception exception) {
			log.error("Error while querying DAO ", exception);
			throw new ApplicationException("Error while querying DAO: ", exception);
		}
	}
	
	protected WritableDAO getWritableDAO(String classname) throws ApplicationException
	{
		DAO dao = getDAO(classname);
		if(dao instanceof WritableDAO)
			return (WritableDAO)dao;

		throw new ApplicationException("Can not execute query on non-writable DAO");
	}

}