/**
 * A class to create a relationship between two other classes
 * @author Nick Hayes & Lincoln Craddock
 */
public class Relationship 
{
    private String src;
    private String dest;

    /**
     * Relationship constructor
     * @param src The name of the source of the relationship
     * @param dest The name of the destination of the relationship
     */
    Relationship(String src, String dest)
    {
        if(ClassHandler.exists(src))
        {
            this.src = src;
        }
        else
        {
            throw new IllegalArgumentException("Source does not exist");
        }
        if(ClassHandler.exists(dest))
        {
            this.dest = dest;
        }
        else
        {
            throw new IllegalArgumentException("Destination does not exist");
        }
    }

    /**
     * Getter method for the src field
     * @return The src of the object called upon
     */
    String getSrc()
    {
        return this.src;
    }

    /**
     * Getter method for the dest field
     * @return The dest of the object called upon
     */
    String getDest()
    {
        return this.dest;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj != null && obj.getClass() == this.getClass())
        {
            Relationship r = (Relationship)obj;
            if(this.src == r.src && this.dest == r.dest)
            {
                return true;
            }
        }
        return false;
    }

    @Override 
    public String toString()
    {
        return this.src + " --> " + this.dest;
    }

    @Override
    public int hashCode()
    {
        return this.src.hashCode() * 5 + this.dest.hashCode() * 7;
    }
}    