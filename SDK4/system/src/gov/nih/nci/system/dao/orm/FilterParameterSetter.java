package gov.nih.nci.system.dao.orm;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public interface FilterParameterSetter 
{
	public boolean getApplyFilters();
	
	public void setParameters(Filter filter);

	public void performPreProcessing(SessionFactory factory, Session session);

	public void performPostProcessing(SessionFactory factory, Session session);
}
