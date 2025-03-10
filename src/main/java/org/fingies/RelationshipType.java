package org.fingies;

public enum RelationshipType {
	
	Aggregation("----◇"),
	Composition("----◆"),
	Inheritance("----▷"),
	Realization("- - ▷"),
	/**
	 * For creating temporary, internal relationships; not for the user.
	 */
	DEFAULT("");
	
	private static final String[] AGGR_NAMES = {"aggregation", "aggr", "a"};
	private static final String[] COMP_NAMES = {"composition", "comp", "c"};
	private static final String[] INHR_NAMES = {"inheritance", "inhr", "i"};
	private static final String[] REAL_NAMES = {"realization", "real", "r"};
	
	private final String symbol;
	
	RelationshipType(String s)
	{
		symbol = s;
	}
	
	@Override
	public String toString()
	{
		return symbol;
	}
	
	public static RelationshipType fromString(String arg0)
	{
		for (String name : AGGR_NAMES)
			if (name.equalsIgnoreCase(arg0))
				return RelationshipType.Aggregation;
		
		for (String name : COMP_NAMES)
			if (name.equalsIgnoreCase(arg0))
				return RelationshipType.Composition;
		
		for (String name : INHR_NAMES)
			if (name.equalsIgnoreCase(arg0))
				return RelationshipType.Inheritance;
		
		for (String name : REAL_NAMES)
			if (name.equalsIgnoreCase(arg0))
				return RelationshipType.Realization;
		
		return null;
	}

}
