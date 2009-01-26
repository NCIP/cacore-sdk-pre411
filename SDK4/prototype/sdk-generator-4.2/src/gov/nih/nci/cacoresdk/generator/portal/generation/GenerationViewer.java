package gov.nih.nci.cacoresdk.generator.portal.generation;

import gov.nih.nci.cacoresdk.generator.common.DeployPropertiesManager;
import gov.nih.nci.cacoresdk.generator.common.FileFilters;
import gov.nih.nci.cacoresdk.generator.common.LookAndFeel;
import gov.nih.nci.cacoresdk.generator.common.ObjectFactory;
import gov.nih.nci.cacoresdk.generator.common.ResourceManager;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.portal.validation.IconFeedbackPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.message.SimpleValidationMessage;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.util.ValidationUtils;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * SDK Generation Viewer
 * 
 * @author <A HREF="MAILTO:dumitrud@mail.nih.gov">Dan Dumitru</A>
 * @created November 6, 2008
 */
public class GenerationViewer extends GenerationViewerBaseComponent {
	
	private static final long serialVersionUID = 1L;
	
	DeployPropertiesManager deployPropsMgr = null;

	private static final String GENERATOR_PROPERTIES_FILE = "generator.properties";
	private static final String DEFAULT_GENERATOR_PROPERTIES_FILE = "conf/deploy.properties.template";

	private static final String SDK_INSTALL_DIR = "SDK installation directory";
	private static final String PROJECT_DIR = "Project generation directory";
	private static final String PROJECT_NAME = "Project name";
	private static final String NAMESPACE_PREFIX = "Project namespace prefix";
	private static final String WEBSERVICE_NAME = "Project Webservice name";
    
	private static final String MODEL_FILE_PATH = "Model file";
	private static final String LOGICAL_MODEL_PACKAGE_NAME = "Logical Model package name";
	private static final String DATA_MODEL_PACKAGE_NAME = "Data Model package name";
	private static final String INCLUDE_PACKAGE_REGEX = "Include package Regular Expression";
    
	private static final String DB_JNDI_URL = "Database JNDI URL";
	private static final String DB_CONNECTION_URL = "Database Connection URL";
	private static final String DB_USERNAME = "Database Username";
	private static final String DB_PASSWORD = "Database Password";
	private static final String DB_DRIVER = "Database Driver";
	private static final String DB_DIALECT = "Database Dialect";
    
	private static final String DATABASE_TYPE = "Database Type";
	private static final String IDENTITY_GENERATOR_TAG = "Identity Generator Tag";
	private static final String CADSR_CONNECTION_URL = "caDSR Connection URL";
    
	private static final String CLM_PROJECT_NAME = "CLM Project Name";
	private static final String CLM_DB_CONNECTION_URL = "CLM DB Connection URL";
	private static final String CLM_DB_USERNAME = "CLM DB Username";
	private static final String CLM_DB_PASSWORD = "CLM DB Password";
	private static final String CLM_DB_DRIVER = "CLM DB Driver";
    
	private static final String CSM_PROJECT_NAME = "CSM Project Name";
	private static final String CSM_DB_JNDI_URL = "CSM Database JNDI URL";
	private static final String CSM_DB_CONNECTION_URL = "CSM Database Connection URL";
	private static final String CSM_DB_USERNAME = "CSM Database Username";
	private static final String CSM_DB_PASSWORD = "CSM Database Password";
	private static final String CSM_DB_DRIVER = "CSM Database Driver";
	private static final String CSM_DB_DIALECT = "CSM Database Dialect";
    
	private static final String CAGRID_LOGIN_MODULE_NAME = "caGrid Login Module Name";
	private static final String CAGRID_AUTHENTICATION_SERVICE_URL = "caGrid Authentication Service URL";
	private static final String CAGRID_DORIAN_SERVICE_URL = "caGrid Dorian Service URL";
	private static final String SDK_GRID_LOGIN_SERVICE_NAME = "SDK Grid Login Service Name";
	private static final String SDK_GRID_LOGIN_SERVICE_URL = "SDK Grid Login Service URL";
    
    public static final String SERVER_URL = "Server URL";
    
    public static final String CACHE_PATH = "Cache Path";
    
    // Maps containing drop-down list configuration information
    private Map<String,String> caGridEnvironmentOptionsMap = null;
    private Map<String,String> databaseTypeOptionsMap = null;
    private Map<String,String> databaseDriverOptionsMap = null;
    private Map<String,String> databaseDialectOptionsMap = null;
    private Map<String,String> modelFileTypeOptionsMap = null;
    private Map<String,String> databaseConnectionUrlOptionsMap = null;

    // Buttons
    private JButton previousButton = null;
    private JButton nextButton = null;
    private JButton generateButton = null;
    private JButton sdkInstallDirButton = null;
    private JButton projectDirButton = null;
    private JButton modelFilePathButton = null;
    private JButton closeButton = null;

    private ValidationResultModel validationModel = new DefaultValidationResultModel();

	/*
	 * caCore SDK Panel definitions
	 */
	private JTabbedPane mainTabbedPane = null;
    private JPanel mainPanel = null;
    private JPanel buttonPanel = null;
    
    // Panel indexes - used to enable/disable entire tab pane
	private int LOGGING_PANE_INDEX = 5;
	private int CSM_PANE_INDEX = 7;
	private int CAGRID_AUTH_PANE_INDEX = 8;

	// Primary
	private JPanel projectSettingsPanel = null;
	private JPanel modelSettingsPanel = null;
	private JPanel codeGenSettingsPanel = null;
	private JPanel ormCodeGenSettingsSubPanel = null;
	private JPanel xsdCodeGenSettingsSubPanel = null;
	private JPanel dbConnectionSettingsPanel = null;
	private JPanel dbConnectionJndiSettingsSubPanel = null;
	private JPanel dbConnectionSettingsSubPanel = null;
	private JPanel dbDialectSettingsSubPanel = null;
	
	// Writable API
	private JPanel writableApiSettingsPanel = null;
	private JPanel writableApiSettingsSubPanel = null;
	
	// Common Logging (CLM) 
	private JPanel clmSettingsPanel = null;
	private JPanel clmSettingsSubPanel = null;
	
	// Security
	private JPanel securitySettingsPanel = null;
	private JPanel securitySettingsSubPanel = null;
	private JPanel csmDbConnectionSettingsPanel = null;
	private JPanel csmDbConnectionJndiSettingsSubPanel = null;
	private JPanel csmDbConnectionSettingsSubPanel = null;
	private JPanel csmDbDialectSettingsSubPanel = null;
	private JPanel caGridAuthSettingsPanel = null;
	private JPanel caGridAuthLoginModuleSettingsSubPanel = null;

	// App Server
	private JPanel appServerSettingsPanel = null;

	// Advanced
	private JPanel advancedSettingsPanel = null;
	
	/*	
	 * caCore SDK Component definitions
	 */
    
    // Project Settings Panel Label Definitions
    private JLabel sdkInstallDirLabel = null;
    private JLabel projectDirLabel = null;
    private JLabel projectNameLabel = null;
    private JLabel namespacePrefixLabel = null;
    private JLabel webserviceNameLabel = null;
    
	//Project Settings Panel Component Definitions
	private JTextField sdkInstallDirField = null;
	private JTextField projectDirField = null;
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
    
    // CLM (Logging) Settings Panel Label Definitions
    private JLabel clmProjectNameLabel = null;
    private JLabel clmDbConnectionUrlLabel = null;
    private JLabel clmDbUsernameLabel = null;
    private JLabel clmDbPasswordLabel = null;
    private JLabel clmDbDriverLabel = null;
    
	//Writable API Settings Panel Component Definitions
    private JCheckBox  enableWritableApiExtensionCheckBox = null;
    private JComboBox databaseTypeComboBox = null;
    private JTextField identityGeneratorTagField = null;
    private JTextField caDsrConnectionUrlField = null;
    private JCheckBox  enableCommonLoggingModuleCheckBox = null;
    
    private JTextField clmProjectName = null;
    private JTextField clmDbConnectionUrlField = null;
    private JTextField clmDbUsernameField = null;
    private JTextField clmDbPasswordField = null;
    private JTextField clmDbDriverField = null;
    
    //Security Settings Panel Label Definitions
    private JLabel enableSecurityLabel = null;
    private JLabel enableInstanceLevelSecurityLabel = null;
    private JLabel enableAttributeLevelSecurityLabel = null;
    private JLabel csmProjectNameLabel = null;
    private JLabel cacheProtectionElementsLabel = null;
    
	//Security Settings Panel Component Definitions
    private JCheckBox  enableSecurityCheckBox = null;
    private JCheckBox  enableInstanceLevelSecurityCheckBox = null;
    private JCheckBox  enableAttributeLevelSecurityCheckBox = null;
    private JTextField csmProjectNameField = null;
    private JCheckBox  cacheProtectionElementsCheckBox = null;

    //CSM DB Connection Settings Panel Label Definitions
    private JLabel csmUseDbConnectionSettingsLabel = null;
    private JLabel csmUseJndiBasedConnectionLabel = null;
    private JLabel csmDbJndiUrlLabel = null;
    private JLabel csmDbConnectionUrlLabel = null;
    private JLabel csmDbUsernameLabel = null;
    private JLabel csmDbPasswordLabel = null;
    private JLabel csmDbDriverLabel = null;
    private JLabel csmDbDialectLabel = null;
    
	//CSM DB Connection Settings Panel Component Definitions
    private JCheckBox  csmUseDbConnectionSettingsCheckBox = null;
    private JCheckBox  csmUseJndiBasedConnectionCheckBox = null;
    private JTextField csmDbJndiUrlField = null;
    private JTextField csmDbConnectionUrlField = null;
    private JTextField csmDbUsernameField = null;
    private JTextField csmDbPasswordField = null;
    private JTextField csmDbDriverField = null;
    private JTextField csmDbDialectField = null;
    
    //caGRID Authentication Settings Panel Label Definitions
    private JLabel enableCaGridLoginModuleLabel = null;
    //private JLabel caGridAuthLoginModuleLabel = null;
    private JLabel caGridEnvironmentLabel = null;
    private JLabel caGridLoginModuleNameLabel = null;
    private JLabel caGridAuthSvcUrlLabel = null;
    private JLabel caGridDorianSvcUrlLabel = null;
    private JLabel sdkGridLoginSvcNameLabel = null;
    private JLabel sdkGridLoginSvcUrlLabel = null;
    
	//caGRID Authentication Settings Panel Component Definitions
    private JCheckBox enableCaGridLoginModuleCheckBox = null;
    //private JComboBox  caGridAuthLoginModuleComboBox = null;
    private JComboBox  caGridEnvironmentComboBox = null;
    private JTextField caGridLoginModuleNameField = null;
    private JTextField caGridAuthSvcUrlField = null;
    private JTextField caGridDorianSvcUrlField = null;
    private JTextField sdkGridLoginSvcNameField = null;
    private JTextField sdkGridLoginSvcUrlField = null;
    	
    //Application Server Settings Panel Label Panel Label Definitions
    private JLabel serverTypeLabel = null;
    private JLabel serverUrlLabel = null;
    
	//Application Server Settings Panel Label Panel Component Definitions
    private JComboBox   serverTypeComboBox = null;
    private JTextField  serverUrlField = null;
    
    //Advanced Settings Panel Label Panel Label Definitions
    private JLabel cachePathLabel = null;
    
	//Advanced Settings Panel Label Panel Component Definitions
    private JTextField  cachePathField = null;
    
    public GenerationViewer() {
        super();
        initialize();
    }


    /**
     * This method initializes this Viewer
     */
    private void initialize() {

    	//Request Project Directory value up front.  Used to determine whether
        //to load deploy settings from either generator template or existing project 
        //properties file
        String projectDirPath=null;

		try {
			projectDirPath = ResourceManager.promptDir("","Select the Project Generation Directory");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
        
        //initialize properties from existing project properties file, if it exists
        File projectDir = new File(projectDirPath);
        if (projectDir.exists()){
        	String generatorPropsPath = projectDirPath+File.separator+GENERATOR_PROPERTIES_FILE;
        	
        	File generatorProps = new File(generatorPropsPath);
        	if (generatorProps.exists())
        		deployPropsMgr = new DeployPropertiesManager(generatorPropsPath);
        } 
        
        //if project properties file does not exist, use default generator template properties
        if (deployPropsMgr==null){
        	deployPropsMgr = new DeployPropertiesManager(DEFAULT_GENERATOR_PROPERTIES_FILE);
        }
        
        initDropDownMaps();
        
        this.setContentPane(getMainPanel());
        this.setFrameIcon(LookAndFeel.getGenerateApplicationIcon());
        this.setTitle("Generate an SDK Application");

        initValidation();
        
        getProjectDirField().setText(projectDirPath);
    }


    private void initValidation() {
    	//Project
        ValidationComponentUtils.setMessageKey(getSdkInstallDirField(), SDK_INSTALL_DIR);
        ValidationComponentUtils.setMandatory(getSdkInstallDirField(), true);
        ValidationComponentUtils.setMessageKey(getProjectDirField(), PROJECT_DIR);
        ValidationComponentUtils.setMandatory(getProjectDirField(), true);
        ValidationComponentUtils.setMessageKey(getProjectNameField(), PROJECT_NAME);
        ValidationComponentUtils.setMandatory(getProjectNameField(), true);
        ValidationComponentUtils.setMessageKey(getNamespacePrefixField(), NAMESPACE_PREFIX);
        ValidationComponentUtils.setMandatory(getNamespacePrefixField(), true);
        ValidationComponentUtils.setMessageKey(getWebServiceNameField(), WEBSERVICE_NAME);
        ValidationComponentUtils.setMandatory(getWebServiceNameField(), true);
        
        //Model
        ValidationComponentUtils.setMessageKey(getModelFileField(), MODEL_FILE_PATH);
        ValidationComponentUtils.setMandatory(getModelFileField(), true);
        ValidationComponentUtils.setMessageKey(getLogicalModelField(), LOGICAL_MODEL_PACKAGE_NAME);
        ValidationComponentUtils.setMandatory(getLogicalModelField(), true);
        ValidationComponentUtils.setMessageKey(getDataModelField(), DATA_MODEL_PACKAGE_NAME);
        ValidationComponentUtils.setMandatory(getDataModelField(), true);
        ValidationComponentUtils.setMessageKey(getIncludePackageField(), INCLUDE_PACKAGE_REGEX);
        ValidationComponentUtils.setMandatory(getIncludePackageField(), true);
        
        //DB Connection
        ValidationComponentUtils.setMessageKey(getDatabaseTypeComboBox(), DATABASE_TYPE);
        ValidationComponentUtils.setMandatory(getDatabaseTypeComboBox(), true);
        ValidationComponentUtils.setMessageKey(getDbJndiUrlField(), DB_JNDI_URL);
        ValidationComponentUtils.setMandatory(getDbJndiUrlField(), true);
        ValidationComponentUtils.setMessageKey(getDbConnectionUrlField(), DB_CONNECTION_URL);
        ValidationComponentUtils.setMandatory(getDbConnectionUrlField(), true);
        ValidationComponentUtils.setMessageKey(getDbUsernameField(), DB_USERNAME);
        ValidationComponentUtils.setMandatory(getDbUsernameField(), true);
        ValidationComponentUtils.setMessageKey(getDbPasswordField(), DB_PASSWORD);
        ValidationComponentUtils.setMandatory(getDbPasswordField(), true);
        ValidationComponentUtils.setMessageKey(getDbDriverField(), DB_DRIVER);
        ValidationComponentUtils.setMandatory(getDbDriverField(), true);
        ValidationComponentUtils.setMessageKey(getDbDialectField(), DB_DIALECT);
        ValidationComponentUtils.setMandatory(getDbDialectField(), true);
        
        //Writable API
        ValidationComponentUtils.setMessageKey(getIdentityGeneratorTagField(), IDENTITY_GENERATOR_TAG);
        ValidationComponentUtils.setMandatory(getIdentityGeneratorTagField(), true);   
        ValidationComponentUtils.setMessageKey(getCaDsrConnectionUrlField(), CADSR_CONNECTION_URL);
        ValidationComponentUtils.setMandatory(getCaDsrConnectionUrlField(), true);
        
		// Common Logging Module DB Connection
        ValidationComponentUtils.setMessageKey(getClmProjectName(), CLM_PROJECT_NAME);
        ValidationComponentUtils.setMandatory(getClmProjectName(), true);
        ValidationComponentUtils.setMessageKey(getClmDbConnectionUrlField(), CLM_DB_CONNECTION_URL);
        ValidationComponentUtils.setMandatory(getClmDbConnectionUrlField(), true);
        ValidationComponentUtils.setMessageKey(getClmDbUsernameField(), CLM_DB_USERNAME);
        ValidationComponentUtils.setMandatory(getClmDbUsernameField(), true);
        ValidationComponentUtils.setMessageKey(getClmDbPasswordField(), CLM_DB_PASSWORD);
        ValidationComponentUtils.setMandatory(getClmDbPasswordField(), true);
        ValidationComponentUtils.setMessageKey(getClmDbDriverField(), CLM_DB_DRIVER);
        ValidationComponentUtils.setMandatory(getClmDbDriverField(), true);
		
        //Security
        ValidationComponentUtils.setMessageKey(getCsmProjectNameField(), CSM_PROJECT_NAME);
        ValidationComponentUtils.setMandatory(getCsmProjectNameField(), true);
        
        //CSM DB
        ValidationComponentUtils.setMessageKey(getCsmDbJndiUrlField(), CSM_DB_JNDI_URL);
        ValidationComponentUtils.setMandatory(getCsmDbJndiUrlField(), true);
        ValidationComponentUtils.setMessageKey(getCsmDbConnectionUrlField(), CSM_DB_CONNECTION_URL);
        ValidationComponentUtils.setMandatory(getCsmDbConnectionUrlField(), true);
        ValidationComponentUtils.setMessageKey(getCsmDbUsernameField(), CSM_DB_USERNAME);
        ValidationComponentUtils.setMandatory(getCsmDbUsernameField(), true);
        ValidationComponentUtils.setMessageKey(getCsmDbPasswordField(), CSM_DB_PASSWORD);
        ValidationComponentUtils.setMandatory(getCsmDbPasswordField(), true);
        ValidationComponentUtils.setMessageKey(getCsmDbDriverField(), CSM_DB_DRIVER);
        ValidationComponentUtils.setMandatory(getCsmDbDriverField(), true);
        ValidationComponentUtils.setMessageKey(getCsmDbDialectField(), CSM_DB_DIALECT);
        ValidationComponentUtils.setMandatory(getCsmDbDialectField(), true);
        
        // caGrid Auth
        ValidationComponentUtils.setMessageKey(getCaGridLoginModuleNameField(), CAGRID_LOGIN_MODULE_NAME);
        ValidationComponentUtils.setMandatory(getCaGridLoginModuleNameField(), true);
        ValidationComponentUtils.setMessageKey(getCaGridAuthSvcUrlField(), CAGRID_AUTHENTICATION_SERVICE_URL);
        ValidationComponentUtils.setMandatory(getCaGridAuthSvcUrlField(), true);
        ValidationComponentUtils.setMessageKey(getCaGridDorianSvcUrlField(), CAGRID_DORIAN_SERVICE_URL);
        ValidationComponentUtils.setMandatory(getCaGridDorianSvcUrlField(), true);
        ValidationComponentUtils.setMessageKey(getSdkGridLoginSvcNameField(), SDK_GRID_LOGIN_SERVICE_NAME);
        ValidationComponentUtils.setMandatory(getSdkGridLoginSvcNameField(), true);
        ValidationComponentUtils.setMessageKey(getSdkGridLoginSvcUrlField(), SDK_GRID_LOGIN_SERVICE_URL);
        ValidationComponentUtils.setMandatory(getSdkGridLoginSvcUrlField(), true);
        
        //Application Server
        ValidationComponentUtils.setMessageKey(getServerUrlField(), SERVER_URL);
        ValidationComponentUtils.setMandatory(getServerUrlField(), true);
        
        //Advanced
        ValidationComponentUtils.setMessageKey(getCachePathField(), CACHE_PATH);
        ValidationComponentUtils.setMandatory(getCachePathField(), true);

        toggleXsdFields();
        toggleDbConnectionFields();
        toggleWritableApiFields();
        toggleSecurityFields();
        syncDbCsmDbFields();
        toggleCsmDbConnectionFields();
        toggleCaGridLoginFields();  
        toggleModelFileType();
        
        validateInput();
        
        updateComponentTreeSeverity();
    }
    
    private void initDropDownMaps() {
    	
		ObjectFactory.initialize("drop-down-config.xml");
		
    	//Model File Type Drop-down
		try {
			modelFileTypeOptionsMap = (Map<String,String>)ObjectFactory.getObject("modelFileTypeInfo");
		} catch (Exception e) {
			System.out.println("ERROR:  Unable to load the model file type drop-down information");
			e.printStackTrace();
		}
		
    	//caGrid Environment Options Drop-down
		try {
			caGridEnvironmentOptionsMap = (Map<String,String>)ObjectFactory.getObject("caGridEnvironmentInfo");
		} catch (Exception e) {
			System.out.println("ERROR:  Unable to load the caGrid Environment drop-down information");
			e.printStackTrace();
		}
		
		//Database Type Options Drop-down
		try {
			databaseTypeOptionsMap = (Map<String,String>)ObjectFactory.getObject("databaseTypeInfo");
		} catch (Exception e) {
			System.out.println("ERROR:  Unable to load the Database Type drop-down information");
			e.printStackTrace();
		}
		
		//Database Connection URL Options Drop-down
		try {
			databaseConnectionUrlOptionsMap = (Map<String,String>)ObjectFactory.getObject("databaseConnectionUrlInfo");
		} catch (Exception e) {
			System.out.println("ERROR:  Unable to load the Database Connection URL pattern drop-down information");
			e.printStackTrace();
		}
		
		
		//Database Driver Options Drop-down
		try {
			databaseDriverOptionsMap = (Map<String,String>)ObjectFactory.getObject("databaseDriverInfo");
		} catch (Exception e) {
			System.out.println("ERROR:  Unable to load the Database Driver option information");
			e.printStackTrace();
		}
		
		//Database Dialect Options Drop-down
		try {
			databaseDialectOptionsMap = (Map<String,String>)ObjectFactory.getObject("databaseDialectInfo");
		} catch (Exception e) {
			System.out.println("ERROR:  Unable to load the Database Dialect option information");
			e.printStackTrace();
		}
		
    }

    private final class FocusChangeHandler implements FocusListener {

        public void focusGained(FocusEvent e) {
            update();
        }

        public void focusLost(FocusEvent e) {
            update();
        }

        private void update() {
            validateInput();
        }
    }


    private void validateInput() {

    	ValidationResult result = new ValidationResult();

    	//Panel Settings Validation
    	if (!ValidationUtils.isNotBlank(this.getProjectDirField().getText())) {
    		result.add(new SimpleValidationMessage(PROJECT_DIR + " must not be blank.", Severity.ERROR, PROJECT_DIR));
    	} else {
    		File file = new File(this.getProjectDirField().getText());
    		if(file.exists()){
    			result.add(new SimpleValidationMessage(PROJECT_DIR + " already exists.  The output directory will be overwritten.", Severity.WARNING, PROJECT_DIR));
    		}
    	}
    	
    	if (!ValidationUtils.isNotBlank(this.getSdkInstallDirField().getText())) {
    		result.add(new SimpleValidationMessage(SDK_INSTALL_DIR + " must not be blank.", Severity.ERROR, SDK_INSTALL_DIR));
    	} else {
    		File file = new File(this.getSdkInstallDirField().getText());
    		if(!file.exists()){
    			result.add(new SimpleValidationMessage(SDK_INSTALL_DIR + " does not exist.  Please select the Home directory of an existing SDK Installation.", Severity.ERROR, SDK_INSTALL_DIR));
    		}
    	}
    	
    	if (ValidationUtils.isNotBlank(this.getProjectDirField().getText()) 
    			&& ValidationUtils.isNotBlank(this.getSdkInstallDirField().getText()) &&
    			this.getProjectDirField().getText().equalsIgnoreCase(this.getSdkInstallDirField().getText())) {
    		result.add(new SimpleValidationMessage(PROJECT_DIR + " and " + SDK_INSTALL_DIR + " must be different.", Severity.ERROR, PROJECT_DIR));
    		result.add(new SimpleValidationMessage(PROJECT_DIR + " and " + SDK_INSTALL_DIR + " must be different.", Severity.ERROR, SDK_INSTALL_DIR));
    	} 

    	if (!ValidationUtils.isNotBlank(this.getProjectNameField().getText())) {
    		result.add(new SimpleValidationMessage(PROJECT_NAME + " must not be blank.", Severity.ERROR, PROJECT_NAME));
    	}

    	if (!ValidationUtils.isNotBlank(this.getNamespacePrefixField().getText())) {
    		result.add(new SimpleValidationMessage(NAMESPACE_PREFIX + " must not be blank.", Severity.ERROR, NAMESPACE_PREFIX));
    	}

    	if (!ValidationUtils.isNotBlank(this.getWebServiceNameField().getText())) {
    		result.add(new SimpleValidationMessage(WEBSERVICE_NAME + " must not be blank.", Severity.ERROR, WEBSERVICE_NAME));
    	}
    	
    	//Model Settings Validation
    	if (!ValidationUtils.isNotBlank(this.getModelFileField().getText())) {
    		result.add(new SimpleValidationMessage(MODEL_FILE_PATH + " path must not be blank.", Severity.ERROR, MODEL_FILE_PATH));
    	} else {
    		File file = new File(this.getModelFileField().getText());
    		if(!file.exists()){
    			result.add(new SimpleValidationMessage(MODEL_FILE_PATH + " does not exist.  Please select or enter a valid absolute path to the model file.", Severity.ERROR, MODEL_FILE_PATH));
    		}
    		
    		if (!this.getModelFileField().getText().endsWith("xmi") && !this.getModelFileField().getText().endsWith("uml")){
    			result.add(new SimpleValidationMessage(MODEL_FILE_PATH + " must refer to an XMI (*.xmi) or Argo UML (*.uml) file.", Severity.ERROR, MODEL_FILE_PATH));
    		}
    		
    		
    	}

    	if (!ValidationUtils.isNotBlank(this.getLogicalModelField().getText())) {
    		result.add(new SimpleValidationMessage(LOGICAL_MODEL_PACKAGE_NAME + " must not be blank.", Severity.ERROR, LOGICAL_MODEL_PACKAGE_NAME));
    	}

    	if (!ValidationUtils.isNotBlank(this.getDataModelField().getText())) {
    		result.add(new SimpleValidationMessage(DATA_MODEL_PACKAGE_NAME + " must not be blank.", Severity.ERROR, DATA_MODEL_PACKAGE_NAME));
    	}

    	if (!ValidationUtils.isNotBlank(this.getIncludePackageField().getText())) {
    		result.add(new SimpleValidationMessage(INCLUDE_PACKAGE_REGEX + " must not be blank.", Severity.ERROR, INCLUDE_PACKAGE_REGEX));
    	}

    	//DB Connection Settings Validation
    	if (!getUseJndiBasedConnectionCheckBox().isSelected()){

    		String dbConnectionUrlField = this.getDbConnectionUrlField().getText();
    		if (!ValidationUtils.isNotBlank(dbConnectionUrlField)) {
    			result.add(new SimpleValidationMessage(DB_CONNECTION_URL + " must not be blank.", Severity.ERROR, DB_CONNECTION_URL));
    		}
    		
    		if (dbConnectionUrlField.indexOf('<') > 1 || dbConnectionUrlField.indexOf('>') > 1) {
    			result.add(new SimpleValidationMessage(DB_CONNECTION_URL + " information is incomplete.  Make sure hostname, port and schema information is correct.", Severity.ERROR, DB_CONNECTION_URL));
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
    	} else {
    		if (!ValidationUtils.isNotBlank(this.getDbJndiUrlField().getText())) {
    			result.add(new SimpleValidationMessage(DB_JNDI_URL + " must not be blank.", Severity.ERROR, DB_JNDI_URL));
    		}
    	}
    	
    	//Writable API Setting Validation
    	if (getEnableWritableApiExtensionCheckBox().isSelected()){

    		if (!ValidationUtils.isNotBlank(this.getIdentityGeneratorTagField().getText())) {
    			result.add(new SimpleValidationMessage(IDENTITY_GENERATOR_TAG + " must not be blank.", Severity.ERROR, IDENTITY_GENERATOR_TAG));
    		} 

    		if (!ValidationUtils.isNotBlank(this.getCaDsrConnectionUrlField().getText())) {
    			result.add(new SimpleValidationMessage(CADSR_CONNECTION_URL + " must not be blank.", Severity.ERROR, CADSR_CONNECTION_URL));
    		} 
    		
    		//CLM Setting Validation
    		if (getEnableCommonLoggingModuleCheckBox().isSelected()){
    			if (!ValidationUtils.isNotBlank(this.getClmProjectName().getText())) {
    				result.add(new SimpleValidationMessage(CLM_PROJECT_NAME + " must not be blank.", Severity.ERROR, CLM_PROJECT_NAME));
    			}

    			String clmDbConnectionUrlField = this.getClmDbConnectionUrlField().getText();
    			if (!ValidationUtils.isNotBlank(clmDbConnectionUrlField)) {
    				result.add(new SimpleValidationMessage(CLM_DB_CONNECTION_URL + " must not be blank.", Severity.ERROR, CLM_DB_CONNECTION_URL));
    			}
    			
        		if (clmDbConnectionUrlField.indexOf('<') > 1 || clmDbConnectionUrlField.indexOf('>') > 1) {
        			result.add(new SimpleValidationMessage(CLM_DB_CONNECTION_URL + " information is incomplete.  Make sure hostname, port and schema information is correct.", Severity.ERROR, CLM_DB_CONNECTION_URL));
        		}

    			if (!ValidationUtils.isNotBlank(this.getClmDbUsernameField().getText())) {
    				result.add(new SimpleValidationMessage(CLM_DB_USERNAME + " must not be blank.", Severity.ERROR, CLM_DB_USERNAME));
    			}

    			if (!ValidationUtils.isNotBlank(this.getClmDbPasswordField().getText())) {
    				result.add(new SimpleValidationMessage(CLM_DB_PASSWORD + " must not be blank.", Severity.ERROR, CLM_DB_PASSWORD));
    			}

    			if (!ValidationUtils.isNotBlank(this.getClmDbDriverField().getText())) {
    				result.add(new SimpleValidationMessage(CLM_DB_DRIVER + " must not be blank.", Severity.ERROR, CLM_DB_DRIVER));
    			}
    		}
    	}
    	
        //Security setting Validation
    	if (getEnableSecurityCheckBox().isSelected()){
    		
			if (!ValidationUtils.isNotBlank(this.getCsmProjectNameField().getText())) {
				result.add(new SimpleValidationMessage(CSM_PROJECT_NAME + " must not be blank.", Severity.ERROR, CSM_PROJECT_NAME));
			}
    		
    		//CSM DB Connection Setting Validation
    		if (!getCsmUseJndiBasedConnectionCheckBox().isSelected() && !getCsmUseDbConnectionSettingsCheckBox().isSelected()){

    			String csmDbConnectionUrlField = this.getCsmDbConnectionUrlField().getText();
    			if (!ValidationUtils.isNotBlank(csmDbConnectionUrlField)) {
    				result.add(new SimpleValidationMessage(CSM_DB_CONNECTION_URL + " must not be blank.", Severity.ERROR, CSM_DB_CONNECTION_URL));
    			}
    			
        		if (csmDbConnectionUrlField.indexOf('<') > 1 || csmDbConnectionUrlField.indexOf('>') > 1) {
        			result.add(new SimpleValidationMessage(CSM_DB_CONNECTION_URL + " information is incomplete.  Make sure hostname, port and schema information is correct.", Severity.ERROR, CSM_DB_CONNECTION_URL));
        		}

    			if (!ValidationUtils.isNotBlank(this.getCsmDbUsernameField().getText())) {
    				result.add(new SimpleValidationMessage(CSM_DB_USERNAME + " must not be blank.", Severity.ERROR, CSM_DB_USERNAME));
    			} 

    			if (!ValidationUtils.isNotBlank(this.getCsmDbPasswordField().getText())) {
    				result.add(new SimpleValidationMessage(CSM_DB_PASSWORD + " must not be blank.", Severity.ERROR, CSM_DB_PASSWORD));
    			} 

    			if (!ValidationUtils.isNotBlank(this.getCsmDbDriverField().getText())) {
    				result.add(new SimpleValidationMessage(CSM_DB_DRIVER + " must not be blank.", Severity.ERROR, CSM_DB_DRIVER));
    			} 

    			if (!ValidationUtils.isNotBlank(this.getCsmDbDialectField().getText())) {
    				result.add(new SimpleValidationMessage(CSM_DB_DIALECT + " must not be blank.", Severity.ERROR, CSM_DB_DIALECT));
    			}
    		} else {
    			if (!ValidationUtils.isNotBlank(this.getCsmDbJndiUrlField().getText())) {
    				result.add(new SimpleValidationMessage(CSM_DB_JNDI_URL + " must not be blank.", Severity.ERROR, CSM_DB_JNDI_URL));
    			}
    		}
    		
    		//caGrid Auth Setting Validation
			if (!ValidationUtils.isNotBlank(this.getCaGridLoginModuleNameField().getText())) {
				result.add(new SimpleValidationMessage(CAGRID_LOGIN_MODULE_NAME + " must not be blank.", Severity.ERROR, CAGRID_LOGIN_MODULE_NAME));
			}

			if (!ValidationUtils.isNotBlank(this.getCaGridAuthSvcUrlField().getText())) {
				result.add(new SimpleValidationMessage(CAGRID_AUTHENTICATION_SERVICE_URL + " must not be blank.", Severity.ERROR, CAGRID_AUTHENTICATION_SERVICE_URL));
			} 

			if (!ValidationUtils.isNotBlank(this.getCaGridDorianSvcUrlField().getText())) {
				result.add(new SimpleValidationMessage(CAGRID_DORIAN_SERVICE_URL + " must not be blank.", Severity.ERROR, CAGRID_DORIAN_SERVICE_URL));
			} 

			if (!ValidationUtils.isNotBlank(this.getSdkGridLoginSvcNameField().getText())) {
				result.add(new SimpleValidationMessage(SDK_GRID_LOGIN_SERVICE_NAME + " must not be blank.", Severity.ERROR, SDK_GRID_LOGIN_SERVICE_NAME));
			} 

			if (!ValidationUtils.isNotBlank(this.getSdkGridLoginSvcUrlField().getText())) {
				result.add(new SimpleValidationMessage(SDK_GRID_LOGIN_SERVICE_URL + " must not be blank.", Severity.ERROR, SDK_GRID_LOGIN_SERVICE_URL));
			}
    	}
        
        //Application Server Components
		if (!ValidationUtils.isNotBlank(this.getServerUrlField().getText())) {
			result.add(new SimpleValidationMessage(SERVER_URL + " must not be blank.", Severity.ERROR, SERVER_URL));
		} 

		//Advanced Settings components
		if (!ValidationUtils.isNotBlank(this.getCachePathField().getText())) {
			result.add(new SimpleValidationMessage(CACHE_PATH + " must not be blank.", Severity.ERROR, CACHE_PATH));
		}

    	this.validationModel.setResult(result);

    	updateComponentTreeSeverity();
    	toggleGenerateButton();
    	togglePreviousButton();
    	toggleNextButton();
    }


    private void updateComponentTreeSeverity() {
        ValidationComponentUtils.updateComponentTreeMandatoryAndBlankBackground(this);
        ValidationComponentUtils.updateComponentTreeSeverityBackground(this, this.validationModel.getResult());
    }

    private void updateCaGridEnvironmentFields(){
	    String caGridEnv = (String)caGridEnvironmentComboBox.getSelectedItem();
	    if (!caGridEnv.equals("")){
	    	caGridAuthSvcUrlField.setText(caGridEnvironmentOptionsMap.get(caGridEnv));
	    	caGridDorianSvcUrlField.setText(caGridEnvironmentOptionsMap.get(caGridEnv));
	    }
	    caGridEnvironmentComboBox.setSelectedItem("");
	}
    
    private void updateDbFields(){
    	String dbType = (String)databaseTypeComboBox.getSelectedItem();
    	
    	dbConnectionUrlField.setText(databaseConnectionUrlOptionsMap.get(dbType));
    	dbDriverField.setText(databaseDriverOptionsMap.get(dbType));
    	dbDialectField.setText(databaseDialectOptionsMap.get(dbType));
    	
    	csmDbConnectionUrlField.setText(databaseConnectionUrlOptionsMap.get(dbType));
    	csmDbDriverField.setText(databaseDriverOptionsMap.get(dbType));
    	csmDbDialectField.setText(databaseDialectOptionsMap.get(dbType));

    }

    private void toggleGenerateButton() {
        if (this.validationModel.hasErrors()) {
            this.getGenerateButton().setEnabled(false);
        } else {
            this.getGenerateButton().setEnabled(true);
        }
    }
    
    private void togglePreviousButton() {
    	if (mainTabbedPane.getSelectedIndex() <= 0){
            this.getPreviousButton().setEnabled(false);
        } else {
            this.getPreviousButton().setEnabled(true);
        }
    }
    
    private void toggleNextButton() {
    	if (mainTabbedPane.getSelectedIndex() >= mainTabbedPane.getTabCount()-1){
            this.getNextButton().setEnabled(false);
        } else {
            this.getNextButton().setEnabled(true);
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
            gridBagConstraints1.anchor = java.awt.GridBagConstraints.CENTER;
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
            buttonPanel.add(getPreviousButton(), null);
            buttonPanel.add(getNextButton(), null);
            buttonPanel.add(getGenerateButton(), null);
            buttonPanel.add(getCloseButton(), null);
        }
        return buttonPanel;
    }

    /**
     * This method initializes the Previous jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getPreviousButton() {
        if (previousButton == null) {
        	previousButton = new JButton();
        	previousButton.setText("Previous");
        	previousButton.setIcon(LookAndFeel.getPreviousIcon());
        	previousButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    mainTabbedPane.setSelectedIndex(getPreviousIndex());
                    validateInput();
                }
            });
        }

        return previousButton;
    }
    
    /**
     * This method initializes the Previous jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getNextButton() {
        if (nextButton == null) {
        	nextButton = new JButton();
        	nextButton.setText("Next");
        	nextButton.setIcon(LookAndFeel.getNextIcon());
        	nextButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	 mainTabbedPane.setSelectedIndex(getNextIndex());
                	 validateInput();
                }
            });
        }

        return nextButton;
    }
    
    private int getPreviousIndex(){
    	int index = mainTabbedPane.getSelectedIndex();

    	while (index >= 0){
    		--index;
    		
    		if (mainTabbedPane.isEnabledAt(index))
    			return index;
    	}
    	
    	return 0;
    }
    
    private int getNextIndex(){
    	int index = mainTabbedPane.getSelectedIndex();

    	while (index <= mainTabbedPane.getTabCount()-1){
    		++index;
    		
    		if (mainTabbedPane.isEnabledAt(index))
    			return index;
    	}
    	
    	return mainTabbedPane.getTabCount() - 1;
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
            generateButton.setIcon(LookAndFeel.getGenerateApplicationIcon());
            generateButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    generateApplication(getSdkInstallDirField().getText(),getProjectDirField().getText(),getGeneratorPropsMap()); 
                }
            });
        }

        return generateButton;
    }

    /**
     * This method initializes projectNameField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getProjectNameField() {
        if (projectNameField == null) {
            projectNameField = new JTextField();
            projectNameField.setText(deployPropsMgr.getDeployPropertyValue("PROJECT_NAME"));
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
    
    
    protected void syncDbCsmDbFields() {
    	if (csmUseDbConnectionSettingsCheckBox.isSelected()){
    		csmUseJndiBasedConnectionCheckBox.setSelected(useJndiBasedConnectionCheckBox.isSelected());
    		csmDbJndiUrlField.setText(dbJndiUrlField.getText());
    		csmDbConnectionUrlField.setText(dbConnectionUrlField.getText());
    		csmDbUsernameField.setText(dbUsernameField.getText());
    		csmDbPasswordField.setText(dbPasswordField.getText());
    		csmDbDialectField.setText(dbDialectField.getText());
    	}
    }
    
    protected void toggleXsdFields() {
		if (generateXsdCheckBox.isSelected()){
			validateGmeTagsCheckBox.setEnabled(true);
			generateCastorMappingCheckBox.setEnabled(true);
			generateXsdWithGmeTagsCheckBox.setEnabled(true);
			generateXsdWithPermissibleValuesCheckBox.setEnabled(true);
		} else{
			validateGmeTagsCheckBox.setEnabled(false);
			generateCastorMappingCheckBox.setEnabled(false);
			generateXsdWithGmeTagsCheckBox.setEnabled(false);
			generateXsdWithPermissibleValuesCheckBox.setEnabled(false);
		}
    }

    protected void toggleDbConnectionFields() {
		if (useJndiBasedConnectionCheckBox.isSelected()){
			dbJndiUrlField.setEnabled(true);
			
			dbConnectionUrlField.setEnabled(false);
			dbUsernameField.setEnabled(false);
			dbPasswordField.setEnabled(false);
			//dbDriverField.setEnabled(false);
			//dbDialectField.setEnabled(false);
		} else{
			dbJndiUrlField.setEnabled(false);
			
			dbConnectionUrlField.setEnabled(true);
			dbUsernameField.setEnabled(true);
			dbPasswordField.setEnabled(true);
			//dbDriverField.setEnabled(true);
			//dbDialectField.setEnabled(true);
		}
    }
    
    protected void toggleWritableApiFields() {
		if (enableWritableApiExtensionCheckBox.isSelected()){
			identityGeneratorTagField.setEnabled(true);
			caDsrConnectionUrlField.setEnabled(true);
			
			mainTabbedPane.setEnabledAt(LOGGING_PANE_INDEX, true); // CLM Panel
			enableCommonLoggingModuleCheckBox.setEnabled(true);
			
			if (enableCommonLoggingModuleCheckBox.isSelected()){
				clmProjectName.setEnabled(true);
				clmDbConnectionUrlField.setEnabled(true);
				clmDbUsernameField.setEnabled(true);
				clmDbPasswordField.setEnabled(true);
				//clmDbDriverField.setEnabled(true); //currently not editable
			} else {
				clmProjectName.setEnabled(false);
				clmDbConnectionUrlField.setEnabled(false);
				clmDbUsernameField.setEnabled(false);
				clmDbPasswordField.setEnabled(false);
				//clmDbDriverField.setEnabled(false); //currently not editable
			}
		} else{
			identityGeneratorTagField.setEnabled(false);
			caDsrConnectionUrlField.setEnabled(false);
			
			mainTabbedPane.setEnabledAt(LOGGING_PANE_INDEX, false); // CLM Panel
			
			enableCommonLoggingModuleCheckBox.setEnabled(false);
			clmProjectName.setEnabled(false);
			clmDbConnectionUrlField.setEnabled(false);
			clmDbUsernameField.setEnabled(false);
			clmDbPasswordField.setEnabled(false);
			//clmDbDriverField.setEnabled(false); //currently not editable
		}
    }
    
    protected void toggleSecurityFields() {
		if (enableSecurityCheckBox.isSelected()){
		    enableInstanceLevelSecurityCheckBox.setEnabled(true);
		    enableAttributeLevelSecurityCheckBox.setEnabled(true);
		    csmProjectNameField.setEnabled(true);
		    cacheProtectionElementsCheckBox.setEnabled(true);

		    mainTabbedPane.setEnabledAt(CSM_PANE_INDEX, true); // CSM Panel
		    mainTabbedPane.setEnabledAt(CAGRID_AUTH_PANE_INDEX, true); // caGrid Auth Panel
		} else{
		    enableInstanceLevelSecurityCheckBox.setEnabled(false);
		    enableAttributeLevelSecurityCheckBox.setEnabled(false);
		    csmProjectNameField.setEnabled(false);
		    cacheProtectionElementsCheckBox.setEnabled(false);

		    mainTabbedPane.setEnabledAt(CSM_PANE_INDEX, false); // CSM Panel
		    mainTabbedPane.setEnabledAt(CAGRID_AUTH_PANE_INDEX, false); // caGrid Auth Panel
		}
    }
    
    protected void toggleCsmDbConnectionFields() {
    	if (csmUseDbConnectionSettingsCheckBox.isSelected()){
    		csmUseJndiBasedConnectionCheckBox.setEnabled(false);
			csmDbJndiUrlField.setEnabled(false);

			csmDbConnectionUrlField.setEnabled(false);
			csmDbUsernameField.setEnabled(false);
			csmDbPasswordField.setEnabled(false);
    	} else {
    		csmUseJndiBasedConnectionCheckBox.setEnabled(true);
    		if (csmUseJndiBasedConnectionCheckBox.isSelected()){
    			csmDbJndiUrlField.setEnabled(true);

    			csmDbConnectionUrlField.setEnabled(false);
    			csmDbUsernameField.setEnabled(false);
    			csmDbPasswordField.setEnabled(false);
    			//csmDbDriverField.setEnabled(false);
    			//csmDbDialectField.setEnabled(false);
    		} else{
    			csmDbJndiUrlField.setEnabled(false);

    			csmDbConnectionUrlField.setEnabled(true);
    			csmDbUsernameField.setEnabled(true);
    			csmDbPasswordField.setEnabled(true);
    			//csmDbDriverField.setEnabled(true);
    			//csmDbDialectField.setEnabled(true);
    		}
    	}
    }
    
    protected void toggleCaGridLoginFields() {
		if (enableCaGridLoginModuleCheckBox.isSelected()){
			caGridEnvironmentComboBox.setEnabled(true);
			caGridAuthSvcUrlField.setEnabled(true);
			sdkGridLoginSvcNameField.setEnabled(true);
			sdkGridLoginSvcUrlField.setEnabled(true);
			caGridLoginModuleNameField.setEnabled(true);
			caGridDorianSvcUrlField.setEnabled(true);
		} else{
			caGridEnvironmentComboBox.setEnabled(false);
			caGridAuthSvcUrlField.setEnabled(false);
			sdkGridLoginSvcNameField.setEnabled(false);
			sdkGridLoginSvcUrlField.setEnabled(false);
			caGridLoginModuleNameField.setEnabled(false);
			caGridDorianSvcUrlField.setEnabled(false);
		}
    }
    
    protected void toggleModelFileType() {
    	if (getModelFileField().getText().endsWith(".xmi")){
    		getModelFileTypeField().setSelectedItem("Enterprise Architect");
    	} else if (getModelFileField().getText().endsWith(".uml")){
    		getModelFileTypeField().setSelectedItem("ArgoUML");
    	} else {
    		getModelFileTypeField().setSelectedItem("");
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

            sdkInstallDirField.setText(deployPropsMgr.getDeployPropertyValue("SDK_INSTALL_DIR"));
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
     * This method initializes jTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getProjectDirField() {
        if (projectDirField == null) {
        	projectDirField = new JTextField();
        	projectDirField.setText("");
        	projectDirField.getDocument().addDocumentListener(new DocumentListener() {
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
        	projectDirField.addFocusListener(new FocusChangeHandler());
        }
        return projectDirField;
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
            sdkInstallDirButton.setIcon(LookAndFeel.getBrowseIcon());
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
    private JButton getProjectDirButton() {
        if (projectDirButton == null) {
        	projectDirButton = new JButton();
        	projectDirButton.setText("Browse");
        	projectDirButton.setIcon(LookAndFeel.getBrowseIcon());
        	projectDirButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        String previous = getProjectDirField().getText();
                        String location = ResourceManager.promptDir(previous);
                        if (location != null && location.length() > 0) {
                        	getProjectDirField().setText(location);
                        } else {
                        	getProjectDirField().setText(previous);
                        }
                        validateInput();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        return projectDirButton;
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
        	modelFilePathButton.setIcon(LookAndFeel.getBrowseIcon());
        	modelFilePathButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        String previous = getModelFileField().getText();
                        String location = ResourceManager.promptFile(previous, FileFilters.XMI_UML_FILTER);
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
            namespacePrefixField.setText(deployPropsMgr.getDeployPropertyValue("NAMESPACE_PREFIX"));
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
            webserviceNameField.setText(deployPropsMgr.getDeployPropertyValue("WEBSERVICE_NAME"));

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
        	
        	modelFileField.setText(deployPropsMgr.getDeployPropertyValue("MODEL_FILE_PATH"));

        	modelFileField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    toggleModelFileType();
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    toggleModelFileType();
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    toggleModelFileType();
                    validateInput();
                }
            });
        	modelFileField.addFocusListener(new FocusChangeHandler());
        	
        	modelFileField.setEditable(false); // Only allow changes via the Model File Button
        	modelFileField.setEnabled(false);
        }
        return modelFileField;
    }
    
    /**
     * This method initializes Model File Type Combo Box
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getModelFileTypeField() {
        if (modelFileTypeComboBox == null) {
        	modelFileTypeComboBox = new JComboBox();
        	
        	if (modelFileTypeOptionsMap!=null){
            	Iterator<String> iter = modelFileTypeOptionsMap.keySet().iterator();
            	
            	while (iter.hasNext()){
            		modelFileTypeComboBox.addItem((String)iter.next());
            	}
        	}

            modelFileTypeComboBox.setSelectedItem(deployPropsMgr.getDeployPropertyValue("MODEL_FILE_TYPE"));
 
        	modelFileTypeComboBox.addFocusListener(new FocusChangeHandler());
        	
        	modelFileTypeComboBox.setEnabled(false);
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
        	logicalModelField.setText(deployPropsMgr.getDeployPropertyValue("LOGICAL_MODEL"));
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
        	dataModelField.setText(deployPropsMgr.getDeployPropertyValue("DATA_MODEL"));
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
        	includePackageField.setText(deployPropsMgr.getDeployPropertyValue("INCLUDE_PACKAGE"));
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
        	excludePackageField.setText(deployPropsMgr.getDeployPropertyValue("EXCLUDE_PACKAGE"));
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
        	excludeNameField.setText(deployPropsMgr.getDeployPropertyValue("EXCLUDE_NAME"));
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
        	excludeNamespaceField.setText(deployPropsMgr.getDeployPropertyValue("EXCLUDE_NAMESPACE"));
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
        	validateLogicalModelCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("VALIDATE_LOGICAL_MODEL")));
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
        	validateModelMappingCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("VALIDATE_MODEL_MAPPING")));
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
        	validateGmeTagsCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("VALIDATE_GME_TAGS")));
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
        	generateBeansCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("GENERATE_BEANS")));
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
        	generateHibernateMappingCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("GENERATE_HIBERNATE_MAPPING")));
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
        	generateCastorMappingCheckBox.setToolTipText("Generate Castor Mapping files?");
        	generateCastorMappingCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	generateCastorMappingCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("GENERATE_CASTOR_MAPPING")));
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
        	generateXsdCheckBox.setToolTipText("Generate XSD files?");
        	generateXsdCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	generateXsdCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("GENERATE_XSD")));
        	generateXsdCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	generateXsdCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					toggleXsdFields();
					validateInput();
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
        	generateXsdWithGmeTagsCheckBox.setToolTipText("Generate XSD files with GME Tags?");
        	generateXsdWithGmeTagsCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	generateXsdWithGmeTagsCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("GENERATE_XSD_WITH_GME_TAGS")));
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
        	generateXsdWithPermissibleValuesCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("GENERATE_XSD_WITH_PERMISSIBLE_VALUES")));
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
        	generateWsddCheckBox.setToolTipText("Generate Web Service Deployment Descriptor (Apache Axis library) files?");
        	generateWsddCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	generateWsddCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("GENERATE_WSDD")));
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
        	generateHibernateValidatorCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("GENERATE_HIBERNATE_VALIDATOR")));
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
        	useJndiBasedConnectionCheckBox.setToolTipText("Use a JNDI-based Connection?");
        	useJndiBasedConnectionCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	useJndiBasedConnectionCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("USE_JNDI_BASED_CONNECTION")));
        	useJndiBasedConnectionCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	useJndiBasedConnectionCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					syncDbCsmDbFields();
					toggleDbConnectionFields();
					validateInput();
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
        	dbJndiUrlField.setText(deployPropsMgr.getDeployPropertyValue("DB_JNDI_URL"));
        	dbJndiUrlField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                	syncDbCsmDbFields();
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                	syncDbCsmDbFields();
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                	syncDbCsmDbFields();
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
        	dbConnectionUrlField.setText(deployPropsMgr.getDeployPropertyValue("DB_CONNECTION_URL"));
        	dbConnectionUrlField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                	syncDbCsmDbFields();
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                	syncDbCsmDbFields();
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                	syncDbCsmDbFields();
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
        	dbUsernameField.setText(deployPropsMgr.getDeployPropertyValue("DB_USERNAME"));
        	dbUsernameField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                	syncDbCsmDbFields();
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                	syncDbCsmDbFields();
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                	syncDbCsmDbFields();
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
        	dbPasswordField.setText(deployPropsMgr.getDeployPropertyValue("DB_PASSWORD"));
        	dbPasswordField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                	syncDbCsmDbFields();
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                	syncDbCsmDbFields();
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                	syncDbCsmDbFields();
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
        	dbDriverField.setText(deployPropsMgr.getDeployPropertyValue("DB_DRIVER"));
        	
        	dbDriverField.setEditable(false);
        	dbDriverField.setEnabled(false);
        	
        	dbDriverField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                	syncDbCsmDbFields();
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                	syncDbCsmDbFields();
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                	syncDbCsmDbFields();
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
        	dbDialectField.setText(deployPropsMgr.getDeployPropertyValue("DB_DIALECT"));
        	
        	dbDialectField.setEditable(false);
        	dbDialectField.setEnabled(false);
        	
        	dbDialectField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                	syncDbCsmDbFields();
                    validateInput();
                }

                public void removeUpdate(DocumentEvent e) {
                	syncDbCsmDbFields();
                    validateInput();
                }

                public void insertUpdate(DocumentEvent e) {
                	syncDbCsmDbFields();
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
        	enableWritableApiExtensionCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("ENABLE_WRITABLE_API_EXTENSION")));
        	enableWritableApiExtensionCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	enableWritableApiExtensionCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					toggleWritableApiFields();
					validateInput();
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
    private JComboBox getDatabaseTypeComboBox() {
        if (databaseTypeComboBox == null) {
        	databaseTypeComboBox = new JComboBox();
        	
        	if (databaseTypeOptionsMap!=null){
            	Iterator<String> iter = databaseTypeOptionsMap.keySet().iterator();
            	
            	while (iter.hasNext()){
            		databaseTypeComboBox.addItem((String)iter.next());
            	}
        	}
        	
        	if ("mysql".equalsIgnoreCase(deployPropsMgr.getDeployPropertyValue("DATABASE_TYPE")))
        		databaseTypeComboBox.setSelectedItem("MySQL");
        	else
        		databaseTypeComboBox.setSelectedItem("Oracle");
        	  		
        	databaseTypeComboBox.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                    	updateDbFields();
                        validateInput();
                    }
                });
        	
        	databaseTypeComboBox.addFocusListener(new FocusChangeHandler());
        }
        return databaseTypeComboBox;
    }
    
    /**
     * This method initializes the Writable API Identity Generator Tag Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getIdentityGeneratorTagField() {
        if (identityGeneratorTagField == null) {
        	identityGeneratorTagField = new JTextField();
        	identityGeneratorTagField.setText(deployPropsMgr.getDeployPropertyValue("IDENTITY_GENERATOR_TAG"));
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
        	caDsrConnectionUrlField.setText(deployPropsMgr.getDeployPropertyValue("CADSR_CONNECTION_URL"));
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
        	enableCommonLoggingModuleCheckBox.setToolTipText("Enable Logging?");
        	enableCommonLoggingModuleCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	enableCommonLoggingModuleCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("ENABLE_WRITABLE_API_EXTENSION")));
        	enableCommonLoggingModuleCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	enableCommonLoggingModuleCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					toggleWritableApiFields();
					validateInput();
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
        	clmProjectName.setText(deployPropsMgr.getDeployPropertyValue("CLM_PROJECT_NAME"));
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
        	clmDbConnectionUrlField.setText(deployPropsMgr.getDeployPropertyValue("CLM_DB_CONNECTION_URL"));
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
        	clmDbUsernameField.setText(deployPropsMgr.getDeployPropertyValue("CLM_DB_USERNAME"));
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
        	clmDbPasswordField.setText(deployPropsMgr.getDeployPropertyValue("CLM_DB_PASSWORD"));
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
        	clmDbDriverField.setText(deployPropsMgr.getDeployPropertyValue("CLM_DB_DRIVER"));

        	// Fix DB Driver Field to MySql value, as no other option is currently available.
        	clmDbDriverField.setEditable(false);
        	clmDbDriverField.setEnabled(false);
        	
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
    
    /**
     * This method initializes the Enable Common Logging Module CheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getEnableSecurityCheckBox() {
        if (enableSecurityCheckBox == null) {
        	enableSecurityCheckBox = new JCheckBox();
        	enableSecurityCheckBox.setToolTipText("Enable Security Extension?");
        	enableSecurityCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	enableSecurityCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("ENABLE_SECURITY")));
        	enableSecurityCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	enableSecurityCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					toggleSecurityFields();
					validateInput();
				}
        	});

        	enableSecurityCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return enableSecurityCheckBox;
    }
    
    /**
     * This method initializes the Enable Instance Level Security CheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getEnableInstanceLevelSecurityCheckBox() {
        if (enableInstanceLevelSecurityCheckBox == null) {
        	enableInstanceLevelSecurityCheckBox = new JCheckBox();
        	enableInstanceLevelSecurityCheckBox.setToolTipText("Enable Instance Level Security?");
        	enableInstanceLevelSecurityCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	enableInstanceLevelSecurityCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("ENABLE_INSTANCE_LEVEL_SECURITY")));
        	enableInstanceLevelSecurityCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	enableInstanceLevelSecurityCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					toggleSecurityFields();
					validateInput();
					confirmCsmTablesPresent();
				}
        	});

        	enableInstanceLevelSecurityCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return enableInstanceLevelSecurityCheckBox;
    }
    
    private void confirmCsmTablesPresent(){
		if (enableInstanceLevelSecurityCheckBox.isSelected()){
			JOptionPane.showMessageDialog(
					this,
					"Instance Level Security requires that the CSM tables be present in the same schema as the model tables.  "
					+ "Make sure this is the case.");
		}
    }
    
    /**
     * This method initializes the Enable Attribute Level Security CheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getEnableAttributeLevelSecurityCheckBox() {
        if (enableAttributeLevelSecurityCheckBox == null) {
        	enableAttributeLevelSecurityCheckBox = new JCheckBox();
        	enableAttributeLevelSecurityCheckBox.setToolTipText("Enable Attribute Level Security?");
        	enableAttributeLevelSecurityCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	enableAttributeLevelSecurityCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("ENABLE_ATTRIBUTE_LEVEL_SECURITY")));
        	enableAttributeLevelSecurityCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	enableAttributeLevelSecurityCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					toggleSecurityFields();
					validateInput();
				}
        	});

        	enableAttributeLevelSecurityCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return enableAttributeLevelSecurityCheckBox;
    }
    
    /**
     * This method initializes the CSM Project Name Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getCsmProjectNameField() {
        if (csmProjectNameField == null) {
        	csmProjectNameField = new JTextField();
        	csmProjectNameField.setText(deployPropsMgr.getDeployPropertyValue("CSM_PROJECT_NAME"));
        	csmProjectNameField.getDocument().addDocumentListener(new DocumentListener() {
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
        	csmProjectNameField.addFocusListener(new FocusChangeHandler());
        }
        return csmProjectNameField;
    } 
    
    /**
     * This method initializes the Cache Protection Elements CheckBox
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getCacheProtectionElementsCheckBox() {
        if (cacheProtectionElementsCheckBox == null) {
        	cacheProtectionElementsCheckBox = new JCheckBox();
        	cacheProtectionElementsCheckBox.setToolTipText("Cache Protection Elements?");
        	cacheProtectionElementsCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	cacheProtectionElementsCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("CACHE_PROTECTION_ELEMENTS")));
        	cacheProtectionElementsCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	cacheProtectionElementsCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					toggleSecurityFields();
					validateInput();
				}
        	});

        	cacheProtectionElementsCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return cacheProtectionElementsCheckBox;
    }
    
    /**
     * This method initializes the CSM 'Use DB Connection Settings?' Check Box
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getCsmUseDbConnectionSettingsCheckBox() {
        if (csmUseDbConnectionSettingsCheckBox == null) {
        	csmUseDbConnectionSettingsCheckBox = new JCheckBox();
        	csmUseDbConnectionSettingsCheckBox.setToolTipText("Use DB Connection Settings?");
        	csmUseDbConnectionSettingsCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	csmUseDbConnectionSettingsCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("CSM_USE_DB_CONNECTION_SETTINGS")));
        	csmUseDbConnectionSettingsCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	csmUseDbConnectionSettingsCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					syncDbCsmDbFields();
					toggleCsmDbConnectionFields();
					validateInput();
				}
        	});

        	csmUseDbConnectionSettingsCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return csmUseDbConnectionSettingsCheckBox;
    } 

    /**
     * This method initializes the CSM Use JNDI Based Connection Check Box
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getCsmUseJndiBasedConnectionCheckBox() {
        if (csmUseJndiBasedConnectionCheckBox == null) {
        	csmUseJndiBasedConnectionCheckBox = new JCheckBox();
        	csmUseJndiBasedConnectionCheckBox.setToolTipText("Use a JNDI-based CSM Connection?");
        	csmUseJndiBasedConnectionCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	csmUseJndiBasedConnectionCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("CSM_USE_JNDI_BASED_CONNECTION")));
        	csmUseJndiBasedConnectionCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	csmUseJndiBasedConnectionCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					toggleCsmDbConnectionFields();
					validateInput();
				}
        	});

        	csmUseJndiBasedConnectionCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return csmUseJndiBasedConnectionCheckBox;
    }  
    
    /**
     * This method initializes the CSM Database JNDI URL Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getCsmDbJndiUrlField() {
        if (csmDbJndiUrlField == null) {
        	csmDbJndiUrlField = new JTextField();
        	csmDbJndiUrlField.setText(deployPropsMgr.getDeployPropertyValue("CSM_DB_JNDI_URL"));
        	csmDbJndiUrlField.getDocument().addDocumentListener(new DocumentListener() {
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
        	csmDbJndiUrlField.addFocusListener(new FocusChangeHandler());
        }
        return csmDbJndiUrlField;
    }
    
    /**
     * This method initializes the CSM Database Connection URL Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getCsmDbConnectionUrlField() {
        if (csmDbConnectionUrlField == null) {
        	csmDbConnectionUrlField = new JTextField();
        	csmDbConnectionUrlField.setText(deployPropsMgr.getDeployPropertyValue("CSM_DB_CONNECTION_URL"));
        	csmDbConnectionUrlField.getDocument().addDocumentListener(new DocumentListener() {
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
        	csmDbConnectionUrlField.addFocusListener(new FocusChangeHandler());
        }
        return csmDbConnectionUrlField;
    }
    
    /**
     * This method initializes the CSM Database Username Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getCsmDbUsernameField() {
        if (csmDbUsernameField == null) {
        	csmDbUsernameField = new JTextField();
        	csmDbUsernameField.setText(deployPropsMgr.getDeployPropertyValue("CSM_DB_USERNAME"));
        	csmDbUsernameField.getDocument().addDocumentListener(new DocumentListener() {
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
        	csmDbUsernameField.addFocusListener(new FocusChangeHandler());
        }
        return csmDbUsernameField;
    }
    
    /**
     * This method initializes the CSM Database Password Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getCsmDbPasswordField() {
        if (csmDbPasswordField == null) {
        	csmDbPasswordField = new JTextField();
        	csmDbPasswordField.setText(deployPropsMgr.getDeployPropertyValue("CSM_DB_PASSWORD"));
        	csmDbPasswordField.getDocument().addDocumentListener(new DocumentListener() {
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
        	csmDbPasswordField.addFocusListener(new FocusChangeHandler());
        }
        return csmDbPasswordField;
    }
    
    /**
     * This method initializes the CSM Database Driver Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getCsmDbDriverField() {
        if (csmDbDriverField == null) {
        	csmDbDriverField = new JTextField();
        	csmDbDriverField.setText(deployPropsMgr.getDeployPropertyValue("CSM_DB_DRIVER"));
        	
        	csmDbDriverField.setEditable(false);
        	csmDbDriverField.setEnabled(false);
        	
        	csmDbDriverField.getDocument().addDocumentListener(new DocumentListener() {
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
        	csmDbDriverField.addFocusListener(new FocusChangeHandler());
        }
        return csmDbDriverField;
    }
    
    /**
     * This method initializes the Database Dialect Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getCsmDbDialectField() {
        if (csmDbDialectField == null) {
        	csmDbDialectField = new JTextField();
        	csmDbDialectField.setText(deployPropsMgr.getDeployPropertyValue("CSM_DB_DIALECT"));
        	
        	csmDbDialectField.setEditable(false);
        	csmDbDialectField.setEnabled(false);
        	
        	csmDbDialectField.getDocument().addDocumentListener(new DocumentListener() {
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
        	
        	csmDbDialectField.addFocusListener(new FocusChangeHandler());

        }
        return csmDbDialectField;
    }
    	
    /**
     * This method initializes the caGrid Authentication Service URL Field
     * 
     * @return javax.swing.JTextField
     */    
    private JTextField getCaGridAuthSvcUrlField() {
    	if (caGridAuthSvcUrlField == null) {
    		caGridAuthSvcUrlField = new JTextField();
    		caGridAuthSvcUrlField.setText(deployPropsMgr.getDeployPropertyValue("CAGRID_AUTHENTICATION_SERVICE_URL"));
    		caGridAuthSvcUrlField.getDocument().addDocumentListener(new DocumentListener() {
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
    		caGridAuthSvcUrlField.addFocusListener(new FocusChangeHandler());
    	}
    	return caGridAuthSvcUrlField;
    }
    
    /**
     * This method initializes the caGrid Dorian Service URL Field
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getCaGridDorianSvcUrlField() {
    	if (caGridDorianSvcUrlField == null) {
    		caGridDorianSvcUrlField = new JTextField();
    		caGridDorianSvcUrlField.setText(deployPropsMgr.getDeployPropertyValue("CAGRID_DORIAN_SERVICE_URL"));
    		caGridDorianSvcUrlField.getDocument().addDocumentListener(new DocumentListener() {
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
    		caGridDorianSvcUrlField.addFocusListener(new FocusChangeHandler());
    	}
    	return caGridDorianSvcUrlField;
    }
    
    /**
     * This method initializes the SDK Grid Login Service Name Field
     * 
     * @return javax.swing.JTextField
     */    
    private JTextField getSdkGridLoginSvcNameField() {
    	if (sdkGridLoginSvcNameField == null) {
    		sdkGridLoginSvcNameField = new JTextField();
    		sdkGridLoginSvcNameField.setText(deployPropsMgr.getDeployPropertyValue("SDK_GRID_LOGIN_SERVICE_NAME"));
    		sdkGridLoginSvcNameField.getDocument().addDocumentListener(new DocumentListener() {
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
    		sdkGridLoginSvcNameField.addFocusListener(new FocusChangeHandler());
    	}
    	return sdkGridLoginSvcNameField;
    }
    
    /**
     * This method initializes the SDK Grid Login Service URL Field
     * 
     * @return javax.swing.JTextField
     */     
    private JTextField getSdkGridLoginSvcUrlField() {
    	if (sdkGridLoginSvcUrlField == null) {
    		sdkGridLoginSvcUrlField = new JTextField();
    			
    		sdkGridLoginSvcUrlField.setText(SDK_GRID_LOGIN_SERVICE_URL);
    		
    		sdkGridLoginSvcUrlField.getDocument().addDocumentListener(new DocumentListener() {
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
    		sdkGridLoginSvcUrlField.addFocusListener(new FocusChangeHandler());
    	}
    	return sdkGridLoginSvcUrlField;
    }
        
    /**
     * This method initializes the caGrid Environment Combo Box
     * 
     * @return javax.swing.JTextField
     */         
    private JComboBox getCaGridEnvironmentComboBox() {
    	if (caGridEnvironmentComboBox == null) {
    		caGridEnvironmentComboBox = new JComboBox();
    		
        	if (caGridEnvironmentOptionsMap!=null){
            	Iterator<String> iter = caGridEnvironmentOptionsMap.keySet().iterator();
            	
            	while (iter.hasNext()){
            		caGridEnvironmentComboBox.addItem((String)iter.next());
            	}
        	}

    		caGridEnvironmentComboBox.setSelectedItem("");

    		caGridEnvironmentComboBox.addActionListener(new ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                	updateCaGridEnvironmentFields();
                    validateInput();
                }
            });

    		
    	}
    	return caGridEnvironmentComboBox;
    }
    
    /**
     * This method initializes the Validate Logical Model Check Box
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getEnableCaGridLoginModuleCheckBox() {
        if (enableCaGridLoginModuleCheckBox == null) {
        	enableCaGridLoginModuleCheckBox = new JCheckBox();
        	enableCaGridLoginModuleCheckBox.setToolTipText("Enable caGrid Authorization Login Module?");
        	enableCaGridLoginModuleCheckBox.setHorizontalAlignment(SwingConstants.LEADING);
        	enableCaGridLoginModuleCheckBox.setSelected(Boolean.parseBoolean(deployPropsMgr.getDeployPropertyValue("ENABLE_GRID_LOGIN_MODULE")));
        	enableCaGridLoginModuleCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			
        	enableCaGridLoginModuleCheckBox.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
                	toggleCaGridLoginFields();
                    validateInput();
				}
        	});

        	enableCaGridLoginModuleCheckBox.addFocusListener(new FocusChangeHandler());
        }
        return enableCaGridLoginModuleCheckBox;
    }
    
    /**
     * This method initializes the caGrid Login Module Name Field
     * 
     * @return javax.swing.JTextField
     */         
    private JTextField getCaGridLoginModuleNameField() {
    	if (caGridLoginModuleNameField == null) {
    		caGridLoginModuleNameField = new JTextField();
    		caGridLoginModuleNameField.setText(deployPropsMgr.getDeployPropertyValue("CAGRID_LOGIN_MODULE_NAME"));
    		caGridLoginModuleNameField.getDocument().addDocumentListener(new DocumentListener() {
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
    		caGridLoginModuleNameField.addFocusListener(new FocusChangeHandler());
    	}
    	return caGridLoginModuleNameField;
    }
   
    /**
     * This method initializes the Cache Path Field
     * 
     * @return javax.swing.JTextField
     */         
    private JTextField getCachePathField() {
    	if (cachePathField == null) {
    		cachePathField = new JTextField();
    		cachePathField.setText(deployPropsMgr.getDeployPropertyValue("CACHE_PATH"));
    		
    		cachePathField.getDocument().addDocumentListener(new DocumentListener() {
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
    		cachePathField.addFocusListener(new FocusChangeHandler());
    	}
    	return cachePathField;
    }
    
    /**
     * This method initializes the Server Type Combo Box
     * 
     * @return javax.swing.JTextField
     */         
    private JComboBox getServerTypeComboBox() {
    	if (serverTypeComboBox == null) {
    		serverTypeComboBox = new JComboBox();
    		serverTypeComboBox.addItem("jboss");
    		serverTypeComboBox.addItem("other");

    		if ("jboss".equalsIgnoreCase(deployPropsMgr.getDeployPropertyValue("SERVER_TYPE"))){
    				serverTypeComboBox.setSelectedItem("jboss");
    		} else{
    			serverTypeComboBox.setSelectedItem("other");
    		}

    		serverTypeComboBox.addFocusListener(new FocusChangeHandler());
    	}
    	return serverTypeComboBox;
    }
    
    /**
     * This method initializes the Server URL Field
     * 
     * @return javax.swing.JTextField
     */         
    private JTextField getServerUrlField() {
    	if (serverUrlField == null) {
    		serverUrlField = new JTextField();
   		
    		serverUrlField.setText(deployPropsMgr.getDeployPropertyValue("SERVER_URL"));
    		
    		serverUrlField.getDocument().addDocumentListener(new DocumentListener() {
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
    		serverUrlField.addFocusListener(new FocusChangeHandler());
    	}
    	return serverUrlField;
    }

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
			mainTabbedPane.addTab("Logging", null, new IconFeedbackPanel(this.validationModel, getClmSettingsPanel()), null);
			
			mainTabbedPane.addTab("Security", null, new IconFeedbackPanel(this.validationModel, getSecuritySettingsPanel()), null);
			mainTabbedPane.addTab("CSM DB", null, new IconFeedbackPanel(this.validationModel, getCsmDbConnectionSettingsPanel()), null);
			mainTabbedPane.addTab("caGrid Auth", null, new IconFeedbackPanel(this.validationModel, getCaGridAuthSettingsPanel()), null);
			
			mainTabbedPane.addTab("App Server", null, new IconFeedbackPanel(this.validationModel, getAppServerSettingsPanel()), null);

			mainTabbedPane.addTab("Advanced", null, new IconFeedbackPanel(this.validationModel, getAdvancedSettingsPanel()), null);
			
			mainTabbedPane.addMouseListener(new java.awt.event.MouseListener() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                	;//do nothing
                }
                public void mouseReleased(java.awt.event.MouseEvent e) {
                	;//do nothing
                }
                public void mouseExited(java.awt.event.MouseEvent e) {
                	;//do nothing
                }
                public void mousePressed(java.awt.event.MouseEvent e) {
                	;//do nothing
                }
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    try {
                        validateInput();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
		}
		return mainTabbedPane;
	}
	
	/**
	 * This method initializes securitySettingsSubPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSecuritySettingsSubPanel() {
		if (securitySettingsSubPanel == null) {
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
		    
		    enableInstanceLevelSecurityLabel = new JLabel();
		    enableInstanceLevelSecurityLabel.setText("Enable Instance Level Security?");
		    
		    enableAttributeLevelSecurityLabel = new JLabel();
		    enableAttributeLevelSecurityLabel.setText("Enable Attribute Level Security?");

		    csmProjectNameLabel = new JLabel();
		    csmProjectNameLabel.setText("Enter CSM Project Name:");

		    cacheProtectionElementsLabel = new JLabel();
		    cacheProtectionElementsLabel.setText("Cache Protection Elements?");

		    securitySettingsSubPanel = new JPanel();
		    securitySettingsSubPanel.setLayout(new GridBagLayout());
		    securitySettingsSubPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Security Options",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));

		    securitySettingsSubPanel.add(enableInstanceLevelSecurityLabel, gridBagConstraints10);
		    securitySettingsSubPanel.add(getEnableInstanceLevelSecurityCheckBox(), gridBagConstraints11);
		    securitySettingsSubPanel.add(enableAttributeLevelSecurityLabel, gridBagConstraints20);
		    securitySettingsSubPanel.add(getEnableAttributeLevelSecurityCheckBox(), gridBagConstraints21);
		    securitySettingsSubPanel.add(csmProjectNameLabel, gridBagConstraints30);
		    securitySettingsSubPanel.add(getCsmProjectNameField(), gridBagConstraints31);
		    securitySettingsSubPanel.add(cacheProtectionElementsLabel, gridBagConstraints40);
		    securitySettingsSubPanel.add(getCacheProtectionElementsCheckBox(), gridBagConstraints41);

		    securitySettingsSubPanel.validate();
		}
		return securitySettingsSubPanel;
	}
	
	/**
	 * This method initializes securitySettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSecuritySettingsPanel() {
		if (securitySettingsPanel == null) {
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
			//gridBagConstraints11.weighty = 1.0D;
			gridBagConstraints11.weightx = 1.0D;  
			gridBagConstraints11.gridwidth = 2;

			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints20.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints20.gridy = 2;
			gridBagConstraints20.gridx = 0;
			gridBagConstraints20.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints20.gridwidth = 3;
			//gridBagConstraints20.weighty = 1.0D;
			gridBagConstraints20.weightx = 1.0D;  
	    
		    enableSecurityLabel = new JLabel();
		    enableSecurityLabel.setText("Enable Security Extension?");

			securitySettingsPanel = new JPanel();
			securitySettingsPanel.setLayout(new GridBagLayout());
			securitySettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Define Security Properties",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));

			securitySettingsPanel.add(enableSecurityLabel, gridBagConstraints10);
			securitySettingsPanel.add(getEnableSecurityCheckBox(), gridBagConstraints11);
			securitySettingsPanel.add(getSecuritySettingsSubPanel(), gridBagConstraints20);

			securitySettingsPanel.validate();
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
			//gridBagConstraints11.weighty = 0.5D; //Non-standard 1.0 setting
			gridBagConstraints11.weightx = 1.0D;  
			gridBagConstraints11.gridwidth = 2;

			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints20.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints20.gridy = 2;
			gridBagConstraints20.gridx = 0;
			gridBagConstraints20.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints20.gridwidth = 3;
			//gridBagConstraints20.weighty = 1.0D; //Non-standard 1.0 setting
			gridBagConstraints20.weightx = 1.0D;  

		    enableWritableApiExtensionLabel = new JLabel();
		    enableWritableApiExtensionLabel.setText("Enable Writable API Extension?");
		    
			writableApiSettingsPanel = new JPanel();
			writableApiSettingsPanel.setLayout(new GridBagLayout());
			writableApiSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Define Writable API Module Properties",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));

			writableApiSettingsPanel.add(enableWritableApiExtensionLabel, gridBagConstraints10);
			writableApiSettingsPanel.add(getEnableWritableApiExtensionCheckBox(), gridBagConstraints11);
			writableApiSettingsPanel.add(getWritableApiSettingsSubPanel(), gridBagConstraints20);

			writableApiSettingsPanel.validate();
		}
		return writableApiSettingsPanel;
	}
	
	/**
	 * This method initializes writableApiSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getWritableApiSettingsSubPanel() {
		if (writableApiSettingsSubPanel == null) {

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

		    identityGeneratorTagLabel = new JLabel();
		    identityGeneratorTagLabel.setText("Enter Hibernate Identity Generator Tag:");

		    caDsrConnectionUrlLabel = new JLabel();
		    caDsrConnectionUrlLabel.setText("Enter caDSR Connection URL:");
		    
			writableApiSettingsSubPanel = new JPanel();
			writableApiSettingsSubPanel.setLayout(new GridBagLayout());
			writableApiSettingsSubPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Writable API Options",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));

			writableApiSettingsSubPanel.add(identityGeneratorTagLabel, gridBagConstraints10);
			writableApiSettingsSubPanel.add(getIdentityGeneratorTagField(), gridBagConstraints11);
			writableApiSettingsSubPanel.add(caDsrConnectionUrlLabel, gridBagConstraints20);
			writableApiSettingsSubPanel.add(getCaDsrConnectionUrlField(), gridBagConstraints21);

			writableApiSettingsSubPanel.validate();
		}
		return writableApiSettingsSubPanel;
	}
	
	/**
	 * This method initializes writableApiSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getClmSettingsPanel() {
		if (clmSettingsPanel == null) {

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
			//gridBagConstraints11.weighty = 1.0D;  // so that the CLM sub panel has priority
			gridBagConstraints11.weightx = 1.0D;  
			gridBagConstraints11.gridwidth = 2;
			
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints20.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints20.gridy = 2;
			gridBagConstraints20.gridx = 0;
			gridBagConstraints20.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints20.gridwidth = 3;
			//gridBagConstraints20.weighty = 1.0D;
			gridBagConstraints20.weightx = 1.0D;  

		    enableCommonLoggingModuleLabel = new JLabel();
		    enableCommonLoggingModuleLabel.setText("Enable Logging?");
		    
		    clmSettingsPanel = new JPanel();
			clmSettingsPanel.setLayout(new GridBagLayout());
			clmSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Define Logging Properties",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));

			clmSettingsPanel.add(enableCommonLoggingModuleLabel, gridBagConstraints10);
			clmSettingsPanel.add(getEnableCommonLoggingModuleCheckBox(), gridBagConstraints11);
			clmSettingsPanel.add(getLoggingSettingsSubPanel(), gridBagConstraints20);

			clmSettingsPanel.validate();
		}
		return clmSettingsPanel;
	}
	
	
	/**
	 * This method initializes clmSettingsSubPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getLoggingSettingsSubPanel() {
		if (clmSettingsSubPanel == null) {

			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.gridy = 5;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.gridx = 0;

			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridy = 5;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.gridwidth = 2;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints11.weighty = 1.0D;
			gridBagConstraints11.gridx = 1;

			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints20.gridy = 6;
			gridBagConstraints20.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints20.gridx = 0;

			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.gridy = 6;
			gridBagConstraints21.weightx = 1.0;
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints21.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints21.gridwidth = 2;
			gridBagConstraints21.weighty = 1.0D;
			gridBagConstraints21.gridx = 1;

			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints30.gridy = 7;
			gridBagConstraints30.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints30.gridx = 0;

			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints31.gridy = 7;
			gridBagConstraints31.weightx = 1.0;
			gridBagConstraints31.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints31.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints31.gridwidth = 2;
			gridBagConstraints31.weighty = 1.0D;
			gridBagConstraints31.gridx = 1;

			GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
			gridBagConstraints40.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints40.gridy = 8;
			gridBagConstraints40.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints40.gridx = 0;

			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints41.gridy = 8;
			gridBagConstraints41.weightx = 1.0;
			gridBagConstraints41.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints41.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints41.gridwidth = 2;
			gridBagConstraints41.weighty = 1.0D;
			gridBagConstraints41.gridx = 1;

			GridBagConstraints gridBagConstraints50 = new GridBagConstraints();
			gridBagConstraints50.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints50.gridy = 9;
			gridBagConstraints50.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints50.gridx = 0;

			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints51.gridy = 9;
			gridBagConstraints51.weightx = 1.0;
			gridBagConstraints51.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints51.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints51.gridwidth = 2;
			gridBagConstraints51.weighty = 1.0D;
			gridBagConstraints51.gridx = 1;

			GridBagConstraints gridBagConstraints60 = new GridBagConstraints();
			gridBagConstraints60.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints60.gridy = 10;
			gridBagConstraints60.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints60.gridx = 0;

			GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
			gridBagConstraints61.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints61.gridy = 10;
			gridBagConstraints61.weightx = 1.0;
			gridBagConstraints61.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints61.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints61.gridwidth = 2;
			gridBagConstraints61.weighty = 1.0D;
			gridBagConstraints61.gridx = 1;

		    clmProjectNameLabel = new JLabel();
		    clmProjectNameLabel.setText("Enter Logging Project Name:");

		    clmDbConnectionUrlLabel = new JLabel();
		    clmDbConnectionUrlLabel.setText("Enter Logging Database Connection URL:");

		    clmDbUsernameLabel = new JLabel();
		    clmDbUsernameLabel.setText("Enter Logging Database Username:");

		    clmDbPasswordLabel = new JLabel();
		    clmDbPasswordLabel.setText("Enter Logging Database Password:");

		    clmDbDriverLabel = new JLabel();
		    clmDbDriverLabel.setText("Enter Logging Database Driver:");
		    
		    clmSettingsSubPanel = new JPanel();
		    clmSettingsSubPanel.setLayout(new GridBagLayout());
		    clmSettingsSubPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Logging Database Properties",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));

		    clmSettingsSubPanel.add(clmProjectNameLabel, gridBagConstraints10);
		    clmSettingsSubPanel.add(getClmProjectName(), gridBagConstraints11);
		    clmSettingsSubPanel.add(clmDbConnectionUrlLabel, gridBagConstraints20);
		    clmSettingsSubPanel.add(getClmDbConnectionUrlField(), gridBagConstraints21);
		    clmSettingsSubPanel.add(clmDbUsernameLabel, gridBagConstraints30);
		    clmSettingsSubPanel.add(getClmDbUsernameField(), gridBagConstraints31);   
		    clmSettingsSubPanel.add(clmDbPasswordLabel, gridBagConstraints40);
		    clmSettingsSubPanel.add(getClmDbPasswordField(), gridBagConstraints41);
		    clmSettingsSubPanel.add(clmDbDriverLabel, gridBagConstraints50);
		    clmSettingsSubPanel.add(getClmDbDriverField(), gridBagConstraints51);

		    clmSettingsSubPanel.validate();
		}
		return clmSettingsSubPanel;
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
            gridBagConstraints21.weighty = 1.0D;
            gridBagConstraints21.weightx = 1.0;
            
            GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
            gridBagConstraints22.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints22.gridy = 2;
            gridBagConstraints22.gridx = 2;
            gridBagConstraints22.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints22.gridwidth = 1;
            
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
            gridBagConstraints51.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints51.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints51.gridwidth = 2;
            gridBagConstraints51.weighty = 1.0D;
            gridBagConstraints51.gridx = 1;
                        
            sdkInstallDirLabel = new JLabel();
            sdkInstallDirLabel.setText("Enter the SDK install home directory:");
            sdkInstallDirLabel.setName("SDK Install Directory");
            
            projectDirLabel = new JLabel();
            projectDirLabel.setText("Enter the project directory:");
            projectDirLabel.setName("Project Directory");
            
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
            
            projectSettingsPanel.add(projectDirLabel, gridBagConstraints10);
            projectSettingsPanel.add(getProjectDirField(), gridBagConstraints11);
            projectSettingsPanel.add(getProjectDirButton(), gridBagConstraints12);
            projectSettingsPanel.add(sdkInstallDirLabel, gridBagConstraints20);
            projectSettingsPanel.add(getSdkInstallDirField(), gridBagConstraints21);
            projectSettingsPanel.add(getSdkInstallDirButton(), gridBagConstraints22);
            projectSettingsPanel.add(projectNameLabel, gridBagConstraints30);
            projectSettingsPanel.add(getProjectNameField(), gridBagConstraints31);
            projectSettingsPanel.add(namespacePrefixLabel, gridBagConstraints40);
            projectSettingsPanel.add(getNamespacePrefixField(), gridBagConstraints41);
            projectSettingsPanel.add(webserviceNameLabel, gridBagConstraints50);
            projectSettingsPanel.add(getWebServiceNameField(), gridBagConstraints51);
            
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
            modelFileLabel.setText("Enter the Model file path:");
            
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
	 * This method initializes the Code Generation settings panel	
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
			gridBagConstraints40.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints40.gridy = 4;
			gridBagConstraints40.weightx = 1.0;
			gridBagConstraints40.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints40.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints40.gridwidth = 3;
			gridBagConstraints40.weighty = 1.0D;
			gridBagConstraints40.gridx = 0;

			GridBagConstraints gridBagConstraints60 = new GridBagConstraints();
			gridBagConstraints60.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints60.gridy = 6;
			gridBagConstraints60.weightx = 1.0;
			gridBagConstraints60.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints60.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints60.gridwidth = 3;
			gridBagConstraints60.weighty = 1.0D;
			gridBagConstraints60.gridx = 0;

		    validateLogicalModelLabel = new JLabel();
		    validateLogicalModelLabel.setText("Validate Logical Model?");
		    
		    generateBeansLabel = new JLabel();
		    generateBeansLabel.setText("Generate domain Java Beans?");
		    
		    generateWsddLabel = new JLabel();
		    generateWsddLabel.setText("Generate WSDD?");

		    codeGenSettingsPanel = new JPanel();
		    codeGenSettingsPanel.setLayout(new GridBagLayout());
		    codeGenSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Define Code Generation Properties",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));

		    codeGenSettingsPanel.add(validateLogicalModelLabel, gridBagConstraints10);
		    codeGenSettingsPanel.add(getValidateLogicalModelCheckBox(), gridBagConstraints11);
			codeGenSettingsPanel.add(generateBeansLabel, gridBagConstraints20);
			codeGenSettingsPanel.add(getGenerateBeansCheckBox(), gridBagConstraints21);
			codeGenSettingsPanel.add(generateWsddLabel, gridBagConstraints30);
			codeGenSettingsPanel.add(getGenerateWsddLabelCheckBox(), gridBagConstraints31);
			codeGenSettingsPanel.add(getOrmCodeGenSettingsPanel(), gridBagConstraints40);
			codeGenSettingsPanel.add(getXsdCodeGenSettingsSubPanel(), gridBagConstraints60);

			
		    codeGenSettingsPanel.validate();
		}
		return codeGenSettingsPanel;
	}
	
	/**
	 * This method initializes the Code Generation settings panel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getOrmCodeGenSettingsPanel() {
		if (ormCodeGenSettingsSubPanel == null) {

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

		    validateModelMappingLabel = new JLabel();
		    validateModelMappingLabel.setText("Validate Model Mapping?");

		    generateHibernateMappingLabel = new JLabel();
		    generateHibernateMappingLabel.setText("Generate Hibernate Mapping Files?");

		    generateHibernateValidatorLabel = new JLabel();
		    generateHibernateValidatorLabel.setText("Generate Hibernate Validator?");

		    ormCodeGenSettingsSubPanel = new JPanel();
		    ormCodeGenSettingsSubPanel.setLayout(new GridBagLayout());
		    ormCodeGenSettingsSubPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Object Relational Mapping Options",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));

		    ormCodeGenSettingsSubPanel.add(validateModelMappingLabel, gridBagConstraints10);
			ormCodeGenSettingsSubPanel.add(getValidateModelMappingCheckBox(), gridBagConstraints11);
			ormCodeGenSettingsSubPanel.add(generateHibernateMappingLabel, gridBagConstraints20);
			ormCodeGenSettingsSubPanel.add(getGenerateHibernateMappingCheckBox(), gridBagConstraints21);
			ormCodeGenSettingsSubPanel.add(generateHibernateValidatorLabel, gridBagConstraints30);
			ormCodeGenSettingsSubPanel.add(getGenerateHibernateValidatorCheckBox(), gridBagConstraints31);


			
		    ormCodeGenSettingsSubPanel.validate();
		}
		return ormCodeGenSettingsSubPanel;
	}
	
	/**
	 * This method initializes the XSD Code Generation settings sub-panel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getXsdCodeGenSettingsSubPanel() {
		if (xsdCodeGenSettingsSubPanel == null) {

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
			gridBagConstraints41.gridwidth = 2;
			gridBagConstraints41.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints41.insets = new java.awt.Insets(2, 2, 2, 2);
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
			gridBagConstraints51.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints51.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints51.gridwidth = 2;
			gridBagConstraints51.weighty = 1.0D;
			gridBagConstraints51.gridx = 1;
			
		    generateXsdLabel = new JLabel();
		    generateXsdLabel.setText("Generate XSD's?");
		    
		    validateGmeTagsLabel = new JLabel();
		    validateGmeTagsLabel.setText("Validate GME Tags?");
		    
		    generateCastorMappingLabel = new JLabel();
		    generateCastorMappingLabel.setText("Generate Castor Mapping files?");

		    generateXsdWithGmeTagsLabel = new JLabel();
		    generateXsdWithGmeTagsLabel.setText("Generate XSD's with GME tags?");
		    
		    generateXsdWithPermissibleValuesLabel = new JLabel();
		    generateXsdWithPermissibleValuesLabel.setText("Generate XSD's with Permissible Values?");

		    xsdCodeGenSettingsSubPanel = new JPanel();
		    xsdCodeGenSettingsSubPanel.setLayout(new GridBagLayout());
		    xsdCodeGenSettingsSubPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "XSD Options",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
		    
		    xsdCodeGenSettingsSubPanel.add(generateXsdLabel, gridBagConstraints10);
		    xsdCodeGenSettingsSubPanel.add(getGenerateXsdCheckBox(), gridBagConstraints11);
		    xsdCodeGenSettingsSubPanel.add(generateCastorMappingLabel, gridBagConstraints20);
		    xsdCodeGenSettingsSubPanel.add(getGenerateCastorMappingCheckBox(), gridBagConstraints21);
		    xsdCodeGenSettingsSubPanel.add(generateXsdWithGmeTagsLabel, gridBagConstraints30);
		    xsdCodeGenSettingsSubPanel.add(generateXsdWithGmeTagsCheckBox(), gridBagConstraints31); 
		    xsdCodeGenSettingsSubPanel.add(validateGmeTagsLabel, gridBagConstraints40);
		    xsdCodeGenSettingsSubPanel.add(getValidateGmeTagsCheckBox(), gridBagConstraints41);
		    xsdCodeGenSettingsSubPanel.add(generateXsdWithPermissibleValuesLabel, gridBagConstraints50);
		    xsdCodeGenSettingsSubPanel.add(getGenerateXsdWithPermissibleValuesCheckBox(), gridBagConstraints51);
			
		    xsdCodeGenSettingsSubPanel.validate();
		}
		return xsdCodeGenSettingsSubPanel;
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
			gridBagConstraints21.gridx = 1;
			gridBagConstraints21.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints21.gridy = 2;
			//gridBagConstraints21.weighty = 1.0D;
			gridBagConstraints21.weightx = 1.0D;  
			gridBagConstraints21.gridwidth = 2;

			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints30.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints30.gridy = 3;
			gridBagConstraints30.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints30.gridx = 0;
			gridBagConstraints30.gridwidth = 3;

			GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
			gridBagConstraints40.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints40.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints40.gridy = 4;
			gridBagConstraints40.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints40.gridx = 0;
			gridBagConstraints40.gridwidth = 3;

			GridBagConstraints gridBagConstraints50 = new GridBagConstraints();
			gridBagConstraints50.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints50.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints50.gridy = 5;
			gridBagConstraints50.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints50.gridx = 0;
			gridBagConstraints50.gridwidth = 3;

		    databaseTypeLabel = new JLabel();
		    databaseTypeLabel.setText("Select Database Type:");
		    
		    useJndiBasedConnectionLabel = new JLabel();
			useJndiBasedConnectionLabel.setText("Use a JNDI-based Connection?");

		    dbConnectionSettingsPanel = new JPanel();
		    dbConnectionSettingsPanel.setLayout(new GridBagLayout());
		    dbConnectionSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Define Database Connection Properties",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			
		    dbConnectionSettingsPanel.add(databaseTypeLabel, gridBagConstraints10);
		    dbConnectionSettingsPanel.add(getDatabaseTypeComboBox(), gridBagConstraints11);
		    dbConnectionSettingsPanel.add(useJndiBasedConnectionLabel, gridBagConstraints20);
		    dbConnectionSettingsPanel.add(getUseJndiBasedConnectionCheckBox(), gridBagConstraints21);
		    dbConnectionSettingsPanel.add(getDbConnectionJndiSettingsSubPanel(), gridBagConstraints30);
		    dbConnectionSettingsPanel.add(getDbConnectionSettingsSubPanel(), gridBagConstraints40);
		    dbConnectionSettingsPanel.add(getDbDialectSettingsSubPanel(), gridBagConstraints50);
			
		    dbConnectionSettingsPanel.validate();
		}
		return dbConnectionSettingsPanel;
	}
	
	/**
	 * This method initializes dbConnectionJndiSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDbConnectionJndiSettingsSubPanel() {
		if (dbConnectionJndiSettingsSubPanel == null) {
			
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

		    dbJndiUrlLabel = new JLabel();
		    dbJndiUrlLabel.setText("Database JNDI URL:         ");

		    dbConnectionJndiSettingsSubPanel = new JPanel();
		    dbConnectionJndiSettingsSubPanel.setLayout(new GridBagLayout());
		    dbConnectionJndiSettingsSubPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "JNDI Options",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			
		    dbConnectionJndiSettingsSubPanel.add(dbJndiUrlLabel, gridBagConstraints10);
		    dbConnectionJndiSettingsSubPanel.add(getDbJndiUrlField(), gridBagConstraints11);

			
		    dbConnectionJndiSettingsSubPanel.validate();
		}
		return dbConnectionJndiSettingsSubPanel;
	}
	
	/**
	 * This method initializes dbConnectionJndiSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDbDialectSettingsSubPanel() {
		if (dbDialectSettingsSubPanel == null) {
			
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

		    dbDialectLabel = new JLabel();
		    dbDialectLabel.setText("Database dialect:");

		    dbDialectSettingsSubPanel = new JPanel();
		    dbDialectSettingsSubPanel.setLayout(new GridBagLayout());
		    dbDialectSettingsSubPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Database Dialect Options",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));

		    dbDialectSettingsSubPanel.add(dbDialectLabel, gridBagConstraints10);
		    dbDialectSettingsSubPanel.add(getDbDialectField(), gridBagConstraints11);   

			
		    dbDialectSettingsSubPanel.validate();
		}
		return dbDialectSettingsSubPanel;
	}
	
	
	/**
	 * This method initializes dbConnectionSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDbConnectionSettingsSubPanel() {
		if (dbConnectionSettingsSubPanel == null) {
			
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.gridy = 1;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.gridx = 0;

			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints11.gridwidth = 2;
			gridBagConstraints11.weighty = 1.0D;
			gridBagConstraints11.gridx = 1;

			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints20.gridy = 2;
			gridBagConstraints20.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints20.gridx = 0;

			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.gridy = 2;
			gridBagConstraints21.weightx = 1.0;
			gridBagConstraints21.gridwidth = 2;
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints21.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints21.weighty = 1.0D;
			gridBagConstraints21.gridx = 1;

			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints30.gridy = 3;
			gridBagConstraints30.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints30.gridx = 0;

			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints31.gridy = 3;
			gridBagConstraints31.weightx = 1.0;
			gridBagConstraints31.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints31.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints31.gridwidth = 2;
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

		    dbConnectionUrlLabel = new JLabel();
		    dbConnectionUrlLabel.setText("Database connection URL:");
		    
		    dbUsernameLabel = new JLabel();
		    dbUsernameLabel.setText("Database username:");

		    dbPasswordLabel = new JLabel();
		    dbPasswordLabel.setText("Database password:");

		    dbDriverLabel = new JLabel();
		    dbDriverLabel.setText("Database driver:");

		    dbConnectionSettingsSubPanel = new JPanel();
		    dbConnectionSettingsSubPanel.setLayout(new GridBagLayout());
		    dbConnectionSettingsSubPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Database Connection Options",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));

		    dbConnectionSettingsSubPanel.add(dbConnectionUrlLabel, gridBagConstraints10);
		    dbConnectionSettingsSubPanel.add(getDbConnectionUrlField(), gridBagConstraints11);
		    dbConnectionSettingsSubPanel.add(dbUsernameLabel, gridBagConstraints20);
		    dbConnectionSettingsSubPanel.add(getDbUsernameField(), gridBagConstraints21);
		    dbConnectionSettingsSubPanel.add(dbPasswordLabel, gridBagConstraints30);
		    dbConnectionSettingsSubPanel.add(getDbPasswordField(), gridBagConstraints31);
		    dbConnectionSettingsSubPanel.add(dbDriverLabel, gridBagConstraints40);
		    dbConnectionSettingsSubPanel.add(getDbDriverField(), gridBagConstraints41);
			
		    dbConnectionSettingsSubPanel.validate();
		}
		return dbConnectionSettingsSubPanel;
	}
	
	/**
	 * This method initializes csmDbConnectionSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCsmDbConnectionSettingsPanel() {
		if (csmDbConnectionSettingsPanel == null) {
			
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.gridy = 1;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.gridx = 0;

			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.insets = new java.awt.Insets(20, 2, 20, 2);
			gridBagConstraints11.gridy = 1;
			//gridBagConstraints11.weighty = 1.0D;
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
			gridBagConstraints21.gridx = 1;
			gridBagConstraints21.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints21.gridy = 2;
			//gridBagConstraints21.weighty = 1.0D;
			gridBagConstraints21.weightx = 1.0D;  
			gridBagConstraints21.gridwidth = 2;
			
			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints30.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints30.gridy = 3;
			gridBagConstraints30.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints30.gridx = 0;
			gridBagConstraints30.gridwidth = 3;

			GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
			gridBagConstraints40.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints40.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints40.gridy = 4;
			gridBagConstraints40.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints40.gridx = 0;
			gridBagConstraints40.gridwidth = 3;

			GridBagConstraints gridBagConstraints50 = new GridBagConstraints();
			gridBagConstraints50.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints50.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints50.gridy = 5;
			gridBagConstraints50.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints50.gridx = 0;
			gridBagConstraints50.gridwidth = 3;
			
		    
		    csmUseDbConnectionSettingsLabel = new JLabel();
		    csmUseDbConnectionSettingsLabel.setText("Use DB connection Settings?");
		    
		    csmUseJndiBasedConnectionLabel = new JLabel();
			csmUseJndiBasedConnectionLabel.setText("Use a JNDI-based Connection?");

		    csmDbConnectionSettingsPanel = new JPanel();
		    csmDbConnectionSettingsPanel.setLayout(new GridBagLayout());
		    csmDbConnectionSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Define Common Security Module (CSM) Database Connection Properties",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
		    
		    csmDbConnectionSettingsPanel.add(csmUseDbConnectionSettingsLabel, gridBagConstraints10);
		    csmDbConnectionSettingsPanel.add(getCsmUseDbConnectionSettingsCheckBox(), gridBagConstraints11);
		    csmDbConnectionSettingsPanel.add(csmUseJndiBasedConnectionLabel, gridBagConstraints20);
		    csmDbConnectionSettingsPanel.add(getCsmUseJndiBasedConnectionCheckBox(), gridBagConstraints21);
		    csmDbConnectionSettingsPanel.add(getCsmDbConnectionJndiSettingsSubPanel(), gridBagConstraints30);
		    csmDbConnectionSettingsPanel.add(getCsmDbConnectionSettingsSubPanel(), gridBagConstraints40);
		    csmDbConnectionSettingsPanel.add(getCsmDbDialectSettingsSubPanel(), gridBagConstraints50);
			
		    csmDbConnectionSettingsPanel.validate();
		}
		return csmDbConnectionSettingsPanel;
	}
	
	/**
	 * This method initializes dbConnectionSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCsmDbConnectionJndiSettingsSubPanel() {
		if (csmDbConnectionJndiSettingsSubPanel == null) {
			
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

		    csmDbJndiUrlLabel = new JLabel();
		    csmDbJndiUrlLabel.setText("CSM Database JNDI URL:         ");

		    csmDbConnectionJndiSettingsSubPanel = new JPanel();
		    csmDbConnectionJndiSettingsSubPanel.setLayout(new GridBagLayout());
		    csmDbConnectionJndiSettingsSubPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CSM JNDI Options",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			
		    csmDbConnectionJndiSettingsSubPanel.add(csmDbJndiUrlLabel, gridBagConstraints10);
		    csmDbConnectionJndiSettingsSubPanel.add(getCsmDbJndiUrlField(), gridBagConstraints11);
			
		    csmDbConnectionJndiSettingsSubPanel.validate();
		}
		return csmDbConnectionJndiSettingsSubPanel;
	}
	
	/**
	 * This method initializes dbConnectionSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCsmDbDialectSettingsSubPanel() {
		if (csmDbDialectSettingsSubPanel == null) {
			
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

		    csmDbDialectLabel = new JLabel();
		    csmDbDialectLabel.setText("CSM Database dialect:");

		    csmDbDialectSettingsSubPanel = new JPanel();
		    csmDbDialectSettingsSubPanel.setLayout(new GridBagLayout());
		    csmDbDialectSettingsSubPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CSM Database Dialect Options",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			
		    csmDbDialectSettingsSubPanel.add(csmDbDialectLabel, gridBagConstraints10);
		    csmDbDialectSettingsSubPanel.add(getCsmDbDialectField(), gridBagConstraints11);

			
		    csmDbDialectSettingsSubPanel.validate();
		}
		return csmDbDialectSettingsSubPanel;
	}
	
	/**
	 * This method initializes dbConnectionSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCsmDbConnectionSettingsSubPanel() {
		if (csmDbConnectionSettingsSubPanel == null) {
			
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.gridy = 1;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.gridx = 0;

			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints11.gridwidth = 2;
			gridBagConstraints11.weighty = 1.0D;
			gridBagConstraints11.gridx = 1;

			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints20.gridy = 2;
			gridBagConstraints20.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints20.gridx = 0;

			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.gridy = 2;
			gridBagConstraints21.weightx = 1.0;
			gridBagConstraints21.gridwidth = 2;
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints21.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints21.weighty = 1.0D;
			gridBagConstraints21.gridx = 1;

			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints30.gridy = 3;
			gridBagConstraints30.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints30.gridx = 0;

			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints31.gridy = 3;
			gridBagConstraints31.weightx = 1.0;
			gridBagConstraints31.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints31.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints31.gridwidth = 2;
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

		    csmDbConnectionUrlLabel = new JLabel();
		    csmDbConnectionUrlLabel.setText("CSM Database connection URL:");
		    
		    csmDbUsernameLabel = new JLabel();
		    csmDbUsernameLabel.setText("CSM Database username:");

		    csmDbPasswordLabel = new JLabel();
		    csmDbPasswordLabel.setText("CSM Database password:");

		    csmDbDriverLabel = new JLabel();
		    csmDbDriverLabel.setText("CSM Database driver:");

		    csmDbConnectionSettingsSubPanel = new JPanel();
		    csmDbConnectionSettingsSubPanel.setLayout(new GridBagLayout());
		    csmDbConnectionSettingsSubPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CSM Database Connection Options",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));

		    csmDbConnectionSettingsSubPanel.add(csmDbConnectionUrlLabel, gridBagConstraints10);
		    csmDbConnectionSettingsSubPanel.add(getCsmDbConnectionUrlField(), gridBagConstraints11);
		    csmDbConnectionSettingsSubPanel.add(csmDbUsernameLabel, gridBagConstraints20);
		    csmDbConnectionSettingsSubPanel.add(getCsmDbUsernameField(), gridBagConstraints21);
		    csmDbConnectionSettingsSubPanel.add(csmDbPasswordLabel, gridBagConstraints30);
		    csmDbConnectionSettingsSubPanel.add(getCsmDbPasswordField(), gridBagConstraints31);
		    csmDbConnectionSettingsSubPanel.add(csmDbDriverLabel, gridBagConstraints40);
		    csmDbConnectionSettingsSubPanel.add(getCsmDbDriverField(), gridBagConstraints41);
			
		    csmDbConnectionSettingsSubPanel.validate();
		}
		return csmDbConnectionSettingsSubPanel;
	}
	
	/**
	 * This method initializes caGrid Authenthication Settings Panel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCaGridAuthSettingsPanel() {
		if (caGridAuthSettingsPanel == null) {
		
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
			//gridBagConstraints11.weighty = 1.0D;
			gridBagConstraints11.weightx = 1.0D;  
			gridBagConstraints11.gridwidth = 2;
        
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints20.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints20.gridy = 2;
			gridBagConstraints20.gridx = 0;
			gridBagConstraints20.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints20.gridwidth = 3;
			//gridBagConstraints20.weighty = 1.0D;
			gridBagConstraints20.weightx = 1.0D;  

			enableCaGridLoginModuleLabel = new JLabel();
			enableCaGridLoginModuleLabel.setText("Enable caGrid Login Module?");

		    caGridAuthSettingsPanel = new JPanel();
		    caGridAuthSettingsPanel.setLayout(new GridBagLayout());
		    caGridAuthSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Define caGrid Authentication Properties",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
		    
		    caGridAuthSettingsPanel.add(enableCaGridLoginModuleLabel, gridBagConstraints10);
		    caGridAuthSettingsPanel.add(getEnableCaGridLoginModuleCheckBox(), gridBagConstraints11);
		    caGridAuthSettingsPanel.add(getCaGridAuthLoginModuleSettingsSubPanel(), gridBagConstraints20);
			
		    caGridAuthSettingsPanel.validate();
		}
		return caGridAuthSettingsPanel;
	}
	
	/**
	 * This method initializes caGrid Authenthication Settings Panel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCaGridAuthLoginModuleSettingsSubPanel() {
		if (caGridAuthLoginModuleSettingsSubPanel == null) {
		
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
			
			caGridEnvironmentLabel = new JLabel();
			caGridEnvironmentLabel.setText("Select caGrid Environment:");

			caGridLoginModuleNameLabel = new JLabel();
			caGridLoginModuleNameLabel.setText("Enter caGrid Login Module Name:");

			caGridAuthSvcUrlLabel = new JLabel();
			caGridAuthSvcUrlLabel.setText("Enter caGrid Authentication Service URL:");
		    
			caGridDorianSvcUrlLabel = new JLabel();
			caGridDorianSvcUrlLabel.setText("Enter caGrid Dorian Service URL:");

			sdkGridLoginSvcNameLabel = new JLabel();
			sdkGridLoginSvcNameLabel.setText("Enter SDK Grid Login Service Name:");

			sdkGridLoginSvcUrlLabel = new JLabel();
			sdkGridLoginSvcUrlLabel.setText("Enter SDK Grid Login Service URL:");

			caGridAuthLoginModuleSettingsSubPanel = new JPanel();
			caGridAuthLoginModuleSettingsSubPanel.setLayout(new GridBagLayout());
			caGridAuthLoginModuleSettingsSubPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "caGrid Authentication Login Module Options",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
		    
			caGridAuthLoginModuleSettingsSubPanel.add(caGridEnvironmentLabel, gridBagConstraints10);
			caGridAuthLoginModuleSettingsSubPanel.add(getCaGridEnvironmentComboBox(), gridBagConstraints11);
			caGridAuthLoginModuleSettingsSubPanel.add(caGridAuthSvcUrlLabel, gridBagConstraints20);
			caGridAuthLoginModuleSettingsSubPanel.add(getCaGridAuthSvcUrlField(), gridBagConstraints21);
			caGridAuthLoginModuleSettingsSubPanel.add(caGridDorianSvcUrlLabel, gridBagConstraints30);
			caGridAuthLoginModuleSettingsSubPanel.add(getCaGridDorianSvcUrlField(), gridBagConstraints31);
			caGridAuthLoginModuleSettingsSubPanel.add(sdkGridLoginSvcNameLabel, gridBagConstraints40);
			caGridAuthLoginModuleSettingsSubPanel.add(getSdkGridLoginSvcNameField(), gridBagConstraints41);
			caGridAuthLoginModuleSettingsSubPanel.add(sdkGridLoginSvcUrlLabel, gridBagConstraints50);
			caGridAuthLoginModuleSettingsSubPanel.add(getSdkGridLoginSvcUrlField(), gridBagConstraints51);
			caGridAuthLoginModuleSettingsSubPanel.add(caGridLoginModuleNameLabel, gridBagConstraints60);
			caGridAuthLoginModuleSettingsSubPanel.add(getCaGridLoginModuleNameField(), gridBagConstraints61);
			caGridAuthLoginModuleSettingsSubPanel.add(caGridDorianSvcUrlLabel, gridBagConstraints70);
			caGridAuthLoginModuleSettingsSubPanel.add(getCaGridDorianSvcUrlField(), gridBagConstraints71);
			
			caGridAuthLoginModuleSettingsSubPanel.validate();
		}
		return caGridAuthLoginModuleSettingsSubPanel;
	}

	/**
	 * This method initializes appServerSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAppServerSettingsPanel() {
		if (appServerSettingsPanel == null) {
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.gridy = 1;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.gridx = 0;

			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.insets = new java.awt.Insets(10, 2, 10, 2);
			gridBagConstraints11.gridy = 1;
			//gridBagConstraints11.weighty = 1.0D;
			gridBagConstraints11.weightx = 1.0D;  
			gridBagConstraints11.gridwidth = 2;

			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints20.gridy = 2;
			gridBagConstraints20.insets = new java.awt.Insets(10, 2, 10, 2);
			gridBagConstraints20.gridx = 0;            

			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints21.gridy = 2;
			gridBagConstraints21.gridx = 1;
			gridBagConstraints21.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints21.gridwidth = 2;
			//gridBagConstraints21.weighty = 1.0D;
			gridBagConstraints21.weightx = 1.0D;  
	
			serverTypeLabel = new JLabel();
			serverTypeLabel.setText("Select Server Type:");

			serverUrlLabel = new JLabel();
			serverUrlLabel.setText("Enter Server URL:");

			appServerSettingsPanel = new JPanel();
			appServerSettingsPanel.setLayout(new GridBagLayout());
			appServerSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Define Application Server Properties",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
		    
			appServerSettingsPanel.add(serverTypeLabel, gridBagConstraints10);
			appServerSettingsPanel.add(getServerTypeComboBox(), gridBagConstraints11);
			appServerSettingsPanel.add(serverUrlLabel, gridBagConstraints20);
			appServerSettingsPanel.add(getServerUrlField(), gridBagConstraints21);
			
			appServerSettingsPanel.validate();
		}
		return appServerSettingsPanel;
	}
	
	/**
	 * This method initializes advancedSettingsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAdvancedSettingsPanel() {
		if (advancedSettingsPanel == null) {
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

			cachePathLabel = new JLabel();
			cachePathLabel.setText("Enter EHCache Path:");

			advancedSettingsPanel = new JPanel();
			advancedSettingsPanel.setLayout(new GridBagLayout());
			advancedSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Define Advanced Properties",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
		    
			advancedSettingsPanel.add(cachePathLabel, gridBagConstraints10);
			advancedSettingsPanel.add(getCachePathField(), gridBagConstraints11);
			
			advancedSettingsPanel.validate();

		}
		return advancedSettingsPanel;
	}
	
	private Map<String,String> getGeneratorPropsMap(){
		Map<String,String> generatorPropsMap=new HashMap<String,String>();
		
		// Project properties
		generatorPropsMap.put("output.dir", (getProjectDirField().getText() + File.separator + "output").replace('\\', '/'));
		generatorPropsMap.put("SDK_INSTALL_DIR", getSdkInstallDirField().getText().replace('\\', '/'));
		
		generatorPropsMap.put("PROJECT_NAME", getProjectNameField().getText());
		generatorPropsMap.put("NAMESPACE_PREFIX", getNamespacePrefixField().getText());
		generatorPropsMap.put("WEBSERVICE_NAME", getWebServiceNameField().getText());
		
		// Model properties
		generatorPropsMap.put("MODEL_FILE_PATH", getModelFileField().getText().replace('\\', '/'));
		generatorPropsMap.put("MODEL_FILE_TYPE", modelFileTypeOptionsMap.get(getModelFileTypeField().getSelectedItem().toString()));
		generatorPropsMap.put("LOGICAL_MODEL", getLogicalModelField().getText());
		generatorPropsMap.put("DATA_MODEL", getDataModelField().getText());
		generatorPropsMap.put("INCLUDE_PACKAGE", getIncludePackageField().getText());
		generatorPropsMap.put("EXCLUDE_PACKAGE", getExcludePackageField().getText());
		generatorPropsMap.put("EXCLUDE_NAME", getExcludeNameField().getText());
		generatorPropsMap.put("EXCLUDE_NAMESPACE", getExcludeNamespaceField().getText());
		
		// Code Generation properties
		generatorPropsMap.put("VALIDATE_LOGICAL_MODEL", Boolean.valueOf(validateLogicalModelCheckBox.isSelected()).toString() );
		generatorPropsMap.put("VALIDATE_MODEL_MAPPING", Boolean.valueOf(validateModelMappingCheckBox.isSelected()).toString() );
		generatorPropsMap.put("VALIDATE_GME_TAGS", Boolean.valueOf(validateGmeTagsCheckBox.isSelected()).toString() );
		generatorPropsMap.put("GENERATE_HIBERNATE_MAPPING", Boolean.valueOf(generateHibernateMappingCheckBox.isSelected()).toString() );
		generatorPropsMap.put("GENERATE_BEANS", Boolean.valueOf(generateBeansCheckBox.isSelected()).toString() );
		generatorPropsMap.put("GENERATE_CASTOR_MAPPING", Boolean.valueOf(generateCastorMappingCheckBox.isSelected()).toString() );
		generatorPropsMap.put("GENERATE_XSD", Boolean.valueOf(generateXsdCheckBox.isSelected()).toString() );
		generatorPropsMap.put("GENERATE_XSD_WITH_GME_TAGS", Boolean.valueOf(generateXsdWithGmeTagsCheckBox.isSelected()).toString() );
		generatorPropsMap.put("GENERATE_XSD_WITH_PERMISSIBLE_VALUES", Boolean.valueOf(generateXsdWithPermissibleValuesCheckBox.isSelected()).toString() );
		generatorPropsMap.put("GENERATE_WSDD", Boolean.valueOf(generateWsddCheckBox.isSelected()).toString() );
		generatorPropsMap.put("GENERATE_HIBERNATE_VALIDATOR", Boolean.valueOf(generateHibernateValidatorCheckBox.isSelected()).toString() );
		
		// DB Connection properties
		generatorPropsMap.put("DATABASE_TYPE", databaseTypeOptionsMap.get(getDatabaseTypeComboBox().getSelectedItem().toString()));
		generatorPropsMap.put("USE_JNDI_BASED_CONNECTION", Boolean.valueOf(useJndiBasedConnectionCheckBox.isSelected()).toString() );
		generatorPropsMap.put("DB_JNDI_URL", getDbJndiUrlField().getText());
		generatorPropsMap.put("DB_CONNECTION_URL", getDbConnectionUrlField().getText());
		generatorPropsMap.put("DB_USERNAME", getDbUsernameField().getText());
		generatorPropsMap.put("DB_PASSWORD", getDbPasswordField().getText());
		generatorPropsMap.put("DB_DRIVER", getDbDriverField().getText());
		generatorPropsMap.put("DB_DIALECT", getDbDialectField().getText());
		
		// Writable API properties
		generatorPropsMap.put("ENABLE_WRITABLE_API_EXTENSION", Boolean.valueOf(enableWritableApiExtensionCheckBox.isSelected()).toString() );
		generatorPropsMap.put("IDENTITY_GENERATOR_TAG", getIdentityGeneratorTagField().getText());
		generatorPropsMap.put("CADSR_CONNECTION_URL", getCaDsrConnectionUrlField().getText());
		
		// Common Logging Module DB Connection properties
		generatorPropsMap.put("ENABLE_COMMON_LOGGING_MODULE", Boolean.valueOf(enableCommonLoggingModuleCheckBox.isSelected()).toString() );
		generatorPropsMap.put("CLM_PROJECT_NAME", getClmProjectName().getText());		
		generatorPropsMap.put("CLM_DB_CONNECTION_URL", getClmDbConnectionUrlField().getText());
		generatorPropsMap.put("CLM_DB_USERNAME", getClmDbUsernameField().getText());
		generatorPropsMap.put("CLM_DB_PASSWORD", getClmDbPasswordField().getText());
		generatorPropsMap.put("CLM_DB_DRIVER", getClmDbDriverField().getText());
		
		// Security properties
		generatorPropsMap.put("ENABLE_SECURITY", Boolean.valueOf(enableSecurityCheckBox.isSelected()).toString() );
		generatorPropsMap.put("ENABLE_INSTANCE_LEVEL_SECURITY", Boolean.valueOf(enableInstanceLevelSecurityCheckBox.isSelected()).toString() );
		generatorPropsMap.put("ENABLE_ATTRIBUTE_LEVEL_SECURITY", Boolean.valueOf(enableAttributeLevelSecurityCheckBox.isSelected()).toString() );
		generatorPropsMap.put("CSM_PROJECT_NAME", getCsmProjectNameField().getText());
		generatorPropsMap.put("CACHE_PROTECTION_ELEMENTS", Boolean.valueOf(cacheProtectionElementsCheckBox.isSelected()).toString() );
		
		// CSM DB Connection properties
		generatorPropsMap.put("CSM_USE_DB_CONNECTION_SETTINGS", Boolean.valueOf(csmUseDbConnectionSettingsCheckBox.isSelected()).toString() );
		generatorPropsMap.put("CSM_USE_JNDI_BASED_CONNECTION", Boolean.valueOf(csmUseJndiBasedConnectionCheckBox.isSelected()).toString() );
		generatorPropsMap.put("CSM_DB_JNDI_URL", getCsmDbJndiUrlField().getText());
		generatorPropsMap.put("CSM_DB_CONNECTION_URL", getCsmDbConnectionUrlField().getText());
		generatorPropsMap.put("CSM_DB_USERNAME", getCsmDbUsernameField().getText());
		generatorPropsMap.put("CSM_DB_PASSWORD", getCsmDbPasswordField().getText());
		generatorPropsMap.put("CSM_DB_DRIVER", getCsmDbDriverField().getText());
		generatorPropsMap.put("CSM_DB_DIALECT", getCsmDbDialectField().getText());
		
		// caGrid Authentication properties
		generatorPropsMap.put("ENABLE_GRID_LOGIN_MODULE", Boolean.valueOf(enableCaGridLoginModuleCheckBox.isSelected()).toString() );
		generatorPropsMap.put("ENABLE_CSM_LOGIN_MODULE", Boolean.valueOf(!enableCaGridLoginModuleCheckBox.isSelected()).toString() );
		generatorPropsMap.put("CAGRID_LOGIN_MODULE_NAME", getCaGridLoginModuleNameField().getText());
		generatorPropsMap.put("CAGRID_AUTHENTICATION_SERVICE_URL", getCaGridAuthSvcUrlField().getText());
		generatorPropsMap.put("CAGRID_DORIAN_SERVICE_URL", getCaGridDorianSvcUrlField().getText());
		generatorPropsMap.put("SDK_GRID_LOGIN_SERVICE_NAME", getSdkGridLoginSvcNameField().getText());
		generatorPropsMap.put("SDK_GRID_LOGIN_SERVICE_URL", getSdkGridLoginSvcUrlField().getText());
		
		// Application Server Properties
		generatorPropsMap.put("SERVER_TYPE", getServerTypeComboBox().getSelectedItem().toString());
		generatorPropsMap.put("SERVER_URL", getServerUrlField().getText());

		// Advanced Settings properties	
		generatorPropsMap.put("CACHE_PATH", getCachePathField().getText());

		return generatorPropsMap;
	}
} 
