package gov.nih.nci.system.dao.orm;


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
			String[] filterNames = getFilterNames();
			if (filterNames != null) {
				for (int i = 0; i < filterNames.length; i++) {
					Filter filter = session.enableFilter(filterNames[i]);
					if (filter == null)
						continue;
					filterParameterSetter.setParameters(filter);
				}
			}
		}
		
		filterParameterSetter.performPostProcessing(getSessionFactory(), session);
	}
	
}
