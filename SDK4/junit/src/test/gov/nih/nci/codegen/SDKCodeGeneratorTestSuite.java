/*L
 *  Copyright Ekagra Software Technologies Ltd.
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cacore-sdk/LICENSE.txt for details.
 */

package test.gov.nih.nci.codegen;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.gov.nih.nci.codegen.validator.ValidatorTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { 
	ValidatorTestSuite.class
	}
)
public class SDKCodeGeneratorTestSuite {
}