/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk/LICENSE.txt for details.
 */

package gov.nih.nci.system.query.hibernate;

import java.io.Serializable;
import java.util.List;

/**
 * @author Satish Patel
 *
 */
public class HQLCriteria implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String hqlString;
	private List parameters;	
	
	public HQLCriteria(String hqlString)
	{
		this.hqlString = hqlString;
	}
	
	public HQLCriteria(String hqlString, List parameters)
	{
		this.hqlString = hqlString;
		this.parameters = parameters;
	}	

	public String getHqlString()
	{
		return this.hqlString;
	}

	public List getParameters() {
		return parameters;
	}

}
