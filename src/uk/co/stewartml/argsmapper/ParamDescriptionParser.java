package uk.co.stewartml.argsmapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses descriptions for command line parameters.  It recognises the following strings:
 * "-f, --f Description": 1 or more flag names prefixed with a dash, followed by a description
 * "-o, --option <value description> Description": 1 or more option names, followed by a value description in square
 *      brackets, followed by a description of the option
 * "0:<value description> Description": an index for an ordered parameter, followed by a value description, followed by
 *      a description of the parameter
 *
 * @author Stewart MacKenzie-Leigh
 */
public class ParamDescriptionParser {
    private char[] input;
    private int pos;


    /**
     * Creates a new ParamDescriptionParser.
     *
     * @param input The string to parse.
     */
    public ParamDescriptionParser(String input) {
        this.input = input.toCharArray();
        pos = 0;
    }


    /**
     * Parse the string given at instantiation into a ParameterDescription object.
     * @return
     */
    public ParameterDescription parse() {
        String[] names = null;
        String valueDescription = null;
        String description;
        Integer order = null;


        if (is('-')) {
            List<String> l = new ArrayList<String>();
            nameList(l);
            names = new String[l.size()];
            l.toArray(names);
        }
        else {
            order = order();
        }

        if (is('<')) {
            valueDescription = valueDescription();
        }

        if (names == null && valueDescription == null)
            throw new ParameterException("Error parsing parameter description - expected either a flag, option or ordered parameter.");

        description = description();

        if (description.length() == 0)
            throw new ParameterException("Expected description.");

        return new ParameterDescription(names, description, valueDescription, order);
    }


    private void nameList(List<String> names) {
        mustbe('-');
        names.add(name());
        space();

        if (is(',')) {
            pos++;
            space();
            nameList(names);
        }
    }


    private String name() {
        StringBuilder sb = new StringBuilder();

        while (!is(',') && !is(' '))
            sb.append(input[pos++]);

        return sb.toString();
    }


    private int order() {
        StringBuilder sb = new StringBuilder();

        if (!Character.isDigit(input[pos]))
            throw new ParameterException("Error parsing parameter description - expected digit.");

        while (Character.isDigit(input[pos]))
            sb.append(input[pos++]);

        space();
        mustbe(':');
        space();
        pos++;

        return Integer.parseInt(sb.toString());
    }


    private String valueDescription() {
        mustbe('<');
        pos++;

        StringBuilder sb = new StringBuilder();

        while (!is('>')) {
            sb.append(input[pos++]);
        }

        mustbe('>');
        pos++;

        return sb.toString();
    }


    private String description() {
        space();

        StringBuilder sb = new StringBuilder();

        while (pos < input.length)
            sb.append(input[pos++]);

        return sb.toString();
    }


    private void space() {
        while (is(' '))
            pos++;
    }


    private boolean is(char c) {
        if (pos >= input.length)
            throw new ParameterException("Error parsing parameter description - ran out of characters.");

        return input[pos] == c;
    }


    private void mustbe(char c) {
        if (!is(c))
            throw new ParameterException("Error parsing parameter description - expected " + c);
    }
}
