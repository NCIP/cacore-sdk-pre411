package gov.nih.nci.system.dao;

import gov.nih.nci.system.applicationservice.ApplicationException;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2001-2004 SAIC. Copyright 2001-2003 SAIC. This software was developed in conjunction with the National Cancer Institute, 
 * and so to the extent government employees are co-authors, any rights in such works shall be subject to Title 17 of the United States Code, section 105. 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the disclaimer of Article 3, below. Redistributions 
 * in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other 
 * materials provided with the distribution. 
 * 2. The end-user documentation included with the redistribution, if any, must include the following acknowledgment:
 * "This product includes software developed by the SAIC and the National Cancer Institute."
 * If no such end-user documentation is to be included, this acknowledgment shall appear in the software itself, 
 * wherever such third-party acknowledgments normally appear. 
 * 3. The names "The National Cancer Institute", "NCI" and "SAIC" must not be used to endorse or promote products derived from this software. 
 * 4. This license does not authorize the incorporation of this software into any third party proprietary programs. This license does not authorize 
 * the recipient to use any trademarks owned by either NCI or SAIC-Frederick. 
 * 5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE, 
 * SAIC, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 * <!-- LICENSE_TEXT_END -->
 */
/**
 * An exception class for DAO related functions
 * 
 * @author caBIO Team
 * @version 1.0
 */

public class DAOException extends ApplicationException {
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
