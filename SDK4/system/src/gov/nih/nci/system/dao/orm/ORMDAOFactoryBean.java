package gov.nih.nci.system.dao.orm;

import gov.nih.nci.system.util.ClassCache;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

public class ORMDAOFactoryBean implements FactoryBean {
	
	private static Logger log = Logger.getLogger(ORMDAOFactoryBean.class.getName());
	
	private LocalSessionFactoryBean lsfb;
	
	private ORMDAOImpl ormDAO;

	public ORMDAOFactoryBean(ClassCache classCache, String configLocation, boolean caseSensitive, int resultCountPerQuery) {	
		lsfb = new LocalSessionFactoryBean();
		Resource resource = new ClassPathResource(configLocation);
		lsfb.setConfigLocation(resource);
		
		try {
			lsfb.afterPropertiesSet();
		} catch (Exception e){
			log.error(e);
		}
		ormDAO = new ORMDAOImpl((SessionFactory)lsfb.getObject(), (Configuration)lsfb.getConfiguration(), classCache, caseSensitive, resultCountPerQuery);
	}
	
	public Object getObject() {
		return ormDAO;
	}
	
	public Class getObjectType() {
		return ormDAO.getClass();
	}
	
	public boolean isSingleton(){
		return true;
	}
}
