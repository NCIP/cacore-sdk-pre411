package gov.nih.nci.cacoresdk.generator.portal;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.SplashScreen;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFrame;

import org.cagrid.grape.GridApplication;
import org.cagrid.grape.model.Application;

public final class SdkGeneratorPortal {

	private static SplashScreen sdkSplash;

	private static void showSdkSplash() {
		try {
			sdkSplash = new SplashScreen("/images/sdkSplashScreen.gif");
			// centers in screen
			sdkSplash.setLocationRelativeTo(null);
			sdkSplash.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void initialize() {

	}

	private static void showSdkGeneratorPortal(String confFile) {
		try {
			initialize();
			showSdkSplash();

			if (confFile == null) {
				confFile = "conf" + File.separator
						+ "sdk-4.1-generator-conf.xml";
			}

			Application app = (Application) Utils.deserializeDocument(confFile,
					Application.class);

			// launch the portal with the passed config
			GridApplication applicationInstance = GridApplication
					.getInstance(app);
			Dimension d = new Dimension(app.getDimensions().getWidth(), app
					.getDimensions().getHeight());

			try {
				applicationInstance.pack();
			} catch (Exception e) {
				applicationInstance.setIconImage(null);
				applicationInstance.pack();
			}
			applicationInstance.setSize(d);
			applicationInstance.setVisible(true);
			applicationInstance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final class SdkSplashCloser implements Runnable {
		public void run() {
			try {
				sdkSplash.dispose();
			} catch (Exception e) {

			}
		}
	}

	public static void main(String[] args) {
		if (args.length > 0) {
			showSdkGeneratorPortal(args[0]);
		} else {
			showSdkGeneratorPortal(null);
		}
		EventQueue.invokeLater(new SdkSplashCloser());
	}

}
