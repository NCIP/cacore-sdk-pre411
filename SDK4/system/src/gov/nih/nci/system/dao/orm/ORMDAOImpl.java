package gov.nih.nci.system.dao.orm;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import gov.nih.nci.system.dao.DAO;
import gov.nih.nci.system.dao.DAOException;
import gov.nih.nci.system.dao.Request;
import gov.nih.nci.system.dao.Response;

/**
 * @author Satish Patel
 *
 */
public class ORMDAOImpl extends HibernateDaoSupport implements DAO 
{

	public Response query(Request request) throws DAOException, Exception {
		//getHibernateTemplate()
		//getSessionFactory();
		
		// TODO Auto-generated method stub
		return null;
	}
}