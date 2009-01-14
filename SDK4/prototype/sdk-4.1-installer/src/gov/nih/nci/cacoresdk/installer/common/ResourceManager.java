package gov.nih.nci.cacoresdk.installer.common;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import org.cagrid.grape.GridApplication;

public class ResourceManager extends gov.nih.nci.cagrid.introduce.common.ResourceManager {
	
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
}
