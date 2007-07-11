package gov.nih.nci.system.dao.orm;

import gov.nih.nci.system.dao.DAO;
import gov.nih.nci.system.dao.DAOException;
import gov.nih.nci.system.dao.Request;
import gov.nih.nci.system.dao.Response;
import gov.nih.nci.system.dao.orm.translator.CQL2HQL;
import gov.nih.nci.system.dao.orm.translator.NestedCriteria2HQL;
import gov.nih.nci.system.query.cql.CQLQuery;
import gov.nih.nci.system.query.hibernate.HQLCriteria;
import gov.nih.nci.system.query.nestedcriteria.NestedCriteria;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Satish Patel, Dan Dumitru
 *
 */
public class ORMDAOImpl extends HibernateDaoSupport implements DAO 
{
	private static Logger log = Logger.getLogger(ORMDAOImpl.class.getName());

	private boolean caseSensitive;
	private int resultCountPerQuery;
	
	private Configuration config;
	
	public ORMDAOImpl(SessionFactory sessionFactory, Configuration config, boolean caseSensitive, int resultCountPerQuery) {
		this.config = config;
		this.setSessionFactory(sessionFactory);
		this.caseSensitive = caseSensitive;
		this.resultCountPerQuery = resultCountPerQuery;
	}

	public Response query(Request request) throws DAOException {

		
		//getHibernateTemplate().getSessionFactory().openSession();
		//Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Session session = getSession(); 

		Object obj = request.getRequest();

		try
		{
			log.debug("****** obj: " + obj.getClass());
			
			if (obj instanceof DetachedCriteria) {				
				return query(request, session, (DetachedCriteria) obj); 	
			} else if (obj instanceof NestedCriteria) {
				return query(request, session, (NestedCriteria) obj); 	
			} else if (obj instanceof HQLCriteria){
				return query(request, session, (HQLCriteria) obj);
			} else if (obj instanceof CQLQuery){
				return query(request, session, (CQLQuery) obj);
			}

		} catch (JDBCException ex){
			log.error("JDBC Exception in ORMDAOImpl ", ex);
			throw new DAOException("JDBC Exception in ORMDAOImpl ", ex);
		} catch(org.hibernate.HibernateException hbmEx)	{
			log.error(hbmEx.getMessage());
			throw new DAOException("Hibernate problem ", hbmEx);
		} catch(Exception e) {
			log.error("Exception ", e);
			throw new DAOException("Exception in ORMDAOImpl ", e);
		} finally {
			try
			{
				session.clear();
				session.close();
			}
			catch (Exception eSession)
			{
				log.error("Could not close the session - "+ eSession.getMessage());
				throw new DAOException("Could not close the session  " + eSession);
			}
		}

		return null;
	}
	
    public List<String> getAllClassNames(){
    	
    	List<String> allClassNames = new ArrayList<String>();
    	Map allClassMetadata = getSessionFactory().getAllClassMetadata();
    	
    	for (Iterator iter = allClassMetadata.keySet().iterator() ; iter.hasNext(); ){		
    		allClassNames.add((String)iter.next());
    	}
    	
    	return allClassNames;
    }
	
	private Response query(Request request, Session session, DetachedCriteria obj) throws Exception
	{
		List rs = null;
		Criteria hCriteria = null;
		Integer rowCount = null;
		
		Integer firstRow = request.getFirstRow();
		Boolean isCount = request.getIsCount();
		
		Response rsp = new Response();
		
		hCriteria = ((org.hibernate.criterion.DetachedCriteria)request.getRequest()).getExecutableCriteria(session);
		log.info("Detached Criteria Query :"+hCriteria.toString());
		if (hCriteria != null)
		{
		    if(isCount != null && isCount.booleanValue())
		    {
		        rowCount = (Integer)hCriteria.setProjection(Projections.rowCount()).uniqueResult();
				log.debug("DetachedCriteria ORMDAOImpl ===== count = " + rowCount);
				rsp.setRowCount(rowCount);
		    }
		    else if((isCount != null && !isCount.booleanValue()) || isCount == null)
		    {
				if(firstRow != null){
		            hCriteria.setFirstResult(firstRow.intValue());

		        }
		    
            	hCriteria.setMaxResults(resultCountPerQuery);
            	
		        rs = hCriteria.list();
		    	rsp.setRowCount(rs.size());
		        rsp.setResponse(rs);
		    }
		}	
			
		return rsp;
	}	
	
	private Response query(Request request, Session session, NestedCriteria obj) throws Exception	
	{
		List rs = null;
		Integer rowCount = null;
		
		Integer firstRow = request.getFirstRow();
		Boolean isCount = request.getIsCount();		
		Query query = null;
		
		Response rsp = new Response();
		
		log.debug("ORMDAOImpl.query: it is a NestedCriteria Object ....");		
		NestedCriteria2HQL converter = new NestedCriteria2HQL((NestedCriteria)obj, config, session, caseSensitive);
		query = converter.translate();
		log.info("HQL Query :"+query.getQueryString());
		if (query != null)
		{
			if(isCount != null && isCount.booleanValue())
		    {			
				log.debug("ORMDAOImpl.  isCount .... .... | converter.getCountQuery() = " + converter.getCountQuery().getQueryString());
				rowCount = (Integer)converter.getCountQuery().uniqueResult();
				log.debug("ORMDAOImpl HQL ===== count = " + rowCount);		
				rsp.setRowCount(rowCount);
			}
			else if((isCount != null && !isCount.booleanValue()) || isCount == null)
		    {	
		    	if(firstRow != null)
		    	{
		    		log.debug("Setting First Row to " + firstRow);
			        query.setFirstResult(firstRow.intValue());				    		
		    	}
		    	
		    	System.out.println("resultCountPerQuery: " + resultCountPerQuery);
		    	query.setMaxResults(resultCountPerQuery);

		    	rs = query.list();
		    	
		    	rsp.setRowCount(rs.size());
		    	rsp.setResponse(rs);
		    }				
		}
		
		return rsp;
	}
	
	//if (obj instanceof HQLCriteria)
	private Response query(Request request, Session session, HQLCriteria obj) throws Exception
	{
		
		List rs = null;
		Integer rowCount = null;
		
		Integer firstRow = request.getFirstRow();
		Boolean isCount = request.getIsCount();	
		
		Response rsp = new Response();
		
		Query hqlQuery = session.createQuery(((HQLCriteria)obj).getHqlString());
		log.info("HQL Criteria Query :"+hqlQuery.getQueryString());
		if(isCount != null && isCount.booleanValue())
	    {
			rowCount = new Integer(hqlQuery.list().size());
			rsp.setRowCount(rowCount);
		}
		else if((isCount != null && !isCount.booleanValue()) || isCount == null)
	    {	
	    	if(firstRow != null)
	    	{
	    		hqlQuery.setFirstResult(firstRow.intValue());				    		
	    	}
	    	
	    	hqlQuery.setMaxResults(resultCountPerQuery);
	    	
	    	rs = hqlQuery.list();
	    	rsp.setRowCount(rs.size());
	    	rsp.setResponse(rs);
	    }	
 		
		return rsp;
	}
	
	//if (obj instanceof CQLQuery)
	private Response query(Request request, Session session, CQLQuery obj) throws Exception	
	{
		List rs = null;
		Integer rowCount = null;
		
		Integer firstRow = request.getFirstRow();
		Boolean isCount = request.getIsCount();
		
		Response rsp = new Response();
		
		HQLCriteria hqlCriteria = CQL2HQL.translate((CQLQuery)obj, false, caseSensitive); 
		String hql = hqlCriteria.getHqlString();
		List params = hqlCriteria.getParameters();
		log.info("CQL Query :"+hql);
		Query hqlQuery = session.createQuery(hql);
		
		for(int i = 0; i<params.size();i++)
			hqlQuery.setParameter(i,params.get(i) );
		
		if(isCount != null && isCount.booleanValue())
	    {
			rowCount = new Integer(hqlQuery.list().size());
			rsp.setRowCount(rowCount);
		}
		else if((isCount != null && !isCount.booleanValue()) || isCount == null)
	    {	
	    	if(firstRow != null)
	    	{
	    		hqlQuery.setFirstResult(firstRow.intValue());				    		
	    	}
	    	
	    	hqlQuery.setMaxResults(resultCountPerQuery);
	    	
	    	rs = hqlQuery.list();
	    	rsp.setRowCount(rs.size());
	    	rsp.setResponse(rs);
	    }	
		
		return rsp;		
	}	
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(ORMDAOImpl.class.getName()+"[\n");
		sb.append("\tSessionFactory: " + getSessionFactory() + "\n");	
		sb.append("]\n");		
		
		return sb.toString();
	}

//	 Sample calls
//		public List getAllBrands() {
//		    return this.getHibernateTemplate().find("from Brand brand");
//		}
//
//		public void save(final Brand brand){
//		    getHibernateTemplate().execute( new HibernateCallback()
//		    {
//		        public Object doInHibernate(Session session)
//		         {
//		            session.save(brand);
//		             return null;
//		         }
//		    });
//		}	
}