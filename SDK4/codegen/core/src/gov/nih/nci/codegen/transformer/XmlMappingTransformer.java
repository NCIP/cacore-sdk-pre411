package gov.nih.nci.codegen.transformer;

import gov.nih.nci.codegen.ArtifactHandler;
import gov.nih.nci.codegen.GenerationException;
import gov.nih.nci.codegen.GeneratorError;
import gov.nih.nci.codegen.GeneratorErrors;
import gov.nih.nci.codegen.Transformer;
import gov.nih.nci.codegen.artifact.BaseArtifact;
import gov.nih.nci.codegen.util.Constant;
import gov.nih.nci.codegen.util.TransformerUtils;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAssociationEnd;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAttribute;
import gov.nih.nci.ncicb.xmiinout.domain.UMLClass;
import gov.nih.nci.ncicb.xmiinout.domain.UMLModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.mmbase.util.Encode;

/**
 *
 * @author caCORE SDK Team
 * @version 1.0
 */
public class XmlMappingTransformer implements Transformer { 


	private static Logger log = Logger.getLogger(XmlMappingTransformer.class);
	
	private GeneratorErrors generatorErrors = new GeneratorErrors();
	
	private ArtifactHandler artifactHandler;
	
	private Encode xmlEncoder = new Encode("ESCAPE_XML");	
	
	private String namespaceUriPrefix;

	private boolean includeAssociations = false;    
	private boolean includeFieldHandler = false;    

	private boolean enabled = true;
	
	private String name = XmlMappingTransformer.class.getName();	

	
	/* @param model The UMLModel containing the classes for which a 
	 * Castor Mapping file should be generated
	 * @see gov.nih.nci.codegen.Transformer#execute(gov.nih.nci.ncicb.xmiinout.domain.UMLModel)
	 */
	public GeneratorErrors execute(UMLModel model)
	{
		log.debug("Model name: " + model.getName());

		GeneratorErrors errors = new GeneratorErrors();
		Collection<UMLClass> classes = null;

		try {
			classes = TransformerUtils.getAllClasses(model);
		} catch(GenerationException ge){
			errors.addError(new GeneratorError(getName() + ": Error while retrieving classes to process: ", ge));
		}
		
		log.debug("classes size: " + classes.size());		
		processClasses(classes);
		
		return generatorErrors;
	}

	/* @param model The UMLModel containing the classes for which a 
	 * Castor Mapping file should be generated.  This method will run any
	 * necessary validations.
	 * @see gov.nih.nci.codegen.Transformer#validate(gov.nih.nci.ncicb.xmiinout.domain.UMLModel)
	 */
	public GeneratorErrors validate(UMLModel model)
	{
		return null;
	}	

	/**
	 * @param classColl The collection of classes for which
	 * a Castor Mapping file will be generated
	 */
	private void processClasses(Collection<UMLClass> classColl)
	{

		Document doc = generateMappingDoc(classColl);
		XMLOutputter p = new XMLOutputter();
		p.setFormat(Format.getPrettyFormat());	

		BaseArtifact artifact = new BaseArtifact();
		artifact.setContent(p.outputString(doc));

		try {
			artifactHandler.handleArtifact(artifact);
		} catch(GenerationException ge) {
			log.error("ERROR: ", ge);
			
		}        
	}

	/**
	 * @param classColl The List of classes for which a Castor Mapping file should be generated.
	 * @return The JDOM Document for the Castor Mapping file.
	 */
	private Document generateMappingDoc(Collection<UMLClass> classColl) {

		Element mappingEl = new Element("mapping");
		DocType docType = new DocType("mapping","-//EXOLAB/Castor Object Mapping DTD Version 1.0//EN","http://www.castor.org/mapping.dtd");

		Document doc = new Document();
		doc.setDocType(docType);
		Collection sortedClassifier = sortClasses(classColl);
		for (Iterator i = sortedClassifier.iterator(); i.hasNext();) {
			UMLClass klass = (UMLClass) i.next();
			doMapping(klass, mappingEl);
		}
		doc.setRootElement(mappingEl);
		return doc;
	}

	/**
	 * @param classColl The List of classes to be sorted.  The caCOREMarshaller 
	 * has problem with forward references.
	 * @return The Sorted list of classes with Generalizations listed first.
	 */
	private Collection sortClasses(Collection<UMLClass> classColl) 
	{
		// The caCOREMarshaller has problem with forward references. Sort
		// the class with the list generalizations to the top.
		class caCOREComparator implements Comparator {
			public int compare(Object obj1, Object obj2)
			{
				int weight = determineWeight((UMLClass)obj1) - determineWeight((UMLClass)obj2);
				log.debug("weight: " + weight);
				return weight;
			}

			private int determineWeight(UMLClass obj)
			{
				int count = -1;
				UMLClass superClass = obj;
				try {
					do {
						superClass = TransformerUtils.getSuperClass(superClass);
						count++;
					} while (superClass!=null);

				} catch(GenerationException ge){
					log.error("ERROR: ", ge);
					generatorErrors.addError(new GeneratorError(getName() + ": " + ge.getMessage(), ge));
				}
				return count;
			}
		};

		ArrayList<UMLClass> list = new ArrayList<UMLClass>(classColl);
		Collections.sort(list, new caCOREComparator());
		return list;
	}

	/**
	 * @param klass The UML Class for which a Namespace URI will be generated
	 * @return The Namespace URI generated for the UML Class 
	 */
	private String getNamespaceURI(UMLClass klass) {
		String packageName = TransformerUtils.getFullPackageName(klass);
		
		StringBuffer nsURI = new StringBuffer();
		nsURI.append(namespaceUriPrefix);
		nsURI.append(packageName);
		return nsURI.toString();
	}

	/**
	 * @param klass The UMLClass for which a Mapping Element should be generated
	 * @param mappingEl The Mapping Element to which the Class information will be added.
	 */
	private void doMapping(UMLClass klass, Element mappingEl) { 
		String superClassName =null;
		
		UMLClass superClass = null;
		try {
			superClass = TransformerUtils.getSuperClass(klass);
		} catch (GenerationException ge){
			log.error("ERROR: ", ge);
			generatorErrors.addError(new GeneratorError(getName() + ": " + ge.getMessage(), ge));
		}	

		if (superClass != null) {
			superClassName = TransformerUtils.getFullPackageName(superClass)+ Constant.DOT+superClass.getName();
		} 	    
		String classElName = "class";
		Element classEl = new Element(classElName);
		mappingEl.addContent(classEl);
		classEl.setAttribute("name", TransformerUtils.getFullPackageName(klass)+Constant.DOT+klass.getName());
		
		
		String idAttrName = "";
		try {
			idAttrName = TransformerUtils.getClassIdAttr(klass).getName();
			log.debug("className: " + klass.getName() + "; idAttrName: " + idAttrName);
		} catch (GenerationException ge){
			log.error("ERROR: ", ge);
			generatorErrors.addError(new GeneratorError(getName() + ": " + ge.getMessage(), ge));
		}
				
		classEl.setAttribute("identity", idAttrName);
		if (superClassName!=null){
			classEl.setAttribute("extends", superClassName);
		}
		Element maptoelement = new Element("map-to");
		maptoelement.setAttribute("xml", klass.getName());
		String nsURI = getNamespaceURI(klass);
		maptoelement.setAttribute("ns-uri",nsURI);
		classEl.addContent(maptoelement);
		
		List<UMLAttribute> primitiveCollectionAtts = new ArrayList<UMLAttribute>();

		//process fields
		for (Iterator i = klass.getAttributes().iterator(); i.hasNext();) {
			UMLAttribute att = (UMLAttribute) i.next();
			Element field = new Element("field");
			field.setAttribute("name", att.getName());
			log.debug("Field name: " + att.getName());

			String type = TransformerUtils.getDataType(att);
			String qName = getQualifiedTypeName(type);
			if (qName.equalsIgnoreCase("collection")) {
				log.debug("Handling type 'collection' - qName: " + qName);            	 
				field.setAttribute("type", "string");
				field.setAttribute("collection", qName);
				Namespace namespace = Namespace.getNamespace(qName,nsURI);

				Element bind = new Element("bind-xml");
				bind.setAttribute("name", qName + ":" + att.getName());
				bind.setAttribute("QName-prefix",qName,namespace);
				bind.setAttribute("node", "attribute");
				field.addContent(bind);
			} else if (qName.startsWith("collection")) { // handle primitive collections; e.g., collection<string>
				log.debug("Handling primitive collection type Name: " + qName);    
				primitiveCollectionAtts.add(att);
				continue;
			} else {
				field.setAttribute("type", getQualifiedTypeName(TransformerUtils.getDataType(att)));
				Element bind = new Element("bind-xml");
				bind.setAttribute("name", att.getName());
				bind.setAttribute("node", "attribute");
				field.addContent(bind);         	 
			}
			
			classEl.addContent(field);
		}
		
		// TODO :: refactor
		// process primitive collections; e.g., collection<string>
		for ( UMLAttribute att : primitiveCollectionAtts){

			Element field = new Element("field");
			
			String name = att.getName();
			field.setAttribute("name", name ); 
			
			String type = TransformerUtils.getDataType(att);
			String collectionType = type.substring(type.lastIndexOf("<")+1, type.lastIndexOf(">"));
			collectionType = getQualifiedTypeName(collectionType);
			log.debug("collectionType: " + collectionType);
			
			field.setAttribute("type", collectionType);
			
			field.setAttribute("collection", "collection" );
			if (includeFieldHandler) {
				field.setAttribute("handler", "gov.nih.nci.system.client.util.xml.CastorCollectionFieldHandler" );
			}

			Element bind = new Element("bind-xml");

			bind.setAttribute("name", collectionType );
			bind.setAttribute("type", collectionType);
			bind.setAttribute("location", name ); 
			bind.setAttribute("node", "element");

			field.addContent(bind);
			classEl.addContent(field);			
		}

		if (includeAssociations) {
			log.debug("*********** klass: " + klass.getName());
			log.debug("*********** TransformerUtils.getAssociationEnds(klass).size(): " + TransformerUtils.getAssociationEnds(klass).size());

			for (Iterator i = TransformerUtils.getAssociationEnds(klass).iterator(); i.hasNext();) {
				UMLAssociationEnd thisEnd = (UMLAssociationEnd) i.next();
				UMLAssociationEnd otherEnd = TransformerUtils.getOtherAssociationEnd(thisEnd);
				addSequenceAssociationElement(classEl, thisEnd, otherEnd);
			}
		}

	}

	/**
	 * @param mappingEl The Mapping Element to which content should be added
	 * @param thisEnd The primary end of the UMLAssociation
	 * @param otherEnd The other end of the UMLAssociation
	 */
	private void addSequenceAssociationElement(Element mappingEl, UMLAssociationEnd thisEnd, UMLAssociationEnd otherEnd) { 
		if (otherEnd.isNavigable()) {
			// If classes belong to the same package then do not qualify the association

			String otherEndTypeName = ((UMLClass)(otherEnd.getUMLElement())).getName();
			log.debug("mappingEl.getName(): " + mappingEl);
			log.debug("thisEnd.getRoleName(): " + thisEnd.getRoleName());
			log.debug("thisEnd.getType().getName(): " + ((UMLClass)(thisEnd.getUMLElement())).getName());    		 
			log.debug("otherEnd.getRoleName(): " + otherEnd.getRoleName()); 
			log.debug("otherEnd.getType().getName(): " + otherEndTypeName);  
			
			log.debug("TransformerUtils.isMany2One(thisEnd, otherEnd) || TransformerUtils.isOne2One(thisEnd, otherEnd): " + (TransformerUtils.isMany2One(thisEnd, otherEnd) || TransformerUtils.isOne2One(thisEnd, otherEnd)));
			log.debug("TransformerUtils.isMany2Many(thisEnd, otherEnd) || TransformerUtils.isOne2Many(thisEnd, otherEnd): " + (TransformerUtils.isMany2Many(thisEnd, otherEnd) || TransformerUtils.isOne2Many(thisEnd, otherEnd))); 

			if (TransformerUtils.isMany2One(thisEnd, otherEnd) || TransformerUtils.isOne2One(thisEnd, otherEnd)) {

				log.debug("UML13Utils.isMany2One(thisEnd, otherEnd): " + true);
				log.debug("lowerBound: " + TransformerUtils.getLowerBound(otherEnd));
				log.debug("upperBound: " + TransformerUtils.getUpperBound(otherEnd));

				Element field = new Element("field");
				field.setAttribute("name", otherEnd.getRoleName() ); //otherEnd.getName());
				String associationPackage = TransformerUtils.getFullPackageName((UMLClass)otherEnd.getUMLElement());
				field.setAttribute("type", associationPackage + Constant.DOT + ( (UMLClass)otherEnd.getUMLElement() ).getName() );
				if (includeFieldHandler) {
					field.setAttribute("handler", "gov.nih.nci.system.client.util.xml.CastorDomainObjectFieldHandler" );
				}
				Element bind = new Element("bind-xml");
				bind.setAttribute("name", otherEndTypeName );
				bind.setAttribute("type", associationPackage + Constant.DOT + otherEndTypeName );
				bind.setAttribute("location", otherEnd.getRoleName() ); //otherEnd.getName());	                 
				bind.setAttribute("node", "element");
				field.addContent(bind);

				mappingEl.addContent(field);

			} else if (TransformerUtils.isMany2Many(thisEnd, otherEnd) || TransformerUtils.isOne2Many(thisEnd, otherEnd)){
				
				log.debug("UML13Utils.isMany2Many(thisEnd, otherEnd): " + TransformerUtils.isMany2Many(thisEnd, otherEnd));
				log.debug("UML13Utils.isOne2Many(thisEnd, otherEnd): " + TransformerUtils.isOne2Many(thisEnd, otherEnd));
				log.debug("lowerBound: " + TransformerUtils.getLowerBound(otherEnd));
				log.debug("upperBound: " + TransformerUtils.getUpperBound(otherEnd));
				
				Element field = new Element("field");
				field.setAttribute("name", otherEnd.getRoleName() ); //otherEnd.getName());
				String associationPackage = TransformerUtils.getFullPackageName((UMLClass)otherEnd.getUMLElement());
				field.setAttribute("type", associationPackage + Constant.DOT + otherEndTypeName);
				field.setAttribute("collection", "collection" );
				if (includeFieldHandler) {
					field.setAttribute("handler", "gov.nih.nci.system.client.util.xml.CastorCollectionFieldHandler" );
				}

				Element bind = new Element("bind-xml");

				bind.setAttribute("name", otherEndTypeName );
				bind.setAttribute("type", associationPackage+Constant.DOT + otherEndTypeName);
				bind.setAttribute("location", otherEnd.getRoleName() ); //otherEnd.getName());
				bind.setAttribute("node", "element");

				field.addContent(bind);
				mappingEl.addContent(field);

			}
		}
	}

	/**
	 * @param type The UMLAttribute type
	 * @return The corresponding Castor Mapping attribute type
	 */
	private String getQualifiedTypeName(String type) {
		
		if (type.indexOf('.') > 0){//java.util.Long, etc.
			//type = type.substring(type.lastIndexOf('.')+1);
			return type;
		}
		
		if ("Character".equalsIgnoreCase(type) || "char".equalsIgnoreCase(type)) {
			return "char";
		}

		if ("HashSet".equalsIgnoreCase(type)) {
			return "set";
		}

		if ("HashMap".equalsIgnoreCase(type)) {
			return "map";
		}

		log.debug("*** type: " + type);
		return type.toLowerCase();
	}

	/**
	 * @param includeAssociations set the flag to indicate whether or not 
	 * associations should be processed during the Mapping generation
	 */
	public void setIncludeAssociations(boolean includeAssociations) {
		this.includeAssociations = includeAssociations;
	}

	/**
	 * @param includeFieldHandler set the flag to indicate whether or not a 
	 * Castor Mapping Field Handler should be generated
	 */
	public void setIncludeFieldHandler(boolean includeFieldHandler) {
		this.includeFieldHandler = includeFieldHandler;
	}

	/**
	 * @param artifactHandler the artifactHandler to set; called by the
	 *        Spring framework
	 */
	public void setArtifactHandler(ArtifactHandler artifactHandler)
	{
		this.artifactHandler = artifactHandler;
	}

	public void setNamespaceUriPrefix(String namespaceUriPrefix) {
		this.namespaceUriPrefix = namespaceUriPrefix.replace(" ", "_");
	}
	
	private String encode(String string){
		return xmlEncoder.encode(string.replace(" ", "_"));	
	}


	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	public Boolean isEnabled() {
		return enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
