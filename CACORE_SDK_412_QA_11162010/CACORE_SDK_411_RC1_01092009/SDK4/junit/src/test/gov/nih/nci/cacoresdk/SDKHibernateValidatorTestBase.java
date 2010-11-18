package test.gov.nih.nci.cacoresdk;

import java.util.Date;

import junit.framework.TestCase;

import org.hibernate.validator.ClassValidator;

/**
 * @author Daniel Dumitru
 */
public class SDKHibernateValidatorTestBase extends TestCase {

	protected void isRight(ClassValidator<?> instanceValidator, String propertyName, String value) {
		assertEquals( "Right " + propertyName, 0, instanceValidator.getPotentialInvalidValues( propertyName, value ).length );
	}

	protected void isWrong(ClassValidator<?> instanceValidator, String propertyName, String value) {
		assertEquals( "Wrong " + propertyName, 1, instanceValidator.getPotentialInvalidValues( propertyName, value ).length );
	}
	
	protected void isRightDate(ClassValidator<?> instanceValidator, String propertyName, Date date) {
		
		assertEquals( "Right " + propertyName, 0, instanceValidator.getPotentialInvalidValues( propertyName, date ).length);
	}

	protected void isWrongDate(ClassValidator<?> instanceValidator, String propertyName, Date date) {
		
		assertEquals( "Wrong " + propertyName, 1, instanceValidator.getPotentialInvalidValues( propertyName, date ).length );
	}
	
	
	public static String getTestCaseName()
	{
		return "SDK Hibernate Validator Base Test Case";
	}

}
