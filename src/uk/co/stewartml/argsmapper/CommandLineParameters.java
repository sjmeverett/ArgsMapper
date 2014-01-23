package uk.co.stewartml.argsmapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author stewart
 */
public class CommandLineParameters {
    private List<String> orderedParameters;
    private Map<String, String> options;


    public CommandLineParameters(String[] args) {
        orderedParameters = new ArrayList<String>();
        options = new HashMap<String, String>();

        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                if (i == args.length - 1 || args[i + 1].startsWith("-")) {
                    options.put(args[i], "true");
                }
                else {
                    options.put(args[i], args[i + 1]);
                    i++;
                }
            }
            else {
                orderedParameters.add(args[i]);
            }
        }
    }


    public String getOrderedParameter(int index) {
        return orderedParameters.get(index);
    }


    public String getOption(String name) {
        return options.get(name);
    }


    public String getOption(String[] names) {
        for (String name: names) {
            if (options.containsKey(name))
                return options.get(name);
        }

        return null;
    }
}
