package uk.co.stewartml.argsmapper;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author stewart
 */
public class CommandLineParametersTest {
    @Test
    public void test() {
        CommandLineParameters params = new CommandLineParameters(new String[] {"-f", "-o", "value", "arg", "-b"});

        assertEquals("true", params.getOption("-f"));
        assertEquals("value", params.getOption("-o"));
        assertEquals("arg", params.getOrderedParameter(0));
        assertEquals("true", params.getOption("-b"));
    }


    @Test
    public void getOptionTest() {
        CommandLineParameters params = new CommandLineParameters(new String[] {"--flag"});

        assertEquals("true", params.getOption(new String[] {"-f", "--flag"}));
    }
}
