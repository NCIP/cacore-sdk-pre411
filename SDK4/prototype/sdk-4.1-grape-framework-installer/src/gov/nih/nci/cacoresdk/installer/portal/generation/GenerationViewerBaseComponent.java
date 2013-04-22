/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk/LICENSE.txt for details.
 */

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
	 * 
	 * 
	 * @param dir
	 *            the path to the location to create the service
	 * @param service
	 *            the name of the service
	 * @param servicePackage
	 *            the package name to use for the created service
	 * @param serviceNamespace
	 *            the namespace to be used for this services wsdl and stubs
	 * @param extensions
	 *            a list of strings with the extension names of the extensions to
	 *            be added
	 */
	public void createService(final String dir, final String service,
			final String servicePackage, final String serviceNamespace,
			final List resourceOptions, final List extensions) {
//		int doIdeleteResult = JOptionPane.OK_OPTION;
//		final File dirFile = new File(dir);
//		if (dirFile.exists() && dirFile.list().length != 0) {
//			doIdeleteResult = JOptionPane.NO_OPTION;
////			File introduceXML = new File(dirFile.getAbsolutePath() + File.separator
////					+ IntroduceConstants.INTRODUCE_XML_FILE);
//			File introduceXML = new File(dirFile.getAbsolutePath() + File.separator
//					+ IntroduceConstants.INTRODUCE_XML_FILE);
//			if (introduceXML.exists()) {
//				doIdeleteResult = JOptionPane
//						.showConfirmDialog(
//								this,
//								"The creation directory ("
//										+ dirFile.getAbsolutePath()
//										+ ") is not empty.  All information in the directory will be lost.",
//								"Confirm Overwrite", JOptionPane.YES_NO_OPTION);
//			} else {
//				JOptionPane
//						.showMessageDialog(
//								this,
//								"The creation directory ("
//										+ dirFile.getAbsolutePath()
//										+ ") is not empty, and does not appear to be an Introduce-created service."
//										+ "  You must manually delete the directory, or specify a different directory.");
//			}
//		}
//
//		if (doIdeleteResult == JOptionPane.OK_OPTION) {
//
//			GenerationViewerBaseComponent.this.setVisible(false);
//			dispose();
//
//			BusyDialogRunnable r = new BusyDialogRunnable(GridApplication.getContext().getApplication(), "Creating") {
//				@Override
//				public void process() {
//					try {
//						String serviceName = service;
//						String dirName = dir;
//						String packageName = servicePackage;
//						String serviceNsDomain = serviceNamespace;
//
//						if (dirFile.exists()) {
//							setProgressText("deleting existing directory");
//							boolean deleted = Utils.deleteDir(dirFile);
//							if (!deleted) {
//								setErrorMessage("Unable to delete creation directory");
//								return;
//							}
//						}
//
//						setProgressText("purging old archives");
////						ResourceManager.purgeArchives(serviceName);
//
//						String serviceResourceOptions = "";
//						for (int i = 0; i < resourceOptions.size(); i++) {
//							serviceResourceOptions += (String) resourceOptions
//									.get(i);
//							if (i < resourceOptions.size() - 1) {
//								serviceResourceOptions += ",";
//							}
//						}
//
//						String serviceExtensions = "";
////						for (int i = 0; i < extensions.size(); i++) {
////							ServiceExtensionDescriptionType edt = ExtensionsLoader
////									.getInstance()
////									.getServiceExtension(
////											(String) extensions.get(i));
////							serviceExtensions += edt.getName();
////							if (i < extensions.size() - 1) {
////								serviceExtensions += ",";
////							}
////						}
//
//						setProgressText("creating service");
//
//						String cmd = AntTools.getAntSkeletonCreationCommand(
//								".", serviceName, dirName, packageName,
//								serviceNsDomain, serviceResourceOptions,
//								serviceExtensions);
//						Process p = CommonTools.createAndOutputProcess(cmd);
//						p.waitFor();
//						if (p.exitValue() != 0) {
//							setErrorMessage("Error creating new service!  Please check the console output for more details.");
//							return;
//						}
//
//						setProgressText("Invoking extension viewers...");
//						Properties properties = new Properties();
//						properties
//								.load(new FileInputStream(
//										dir
//												+ File.separator
//												+ IntroduceConstants.INTRODUCE_PROPERTIES_FILE));
//						ServiceDescription introService = (ServiceDescription) Utils
//								.deserializeDocument(
//										dir
//												+ File.separator
//												+ IntroduceConstants.INTRODUCE_XML_FILE,
//										ServiceDescription.class);
//						ServiceInformation info = new ServiceInformation(
//								introService, properties, new File(dir));
//
////						for (int i = 0; i < extensions.size(); i++) {
////							ServiceExtensionDescriptionType edt = ExtensionsLoader
////									.getInstance()
////									.getServiceExtension(
////											(String) extensions.get(i));
////							JDialog extDialog = gov.nih.nci.cagrid.introduce.portal.extension.tools.ExtensionTools
////									.getCreationUIDialog(GridApplication.getContext().getApplication(), edt
////											.getName(), info);
////							if (extDialog != null) {
////								GridApplication.getContext().centerDialog(extDialog);
////								extDialog.setVisible(true);
////							}
////						}
//
//						Utils.serializeDocument(dir + File.separator
//								+ IntroduceConstants.INTRODUCE_XML_FILE,
//								introService,
//								IntroduceConstants.INTRODUCE_SKELETON_QNAME);
//
//						setProgressText("invoking post creation processes");
//						cmd = AntTools.getAntSkeletonPostCreationCommand(".",
//								serviceName, dirName, packageName,
//								serviceNsDomain, serviceExtensions);
//						p = CommonTools.createAndOutputProcess(cmd);
//						p.waitFor();
//						if (p.exitValue() != 0) {
//							setErrorMessage("Error during service post creations!");
//							return;
//						}
//
//						setProgressText("Building created service...");
//						cmd = AntTools.getAntAllCommand(dirName);
//						p = CommonTools.createAndOutputProcess(cmd);
//						p.waitFor();
////						if (p.exitValue() == 0) {
////							setProgressText("creating archive");
////							info.createArchive();
////							setProgressText("launching modification viewer");
////							ModificationViewer modViewer = new ModificationViewer(
////									new File(dirName),this);
////							RenderOptions ro = new RenderOptions(false,true);
////							GridApplication.getContext().getApplication().addApplicationComponent(modViewer,null,ro);
////						} else {
////							setErrorMessage("Error creating new service!");
////							return;
////						}
//					} catch (Exception ex) {
//						ex.printStackTrace();
//						setErrorMessage("Error: " + ex.getMessage());
//						return;
//					}
//				}
//			};
//
//			Thread th = new Thread(r);
//			th.start();
//
//		}
	}
	
	public void generateApplication(final String sdkDir,final Map<String,String> installerPropsMap) {
		int doIdeleteResult = JOptionPane.OK_OPTION;
		final File sdkDirFile = new File(sdkDir);
		final File installerPropsFile = new File(sdkDir +"/" + "installer.properties");						

			GenerationViewerBaseComponent.this.setVisible(false);
			dispose();

			BusyDialogRunnable r = new BusyDialogRunnable(GridApplication.getContext().getApplication(), "Generating") {
				@Override
				public void process() {
					try {
						if (!sdkDirFile.exists()) {
								setErrorMessage("The specified SDK installtion home directory does not exist: "+sdkDir);
								return;
						}
						
						setProgressText("Cleaning previous build");
						
						String cmd = AntTools.getAntCommand("clean-all", sdkDir);
						System.out.println("cmd: "+cmd);
						System.out.println(System.getProperty("java.class.path"));
						Process p = CommonTools.createAndOutputProcess(cmd);
						p.waitFor();
						if (p.exitValue() != 0) {
							setErrorMessage("Error cleaning the previous build!  Please check the console output for more details.");
							return;
						}
						
						setProgressText("Saving installer properties to "+installerPropsFile.getAbsolutePath());
						
						if (!saveInstallerProps(installerPropsFile,installerPropsMap)){
							setErrorMessage("IO error encountered saving installer properties to: "+installerPropsFile.getAbsolutePath());
							return;
						}
						
						setProgressText("Generating the application");

//						String cmd = AntTools.getAntSkeletonCreationCommand(
//								".", serviceName, dirName, packageName,
//								serviceNsDomain, serviceResourceOptions,
//								serviceExtensions);
						cmd = AntTools.getAntCommandCall(sdkDir, false);
						System.out.println("cmd: "+cmd);
						System.out.println(System.getProperty("java.class.path"));
						p = CommonTools.createAndOutputProcess(cmd);
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
