package org.fingies;

import java.util.ArrayList;
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
	
	private List<Relationship> currRelationships;
	
	/**
	 * Default constructor of a Change Object
	 * @param oldClass The class to copy
	 * @param oldRelationships A list of all of the relationships associated with this class to copy
	 */
	public Change(UMLClass oldClass, List<Relationship> oldRelationships)
	{
		if (oldClass != null)
		{
			this.oldClass = new UMLClass(oldClass);
			this.oldRelationships = new ArrayList<>();
			for (Relationship r : oldRelationships)
			{
				this.oldRelationships.add(new Relationship(r));
			}
		}
		else
		{
			this.oldClass = null;
			this.oldRelationships = List.of();
		}
	}
	
	/*
	 * Getter & Setter methods all create a copy to return/store.
	 * It is important that the Change class doesn't contain any links to existing any objects
	 * in the diagram, just copies of their data values.
	 */
	
	public void setCurrClass(UMLClass currClass)
	{
		if (currClass != null)
			this.currClass = new UMLClass(currClass);
		else
			this.currClass = null;
	}
	
	public void setCurrRelationships(List<Relationship> currRelationships)
	{
		this.currRelationships = new ArrayList<Relationship>();
		if(currRelationships == null)
		{
			return;
		}
		for (Relationship r : currRelationships)
		{
			this.currRelationships.add(new Relationship(r));
		}
	}

	public UMLClass getOldClass() 
	{
		if (oldClass != null)
			return new UMLClass(oldClass);
		else
			return null;
	}

	public UMLClass getCurrClass() 
	{
		if (currClass != null)
			return new UMLClass(currClass);
		else
			return null;
	}
	
	public List<Relationship> getOldRelationships()
	{
		List<Relationship> oldRelationships = new ArrayList<Relationship>();
		if(this.oldRelationships == null)
		{
			return null;
		}
		for (Relationship r : this.oldRelationships)
		{
			oldRelationships.add(r);
		}
		return oldRelationships;
	}
	
	public List<Relationship> getCurrRelationships()
	{
		List<Relationship> currRelationships = new ArrayList<Relationship>();
		if(this.currRelationships == null)
		{
			return null;
		}
		for (Relationship r : this.currRelationships)
		{
			currRelationships.add(new Relationship(r));
		}
		return currRelationships;
	}
	
	/**
	 * Gets the old relationships of this class.
	 * 
	 * Since its important for Relationship objects to store links to the correct UMLClass objects in the diagram,
	 * this method accepts a UMLClass and links each relationship in the returned list to that class.
	 * 
	 * @param classToLinkTo The UMLClass to replace every instance of oldClass in the list of relationships with.
	 * @return A list of relationships, with each relationship linking to the given UMLClass object.
	 */
	public List<Relationship> getOldRelationshipsUsingLink(UMLClass classToLinkTo)
	{
		List<Relationship> oldRelationships = new ArrayList<Relationship>();
		if(this.oldRelationships == null)
		{
			return null;
		}
		for (Relationship r : this.oldRelationships)
		{
			oldRelationships.add(new Relationship(r.getSrc().getName().equals(classToLinkTo.getName()) ? classToLinkTo : r.getSrc(),
					r.getDest().getName().equals(classToLinkTo.getName()) ? classToLinkTo : r.getDest(),
					r.getType()));
		}
		return oldRelationships;
	}
	
	/**
	 * Gets the old relationships of this class.
	 * 
	 * Since its important for Relationship objects to store links to the correct UMLClass objects in the diagram,
	 * this method accepts a UMLClass and links each relationship in the returned list to that class.
	 * 
	 * @param classToLinkTo The UMLClass to replace every instance of oldClass in the list of relationships with.
	 * @return A list of relationships, with each relationship linking to the given UMLClass object.
	 */
	public List<Relationship> getCurrRelationshipsUsingLink(UMLClass classToLinkTo)
	{
		List<Relationship> currRelationships = new ArrayList<Relationship>();
		if(this.currRelationships == null)
		{
			return null;
		}
		for (Relationship r : this.currRelationships)
		{
			currRelationships.add(new Relationship(r.getSrc().getName().equals(classToLinkTo.getName()) ? classToLinkTo : r.getSrc(),
					r.getDest().getName().equals(classToLinkTo.getName()) ? classToLinkTo : r.getDest(),
					r.getType()));
		}
		return currRelationships;
	}
	
	
}	
