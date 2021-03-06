/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package gov.nih.nci.system.query.nestedcriteria;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * This class holds the nested criteria in the linked list format
 * 
 * @author Satish Patel
 */
public class NestedCriteria implements Serializable{

	/**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	private String targetObjectName;
	private String sourceObjectName;
	private String roleName;
	private boolean targetCollection;

	private List sourceObjectList = new ArrayList();

	private NestedCriteria internalNestedCriteria;

	public void setTargetObjectName(String targetName)
	{
		this.targetObjectName = targetName;
	}

	public String getTargetObjectName()
	{
		return this.targetObjectName;
	}

	public void setSourceObjectName(String sourceName)
	{
		this.sourceObjectName = sourceName;
	}

	public String getSourceName()
	{
		return this.sourceObjectName;
	}

	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}

	public String getRoleName()
	{
		return this.roleName;
	}

	public void setInternalNestedCriteria(NestedCriteria nestedCriteria)
	{
		this.internalNestedCriteria = nestedCriteria;
	}

	public NestedCriteria getInternalNestedCriteria()
	{
		return this.internalNestedCriteria;
	}

	public void setSourceObjectList(List objList)
	{
		this.sourceObjectList = objList;
	}

	public List getSourceObjectList()
	{
		return this.sourceObjectList;
	}

	public void addSourceObject(Object obj)
	{
		sourceObjectList.add(obj);
	}

	public boolean isTargetCollection()
	{
		return targetCollection;
	}

	public void setTargetCollection(boolean targetCollection)
	{
		this.targetCollection = targetCollection;
	}

	public NestedCriteria(String sourceObjectName, String targetObjectName, String roleName, NestedCriteria internalNestedCriteria)
	{
		super();
		this.sourceObjectName = sourceObjectName;
		this.targetObjectName = targetObjectName;
		this.roleName = roleName;
		this.internalNestedCriteria = internalNestedCriteria;
	}

	public NestedCriteria() {

	}
}

