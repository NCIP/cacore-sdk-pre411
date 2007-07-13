package gov.nih.nci.system.client;

import gov.nih.nci.system.applicationservice.ApplicationService;

import java.io.ByteArrayInputStream;
import java.util.Map;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.InputStreamResource;

public class ApplicationServiceProvider
{
	private static ApplicationContext ctx = new ClassPathXmlApplicationContext("application-config.xml");

	private static String DEFAULT_SERVICE = "ServiceInfo";
	
	private static Object lock = new Object();
	
	public static ApplicationService getApplicationService() throws Exception
	{
		return getApplicationService(DEFAULT_SERVICE,null,null,null);
	}

	public static ApplicationService getApplicationService(String username, String password) throws Exception
	{
		if(username == null || password == null || username.trim().length() == 0 || password.trim().length() == 0)
				throw new Exception("Cannot authenticate user");
		return getApplicationService(DEFAULT_SERVICE,null,username,password);
	}

	public static ApplicationService getApplicationService(String service) throws Exception
	{
		return getApplicationService(service, null,null,null);
	}	

	public static ApplicationService getApplicationService(String service, String username, String password) throws Exception
	{
		if(username == null || password == null || username.trim().length() == 0 || password.trim().length() == 0)
			throw new Exception("Cannot authenticate user");
		return getApplicationService(service,null,username,password);
	}
	
	public static ApplicationService getApplicationServiceFromUrl(String url) throws Exception
	{
		return getApplicationService(DEFAULT_SERVICE,url,null,null);
	}

	public static ApplicationService getApplicationServiceFromUrl(String url,String username, String password) throws Exception
	{
		if(username == null || password == null || username.trim().length() == 0 || password.trim().length() == 0)
			throw new Exception("Cannot authenticate user");
		return getApplicationService(DEFAULT_SERVICE,url,username,password);
	}

	public static ApplicationService getApplicationServiceFromUrl(String url, String service) throws Exception
	{
		return getApplicationService(service, url,null,null);
	}	

	public static ApplicationService getApplicationServiceFromUrl(String url, String service, String username, String password) throws Exception
	{
		if(username == null || password == null || username.trim().length() == 0 || password.trim().length() == 0)
			throw new Exception("Cannot authenticate user");
		return getApplicationService(service,url,username,password);
	}


	
	@SuppressWarnings("unchecked")
	public static ApplicationService getApplicationService(String service, String url, String username, String password) throws Exception
	{
		Boolean secured = username!=null && password!=null;
		ApplicationContext context = getApplicationContext(service, url,secured);

		ApplicationService as = null;
		AuthenticationProvider ap = null;
		Map<String,Object> serviceInfoMap = (Map<String, Object>) ctx.getBean(service);
		if(serviceInfoMap == null)
		{
			as = (ApplicationService) context.getBean("applicationService");
			ap = (AuthenticationProvider)context.getBean("authenticationManager");
		}
		else
		{
			if(url == null) url ="";

			as = (ApplicationService) serviceInfoMap.get("APPLICATION_SERVICE_BEAN"+url);
			ap = (AuthenticationProvider)serviceInfoMap.get("AUTHENTICATION_SERVICE_BEAN"+url);
		}

		if(!secured) return as;
		
		Authentication auth = new UsernamePasswordAuthenticationToken(username,password);
		auth = ap.authenticate(auth);
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		return as;
	}
	
	@SuppressWarnings("unchecked")
	private static ApplicationContext getApplicationContext(String service, String url, Boolean secured) throws Exception
	{
		if(service == null || service.trim().length() == 0)
			throw new Exception("Name of the service can not be empty");
		
		Map<String,Object> serviceInfoMap = (Map<String, Object>) ctx.getBean(service);
		
		if(serviceInfoMap == null)
			throw new Exception("Change the damn configuration file!!!");
		
		//Initialized instances found in the configuration
		ApplicationService as = (ApplicationService)serviceInfoMap.get("APPLICATION_SERVICE_BEAN");
		AuthenticationProvider ap = (AuthenticationProvider)serviceInfoMap.get("AUTHENTICATION_SERVICE_BEAN");
		
		//Returning initialized instance
		if((!secured && as!=null && url == null)||(secured && as!=null && ap!=null && url == null)) //Empty URL, return the service. This helps in improving performance
			return ctx;
		
		String serviceInfo = (String)serviceInfoMap.get("APPLICATION_SERVICE_CONFIG");;
		if(url == null)	url = (String)serviceInfoMap.get("APPLICATION_SERVICE_URL");

		//URL_KEY must be present if the user is trying to use the url to reach the service
		if(serviceInfo==null ||(url == null && serviceInfo.indexOf("URL_KEY")>0) || (url!=null && serviceInfo.indexOf("URL_KEY")<0))
			throw new Exception("Change the damn configuration file!!!");
		
		//Resetting the URL as URL_KEY is absent in the configuration
		if(serviceInfo.indexOf("URL_KEY") <0 || url == null)
			url="";
		
		as = (ApplicationService)serviceInfoMap.get("APPLICATION_SERVICE_BEAN"+url);
		ap = (AuthenticationProvider)serviceInfoMap.get("AUTHENTICATION_SERVICE_BEAN"+url);

		if((!secured && as!=null)||(secured && as!=null && ap!=null)) //Return pre-built service. This helps in improving performance
			return ctx;
		
		
		serviceInfo = serviceInfo.replace("URL_KEY", url);
		
		//Prepare in memory configuration from the information retrieved of the configuration file
		String xmlFileString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE beans PUBLIC \"-//SPRING//DTD BEAN//EN\" \"http://www.springframework.org/dtd/spring-beans.dtd\"><beans>"+serviceInfo+"</beans>";
		GenericApplicationContext context = new GenericApplicationContext();
		XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);
		xmlReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
		InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(xmlFileString.getBytes()));
		xmlReader.loadBeanDefinitions(inputStreamResource);
		context.refresh();

		as = (ApplicationService) context.getBean("applicationService");
		ap = (AuthenticationProvider)context.getBean("authenticationManager");
		
		//Make sure the configuration has the required objects present
		if(as==null || (secured && ap==null))
			throw new Exception("Change the damn configuration file!!!");

		synchronized (lock){
			//Store the services in the map. Improves performance for next time access
			serviceInfoMap.put("APPLICATION_SERVICE_BEAN"+url, as);
			serviceInfoMap.put("AUTHENTICATION_SERVICE_BEAN"+url, ap);
		}
		return context;
	}
}