
Steps to run Webservice Java client:

1. Download and install Axis 1.1 from http://ws.apache.org/axis. Include all Axis jars in your classpath

2. Obtain caCORE domain objects. This can be done in one of two ways:
	a. Download the caCORE client jar, and include it in your classpath; or
	b. Compile client side webservice stub driectly from wsdl:
		java org.apache.axis.wsdl.WSDL2Java http://cbiodev104.nci.nih.gov:29080/cacore30/ws/caCoreWebService?wsdl
	   After generating the domain objects, place them in your classpath.
	
3. Compile client:
	javac WSTestClient.java

4. Run test client:
	java WSTestClient
	
Note: You should use 1.4 JVM, we have encountered a NULL pointer exception when using 1.5 JVM.


