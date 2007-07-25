package gov.nih.nci.system.client.util.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.exolab.castor.mapping.GeneralizedFieldHandler;

/**
 * The FieldHandler for the Date class
 *
 */
public class CastorCollectionFieldHandler
	extends GeneralizedFieldHandler
{

    private static Logger log = Logger.getLogger(CastorCollectionFieldHandler.class);

    /**
     * Creates a new MyDateHandler instance
     */
    public CastorCollectionFieldHandler() {
        super();
        setCollectionIteration(false);
//        System.out.println("CastorCollectionFieldHandler()");
    }
    
    
    public Object convertUponGet(Object value) {
//    	System.out.println("*** convertUponGet(Object value) called ***");
//    	System.out.println("Value: " + value);
//    	System.out.println("Value.class: " + value.getClass().getName());
    	
        if (value == null) return null;
        
        String setMethodName, getMethodName;
        
        Class klass;
        Method[] methods;
        Method tempMethod;
        Object tempObject = null;
        
        java.util.Collection<Object> tempCollection = new ArrayList<Object>();
    	HashSet tempHS = new java.util.HashSet();
        Object[] args = {tempHS};
        Class[] parameterTypes = {Collection.class};
//    	System.out.println("args array initialized: " + args[0].getClass().getName());       

        Enumeration collIterator = (Enumeration)value;
        
        while (collIterator.hasMoreElements()){

        	try {
        		tempCollection.add(CastorCollectionFieldHandler.deepCopy((Object)collIterator.nextElement()));
        	} catch (Exception e) {
        		System.out.println("Exception caught trying to deepCopy object");
        	}
        }
        
//        System.out.println("*** Pre-final tempCollection.size(): " + tempCollection.size());
        
        Iterator iter = tempCollection.iterator();
        while (iter.hasNext()){  
        	tempObject = iter.next();
            klass = tempObject.getClass();
	        methods = klass.getDeclaredMethods();
	        
//	    	System.out.println("Number of methods: " + methods.length);
	
	        for (int i=0; i < methods.length; i++){
	        	
	        	tempMethod = methods[i];
	
//	        	System.out.println("tempMethod[" + i + "].getName(): " + tempMethod.getName());
//	        	System.out.println("tempMethod[" + i + "].getReturnType().getName(): " + tempMethod.getReturnType().getName());
	        	
	        	if ("java.util.Collection".equalsIgnoreCase(tempMethod.getReturnType().getName())){
	        		try {
	        			
	        			getMethodName = tempMethod.getName();
//	                	System.out.println("getMethodName: " + getMethodName);
	                	setMethodName = 's' + getMethodName.substring(1);
//	                	System.out.println("setMethodName: " + setMethodName);
	                	
	                	tempMethod = klass.getDeclaredMethod(setMethodName, parameterTypes);
	        			tempMethod.invoke(tempObject, args);
//	            		System.out.println("Successful: Collection Attribute set to empty HashSet for method " + tempMethod.getName());

	        		} catch (Exception e) {
	        			System.out.println("Exception: " + e.getMessage());
	        			System.out.println("Unsuccessful:  Collection Attribute NOT set to empty HashSet for method " + tempMethod.getName());
	        		}
	        	}
	        }
        }
        
//        System.out.println("*** final tempCollection.size(): " + tempCollection.size());
        if (tempCollection.size() == 0){return null;}
        return tempCollection;
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
//    	System.out.println("*** convertUponSet() called - do nothing ***");
//    	System.out.println("*** Value: " + value);
//    	System.out.println("*** Value class: " + value.getClass().getName());
//    	System.out.println("*** Value size: " + ((ArrayList)value).size());
        return value;
    }

    /**
     * Returns the class type for the field that this
     * GeneralizedFieldHandler converts to and from. This
     * should be the type that is used in the
     * object model.
     *
     * @return the class type of the field
     */
    public Class getFieldType() {
//    	System.out.println("*** Domain getFieldType() - returning null ***");
    	return null;
    }

    /**
     * Creates a new instance of the object described by
     * this field.
     *
     * @param parent The object for which the field is created
     * @return A new instance of the field's value
     * @throws IllegalStateException This field is a simple
     *  type and cannot be instantiated
     */
    public Object newInstance( Object parent )
        throws IllegalStateException
    {
//    	System.out.println("*** newInstance() called ***");
//		System.out.println("**** setValue parent.getClass().getName(): " + parent.getClass().getName());
//    	System.out.println("*** END newInstance() ***\n");
        return null;
    }
    
   static public Object deepCopy(Object oldObj) throws Exception
   {
//	  System.out.println("deepCopy called");
      ObjectOutputStream oos = null;
      ObjectInputStream ois = null;
      try
      {
         ByteArrayOutputStream bos = 
               new ByteArrayOutputStream(); // A
         oos = new ObjectOutputStream(bos); // B
         // serialize and pass the object
         oos.writeObject(oldObj);   // C
         oos.flush();               // D
         ByteArrayInputStream bin = 
               new ByteArrayInputStream(bos.toByteArray()); // E
         ois = new ObjectInputStream(bin);                  // F
         // return the new object
         return ois.readObject(); // G
      }
      catch(Exception e)
      {
         System.out.println("Exception in ObjectCloner = " + e);
         throw(e);
      }
      finally
      {
         oos.close();
         ois.close();
      }
   }
       

}
