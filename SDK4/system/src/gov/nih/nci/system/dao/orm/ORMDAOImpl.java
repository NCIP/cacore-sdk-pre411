package gov.nih.nci.system.dao.orm;

import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.acegi.authentication.CSMAuthenticationProvider;
import gov.nih.nci.security.authorization.attributeLevel.AttributeSecuritySessionInterceptor;
import gov.nih.nci.security.authorization.attributeLevel.UserClassAttributeMapCache;
import gov.nih.nci.security.authorization.instancelevel.InstanceLevelSecurityHelper;
import gov.nih.nci.system.dao.DAO;
import gov.nih.nci.system.dao.DAOException;
import gov.nih.nci.system.dao.Request;
import gov.nih.nci.system.dao.Response;
import gov.nih.nci.system.dao.orm.translator.CQL2HQL;
import gov.nih.nci.system.dao.orm.translator.NestedCriteria2HQL;
import gov.nih.nci.system.dao.orm.translator.Path2NestedCriteria;
import gov.nih.nci.system.query.cql.CQLQuery;
import gov.nih.nci.system.query.hibernate.HQLCriteria;
import gov.nih.nci.system.query.nestedcriteria.NestedCriteria;
import gov.nih.nci.system.query.nestedcriteria.NestedCriteriaPath;
import gov.nih.nci.system.util.ClassCache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.dao.UserCache;
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
	
	private boolean instanceLevelSecurity;
	private boolean attributeLevelSecurity;
	
	private CSMAuthenticationProvider authenticationProvider;
	
	public ORMDAOImpl(SessionFactory sessionFactory, Configuration config, boolean caseSensitive, int resultCountPerQuery, boolean instanceLevelSecurity, boolean attributeLevelSecurity, CSMAuthenticationProvider authenticationProvider) {
		this.config = config;
		this.setSessionFactory(sessionFactory);
		this.caseSensitive = caseSensitive;
		this.resultCountPerQuery = resultCountPerQuery;
		this.instanceLevelSecurity=instanceLevelSecurity;
		this.attributeLevelSecurity=attributeLevelSecurity;
		this.authenticationProvider=authenticationProvider;
	}

	private Session getSecuredSession()
	{
		Session session = null;
		if (attributeLevelSecurity)
		{
			session = this.getSessionFactory().openSession(new AttributeSecuritySessionInterceptor());
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			AuthorizationManager authorizationManager = authenticationProvider.getUserDetailsService().authorizationManagerInstance();
			UserClassAttributeMapCache.setAttributeMap(userName,this.getSessionFactory(), authorizationManager);
		}
		else
			session = getSession(); 
		
		if (instanceLevelSecurity)
		{
			AuthorizationManager authorizationManager = authenticationProvider.getUserDetailsService().authorizationManagerInstance();
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			InstanceLevelSecurityHelper.initializeFilters(userName, session, authorizationManager);
		}
		return session;
	}
	
	
	public Response query(Request request) throws DAOException 
	{
		Session session = (instanceLevelSecurity || attributeLevelSecurity)?getSecuredSession():getSession();

		Object obj = request.getRequest();

		try
		{
			log.debug("****** obj: " + obj.getClass());
			
			if (obj instanceof DetachedCriteria) {				
				return query(request, session, (DetachedCriteria) obj); 	
			} else if (obj instanceof NestedCriteriaPath) {
				return query(request, session, (NestedCriteriaPath) obj); 	
			} else if (obj instanceof HQLCriteria){
				return query(request, session, (HQLCriteria) obj);
			} else if (obj instanceof CQLQuery){
				return query(request, session, (CQLQuery) obj);
			} else
				throw new DAOException("Can not determine type of the query");

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
		hCriteria.setMaxResults(10000);
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
	
	private Response query(Request request, Session session, NestedCriteriaPath obj) throws Exception	
	{
		List rs = null;
		Integer rowCount = null;
		
		Integer firstRow = request.getFirstRow();
		Boolean isCount = request.getIsCount();		
		Query query = null;
		
		Response rsp = new Response();
		
		log.debug("ORMDAOImpl.query: it is a NestedCriteriaPath Object ....");	
		NestedCriteria nc = Path2NestedCriteria.createNestedCriteria(obj.getpathString(), obj.getParameters(), request.getClassCache());
		NestedCriteria2HQL converter = new NestedCriteria2HQL(nc, config, session, caseSensitive);
		query = converter.translate();
		log.info("HQL Query :"+query.getQueryString());
		if (query != null)
		{
			if(isCount != null && isCount.booleanValue())
		    {
				converter.getCountQuery().setMaxResults(1);
				log.debug("ORMDAOImpl.  isCount .... .... | converter.getCountQuery() = " + converter.getCountQuery().getQueryString());
				rowCount = Integer.parseInt(converter.getCountQuery().uniqueResult()+"");
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
		if(obj.getParameters()!=null && obj.getParameters().size()>0)
		{
			int count=0;
			for(Object param:obj.getParameters())
				hqlQuery.setParameter(count++, param);
		}
		hqlQuery.setMaxResults(100000);
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
		
		CQL2HQL converter = new CQL2HQL(request.getClassCache());
		HQLCriteria hqlCriteria = converter.translate((CQLQuery)obj, false, caseSensitive); 
		String hql = hqlCriteria.getHqlString();
		List params = hqlCriteria.getParameters();
		log.info("CQL Query :"+hql);
		Query hqlQuery = session.createQuery(hql);
		
		for(int i = 0; i<params.size();i++)
			hqlQuery.setParameter(i,params.get(i) );

		hqlQuery.setMaxResults(100000);
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
}