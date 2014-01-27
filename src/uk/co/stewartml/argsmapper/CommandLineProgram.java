package uk.co.stewartml.argsmapper;

import uk.co.stewartml.argsmapper.annotations.Command;
import uk.co.stewartml.argsmapper.annotations.Param;
import uk.co.stewartml.argsmapper.annotations.ParamsObject;
import uk.co.stewartml.argsmapper.stringconverters.*;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author stewart
 */
public class CommandLineProgram {
    private Map<String, Class<? extends CommandClass>> commands;
    private Map<Class<?>, StringConverter> converters;
    private Class<? extends CommandClass> program;


    public CommandLineProgram() {
        commands = new HashMap<String, Class<? extends CommandClass>>();
        converters = new HashMap<Class<?>, StringConverter>();

        converters.put(Integer.class, new IntegerStringConverter());
        converters.put(Double.class, new DoubleStringConverter());
        converters.put(Boolean.class, new BooleanStringConverter());
        converters.put(File.class, new FileStringConverter());
    }


    public CommandLineProgram command(Class<? extends CommandClass> command) {
        if (program != null)
            throw new ParameterException("You can either have a program or a list of commands, not both.");

        Command cmd = command.getAnnotation(Command.class);

        if (cmd == null)
            throw new IllegalArgumentException("command must have a Command annotation.");

        commands.put(cmd.name(), command);
        return this;
    }


    public CommandLineProgram program(Class<? extends CommandClass> command) {
        if (commands.size() > 0)
            throw new ParameterException("You can either have a program or a list of commands, not both.");

        Command cmd = command.getAnnotation(Command.class);

        if (cmd == null)
            throw new IllegalArgumentException("command must have a Command annotation.");

        program = command;
        return this;
    }


    public CommandLineProgram registerConverter(Class<?> cls, StringConverter converter) {
        converters.put(cls, converter);
        return this;
    }


    public void run(String[] args) {
        if (args.length > 0 && (args[0].equals("-h") || args[0].equals("--help"))) {
            showHelp();
        }
        else if (program != null) {
            run(program, args);
        }
        else {
            if (commands.size() == 0)
                throw new ParameterException("No commands defined.");

            if (args.length < 1)
                throw new ParameterException("Command must be specified.");

            Class<? extends CommandClass> command = commands.get(args[0]);

            if (command == null)
                throw new ParameterException("Command not found.");

            if (args.length > 1 && (args[1].equals("-h") || args[1].equals("--help"))) {
                showHelp(command);
            }
            else {
                args = Arrays.copyOfRange(args, 1, args.length);
                run(command, args);
            }
        }
    }


    private void run(Class<? extends CommandClass> command, String[] args) {
        CommandLineParameters params = new CommandLineParameters(args);
        ParameterFieldFiller filler = new ParameterFieldFiller(command, converters);
        CommandClass c = (CommandClass)filler.fill(params);
        c.run();
    }


    private void showHelp() {
        if (program != null) {
            showHelp(program);
        }
        else {
            for (Class<?> command: commands.values()) {
                showHelp(command);
                System.out.println();
            }
        }
    }


    private void showHelp(Class<?> command) {
        Command cmd = command.getAnnotation(Command.class);
        List<ParameterDescription> descriptions = getParameterDescriptions(command);
        DescriptionTable table = new DescriptionTable();

        System.out.printf("\033[1m%s\033[0m ", cmd.name());

        //add ordered parameters to treemap to put them in order and also to description table
        Map<Integer, ParameterDescription> orderedParameters = new TreeMap<Integer, ParameterDescription>();

        for (ParameterDescription description: descriptions) {
            if (description.isOrderedParam()) {
                orderedParameters.put(description.getOrder(), description);
                table.add("<" + description.getValueDescription() + ">", description.getDescription());
            }
        }

        //print out ordered parameters in order
        for (ParameterDescription description: orderedParameters.values()) {
            System.out.print("<" + description.getValueDescription() + "> ");
        }

        System.out.println();

        //print the command description
        System.out.println(cmd.description());

        //add switches to table
        for (ParameterDescription description: descriptions) {
            if (!description.isOrderedParam()) {
                StringBuilder sb = new StringBuilder();
                String[] names = description.getNames();

                for (int i = 0; i < names.length; i++) {
                    sb.append(names[i]);

                    if (i < names.length - 1)
                        sb.append(", ");
                }

                table.add(sb.toString(), description.getDescription());
            }
        }

        //print the table
        table.print();
    }


    private List<ParameterDescription> getParameterDescriptions(Class<?> cls) {
        List<ParameterDescription> descriptions = new ArrayList<ParameterDescription>();

        for (Field field: cls.getDeclaredFields()) {
            Param param = field.getAnnotation(Param.class);

            if (param != null) {
                ParamDescriptionParser parser = new ParamDescriptionParser(param.value());
                descriptions.add(parser.parse());
            }
            else {
                ParamsObject paramsObject = field.getAnnotation(ParamsObject.class);

                if (paramsObject != null) {
                    descriptions.addAll(getParameterDescriptions(field.getType()));
                }
            }
        }

        return descriptions;
    }
}
