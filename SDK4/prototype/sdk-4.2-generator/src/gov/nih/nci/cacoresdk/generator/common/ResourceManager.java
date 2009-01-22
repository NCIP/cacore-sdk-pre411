package gov.nih.nci.cacoresdk.generator.common;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.cagrid.grape.GridApplication;

public class ResourceManager {

	public final static String STATE_FILE = "sdk.generator.state.properties";

	public final static String LAST_DIRECTORY = "sdk.generator.lastdir";

	public final static String LAST_FILE = "sdk.generator.lastfile";

	public static String promptDir(String defaultLocation, String dialogTitle) throws IOException {
		return promptDir(GridApplication.getContext().getApplication(), defaultLocation, dialogTitle);
	}

	public static String promptDir(Component owner, String defaultLocation, String dialogTitle) throws IOException {
		JFileChooser chooser = null;
		if ((defaultLocation != null) && (defaultLocation.length() > 0) && new File(defaultLocation).exists()) {
			chooser = new JFileChooser(new File(defaultLocation));
		} else if (getStateProperty(LAST_DIRECTORY) != null) {
			chooser = new JFileChooser(new File(getStateProperty(LAST_DIRECTORY)));
		} else {
			chooser = new JFileChooser();
		}
		chooser.setApproveButtonText("Open");
		chooser.setDialogTitle(dialogTitle);
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		GridApplication.getContext().centerComponent(chooser);

		int returnVal = chooser.showOpenDialog(owner);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			setStateProperty(ResourceManager.LAST_DIRECTORY, chooser.getSelectedFile().getAbsolutePath());
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	public static String promptFile(String defaultLocation, FileFilter filter) throws IOException {
		return promptFile(GridApplication.getContext().getApplication(), defaultLocation, filter);
	}

	public static String promptFile(Component owner, String defaultLocation, FileFilter filter) throws IOException {
		String[] files = internalPromptFiles(owner, defaultLocation, filter, false, "Select File");
		if (files != null) {
			return files[0];
		}
		return null;
	}

	private static String[] internalPromptFiles(Component owner, String defaultLocation, FileFilter filter,
			boolean multiSelect, String title) throws IOException {
		String[] fileNames = null;
		JFileChooser chooser = null;
		if ((defaultLocation != null) && (defaultLocation.length() != 0) && new File(defaultLocation).exists()) {
			chooser = new JFileChooser(new File(defaultLocation));
		} else if (getStateProperty(LAST_FILE) != null) {
			chooser = new JFileChooser(new File(getStateProperty(LAST_FILE)));
		} else {
			chooser = new JFileChooser();
		}
		chooser.setApproveButtonText("Open");
		chooser.setApproveButtonToolTipText("Open");
		chooser.setMultiSelectionEnabled(multiSelect);
		chooser.setDialogTitle(title);
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		GridApplication.getContext().centerComponent(chooser);

		int choice = chooser.showOpenDialog(owner);
		if (choice == JFileChooser.APPROVE_OPTION) {
			File[] files = null;
			if (multiSelect) {
				files = chooser.getSelectedFiles();
			} else {
				files = new File[]{chooser.getSelectedFile()};
			}
			setStateProperty(ResourceManager.LAST_FILE, files[0].getAbsolutePath());
			fileNames = new String[files.length];
			for (int i = 0; i < fileNames.length; i++) {
				fileNames[i] = files[i].getAbsolutePath();
			}
		}
		return fileNames;
	}

	public static String promptDir(String defaultLocation) throws IOException {
		return promptDir(GridApplication.getContext().getApplication(), defaultLocation);
	}


	public static String promptDir(Component owner, String defaultLocation) throws IOException {
		JFileChooser chooser = null;
		if ((defaultLocation != null) && (defaultLocation.length() > 0) && new File(defaultLocation).exists()) {
			chooser = new JFileChooser(new File(defaultLocation));
		} else if (getStateProperty(LAST_DIRECTORY) != null) {
			chooser = new JFileChooser(new File(getStateProperty(LAST_DIRECTORY)));
		} else {
			chooser = new JFileChooser();
		}
		chooser.setApproveButtonText("Open");
		chooser.setDialogTitle("Select Directory");
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		GridApplication.getContext().centerComponent(chooser);

		int returnVal = chooser.showOpenDialog(owner);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			setStateProperty(ResourceManager.LAST_DIRECTORY, chooser.getSelectedFile().getAbsolutePath());
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	public static String getStateProperty(String key) throws IOException {
		File lastDir = new File(getResourcePath() + File.separator + STATE_FILE);
		Properties properties = new Properties();
		if (!lastDir.exists()) {
			lastDir.createNewFile();
		}
		properties.load(new FileInputStream(lastDir));
		return properties.getProperty(key);
	}


	public static void setStateProperty(String key, String value) throws IOException {
		if (key != null) {
			File lastDir = new File(getResourcePath() + File.separator + STATE_FILE);
			if (!lastDir.exists()) {
				lastDir.createNewFile();
			}
			Properties properties = new Properties();
			properties.load(new FileInputStream(lastDir));
			properties.setProperty(key, value);
			properties.store(new FileOutputStream(lastDir), "");
		}
	}

	public static File getSdkGeneratorUserHome() {
		String userHome = System.getProperty("user.home");
		File userHomeF = new File(userHome);
		File sdkGeneratorCache = new File(userHomeF.getAbsolutePath() + File.separator + ".sdk.generator_"
				+ getSdkGeneratorVersion().replace(".", "_"));
		if (!sdkGeneratorCache.exists()) {
			sdkGeneratorCache.mkdirs();
		}
		return sdkGeneratorCache;
	}


	public static String getResourcePath() {
		File sdkGeneratorCache = getSdkGeneratorUserHome();
		sdkGeneratorCache.mkdir();
		return sdkGeneratorCache.getAbsolutePath();
	}

	public static String getSdkGeneratorVersion() {
		return getSdkGeneratorPropertyValue(SdkGeneratorConstants.SDK_GENERATOR_VERSION_PROPERTY);
	}

	public static String getSdkGeneratorPropertyValue(String propertyKey) {
		Properties engineProps = new Properties();
		try {
			engineProps.load(new FileInputStream(SdkGeneratorConstants.SDK_GENERATOR_ENGINE_PROPERTIES));
			return engineProps.getProperty(propertyKey);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
