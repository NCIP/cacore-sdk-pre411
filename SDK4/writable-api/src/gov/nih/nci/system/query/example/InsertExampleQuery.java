package gov.nih.nci.system.query.example;

import gov.nih.nci.system.query.ExampleManipulationQuery;

public class InsertExampleQuery extends ExampleQuery implements ExampleManipulationQuery
{
	private static final long serialVersionUID = 1L;

	public InsertExampleQuery(Object example) {
		super(example);
	}
}