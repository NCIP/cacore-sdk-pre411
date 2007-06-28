package gov.nih.nci.codegen.transformer;

import gov.nih.nci.codegen.ArtifactHandler;
import gov.nih.nci.codegen.GenerationException;
import gov.nih.nci.codegen.GeneratorError;
import gov.nih.nci.codegen.GeneratorErrors;
import gov.nih.nci.codegen.Transformer;
import gov.nih.nci.codegen.artifact.BaseArtifact;
import gov.nih.nci.codegen.util.TransformerUtils;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAssociationEnd;
import gov.nih.nci.ncicb.xmiinout.domain.UMLAttribute;
import gov.nih.nci.ncicb.xmiinout.domain.UMLClass;
import gov.nih.nci.ncicb.xmiinout.domain.UMLModel;
import gov.nih.nci.ncicb.xmiinout.domain.UMLPackage;
import gov.nih.nci.ncicb.xmiinout.domain.UMLTaggableElement;

import java.util.ArrayList;
import java.util.Collection;
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

	/* @param model The UMLModel containing the classes for which a 
	 * Castor Mapping file should be generated
	 * @see gov.nih.nci.codegen.Transformer#execute(gov.nih.nci.ncicb.xmiinout.domain.UMLModel)
	 */	
	public GeneratorErrors execute(UMLModel model)
	{
		
		// TODO :: Add error checking... or is that part of the validation process?
		Hashtable<UMLPackage, Collection<UMLClass>> pkgColl = new Hashtable<UMLPackage, Collection<UMLClass>>();
		List<UMLClass> classColl = new ArrayList<UMLClass>();

		log.debug("Model name: " + model.getName());
		TransformerUtils.collectPackages(model.getPackages(), pkgColl, classColl);

		processPackages(pkgColl, classColl);

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
	private void processPackages(Hashtable<UMLPackage, Collection<UMLClass>> pkgColl, List<UMLClass> classColl)
	{
		BaseArtifact artifact;
		Document doc = null;
		String fQPkgName = null;

		for (Enumeration e = pkgColl.keys() ; e.hasMoreElements() ;) {
			UMLPackage pkg = (UMLPackage)e.nextElement();
			Collection klasses = (Collection)pkgColl.get(pkg);
			log.debug("pkg.getName: " + pkg.getName() + " has " + klasses.size() + " classes");

			fQPkgName = TransformerUtils.getFullPackageName(pkg);

			artifact = new BaseArtifact();
			artifact.createSourceName(pkg);

			doc = generateRepository(classColl, fQPkgName, pkgColl.get(pkg));

			XMLOutputter p = new XMLOutputter();
			p.setFormat(Format.getPrettyFormat());
			artifact.setContent(p.outputString(doc));

			try {
				artifactHandler.handleArtifact(artifact);
			} catch(GenerationException ge) {
				log.error("ERROR: ", ge);
				generatorErrors.addError(new GeneratorError(ge.getMessage(), ge));
			}
		}
	}

	private List<Namespace> getRelatedNamespaces(Collection<UMLClass> classColl, String fQPkgName) {
		List<Namespace> namespaces = new ArrayList<Namespace>();

		for (UMLClass klass:classColl) {
			String relatedPackageName = TransformerUtils.getFullPackageName(klass);
			log.debug("related package name: " + relatedPackageName + "; current package name: " + fQPkgName);
			if(!relatedPackageName.equals(fQPkgName)) {
				
				String relatedURI = namespaceUriPrefix + relatedPackageName;
				log.debug("relatedURI: " + relatedURI);
				Namespace relatedNamespace = Namespace.getNamespace(relatedPackageName,encode(relatedURI));
				namespaces.add(relatedNamespace);
			}
		}
		return namespaces;
	}

	private HashSet getRelatedNamespacesImports(Collection<UMLClass> classColl, String fQPkgName, Namespace w3cNS) {
		HashSet<Element> elements = new HashSet<Element>();
		Vector<String> tmpList = new Vector<String>();
		for (Iterator i = classColl.iterator(); i.hasNext();) {
			UMLClass klass = (UMLClass)i.next();
			String relatedPackageName = TransformerUtils.getFullPackageName(klass);
			if(!relatedPackageName.equals(fQPkgName)) {
				if (!tmpList.contains(relatedPackageName)) {
					String relatedURI = namespaceUriPrefix + relatedPackageName;
					log.debug("Import relatedURI: " + relatedURI);
					Element importElement = new Element("import", w3cNS);
					importElement.setAttribute("namespace",relatedURI);
					importElement.setAttribute("schemaLocation", relatedPackageName+".xsd");
					elements.add(importElement);
					tmpList.add(relatedPackageName);
				}
			}
		}
		return elements;
	}

	private Document generateRepository(Collection<UMLClass> classColl, String fQPkgName, Collection<UMLClass> pkgClassCollection) {

		String caBIGNS_URI = namespaceUriPrefix+ fQPkgName;
		Namespace w3cNS = Namespace.getNamespace("xs","http://www.w3.org/2001/XMLSchema");
		Element schemaElem = new Element("schema", w3cNS);
		Namespace caBIGNS = Namespace.getNamespace(encode(caBIGNS_URI));
		schemaElem.addNamespaceDeclaration(caBIGNS);
		schemaElem.addNamespaceDeclaration(w3cNS);

		List<Namespace> relatedNamespaces = getRelatedNamespaces(classColl, fQPkgName);
		for(Namespace rNamespace:relatedNamespaces){
			schemaElem.addNamespaceDeclaration(rNamespace);
		}        

		Attribute targetNSAttr = new Attribute("targetNamespace", caBIGNS_URI);
		schemaElem.setAttribute(targetNSAttr);
		HashSet relatedNamespacesImports = getRelatedNamespacesImports(classColl, fQPkgName, w3cNS);
		Attribute formDefault = new Attribute("elementFormDefault","qualified");
		schemaElem.setAttribute(formDefault);

		for (Iterator j = relatedNamespacesImports.iterator(); j.hasNext();) {
			Element namespaceImport = (Element) j.next();
			schemaElem.addContent(namespaceImport);
		}

		for (Iterator i = pkgClassCollection.iterator(); i.hasNext();) {
			UMLClass klass = (UMLClass) i.next();
			doMapping(klass, schemaElem, w3cNS);
		}

		Document doc = new Document();
		doc.setRootElement(schemaElem);
		return doc;
	}

	private void doMapping(UMLClass klass, Element mappingEl, Namespace w3cNS) {

		String superClassName = null;

		UMLClass superClass = null;
		try {
			superClass = TransformerUtils.getSuperClass(klass);
		} catch(GenerationException ge){
			log.error("Exception caught while getting Superclass for " + klass.getName(), ge);
			generatorErrors.addError(new GeneratorError(ge.getMessage(), ge));
		}

		if (superClass != null) {
			String klassPackageName = TransformerUtils.getFullPackageName(klass);
			String superClassPackageName = TransformerUtils.getFullPackageName(superClass);

			if (klassPackageName.equals(superClassPackageName)){
				superClassName = superClass.getName();
			} else {
				superClassName = superClassPackageName + ':'+superClass.getName();
			}
		} 	
		Element classEl = new Element("element", w3cNS);
		classEl.setAttribute("name",klass.getName());
		classEl.setAttribute("type", klass.getName());
		mappingEl.addContent(classEl);
		Element classE2 = new Element("complexType", w3cNS);

		classE2.setAttribute("name", klass.getName());
		mappingEl.addContent(classE2);
		
		addCaDSRAnnotation(klass, classE2, w3cNS);

		if (superClassName!=null) {
			log.debug("superClassName: " + superClassName);

			Element complexContent = new Element("complexContent", w3cNS);
			Element extension = new Element("extension",w3cNS);
			extension.setAttribute("base", superClassName);
			complexContent.addContent(extension);
			classE2.addContent(complexContent);
			Element sequence = new Element("sequence",w3cNS);
			extension.addContent(sequence);

			//Do properties
			for (Iterator i = klass.getAttributes().iterator(); i.hasNext();) {
				UMLAttribute att = (UMLAttribute) i.next();
				log.debug("att.getName(): " + att.getName()); 

				// Only process non-static attributes
				log.debug("isStatic: " + TransformerUtils.isStatic(att));
				if (!TransformerUtils.isStatic(att)){
					Element attributeElement = new Element("attribute", w3cNS);
					attributeElement.setAttribute("name", att.getName());
					String type = getName(TransformerUtils.getType(att));
					log.debug("Attribute type: " + type);
					attributeElement.setAttribute("type", type);
					extension.addContent(attributeElement);
				}
			}

			for (Iterator i = TransformerUtils.getAssociationEnds(klass).iterator(); i.hasNext();) {
				UMLAssociationEnd thisEnd = (UMLAssociationEnd) i.next();
				UMLAssociationEnd otherEnd = TransformerUtils.getOtherAssociationEnd(thisEnd);
				addSequenceAssociationElement(sequence, klass,thisEnd, otherEnd,w3cNS);
			}

		} else { // superClassname == null
			Element sequence = new Element("sequence",w3cNS);
			classE2.addContent(sequence);

			//Do properties
			for (Iterator i = klass.getAttributes().iterator(); i.hasNext();) {
				UMLAttribute att = (UMLAttribute) i.next();
				log.debug("att.getName(): " + att.getName());

				// Only process non-static attributes
				log.debug("isStatic: " + TransformerUtils.isStatic(att));
				if (!TransformerUtils.isStatic(att)){

					Element attributeElement = new Element("attribute", w3cNS);
					attributeElement.setAttribute("name", att.getName());

					String type = getName(TransformerUtils.getType(att));
					log.debug("Attribute type: " + type);
					attributeElement.setAttribute("type", type);
					
					addCaDSRAnnotation(att, attributeElement, w3cNS);
					
					classE2.addContent(attributeElement);
				}
			}

			for (Iterator i = TransformerUtils.getAssociationEnds(klass).iterator(); i.hasNext();) {
				UMLAssociationEnd thisEnd = (UMLAssociationEnd) i.next();
				UMLAssociationEnd otherEnd = TransformerUtils.getOtherAssociationEnd(thisEnd);
				addSequenceAssociationElement(sequence, klass,thisEnd, otherEnd,w3cNS);
			}
		}
	}
	
	private void addCaDSRAnnotation(UMLTaggableElement tgElt, Element elt, Namespace w3cNS) {
		String caDSRAnnotation = TransformerUtils.getCaDSRAnnotationContent(tgElt);
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
			log.debug("otherEnd type: " + ((UMLClass)(otherEnd.getUMLElement())).getName());

			Element associationElement = new Element("element", w3cNS);
			sequence.addContent(associationElement);

			associationElement.setAttribute("name", otherEnd.getRoleName());

			String maxOccurs = TransformerUtils.getUpperBound(otherEnd);
			log.debug("maxOccurs: " + maxOccurs);

			// A collection - model association as an element with the association name that 
			// has a sequence of the associated type.  See GForge #1311.
			associationElement.setAttribute("minOccurs","0");   
			associationElement.setAttribute("maxOccurs","1");

			Element complexType = new Element("complexType",w3cNS);
			Element innerSequence = new Element("sequence", w3cNS);
			Element associatedObjElement = new Element("element", w3cNS);

			associatedObjElement.setAttribute("ref", ((UMLClass)(otherEnd.getUMLElement())).getName());
			associatedObjElement.setAttribute("minOccurs","0");   
			associatedObjElement.setAttribute("maxOccurs",maxOccurs);  

			innerSequence.addContent(associatedObjElement);
			complexType.addContent(innerSequence);
			associationElement.addContent(complexType);           	

		}
	}

	private String getName(String type) {
		String finalType = "xs:";
		if (type.equalsIgnoreCase("collection")) {
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
	
	private String encode(String namespaceUriPrefix){
		
		return xmlEncoder.encode(namespaceUriPrefix);
		
	}
}
