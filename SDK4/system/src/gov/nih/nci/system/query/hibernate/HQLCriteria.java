package gov.nih.nci.system.query.hibernate;

import java.io.Serializable;

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
	
	public HQLCriteria(String hqlString)
	{
		setHqlString(hqlString);
	}
	
	public void setHqlString(String hqlString)
	{
		this.hqlString = hqlString;		
	}
	public String getHqlString()
	{
		return this.hqlString;
	}

}
