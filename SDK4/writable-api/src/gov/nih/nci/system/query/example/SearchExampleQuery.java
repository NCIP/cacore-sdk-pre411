package gov.nih.nci.system.query.example;

import gov.nih.nci.system.query.ExampleSearchQuery;

public class SearchExampleQuery extends ExampleQuery implements ExampleSearchQuery
{
	private static final long serialVersionUID = 1L;

	public SearchExampleQuery(Object example) {
		super(example);
	}
}