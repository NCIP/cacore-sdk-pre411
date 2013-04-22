/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk.domain.other.primarykey;

import java.util.List;
import org.jdom.Document;
import org.jdom.Element;

import test.gov.nih.nci.cacoresdk.SDKHBMMappingTestBase;

public class NoIdKeyPkHBMMappingTest extends SDKHBMMappingTestBase {

	private Document doc = null;

	public static String getTestCaseName() {
		return "NoIdKeyPkValidator HBM Mapping Test Case";
	}

	protected void setUp() throws Exception {
		super.setUp();
		String hbmfileName="/gov/nih/nci/cacoresdk/domain/other/primarykey/NoIdKey.hbm.xml";
		doc=getDocument(hbmfileName);
	}

	@Override
	public Document getDoc() {
		return doc;
	}
	
	public void testHiloPkGeneratorClassParams() throws Exception {
	        
			List<Element> ids = queryXMLMapping(doc, "/hibernate-mapping/class/id");
			for (Element element : ids) {
				assertNotNull(element.getAttribute("column"));
				assertNotNull(element.getAttribute("name"));
				assertNotNull(element.getAttribute("type"));
	
				List<Element> idgenerators = element.getChildren();
				for (Element generatorElement : idgenerators) {
					assertEquals("hilo", generatorElement.getAttributeValue("class"));
					List<Element> params = generatorElement.getChildren();
					for (int i=0;i<params.size();i++) {
						Element param=params.get(i);
						if(i==0){
							assertEquals("table", param.getAttributeValue("name"));
							assertEquals("hi_value", param.getContent(0).getValue());
						}
						if(i==1){
							assertEquals("column", param.getAttributeValue("name"));
							assertEquals("hi_value2", param.getContent(0).getValue());
						}
						if(i==2){
							assertEquals("max_lo", param.getAttributeValue("name"));
							assertEquals("100", param.getContent(0).getValue());
						}
					}
				}
			}
		}
}
