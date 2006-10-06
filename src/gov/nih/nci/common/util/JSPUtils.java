/**
 * Created on Dec 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nih.nci.common.util;

import java.util.*;
import javax.servlet.*;
import java.lang.reflect.*;
import org.apache.log4j.*;


/**
 * @author LeThai
 *
 */

public class JSPUtils 
{    
    private static Logger log = Logger.getLogger(JSPUtils.class.getName());
    private static JSPUtils jspUtils;
    private static Properties properties = new Properties();    
    private static List domainNames = new ArrayList();
    private static Set packages = new HashSet();
    private static final String SEMICOLON_SEPARATOR = ";";
    private static final String COMMA_SEPARATOR = ",";
       
   
    /**
     * Instantiate JSPUtils and read bean properties
     * @param config
     * @return JSPUtils
     */
    synchronized public static JSPUtils getJSPUtils(ServletConfig config)
    {       
        try
        {
            if(jspUtils == null)
            {                
                List fileList = new ArrayList();
                ServletContext context = config.getServletContext();
                String beanFiles = context.getInitParameter ("cacoreBeans.Properties");
                
                jspUtils = new JSPUtils();         
                fileList = getFileList(beanFiles);
                
                loadProperties(fileList);
            }
        }
        catch(Exception e)
        {
            log.error(e.getMessage());
        }
        return jspUtils;
        
    }

    
    /**
     * Get all the domain names includes package information
     * @return list of domain names
     */
    public List getDomainNames()
    {      
          
       Collections.sort(domainNames);       
        return domainNames;
        
    }
    /**
     * Get all the packages
     * @return all packages
     */
    public List getPackages()
    {        
        List pkg = new ArrayList(packages);        
        Collections.sort(pkg);        
        return pkg;
    }
    
    
    /**
     * Get all the domain names includes package information for a specified package name
     * @param packageName
     * @return list of domain names
     */
    public List getClassNames(String packageName)
    {
        Collections.sort(domainNames);
        if(packageName == null || packageName.equalsIgnoreCase("All"))
        {
            return domainNames;
        }
        //filter the domainNames to get all classes in this package
        List packageClasses = new ArrayList();
        String ckassName="";
        for(int i=0; i<domainNames.size(); i++)
        {
            ckassName = (String)domainNames.get(i);
            if(ckassName.indexOf(packageName, 0) >= 0)
            {      
                packageClasses.add(ckassName);                
            }
        }
        Collections.sort(packageClasses);
        return packageClasses;
    }
    
       
    /**
     * Get the list of all fields for the class
     * @param className
     * @return List of all fields for the given class
     */
    public List getAllFields(String className)
    {
        List  fieldNames = new ArrayList();
        
        try
        {
            Class ckass = Class.forName(className);
                        
            HTTPUtils httpUtils = new HTTPUtils();
            Field[] fields = httpUtils.getAllFields(ckass);
            String fieldType;
            for(int i=0; i< fields.length; i++)
            {
                fields[i].setAccessible(true);
                fieldType = fields[i].getType().getName();
                
                if (isSearchable(fieldType))
                {
                	fieldNames.add(fields[i].getName());    
                }
            }
        }
        catch(Exception e)
        {
            log.error(e.getMessage());
            
        }
        return fieldNames;
    
    }
    
    /**
     * Get the list of all Fields for the class
     * @param className
     * @return Field[] of all fields for the given class
     */
    public List getSearchableFields(String className)
    {
        Field[] fields = null;
        List searchableFields = new ArrayList();
        
        try
        {
            Class ckass = Class.forName(className);
                        
            HTTPUtils httpUtils = new HTTPUtils();
            fields = httpUtils.getAllFields(ckass);
            String fieldType;
            
            for(int i=0; i< fields.length; i++)
            {
                fields[i].setAccessible(true);
                fieldType = fields[i].getType().getName();
                
                if (isSearchable(fieldType))
                {
                	searchableFields.add(fields[i]);    
                }
               
            }            
        }
        catch(Exception e)
        {
            log.error(e.getMessage());
            
        }
        return searchableFields;
    
    }    
     
    private boolean isSearchable(String fieldType){
    	boolean isSearchable=false;
    	
    	if(fieldType.equals("java.lang.Long") || 
        		fieldType.equals("java.lang.String") || 
        		fieldType.equals("java.lang.Integer") || 
        		fieldType.equals("java.lang.Float") || 
        		fieldType.equals("java.lang.Double") || 
        		fieldType.equals("java.lang.Boolean") || 
        		fieldType.equals("java.util.Date")
        		){
    		isSearchable = true;
    	}
    	
    	return isSearchable;
    }
   
    private static void loadProperties(List fileList) throws Exception
    {     
        if(properties.size() <1)
        {  String fileName="";
            for(int i=0; i<fileList.size(); i++)
            {
                try
                {
                    fileName = (String)fileList.get(i);
                    properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
                    //System.out.println("reading properties ...  " + fileName);
                }catch(Exception ex)
                {
                    log.warn("Cannot locate the file: " + fileName);
                    continue;
                    //throw new Exception("Cannot locate file - "+ fileName);
                }
            }              
                
        }
        
        if(properties != null)
        {
            String value = null;
            for(Iterator i= properties.keySet().iterator(); i.hasNext();){
                String key = (String)i.next();    
                domainNames.add(key);                
                value = (String)properties.get(key);                
                packages.add(value);
                              
            }
        }
        
    }
    static List getFileList(String files){
        List fileList = new ArrayList();
        
        if(files.indexOf(SEMICOLON_SEPARATOR)>0){
            StringTokenizer st = new StringTokenizer(files, SEMICOLON_SEPARATOR);
            while(st.hasMoreTokens()){
                fileList.add((st.nextToken()).trim());
            }            
        }
        else if(files.indexOf(COMMA_SEPARATOR)>0){
            StringTokenizer st = new StringTokenizer(files, COMMA_SEPARATOR);
            while(st.hasMoreTokens()){
                fileList.add((st.nextToken()).trim());
            }
        }
        else{
            fileList.add(files.trim());
        }
        
        return fileList;
    }
    
   
    
}
