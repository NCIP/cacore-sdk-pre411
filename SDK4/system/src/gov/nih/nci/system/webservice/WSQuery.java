/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package gov.nih.nci.system.webservice;

import java.util.List;

public interface WSQuery {
	
	public int getTotalNumberOfRecords(String targetClassName, Object criteria) throws Exception;

	public List queryObject(String targetClassName, Object criteria) throws Exception;

	public List query(String targetClassName, Object criteria, int startIndex) throws Exception;
	
	public List getAssociation(Object source, String associationName, int startIndex) throws Exception;

}
