package gov.nih.nci.system.dao.orm;

import java.util.List;

import gov.nih.nci.system.dao.DAOException;
import gov.nih.nci.system.dao.Request;
import gov.nih.nci.system.dao.Response;
import gov.nih.nci.system.dao.WritableDAO;
import gov.nih.nci.system.query.SDKQuery;
import gov.nih.nci.system.query.example.DeleteExampleQuery;
import gov.nih.nci.system.query.example.InsertExampleQuery;
import gov.nih.nci.system.query.example.SearchExampleQuery;
import gov.nih.nci.system.query.example.UpdateExampleQuery;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;

public class WritableORMDAOImpl extends ORMDAOImpl implements WritableDAO
{

	@Override
	public Response query(Request request) throws DAOException 
	{
		if(! (request.getRequest() instanceof SDKQuery))
			return super.query(request);

		Object retObj = null;
		try 
		{
			SDKQuery q = (SDKQuery) request.getRequest();
			if (q instanceof SearchExampleQuery)
				retObj = search(((SearchExampleQuery) q).getExample());
			else if (q instanceof InsertExampleQuery)
				retObj = insert(((InsertExampleQuery) q).getExample());
			else if (q instanceof DeleteExampleQuery)
				delete(((DeleteExampleQuery) q).getExample());
			else if (q instanceof UpdateExampleQuery) {
				update(((UpdateExampleQuery) q).getExample());
				retObj = ((UpdateExampleQuery) q).getExample();
			}
		} catch (Exception e) {
			throw new DAOException(e);
		} finally {
		}
		Response resp = new Response();
		resp.setResponse(retObj);
		return resp;
	}

	protected Object insert(Object o) throws Exception
	{
		log.info("In the writable DAO. executing the Insert query");
		return getHibernateTemplate().save(o);
	}

	protected void update(Object o) throws Exception
	{
		log.info("In the writable DAO. executing the Update query");
		getHibernateTemplate().update(o);
	}

	protected void delete(Object o) throws Exception
	{
		log.info("In the writable DAO. executing the Delete query");
		getHibernateTemplate().delete(o);
	}

	protected List search(Object o) throws Exception
	{
		log.info("In the writable DAO. executing the Search query");
		return getHibernateTemplate().findByExample(o);
	}
}