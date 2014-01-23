package uk.co.stewartml.argsmapper.stringconverters;

/**
 * @author stewart
 */
public class BooleanStringConverter implements StringConverter {
    @Override
    public Object convert(String value) {
        return Boolean.parseBoolean(value);
    }
}
