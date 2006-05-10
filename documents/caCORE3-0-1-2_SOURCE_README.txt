                            Readme.txt

              caCORE: source code and server installation
                          Version 3.0.1.2
                         September 9, 2005
    
================================================================
                            Contents
================================================================
    
    1.0 Introduction
    2.0 Required Software 
          2.1 Java 2 Platform
          2.2 Servlet Container
        
    3.0 Intall in Tomcat
          3.1 Edit server.xml
          3.2 Deploy the war file

    4.0 Intall in JBoss
          4.1 Edit oracle-ds.xml
          4.2 Deploy the war file
   
    5.0 Tests
         6.1 Web application
        
    6.0 License
    
================================================================
                       1.0 Introduction
================================================================
    
    This document contains the instructions for installing 
    a local version of caCORE version 3.0 on a machine running a
    Windows 2000/NT/UNIX/LINUX operating system.  
    
    
     
================================================================
                     2.0 Required Software 
================================================================
    
----------------------------------------------------------------
2.1 Java 2 Platform (required)
----------------------------------------------------------------

    Java 2 Platform Enterprise Edition (J2EE) or Standard
    Edition (J2SE) is required to compile and run caCORE. 
    J2SDK 1.4.2_06 or later version is required. You can 
    download the JDK from Sun Microsystems, Inc. at the 
    following locations:
    
        http://java.sun.com/j2ee/
        http://java.sun.com/j2se/

----------------------------------------------------------------
2.2 Servlet Container
----------------------------------------------------------------
    This installation assumes that you have either JBoss 4.0.0 or 
    later or Tomcat 4.1 or later installed.

    If using JBoss skip to section "4.0. Install in JBoss"
    If using Tomcat proceed to the next section.

    If you don't have either they can be found at:


      JBOSS:  http://sourceforge.net/projects/jboss/
      TOMCAT: http://jakarta.apache.org/tomcat/

================================================================
                   3.0 Install in Tomcat
================================================================
    
----------------------------------------------------------------
 3.1 Edit server.xml
----------------------------------------------------------------

    Consult Tomcat documentation on the proper way to configure your
    data sources in the server.xml file.

----------------------------------------------------------------
 3.1 Deploy the war files 
----------------------------------------------------------------

    The following file cacore30.war should be dropped into the 
    tomcat webapps directory.
  
  
    Restart Tomcat.


================================================================
                   4.0 Install in JBoss
================================================================
     

----------------------------------------------------------------
 4.1 Edit oracle-ds.xml
----------------------------------------------------------------

    The following section must be added to oracle-ds.xml to provide
    datasource information. This oracle-ds.xml file should be placed
    into default\deploy directory of Jboss. 
    <local-tx-datasource>
        <jndi-name>cabio</jndi-name>
        <connection-url>{your_db_server_url}</connection-url>
        <driver-class>{your_database_driver}</driver-class>
        <user-name>{your_username}</user-name>
        <password>{your_password}</password>
        <exception-sorter-class-name>org.jboss.resource.adapter.jdbc.vendor.OracleExceptionSorter</exception-sorter-class-name>
    
        <!--pooling parameters-->
        <min-pool-size>10</min-pool-size>
        <max-pool-size>20</max-pool-size>
        <blocking-timeout-millis>5000</blocking-timeout-millis>
        <idle-timeout-minutes>15</idle-timeout-minutes>
      </local-tx-datasource>

    
----------------------------------------------------------------
 4.2 Deploy the war files 
----------------------------------------------------------------

    The following file cacore30.war should be dropped into the 
    jboss default\deploy directory.
  
  
    Restart JBoss.
    

================================================================
                          5.0 Tests
================================================================

    Assuming you are running Tomcat or JBoss locally, the following should
    work.
    
----------------------------------------------------------------
6.1 Web application
----------------------------------------------------------------

    Entering the following in a web browser should display
    the Happy page for caBIO Example Project if everything is working
    correctly:
   
        http://localhost:8080/cacore30/Happy.jsp


================================================================
                          5.0 Building caCORE
================================================================
   	
    To rebuild the caCORE war file  all you need to do is
    run "ant build-system" from your base installation directory. 
    The warfile will be saved to your output\package\localhost directory.
    In order for the warfile to work properly with your client.jar you
    will need to configure your datasource connections for jboss or
    tomcat.
    
================================================================
                         6.0 License
================================================================
    
    The caBIO version 3.0 software is licensed under the terms
    contained in the licence located at:
    
        - http://ncicb.nci.nih.gov/core/caBIO/technical_resources/core_jar/license
    
    This product includes software developed by the
    Apache Software Foundation (http://www.apache.org/).
    Apache SOAP, Crimson, Xerces, and Xalan are part of Apache
    XML project, Tomcat, ORO, and Lucene are part of Apache
    Jakarta project. All aforementioned Apache projects are trademarks of 
    The Apache Software Foundation. For further
    open source licensing issues pertaining to Apache Software
    Foundation, visit:
    
        - http://www.apache.org/LICENSE 
    
    Sun, Sun Microsystems, Solaris, Java, JavaServer Web
    Development Kit, and JavaServer Pages are trademarks or
    registered trademarks of Sun Microsystems, Inc. The jaxp.jar
    and jaxb-rt-1.0-ea.jar are redistributed as whole binary
    jars and are subject to the Sun license terms as stated in
    
        - http://java.sun.com/xml/docs/summer02/LICENSE.html
    
    UNIX is a registered trademark in the United States and
    other countries, exclusively licensed through X/Open
    Company, Ltd.

    Oracle is a registered trademark of Oracle Corporation.
    
    Windows, WindowsNT, and Win32 are registered trademarks of
    Microsoft Corp. 
    
    All other product names mentioned herein and throughout the
    entire caBIO project are trademarks of their respective
    owners.
    
    
//end
