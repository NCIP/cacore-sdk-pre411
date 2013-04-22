/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk/LICENSE.txt for details.
 */

package gov.nih.nci.system.client.util.xml;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

/**
 * The FieldHandler for the Date class
 *
 */
public class CastorDomainObjectFieldHandler
    extends BaseCastorFieldHandler
{

    private static Logger log = Logger.getLogger(CastorDomainObjectFieldHandler.class);

    /**
     * Creates a new MyDateHandler instance
     */
    public CastorDomainObjectFieldHandler() {
        super();
        setCollectionIteration(false);
    }

    /**
     * This method is used to convert the value when the
     * getValue method is called. The getValue method will
     * obtain the actual field value from given 'parent' object.
     * This convert method is then invoked with the field's
     * value. The value returned from this method will be
     * the actual value returned by getValue method.
     *
     * @param value the object value to convert after
     *  performing a get operation
     * @return the converted value.
     */
    public Object convertUponGet(Object value) {
    	
    	log.debug("Value: " + value);
        if (value == null) return null;
        
        try {
        	value = convertObject(value);
        } catch (Exception e){
        	log.error("Exception caught: ", e);
        }
        
        String setMethodName, getMethodName;
        Class klass = value.getClass();
        Method[] methods = klass.getMethods();
        Method tempMethod;
        
    	log.debug("Number of methods: " + methods.length);
   	
    	ArrayList<Object> tempList = new ArrayList<Object>();
        Object[] args = {tempList};
        Class[] parameterTypes = {Collection.class};
    	log.debug("args array initialized: " + args[0].getClass().getName());

        for (int i=0; i < methods.length; i++){
        	
        	tempMethod = methods[i];

        	if ("java.util.Collection".equalsIgnoreCase(tempMethod.getReturnType().getName())){
        		try {
        			
        			getMethodName = tempMethod.getName();
                	log.debug("getMethodName: " + getMethodName);
                	setMethodName = 's' + getMethodName.substring(1);
                	log.debug("setMethodName: " + setMethodName);
                	
                	tempMethod = klass.getMethod(setMethodName, parameterTypes);
        			tempMethod.invoke(value, args);
            		log.debug("Successfully set Collection Attribute to empty ArrayList for method " + tempMethod.getName());
        		} catch (Exception e) {
        			//log.debug("Exception: " + e.getMessage(), e);
        			log.error("Exception: " + e.getMessage());
        			log.error("Collection Attribute to empty HashSet for method " + tempMethod.getName());
        		}
        	}
        }

        return value;
    }


    /**
     * This method is used to convert the value when the
     * setValue method is called. The setValue method will
     * call this method to obtain the converted value.
     * The converted value will then be used as the value to
     * set for the field.
     *
     * @param value the object value to convert before
     *  performing a set operation
     * @return the converted value.
     */
    public Object convertUponSet(Object value) {
    	log.debug("*** Domain convertUponSet() called - do nothing ***");
    	return value;
    }   
    public Class getFieldType() {
    	log.debug("*** Domain getFieldType() - returning null ***");   	
        return null;
    }

}
