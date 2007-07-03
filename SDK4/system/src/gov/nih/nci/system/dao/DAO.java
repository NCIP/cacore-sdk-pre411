package gov.nih.nci.system.dao;

/**
 * @author Satish Patel
 */
public interface DAO {

	/**
	 * Queries the datasource 
	 * 
	 * @param request 
	 *           
	 * @return
	 * @throws DAOException
	 */
	public Response query(Request request) throws DAOException, Exception;

}
