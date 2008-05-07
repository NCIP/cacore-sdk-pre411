package gov.nih.nci.system.applicationservice;

import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.query.SDKQuery;

public interface WritableApplicationService extends ApplicationService
{
	public Object executeQuery(SDKQuery query) throws ApplicationException;
}