package gov.nih.nci.system.dao.orm;


import java.util.Collection;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class FilterableHibernateTemplate extends HibernateTemplate {

	private FilterParameterSetter filterParameterSetter;

	public FilterableHibernateTemplate(SessionFactory sessionFactory,FilterParameterSetter filterParameterSetter) {
		super(sessionFactory);
		this.filterParameterSetter = filterParameterSetter;
	}

	protected void enableFilters(Session session) {
		if (filterParameterSetter == null) {
			super.enableFilters(session);
			return;
		}
		
		filterParameterSetter.performPreProcessing(getSessionFactory(), session);
		
		if(filterParameterSetter.getApplyFilters())
		{
			Collection<String> filterNames = getSessionFactory().getDefinedFilterNames();
			if (filterNames != null) {
				for (String name: filterNames) {
					Filter filter = session.enableFilter(name);
					if (filter == null)
						continue;
					filterParameterSetter.setParameters(filter);
				}
			}
		}
		
		filterParameterSetter.performPostProcessing(getSessionFactory(), session);
	}
	
}
