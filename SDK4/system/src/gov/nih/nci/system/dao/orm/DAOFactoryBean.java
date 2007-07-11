package gov.nih.nci.system.dao.orm;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

public class DAOFactoryBean implements FactoryBean {
	
	private static Logger log = Logger.getLogger(DAOFactoryBean.class.getName());
	
	private DAOSessionFactoryBean sessionFactory;
	private LocalSessionFactoryBean lsfb;
	
	private ORMDAOImpl ormDAO;

	public DAOFactoryBean(String configLocation, boolean caseSensitive, int resultCountPerQuery) {	
		super();
		
//		sessionFactory = new DAOSessionFactoryBean(configLocation);
		lsfb = new LocalSessionFactoryBean();
		Resource resource = new ClassPathResource(configLocation);
		lsfb.setConfigLocation(resource);
		
		try {
			lsfb.afterPropertiesSet();
		} catch (Exception e){
			log.error(e);
		}
		
		
		Configuration config = lsfb.getConfiguration();
			
		PersistentClass pClass;
		for(Iterator iter = config.getClassMappings(); iter.hasNext();){
			pClass = (PersistentClass)iter.next();
			log.debug("pClass: " + pClass.getClassName());
		}

		ormDAO = new ORMDAOImpl((SessionFactory)lsfb.getObject(), config, caseSensitive, resultCountPerQuery);
	}
	
	public Object getObject() {
		return ormDAO;
	}
	
	public Class getObjectType() {
		return ormDAO.getClass();
	}
	
	public boolean isSingleton(){
		return false;
	}
}
