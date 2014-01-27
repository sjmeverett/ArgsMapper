package uk.co.stewartml.argsmapper;

import org.junit.Test;
import uk.co.stewartml.argsmapper.stringconverters.BooleanStringConverter;
import uk.co.stewartml.argsmapper.stringconverters.DoubleStringConverter;
import uk.co.stewartml.argsmapper.stringconverters.IntegerStringConverter;

import static org.junit.Assert.*;

/**
 * @author stewart
 */
public class StringConvertersTest {
    @Test
    public void IntegerStringConverterTest() {
        IntegerStringConverter c = new IntegerStringConverter();
        assertEquals(5, c.convert("5"));
        assertEquals(null, c.convert(null));
    }


    @Test
    public void DoubleStringConverterTest() {
        DoubleStringConverter c = new DoubleStringConverter();
        assertEquals(5.5, c.convert("5.5"));
        assertEquals(null, c.convert(null));
    }


    @Test
    public void BooleanStringConverterTest() {
        BooleanStringConverter c = new BooleanStringConverter();
        assertEquals(true, c.convert("true"));
        assertEquals(null, c.convert(null));
    }
}
