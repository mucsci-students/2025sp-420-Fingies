package org.fingies;

/**
 * @Author Nick Hayes & Lincoln Craddock
 */

public class Change 
{
	private UMLClass oldClass;
	private UMLClass currClass;
	
	/**
	 * Default constructor of a Change Object
	 * @param oldClass The class to copy and store in the undo stack
	 */
	public Change(UMLClass oldClass)
	{
		this.oldClass = new UMLClass(oldClass);
	}
	
	public void setCurrClass(UMLClass currClass)
	{
		this.currClass = currClass;
	}

	public UMLClass getOldClass() 
	{
		return oldClass;
	}

	public UMLClass getCurrClass() 
	{
		return currClass;
	}
	
	
}	
