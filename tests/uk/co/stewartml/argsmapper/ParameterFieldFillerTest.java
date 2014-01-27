package uk.co.stewartml.argsmapper;

import uk.co.stewartml.argsmapper.parameterobjects.DelegatingParameters;
import uk.co.stewartml.argsmapper.parameterobjects.TestParameters;
import uk.co.stewartml.argsmapper.stringconverters.StringConverter;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;

/**
 * @author stewart
 */
public class ParameterFieldFillerTest {
    @Test
    public void test() {
        ParameterFieldFiller filler = new ParameterFieldFiller(TestParameters.class, new HashMap<Class<?>, StringConverter>());
        CommandLineParameters params = new CommandLineParameters(new String[] {"-v", "--option", "value", "parameter"});
        TestParameters parameters = (TestParameters)filler.fill(params);

        assertEquals(true, parameters.verbose);
        assertEquals("value", parameters.option);
        assertEquals("parameter", parameters.parameter);
    }


    @Test(expected = ParameterException.class)
    public void orderedParamNotPresentTest() {
        ParameterFieldFiller filler = new ParameterFieldFiller(TestParameters.class, new HashMap<Class<?>, StringConverter>());
        CommandLineParameters params = new CommandLineParameters(new String[] {"-v", "--option", "value"});
        TestParameters parameters = (TestParameters)filler.fill(params);
    }


    @Test
    public void delegateTest() {
        ParameterFieldFiller filler = new ParameterFieldFiller(DelegatingParameters.class, new HashMap<Class<?>, StringConverter>());
        CommandLineParameters params = new CommandLineParameters(new String[] {"-v", "--option", "value", "parameter"});
        DelegatingParameters parameters = (DelegatingParameters)filler.fill(params);

        assertEquals(true, parameters.params.verbose);
        assertEquals("value", parameters.params.option);
        assertEquals("parameter", parameters.params.parameter);
    }
}
