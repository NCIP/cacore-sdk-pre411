package gov.nih.nci.system.query.example;

import gov.nih.nci.system.query.ExampleManipulationQuery;

public class DeleteExampleQuery extends ExampleQuery implements ExampleManipulationQuery
{
	private static final long serialVersionUID = 1L;

	public DeleteExampleQuery(Object example) {
		super(example);
	}
}