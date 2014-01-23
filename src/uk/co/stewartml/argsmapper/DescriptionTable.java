package uk.co.stewartml.argsmapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author stewart
 */
public class DescriptionTable {
    private List<String> parameters;
    private List<String> descriptions;
    private int parameterWidth;


    public DescriptionTable() {
        parameters = new ArrayList<String>();
        descriptions = new ArrayList<String>();
        parameterWidth = 0;
    }


    public void add(String parameter, String description) {
        parameters.add(parameter);
        descriptions.add(description);

        if (parameter.length() > parameterWidth)
            parameterWidth = parameter.length();
    }


    public void print() {
        for (int i = 0; i < parameters.size(); i++) {
            printLine(i);
        }
    }


    public void printLine(int line) {
        String parameter = parameters.get(line);
        String description = descriptions.get(line);

        System.out.print("  " + parameter);

        for (int i = parameter.length(); i < parameterWidth; i++)
            System.out.print(" ");

        System.out.println("  " + description);
    }
}
