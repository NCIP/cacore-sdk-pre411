package gov.nih.nci.codegen.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValidatorAttribute {

	private String name; 
	private List<ValidatorConstraint> constraints;
	private static final String NL = "\n";
	private static final String TAB = "\t";

	public ValidatorAttribute(String name, List<ValidatorConstraint> constraints) {
		this.name=name;
		this.constraints = constraints;
	}
	
	public String getName() {
		return name;
	}

	public String toString() {
		
		StringBuilder retValue = new StringBuilder();
		retValue.append("HVAttribute ( ").append("name=").append(this.name).append(NL);
		for(ValidatorConstraint constraint: constraints){
			retValue.append(constraint.getAnnotationString()).append(NL);
		}

		retValue.append(")").append(NL);

		return retValue.toString();
	}
	
	public Collection<String> getConstraintCollection(){
		Collection<String> constraintCollection = new ArrayList<String>();
		for(ValidatorConstraint constraint: constraints){
			constraintCollection.addAll(constraint.getConstraintValues());
		}
		
		return constraintCollection;
	}
	
	public String getConstraintAnnotationString(){
		StringBuilder retValue = new StringBuilder();
		for(ValidatorConstraint constraint: constraints){
			retValue.append(NL).append(TAB).append(constraint.getAnnotationString());
		}
		
		return retValue.toString();
	}
	
	public Set<String> getConstraintImports(){
		HashSet<String> set = new HashSet<String>();
		for(ValidatorConstraint constraint: constraints){
			set.add(constraint.getValidatorClassName());
		}
		
		return set;
	}
	
}
