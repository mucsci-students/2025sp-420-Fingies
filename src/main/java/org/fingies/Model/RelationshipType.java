package org.fingies.Model;

public enum RelationshipType {
	
	Aggregation("----◇", "Aggregation"),
	Composition("----◆", "Composition"),
	Inheritance("----▷", "Inheritance"),
	Realization("- - ▷", "Realization"),
	/**
	 * For creating temporary, internal relationships; not for the user.
	 */
	DEFAULT("", "default");
	
	private static final String[] AGGR_NAMES = {"aggregation", "aggr", "a"};
	private static final String[] COMP_NAMES = {"composition", "comp", "c"};
	private static final String[] INHR_NAMES = {"inheritance", "inhr", "i"};
	private static final String[] REAL_NAMES = {"realization", "real", "r"};
	
	private final String symbol;
	private final String name;
	
	RelationshipType(String s, String n)
	{
		symbol = s;
		name = n;
	}
	
	@Override
	public String toString()
	{
		return symbol;
	}
	
	public String getName()
	{
		return name;
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
