package gov.nih.nci.cacoresdk.generator.common;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public interface FileFilters extends gov.nih.nci.cagrid.introduce.common.FileFilters {

	public static final FileFilter XMI_UML_FILTER = new XMIandUMLFileFilter();
	
	public class XMIandUMLFileFilter extends javax.swing.filechooser.FileFilter implements java.io.FileFilter {
		public boolean accept(File file) {
			String filename = file.getName();
			return file.isDirectory() || filename.endsWith(".xmi") || filename.endsWith(".uml");
		}

		public String getDescription() {
			return "XML Metadata Interchange Files (*.xmi) and ARGO UML Model Files (*.uml)";
		}
	}

}
