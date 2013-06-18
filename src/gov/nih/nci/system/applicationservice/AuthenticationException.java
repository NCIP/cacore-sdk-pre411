/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

/*
 * Created on Jun 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nih.nci.system.applicationservice;

/**
 * This exception is thrown by all the methods of the {@link ApplicationService}
 * interface. This exception contains the actual error or the error message of 
 * the business error that occured during processing the request.
 * 
 * @author Ekagra Software Technologies Ltd.
 */
public class AuthenticationException extends SecurityException
{
	/**
	 * Default constructor. Constructs the (@link AuthenticationException) object 
	 */
	public AuthenticationException()
	{
		super();
	}
	/**
	 * Constructs the {@link AuthenticationException} object with the passed message 
	 * @param message The message which is describes the exception caused
	 */
	public AuthenticationException(String message)
	{
		super(message);
	}
	/**
	 * Constructs the {@link AuthenticationException} object with the passed message.
	 * It also stores the actual exception that occured 
	 * @param message The message which describes the exception
	 * @param cause The actual exception that occured
	 */
	public AuthenticationException(String message, Throwable cause)
	{
		super(message, cause);
	}
	/**
	 * Constructs the {@link AuthenticationException} object storing the actual 
	 * exception that occured 
	 * @param cause The actual exception that occured
	 */
	public AuthenticationException(Throwable cause)
	{
		super(cause);
	}
}
