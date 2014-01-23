package uk.co.stewartml.argsmapper.stringconverters;

import java.io.File;

/**
 * @author stewart
 */
public class FileStringConverter implements StringConverter {
    @Override
    public Object convert(String value) {
        return new File(value);
    }
}
