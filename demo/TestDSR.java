import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.system.applicationservice.ApplicationService;

import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;

import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;


/**
 * @author Jane Jiang <a href="mailto: jane.jiang@oracle.com"></a>
 * @version 1.0
 */


/**
 * TestDSR.java demonstartes various ways to search caDSR objects
 */
public class TestDSR {


   public static void main(String[] args) {
      Project project = null;
      System.out.println("*** TestUml...");
      try {
         ApplicationService appService = ApplicationServiceProvider.getApplicationService();
         //     ApplicationService.getRemoteInstance("http://cbiodev104.nci.nih.gov:49080/cacore31/http/remoteService");
         //     pplicationService.getRemoteInstance("http://cbioqa101.nci.nih.gov:49080/cacore31/http/remoteService");
         //     ApplicationService.getRemoteInstance("http://cabio-stage.nci.nih.gov/cacore31/http/remoteService");


         System.out.println("Using basic search.  Retrieving all projects");
         DetachedCriteria projectCriteria =
            DetachedCriteria.forClass(Project.class);
         projectCriteria.addOrder(Order.asc("shortName"));

         try {
            System.out
            .println("Scenario 1: Using basic search.  Retrieving all projects, display version and context information...");

            List<Project> resultList =
               appService.query(projectCriteria, Project.class.getName());
            ;
            System.out.println(resultList.size() + " projects retrieved..");
            for (Iterator resultsIterator = resultList.iterator();
                 resultsIterator.hasNext(); ) {
               project = (Project)resultsIterator.next();
               System.out.println("Project name: " + project.getShortName());
               System.out.println("        version: " + project.getVersion());
               System.out
               .println("        context: " + project.getClassificationScheme()
                                  .getContext().getName());
            }
            /*
              for (Iterator classIterator = project.getUMLClassMetadataCollection().iterator();
                      classIterator.hasNext();) {
                      UMLClassMetadata umlClass = (UMLClassMetadata) classIterator.next();
                      System.out.println("class Name: " +umlClass.getName());
              }
*/
            System.out.println();
            System.out
            .println("Scenario 2: Retrieving class named Gene, display class information");
            UMLClassMetadata umlClass = new UMLClassMetadata();
            umlClass.setName("gene");
            resultList = appService.search(UMLClassMetadata.class, umlClass);
            System.out.println(resultList.size() + " classes retrieved..");
            for (Iterator resultsIterator = resultList.iterator();
                 resultsIterator.hasNext(); ) {
               umlClass = (UMLClassMetadata)resultsIterator.next();
               System.out
               .println("  class full name: " + umlClass.getFullyQualifiedName());
               System.out
               .println("  class description: " + umlClass.getDescription());
               System.out
               .println("  project version: " + umlClass.getProject().getVersion());
               System.out
               .println("  object class public id: " + umlClass.getObjectClass()
                                  .getPublicID());
            }


            System.out.println();
            System.out
            .println("Scenario 3: Retrieving attributes for a class, display attribute information");
            if (umlClass != null) {
               for (Iterator resultsIterator =
                       umlClass.getUMLAttributeMetadataCollection().iterator();
                    resultsIterator.hasNext(); ) {
                  UMLAttributeMetadata umlAttribute =
                     (UMLAttributeMetadata)resultsIterator.next();
                  printAttributeInfo(umlAttribute);

               }
            }
            
            System.out.println();
            System.out
            .println("Scenario 4: Retrieving attributes named *id, display attribute information");
            UMLAttributeMetadata umlAttr = new UMLAttributeMetadata();
            umlAttr.setName("*:id");
            resultList = appService.search(UMLAttributeMetadata.class, umlAttr);
            System.out.println(resultList.size() + " attributes retrieved..");
            

         } catch (Exception e) {
            e.printStackTrace();
         }
      } catch (RuntimeException e2) {
         e2.printStackTrace();
      }
   }

   private static void printAttributeInfo(UMLAttributeMetadata umlAttribute) {
      System.out.println("  Attribute name: " + umlAttribute.getName());
      System.out
      .println("  Attribute type: " + umlAttribute.getAttributeTypeMetadata()
                         .getValueDomainDataType());
      System.out
      .println("  Data Element public id: " + umlAttribute.getDataElement()
                         .getPublicID());

   }
}
