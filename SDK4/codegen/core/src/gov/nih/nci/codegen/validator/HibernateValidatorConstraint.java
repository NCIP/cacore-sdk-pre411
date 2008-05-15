package gov.nih.nci.codegen.validator;

import java.util.Iterator;
import java.util.Map;

public class HibernateValidatorConstraint {

	private String validatorClassName; 
	private Map<String,String> constraintProperties;

	public HibernateValidatorConstraint(String validatorClass, Map<String,String> constraintProperties) {
		this.validatorClassName=validatorClass;
		this.constraintProperties = constraintProperties;
	}
	
	public String getValidatorClassName() {
		return validatorClassName;
	}

	public String getAnnotationString() {
		StringBuilder retValue = new StringBuilder();
		retValue.append(getAnnotationClassName());
		
		if (constraintProperties != null && !constraintProperties.isEmpty()){
			retValue.append("(");
			
			Iterator iter = constraintProperties.keySet().iterator();
			String key = (String)iter.next();
			retValue.append(key).append("=").append(constraintProperties.get(key));
			while(iter.hasNext()) {
				key = (String)iter.next();
				retValue.append(",").append(key).append("=").append(constraintProperties.get(key));
			}
			retValue.append(")");
		}

		return retValue.toString();
	}
	
	private String getAnnotationClassName(){
		if (validatorClassName == null) return "";
		
		return "@" + validatorClassName.substring(validatorClassName.lastIndexOf(".")+1);
	}
	
}
