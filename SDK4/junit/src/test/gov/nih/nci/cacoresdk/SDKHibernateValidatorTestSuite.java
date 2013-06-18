/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk-pre411/LICENSE.txt for details.
 */

package test.gov.nih.nci.cacoresdk;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import test.gov.nih.nci.cacoresdk.domain.other.validationtype.AllValidationTypeHibernateValidatorTest;

public  class SDKHibernateValidatorTestSuite extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite("Suite for Hibernate Validator Test Files");
		suite.addTest(new TestSuite(AllValidationTypeHibernateValidatorTest.class,AllValidationTypeHibernateValidatorTest.getTestCaseName()));
		return suite;
	}
}