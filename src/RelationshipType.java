
public enum RelationshipType {
	
	AGGREGATION("----◇"),
	COMPOSITION("----◆"),
	INHERITANCE("----▷"),
	REALIZATION("- - ▷"),
	/**
	 * For creating temporary, internal relationships; not for the user.
	 */
	DEFAULT("");
	
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

}
