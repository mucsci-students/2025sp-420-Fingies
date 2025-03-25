package org.fingies;

import java.util.List;

/**
 * @Author Nick Hayes & Lincoln Craddock
 */

public class Change 
{
	/** A copy of the old class before a change took place. */
	private UMLClass oldClass;
	/** A copy of the current class. */
	private UMLClass currClass;
	/** A copy of all of the relationships the old class was the source or destination for before the change. */
	private List<Relationship> oldRelationships;
	
	/**
	 * Default constructor of a Change Object
	 * @param oldClass The class to copy
	 * @param oldRelationships A list of all of the relationships associated with this class to copy
	 */
	public Change(UMLClass oldClass, List<Relationship> oldRelatioships)
	{
		if (oldClass != null)
		{
			this.oldClass = new UMLClass(oldClass);
			this.oldRelationships = List.copyOf(oldRelationships);
		}
		else
		{
			this.oldClass = null;
			this.oldRelationships = List.of();
		}
	}
	
	public void setCurrClass(UMLClass currClass)
	{
		if (currClass != null)
			this.currClass = new UMLClass(currClass);
		else
			this.oldClass = null;
	}

	public UMLClass getOldClass() 
	{
		return oldClass;
	}

	public UMLClass getCurrClass() 
	{
		return currClass;
	}
	
	public List<Relationship> getOldRelationships()
	{
		return oldRelationships;
	}
	
	
}	
