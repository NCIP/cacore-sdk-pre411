package gov.nih.nci.system.applicationservice;

import gov.nih.nci.system.query.cql.CQLQuery;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

/**
 * Service layer interface whose methods will be exposed to all the different tiers (webservvice, remote and web)
 * 
 * @author Satish Patel, Dan Dumitru
 */
public interface ApplicationService
{
	//Methods to query the system
	@Deprecated
	public List<Object> query(CQLQuery cqlQuery, String targetClassName) throws ApplicationException;
	public List<Object> query(CQLQuery cqlQuery) throws ApplicationException;

	@Deprecated
	public List<Object> query(DetachedCriteria detachedCriteria, String targetClassName) throws ApplicationException;
	public List<Object> query(DetachedCriteria detachedCriteria) throws ApplicationException;
	
	@Deprecated
	public List<Object> query(HQLCriteria hqlCriteria, String targetClassName) throws ApplicationException;
	public List<Object> query(HQLCriteria hqlCriteria) throws ApplicationException;

	public List<Object> search(Class targetClass, List<Object> objList) throws ApplicationException;
	public List<Object> search(Class targetClass, Object obj) throws ApplicationException;
	public List<Object> search(String path, List<Object> objList) throws ApplicationException;
	public List<Object> search(String path, Object obj) throws ApplicationException;	

	//Infrastucture Support Methods
	public List<Object> query(Object criteria, Integer firstRow, String targetClassName) throws ApplicationException;
	public Integer getQueryRowCount(Object criteria, String targetClassName) throws ApplicationException;
	public List<Object> getAssociation(Object source, String associationName) throws ApplicationException;
}