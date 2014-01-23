package uk.co.stewartml.argsmapper.stringconverters;

/**
 * @author stewart
 */
public class IntegerStringConverter implements StringConverter {
    @Override
    public Object convert(String value) {
        if (value != null)
            return Integer.parseInt(value);
        else
            return null;
    }
}
