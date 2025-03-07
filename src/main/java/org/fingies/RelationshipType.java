package org.fingies;

public enum RelationshipType {
	
	AGGREGATION("----◇"),
	COMPOSITION("----◆"),
	INHERITANCE("----▷"),
	REALIZATION("- - ▷"),
	/**
	 * For creating temporary, internal relationships; not for the user.
	 */
	DEFAULT("");
	
	private static final String[] AGGR_NAMES = {"aggregation", "aggr"};
	private static final String[] COMP_NAMES = {"composition", "comp"};
	private static final String[] INHR_NAMES = {"inheritance", "inhr"};
	private static final String[] REAL_NAMES = {"realization", "real"};
	
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
				return RelationshipType.AGGREGATION;
		
		for (String name : COMP_NAMES)
			if (name.equalsIgnoreCase(arg0))
				return RelationshipType.COMPOSITION;
		
		for (String name : INHR_NAMES)
			if (name.equalsIgnoreCase(arg0))
				return RelationshipType.INHERITANCE;
		
		for (String name : REAL_NAMES)
			if (name.equalsIgnoreCase(arg0))
				return RelationshipType.REALIZATION;
		
		return null;
	}

}
