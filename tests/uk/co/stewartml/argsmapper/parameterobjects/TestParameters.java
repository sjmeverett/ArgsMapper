package uk.co.stewartml.argsmapper.parameterobjects;

import uk.co.stewartml.argsmapper.annotations.Param;

/**
 * @author stewart
 */
public class TestParameters {
    @Param("-v, --verbose Prints out more log information.")
    public boolean verbose;


    @Param("-o, --option <value> An option.")
    public String option;


    @Param("0:<parameter> A parameter.")
    public String parameter;
}
