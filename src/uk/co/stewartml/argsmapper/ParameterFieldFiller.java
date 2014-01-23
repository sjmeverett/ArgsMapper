package uk.co.stewartml.argsmapper;

import uk.co.stewartml.argsmapper.annotations.Param;
import uk.co.stewartml.argsmapper.annotations.ParamsObject;
import uk.co.stewartml.argsmapper.stringconverters.StringConverter;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author stewart
 */
public class ParameterFieldFiller {
    private Class<?> cls;
    private Map<Class<?>, StringConverter> converters;

    public ParameterFieldFiller(Class<?> cls, Map<Class<?>, StringConverter> converters) {
        this.cls = cls;
        this.converters = converters;
    }


    public Object fill(CommandLineParameters params) {
        try {
            Object obj = cls.newInstance();

            for (Field field: cls.getDeclaredFields()) {
                field.setAccessible(true);
                Param param = field.getAnnotation(Param.class);

                if (param != null) {
                    ParamDescriptionParser parser = new ParamDescriptionParser(param.value());
                    ParameterDescription description = parser.parse();

                    fill(obj, field, description, params);
                }
                else {
                    ParamsObject paramsObject = field.getAnnotation(ParamsObject.class);

                    if (paramsObject != null) {
                        fill(obj, field, params);
                    }
                }
            }

            return obj;
        }
        catch (InstantiationException e) {
            throw new ParameterException("Could not instantiate params object " + cls.getName());
        }
        catch (IllegalAccessException e) {
            throw new ParameterException("Inaccessible constructor or field: " + cls.getName());
        }
    }


    private void fill(Object obj, Field field, ParameterDescription description, CommandLineParameters params) throws IllegalAccessException {
        String value;

        if (description.isOrderedParam()) {
            value = params.getOrderedParameter(description.getOrder());
        }
        else {
            value = params.getOption(description.getNames());
        }

        if (description.isFlag()) {
            if (!field.getType().isAssignableFrom(boolean.class))
                throw new ParameterException(field.getName() + " flag must be boolean.");

            field.setBoolean(obj, value != null);
        }
        else if (field.getType().equals(String.class)) {
            field.set(obj, value);
        }
        else {
            StringConverter converter = converters.get(field.getType());

            if (converter == null)
                throw new ParameterException("No converter for type " + field.getType().getName());

            Object o = converter.convert(value);

            if (o != null)
                field.set(obj, o);
        }
    }


    private void fill(Object obj, Field field, CommandLineParameters params) throws IllegalAccessException {
        ParameterFieldFiller filler = new ParameterFieldFiller(field.getType(), converters);
        Object o = filler.fill(params);
        field.set(obj, o);
    }
}
