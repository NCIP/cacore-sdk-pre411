package gov.nih.nci.cacoresdk.generator.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class DeployPropertiesManager {

	Properties deployProperties=null;

	public DeployPropertiesManager(String deployPropertiesFilePath) {
		super();

		deployProperties = new Properties();
		try {
			deployProperties.load(new FileInputStream(deployPropertiesFilePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getDeployPropertyValue(String propertyKey) {
		return deployProperties.getProperty(propertyKey);
	}
}
