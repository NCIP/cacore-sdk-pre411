/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package gov.nih.nci.system.dao;

import gov.nih.nci.system.applicationservice.ApplicationException;

/**
 * An exception class for DAO related functions
 * 
 * @author Satish Patel
 */

public class DAOException extends ApplicationException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor. Constructs the (@link RollbackException) object
	 */
	public DAOException() {
		super();
	}

	/**
	 * DAOException constructor with exception message
	 * 
	 * @param message -
	 *            exception message
	 */
	public DAOException(String message) {
		super(message);
	}

	/**
	 * DAOException constructor with exception message and Throwable object
	 * 
	 * @param message -
	 *            exception message
	 * @param t -
	 *            Throwable object
	 */
	public DAOException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * DAOException constructor with Throwable object
	 * 
	 * @param t -
	 *            throwable object
	 */
	public DAOException(Throwable t) {
		super(t);
	}

}
