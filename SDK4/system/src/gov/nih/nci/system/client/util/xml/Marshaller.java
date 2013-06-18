/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package gov.nih.nci.system.client.util.xml;

/**
 * The object used for serializing a Java object
 */

public interface Marshaller {
	/**
	 * Returns a String representation of a Java object.
	 *
	 * @param  name the location of the image, relative to the url argument
	 * @return      the string representation of  the object
	 */

	String toXML(Object object)throws XMLUtilityException;
	
	/**
	 * Serializes an object to a java.io.Writer
	 *
	 * @param  object the object to be serialized.
	 * @param  reader the writer to be written to.
	 */
	void toXML(Object object, java.io.Writer writer)throws XMLUtilityException;
	
	/**
	 * Returns the base marshaller
	 *
	 * @return      the base marshaller object
	 */

	Object getBaseMarshaller()throws XMLUtilityException;
}
