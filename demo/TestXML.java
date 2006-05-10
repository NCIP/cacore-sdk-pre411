
import gov.nih.nci.system.applicationservice.*;
import java.util.*;

import gov.nih.nci.camod.domain.*;
import gov.nih.nci.camod.domain.impl.*;

import gov.nih.nci.cadsr.domain.*;
import gov.nih.nci.cadsr.domain.impl.*;

import gov.nih.nci.cabio.domain.*;
import gov.nih.nci.cabio.domain.impl.*;
import gov.nih.nci.common.util.*;

import org.hibernate.criterion.*;
import javax.xml.parsers.*;

import javax.xml.validation.*;
import javax.xml.transform.*;
import java.io.*;

import javax.xml.transform.dom.*;
import org.xml.sax.*;

import javax.xml.validation.*;
import javax.xml.*;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.*;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
//import org.jdom.*;
//import org.jdom.output.XMLOutputter;
import java.io.*;

/**
 * <!-- LICENSE_TEXT_START -->
* Copyright 2001-2004 SAIC. Copyright 2001-2003 SAIC. This software was developed in conjunction with the National Cancer Institute,
* and so to the extent government employees are co-authors, any rights in such works shall be subject to Title 17 of the United States Code, section 105.
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
* 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the disclaimer of Article 3, below. Redistributions
* in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other
* materials provided with the distribution.
* 2. The end-user documentation included with the redistribution, if any, must include the following acknowledgment:
* "This product includes software developed by the SAIC and the National Cancer Institute."
* If no such end-user documentation is to be included, this acknowledgment shall appear in the software itself,
* wherever such third-party acknowledgments normally appear.
* 3. The names "The National Cancer Institute", "NCI" and "SAIC" must not be used to endorse or promote products derived from this software.
* 4. This license does not authorize the incorporation of this software into any third party proprietary programs. This license does not authorize
* the recipient to use any trademarks owned by either NCI or SAIC-Frederick.
* 5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
* SAIC, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author caBIO Team
 * @version 1.0
 */



/**
 * TestXML.java demonstartes various ways to serialize / deserialize object using the XMLUtility class
*/

public class TestXML {

	public static void main(String[] args) {

		System.out.println("*** TestClient...");
		try {
			ApplicationService appService = ApplicationServiceProvider.getApplicationService();

			try {
				System.out.println("Scenario 1: Retrieving a Gene based on a Gene id.");
				Gene gene = new Gene();
				gene.setId(Long.valueOf(2));

				try {
					XMLUtility myUtil = new XMLUtility();
					List resultList = appService.search(Gene.class, gene);
					System.out.println("Result list size: " + resultList.size()	+ "\n");
					long startTime = System.currentTimeMillis();
					for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
						Gene returnedGene = (Gene) resultsIterator.next();
						System.out.println("Gene object right after search: \n\n");
						System.out.println("   Id: " + returnedGene.getId()	+ "\n");
						System.out.println("   Fullname: " + returnedGene.getFullName() + "\n");
						System.out.println("   ClusterId: "	+ returnedGene.getClusterId() + "\n");
						System.out.println("   Symbol: " + returnedGene.getSymbol() + "\n\n\n");
						
						File myFile = new File("C:/test.xml");
						FileWriter myWriter = new FileWriter(myFile);
						myUtil.toXML(returnedGene, myWriter);
						
						DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
						Document document = parser.parse(myFile);
						SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
						//Source schemaFile = new StreamSource(new File("C:/cacore/lib/gov.nih.nci.cabio.domain.xsd"));
						//Schema schema = factory.newSchema(schemaFile);
						//Validator validator = schema.newValidator();
						//System.out.println("Validating gene against the schema......\n\n");
						//validator.validate(new DOMSource(document));
						//System.out.println("Gene has been validated!!!\n\n");
						
						Gene myGene = (Gene) myUtil.fromXML(myFile);
						System.out.println("Retrieving gene from xml ....\n\n");
						System.out.println("   Id: " + myGene.getId() + "\n");
						System.out.println("   Fullname: " + myGene.getFullName() + "\n");
						System.out.println("   ClusterId: " + myGene.getClusterId() + "\n");
						System.out.println("   Symbol: " + myGene.getSymbol() + "\n");
					}
					long endTime = System.currentTimeMillis();
					System.out.println("latency in miliseconds = " + (endTime - startTime));

				} catch (ParserConfigurationException ea) {
					ea.printStackTrace();
				} catch (SAXException eb) {
					eb.printStackTrace();
				} catch (IOException ec) {
					ec.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (RuntimeException e2) {
				e2.printStackTrace();
			}
			try {
				System.out.println("Scenario 2: Retrieving a Form based on an id.");
				Form crf = new Form();
				crf.setId("B27B3670-3E5E-21DA-E034-0003BA12F5E7");
				crf.setDisplayName("Toxicity");
				try {
					XMLUtility myUtil1 = new XMLUtility();
					List resultList1 = appService.search(Form.class, crf);
					System.out.println("Result list size: "	+ resultList1.size() + "\n");
					long startTime = System.currentTimeMillis();
					for (Iterator resultsIterator1 = resultList1.iterator(); resultsIterator1.hasNext();) {
						Form returnedCRF = (Form) resultsIterator1.next();
						System.out.println("Form object right after search: \n\n");
						System.out.println("   Id: " + returnedCRF.getId() + "\n");
						
						File myFile1 = new File("C:/test1.xml");
						FileWriter myWriter1 = new FileWriter(myFile1);
						myUtil1.toXML(returnedCRF, myWriter1);
						
						DocumentBuilder parser1 = DocumentBuilderFactory
								.newInstance().newDocumentBuilder();
						Document document1 = parser1.parse(myFile1);
						SchemaFactory factory1 = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
						//Source schemaFile1 = new StreamSource(new File("C:/cacore/lib/gov.nih.nci.cadsr.domain.xsd"));
						//Schema schema1 = factory1.newSchema(schemaFile1);
						//Validator validator1 = schema1.newValidator();
						//System.out.println("Validating Form against the schema......\n\n");
						//validator1.validate(new DOMSource(document1));
						//System.out.println("Form has been validated!!!\n\n");
						
						Form myCRF = (Form) myUtil1.fromXML(myFile1);
						System.out.println("Retrieving Form from xml ....\n\n");
						System.out.println("Form object right after search: \n\n");
						System.out.println("   Id: " + myCRF.getId() + "\n");
					}
					long endTime = System.currentTimeMillis();
					System.out.println("latency in miliseconds = " + (endTime - startTime));

				} catch (ParserConfigurationException ea) {
					ea.printStackTrace();
				} catch (SAXException eb) {
					eb.printStackTrace();
				} catch (IOException ec) {
					ec.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (RuntimeException e2) {
				e2.printStackTrace();
			}

			try {
				System.out.println("Scenario 3: Retrieving a PhysicalLocation based on an id.");
				PhysicalLocation crf = new PhysicalLocation();
				crf.setId(Long.valueOf(4268));
				try {
					XMLUtility myUtil1 = new XMLUtility();
					List resultList1 = appService.search(PhysicalLocation.class, crf);
					System.out.println("Result list size: "	+ resultList1.size() + "\n");
					long startTime = System.currentTimeMillis();
					for (Iterator resultsIterator1 = resultList1.iterator(); resultsIterator1.hasNext();) {
						PhysicalLocation returnedCRF = (PhysicalLocation) resultsIterator1.next();
						System.out.println("PhysicalLocation object right after search: \n\n");
						System.out.println("   Id: " + returnedCRF.getId() + "\n");
						System.out.println("   PreferredName: " + returnedCRF.getChromosomalStartPosition() + "\n");
						System.out.println("   PreferredDefinition: " + returnedCRF.getChromosomalEndPosition() + "\n");

						File myFile1 = new File("C:/test1.xml");
						FileWriter myWriter1 = new FileWriter(myFile1);
						myUtil1.toXML(returnedCRF, myWriter1);
						DocumentBuilder parser1 = DocumentBuilderFactory.newInstance().newDocumentBuilder();
						Document document1 = parser1.parse(myFile1);
						SchemaFactory factory1 = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
						//Source schemaFile1 = new StreamSource(new File("C:/cacore/lib/gov.nih.nci.cadsr.domain.xsd"));
						//Schema schema1 = factory1.newSchema(schemaFile1);
						//Validator validator1 = schema1.newValidator();
						//System.out.println("Validating CaseReportForm ......\n\n");
						//validator1.validate(new DOMSource(document1));
						//System.out.println("CaseReportForm has been validated!!!\n\n");
						
						PhysicalLocation myCRF = (PhysicalLocation) myUtil1.fromXML(myFile1);
						System.out.println("Retrieving PhysicalLocation from xml ....\n\n");
						System.out.println("   Id: " + myCRF.getId() + "\n");
						System.out.println("   PreferredName: "+ myCRF.getChromosomalStartPosition() + "\n");
						System.out.println("   PreferredDefinition: "+ myCRF.getChromosomalEndPosition() + "\n");
					}
					long endTime = System.currentTimeMillis();
					System.out.println("latency in miliseconds = "+ (endTime - startTime));

				} catch (ParserConfigurationException ea) {
					ea.printStackTrace();
				} catch (SAXException eb) {
					eb.printStackTrace();
				} catch (IOException ec) {
					ec.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (RuntimeException e2) {
				e2.printStackTrace();
			}

			try {
				System.out
						.println("Scenario 4: Retrieving a DataElement based on an id.");
				DataElement crf = new DataElement();
				crf.setId("DB97A435-C81B-2B4B-E034-0003BA12F5E7");

				try {
					XMLUtility myUtil1 = new XMLUtility();
					List resultList1 = appService
							.search(DataElement.class, crf);
					System.out.println("Result list size: "+ resultList1.size() + "\n");
					long startTime = System.currentTimeMillis();
					for (Iterator resultsIterator1 = resultList1.iterator(); resultsIterator1.hasNext();) {
						DataElement returnedCRF = (DataElement) resultsIterator1.next();
						System.out.println("DataElement object right after search: \n\n");
						System.out.println("   Id: " + returnedCRF.getId()+ "\n");
						
						File myFile1 = new File("C:/test1.xml");
						FileWriter myWriter1 = new FileWriter(myFile1);
						myUtil1.toXML(returnedCRF, myWriter1);
						
						DocumentBuilder parser1 = DocumentBuilderFactory.newInstance().newDocumentBuilder();
						Document document1 = parser1.parse(myFile1);
						SchemaFactory factory1 = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
						//Source schemaFile1 = new StreamSource(new File("C:/cacore/lib/gov.nih.nci.cadsr.domain.xsd"));
						//Schema schema1 = factory1.newSchema(schemaFile1);
						//Validator validator1 = schema1.newValidator();
						//System.out.println("Validating DataElement against the schema ......\n\n");
						//validator1.validate(new DOMSource(document1));
						
						System.out.println("DataElement has been validated!!!\n\n");
						DataElement myCRF = (DataElement) myUtil1.fromXML(myFile1);
						System.out.println("Retrieving DataElement from xml ....\n\n");
						System.out.println("   Id: " + myCRF.getId() + "\n");
					}
					long endTime = System.currentTimeMillis();
					System.out.println("latency in miliseconds = "+ (endTime - startTime));

				} catch (ParserConfigurationException ea) {
					ea.printStackTrace();
				} catch (SAXException eb) {
					eb.printStackTrace();
				} catch (IOException ec) {
					ec.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (RuntimeException e2) {
				e2.printStackTrace();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}