package gov.nih.nci.cacoresdk.installer.portal.generation;

import gov.nih.nci.cacoresdk.installer.common.AntTools;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
//import gov.nih.nci.cagrid.introduce.common.AntTools;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
//import gov.nih.nci.cagrid.introduce.portal.modification.ModificationViewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.cagrid.grape.ApplicationComponent;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.model.RenderOptions;
import org.cagrid.grape.utils.BusyDialogRunnable;

/**implementation is provided, however, to create
 * specific look-feel or additions one should extend this panel and add this
 * componenet to the introduce portal configuration.
 * 
 * @author <A HREF="MAILTO:dumitrud@mail.nih.gov">Dan Dumitru</A>
 * @created November 10, 2008
 */
public abstract class GenerationViewerBaseComponent extends ApplicationComponent {

	/**
	 * Will call the generate application engine component to generate a caCORE
	 * like application using the supplied deploy settings and an SDK 4.1 or newer
	 * installation
	 * 
	 * @param sdkDirPath
	 *            the path to the SDK install home directory
	 * @param projectDirPath
	 *            the path to the project generation directory
	 * @param installerPropsMap
	 *            a map of the properties to be used during the 
	 *            project application generation
	 */

	public void generateApplication(final String sdkDirPath,final String projectDirPath,final Map<String,String> installerPropsMap) {
		int doIdeleteResult = JOptionPane.OK_OPTION;
		final File projectDir = new File(projectDirPath);

		if (projectDir.exists() && projectDir.list().length != 0) {
			doIdeleteResult = JOptionPane.NO_OPTION;
			File buildScript = new File(projectDir.getAbsolutePath() + File.separator
					+ "build.xml");
			File installerPropertiesFile = new File(projectDir.getAbsolutePath() + File.separator
					+ "installer.properties");
			if (buildScript.exists() && installerPropertiesFile.exists()) {
				doIdeleteResult = JOptionPane
				.showConfirmDialog(
						this,
						"The project generation directory ("
						+ projectDirPath
						+ ") is not empty.  All existing information in the directory will be overwritten.",
						"Confirm Overwrite", JOptionPane.YES_NO_OPTION);
			} else {
				JOptionPane
				.showMessageDialog(
						this,
						"The project generation directory ("
						+ projectDirPath
						+ ") is not empty, and does not appear to be a SDK installer generated directory."
						+ "  You must manually delete the directory, or specify a different directory.");
			}
		} 

		if (doIdeleteResult == JOptionPane.OK_OPTION) {
			final File sdkDirFile = new File(sdkDirPath);
			final File installerPropertiesFile = new File(projectDir.getAbsolutePath() + File.separator + "installer.properties");						

			GenerationViewerBaseComponent.this.setVisible(false);
			dispose();

			BusyDialogRunnable r = new BusyDialogRunnable(GridApplication.getContext().getApplication(), "Generating") {
				@Override
				public void process() {
					try {
						if (!sdkDirFile.exists()) {
							setErrorMessage("The specified SDK installtion home directory does not exist: "+sdkDirPath);
							return;
						}
						
						if (!projectDir.exists()){
							setProgressText("Creating and configuring the project generation directory: "+projectDir.getAbsolutePath());
							
							String cmd = AntTools.getAntConfigureProjectCommand(
									".", projectDir.getAbsolutePath(), false);
							Process p = CommonTools.createAndOutputProcess(cmd);
							p.waitFor();
							if (p.exitValue() != 0) {
								setErrorMessage("Error creating and configuring the project generation directory!  Please check the console output for more details.");
								return;
							}

							setProgressText("Project generation directory successfully configured");
						}

						setProgressText("Saving installer properties to "+installerPropertiesFile.getAbsolutePath());

						if (!saveInstallerProps(installerPropertiesFile,installerPropsMap)){
							setErrorMessage("IO error encountered saving installer properties to: "+installerPropertiesFile.getAbsolutePath());
							return;
						}

						setProgressText("Generating the application");

						String cmd = AntTools.getAntCommandCall(projectDirPath, false);
						System.out.println("cmd: "+cmd);
						System.out.println(System.getProperty("java.class.path"));
						Process p = CommonTools.createAndOutputProcess(cmd);
						p.waitFor();
						if (p.exitValue() != 0) {
							setErrorMessage("Error generating the application!  Please check the console output for more details.");
							return;
						}
						setProgressText("Application successfully generated");

					} catch (Exception ex) {
						ex.printStackTrace();
						setErrorMessage("Error: " + ex.getMessage());
						return;
					}
				}
			};

			Thread th = new Thread(r);
			th.start();
		}


	}
	
	private boolean saveInstallerProps(File installerPropsFile, Map<String,String>installerPropsMap){
		
		try {
			FileWriter installerPropsWriter = new FileWriter(installerPropsFile);
			
			if (installerPropsMap != null)
			{
				Iterator<String> keys = installerPropsMap.keySet().iterator();
				while (keys.hasNext())
				{
					String key = (String) keys.next();
					String value = (String) installerPropsMap.get(key);
					
					installerPropsWriter.write(key+"="+value+"\n");
				}
			}
			
			installerPropsWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

}
