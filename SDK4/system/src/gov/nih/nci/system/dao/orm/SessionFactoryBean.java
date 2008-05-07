package gov.nih.nci.system.dao.orm;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

public class SessionFactoryBean extends LocalSessionFactoryBean {
	
	protected HibernateConfigurationHolder configHolder;
	
	@Override
	protected Configuration newConfiguration() throws HibernateException {
		Configuration cfg = super.newConfiguration(); 
		configHolder.setConfiguration(cfg);
		return cfg;
	}
	
	public HibernateConfigurationHolder getConfigHolder() {
		return configHolder;
	}
	public void setConfigHolder(HibernateConfigurationHolder configHolder) {
		this.configHolder = configHolder;
	}
}
