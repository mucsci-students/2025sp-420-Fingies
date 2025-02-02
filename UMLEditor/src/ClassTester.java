import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.TreeSet;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class ClassTester {

    Class c;

    @Before
    public void setUp()
    {
        c = new Class ("Class");
    }

    @Test
    public void renameClassToCar()
    {
        c.renameClass ("Car");
        assertEquals("Car", c.getName());

    }

    @Test
    public void createOneAttributeCalledEngine()
    {
        c.addAttribute("Engine");
        TreeSet<String> set = c.getAllAttributes();
        assertTrue(set.contains("Engine"));
    }
}
