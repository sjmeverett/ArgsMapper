ArgsMapper
===============

This project simplifies writing applications in Java that take command line arguments.  There are a few other projects
that help with this, but none seemed to fit my needs exactly.

To use in a program, write your program as a class which implements the `CommandClass` interface; the `run` method is
analogous to `static void main` in regular programs.  The `Command` annotation gives information about the command,
and the `Param` annotation marks fields which are to be filled out from command line arguments.

The `Param` annotation takes a string description as an argument which resembles standard help output.  Here's an
example.

```java
import uk.co.stewartml.argsmapper.CommandClass;
import uk.co.stewartml.argsmapper.annotations.*;

@Command(
    name = "greeting",
    description = "This program prints a greeting to the command line."
)
public class GreetingCommand implements CommandClass {
    @Param("0:<name> The name of the person to greet.")
    public String name;

    @Param("-g, --greeting <greeting> The greeting to use (default 'Hello').")
    public String greeting = "Hello";

    @Param("-f A boolean flag")
    public Boolean flag;

    @Override
    public void run() {
        System.out.printf("%s, %s!", greeting, name);
    }
}
```

This creates a command which has two parameters.  `name` is position based, i.e., it is the first non-flag argument
supplied on the command line.  This is denoted by an integer specifying position, followed by a colon and a name for
the parameter enclosed in angle brackets.  The string afterwords gives a description which will be automatically
shown if the program is run with a `-h` or `--help` flag.

The entry point should look something like the following:

```java
public static void main(String[] args) {
    CommandLineProgram program = new CommandLineProgram();

    program
        .program(GreetingCommand.class)
        .run(args);
}
```

This creates a new instance of the `CommandLineProgram` class, which is the entry point for the framework.  The
`program` method tells it what class represents the program, and the `run` method actually runs it, parsing the supplied
arguments.

The `CommandLineProgram` class also has a `command` method, to be used instead of `program`.  If you want to implement
something like the git command line program, which has multiple commands like `add` and `push` and so on, this is what
you would use.

```java
program
    .command(AddCommand.class)
    .command(PushCommand.class)
    ...
    .run(args);
```

In order to determine what command to use, it will look at the first argument supplied and compare it to the names
specified in the `Command` annotations in the given classes.  It will automatically show an error if it can't figure
which command class to use, and the help message will automatically include all the commands.

Arguments are mapped to values using implementations of the `StringConverter` interface.  There's a few inbuilt ones,
for converting to types of `Boolean`, `Integer`, and `File`.  You can register your own with the `registerConverter`
method.

```java
program
    .command(AddCommand.class)
    .command(PushCommand.class)
    ...
    .registerConverter(MyType.class, new MyTypeStringConverter())
    .run(args);
```

If you have a set of parameters common to a few different commands, you can use the `ParamsObject` annotation.  This
will map command line arguments to appropriately annotated fields in the custom object, even other fields annotated
with `ParamsObject`, to 'any' depth.  E.g.:

```java
@Command(...)
public class MyCommand implements CommandClass {
    @ParamsObject
    public MyCommonParams common;

    @Param("-n, --number <value> An integer");
    public Integer n;

    @Override
    public void run() { ... }
}

...

public class MyCommonParams {
    @Param("--param <value> A common parameter.")
    public String value;

    //could also have another field marked @ParamsObject here, etc
}
```

That's hopefully a good enough brief introduction.  I'll hopefully expand on this later.

It's all MIT licensed, comments, suggestions and improvements are welcomed.  Have fun!
