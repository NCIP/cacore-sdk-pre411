package gov.nih.nci.system.dao.orm;

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
import gov.nih.nci.system.security.helper.SecurityInitializationHelper;

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
import org.hibernate.event.PreInsertEventListener;
import org.hibernate.event.PreUpdateEventListener;
import org.hibernate.validator.event.ValidateEventListener;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Satish Patel, Dan Dumitru
 *
 */
public class ORMDAOImpl extends HibernateDaoSupport implements DAO 
{
	protected static Logger log = Logger.getLogger(DAO.class.getName());

	private Configuration config;
	private SecurityInitializationHelper securityHelper;

	private boolean caseSensitive;
	private int resultCountPerQuery;

	private boolean registerHibernateValidatorEventListeners;
	
	protected HibernateTemplate createHibernateTemplate(SessionFactory sessionFactory)
	{
		if(securityHelper!=null && securityHelper.isSecurityEnabled() && securityHelper.getAuthorizationManager()!=null && securityHelper.isInstanceLevelSecurityEnabled())
			return new FilterableHibernateTemplate(sessionFactory, new CSMFilterParameterSetter(securityHelper));
		else
			return super.createHibernateTemplate(sessionFactory);
	}

	public Response query(Request request) throws DAOException 
	{
		Session session = getSession();
		Object obj = request.getRequest();

		try
		{
			log.debug("****** obj: " + obj.getClass());
			if (obj instanceof DetachedCriteria) 				
				return query(request, session, (DetachedCriteria) obj); 	
			else if (obj instanceof NestedCriteriaPath)
				return query(request, session, (NestedCriteriaPath) obj); 	
			else if (obj instanceof HQLCriteria)
				return query(request, session, (HQLCriteria) obj);
			else if (obj instanceof CQLQuery)
				return query(request, session, (CQLQuery) obj);
			else
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
	
	protected Response query(Request request, Session session, DetachedCriteria obj) throws Exception
	{
		Response rsp = new Response();
		Criteria hCriteria = ((org.hibernate.criterion.DetachedCriteria)request.getRequest()).getExecutableCriteria(session);
		log.info("Detached Criteria Query :"+hCriteria.toString());

		if (hCriteria != null)
		{
		    if(request.getIsCount() != null && request.getIsCount())
		    {
		        Integer rowCount = (Integer)hCriteria.setProjection(Projections.rowCount()).uniqueResult();
				log.debug("DetachedCriteria ORMDAOImpl ===== count = " + rowCount);
				rsp.setRowCount(rowCount);
		    }
		    else 
		    {
				if(request.getFirstRow() != null)
		            hCriteria.setFirstResult(request.getFirstRow());
            	hCriteria.setMaxResults(resultCountPerQuery);
		        List rs = hCriteria.list();
		    	rsp.setRowCount(rs.size());
		        rsp.setResponse(rs);
		    }
		}	
			
		return rsp;
	}	
	
	//	if (obj instanceof NestedCriteriaPath)
	protected Response query(Request request, Session session, NestedCriteriaPath obj) throws Exception	
	{
		NestedCriteria nc = Path2NestedCriteria.createNestedCriteria(obj.getpathString(), obj.getParameters(), request.getClassCache());
		NestedCriteria2HQL converter = new NestedCriteria2HQL(nc, config, session, caseSensitive);
		Query query = converter.translate();

		if(request.getIsCount() != null && request.getIsCount())
			return executeCountQuery(converter.getCountQuery());
		else
			return executeResultQuery(request, query);
	}
	
	//if (obj instanceof CQLQuery)
	protected Response query(Request request, Session session, CQLQuery obj) throws Exception	
	{
		CQL2HQL converter = new CQL2HQL(request.getClassCache());
		HQLCriteria hqlCriteria = converter.translate((CQLQuery)obj, false, caseSensitive);
		return query(request, session, hqlCriteria);		
	}

	//if (obj instanceof HQLCriteria)
	protected Response query(Request request, Session session, HQLCriteria hqlCriteria) throws Exception
	{
		Query hqlQuery = null;
		
		if(request.getIsCount() != null && request.getIsCount())
			hqlQuery = session.createQuery(getCountQuery(hqlCriteria.getHqlString()));
		else
			hqlQuery = session.createQuery(hqlCriteria.getHqlString());
		
		int i=0;
		for(Object param: hqlCriteria.getParameters())
			hqlQuery.setParameter(i++,param );
		

		if(request.getIsCount() != null && request.getIsCount())
			return executeCountQuery(hqlQuery);
		else 
			return executeResultQuery(request, hqlQuery);
	}
	
	
	protected Response executeCountQuery(Query hqlQuery)
	{
		log.info("HQL Query :"+hqlQuery.getQueryString());
		Response rsp = new Response();
		hqlQuery.setMaxResults(1);
		Integer rowCount = Integer.parseInt(hqlQuery.uniqueResult()+"");
		log.debug("HQL Query : count = " + rowCount);		
		rsp.setRowCount(rowCount);
		return rsp;
	}

	protected Response executeResultQuery(Request request, Query hqlQuery)
	{
		log.info("HQL Query :"+hqlQuery.getQueryString());
		
		Response rsp = new Response();
    	if(request.getFirstRow() != null)
    		hqlQuery.setFirstResult(request.getFirstRow().intValue());				    		
    	
    	hqlQuery.setMaxResults(resultCountPerQuery);
    	
    	List rs = hqlQuery.list();
    	rsp.setRowCount(rs.size());
    	rsp.setResponse(rs);
		return rsp;
	}
	
	
	private String getCountQuery(String hql)
	{
		return NestedCriteria2HQL.getCountQuery(hql);
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public Configuration getConfig() {
		return config;
	}

	public void setConfig(Configuration config) {
		this.config = config;
	}

	public int getResultCountPerQuery() {
		return resultCountPerQuery;
	}

	public void setResultCountPerQuery(int resultCountPerQuery) {
		this.resultCountPerQuery = resultCountPerQuery;
	}

	public SecurityInitializationHelper getSecurityHelper() {
		return securityHelper;
	}

	public void setSecurityHelper(SecurityInitializationHelper securityHelper) {
		this.securityHelper = securityHelper;
	}

	public boolean isRegisterHibernateValidatorEventListeners() {
		return registerHibernateValidatorEventListeners;
	}

	public void setRegisterHibernateValidatorEventListeners(
			boolean registerHibernateValidatorEventListeners) {
		this.registerHibernateValidatorEventListeners = registerHibernateValidatorEventListeners;
		
		if (this.registerHibernateValidatorEventListeners){
			this.config.getEventListeners()
				.setPreInsertEventListeners( new PreInsertEventListener[]{new ValidateEventListener()} );
			this.config.getEventListeners()
				.setPreUpdateEventListeners( new PreUpdateEventListener[]{new ValidateEventListener()} );
		}
	}
}