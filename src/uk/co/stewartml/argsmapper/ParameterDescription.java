package uk.co.stewartml.argsmapper;

/**
 * Represents a command line parameter.
 *
 * @author Stewart MacKenzie-Leigh
 */
public class ParameterDescription {
    private String[] names;
    private String description;
    private String valueDescription;
    private Integer order;


    public ParameterDescription(String[] names, String description, String valueDescription, Integer order) {
        this.names = names;
        this.description = description;
        this.valueDescription = valueDescription;
        this.order = order;
    }


    public String[] getNames() {
        return names;
    }


    public String getDescription() {
        return description;
    }


    public String getValueDescription() {
        return valueDescription;
    }


    public Integer getOrder() {
        return order;
    }


    public boolean isOrderedParam() {
        return order != null;
    }


    public boolean isFlag() {
        return order == null && valueDescription == null;
    }
}
