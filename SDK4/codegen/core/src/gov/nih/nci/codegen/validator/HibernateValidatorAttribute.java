package gov.nih.nci.codegen.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HibernateValidatorAttribute {

	private String name; 
	private List<HibernateValidatorConstraint> constraints;
	private static final String NL = "\n";
	private static final String TAB = "\t";

	public HibernateValidatorAttribute(String name, List<HibernateValidatorConstraint> constraints) {
		this.name=name;
		this.constraints = constraints;
	}
	
	public String getName() {
		return name;
	}

	public String toString() {
		
		StringBuilder retValue = new StringBuilder();
		retValue.append("HVAttribute ( ").append("name=").append(this.name).append(NL);
		for(HibernateValidatorConstraint constraint: constraints){
			retValue.append(constraint.getAnnotationString()).append(NL);
		}

		retValue.append(")").append(NL);

		return retValue.toString();
	}
	
	public String getConstraintAnnotationString(){
		StringBuilder retValue = new StringBuilder();
		for(HibernateValidatorConstraint constraint: constraints){
			retValue.append(NL).append(TAB).append(constraint.getAnnotationString());
		}
		
		return retValue.toString();
	}
	
	public Set<String> getConstraintImports(){
		HashSet<String> set = new HashSet<String>();
		for(HibernateValidatorConstraint constraint: constraints){
			set.add(constraint.getValidatorClassName());
		}
		
		return set;
	}
	
}
