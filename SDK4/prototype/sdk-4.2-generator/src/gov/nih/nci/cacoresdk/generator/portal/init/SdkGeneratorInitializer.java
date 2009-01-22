package gov.nih.nci.cacoresdk.generator.portal.init;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.cagrid.grape.ApplicationInitializer;

public class SdkGeneratorInitializer implements ApplicationInitializer {
	private static final int HELP_MENU = 4;
	private static final int CONFIG_MENU = 3;

	public void intialize() throws Exception {
		PropertyConfigurator.configure("." + File.separator + "conf"
				+ File.separator + "log4j.properties");

		setConfigurationOptions();
		prepareMenus();
	}

	private void setConfigurationOptions() {
		;
	}

	private void prepareMenus() {
		;
	}

}
