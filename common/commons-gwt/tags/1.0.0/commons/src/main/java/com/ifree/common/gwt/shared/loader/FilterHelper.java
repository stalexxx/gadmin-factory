package com.ifree.common.gwt.shared.loader;

import com.google.gwt.text.shared.Parser;
import com.ifree.common.gwt.client.ui.fields.BaseField;

import java.text.ParseException;
import java.util.Collection;

/**
 * Created by alex on 18.04.14.
 */
public class FilterHelper {

    public static final String BOOLEAN_TYPE = "boolean";
    public static final String STRING_TYPE = "string";
    public static final String FLOAT_TYPE = "number";

    private static BooleanFilterHandler booleanFilterHandler;

    private static StringFilterHandler stringFilterHandler;

    private static NumberFilterHandler<Float> floatNumberFilterHandler;

    static {
        booleanFilterHandler = new BooleanFilterHandler();
        stringFilterHandler = new StringFilterHandler();
        floatNumberFilterHandler = new NumberFilterHandler<Float>(new Parser<Float>() {
            @Override
            public Float parse(CharSequence text) throws ParseException {
                return Float.parseFloat(text.toString());
            }
        });
    }



    public <V> FilterConfigBean createConfig(BaseField field, V value) {
        FilterConfigBean configBean = new FilterConfigBean();

        configBean.setField(field.getPath());
        if (value instanceof String) {

            configBean.setType(STRING_TYPE);
            configBean.setValue(stringFilterHandler.convertToString((String) value));
        } else if (value instanceof Boolean) {

            configBean.setType(BOOLEAN_TYPE);
            configBean.setValue(booleanFilterHandler.convertToString((Boolean) value));
        } else if (value instanceof Float) {

            configBean.setType(FLOAT_TYPE);
            configBean.setValue(floatNumberFilterHandler.convertToString((Float) value));
        }


        return configBean;
    }

    public <V> void appendTo(Collection<FilterConfigBean> collection, BaseField field, V value) {
        collection.add(createConfig(field, value));
    }

    public <V> V getValue(String type, String value) {
        if (STRING_TYPE.equals(type)) {
            return (V) stringFilterHandler.convertToObject(value);
        } else if (BOOLEAN_TYPE.equals(type)) {
            return (V) booleanFilterHandler.convertToObject(value);
        } else if (FLOAT_TYPE.equals(type)) {
            return (V) floatNumberFilterHandler.convertToObject(value);
        }
        return null;
    }
}
