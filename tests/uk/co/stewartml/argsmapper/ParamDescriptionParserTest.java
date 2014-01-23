package uk.co.stewartml.argsmapper;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author stewart
 */
public class ParamDescriptionParserTest {
    @Test
    public void orderedParameterTest() {
        ParamDescriptionParser parser = new ParamDescriptionParser("5:<option> hello world");
        ParameterDescription description = parser.parse();

        assertNotNull(description);
        assertTrue(description.isOrderedParam());
        assertFalse(description.isFlag());
        assertNull(description.getNames());
        assertEquals("option", description.getValueDescription());
        assertEquals("hello world", description.getDescription());
        assertEquals((Integer)5, description.getOrder());
    }


    @Test
    public void flagTest() {
        ParamDescriptionParser parser = new ParamDescriptionParser("-f, --flag the description");
        ParameterDescription description = parser.parse();

        assertNotNull(description);
        assertTrue(description.isFlag());
        assertFalse(description.isOrderedParam());
        assertNull(description.getValueDescription());

        String[] names = description.getNames();
        assertEquals(2, names.length);
        assertEquals("-f", names[0]);
        assertEquals("--flag", names[1]);

        assertEquals("the description", description.getDescription());
    }


    @Test
    public void optionTest() {
        ParamDescriptionParser parser = new ParamDescriptionParser("-o, --option <value> an option");
        ParameterDescription description = parser.parse();

        assertNotNull(description);
        assertFalse(description.isFlag());
        assertFalse(description.isOrderedParam());

        String[] names = description.getNames();
        assertEquals(2, names.length);
        assertEquals("-o", names[0]);
        assertEquals("--option", names[1]);

        assertEquals("value", description.getValueDescription());
        assertEquals("an option", description.getDescription());
    }


    @Test(expected = ParameterException.class)
    public void noDescriptionTest() {
        ParamDescriptionParser parser = new ParamDescriptionParser("-f");
        parser.parse();
    }


    @Test(expected = ParameterException.class)
    public void malformedTest() {
        ParamDescriptionParser parser = new ParamDescriptionParser("hello");
        parser.parse();
    }
}
