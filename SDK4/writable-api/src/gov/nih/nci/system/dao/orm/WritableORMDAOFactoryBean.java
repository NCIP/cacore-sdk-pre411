package gov.nih.nci.system.dao.orm;

import java.util.Map;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class WritableORMDAOFactoryBean extends ORMDAOFactoryBean
{

	public WritableORMDAOFactoryBean(String configLocation, Properties systemProperties, Map systemPropertiesMap) throws Exception {
		super(configLocation, systemProperties, systemPropertiesMap);
	}
	
	protected void createDAO()
	{
		if (isSecurityEnabledFlag())
			setOrmDAO(new WritableORMDAOImpl((SessionFactory)this.getSessionFactory(), (Configuration)this.getConfiguration(), isCaseSensitive(), getResultCountPerQuery(), isInstanceLevelSecurity(), isAttributeLevelSecurity(), getAuthenticationProvider()));
		else
			setOrmDAO(new WritableORMDAOImpl((SessionFactory)this.getSessionFactory(), (Configuration)this.getConfiguration(), isCaseSensitive(), getResultCountPerQuery(), isInstanceLevelSecurity(), isAttributeLevelSecurity(), null));
	}

}