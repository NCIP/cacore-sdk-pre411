package gov.nih.nci.cacoresdk.installer.portal.generation;

import gov.nih.nci.cacoresdk.installer.common.DeployPropertiesManager;
import gov.nih.nci.cacoresdk.installer.common.SdkInstallerLookAndFeel;
//import gov.nih.nci.cacoresdk.installer.common.IntroduceEnginePropertiesManager;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.validation.IconFeedbackPanel;
//import gov.nih.nci.cagrid.introduce.IntroduceConstants;
//import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
//import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;
//import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
//import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;
//import gov.nih.nci.cagrid.introduce.portal.modification.extensions.ExtensionsTable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.cagrid.grape.GridApplication;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.message.SimpleValidationMessage;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.util.ValidationUtils;
import com.jgoodies.validation.view.ValidationComponentUtils;


/**
 * GenerationViewer
 * 
 * @author <A HREF="MAILTO:dumitrud@mail.nih.gov">Dan Dumitru</A>
 * @created November 6, 2008
 */
public class GenerationViewer extends GenerationViewerBaseComponent {
	
	private static final long serialVersionUID = 1L;

    public static final String SCHEMA_DIR = "schema";

    public static final String SDK_INSTALL_DIR = "SDK installation directory";
    public static final String PROJECT_NAME = "Project name";
    public static final String NAMESPACE_PREFIX = "Project namespace prefix";
    public static final String WEBSERVICE_NAME = "Project Webservice name";
    public static final String MODEL_FILE_PATH = "Model file absolute path";
    
    public static final String DB_CONNECTION_URL = "Database Connection URL";
    public static final String DB_USERNAME = "Database Username";
    public static final String DB_PASSWORD = "Database Password";
    public static final String DB_DRIVER = "Database Driver";
    public static final String DB_DIALECT = "Database Dialect";

    private JPanel mainPanel = null;
    private JPanel buttonPanel = null;
    
    // Buttons
    private JButton generateButton = null;
    private JButton sdkInstallDirButton = null;
    private JButton modelFilePathButton = null;
    private JButton closeButton = null;
    private JButton addExtensionButton = null;
    private JButton removeExtensionButton = null;

    private JComboBox serviceStyleSeletor = null;

    private JPanel extensionsPanel = null;

    private JScrollPane extensionsScrollPane = null;

//    private ExtensionsTable extensionsTable = null;

    private JPanel extensionsTablePanel = null;

    private JLabel upExtensionLabel = null;

    private JLabel downExtensionLabel = null;

    private JPanel extSelectionPanel = null;

    private JPanel serviceStylePanel = null;

    private ValidationResultModel validationModel = new DefaultValidationResultModel();

	private JTabbedPane mainTabbedPane = null;

	private JPanel resourceOptionsConfigPanel = null;

	private JCheckBox lifetimeResource = null;

	private JCheckBox persistantResource = null;

	private JCheckBox notificationResource = null;

	private JCheckBox secureResource = null;

	private JCheckBox resourceProperty = null;
	
	/*
	 * caCore SDK Panel definitions
	 */

	// Primary
	private JPanel projectSettingsPanel = null;
	private JPanel modelSettingsPanel = null;
	private JPanel codeGenSettingsPanel = null;
	private JPanel dbConnectionSettingsPanel = null;
	
	// Writable API
	private JPanel writableApiSettingsPanel = null;
	//private JPanel loggingSettingsPanel = null;
	
	// Security
	private JPanel securitySettingsPanel = null;
	private JPanel csmDbConnectionSettingsPanel = null;
	private JPanel caGridAuthSettingsPanel = null;

	// App Server
	private JPanel appServerSettingsPanel = null;

	// Advanced
	private JPanel advancedSettingsPanel = null;
	
	/*	
	 * caCore SDK Component definitions
	 */
    
    // Project Settings Panel Label Definitions
    private JLabel sdkInstallDirLabel = null;
    private JLabel projectNameLabel = null;
    private JLabel namespacePrefixLabel = null;
    private JLabel webserviceNameLabel = null;
    
	//Project Settings Panel Component Definitions
	private JTextField sdkInstallDirField = null;
    private JTextField projectNameField = null;
	private JTextField namespacePrefixField = null;
    private JTextField webserviceNameField = null;
    private boolean hasUserModifiedWebserviceName=false;
    
    //Model Settings Panel Label Definitions
    private JLabel modelFileLabel = null;
    private JLabel modelFileTypeLabel = null;
    private JLabel logicalModelLabel = null;
    private JLabel dataModelLabel = null;
    private JLabel includePackageLabel = null;
    private JLabel excludePackageLabel = null;
    private JLabel excludeNameLabel = null;
    private JLabel excludeNamespaceLabel = null;
    
    //Model Settings Panel Component Definitions
    private JTextField modelFileField = null;
    private JComboBox  modelFileTypeComboBox = null;
    private JTextField logicalModelField = null;
    private JTextField dataModelField = null;
    private JTextField includePackageField = null;
    private JTextField excludePackageField = null;
    private JTextField excludeNameField = null;
    private JTextField excludeNamespaceField = null;
    
    //Code Generation Settings Panel Label Definitions
    private JLabel validateLogicalModelLabel = null;
    private JLabel validateModelMappingLabel = null;
    private JLabel validateGmeTagsLabel = null;
    private JLabel generateHibernateMappingLabel = null;
    private JLabel generateBeansLabel = null;
    private JLabel generateCastorMappingLabel = null;
    private JLabel generateXsdLabel = null;
    private JLabel generateXsdWithGmeTagsLabel = null;
    private JLabel generateXsdWithPermissibleValuesLabel = null;
    private JLabel generateWsddLabel = null;
    private JLabel generateHibernateValidatorLabel = null;
    
	//Code Generation Settings Panel Component Definitions
    private JCheckBox validateLogicalModelCheckBox = null;
    private JCheckBox validateModelMappingCheckBox = null;
    private JCheckBox validateGmeTagsCheckBox = null;
    private JCheckBox generateHibernateMappingCheckBox = null;
    private JCheckBox generateBeansCheckBox = null;
    private JCheckBox generateCastorMappingCheckBox = null;
    private JCheckBox generateXsdCheckBox = null;
    private JCheckBox generateXsdWithGmeTagsCheckBox = null;
    private JCheckBox generateXsdWithPermissibleValuesCheckBox = null;
    private JCheckBox generateWsddCheckBox = null;
    private JCheckBox generateHibernateValidatorCheckBox = null;
    
    //DB Connection Settings Panel Label Definitions
    private JLabel useJndiBasedConnectionLabel = null;
    private JLabel dbJndiUrlLabel = null;
    private JLabel dbConnectionUrlLabel = null;
    private JLabel dbUsernameLabel = null;
    private JLabel dbPasswordLabel = null;
    private JLabel dbDriverLabel = null;
    private JLabel dbDialectLabel = null;
    
	//DB Connection Settings Panel Component Definitions

    private JCheckBox  useJndiBasedConnectionCheckBox = null;
    private JTextField dbJndiUrlField = null;
    private JTextField dbConnectionUrlField = null;
    private JTextField dbUsernameField = null;
    private JTextField dbPasswordField = null;
    private JTextField dbDriverField = null;
    private JTextField dbDialectField = null;
    
    //Writable API Settings Panel Label Definitions
    private JLabel enableWritableApiExtensionLabel = null;
    private JLabel databaseTypeLabel = null;
    private JLabel identityGeneratorTagLabel = null;
    private JLabel caDsrConnectionUrlLabel = null;
    private JLabel enableCommonLoggingModuleLabel = null;
    private JLabel clmProjectNameLabel = null;
    private JLabel clmDbConnectionUrlLabel = null;
    private JLabel clmDbUsernameLabel = null;
    private JLabel clmDbPasswordLabel = null;
    private JLabel clmDbDriverLabel = null;
    
	//Writable API Settings Panel Component Definitions
    private JCheckBox  enableWritableApiExtensionCheckBox = null;
    private JTextField databaseTypeField = null;
    private JTextField identityGeneratorTagField = null;
    private JTextField caDsrConnectionUrlField = null;
    private JCheckBox  enableCommonLoggingModuleCheckBox = null;
    private JTextField clmProjectName = null;
    private JTextField clmDbConnectionUrlField = null;
    private JTextField clmDbUsernameField = null;
    private JTextField clmDbPasswordField = null;
    private JTextField clmDbDriverField = null;
    
    //TODO
    
    public GenerationViewer() {
        super();
        initialize();
    }


    /**
     * This method initializes this Viewer
     */
    private void initialize() {
        this.setContentPane(getMainPanel());
        this.setFrameIcon(SdkInstallerLookAndFeel.getGenerateApplicationIcon());
        this.setTitle("Generate an SDK Application");

        initValidation();
    }


    private void initValidation() {
        ValidationComponentUtils.setMessageKey(getSdkInstallDirField(), SDK_INSTALL_DIR);
        ValidationComponentUtils.setMessageKey(getProjectNameField(), PROJECT_NAME);
        ValidationComponentUtils.setMessageKey(getNamespacePrefixField(), NAMESPACE_PREFIX);
        ValidationComponentUtils.setMessageKey(getWebServiceNameField(), WEBSERVICE_NAME);
        
        ValidationComponentUtils.setMessageKey(getModelFileField(), MODEL_FILE_PATH);
        ValidationComponentUtils.setMandatory(getModelFileField(), true);
        
        ValidationComponentUtils.setMessageKey(getDbConnectionUrlField(), DB_CONNECTION_URL);
        ValidationComponentUtils.setMessageKey(getDbUsernameField(), DB_USERNAME);
        ValidationComponentUtils.setMessageKey(getDbPasswordField(), DB_PASSWORD);
        ValidationComponentUtils.setMessageKey(getDbDriverField(), DB_DRIVER);
        ValidationComponentUtils.setMessageKey(getDbDialectField(), DB_DIALECT);

        //TODO - Add Writable API components
        validateInput();
        
        toggleDbConnectionFields();
        toggleWritableApiFields();
        
        updateComponentTreeSeverity();
    }


    private final class FocusChangeHandler implements FocusListener {

        public void focusGained(FocusEvent e) {
            update();

        }


        public void focusLost(FocusEvent e) {
            update();
        }


        private void update() {
            //updateModel();
            validateInput();
        }
    }


    private void validateInput() {

        ValidationResult result = new ValidationResult();

        if (!ValidationUtils.isNotBlank(this.getSdkInstallDirField().getText())) {
            result.add(new SimpleValidationMessage(SDK_INSTALL_DIR + " must not be blank.", Severity.ERROR, SDK_INSTALL_DIR));
        } else {
            File file = new File(this.getSdkInstallDirField().getText());
            if(!file.exists()){
                result.add(new SimpleValidationMessage(SDK_INSTALL_DIR + " does not exist.  Please select the Home directory of an existing SDK Installation.", Severity.ERROR, SDK_INSTALL_DIR));
            }
        }
        
        if (!ValidationUtils.isNotBlank(this.getModelFileField().getText())) {
            result.add(new SimpleValidationMessage(MODEL_FILE_PATH + " must not be blank.", Severity.ERROR, MODEL_FILE_PATH));
        } else {
            File file = new File(this.getModelFileField().getText());
            if(!file.exists()){
                result.add(new SimpleValidationMessage(MODEL_FILE_PATH + " does not exist.  Please select an existing Model file.", Severity.ERROR, MODEL_FILE_PATH));
            }
        }
        	
        //DB Connection Setting Validation
        if (!getUseJndiBasedConnectionCheckBox().isSelected()){

        	if (!ValidationUtils.isNotBlank(this.getDbConnectionUrlField().getText())) {
        		result.add(new SimpleValidationMessage(DB_CONNECTION_URL + " must not be blank.", Severity.ERROR, DB_CONNECTION_URL));
        	}
        	
        	if (!ValidationUtils.isNotBlank(this.getDbUsernameField().getText())) {
        		result.add(new SimpleValidationMessage(DB_USERNAME + " must not be blank.", Severity.ERROR, DB_USERNAME));
        	} 
        	
        	if (!ValidationUtils.isNotBlank(this.getDbPasswordField().getText())) {
        		result.add(new SimpleValidationMessage(DB_PASSWORD + " must not be blank.", Severity.ERROR, DB_PASSWORD));
        	} 
        	
        	if (!ValidationUtils.isNotBlank(this.getDbDriverField().getText())) {
        		result.add(new SimpleValidationMessage(DB_DRIVER + " must not be blank.", Severity.ERROR, DB_DRIVER));
        	} 
        	
        	if (!ValidationUtils.isNotBlank(this.getDbDialectField().getText())) {
        		result.add(new SimpleValidationMessage(DB_DIALECT + " must not be blank.", Severity.ERROR, DB_DIALECT));
        	}
        }

        this.validationModel.setResult(result);

        updateComponentTreeSeverity();
        updateGenerationButton();
    }


    private void updateComponentTreeSeverity() {
        ValidationComponentUtils.updateComponentTreeMandatoryAndBlankBackground(this);
        ValidationComponentUtils.updateComponentTreeSeverityBackground(this, this.validationModel.getResult());
    }


    private void updateGenerationButton() {
        if (this.validationModel.hasErrors()) {
            this.getGenerateButton().setEnabled(false);
        } else {
            this.getGenerateButton().setEnabled(true);
        }
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            GridBagConstraints gridBagConstraints111 = new GridBagConstraints();
            gridBagConstraints111.fill = GridBagConstraints.BOTH;
            gridBagConstraints111.weighty = 1.0;
            gridBagConstraints111.weightx = 1.0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 2;
            gridBagConstraints1.anchor = java.awt.GridBagConstraints.SOUTH;
            gridBagConstraints1.gridheight = 1;
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.add(getButtonPanel(), gridBagConstraints1);
            mainPanel.add(getMainTabbedPane(), gridBagConstraints111);
        }
        return mainPanel;
    }


    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            buttonPanel.add(getGenerateButton(), null);
            buttonPanel.add(getCloseButton(), null);
        }
        return buttonPanel;
    }


    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getGenerateButton() {
        if (generateButton == null) {
            generateButton = new JButton();
            generateButton.setText("Generate");
            generateButton.setIcon(SdkInstallerLookAndFeel.getGenerateApplicationIcon());
            generateButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	
                    generateApplication(getSdkInstallDirField().getText(),getInstallerPropsMap()); 
                
                }
            });
        }

        return generateButton;
    }


    /**
     * This method initializes serviceStyleSeletor
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getServiceStyleSeletor() {
        if (serviceStyleSeletor == null) {
            serviceStyleSeletor = new JComboBox();
            serviceStyleSeletor.addItem("NONE");

//            List extensionDescriptors = ExtensionsLoader.getInstance().getServiceExtensions();
//            for (int i = 0; i < extensionDescriptors.size(); i++) {
//                ServiceExtensionDescriptionType ex = (ServiceExtensionDescriptionType) extensionDescriptors.get(i);
//                serviceStyleSeletor.addItem(ex.getDisplayName());
//            }
        }
        return serviceStyleSeletor;
    }


    /**
     * This method initializes projectNameField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getProjectNameField() {
        if (projectNameField == null) {
            projectNameField = new JTextField();
            projectNameField.setText(DeployPropertiesManager.getDeployPropertyValue("PROJECT_NAME"));
            projectNameField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    updateSuggestedWebserviceName();
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    updateSuggestedWebserviceName();
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    updateSuggestedWebserviceName();
                    validateInput();
                }
            });
            projectNameField.addFocusListener(new FocusChangeHandler());
        }
        return projectNameField;
    }


    protected void updateSuggestedWebserviceName() {
    	if (!hasUserModifiedWebserviceName)
    		getWebServiceNameField().setText(getProjectNameField().getText()+"Service");
    }
    

    protected void toggleDbConnectionFields() {
		if (useJndiBasedConnectionCheckBox.isSelected()){
			dbJndiUrlField.setEnabled(true);
			
			dbConnectionUrlField.setEnabled(false);
			dbUsernameField.setEnabled(false);
			dbPasswordField.setEnabled(false);
			dbDriverField.setEnabled(false);
			dbDialectField.setEnabled(false);
		} else{
			dbJndiUrlField.setEnabled(false);
			
			dbConnectionUrlField.setEnabled(true);
			dbUsernameField.setEnabled(true);
			dbPasswordField.setEnabled(true);
			dbDriverField.setEnabled(true);
			dbDialectField.setEnabled(true);
		}
    }
    
    protected void toggleWritableApiFields() {
		if (enableWritableApiExtensionCheckBox.isSelected()){
			databaseTypeField.setEnabled(true);
			identityGeneratorTagField.setEnabled(true);
			caDsrConnectionUrlField.setEnabled(true);
			
			enableCommonLoggingModuleCheckBox.setEnabled(true);
			
			if (enableCommonLoggingModuleCheckBox.isSelected()){
				clmProjectName.setEnabled(true);
				clmDbConnectionUrlField.setEnabled(true);
				clmDbUsernameField.setEnabled(true);
				clmDbPasswordField.setEnabled(true);
				clmDbDriverField.setEnabled(true);
			} else {
				clmProjectName.setEnabled(false);
				clmDbConnectionUrlField.setEnabled(false);
				clmDbUsernameField.setEnabled(false);
				clmDbPasswordField.setEnabled(false);
				clmDbDriverField.setEnabled(false);
			}
		} else{
			databaseTypeField.setEnabled(false);
			identityGeneratorTagField.setEnabled(false);
			caDsrConnectionUrlField.setEnabled(false);
			
			enableCommonLoggingModuleCheckBox.setEnabled(false);
			clmProjectName.setEnabled(false);
			clmDbConnectionUrlField.setEnabled(false);
			clmDbUsernameField.setEnabled(false);
			clmDbPasswordField.setEnabled(false);
			clmDbDriverField.setEnabled(false);
		}
    }

    /**
     * This method initializes jTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getSdkInstallDirField() {
        if (sdkInstallDirField == null) {
            sdkInstallDirField = new JTextField();
//            sdkInstallDirField.setText(home + File.separator
//                + IntroduceEnginePropertiesManager.getIntroducePropertyValue(IntroduceConstants.DEFAULT_SERVICE_NAME));
            sdkInstallDirField.setText("");
            sdkInstallDirField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }


                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
            sdkInstallDirField.addFocusListener(new FocusChangeHandler());
        }
        return sdkInstallDirField;
    }


    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getSdkInstallDirButton() {
        if (sdkInstallDirButton == null) {
            sdkInstallDirButton = new JButton();
            sdkInstallDirButton.setText("Browse");
            sdkInstallDirButton.setIcon(SdkInstallerLookAndFeel.getBrowseIcon());
            sdkInstallDirButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        String previous = getSdkInstallDirField().getText();
                        String location = ResourceManager.promptDir(previous);
                        if (location != null && location.length() > 0) {
                            getSdkInstallDirField().setText(location);
                        } else {
                            getSdkInstallDirField().setText(previous);
                        }
                        validateInput();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        return sdkInstallDirButton;
    }
    
    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getModelFilePathButton() {
        if (modelFilePathButton == null) {
        	modelFilePathButton = new JButton();
        	modelFilePathButton.setText("Browse");
        	modelFilePathButton.setIcon(SdkInstallerLookAndFeel.getBrowseIcon());
        	modelFilePathButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        String previous = getModelFileField().getText();
                        String location = ResourceManager.promptFile(previous, FileFilters.XMI_FILTER);
                        if (location != null && location.length() > 0) {
                        	getModelFileField().setText(location);
                        } else {
                        	getModelFileField().setText(previous);
                        }
                        validateInput();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        return modelFilePathButton;
    }


    /**
     * This method initializes Project Namespace Prefix Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getNamespacePrefixField() {
        if (namespacePrefixField == null) {
            namespacePrefixField = new JTextField();
            namespacePrefixField.setText(DeployPropertiesManager.getDeployPropertyValue("NAMESPACE_PREFIX"));
            namespacePrefixField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
            namespacePrefixField.addFocusListener(new FocusChangeHandler());
        }
        return namespacePrefixField;
    }


    /**
     * This method initializes webserviceNameField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getWebServiceNameField() {
        if (webserviceNameField == null) {
            webserviceNameField = new JTextField();
            //webserviceNameField.setText(DeployPropertiesManager.getDeployPropertyValue("WEBSERVICE_NAME"));
            webserviceNameField.setText(DeployPropertiesManager.getDeployPropertyValue("PROJECT_NAME")+"Service");
            webserviceNameField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) { 
                	if (isFocusOwner())
                		hasUserModifiedWebserviceName=true;
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                	if (isFocusOwner())
                		hasUserModifiedWebserviceName=true;
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                	if (isFocusOwner())
                		hasUserModifiedWebserviceName=true;
                    validateInput();
                }
            });
            webserviceNameField.addFocusListener(new FocusChangeHandler());
        }
        return webserviceNameField;
    }
    
    /**
     * This method initializes Model File Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getModelFileField() {
        if (modelFileField == null) {
        	modelFileField = new JTextField();
        	//modelFileField.setText(DeployPropertiesManager.getDeployPropertyValue("MODEL_FILE"));
        	modelFileField.setText("");
        	modelFileField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	modelFileField.addFocusListener(new FocusChangeHandler());
        }
        return modelFileField;
    }
    
    /**
     * This method initializes Model File Type Field
     * 
     * @return javax.swing.JTextField
     */
    private JComboBox getModelFileTypeField() {
        if (modelFileTypeComboBox == null) {
        	modelFileTypeComboBox = new JComboBox();
        	modelFileTypeComboBox.addItem("EA");
        	modelFileTypeComboBox.addItem("ARGO");

            String modelFileType = DeployPropertiesManager.getDeployPropertyValue("MODEL_FILE_TYPE");
            modelFileTypeComboBox.setSelectedItem(modelFileType);

//        	modelFileTypeComboBox.getDocument().addDocumentListener(new DocumentListener() {
//                public void changedUpdate(DocumentEvent e) {
//                    validateInput();
//                }
//
//                public void removeUpdate(DocumentEvent e) {
//                    validateInput();
//                }
//
//                public void insertUpdate(DocumentEvent e) {
//                    validateInput();
//                }
//            });
        	modelFileTypeComboBox.addFocusListener(new FocusChangeHandler());
        }
        return modelFileTypeComboBox;
    }
    
    /**
     * This method initializes Logical Model Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getLogicalModelField() {
        if (logicalModelField == null) {
        	logicalModelField = new JTextField();
        	logicalModelField.setText(DeployPropertiesManager.getDeployPropertyValue("LOGICAL_MODEL"));
        	logicalModelField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	logicalModelField.addFocusListener(new FocusChangeHandler());
        }
        return logicalModelField;
    }
    
    /**
     * This method initializes Data Model Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getDataModelField() {
        if (dataModelField == null) {
        	dataModelField = new JTextField();
        	dataModelField.setText(DeployPropertiesManager.getDeployPropertyValue("DATA_MODEL"));
        	dataModelField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	dataModelField.addFocusListener(new FocusChangeHandler());
        }
        return dataModelField;
    }
    
    /**
     * This method initializes the Include Package Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getIncludePackageField() {
        if (includePackageField == null) {
        	includePackageField = new JTextField();
        	includePackageField.setText(DeployPropertiesManager.getDeployPropertyValue("INCLUDE_PACKAGE"));
        	includePackageField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	includePackageField.addFocusListener(new FocusChangeHandler());
        }
        return includePackageField;
    }
    
    /**
     * This method initializes the Exclude Package Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getExcludePackageField() {
        if (excludePackageField == null) {
        	excludePackageField = new JTextField();
        	excludePackageField.setText(DeployPropertiesManager.getDeployPropertyValue("EXCLUDE_PACKAGE"));
        	excludePackageField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	excludePackageField.addFocusListener(new FocusChangeHandler());
        }
        return excludePackageField;
    }
    
    /**
     * This method initializes the Exclude Name Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getExcludeNameField() {
        if (excludeNameField == null) {
        	excludeNameField = new JTextField();
        	excludeNameField.setText(DeployPropertiesManager.getDeployPropertyValue("EXCLUDE_NAME"));
        	excludeNameField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	excludeNameField.addFocusListener(new FocusChangeHandler());
        }
        return excludeNameField;
    }

    /**
     * This method initializes the Exclude Namespace Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getExcludeNamespaceField() {
        if (excludeNamespaceField == null) {
        	excludeNamespaceField = new JTextField();
        	excludeNamespaceField.setText(DeployPropertiesManager.getDeployPropertyValue("EXCLUDE_NAMESPACE"));
        	excludeNamespaceField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	excludeNamespaceField.addFocusListener(new FocusChangeHandler());
        }
        return excludeNamespaceField;
    }
    
    /**
     * This method initializes the Validate Logical Model Check Box
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getValidateLogicalModelCheckBox() {
        if (validateLogicalModelCheckBox == null) {
        	validateLogicalModelCheckBox = new JCheckBox();
        	validateLogicalModelCheckBox.setToolTipText("Validate the Logical Model prior to generating the application?");
        	validateLogicalModelCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	validateLogicalModelCheckBox.setSelected(Boolean.parseBoolean(DeployPropertiesManager.getDeployPropertyValue("VALIDATE_LOGICAL_MODEL")));
        	validateLogicalModelCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	validateLogicalModelCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//e.g.: checkResourcePropertyOptions();
				}
        	});

        	validateLogicalModelCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return validateLogicalModelCheckBox;
    }
    
    /**
     * This method initializes the Validate Model Mapping Check Box
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getValidateModelMappingCheckBox() {
        if (validateModelMappingCheckBox == null) {
        	validateModelMappingCheckBox = new JCheckBox();
        	validateModelMappingCheckBox.setToolTipText("Validate the Model Mapping prior to generating the application?");
        	validateModelMappingCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	validateModelMappingCheckBox.setSelected(Boolean.parseBoolean(DeployPropertiesManager.getDeployPropertyValue("VALIDATE_MODEL_MAPPING")));
        	validateModelMappingCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	validateModelMappingCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					;//e.g.: checkResourcePropertyOptions();
				}
        	});

        	validateModelMappingCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return validateModelMappingCheckBox;
    }
    
    /**
     * This method initializes the Validate Model Mapping Check Box
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getValidateGmeTagsCheckBox() {
        if (validateGmeTagsCheckBox == null) {
        	validateGmeTagsCheckBox = new JCheckBox();
        	validateGmeTagsCheckBox.setToolTipText("Validate GME tags in the logical model prior to generating the application?");
        	validateGmeTagsCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	validateGmeTagsCheckBox.setSelected(Boolean.parseBoolean(DeployPropertiesManager.getDeployPropertyValue("VALIDATE_GME_TAGS")));
        	validateGmeTagsCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	validateGmeTagsCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					;//e.g.: checkResourcePropertyOptions();
				}
        	});

        	validateGmeTagsCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return validateGmeTagsCheckBox;
    } 
    
    /**
     * This method initializes the Generate Beans Check Box
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getGenerateBeansCheckBox() {
        if (generateBeansCheckBox == null) {
        	generateBeansCheckBox = new JCheckBox();
        	generateBeansCheckBox.setToolTipText("Generate domain Java Beans?");
        	generateBeansCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	generateBeansCheckBox.setSelected(Boolean.parseBoolean(DeployPropertiesManager.getDeployPropertyValue("GENERATE_BEANS")));
        	generateBeansCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	generateBeansCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					;//e.g.: checkResourcePropertyOptions();
				}
        	});

        	generateBeansCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return generateBeansCheckBox;
    } 
    
    /**
     * This method initializes the Generate Hibernate Mapping File Check Box
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getGenerateHibernateMappingCheckBox() {
        if (generateHibernateMappingCheckBox == null) {
        	generateHibernateMappingCheckBox = new JCheckBox();
        	generateHibernateMappingCheckBox.setToolTipText("Generate Hibernate mapping files?");
        	generateHibernateMappingCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	generateHibernateMappingCheckBox.setSelected(Boolean.parseBoolean(DeployPropertiesManager.getDeployPropertyValue("GENERATE_HIBERNATE_MAPPING")));
        	generateHibernateMappingCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	generateHibernateMappingCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					;//e.g.: checkResourcePropertyOptions();
				}
        	});

        	generateHibernateMappingCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return generateHibernateMappingCheckBox;
    } 
    
    /**
     * This method initializes the Generate Castor Mapping File Check Box
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getGenerateCastorMappingCheckBox() {
        if (generateCastorMappingCheckBox == null) {
        	generateCastorMappingCheckBox = new JCheckBox();
        	generateCastorMappingCheckBox.setToolTipText("Generate Castor mapping files?");
        	generateCastorMappingCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	generateCastorMappingCheckBox.setSelected(Boolean.parseBoolean(DeployPropertiesManager.getDeployPropertyValue("GENERATE_CASTOR_MAPPING")));
        	generateCastorMappingCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	generateCastorMappingCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					;//e.g.: checkResourcePropertyOptions();
				}
        	});

        	generateCastorMappingCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return generateCastorMappingCheckBox;
    }
    
    /**
     * This method initializes the Generate XSD Schema Files Check Box
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getGenerateXsdCheckBox() {
        if (generateXsdCheckBox == null) {
        	generateXsdCheckBox = new JCheckBox();
        	generateXsdCheckBox.setToolTipText("Generate Castor mapping files?");
        	generateXsdCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	generateXsdCheckBox.setSelected(Boolean.parseBoolean(DeployPropertiesManager.getDeployPropertyValue("GENERATE_XSD")));
        	generateXsdCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	generateXsdCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					;//e.g.: checkResourcePropertyOptions();
				}
        	});

        	generateXsdCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return generateXsdCheckBox;
    }
    
    /**
     * This method initializes the Generate XSD Files Check Box
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox generateXsdWithGmeTagsCheckBox() {
        if (generateXsdWithGmeTagsCheckBox == null) {
        	generateXsdWithGmeTagsCheckBox = new JCheckBox();
        	generateXsdWithGmeTagsCheckBox.setToolTipText("Generate XSD files?");
        	generateXsdWithGmeTagsCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	generateXsdWithGmeTagsCheckBox.setSelected(Boolean.parseBoolean(DeployPropertiesManager.getDeployPropertyValue("GENERATE_XSD_WITH_GME_TAGS")));
        	generateXsdWithGmeTagsCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	generateXsdWithGmeTagsCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					;//e.g.: checkResourcePropertyOptions();
				}
        	});

        	generateXsdWithGmeTagsCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return generateXsdWithGmeTagsCheckBox;
    }
    
    /**
     * This method initializes the Generate XSD with Permissible Values Check Box
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getGenerateXsdWithPermissibleValuesCheckBox() {
        if (generateXsdWithPermissibleValuesCheckBox == null) {
        	generateXsdWithPermissibleValuesCheckBox = new JCheckBox();
        	generateXsdWithPermissibleValuesCheckBox.setToolTipText("Generate XSD files using Permissible Values?");
        	generateXsdWithPermissibleValuesCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	generateXsdWithPermissibleValuesCheckBox.setSelected(Boolean.parseBoolean(DeployPropertiesManager.getDeployPropertyValue("GENERATE_XSD_WITH_PERMISSIBLE_VALUES")));
        	generateXsdWithPermissibleValuesCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	generateXsdWithPermissibleValuesCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					;//e.g.: checkResourcePropertyOptions();
				}
        	});

        	generateXsdWithPermissibleValuesCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return generateXsdWithPermissibleValuesCheckBox;
    }
    
    /**
     * This method initializes the Generate XSD with Permissible Values Check Box
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getGenerateWsddLabelCheckBox() {
        if (generateWsddCheckBox == null) {
        	generateWsddCheckBox = new JCheckBox();
        	generateWsddCheckBox.setToolTipText("Generate XSD files?");
        	generateWsddCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	generateWsddCheckBox.setSelected(Boolean.parseBoolean(DeployPropertiesManager.getDeployPropertyValue("GENERATE_WSDD")));
        	generateWsddCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	generateWsddCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					;//e.g.: checkResourcePropertyOptions();
				}
        	});

        	generateWsddCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return generateWsddCheckBox;
    }
    
    /**
     * This method initializes the Generate XSD with Permissible Values Check Box
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getGenerateHibernateValidatorCheckBox() {
        if (generateHibernateValidatorCheckBox == null) {
        	generateHibernateValidatorCheckBox = new JCheckBox();
        	generateHibernateValidatorCheckBox.setToolTipText("Generate Hibernate Validator?");
        	generateHibernateValidatorCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	generateHibernateValidatorCheckBox.setSelected(Boolean.parseBoolean(DeployPropertiesManager.getDeployPropertyValue("GENERATE_HIBERNATE_VALIDATOR")));
        	generateHibernateValidatorCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	generateHibernateValidatorCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					;//e.g.: checkResourcePropertyOptions();
				}
        	});

        	generateHibernateValidatorCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return generateHibernateValidatorCheckBox;
    }
    
    /**
     * This method initializes the Use JNDI Based Connection Check Box
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getUseJndiBasedConnectionCheckBox() {
        if (useJndiBasedConnectionCheckBox == null) {
        	useJndiBasedConnectionCheckBox = new JCheckBox();
        	useJndiBasedConnectionCheckBox.setToolTipText("Use a JNDI-based Databse Connection?");
        	useJndiBasedConnectionCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	useJndiBasedConnectionCheckBox.setSelected(Boolean.parseBoolean(DeployPropertiesManager.getDeployPropertyValue("USE_JNDI_BASED_CONNECTION")));
        	useJndiBasedConnectionCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	useJndiBasedConnectionCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					toggleDbConnectionFields();
				}
        	});

        	useJndiBasedConnectionCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return useJndiBasedConnectionCheckBox;
    }  
    
    /**
     * This method initializes the Database JNDI URL Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getDbJndiUrlField() {
        if (dbJndiUrlField == null) {
        	dbJndiUrlField = new JTextField();
        	dbJndiUrlField.setText(DeployPropertiesManager.getDeployPropertyValue("DB_JNDI_URL"));
        	dbJndiUrlField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	dbJndiUrlField.addFocusListener(new FocusChangeHandler());
        }
        return dbJndiUrlField;
    }
    
    /**
     * This method initializes the Database Connection URL Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getDbConnectionUrlField() {
        if (dbConnectionUrlField == null) {
        	dbConnectionUrlField = new JTextField();
        	dbConnectionUrlField.setText(DeployPropertiesManager.getDeployPropertyValue("DB_CONNECTION_URL"));
        	dbConnectionUrlField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	dbConnectionUrlField.addFocusListener(new FocusChangeHandler());
        }
        return dbConnectionUrlField;
    }
    
    /**
     * This method initializes the Database Username Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getDbUsernameField() {
        if (dbUsernameField == null) {
        	dbUsernameField = new JTextField();
        	dbUsernameField.setText(DeployPropertiesManager.getDeployPropertyValue("DB_USERNAME"));
        	dbUsernameField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	dbUsernameField.addFocusListener(new FocusChangeHandler());
        }
        return dbUsernameField;
    }
    
    /**
     * This method initializes the Database Password Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getDbPasswordField() {
        if (dbPasswordField == null) {
        	dbPasswordField = new JTextField();
        	dbPasswordField.setText(DeployPropertiesManager.getDeployPropertyValue("DB_PASSWORD"));
        	dbPasswordField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	dbPasswordField.addFocusListener(new FocusChangeHandler());
        }
        return dbPasswordField;
    }
    
    /**
     * This method initializes the Database Driver Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getDbDriverField() {
        if (dbDriverField == null) {
        	dbDriverField = new JTextField();
        	dbDriverField.setText(DeployPropertiesManager.getDeployPropertyValue("DB_DRIVER"));
        	dbDriverField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	dbDriverField.addFocusListener(new FocusChangeHandler());
        }
        return dbDriverField;
    }
    
    /**
     * This method initializes the Database Driver Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getDbDialectField() {
        if (dbDialectField == null) {
        	dbDialectField = new JTextField();
        	dbDialectField.setText(DeployPropertiesManager.getDeployPropertyValue("DB_DIALECT"));
        	dbDialectField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	dbDialectField.addFocusListener(new FocusChangeHandler());
        }
        return dbDialectField;
    }
    
    /**
     * This method initializes the Enable Writable API Extension CheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getEnableWritableApiExtensionCheckBox() {
        if (enableWritableApiExtensionCheckBox == null) {
        	enableWritableApiExtensionCheckBox = new JCheckBox();
        	enableWritableApiExtensionCheckBox.setToolTipText("Enable Writable API Extension?");
        	enableWritableApiExtensionCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	enableWritableApiExtensionCheckBox.setSelected(Boolean.parseBoolean(DeployPropertiesManager.getDeployPropertyValue("ENABLE_WRITABLE_API_EXTENSION")));
        	enableWritableApiExtensionCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	enableWritableApiExtensionCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					toggleWritableApiFields();
				}
        	});

        	enableWritableApiExtensionCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return enableWritableApiExtensionCheckBox;
    }

    /**
     * This method initializes the Writable API Database Type Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getDatabaseTypeField() {
        if (databaseTypeField == null) {
        	databaseTypeField = new JTextField();
        	databaseTypeField.setText(DeployPropertiesManager.getDeployPropertyValue("DATABASE_TYPE"));
        	databaseTypeField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	databaseTypeField.addFocusListener(new FocusChangeHandler());
        }
        return databaseTypeField;
    }
    
    /**
     * This method initializes the Writable API Identity Generator Tag Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getIdentityGeneratorTagField() {
        if (identityGeneratorTagField == null) {
        	identityGeneratorTagField = new JTextField();
        	identityGeneratorTagField.setText(DeployPropertiesManager.getDeployPropertyValue("IDENTITY_GENERATOR_TAG"));
        	identityGeneratorTagField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	identityGeneratorTagField.addFocusListener(new FocusChangeHandler());
        }
        return identityGeneratorTagField;
    }

    /**
     * This method initializes the CLM Database Connection URL Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getCaDsrConnectionUrlField() {
        if (caDsrConnectionUrlField == null) {
        	caDsrConnectionUrlField = new JTextField();
        	caDsrConnectionUrlField.setText(DeployPropertiesManager.getDeployPropertyValue("CADSR_CONNECTION_URL"));
        	caDsrConnectionUrlField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	caDsrConnectionUrlField.addFocusListener(new FocusChangeHandler());
        }
        return caDsrConnectionUrlField;
    }
    

    
    /**
     * This method initializes the Enable Common Logging Module CheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getEnableCommonLoggingModuleCheckBox() {
        if (enableCommonLoggingModuleCheckBox == null) {
        	enableCommonLoggingModuleCheckBox = new JCheckBox();
        	enableCommonLoggingModuleCheckBox.setToolTipText("Enable Common Logging Module Extension?");
        	enableCommonLoggingModuleCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	enableCommonLoggingModuleCheckBox.setSelected(Boolean.parseBoolean(DeployPropertiesManager.getDeployPropertyValue("ENABLE_WRITABLE_API_EXTENSION")));
        	enableCommonLoggingModuleCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	enableCommonLoggingModuleCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					toggleWritableApiFields();
				}
        	});

        	enableCommonLoggingModuleCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return enableCommonLoggingModuleCheckBox;
    }
    
    /**
     * This method initializes the CLM Project Name Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getClmProjectName() {
        if (clmProjectName == null) {
        	clmProjectName = new JTextField();
        	clmProjectName.setText(DeployPropertiesManager.getDeployPropertyValue("PROJECT_NAME"));
        	clmProjectName.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	clmProjectName.addFocusListener(new FocusChangeHandler());
        }
        return clmProjectName;
    }

    /**
     * This method initializes the CLM Database Connection URL Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getClmDbConnectionUrlField() {
        if (clmDbConnectionUrlField == null) {
        	clmDbConnectionUrlField = new JTextField();
        	clmDbConnectionUrlField.setText(DeployPropertiesManager.getDeployPropertyValue("DB_CONNECTION_URL"));
        	clmDbConnectionUrlField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	clmDbConnectionUrlField.addFocusListener(new FocusChangeHandler());
        }
        return clmDbConnectionUrlField;
    }
    
    /**
     * This method initializes the CLM Database Username Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getClmDbUsernameField() {
        if (clmDbUsernameField == null) {
        	clmDbUsernameField = new JTextField();
        	clmDbUsernameField.setText(DeployPropertiesManager.getDeployPropertyValue("DB_USERNAME"));
        	clmDbUsernameField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	clmDbUsernameField.addFocusListener(new FocusChangeHandler());
        }
        return clmDbUsernameField;
    }
    
    /**
     * This method initializes the CLM Database Password Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getClmDbPasswordField() {
        if (clmDbPasswordField == null) {
        	clmDbPasswordField = new JTextField();
        	clmDbPasswordField.setText(DeployPropertiesManager.getDeployPropertyValue("DB_PASSWORD"));
        	clmDbPasswordField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	clmDbPasswordField.addFocusListener(new FocusChangeHandler());
        }
        return clmDbPasswordField;
    }
    
    /**
     * This method initializes the Database Driver Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getClmDbDriverField() {
        if (clmDbDriverField == null) {
        	clmDbDriverField = new JTextField();
        	clmDbDriverField.setText(DeployPropertiesManager.getDeployPropertyValue("DB_DRIVER"));
        	clmDbDriverField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validateInput();
                }
            });
        	clmDbDriverField.addFocusListener(new FocusChangeHandler());
        }
        return clmDbDriverField;
    }
    
    //TODO
    
    /**
     * This method initializes closeButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getCloseButton() {
        if (closeButton == null) {
            closeButton = new JButton();
            closeButton.setIcon(PortalLookAndFeel.getCloseIcon());
            closeButton.setText("Cancel");
            closeButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    dispose();
                }
            });
        }
        return closeButton;
    }


    /**
     * This method initializes extensionsPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getExtensionsPanel() {
        if (extensionsPanel == null) {
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.gridx = 0;
            gridBagConstraints13.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints13.weightx = 1.0D;
            gridBagConstraints13.weighty = 0.0D;
            gridBagConstraints13.gridy = 0;
            GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
            gridBagConstraints20.gridx = 0;
            gridBagConstraints20.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints20.gridwidth = 3;
            gridBagConstraints20.weightx = 1.0D;
            gridBagConstraints20.weighty = 1.0D;
            gridBagConstraints20.insets = new java.awt.Insets(5, 2, 5, 2);
            gridBagConstraints20.gridy = 1;
            GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
            gridBagConstraints19.gridx = 0;
            gridBagConstraints19.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints19.gridheight = 2;
            gridBagConstraints19.weightx = 1.0D;
            gridBagConstraints19.weighty = 1.0D;
            gridBagConstraints19.gridy = 2;
            extensionsPanel = new JPanel();
            extensionsPanel.setLayout(new GridBagLayout());
            extensionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Service Extensions",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
            extensionsPanel.add(getExtSelectionPanel(), gridBagConstraints13);
//            extensionsPanel.add(getExtensionsTable(), gridBagConstraints19);
            extensionsPanel.add(getExtensionsTableionsTablePanel(), gridBagConstraints20);
        }
        return extensionsPanel;
    }


    /**
     * This method initializes addExtensionButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getAddExtensionButton() {
        if (addExtensionButton == null) {
            addExtensionButton = new JButton();
            addExtensionButton.setText("Add");
            addExtensionButton.setIcon(PortalLookAndFeel.getAddIcon());
            addExtensionButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (!((String) getServiceStyleSeletor().getSelectedItem()).equals("NONE")) {
//                        getExtensionsTable().addRow((String) getServiceStyleSeletor().getSelectedItem());
                    }
                }
            });
        }
        return addExtensionButton;
    }


    /**
     * This method initializes removeExtensionButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getRemoveExtensionButton() {
        if (removeExtensionButton == null) {
            removeExtensionButton = new JButton();
            removeExtensionButton.setText("Remove");
            removeExtensionButton.setIcon(PortalLookAndFeel.getRemoveIcon());
            removeExtensionButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
//                        getExtensionsTable().removeSelectedRow();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

            });
        }
        return removeExtensionButton;
    }


    /**
     * This method initializes extensionsScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getExtensionsScrollPane() {
        if (extensionsScrollPane == null) {
            extensionsScrollPane = new JScrollPane();
//            extensionsScrollPane.setViewportView(getExtensionsTable());
        }
        return extensionsScrollPane;
    }


    /**
     * This method initializes extensionsTable
     * 
     * @return javax.swing.JTable
     */
//    private ExtensionsTable getExtensionsTable() {
//        if (extensionsTable == null) {
//            extensionsTable = new ExtensionsTable();
//        }
//        return extensionsTable;
//    }


    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getExtensionsTableionsTablePanel() {
        if (extensionsTablePanel == null) {
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.gridx = 1;
            gridBagConstraints21.anchor = java.awt.GridBagConstraints.SOUTHWEST;
            gridBagConstraints21.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints21.gridy = 0;
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.gridx = 1;
            gridBagConstraints14.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints14.gridy = 1;
            downExtensionLabel = new JLabel();
            downExtensionLabel.setToolTipText("moves the selected extension down "
                + "in the list so that it will be executed after the preceding extensions");
            downExtensionLabel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    try {
//                        getExtensionsTable().moveSelectedRowDown();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
//            downExtensionLabel.setIcon(IntroduceLookAndFeel.getDownIcon());
            upExtensionLabel = new JLabel();
            upExtensionLabel.setToolTipText("moves the selected extension "
                + "higher in the list so that it will be executed before the following extensions");
            upExtensionLabel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    try {
//                        getExtensionsTable().moveSelectedRowUp();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
//            upExtensionLabel.setIcon(IntroduceLookAndFeel.getUpIcon());
            GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
            gridBagConstraints18.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints18.gridy = 0;
            gridBagConstraints18.weightx = 1.0;
            gridBagConstraints18.weighty = 1.0;
            gridBagConstraints18.gridheight = 2;
            gridBagConstraints18.gridx = 0;
            extensionsTablePanel = new JPanel();
            extensionsTablePanel.setLayout(new GridBagLayout());
            extensionsTablePanel.add(getExtensionsScrollPane(), gridBagConstraints18);
            extensionsTablePanel.add(upExtensionLabel, gridBagConstraints21);
            extensionsTablePanel.add(downExtensionLabel, gridBagConstraints14);
        }
        return extensionsTablePanel;
    }


    /**
     * This method initializes extSelectionPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getExtSelectionPanel() {
        if (extSelectionPanel == null) {
            GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
            gridBagConstraints22.gridy = 0;
            gridBagConstraints22.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints22.gridx = 1;
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.gridy = 0;
            gridBagConstraints16.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints16.gridx = 2;
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints15.gridx = 0;
            gridBagConstraints15.gridy = 0;
            gridBagConstraints15.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints15.weightx = 1.0;
            extSelectionPanel = new JPanel();
            extSelectionPanel.setLayout(new GridBagLayout());
            extSelectionPanel.add(getServiceStyleSeletor(), gridBagConstraints15);
            extSelectionPanel.add(getRemoveExtensionButton(), gridBagConstraints16);
            extSelectionPanel.add(getAddExtensionButton(), gridBagConstraints22);
        }
        return extSelectionPanel;
    }


    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getServiceStylePanel() {
        if (serviceStylePanel == null) {
            GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
            gridBagConstraints17.gridx = 0;
            gridBagConstraints17.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints17.weightx = 1.0D;
            gridBagConstraints17.weighty = 1.0D;
            gridBagConstraints17.gridy = 0;
            serviceStylePanel = new JPanel();
            serviceStylePanel.setLayout(new GridBagLayout());
            serviceStylePanel.add(getExtensionsPanel(), gridBagConstraints17);
        }
        return serviceStylePanel;
    }


	/**
	 * This method initializes mainTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getMainTabbedPane() {
		if (mainTabbedPane == null) {
			mainTabbedPane = new JTabbedPane();
			mainTabbedPane.addTab("Project", null, new IconFeedbackPanel(this.validationModel, getProjectSettingsPanel()), null);
			mainTabbedPane.addTab("Model", null, new IconFeedbackPanel(this.validationModel, getModelSettingsPanel()), null);
			mainTabbedPane.addTab("Code Gen", null, new IconFeedbackPanel(this.validationModel, getCodeGenSettingsPanel()), null);
			mainTabbedPane.addTab("DB", null, new IconFeedbackPanel(this.validationModel, getDbConnectionSettingsPanel()), null);
			
			mainTabbedPane.addTab("Writable API", null, new IconFeedbackPanel(this.validationModel, getWritableApiSettingsPanel()), null);
//			mainTabbedPane.addTab("Logging", null, new IconFeedbackPanel(this.validationModel, getClmSettingsPanel()), null);
			
			mainTabbedPane.addTab("Security", null, new IconFeedbackPanel(this.validationModel, getSecuritySettingsPanel()), null);
			mainTabbedPane.addTab("CSM DB", null, new IconFeedbackPanel(this.validationModel, getCsmDbConnectionSettingsPanel()), null);
			mainTabbedPane.addTab("caGrid Auth", null, new IconFeedbackPanel(this.validationModel, getCaGridAuthSettingsPanel()), null);
			
			mainTabbedPane.addTab("App Server", null, new IconFeedbackPanel(this.validationModel, getAppServerSettingsPanel()), null);

			mainTabbedPane.addTab("Advanced", null, new IconFeedbackPanel(this.validationModel, getAdvancedSettingsPanel()), null);
		}
		return mainTabbedPane;
	}
	
	/**
	 * This method initializes securitySettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSecuritySettingsPanel() {
		if (securitySettingsPanel == null) {
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.gridx = 0;
			gridBagConstraints23.fill = GridBagConstraints.BOTH;
			gridBagConstraints23.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridy = 0;
			securitySettingsPanel = new JPanel();
			securitySettingsPanel.setLayout(new GridBagLayout());
//			securitySettingsPanel.add(getServiceStylePanel(), gridBagConstraints);
//			securitySettingsPanel.add(getResourceOptionsConfigPanel(), gridBagConstraints23);
		}
		return securitySettingsPanel;
	}
	
	/**
	 * This method initializes writableApiSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getWritableApiSettingsPanel() {
		if (writableApiSettingsPanel == null) {

			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.gridy = 1;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.gridx = 0;

			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weighty = 1.0D;
			gridBagConstraints11.weightx = 1.0D;  
			gridBagConstraints11.gridwidth = 2;

			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints20.gridy = 2;
			gridBagConstraints20.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints20.gridx = 0;            

			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints21.gridy = 2;
			gridBagConstraints21.gridx = 1;
			gridBagConstraints21.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints21.gridwidth = 2;
			gridBagConstraints21.weighty = 1.0D;
			gridBagConstraints21.weightx = 1.0D;  

			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints30.gridy = 3;
			gridBagConstraints30.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints30.gridx = 0;

			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints31.gridy = 3;
			gridBagConstraints31.weightx = 1.0;
			gridBagConstraints31.gridwidth = 2;
			gridBagConstraints31.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints31.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints31.weighty = 1.0D;
			gridBagConstraints31.gridx = 1;

			GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
			gridBagConstraints40.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints40.gridy = 4;
			gridBagConstraints40.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints40.gridx = 0;

			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints41.gridy = 4;
			gridBagConstraints41.weightx = 1.0;
			gridBagConstraints41.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints41.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints41.gridwidth = 2;
			gridBagConstraints41.weighty = 1.0D;
			gridBagConstraints41.gridx = 1;

			GridBagConstraints gridBagConstraints50 = new GridBagConstraints();
			gridBagConstraints50.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints50.gridy = 5;
			gridBagConstraints50.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints50.gridx = 0;

			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints51.gridy = 5;
			gridBagConstraints51.weightx = 1.0;
			gridBagConstraints51.gridwidth = 2;
			gridBagConstraints51.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints51.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints51.weighty = 1.0D;
			gridBagConstraints51.gridx = 1;

			GridBagConstraints gridBagConstraints60 = new GridBagConstraints();
			gridBagConstraints60.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints60.gridy = 6;
			gridBagConstraints60.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints60.gridx = 0;

			GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
			gridBagConstraints61.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints61.gridy = 6;
			gridBagConstraints61.weightx = 1.0;
			gridBagConstraints61.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints61.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints61.gridwidth = 2;
			gridBagConstraints61.weighty = 1.0D;
			gridBagConstraints61.gridx = 1;

			GridBagConstraints gridBagConstraints70 = new GridBagConstraints();
			gridBagConstraints70.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints70.gridy = 7;
			gridBagConstraints70.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints70.gridx = 0;

			GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
			gridBagConstraints71.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints71.gridy = 7;
			gridBagConstraints71.weightx = 1.0;
			gridBagConstraints71.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints71.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints71.gridwidth = 2;
			gridBagConstraints71.weighty = 1.0D;
			gridBagConstraints71.gridx = 1;

			GridBagConstraints gridBagConstraints80 = new GridBagConstraints();
			gridBagConstraints80.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints80.gridy = 8;
			gridBagConstraints80.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints80.gridx = 0;

			GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
			gridBagConstraints81.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints81.gridy = 8;
			gridBagConstraints81.weightx = 1.0;
			gridBagConstraints81.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints81.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints81.gridwidth = 2;
			gridBagConstraints81.weighty = 1.0D;
			gridBagConstraints81.gridx = 1;

			GridBagConstraints gridBagConstraints90 = new GridBagConstraints();
			gridBagConstraints90.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints90.gridy = 9;
			gridBagConstraints90.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints90.gridx = 0;

			GridBagConstraints gridBagConstraints91 = new GridBagConstraints();
			gridBagConstraints91.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints91.gridy = 9;
			gridBagConstraints91.weightx = 1.0;
			gridBagConstraints91.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints91.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints91.gridwidth = 2;
			gridBagConstraints91.weighty = 1.0D;
			gridBagConstraints91.gridx = 1;

			GridBagConstraints gridBagConstraints100 = new GridBagConstraints();
			gridBagConstraints100.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints100.gridy = 10;
			gridBagConstraints100.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints100.gridx = 0;

			GridBagConstraints gridBagConstraints101 = new GridBagConstraints();
			gridBagConstraints101.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints101.gridy = 10;
			gridBagConstraints101.weightx = 1.0;
			gridBagConstraints101.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints101.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints101.gridwidth = 2;
			gridBagConstraints101.weighty = 1.0D;
			gridBagConstraints101.gridx = 1;

//			GridBagConstraints gridBagConstraints110 = new GridBagConstraints();
//			gridBagConstraints110.anchor = java.awt.GridBagConstraints.WEST;
//			gridBagConstraints110.gridy = 11;
//			gridBagConstraints110.insets = new java.awt.Insets(2, 2, 2, 2);
//			gridBagConstraints110.gridx = 0;
//
//			GridBagConstraints gridBagConstraints111 = new GridBagConstraints();
//			gridBagConstraints111.fill = java.awt.GridBagConstraints.HORIZONTAL;
//			gridBagConstraints111.gridy = 11;
//			gridBagConstraints111.weightx = 1.0;
//			gridBagConstraints111.anchor = java.awt.GridBagConstraints.WEST;
//			gridBagConstraints111.insets = new java.awt.Insets(2, 2, 2, 2);
//			gridBagConstraints111.gridwidth = 2;
//			gridBagConstraints111.weighty = 1.0D;
//			gridBagConstraints111.gridx = 1;

		    enableWritableApiExtensionLabel = new JLabel();
		    enableWritableApiExtensionLabel.setText("Enable Writable API Extension?");

		    databaseTypeLabel = new JLabel();
		    databaseTypeLabel.setText("Enter Database Type:");

		    identityGeneratorTagLabel = new JLabel();
		    identityGeneratorTagLabel.setText("Enter Hibernate Identity Generator Tag:");

		    caDsrConnectionUrlLabel = new JLabel();
		    caDsrConnectionUrlLabel.setText("Enter caDSR Connection URL:");

		    enableCommonLoggingModuleLabel = new JLabel();
		    enableCommonLoggingModuleLabel.setText("Enable Common Logging Module?");

		    clmProjectNameLabel = new JLabel();
		    clmProjectNameLabel.setText("Enter CLM Project Name:");

		    clmDbConnectionUrlLabel = new JLabel();
		    clmDbConnectionUrlLabel.setText("Enter CLM Database Connection URL:");

		    clmDbUsernameLabel = new JLabel();
		    clmDbUsernameLabel.setText("Enter CLM Database Username:");

		    clmDbPasswordLabel = new JLabel();
		    clmDbPasswordLabel.setText("Enter CLM Database Password:");

		    clmDbDriverLabel = new JLabel();
		    clmDbDriverLabel.setText("Enter CLM Database Driver:");
		    
			writableApiSettingsPanel = new JPanel();
			writableApiSettingsPanel.setLayout(new GridBagLayout());
			writableApiSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Define Writable API and Common Logging Module Properties",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));

			writableApiSettingsPanel.add(enableWritableApiExtensionLabel, gridBagConstraints10);
			writableApiSettingsPanel.add(getEnableWritableApiExtensionCheckBox(), gridBagConstraints11);
			writableApiSettingsPanel.add(databaseTypeLabel, gridBagConstraints20);
			writableApiSettingsPanel.add(getDatabaseTypeField(), gridBagConstraints21);
			writableApiSettingsPanel.add(identityGeneratorTagLabel, gridBagConstraints30);
			writableApiSettingsPanel.add(getIdentityGeneratorTagField(), gridBagConstraints31);
			writableApiSettingsPanel.add(caDsrConnectionUrlLabel, gridBagConstraints40);
			writableApiSettingsPanel.add(getCaDsrConnectionUrlField(), gridBagConstraints41);
			writableApiSettingsPanel.add(enableCommonLoggingModuleLabel, gridBagConstraints50);
			writableApiSettingsPanel.add(getEnableCommonLoggingModuleCheckBox(), gridBagConstraints51);
			writableApiSettingsPanel.add(clmProjectNameLabel, gridBagConstraints60);
			writableApiSettingsPanel.add(getClmProjectName(), gridBagConstraints61);
			writableApiSettingsPanel.add(clmDbConnectionUrlLabel, gridBagConstraints70);
			writableApiSettingsPanel.add(getClmDbConnectionUrlField(), gridBagConstraints71);
			writableApiSettingsPanel.add(clmDbUsernameLabel, gridBagConstraints80);
			writableApiSettingsPanel.add(getClmDbUsernameField(), gridBagConstraints81);   
			writableApiSettingsPanel.add(clmDbPasswordLabel, gridBagConstraints90);
			writableApiSettingsPanel.add(getClmDbPasswordField(), gridBagConstraints91);
			writableApiSettingsPanel.add(clmDbDriverLabel, gridBagConstraints100);
			writableApiSettingsPanel.add(getClmDbDriverField(), gridBagConstraints101);

			writableApiSettingsPanel.validate();
		}
		return writableApiSettingsPanel;
	}

	/**
	 * This method initializes appServerSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAppServerSettingsPanel() {
		if (appServerSettingsPanel == null) {
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.gridx = 0;
			gridBagConstraints23.fill = GridBagConstraints.BOTH;
			gridBagConstraints23.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridy = 0;
			appServerSettingsPanel = new JPanel();
			appServerSettingsPanel.setLayout(new GridBagLayout());
			appServerSettingsPanel.add(getServiceStylePanel(), gridBagConstraints);
			appServerSettingsPanel.add(getResourceOptionsConfigPanel(), gridBagConstraints23);
		}
		return appServerSettingsPanel;
	}
	
    /**
     * This method initializes the Project Settings jPanel
     */
    private JPanel getProjectSettingsPanel() {
        if (projectSettingsPanel == null) {
        	
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints10.gridy = 1;
            gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints10.gridx = 0;
            
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints11.gridx = 1;
            gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints11.gridy = 1;
            gridBagConstraints11.weighty = 1.0D;
            gridBagConstraints11.weightx = 1.0;          
            
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints12.gridy = 1;
            gridBagConstraints12.gridx = 2;
            gridBagConstraints12.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints12.gridwidth = 1;
            
            GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
            gridBagConstraints20.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints20.gridy = 2;
            gridBagConstraints20.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints20.gridx = 0;            

            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints21.gridy = 2;
            gridBagConstraints21.gridx = 1;
            gridBagConstraints21.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints21.gridwidth = 2;
            gridBagConstraints21.weighty = 1.0D;
            gridBagConstraints21.weightx = 1.0;
            
            GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
            gridBagConstraints30.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints30.gridy = 3;
            gridBagConstraints30.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints30.gridx = 0;
            
            GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
            gridBagConstraints31.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints31.gridy = 3;
            gridBagConstraints31.weightx = 1.0;
            gridBagConstraints31.gridwidth = 2;
            gridBagConstraints31.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints31.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints31.weighty = 1.0D;
            gridBagConstraints31.gridx = 1;
     
            GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
            gridBagConstraints40.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints40.gridy = 4;
            gridBagConstraints40.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints40.gridx = 0;
            
            GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
            gridBagConstraints41.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints41.gridy = 4;
            gridBagConstraints41.weightx = 1.0;
            gridBagConstraints41.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints41.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints41.gridwidth = 2;
            gridBagConstraints41.weighty = 1.0D;
            gridBagConstraints41.gridx = 1;
                        
            sdkInstallDirLabel = new JLabel();
            sdkInstallDirLabel.setText("Select your existing SDK Home directory:");
            sdkInstallDirLabel.setName("SDK Directory");
            
            projectNameLabel = new JLabel();
            projectNameLabel.setText("Enter the Project Name:");
            
            namespacePrefixLabel = new JLabel();
            namespacePrefixLabel.setText("Enter the Project Namespace Prefix:");
            
            webserviceNameLabel = new JLabel();
            webserviceNameLabel.setText("Enter the Project Web Service Name:");
            
            projectSettingsPanel = new JPanel();
            projectSettingsPanel.setLayout(new GridBagLayout());
            projectSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Define Project Properties",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
            
            projectSettingsPanel.add(sdkInstallDirLabel, gridBagConstraints10);
            projectSettingsPanel.add(getSdkInstallDirField(), gridBagConstraints11);
            projectSettingsPanel.add(getSdkInstallDirButton(), gridBagConstraints12);
            projectSettingsPanel.add(projectNameLabel, gridBagConstraints20);
            projectSettingsPanel.add(getProjectNameField(), gridBagConstraints21);
            projectSettingsPanel.add(namespacePrefixLabel, gridBagConstraints30);
            projectSettingsPanel.add(getNamespacePrefixField(), gridBagConstraints31);
            projectSettingsPanel.add(webserviceNameLabel, gridBagConstraints40);
            projectSettingsPanel.add(getWebServiceNameField(), gridBagConstraints41);
            
            projectSettingsPanel.validate();
        }
        return projectSettingsPanel;
    }
	
	/**
	 * This method initializes modelSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getModelSettingsPanel() {
		if (modelSettingsPanel == null) {
			
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints10.gridy = 1;
            gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints10.gridx = 0;
            
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints11.gridx = 1;
            gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints11.gridy = 1;
            gridBagConstraints11.weighty = 1.0D;
            gridBagConstraints11.weightx = 1.0;          
            
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints12.gridy = 1;
            gridBagConstraints12.gridx = 2;
            gridBagConstraints12.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints12.gridwidth = 1;
            
            GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
            gridBagConstraints20.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints20.gridy = 2;
            gridBagConstraints20.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints20.gridx = 0;            

            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints21.gridy = 2;
            gridBagConstraints21.gridx = 1;
            gridBagConstraints21.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints21.gridwidth = 1;
            gridBagConstraints21.weightx = 0.3D;
            
            GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
            gridBagConstraints30.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints30.gridy = 3;
            gridBagConstraints30.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints30.gridx = 0;
            
            GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
            gridBagConstraints31.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints31.gridy = 3;
            gridBagConstraints31.weightx = 1.0;
            gridBagConstraints31.gridwidth = 2;
            gridBagConstraints31.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints31.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints31.weighty = 1.0D;
            gridBagConstraints31.gridx = 1;
            
            GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
            gridBagConstraints40.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints40.gridy = 4;
            gridBagConstraints40.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints40.gridx = 0;
            
            GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
            gridBagConstraints41.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints41.gridy = 4;
            gridBagConstraints41.weightx = 1.0;
            gridBagConstraints41.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints41.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints41.gridwidth = 2;
            gridBagConstraints41.weighty = 1.0D;
            gridBagConstraints41.gridx = 1;
            
            GridBagConstraints gridBagConstraints50 = new GridBagConstraints();
            gridBagConstraints50.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints50.gridy = 5;
            gridBagConstraints50.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints50.gridx = 0;
            
            GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
            gridBagConstraints51.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints51.gridy = 5;
            gridBagConstraints51.gridwidth = 2;
            gridBagConstraints51.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints51.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints51.weightx = 1.0D;
            gridBagConstraints51.weighty = 1.0D;
            gridBagConstraints51.gridx = 1;
            
            GridBagConstraints gridBagConstraints60 = new GridBagConstraints();
            gridBagConstraints60.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints60.gridy = 6;
            gridBagConstraints60.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints60.gridx = 0;
            
            GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
            gridBagConstraints61.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints61.gridy = 6;
            gridBagConstraints61.weightx = 1.0;
            gridBagConstraints61.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints61.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints61.gridwidth = 2;
            gridBagConstraints61.weighty = 1.0D;
            gridBagConstraints61.gridx = 1;
            
            GridBagConstraints gridBagConstraints70 = new GridBagConstraints();
            gridBagConstraints70.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints70.gridy = 7;
            gridBagConstraints70.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints70.gridx = 0;
            
            GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
            gridBagConstraints71.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints71.gridy = 7;
            gridBagConstraints71.weightx = 1.0;
            gridBagConstraints71.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints71.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints71.gridwidth = 2;
            gridBagConstraints71.weighty = 1.0D;
            gridBagConstraints71.gridx = 1;
            
            GridBagConstraints gridBagConstraints80 = new GridBagConstraints();
            gridBagConstraints80.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints80.gridy = 8;
            gridBagConstraints80.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints80.gridx = 0;
            
            GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
            gridBagConstraints81.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints81.gridy = 8;
            gridBagConstraints81.weightx = 1.0;
            gridBagConstraints81.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints81.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints81.gridwidth = 2;
            gridBagConstraints81.weighty = 1.0D;
            gridBagConstraints81.gridx = 1;

            modelFileLabel = new JLabel();
            modelFileLabel.setText("Enter the Model file name:");
            
            modelFileTypeLabel = new JLabel();
            modelFileTypeLabel.setText("Enter the Model file type:");
            
            logicalModelLabel = new JLabel();
            logicalModelLabel.setText("Enter the 'Logical Model' package name:");
            
            dataModelLabel = new JLabel();
            dataModelLabel.setText("Enter the 'Data Model' package name:");
            
            includePackageLabel = new JLabel();
            includePackageLabel.setText("Enter the 'Include Package' regex:");
            
            excludePackageLabel = new JLabel();
            excludePackageLabel.setText("Enter the 'Exclude Package' regex:");
            
            excludeNameLabel = new JLabel();
            excludeNameLabel.setText("Enter the 'Exclude Class Name' regex:");
            
            excludeNamespaceLabel = new JLabel();
            excludeNamespaceLabel.setText("Enter the 'Exclude Namespace' regex:");
            
            modelSettingsPanel = new JPanel();
            modelSettingsPanel.setLayout(new GridBagLayout());
            modelSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Define Model Properties",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
            
            modelSettingsPanel.add(modelFileLabel, gridBagConstraints10);
            modelSettingsPanel.add(getModelFileField(), gridBagConstraints11);
            modelSettingsPanel.add(getModelFilePathButton(), gridBagConstraints12);
            modelSettingsPanel.add(modelFileTypeLabel, gridBagConstraints20);
            modelSettingsPanel.add(getModelFileTypeField(), gridBagConstraints21);
            modelSettingsPanel.add(logicalModelLabel, gridBagConstraints30);
            modelSettingsPanel.add(getLogicalModelField(), gridBagConstraints31);
            modelSettingsPanel.add(dataModelLabel, gridBagConstraints40);
            modelSettingsPanel.add(getDataModelField(), gridBagConstraints41);
            modelSettingsPanel.add(includePackageLabel, gridBagConstraints50);
            modelSettingsPanel.add(getIncludePackageField(), gridBagConstraints51);
            modelSettingsPanel.add(excludePackageLabel, gridBagConstraints60);
            modelSettingsPanel.add(getExcludePackageField(), gridBagConstraints61);
            modelSettingsPanel.add(excludeNameLabel, gridBagConstraints70);
            modelSettingsPanel.add(getExcludeNameField(), gridBagConstraints71);
            modelSettingsPanel.add(excludeNamespaceLabel, gridBagConstraints80);
            modelSettingsPanel.add(getExcludeNamespaceField(), gridBagConstraints81);             
            
            modelSettingsPanel.validate();
		}
		return modelSettingsPanel;
	}
	
	/**
	 * This method initializes loggingSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCodeGenSettingsPanel() {
		if (codeGenSettingsPanel == null) {

			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.gridy = 1;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.gridx = 0;

			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weighty = 1.0D;
			gridBagConstraints11.weightx = 1.0D;  
			gridBagConstraints11.gridwidth = 2;

			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints20.gridy = 2;
			gridBagConstraints20.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints20.gridx = 0;            

			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints21.gridy = 2;
			gridBagConstraints21.gridx = 1;
			gridBagConstraints21.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints21.gridwidth = 2;
			gridBagConstraints21.weighty = 1.0D;
			gridBagConstraints21.weightx = 1.0D;  

			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints30.gridy = 3;
			gridBagConstraints30.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints30.gridx = 0;

			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints31.gridy = 3;
			gridBagConstraints31.weightx = 1.0;
			gridBagConstraints31.gridwidth = 2;
			gridBagConstraints31.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints31.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints31.weighty = 1.0D;
			gridBagConstraints31.gridx = 1;

			GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
			gridBagConstraints40.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints40.gridy = 4;
			gridBagConstraints40.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints40.gridx = 0;

			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints41.gridy = 4;
			gridBagConstraints41.weightx = 1.0;
			gridBagConstraints41.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints41.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints41.gridwidth = 2;
			gridBagConstraints41.weighty = 1.0D;
			gridBagConstraints41.gridx = 1;

			GridBagConstraints gridBagConstraints50 = new GridBagConstraints();
			gridBagConstraints50.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints50.gridy = 5;
			gridBagConstraints50.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints50.gridx = 0;

			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints51.gridy = 5;
			gridBagConstraints51.weightx = 1.0;
			gridBagConstraints51.gridwidth = 2;
			gridBagConstraints51.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints51.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints51.weighty = 1.0D;
			gridBagConstraints51.gridx = 1;

			GridBagConstraints gridBagConstraints60 = new GridBagConstraints();
			gridBagConstraints60.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints60.gridy = 6;
			gridBagConstraints60.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints60.gridx = 0;

			GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
			gridBagConstraints61.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints61.gridy = 6;
			gridBagConstraints61.weightx = 1.0;
			gridBagConstraints61.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints61.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints61.gridwidth = 2;
			gridBagConstraints61.weighty = 1.0D;
			gridBagConstraints61.gridx = 1;

			GridBagConstraints gridBagConstraints70 = new GridBagConstraints();
			gridBagConstraints70.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints70.gridy = 7;
			gridBagConstraints70.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints70.gridx = 0;

			GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
			gridBagConstraints71.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints71.gridy = 7;
			gridBagConstraints71.weightx = 1.0;
			gridBagConstraints71.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints71.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints71.gridwidth = 2;
			gridBagConstraints71.weighty = 1.0D;
			gridBagConstraints71.gridx = 1;

			GridBagConstraints gridBagConstraints80 = new GridBagConstraints();
			gridBagConstraints80.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints80.gridy = 8;
			gridBagConstraints80.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints80.gridx = 0;

			GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
			gridBagConstraints81.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints81.gridy = 8;
			gridBagConstraints81.weightx = 1.0;
			gridBagConstraints81.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints81.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints81.gridwidth = 2;
			gridBagConstraints81.weighty = 1.0D;
			gridBagConstraints81.gridx = 1;
			
			GridBagConstraints gridBagConstraints90 = new GridBagConstraints();
			gridBagConstraints90.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints90.gridy = 9;
			gridBagConstraints90.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints90.gridx = 0;

			GridBagConstraints gridBagConstraints91 = new GridBagConstraints();
			gridBagConstraints91.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints91.gridy = 9;
			gridBagConstraints91.weightx = 1.0;
			gridBagConstraints91.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints91.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints91.gridwidth = 2;
			gridBagConstraints91.weighty = 1.0D;
			gridBagConstraints91.gridx = 1;
			
			GridBagConstraints gridBagConstraints100 = new GridBagConstraints();
			gridBagConstraints100.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints100.gridy = 10;
			gridBagConstraints100.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints100.gridx = 0;

			GridBagConstraints gridBagConstraints101 = new GridBagConstraints();
			gridBagConstraints101.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints101.gridy = 10;
			gridBagConstraints101.weightx = 1.0;
			gridBagConstraints101.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints101.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints101.gridwidth = 2;
			gridBagConstraints101.weighty = 1.0D;
			gridBagConstraints101.gridx = 1;
			
			GridBagConstraints gridBagConstraints110 = new GridBagConstraints();
			gridBagConstraints110.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints110.gridy = 11;
			gridBagConstraints110.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints110.gridx = 0;

			GridBagConstraints gridBagConstraints111 = new GridBagConstraints();
			gridBagConstraints111.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints111.gridy = 11;
			gridBagConstraints111.weightx = 1.0;
			gridBagConstraints111.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints111.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints111.gridwidth = 2;
			gridBagConstraints111.weighty = 1.0D;
			gridBagConstraints111.gridx = 1;

		    validateLogicalModelLabel = new JLabel();
		    validateLogicalModelLabel.setText("Validate Logical Model?");

		    validateModelMappingLabel = new JLabel();
		    validateModelMappingLabel.setText("Validate Model Mapping?");

		    validateGmeTagsLabel = new JLabel();
		    validateGmeTagsLabel.setText("Validate GME Tags?");
		    
		    generateBeansLabel = new JLabel();
		    generateBeansLabel.setText("Generate domain Java Beans?");

		    generateHibernateMappingLabel = new JLabel();
		    generateHibernateMappingLabel.setText("Generate Hibernate Mapping Files?");

		    generateCastorMappingLabel = new JLabel();
		    generateCastorMappingLabel.setText("Generate Castor Mapping files?");

		    generateXsdLabel = new JLabel();
		    generateXsdLabel.setText("Generate XSD's?");

		    generateXsdWithGmeTagsLabel = new JLabel();
		    generateXsdWithGmeTagsLabel.setText("Generate XSD's with GME tags?");
		    
		    generateXsdWithPermissibleValuesLabel = new JLabel();
		    generateXsdWithPermissibleValuesLabel.setText("Generate XSD's with Permissible Values?");
    
		    generateWsddLabel = new JLabel();
		    generateWsddLabel.setText("Generate WSDD?");

		    generateHibernateValidatorLabel = new JLabel();
		    generateHibernateValidatorLabel.setText("Generate Hibernate Validator?");

		    codeGenSettingsPanel = new JPanel();
		    codeGenSettingsPanel.setLayout(new GridBagLayout());
		    codeGenSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Define Code Generation Properties",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));

		    codeGenSettingsPanel.add(validateLogicalModelLabel, gridBagConstraints10);
		    codeGenSettingsPanel.add(getValidateLogicalModelCheckBox(), gridBagConstraints11);
		    codeGenSettingsPanel.add(validateModelMappingLabel, gridBagConstraints20);
			codeGenSettingsPanel.add(getValidateModelMappingCheckBox(), gridBagConstraints21);
			codeGenSettingsPanel.add(validateGmeTagsLabel, gridBagConstraints30);
			codeGenSettingsPanel.add(getValidateGmeTagsCheckBox(), gridBagConstraints31);
			codeGenSettingsPanel.add(generateBeansLabel, gridBagConstraints40);
			codeGenSettingsPanel.add(getGenerateBeansCheckBox(), gridBagConstraints41);
			codeGenSettingsPanel.add(generateHibernateMappingLabel, gridBagConstraints50);
			codeGenSettingsPanel.add(getGenerateHibernateMappingCheckBox(), gridBagConstraints51);
			codeGenSettingsPanel.add(generateCastorMappingLabel, gridBagConstraints60);
			codeGenSettingsPanel.add(getGenerateCastorMappingCheckBox(), gridBagConstraints61);
			codeGenSettingsPanel.add(generateXsdLabel, gridBagConstraints70);
			codeGenSettingsPanel.add(getGenerateXsdCheckBox(), gridBagConstraints71);
			codeGenSettingsPanel.add(generateXsdWithGmeTagsLabel, gridBagConstraints80);
			codeGenSettingsPanel.add(generateXsdWithGmeTagsCheckBox(), gridBagConstraints81);   
			codeGenSettingsPanel.add(generateXsdWithPermissibleValuesLabel, gridBagConstraints90);
			codeGenSettingsPanel.add(getGenerateXsdWithPermissibleValuesCheckBox(), gridBagConstraints91);
			codeGenSettingsPanel.add(generateWsddLabel, gridBagConstraints100);
			codeGenSettingsPanel.add(getGenerateWsddLabelCheckBox(), gridBagConstraints101);
			codeGenSettingsPanel.add(generateHibernateValidatorLabel, gridBagConstraints110);
			codeGenSettingsPanel.add(getGenerateHibernateValidatorCheckBox(), gridBagConstraints111);
			
		    codeGenSettingsPanel.validate();
		}
		return codeGenSettingsPanel;
	}
	
	/**
	 * This method initializes dbConnectionSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDbConnectionSettingsPanel() {
		if (dbConnectionSettingsPanel == null) {
			
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.gridy = 1;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.gridx = 0;

			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weighty = 1.0D;
			gridBagConstraints11.weightx = 1.0D;  
			gridBagConstraints11.gridwidth = 2;

			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints20.gridy = 2;
			gridBagConstraints20.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints20.gridx = 0;            

			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints21.gridy = 2;
			gridBagConstraints21.gridx = 1;
			gridBagConstraints21.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints21.gridwidth = 2;
			gridBagConstraints21.weighty = 1.0D;
			gridBagConstraints21.weightx = 1.0D;  

			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints30.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints30.gridy = 3;
			gridBagConstraints30.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints30.gridx = 0;
			gridBagConstraints30.gridwidth = 3;

			GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
			gridBagConstraints40.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints40.gridy = 4;
			gridBagConstraints40.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints40.gridx = 0;

			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints41.gridy = 4;
			gridBagConstraints41.weightx = 1.0;
			gridBagConstraints41.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints41.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints41.gridwidth = 2;
			gridBagConstraints41.weighty = 1.0D;
			gridBagConstraints41.gridx = 1;

			GridBagConstraints gridBagConstraints50 = new GridBagConstraints();
			gridBagConstraints50.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints50.gridy = 5;
			gridBagConstraints50.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints50.gridx = 0;

			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints51.gridy = 5;
			gridBagConstraints51.weightx = 1.0;
			gridBagConstraints51.gridwidth = 2;
			gridBagConstraints51.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints51.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints51.weighty = 1.0D;
			gridBagConstraints51.gridx = 1;

			GridBagConstraints gridBagConstraints60 = new GridBagConstraints();
			gridBagConstraints60.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints60.gridy = 6;
			gridBagConstraints60.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints60.gridx = 0;

			GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
			gridBagConstraints61.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints61.gridy = 6;
			gridBagConstraints61.weightx = 1.0;
			gridBagConstraints61.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints61.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints61.gridwidth = 2;
			gridBagConstraints61.weighty = 1.0D;
			gridBagConstraints61.gridx = 1;

			GridBagConstraints gridBagConstraints70 = new GridBagConstraints();
			gridBagConstraints70.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints70.gridy = 7;
			gridBagConstraints70.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints70.gridx = 0;

			GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
			gridBagConstraints71.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints71.gridy = 7;
			gridBagConstraints71.weightx = 1.0;
			gridBagConstraints71.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints71.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints71.gridwidth = 2;
			gridBagConstraints71.weighty = 1.0D;
			gridBagConstraints71.gridx = 1;

			GridBagConstraints gridBagConstraints80 = new GridBagConstraints();
			gridBagConstraints80.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints80.gridy = 8;
			gridBagConstraints80.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints80.gridx = 0;

			GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
			gridBagConstraints81.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints81.gridy = 8;
			gridBagConstraints81.weightx = 1.0;
			gridBagConstraints81.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints81.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints81.gridwidth = 2;
			gridBagConstraints81.weighty = 1.0D;
			gridBagConstraints81.gridx = 1;

		    useJndiBasedConnectionLabel = new JLabel();
			useJndiBasedConnectionLabel.setText("Use a JNDI-based Database Connection?");

		    dbJndiUrlLabel = new JLabel();
		    dbJndiUrlLabel.setText("Database JNDI URL:");

		    dbConnectionUrlLabel = new JLabel();
		    dbConnectionUrlLabel.setText("Database connection URL:");
		    
		    dbUsernameLabel = new JLabel();
		    dbUsernameLabel.setText("Database username:");

		    dbPasswordLabel = new JLabel();
		    dbPasswordLabel.setText("Database password:");

		    dbDriverLabel = new JLabel();
		    dbDriverLabel.setText("Database driver:");

		    dbDialectLabel = new JLabel();
		    dbDialectLabel.setText("Database dialect");

		    dbConnectionSettingsPanel = new JPanel();
		    dbConnectionSettingsPanel.setLayout(new GridBagLayout());
		    dbConnectionSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Define Database Connection Properties",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			
		    dbConnectionSettingsPanel.add(useJndiBasedConnectionLabel, gridBagConstraints10);
		    dbConnectionSettingsPanel.add(getUseJndiBasedConnectionCheckBox(), gridBagConstraints11);
		    dbConnectionSettingsPanel.add(dbJndiUrlLabel, gridBagConstraints20);
		    dbConnectionSettingsPanel.add(getDbJndiUrlField(), gridBagConstraints21);
		    dbConnectionSettingsPanel.add(new JLabel(), gridBagConstraints30);//Blank spacer line
		    dbConnectionSettingsPanel.add(dbConnectionUrlLabel, gridBagConstraints40);
		    dbConnectionSettingsPanel.add(getDbConnectionUrlField(), gridBagConstraints41);
		    dbConnectionSettingsPanel.add(dbUsernameLabel, gridBagConstraints50);
		    dbConnectionSettingsPanel.add(getDbUsernameField(), gridBagConstraints51);
		    dbConnectionSettingsPanel.add(dbPasswordLabel, gridBagConstraints60);
		    dbConnectionSettingsPanel.add(getDbPasswordField(), gridBagConstraints61);
		    dbConnectionSettingsPanel.add(dbDriverLabel, gridBagConstraints70);
		    dbConnectionSettingsPanel.add(getDbDriverField(), gridBagConstraints71);
		    dbConnectionSettingsPanel.add(dbDialectLabel, gridBagConstraints80);
		    dbConnectionSettingsPanel.add(getDbDialectField(), gridBagConstraints81);   
			
		    dbConnectionSettingsPanel.validate();
		}
		return dbConnectionSettingsPanel;
	}
	
	/**
	 * This method initializes csmDbConnectionSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCsmDbConnectionSettingsPanel() {
		if (csmDbConnectionSettingsPanel == null) {
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.gridx = 0;
			gridBagConstraints23.fill = GridBagConstraints.BOTH;
			gridBagConstraints23.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridy = 0;
			csmDbConnectionSettingsPanel = new JPanel();
			csmDbConnectionSettingsPanel.setLayout(new GridBagLayout());
			csmDbConnectionSettingsPanel.add(getServiceStylePanel(), gridBagConstraints);
			csmDbConnectionSettingsPanel.add(getResourceOptionsConfigPanel(), gridBagConstraints23);
		}
		return csmDbConnectionSettingsPanel;
	}
	
//	/**
//	 * This method initializes loggingSettingsPanel	
//	 * 	
//	 * @return javax.swing.JPanel	
//	 */
//	private JPanel getClmSettingsPanel() {
//		if (loggingSettingsPanel == null) {
//			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
//			gridBagConstraints23.gridx = 0;
//			gridBagConstraints23.fill = GridBagConstraints.BOTH;
//			gridBagConstraints23.gridy = 0;
//			GridBagConstraints gridBagConstraints = new GridBagConstraints();
//			gridBagConstraints.gridx = 1;
//			gridBagConstraints.fill = GridBagConstraints.BOTH;
//			gridBagConstraints.weightx = 1.0D;
//			gridBagConstraints.weighty = 1.0D;
//			gridBagConstraints.gridy = 0;
//			loggingSettingsPanel = new JPanel();
//			loggingSettingsPanel.setLayout(new GridBagLayout());
//			loggingSettingsPanel.add(getServiceStylePanel(), gridBagConstraints);
//			loggingSettingsPanel.add(getResourceOptionsConfigPanel(), gridBagConstraints23);
//		}
//		return loggingSettingsPanel;
//	}
	
	/**
	 * This method initializes loggingSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCaGridAuthSettingsPanel() {
		if (caGridAuthSettingsPanel == null) {
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.gridx = 0;
			gridBagConstraints23.fill = GridBagConstraints.BOTH;
			gridBagConstraints23.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridy = 0;
			caGridAuthSettingsPanel = new JPanel();
			caGridAuthSettingsPanel.setLayout(new GridBagLayout());
			caGridAuthSettingsPanel.add(getServiceStylePanel(), gridBagConstraints);
			caGridAuthSettingsPanel.add(getResourceOptionsConfigPanel(), gridBagConstraints23);
		}
		return caGridAuthSettingsPanel;
	}

	
	/**
	 * This method initializes advancedSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAdvancedSettingsPanel() {
		if (advancedSettingsPanel == null) {
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.gridx = 0;
			gridBagConstraints23.fill = GridBagConstraints.BOTH;
			gridBagConstraints23.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridy = 0;
			advancedSettingsPanel = new JPanel();
			advancedSettingsPanel.setLayout(new GridBagLayout());
			advancedSettingsPanel.add(getServiceStylePanel(), gridBagConstraints);
			advancedSettingsPanel.add(getResourceOptionsConfigPanel(), gridBagConstraints23);
		}
		return advancedSettingsPanel;
	}

	/**
	 * This method initializes resourceOptionsConfigPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResourceOptionsConfigPanel() {
		if (resourceOptionsConfigPanel == null) {
			GridBagConstraints gridBagConstraints201 = new GridBagConstraints();
			gridBagConstraints201.gridwidth = 2;
			gridBagConstraints201.gridy = 2;
			gridBagConstraints201.gridx = 0;
			GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
			gridBagConstraints61.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints61.gridy = 1;
			gridBagConstraints61.gridx = 1;
			GridBagConstraints gridBagConstraints181 = new GridBagConstraints();
			gridBagConstraints181.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints181.gridy = 0;
			gridBagConstraints181.gridx = 1;
			GridBagConstraints gridBagConstraints171 = new GridBagConstraints();
			gridBagConstraints171.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints171.gridy = 1;
			gridBagConstraints171.gridx = 0;
			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints51.gridy = 0;
			gridBagConstraints51.gridx = 0;
			resourceOptionsConfigPanel = new JPanel();
			resourceOptionsConfigPanel.setLayout(new GridBagLayout());
			resourceOptionsConfigPanel.setBorder(BorderFactory.createTitledBorder(null, "Resource Framework Options", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, SdkInstallerLookAndFeel.getPanelLabelColor()));//BlueViolet-Test
			resourceOptionsConfigPanel.add(getLifetimeResource(), gridBagConstraints51);
			resourceOptionsConfigPanel.add(getPersistantResource(), gridBagConstraints171);
			resourceOptionsConfigPanel.add(getNotificationResource(), gridBagConstraints181);
			resourceOptionsConfigPanel.add(getSecureResource(), gridBagConstraints61);
			resourceOptionsConfigPanel.add(getResourceProperty(), gridBagConstraints201);
		}
		return resourceOptionsConfigPanel;
	}


	/**
	 * This method initializes lifetimeResource	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getLifetimeResource() {
		if (lifetimeResource == null) {
			lifetimeResource = new JCheckBox();
			lifetimeResource.setToolTipText("add support for the WS-Lifetime specification");
			lifetimeResource.setHorizontalAlignment(SwingConstants.LEADING);
			lifetimeResource.setSelected(false);
//			lifetimeResource.setText(IntroduceConstants.INTRODUCE_LIFETIME_RESOURCE);
			lifetimeResource.setText("LifeTimeResource Text");
			lifetimeResource.setHorizontalTextPosition(SwingConstants.TRAILING);
			lifetimeResource.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					checkResourcePropertyOptions();
				}
			});
		}
		return lifetimeResource;
	}


	/**
	 * This method initializes persistantResource	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getPersistantResource() {
		if (persistantResource == null) {
			persistantResource = new JCheckBox();
			persistantResource.setToolTipText("enables resources to save state to disk and recover from disk");
			persistantResource.setSelected(false);
//			persistantResource.setText(IntroduceConstants.INTRODUCE_PERSISTENT_RESOURCE);
			persistantResource.setText("PersistantResource Text");
			persistantResource.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					checkResourcePropertyOptions();
				}
			});
		}
		return persistantResource;
	}


	/**
	 * This method initializes notificationResource	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getNotificationResource() {
		if (notificationResource == null) {
			notificationResource = new JCheckBox();
			notificationResource.setSelected(false);
			notificationResource.setToolTipText("add support for WS-Notification");
//			notificationResource.setText(IntroduceConstants.INTRODUCE_NOTIFICATION_RESOURCE);
			notificationResource.setText("NotificationResource Text");
			notificationResource.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					checkResourcePropertyOptions();
				}
			});
		}
		return notificationResource;
	}


	/**
	 * This method initializes secureResource	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getSecureResource() {
		if (secureResource == null) {
			secureResource = new JCheckBox();
			secureResource.setSelected(false);
			secureResource.setToolTipText("enables security to be added to this resource type");
//			secureResource.setText(IntroduceConstants.INTRODUCE_SECURE_RESOURCE);
			secureResource.setText("SecureResource");
			secureResource.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					checkResourcePropertyOptions();
				}
			});
		}
		return secureResource;
	}


	/**
	 * This method initializes resourceProperty	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getResourceProperty() {
		if (resourceProperty == null) {
			resourceProperty = new JCheckBox();
			resourceProperty.setSelected(true);
			resourceProperty.setToolTipText("enables access/query/set operations for the resource properties");
			resourceProperty.setText("resource property access");
			resourceProperty.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					checkResourcePropertyOptions();
				}
			});
		}
		return resourceProperty;
	}
	
	private void checkResourcePropertyOptions() {
		
		getLifetimeResource().setEnabled(true);
		getPersistantResource().setEnabled(true);
		getNotificationResource().setEnabled(true);
		getSecureResource().setEnabled(true);
		getResourceProperty().setEnabled(false);

	}
	
	private Map<String,String> getInstallerPropsMap(){
		Map<String,String> installerPropsMap=new HashMap<String,String>();
		
		// Project properties
		installerPropsMap.put("PROJECT_NAME", getProjectNameField().getText());
		installerPropsMap.put("NAMESPACE_PREFIX", getNamespacePrefixField().getText());
		installerPropsMap.put("WEBSERVICE_NAME", getWebServiceNameField().getText());
		
		// Model properties
		installerPropsMap.put("MODEL_FILE_PATH", getModelFileField().getText());
		installerPropsMap.put("MODEL_FILE_TYPE", getModelFileTypeField().getSelectedItem().toString());
		installerPropsMap.put("LOGICAL_MODEL", getLogicalModelField().getText());
		installerPropsMap.put("DATA_MODEL", getDataModelField().getText());
		installerPropsMap.put("INCLUDE_PACKAGE", getIncludePackageField().getText());
		installerPropsMap.put("EXCLUDE_PACKAGE", getExcludePackageField().getText());
		installerPropsMap.put("EXCLUDE_NAME", getExcludeNameField().getText());
		installerPropsMap.put("EXCLUDE_NAMESPACE", getExcludeNamespaceField().getText());
		
		// Code Generation properties
		installerPropsMap.put("VALIDATE_LOGICAL_MODEL", Boolean.valueOf(validateLogicalModelCheckBox.isSelected()).toString() );
		installerPropsMap.put("VALIDATE_MODEL_MAPPING", Boolean.valueOf(validateModelMappingCheckBox.isSelected()).toString() );
		installerPropsMap.put("VALIDATE_MODEL_MAPPING", Boolean.valueOf(validateGmeTagsCheckBox.isSelected()).toString() );
		installerPropsMap.put("GENERATE_HIBERNATE_MAPPING", Boolean.valueOf(generateHibernateMappingCheckBox.isSelected()).toString() );
		installerPropsMap.put("GENERATE_BEANS", Boolean.valueOf(generateBeansCheckBox.isSelected()).toString() );
		installerPropsMap.put("GENERATE_CASTOR_MAPPING", Boolean.valueOf(generateCastorMappingCheckBox.isSelected()).toString() );
		installerPropsMap.put("GENERATE_XSD", Boolean.valueOf(generateXsdCheckBox.isSelected()).toString() );
		installerPropsMap.put("GENERATE_XSD_WITH_GME_TAGS", Boolean.valueOf(generateXsdWithGmeTagsCheckBox.isSelected()).toString() );
		installerPropsMap.put("GENERATE_XSD_WITH_PERMISSIBLE_VALUES", Boolean.valueOf(generateXsdWithPermissibleValuesCheckBox.isSelected()).toString() );
		installerPropsMap.put("GENERATE_WSDD", Boolean.valueOf(generateWsddCheckBox.isSelected()).toString() );
		installerPropsMap.put("GENERATE_HIBERNATE_VALIDATOR", Boolean.valueOf(generateHibernateValidatorCheckBox.isSelected()).toString() );
		
		// DB Connection properties
		installerPropsMap.put("USE_JNDI_BASED_CONNECTION", Boolean.valueOf(useJndiBasedConnectionCheckBox.isSelected()).toString() );
		installerPropsMap.put("DB_JNDI_URL", getDbJndiUrlField().getText());
		installerPropsMap.put("DB_CONNECTION_URL", getDbConnectionUrlField().getText());
		installerPropsMap.put("DB_USERNAME", getDbUsernameField().getText());
		installerPropsMap.put("DB_PASSWORD", getDbPasswordField().getText());
		installerPropsMap.put("DB_DRIVER", getDbDriverField().getText());
		installerPropsMap.put("DB_DIALECT", getDbDialectField().getText());
		
		// Writable API and Common Logging Module DB Connection properties
		installerPropsMap.put("ENABLE_WRITABLE_API_EXTENSION", Boolean.valueOf(enableWritableApiExtensionCheckBox.isSelected()).toString() );
		installerPropsMap.put("DATABASE_TYPE", getDatabaseTypeField().getText());
		installerPropsMap.put("IDENTITY_GENERATOR_TAG", getIdentityGeneratorTagField().getText());
		installerPropsMap.put("CADSR_CONNECTION_URL", getCaDsrConnectionUrlField().getText());
		
		installerPropsMap.put("ENABLE_COMMON_LOGGING_MODULE", Boolean.valueOf(enableCommonLoggingModuleCheckBox.isSelected()).toString() );

		installerPropsMap.put("CLM_PROJECT_NAME", getClmProjectName().getText());		
		installerPropsMap.put("CLM_DB_CONNECTION_URL", getClmDbConnectionUrlField().getText());
		installerPropsMap.put("CLM_DB_USERNAME", getClmDbUsernameField().getText());
		installerPropsMap.put("CLM_DB_PASSWORD", getClmDbPasswordField().getText());
		installerPropsMap.put("CLM_DB_DRIVER", getClmDbDriverField().getText());
		
		return installerPropsMap;
	}
} 
