import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.fingies.Parameter;
import org.junit.Before;
import org.junit.Test;

public class ParameterTest {
    
    Parameter p;

    @Before
    public void setup()
    {
        p = new Parameter("Name", "String");
    }

    @Test
    public void getParameterName()
    {
        assertEquals(p.getName(), "Name");
    }

    @Test
    public void setParameterName()
    {
        p.setName("Age");
        assertEquals(p.getName(), "Age");
    }

    @Test
    public void getParameterType()
    {
        assertEquals(p.getType(), "String");
    }

    @Test
    public void setParameterType()
    {
        p.setType("int");
        assertEquals(p.getType(), "int");
    }

    @Test
    public void checkParameterEqualsTrue()
    {
        Parameter q = new Parameter("Name", "String");
        assertTrue(p.equals(q));
    }

    @Test
    public void checkParameterEqualsFalse()
    {
        Parameter q = new Parameter("Age", "int");
        assertFalse(p.equals(q));
    }

    @Test
    public void checkHashCodeEqualsTrue()
    {
        Parameter q = new Parameter("Name", "String");
        assertTrue(p.hashCode() == q.hashCode());
    }

    @Test
    public void checkHashCodeEqualsFalse()
    {
        Parameter q = new Parameter("Age", "int");
        assertFalse(p.hashCode() == q.hashCode());
    }

    @Test
    public void checkToString()
    {
        assertEquals(p.toString(), "String Name");
    }

    @Test
    public void testNotEquals() {
        
    }
}
