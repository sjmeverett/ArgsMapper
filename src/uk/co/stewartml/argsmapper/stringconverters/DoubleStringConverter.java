package uk.co.stewartml.argsmapper.stringconverters;

/**
 * @author stewart
 */
public class DoubleStringConverter implements StringConverter {
    @Override
    public Object convert(String value) {
        if (value != null)
            return Double.parseDouble(value);
        else
            return null;
    }
}
