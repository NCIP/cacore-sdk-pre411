import gov.nih.nci.system.applicationservice.*;
import java.util.*;

import gov.nih.nci.camod.domain.*;
import gov.nih.nci.cadsr.domain.*;
import gov.nih.nci.cabio.domain.*;
import gov.nih.nci.common.util.*;
import org.hibernate.criterion.*;
import org.w3c.dom.Document;

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
 * TestClient.java demonstartes various ways to execute searches with and
 * without using Application Service Layer (convenience layer that abstracts
 * building criteria Uncomment different scenarios below to demonstrate the
 * various types of searches
 */

public class TestClient {

	public static void main(String[] args) {

		System.out.println("*** TestClient...");
		try {
            String prodUrl =  "http://cabio.nci.nih.gov/cacore31/http/remoteService";
            String localUrl = "http://localhost:8080/cacore31/http/remoteService";
            String stageUrl =  "http://cabio-stage.nci.nih.gov/cacore31/http/remoteService";
            String qaUrl = "http://cabio-qa.nci.nih.gov/cacore31/http/remoteService";

			//ApplicationService appService = ApplicationServiceProvider.getApplicationService();
			ApplicationService appService = ApplicationService.getRemoteInstance(prodUrl);
			
			
			try {

				/** ********** Examples used in Developer Guide ***************** */

				try {
					// Scenario 1: Using basic search
					// Retrieving Genes based on symbol and using Gene class as
					// the Target
					System.out
							.println("Scenario 1: Using basic search.  Retrieving Genes based on symbol and using Gene class as the Target.");
					Gene gene = new Gene();
					gene.setSymbol("brca*"); // searching for all gene’s
												// whose symbol start with
												// brac*”;

					try {
						List resultList = appService.search(Gene.class, gene);

						for (Iterator resultsIterator = resultList.iterator(); resultsIterator
								.hasNext();) {
							Gene returnedGene = (Gene) resultsIterator.next();
							System.out.println("Symbol: "
									+ returnedGene.getSymbol()
									+ "\tTaxon:"
									+ returnedGene.getTaxon()
											.getScientificName() + "\tName "
									+ returnedGene.getFullName());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (RuntimeException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				try {
					// Scenario 2: Building compound criteria
					// Retrieving a List of Pathway objects for a particular
					// Taxon and a Gene
					System.out
							.println("Scenario 2: Building compound criteria.  Retrieving a List of Pathway objects for a particular Taxon and a Gene.");
					Pathway pathway = new Pathway();

					Taxon taxon = new Taxon();
					taxon.setAbbreviation("hs");

					Gene gene = new Gene();
					gene.setTaxon(taxon);

					Gene geneEGFR = new Gene();
					geneEGFR.setSymbol("TP53");

					List geneList = new ArrayList();
					geneList.add(gene);

					pathway.setGeneCollection(geneList);
					pathway.setTaxon(taxon);

					try {
						List resultList = appService.search(
								"gov.nih.nci.cabio.domain.Pathway", pathway);
						System.out.println("\n Total # of  records = "
								+ resultList.size());

						for (Iterator resultsIterator = resultList.iterator(); resultsIterator
								.hasNext();) {
							Pathway returnedPathway = (Pathway) resultsIterator
									.next();
							System.out.println(returnedPathway
									.getDisplayValue());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (RuntimeException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}

				// NucleicAcidSequence sequence= new NucleicAcidSequence();
				// sequence.setAccessionNumber("NM_000546");
				// Protein protein = new Protein();
				// protein.setPrimaryAccession("P04637");
				try {
					// Scenario 3: Building a Nested search
					Gene gene = new Gene();
					gene.setSymbol("tp53");

					System.out
							.println("Scenario 3: Building a Nested  search.  Retrieving a List of ProteinSequence for a particular gene via Protein.");
					try {
						List resultList = appService
								.search(
										"gov.nih.nci.cabio.domain.ProteinSequence,gov.nih.nci.cabio.domain.Protein",
										gene);
						System.out.println("\n Total # of  records = "
								+ resultList.size());

						PrintUtils printUtil = new PrintUtils();
						printUtil.printResults(resultList); // View details
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (RuntimeException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// Scenario 4: Creating an Hibernate's Detached Criteria by
				// directly using Hibernate API
				System.out
						.println("Scenario 4: Creating an Hibernate's Detached Criteria by directly using Hibernate API.");
				DetachedCriteria criteria = DetachedCriteria
						.forClass(Pathway.class);
				criteria.add(Restrictions.like("name", "%_prc2%"));

				try {
					List resultList = appService.query(criteria, Pathway.class
							.getName());
					System.out.println("\n Total # of  records = "
							+ resultList.size());

					for (Iterator resultsIterator = resultList.iterator(); resultsIterator
							.hasNext();) {
						Pathway returnedPathway = (Pathway) resultsIterator
								.next();
						System.out.println(returnedPathway.getDisplayValue());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				/**
				 * ********** Common variables for the test cased below
				 * ***************
				 */
				// _gene0 is an empty Gene
				Gene _gene0 = new Gene();

				Taxon _taxon = new Taxon();
				_taxon.setId(new Long(5));

				// _gene1 contain a taxon.id=5 object
				Gene _gene1 = new Gene();
				_gene1.setTaxon(_taxon);

				Gene _gene2 = new Gene();
				_gene2.setId(new Long(2));

				Gene _gene3 = new Gene();
				_gene3.setSymbol("D*");

				Library _lib1 = new Library();
				_lib1.setId(new Long(22));

				Library _lib2 = new Library();
				_lib2.setId(new Long(33));

				List _libList = new ArrayList();
				_libList.add(_lib1);
				_libList.add(_lib2);

				Target _target1 = new Target();
				_target1.setId(new Long(61));

				Target _target2 = new Target();
				_target2.setId(new Long(42));

				List _targetList = new ArrayList();
				_targetList.add(_target1);
				_targetList.add(_target2);

				Chromosome _chrom = new Chromosome();
				_chrom.setNumber("YX");

				Gene _gene4 = new Gene();
				_gene4.setId(new Long(20));
				_gene4.setChromosome(_chrom);

				Gene _gene5 = new Gene();
				_gene5.setTargetCollection(_targetList);

				Gene _gene6 = new Gene();
				_gene6.setId(new Long(901));

				// _protein contains a list of genes and also with the name
				// attribute set wild card
				Protein _protein = new Protein();
				List _geneList = new ArrayList();
				_geneList.add(_gene2);
				_geneList.add(_gene3);
				_protein.setGeneCollection(_geneList);
				_protein.setName("*b-27*");

				List _geneList2 = new ArrayList();
				_geneList2.add(_gene2);
				_geneList2.add(_gene5);

				Target _target = new Target();
				_target.setGeneCollection(_geneList);

				Target _target3 = new Target();
				_target3.setName("CD*");

				NucleicAcidSequence _seq1 = new NucleicAcidSequence();
				_seq1.setId(new Long(1507));

				Clone _clone1 = new Clone();
				_clone1.setId(new Long(1420));
				// _clone1.setName("IMAGE:5228554");

				NucleicAcidSequence _seq2 = new NucleicAcidSequence();
				_seq2.setAccessionNumber("AA247215");

				// Test Case 1: search for the same kind of object. _gene3 has
				// certain criteria set
				System.out
						.println("\n\n\nTest Case 1: search for the same kind of object search(\"Gene\", _gene3), ");
				List resultList1 = appService.search(
						"gov.nih.nci.cabio.domain.Gene", _gene3);
				if (resultList1.size() < 1) {
					System.out.println("(Test Case 1 ) No records found");
				} else {
					System.out.println("(Test Case 1 )Total # of  records = "
							+ resultList1.size());
					Iterator iterator = resultList1.iterator();
					while (iterator.hasNext()) {
						Gene gene = (Gene) iterator.next();
						System.out.println(" result id = " + gene.getId()
								+ " | result name = " + gene.getSymbol());
					}
				}

				// Test Case 2: have the same name in the search path
				System.out
						.println("\n\n\nTest Case 2: search(\"Taxon, Taxon\", gene), gene.id = 10, should have one result back.");
				List resultList2 = appService
						.search(
								"gov.nih.nci.cabio.domain.Taxon,gov.nih.nci.cabio.domain.Taxon",
								_gene2);
				if (resultList2.size() < 1) {
					System.out.println("(Test Case 2 ) No records found");
				} else {
					System.out.println("(Test Case 2 )Total # of  records = "
							+ resultList2.size());

					Iterator iterator = resultList2.iterator();
					while (iterator.hasNext()) {
						Taxon taxon = (Taxon) iterator.next();
						System.out
								.println(" result id = " + taxon.getId()
										+ " | result name = "
										+ taxon.getAbbreviation());
					}
				}

				// Test Case 3: Query objects with an empty object(no attribute,
				// no association set)
				System.out
						.println("\n\n\nTest Case 3: search(\"Taxon\", gene), , should get all genes back.");
				List resultList3 = appService.search(
						"gov.nih.nci.cabio.domain.Chromosome", _gene0);
				if (resultList3.size() < 1) {
					System.out.println("(Test Case 3) No records found");
				} else {
					System.out.println("(Test Case 3 )Total # of  records = "
							+ resultList3.size());
					Iterator iterator = resultList3.iterator();
					while (iterator.hasNext()) {
						Chromosome chrom = (Chromosome) iterator.next();
						System.out.println(" result id = " + chrom.getId()
								+ " | result number = " + chrom.getNumber());
					}
				}

				// Test Case 4: Query Pathway object to test CLOB
				System.out
						.println("\n\n\nTest Case 4: search(\"Pathway\", gene).  Testing for CLOB datatype");
				List resultList4 = appService.search(
						"gov.nih.nci.cabio.domain.Pathway", _gene6);
				if (resultList4.size() < 1) {
					System.out.println("(Test Case 4) No records found");
				} else {
					System.out.println("(Test Case 4 )Total # of  records = "
							+ resultList4.size());
					Iterator iterator = resultList4.iterator();
					while (iterator.hasNext()) {
						Pathway pathway = (Pathway) iterator.next();
						System.out.println(" result id = " + pathway.getId()
								+ " | result name = " + pathway.getName());
					}
				}

				// Test Case 5: Testing type 1 nested query.
				System.out
						.println("\n\n\nTest Case 5: search(\"ProteinAlias,Protein,Gene\", chromosome).  Testing type 1 nested Query");
				List resultList5 = appService.search(
						"gov.nih.nci.cabio.domain.ProteinAlias,"
								+ "gov.nih.nci.cabio.domain.Protein,"
								+ "gov.nih.nci.cabio.domain.Gene", _chrom);
				if (resultList5.size() < 1) {
					System.out.println("(Test Case 5) No records found");
				} else {
					System.out.println("(Test Case 5 )Total # of  records = "
							+ resultList5.size());
					Iterator iterator = resultList5.iterator();
					while (iterator.hasNext()) {
						ProteinAlias proteinA = (ProteinAlias) iterator.next();
						System.out.println(" result id = " + proteinA.getId()
								+ " | result name = " + proteinA.getName());
					}
				}

				// Test Case 6: Nested Query combine type 1 and 2. Chromosome
				// object contains a list of Gene
				System.out
						.println("\n\n\nTest Case 6: search(\"Chromosome\", GeneList).  list query ... ...");
				List resultList6 = appService.search(
						"gov.nih.nci.cabio.domain.Chromosome", _geneList);
				if (resultList6.size() < 1) {
					System.out.println("(Test Case 6) No records found");
				} else {
					System.out.println("(Test Case 6 )Total # of  records = "
							+ resultList6.size());
					Iterator iterator = resultList6.iterator();
					while (iterator.hasNext()) {
						Chromosome chrom = (Chromosome) iterator.next();
						System.out.println(" result id = " + chrom.getId()
								+ " | result number = " + chrom.getNumber());
					}
				}

				// Test Case 7: Nested Query type 2. TARGET object contains a
				// list of Gene
				System.out
						.println("\n\n\nTest Case 7: search(\"Agent\", Target).  Testing type 2 nested Query");
				List resultList7 = appService.search(
						"gov.nih.nci.cabio.domain.Agent", _target);
				if (resultList7.size() < 1) {
					System.out.println("(Test Case 7) No records found");
				} else {
					System.out.println("(Test Case 7 )Total # of  records = "
							+ resultList7.size());
					Iterator iterator = resultList7.iterator();
					while (iterator.hasNext()) {
						Agent agent = (Agent) iterator.next();
						System.out.println(" result id = " + agent.getId()
								+ " | result name = " + agent.getName());
					}
				}

				// Test Case 9: Combine type 2 and list query, one of gene
				// object in the list contains another collection
				System.out
						.println("\n\n\nTest Case 9: search(\"Protein, Gene\", Sequence) (// bi-direction (sequence m -> m gene m -> m protein).  Testing type 2 nested Query");
				List resultList9 = appService
						.search(
								"gov.nih.nci.cabio.domain.Protein,gov.nih.nci.cabio.domain.Gene",
								_seq1);
				if (resultList9.size() < 1) {
					System.out.println("(Test Case 9) No records found");
				} else {
					System.out.println("(Test Case 9 )Total # of  records = "
							+ resultList9.size());
					Iterator iterator = resultList9.iterator();
					while (iterator.hasNext()) {
						Protein protein = (Protein) iterator.next();
						System.out.println(" result id = " + protein.getId()
								+ " | result name = " + protein.getName());
					}
				}

				// Test Case 10: Combine type 2 and list query, one of gene
				// object in the list contains another collection
				System.out
						.println("\n\n\nTest Case 10: search(\"Sequence\", Clone) (// uni-direction (clone 1 -> m sequence). ");
				List resultList10 = appService
						.search("gov.nih.nci.cabio.domain.NucleicAcidSequence",
								_clone1);
				if (resultList10.size() < 1) {
					System.out.println("(Test Case 10) No records found");
				} else {
					System.out.println("(Test Case 10 )Total # of  records = "
							+ resultList10.size());
					Iterator iterator = resultList10.iterator();
					while (iterator.hasNext()) {
						NucleicAcidSequence seq = (NucleicAcidSequence) iterator
								.next();
						System.out.println(" result id = " + seq.getId()
								+ " | result accessionNumber = "
								+ seq.getAccessionNumber());
					}
				}

				// Test Case 11: Combine type 2 and list query, one of gene
				// object in the list contains another collection
				System.out
						.println("\n\n\nTest Case 11: search(\"Sequence\", Sequence), Get sequence from sequnce accession number");
				List resultList11 = appService.search(
						"gov.nih.nci.cabio.domain.Clone", _clone1);
				if (resultList11.size() < 1) {
					System.out.println("(Test Case 11) No records found");
				} else {
					System.out.println("(Test Case 11 )Total # of  records = "
							+ resultList11.size());
					Iterator iterator = resultList11.iterator();
					while (iterator.hasNext()) {
						Clone seq = (Clone) iterator.next();
						System.out.println(" result id = " + seq.getId()
								+ " | result name = " + seq.getName());
					}
				}

				// Test Case 12: uni-direction (nested) (clone 1 -> m sequence m
				// -> m gene)
				System.out
						.println("\n\n\nTest Case 12: search(\"Gene, Sequence\", Clone), // uni-direction (nested) (clone 1 -> m sequence m -> m gene)");
				List resultList12 = appService
						.search(
								"gov.nih.nci.cabio.domain.Gene, gov.nih.nci.cabio.domain.NucleicAcidSequence",
								_clone1);
				if (resultList12.size() < 1) {
					System.out.println("(Test Case 12) No records found");
				} else {
					System.out.println("(Test Case 12 )Total # of  records = "
							+ resultList12.size());
					Iterator iterator = resultList12.iterator();
					while (iterator.hasNext()) {
						Gene gene = (Gene) iterator.next();
						System.out.println(" result id = " + gene.getId()
								+ " | result symbol = " + gene.getSymbol());
					}
				}

				// Test Case 14: many-to-one relationship Bidirectional (Use
				// Gene to get Taxon)
				System.out
						.println("\n\n\nTest Case 14: search(\"Taxon\", Gene),many-to-one relationship Bidirectional... ...");
				List resultList14 = appService.search(
						"gov.nih.nci.cabio.domain.Taxon", _gene2);
				if (resultList14.size() < 1) {
					System.out
							.println("\n(Test Case 14: many-to-one Bidirectional) No records found");
				} else {
					System.out
							.println("\n(Test Case 14: many-to-one Bidirectional) Total # of  records = "
									+ resultList14.size());
					Iterator iterator = resultList14.iterator();
					while (iterator.hasNext()) {
						Taxon taxon = (Taxon) iterator.next();
						System.out.println(" result id = " + taxon.getId()
								+ " | result name = " + taxon.getCommonName());
					}
				}

				// Test Case 15: one-to-many bidirectional relationship (Use
				// Target to get Gene)
				System.out
						.println("\n\n\nTest Case 15: search(\"Gene\", Target),one-to-many bidirectional relationship... ...");
				List resultList15 = appService.search(
						"gov.nih.nci.cabio.domain.Gene", _target3);
				if (resultList15.size() < 1) {
					System.out
							.println("\n(Test case 15: one-to-many Bidirectional) No records found");
				} else {
					System.out
							.println("\n(Test case 15: one-to-many Bidirectional) Total # of  records = "
									+ resultList15.size());
					Iterator iterator = resultList15.iterator();
					while (iterator.hasNext()) {
						Gene gene = (Gene) iterator.next();
						System.out.println(" result id = " + gene.getId()
								+ " | result Symbol = " + gene.getSymbol());
					}
				}

				// One-to-One relationship
				// Test Case 16: one-to-one bidirectional relationship (Use
				// CloneRelativeLocation to get NucleicAcidSequence)
				System.out
						.println("\n\n\nTest Case 16: search(\"NucleicAcidSequence\", CloneRelativeLocation),one-to-one bidirectional relationship... ...");
				CloneRelativeLocation _crlocation = new CloneRelativeLocation();
				_crlocation.setId(new Long(5));
				List resultList16 = appService.search(
						"gov.nih.nci.cabio.domain.NucleicAcidSequence",
						_crlocation);
				if (resultList16.size() < 1) {
					System.out
							.println("\n(Test case 16: one-to-one Bidirectional) No records found");
				} else {
					System.out
							.println("\n(Test case 16: One-to-One Bidirectional) Total # of  records = "
									+ resultList16.size());
					Iterator iterator = resultList16.iterator();
					while (iterator.hasNext()) {
						NucleicAcidSequence seq = (NucleicAcidSequence) iterator
								.next();
						System.out.println(" result id = " + seq.getId()
								+ " | result accession number = "
								+ seq.getAccessionNumber());
					}
				}

				// Test Case 17: One-to-Many Unidirectional relationship (Use
				// CellLine to get Publication)
				System.out
						.println("\n\n\nTest Case 17: search(\"Publication\", CellLine),One-to-Many Unidirectional relationship... ...");
				CellLine _cellLine = new CellLine();
				_cellLine.setId(new Long(2220));
				List resultList17 = appService.search(
						"gov.nih.nci.camod.domain.Publication", _cellLine);
				if (resultList17.size() < 1) {
					System.out
							.println("\n(Test case 17: one-to-many Unidirectional) No records found");
				} else {
					System.out
							.println("\n(Test case 17: One-to-Many Unidirectional) Total # of  records = "
									+ resultList17.size());
					Iterator iterator = resultList17.iterator();
					while (iterator.hasNext()) {
						Publication pub = (Publication) iterator.next();
						System.out.println(" result id = " + pub.getId()
								+ " | result authors = " + pub.getAuthors());
					}
				}

				// Test Case 18: Many-to-One Unidirectional relationship (Use
				// Publication to get PublicationStatus)
				System.out
						.println("\n\n\nTest Case 18: search(\"PublicationStatus\", Publication),Many-to-One Unidirectional relationship... ...");
				Publication _publication1 = new Publication();
				_publication1.setId(new Long(1371));
				List resultList18 = appService.search(
						"gov.nih.nci.camod.domain.PublicationStatus",
						_publication1);
				if (resultList18.size() < 1) {
					System.out
							.println("\n(Test Case 18: many-to-one Unidirectional) No records found");
				} else {
					System.out
							.println("\n(Test Case 18: Many-to-One Unidirectional) Total # of  records = "
									+ resultList18.size());
					Iterator iterator = resultList18.iterator();
					while (iterator.hasNext()) {
						PublicationStatus pub = (PublicationStatus) iterator
								.next();
						System.out.println(" result id = " + pub.getId()
								+ " | result name = " + pub.getName());
					}
				}

				// Test Case 19: Many-to-Many Unidirectional relationship (Use
				// Pathway to get Histopathology)
				System.out
						.println("\n\n\nTest Case 19: search(\"Histopathology\", Pathway),Many-to-Many Unidirectional relationship... ...");
				Pathway _pathway1 = new Pathway();
				_pathway1.setName("h_C*");
				List resultList19 = appService.search(
						"gov.nih.nci.cabio.domain.Histopathology", _pathway1);
				if (resultList19.size() < 1) {
					System.out
							.println("\n(Test Case 19: many-to-many Unidirectional) No records found");
				} else {
					System.out
							.println("\n(Test Case 19: Many-to-many Unidirectional) Total # of  records = "
									+ resultList19.size());
					Iterator iterator = resultList19.iterator();
					while (iterator.hasNext()) {
						Histopathology his = (Histopathology) iterator.next();
						System.out.println(" result id = " + his.getId()
								+ " | result description = "
								+ his.getGrossDescription());
					}
				}

				// Test Case 20: Two level test
				// Use Chromosome number "Y" get 290 Genes back, it will work
				// Use Chromosome number "1" get 5749 Genes back, then it will
				// NOT work to get SNP
				System.out
						.println("\n\n\nTest Case 20: search(\"GeneOntology, Gene\", Chromosome), two level nested query... ...");
				Chromosome _chrom2 = new Chromosome();
				_chrom2.setNumber("Y");
				List resultList20 = appService
						.search(
								"gov.nih.nci.cabio.domain.GeneOntology,gov.nih.nci.cabio.domain.Gene",
								_chrom2); // return total 87
				if (resultList20.size() < 1) {
					System.out
							.println("\n(Test Case 20: two levels Chromosome->Gene->GeneOntology) No records found");
				} else {
					System.out
							.println("\n(Test Case 20: two levels Chromosome->Gene->GeneOntology) Total # of  records = "
									+ resultList20.size());
					Iterator iterator = resultList20.iterator();
					while (iterator.hasNext()) {
						GeneOntology go = (GeneOntology) iterator.next();
						System.out.println(" result id = " + go.getId()
								+ " | result name = " + go.getName());
					}
				}


			} catch (RuntimeException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Test client throws Exception = " + ex);
		}

	}
}
