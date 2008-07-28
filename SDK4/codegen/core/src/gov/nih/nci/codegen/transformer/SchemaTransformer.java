package gov.nih.nci.codegen.transformer;

import gov.nih.nci.codegen.ArtifactHandler;
import gov.nih.nci.codegen.GenerationException;
import gov.nih.nci.codegen.GeneratorError;
import gov.nih.nci.codegen.GeneratorErrors;
import gov.nih.nci.codegen.Transformer;
import gov.nih.nci.codegen.artifact.BaseArtifact;
import gov.nih.nci.codegen.util.TransformerUtils;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAssociation;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAssociationEnd;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAttribute;
import gov.nih.nci.ncicb.xmiinout.domain.UMLClass;
import gov.nih.nci.ncicb.xmiinout.domain.UMLModel;
import gov.nih.nci.ncicb.xmiinout.domain.UMLPackage;
import gov.nih.nci.ncicb.xmiinout.domain.UMLTaggableElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.mmbase.util.Encode;

/**
 * Produces an XML schema for the project UML Model
 * <p>
 *
 * @author Daniel Dumitru
 * @version 1.0
 */
public class SchemaTransformer implements Transformer { 


	private static Logger log = Logger.getLogger(SchemaTransformer.class);

	private GeneratorErrors generatorErrors = new GeneratorErrors();

	private ArtifactHandler artifactHandler;
	
	private Encode xmlEncoder = new Encode("ESCAPE_XML");

	private String namespaceUriPrefix;
	
	private boolean useGMETags = false;

	private boolean enabled = true;
	
	private String name = SchemaTransformer.class.getName();
	
	protected TransformerUtils transformerUtils;

	public void setTransformerUtils(TransformerUtils transformerUtils) {
		this.transformerUtils = transformerUtils;
	}
	
	/* @param model The UMLModel containing the classes for which a 
	 * Castor Mapping file should be generated
	 * @see gov.nih.nci.codegen.Transformer#execute(gov.nih.nci.ncicb.xmiinout.domain.UMLModel)
	 */	
	public GeneratorErrors execute(UMLModel model)
	{
		Hashtable<String, Collection<UMLClass>> pkgColl = new Hashtable<String, Collection<UMLClass>>();
		List<UMLClass> classColl = new ArrayList<UMLClass>();
		log.debug("Model name: " + model.getName());
		
		try {
			if (useGMETags){
				setModelNamespace(model);//if set, override default Namespace prefix
				transformerUtils.collectPackages(model.getPackages(), pkgColl, classColl,namespaceUriPrefix);
			} else {
				transformerUtils.collectPackages(model.getPackages(), pkgColl, classColl);
			}

			processPackages(pkgColl, classColl);
		} catch (GenerationException ge) {
			log.error("ERROR: ", ge);
			generatorErrors.addError(new GeneratorError(getName() + ": " + ge.getMessage(), ge));
		}
		
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
	private void processPackages(Hashtable<String, Collection<UMLClass>> pkgColl, List<UMLClass> classColl)
	{
		BaseArtifact artifact;
		Document doc = null;

		for (Enumeration e = pkgColl.keys(); e.hasMoreElements() ;) {
			try {
				String pkgName = (String)e.nextElement();
				Collection klasses = (Collection)pkgColl.get(pkgName);
				log.debug("pkg.getName: " + pkgName + " has " + klasses.size() + " classes");

				artifact = new BaseArtifact(transformerUtils);
				artifact.createSourceName(pkgName);

				doc = generateRepository(classColl, klasses);

				XMLOutputter p = new XMLOutputter();
				p.setFormat(Format.getPrettyFormat());
				artifact.setContent(p.outputString(doc));

				artifactHandler.handleArtifact(artifact);
			} catch(GenerationException ge) {
				log.error("ERROR: ", ge);
				generatorErrors.addError(new GeneratorError(getName() + ": " + ge.getMessage(), ge));
			}
		}
	}

	private List<Namespace> getAssociatedNamespaces(UMLClass klass, List<UMLPackage> associatedPackages) {
		List<Namespace> namespaces = new ArrayList<Namespace>();

		for (UMLPackage associatedPackage:associatedPackages) {
				String associatedURI = getNamespace(associatedPackage);
				String associatedPackageName=getPackageName(associatedPackage);
				log.debug("associatedURI: " + associatedURI);
				log.debug("associatedPackageName: " + associatedPackageName);
				log.debug("encoded associatedURI: " + encode(associatedURI));
				log.debug("encoded associatedPackageName: " + encode(associatedPackageName));				
				Namespace associatedNamespace = Namespace.getNamespace(encode(associatedPackageName),encode(associatedURI));
				namespaces.add(associatedNamespace);
		}
		return namespaces;
	}

	private List<Element> getAssociatedNamespaceImports(UMLClass klass, List<UMLPackage> associatedPackages, Namespace w3cNS) {
		HashSet<Element> elements = new HashSet<Element>();
		Vector<String> tmpList = new Vector<String>();
		
		for (UMLPackage associatedPackage : associatedPackages){
			String associatedPackageName=getPackageName(associatedPackage);
			
			if (!tmpList.contains(associatedPackageName)) {
				String associatedURI = getNamespace(associatedPackage); 
				log.debug("Import associatedURI: " + associatedURI);
				Element importElement = new Element("import", w3cNS);
				importElement.setAttribute("namespace",associatedURI);
				importElement.setAttribute("schemaLocation", associatedPackageName+".xsd");
				elements.add(importElement);
				tmpList.add(associatedPackageName);
			}
		}

		return sortImportStatements(elements);
	}

	private Document generateRepository(Collection<UMLClass> classColl, Collection<UMLClass> pkgClassCollection) {

		String caBIGNS_URI = null;

		UMLPackage pkg=null;
		for (Iterator i = pkgClassCollection.iterator(); i.hasNext();) {
			UMLClass klass = (UMLClass) i.next();
			pkg=klass.getPackage();
			break;
		}
		caBIGNS_URI = getNamespace(pkg);
		
		Namespace w3cNS = Namespace.getNamespace("xs","http://www.w3.org/2001/XMLSchema");
		Element schemaElem = new Element("schema", w3cNS);
		Namespace caBIGNS = Namespace.getNamespace(encode(caBIGNS_URI));
		schemaElem.addNamespaceDeclaration(caBIGNS);
		schemaElem.addNamespaceDeclaration(w3cNS);

		Attribute targetNSAttr = new Attribute("targetNamespace", caBIGNS_URI);
		schemaElem.setAttribute(targetNSAttr);
		
		Attribute formDefault = new Attribute("elementFormDefault","qualified");
		schemaElem.setAttribute(formDefault);	

		for (Iterator i = pkgClassCollection.iterator(); i.hasNext();) {
			UMLClass klass = (UMLClass) i.next();
			doMapping(klass, schemaElem, w3cNS);
		}

		Document doc = new Document();
		doc.setRootElement(schemaElem);
		return doc;
	}

	private void doMapping(UMLClass klass, Element schemaElem, Namespace w3cNS) {

		String superClassName = null;

		UMLClass superClass = null;
		try {
			superClass = transformerUtils.getSuperClass(klass);
		} catch(GenerationException ge){
			log.error("Exception caught while getting Superclass for " + klass.getName(), ge);
			generatorErrors.addError(new GeneratorError(getName() + ": " + ge.getMessage(), ge));
		}

		if (superClass != null) {
			String klassPackageName = getPackageName(klass.getPackage());
			String superClassPackageName = getPackageName(superClass.getPackage());

			if (klassPackageName.equals(superClassPackageName)){
				superClassName = getClassName(superClass);
			} else {
				superClassName = superClassPackageName + ':'+getClassName(superClass);
			}
		}
	
		Element classEl = new Element("element", w3cNS);
		classEl.setAttribute("name",getClassName(klass));
		classEl.setAttribute("type", getClassName(klass));
		
		if (transformerUtils.isAbstract(klass)){
			classEl.setAttribute("abstract", "true");
		}
		
		schemaElem.addContent(classEl);
		
		Element classE2 = new Element("complexType", w3cNS);
		classE2.setAttribute("name", getClassName(klass));
		schemaElem.addContent(classE2);
		
		addCaDSRAnnotation(klass, classE2, w3cNS);

		List<UMLPackage> associatedPackages = new ArrayList<UMLPackage>();
		
		if (superClassName!=null) {
			log.debug("superClassName: " + superClassName);

			Element complexContent = new Element("complexContent", w3cNS);
			Element extension = new Element("extension",w3cNS);
			extension.setAttribute("base", superClassName);
			complexContent.addContent(extension);
			classE2.addContent(complexContent);
			Element sequence = new Element("sequence",w3cNS);
			extension.addContent(sequence);

			List<UMLAttribute> primitiveCollectionAtts = new ArrayList<UMLAttribute>();
			
			//Do properties
			for (Iterator i = klass.getAttributes().iterator(); i.hasNext();) {
				UMLAttribute attr = (UMLAttribute) i.next();
				log.debug("att.getName(): " + attr.getName()); 

				// Only process non-static attributes
				log.debug("isStatic: " + transformerUtils.isStatic(attr));
				if (!transformerUtils.isStatic(attr)){
					Element attributeElement = new Element("attribute", w3cNS);
					attributeElement.setAttribute("name", getAttributeName(attr));
					String type = getName(transformerUtils.getDataType(attr));
					log.debug("Attribute type: " + type);
					if (type.startsWith("xs:collection")) { // handle primitive collections; e.g., collection<string>
						log.debug("Handling primitive collection type Name: " + type);    
						primitiveCollectionAtts.add(attr);
						continue;
					}
					attributeElement.setAttribute("type", type);
					extension.addContent(attributeElement);
				}
			}
			
			// TODO :: refactor			
			// process primitive collections; e.g., collection<string>
			for ( UMLAttribute att : primitiveCollectionAtts){
				addSequencePrimitiveCollectionElements(sequence, klass, att, w3cNS);			
			}

			for (Iterator i = transformerUtils.getAssociationEnds(klass).iterator(); i.hasNext();) {
				UMLAssociationEnd thisEnd = (UMLAssociationEnd) i.next();
				UMLAssociationEnd otherEnd = transformerUtils.getOtherAssociationEnd(thisEnd);
				collectAssociatedPackages(thisEnd, otherEnd, associatedPackages);
				addSequenceAssociationElement(sequence,klass,thisEnd,otherEnd,w3cNS);
			}

		} else { // superClassname == null
			Element sequence = new Element("sequence",w3cNS);
			classE2.addContent(sequence);

			List<UMLAttribute> primitiveCollectionAtts = new ArrayList<UMLAttribute>();
			
			//Do properties
			for (Iterator i = klass.getAttributes().iterator(); i.hasNext();) {
				UMLAttribute attr = (UMLAttribute) i.next();
				log.debug("att.getName(): " + attr.getName());

				// Only process non-static attributes
				log.debug("isStatic: " + transformerUtils.isStatic(attr));
				if (!transformerUtils.isStatic(attr)){

					Element attributeElement = new Element("attribute", w3cNS);
					attributeElement.setAttribute("name", getAttributeName(attr));

					String type = getName(transformerUtils.getDataType(attr));
					log.debug("Attribute type: " + type);
					
					if (type.startsWith("xs:collection")) { // handle primitive collections; e.g., collection<string>
						log.debug("Handling primitive collection type Name: " + type);    
						primitiveCollectionAtts.add(attr);
						continue;
					}				
					
					attributeElement.setAttribute("type", type);
					
					addCaDSRAnnotation(attr, attributeElement, w3cNS);
					
					classE2.addContent(attributeElement);
				}
			}
			
			// TODO :: refactor			
			// process primitive collections; e.g., collection<string>
			for ( UMLAttribute att : primitiveCollectionAtts){
				addSequencePrimitiveCollectionElements(sequence, klass, att, w3cNS);			
			}
			
			for (Iterator i = transformerUtils.getAssociationEnds(klass).iterator(); i.hasNext();) {
				UMLAssociationEnd thisEnd = (UMLAssociationEnd) i.next();
				UMLAssociationEnd otherEnd = transformerUtils.getOtherAssociationEnd(thisEnd);
				collectAssociatedPackages(thisEnd, otherEnd, associatedPackages);
				addSequenceAssociationElement(sequence,klass,thisEnd,otherEnd,w3cNS);
			}
		}
		
		//Add namespace declarations
		for(Namespace rNamespace: getAssociatedNamespaces(klass, associatedPackages)){
			schemaElem.addNamespaceDeclaration(rNamespace);
		}        

		//Add namespace import elements
		for (Element namespaceImportElement : getAssociatedNamespaceImports(klass, associatedPackages, w3cNS)){
			schemaElem.addContent(0,namespaceImportElement);
		}
	}
	
	private void addCaDSRAnnotation(UMLTaggableElement tgElt, Element elt, Namespace w3cNS) {
		String caDSRAnnotation = transformerUtils.getCaDSRAnnotationContent(tgElt);
		if (caDSRAnnotation != null){
			Element annotationElt = new Element("annotation", w3cNS);
			Element documentationElt = new Element("documentation", w3cNS);
			
			documentationElt.addContent(caDSRAnnotation);
			annotationElt.addContent(documentationElt);
			
			elt.addContent(annotationElt);
		}
	}

	private void addSequenceAssociationElement(Element sequence, UMLClass klass, UMLAssociationEnd thisEnd, UMLAssociationEnd otherEnd,Namespace w3cNS) {
		if (otherEnd.isNavigable()) {
			log.debug("sequence name: " + sequence.getName());
			log.debug("otherEnd getRoleName: " + otherEnd.getRoleName());
			String otherEndType = getClassName((UMLClass)(otherEnd.getUMLElement()));
			log.debug("otherEnd type: " + otherEndType);
			String thisEndType = getClassName((UMLClass)(thisEnd.getUMLElement()));
			log.debug("thisEnd type: " + thisEndType);

			Element associationElement = new Element("element", w3cNS);
			sequence.addContent(associationElement);

			associationElement.setAttribute("name", getRoleName(otherEnd)); //otherEnd.getRoleName()

			String maxOccurs = transformerUtils.getUpperBound(otherEnd);
			log.debug("maxOccurs: " + maxOccurs);

			// A collection - model association as an element with the association name that 
			// has a sequence of the associated type.  See GForge #1311.
			associationElement.setAttribute("minOccurs","0");   
			associationElement.setAttribute("maxOccurs","1");

			Element complexType = new Element("complexType",w3cNS);
			Element innerSequence = new Element("sequence", w3cNS);
			Element associatedObjElement = new Element("element", w3cNS);

			String associationPackage=null;
			String thisPackage=null;

			thisPackage = getPackageName(((UMLClass)(thisEnd.getUMLElement())).getPackage());
			associationPackage = getPackageName(((UMLClass)(otherEnd.getUMLElement())).getPackage());

			log.debug("associationPackage.equals(thisPackage): " + associationPackage.equals(thisPackage));

			String type = (associationPackage.equals(thisPackage)) ? otherEndType : associationPackage + ":" + otherEndType;

			associatedObjElement.setAttribute("ref", type);
			associatedObjElement.setAttribute("minOccurs","0");   
			associatedObjElement.setAttribute("maxOccurs",maxOccurs);  

			innerSequence.addContent(associatedObjElement);
			complexType.addContent(innerSequence);
			associationElement.addContent(complexType);
		}
	}
	
	private void collectAssociatedPackages(UMLAssociationEnd thisEnd, UMLAssociationEnd otherEnd, List<UMLPackage> associatedPackageNames) {
		if (otherEnd.isNavigable()) {

			String associationPackage = transformerUtils.getFullPackageName(((UMLClass)(otherEnd.getUMLElement())));
			String thisPackage = transformerUtils.getFullPackageName(((UMLClass)(thisEnd.getUMLElement())));
			
			log.debug("associationPackage.equals(thisPackage): " + associationPackage.equals(thisPackage));
			
			if (!associationPackage.equalsIgnoreCase(thisPackage)){
				associatedPackageNames.add( ((UMLClass)(otherEnd.getUMLElement())).getPackage());
			}
			
		}
	}
	
	private void addSequencePrimitiveCollectionElements(Element sequence, UMLClass klass, UMLAttribute att, Namespace w3cNS) {

			Element field = new Element("field");
			
			String name = att.getName();
			field.setAttribute("name", name ); 
			
			String type = transformerUtils.getDataType(att);
			String collectionType = type.substring(type.lastIndexOf("<")+1, type.lastIndexOf(">"));
			collectionType = getName(collectionType);
			log.debug("collectionType: " + collectionType);
			
			String collectionTypeName = collectionType.substring(collectionType.indexOf(":")+1,collectionType.length());
			
			Element associationElement = new Element("element", w3cNS);
			sequence.addContent(associationElement);

			associationElement.setAttribute("name", name);

			// A collection - model association as an element with the association name that 
			// has a sequence of the associated type.  See GForge #1311.
			associationElement.setAttribute("minOccurs","0");   
			associationElement.setAttribute("maxOccurs","1");

			Element complexType = new Element("complexType",w3cNS);
			Element innerSequence = new Element("sequence", w3cNS);
			Element associatedObjElement = new Element("element", w3cNS);

			associatedObjElement.setAttribute("name", collectionTypeName);
			associatedObjElement.setAttribute("type", collectionType);
			associatedObjElement.setAttribute("minOccurs","0");   
			associatedObjElement.setAttribute("maxOccurs","unbounded");  

			innerSequence.addContent(associatedObjElement);
			complexType.addContent(innerSequence);
			associationElement.addContent(complexType);    
	}	

	private String getName(String type) {
		String finalType = "xs:";
		
		if (type.indexOf('.') > 0){//java.util.Long, etc.
			type = type.substring(type.lastIndexOf('.')+1);
		}
		
		if ("collection".equalsIgnoreCase(type)
				|| "character".equalsIgnoreCase(type)
				|| "char".equalsIgnoreCase(type)) {
			finalType = finalType + "string";
		} else {
			finalType = finalType + type.toLowerCase();
			if (finalType.equals("xs:date")) {
				finalType = "xs:dateTime";
			}
		}
		return finalType;
	}

	/**
	 * @param classColl The List of classes to be sorted.  The caCOREMarshaller 
	 * has problem with forward references.
	 * @return The Sorted list of classes with Generalizations listed first.
	 */
	private List<Element> sortImportStatements(HashSet<Element> elementColl) 
	{
		class caCOREComparator implements Comparator {
			public int compare(Object obj1, Object obj2)
			{
				return (((Element)obj1).getAttribute("schemaLocation").getValue().compareTo(((Element)obj2).getAttribute("schemaLocation").getValue()));
			}
		};

		ArrayList<Element> list = new ArrayList<Element>(elementColl);
		Collections.sort(list, new caCOREComparator());
		return list;
	}
	
	private void setModelNamespace(UMLModel model){
		//override deploy.properties NAMESPACE_PREFIX property with GME namespace tag value, if it exists
		try {
			String modelNamespacePrefix = transformerUtils.getNamespace(model);
			if (modelNamespacePrefix != null) {
				if (!modelNamespacePrefix.endsWith("/"))
					modelNamespacePrefix=modelNamespacePrefix+"/";
				this.namespaceUriPrefix = modelNamespacePrefix.replace(" ", "_");
			}
		} catch (GenerationException ge) {
			log.error("ERROR: ", ge);
			generatorErrors.addError(new GeneratorError(getName() + ": Error getting the GME Namespace value for model: " + model.getName(), ge));
		}
	}
	
	private String getNamespace(UMLPackage pkg){
		if (useGMETags){
			try {
				String gmeNamespace = transformerUtils.getNamespace(pkg);
				if (gmeNamespace != null) return gmeNamespace;
			} catch (GenerationException ge) {
				log.error("ERROR: ", ge);
				generatorErrors.addError(new GeneratorError(getName() + ": Error getting the GME Namespace value for package: " + transformerUtils.getFullPackageName(pkg), ge));
			}
		}
		
		return namespaceUriPrefix + transformerUtils.getFullPackageName(pkg);
	}
	
	private String getPackageName(UMLPackage pkg){
		String namespacePkgName = null;
		if (useGMETags){
			try {
				namespacePkgName = transformerUtils.getNamespacePackageName(pkg);
				if (namespacePkgName!=null)
					return namespacePkgName;
			} catch(GenerationException ge) {
				log.error("ERROR: ", ge);
				generatorErrors.addError(new GeneratorError(getName() + ": Error getting the GME package name for package: " + transformerUtils.getFullPackageName(pkg), ge));
			}
		}
		return transformerUtils.getFullPackageName(pkg);
	}
	
	private String getClassName(UMLClass klass){
		if (useGMETags){
			try {
				String klassName = transformerUtils.getXMLClassName(klass);
				if (klassName!=null)
					return klassName;
			} catch(GenerationException ge) {
				log.error("ERROR: ", ge);
				generatorErrors.addError(new GeneratorError(getName() + ": Error getting the GME Class (XML Element) name for klass: " + transformerUtils.getFQCN(klass), ge));
			}
		}
		return klass.getName();
	}
	
	private String getAttributeName(UMLAttribute attr){
		if (useGMETags){
			try {
				String attrName = transformerUtils.getXMLAttributeName(attr);
				if (attrName!=null)
					return attrName;
			} catch(GenerationException ge) {
				log.error("ERROR: ", ge);
				generatorErrors.addError(new GeneratorError(getName() + ": Error getting the GME Attribute (XML Loc Ref) name for attribute: " + attr.getName(), ge));
			}
		}
		return attr.getName();
	}

	private String getRoleName(UMLAssociationEnd assocEnd){
		if (useGMETags){
			try {
				String rolename = transformerUtils.getXMLLocRef(assocEnd);
				if (rolename!=null)
					return rolename;
			} catch(GenerationException ge) {
				log.error("ERROR: ", ge);
				generatorErrors.addError(new GeneratorError(getName() + ": Error getting the GME rolename (XML Source or Target Location Reference) name for association roleName: " + assocEnd.getRoleName(), ge));
			}
		}
		return assocEnd.getRoleName();
	}

	/**
	 * @param namespaceUriPrefix e.g.: gme://caCORE.caCORE/3.2
	 */
	public void setNamespaceUriPrefix(String namespaceUriPrefix) {
		this.namespaceUriPrefix = namespaceUriPrefix.replace(" ", "_");
	}

	/**
	 * @param artifactHandler the artifactHandler to set; called by the
	 *        Spring framework
	 */
	public void setArtifactHandler(ArtifactHandler artifactHandler)
	{
		this.artifactHandler = artifactHandler;
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

	public void setUseGMETags(boolean useGMETags) {
		this.useGMETags = useGMETags;
	}
}
