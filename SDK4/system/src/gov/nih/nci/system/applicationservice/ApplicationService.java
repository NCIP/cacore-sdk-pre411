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
	
	public int getQueryRowCount(Object criteria, String targetClassName) throws ApplicationException;
	
	public List<Object> query(CQLQuery cqlQuery, String targetClassName) throws ApplicationException;

	public List<Object> query(DetachedCriteria detachedCriteria, String targetClassName) throws ApplicationException;
	
	public List<Object> query(HQLCriteria hqlCriteria, String targetClassName) throws ApplicationException;
	
	public List<Object> query(Object criteria, int firstRow, int resultsPerQuery, String targetClassName) throws ApplicationException;
	
	public List<Object> search(Class targetClass, List objList) throws ApplicationException;
	
	public List<Object> search(Class targetClass, Object obj) throws ApplicationException;
	
	public List search(String path, List objList) throws ApplicationException;
	
	public List<Object> search(String path, Object obj) throws ApplicationException;	

}