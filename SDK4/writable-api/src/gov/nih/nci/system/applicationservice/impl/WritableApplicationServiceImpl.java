package gov.nih.nci.system.applicationservice.impl;

import gov.nih.nci.system.applicationservice.WritableApplicationService;
import gov.nih.nci.system.util.ClassCache;

import java.util.Properties;


public class WritableApplicationServiceImpl extends ApplicationServiceImpl implements WritableApplicationService
{
	public WritableApplicationServiceImpl(ClassCache classCache, Properties systemProperties) {
		super(classCache, systemProperties);
	}

}