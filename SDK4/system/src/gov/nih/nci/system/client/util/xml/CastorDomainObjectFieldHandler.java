package gov.nih.nci.system.client.util.xml;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.exolab.castor.mapping.GeneralizedFieldHandler;

/**
 * The FieldHandler for the Date class
 *
 */
public class CastorDomainObjectFieldHandler
    extends GeneralizedFieldHandler
{

    private static Logger log = Logger.getLogger(CastorDomainObjectFieldHandler.class);

    /**
     * Creates a new MyDateHandler instance
     */
    public CastorDomainObjectFieldHandler() {
        super();
        setCollectionIteration(false);
//        System.out.println("CastorDomainObjectFieldHandler()");
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
    	
//    	System.out.println("Value: " + value);
        if (value == null) return null;
        
        String setMethodName, getMethodName;
        Class klass = value.getClass();
        Method[] methods = klass.getDeclaredMethods();
        Method tempMethod;
        
//    	System.out.println("Number of methods: " + methods.length);
   	
    	HashSet tempHS = new java.util.HashSet();
        Object[] args = {tempHS};
        Class[] parameterTypes = {Collection.class};
//    	System.out.println("args array initialized: " + args[0].getClass().getName());

        for (int i=0; i < methods.length; i++){
        	
        	tempMethod = methods[i];

        	if ("java.util.Collection".equalsIgnoreCase(tempMethod.getReturnType().getName())){
        		try {
        			
        			getMethodName = tempMethod.getName();
//                	System.out.println("getMethodName: " + getMethodName);
                	setMethodName = 's' + getMethodName.substring(1);
//                	System.out.println("setMethodName: " + setMethodName);
                	
                	tempMethod = klass.getDeclaredMethod(setMethodName, parameterTypes);
        			tempMethod.invoke(value, args);
//            		System.out.println("Successfully set Collection Attribute to empty HashSet for method " + tempMethod.getName());
        		} catch (Exception e) {
        			//log.error("Exception: " + e.getMessage(), e);
        			System.out.println("Exception: " + e.getMessage());
        			System.out.println("Collection Attribute to empty HashSet for method " + tempMethod.getName());
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
//    	System.out.println("*** Domain convertUponSet() called - do nothing ***");
//    	System.out.println("*** Domain Value: " + value);
//    	System.out.println("*** Domain Value class: " + value.getClass().getName());
//    	System.out.println("*** Domain Value size: " + ((HashSet)value).size());
    	return value;
    }   
    public Class getFieldType() {
//    	System.out.println("*** Domain getFieldType() - returning null ***");   	
        return null;
    }

}
