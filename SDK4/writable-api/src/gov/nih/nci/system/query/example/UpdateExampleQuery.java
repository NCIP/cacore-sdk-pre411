package gov.nih.nci.system.query.example;

import gov.nih.nci.system.query.ExampleManipulationQuery;

public class UpdateExampleQuery extends ExampleQuery implements ExampleManipulationQuery
{
	private static final long serialVersionUID = 1L;

	public UpdateExampleQuery(Object example) {
		super(example);
	}
}