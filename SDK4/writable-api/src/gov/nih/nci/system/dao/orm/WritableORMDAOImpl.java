package gov.nih.nci.system.dao.orm;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import gov.nih.nci.security.acegi.authentication.CSMAuthenticationProvider;
import gov.nih.nci.system.dao.WritableDAO;

public class WritableORMDAOImpl extends ORMDAOImpl implements WritableDAO
{

	public WritableORMDAOImpl(SessionFactory sessionFactory, Configuration config, boolean caseSensitive, int resultCountPerQuery, boolean instanceLevelSecurity, boolean attributeLevelSecurity, CSMAuthenticationProvider authenticationProvider) {
		super(sessionFactory, config, caseSensitive, resultCountPerQuery,
				instanceLevelSecurity, attributeLevelSecurity, authenticationProvider);
	}

}