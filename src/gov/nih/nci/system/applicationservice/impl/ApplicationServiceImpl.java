package gov.nih.nci.system.applicationservice.impl;

import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.dao.WritableDAO;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Kunal Modi (Ekagra Software Technologies Ltd.)
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ApplicationServiceImpl extends ApplicationServiceBusinessImpl implements ApplicationService
{
	private static Logger log = Logger.getLogger(ApplicationServiceImpl.class.getName());
	private WritableDAO writableDAO = null;

	/**
	 * Default Constructor. It obtains appropriate implementation of the
	 * {@link ApplicationService}interface and caches it. It also instantiates
	 * the instance of writableDAO and caches it.
	 */
	public ApplicationServiceImpl()
	{
		this.writableDAO = new WritableDAO();
	}


	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getBeanInstance()
	 */
	//@Override
	public ApplicationService getBeanInstance()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getBeanInstance(java.lang.String)
	 */
	//@Override
	public ApplicationService getBeanInstance(String URL)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#createObject(java.lang.Object)
	 */
	/*@WRITABLE_API_START@*/
	// NOTE: Use only "//" for comments in the following method
	public Object createObject(Object obj) throws ApplicationException
	{
		return writableDAO.createObject(obj);
	}
	/*@WRITABLE_API_END@*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#updateObject(java.lang.Object)
	 */
	/*@WRITABLE_API_START@*/
	// NOTE: Use only "//" for comments in the following method
	public Object updateObject(Object obj) throws ApplicationException
	{
		return writableDAO.updateObject(obj);
	}
	/*@WRITABLE_API_END@*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#removeObject(java.lang.Object)
	 */
	/*@WRITABLE_API_START@*/
	// NOTE: Use only "//" for comments in the following method
	public void removeObject(Object obj) throws ApplicationException
	{
		writableDAO.removeObject(obj);
	}
	/*@WRITABLE_API_END@*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.system.applicationservice.ApplicationService#getObjects(java.lang.Object)
	 */
	/*@WRITABLE_API_START@*/
	// NOTE: Use only "//" for comments in the following method
	public List getObjects(Object obj) throws ApplicationException
	{
		return writableDAO.getObjects(obj);
	}
	/*@WRITABLE_API_END@*/

}
