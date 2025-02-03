import java.util.Set;
/**
 * Relationship Handler class to add, delete and get relationships
 * @author Nick Hayes & Lincoln Craddock
 */
public class RelationshipHandler
{
    //Tree Set for no duplicates and it is sorted
    private TreeSet<Relationship> relationships;

    /**
     * @throws IllegalArgumentException when trying to add a relationship that already exists
     * @throws IllegalArgumentException when trying to add a relationship between at least 1 non existing class
     */
    static void addRelationship(String src, String dest)
    {
        if(exists(src) && exists(dest))
        {
            Relationship r = new Relationship(src, dest);
            if(relationship.contains(r))
            {
                throw new IllegalArgumentException("This relationship already exists");
            }
            relationships.add(r);
        }
        else
        {
            throw new IllegalArgumentException("Either the source or destination is invalid");
        }
    }
}
