package com.ifree.common.gwt.client.ui.grids;

import com.google.common.collect.Maps;
import com.google.gwt.text.shared.Parser;
import com.ifree.common.gwt.shared.ValueProvider;
import com.ifree.common.gwt.shared.loader.*;

import java.text.ParseException;
import java.util.Collection;
import java.util.Map;

/**
 * Created by alex on 18.04.14.
 */
public class FilterHelper {

    public static final String BOOLEAN_TYPE = "boolean";
    public static final String STRING_TYPE = "string";
    public static final String FLOAT_TYPE = "float";
    public static final String INTEGER_TYPE = "integer";


    private static BooleanFilterHandler booleanFilterHandler;

    private static StringFilterHandler stringFilterHandler;

    private static NumberFilterHandler<Float> floatNumberFilterHandler;
    private static NumberFilterHandler<Integer> integerFilterHandler;

    private Map<String, FilterHandler> handlerMap = Maps.newHashMap();
    private Map<Class, String> typeMap = Maps.newHashMap();

    static {
        booleanFilterHandler = new BooleanFilterHandler();
        stringFilterHandler = new StringFilterHandler();
        floatNumberFilterHandler = new NumberFilterHandler<Float>(new Parser<Float>() {
            @Override
            public Float parse(CharSequence text) throws ParseException {
                return Float.parseFloat(text.toString());
            }
        });
        integerFilterHandler = new NumberFilterHandler(new Parser<Integer>() {
            @Override
            public Integer parse(CharSequence text) throws ParseException {
                return Integer.parseInt(text.toString());
            }
        });
    }

    {
        handlerMap.put(STRING_TYPE, stringFilterHandler);
        handlerMap.put(BOOLEAN_TYPE, booleanFilterHandler);
        handlerMap.put(FLOAT_TYPE, floatNumberFilterHandler);
        handlerMap.put(INTEGER_TYPE, integerFilterHandler);

        typeMap.put(String.class, STRING_TYPE);
        typeMap.put(Boolean.class, BOOLEAN_TYPE);
        typeMap.put(Float.class, FLOAT_TYPE);
        typeMap.put(Integer.class, INTEGER_TYPE);

    }

    protected <T> void register(String type, Class<T> clazz,  FilterHandler<T> handler) {
        handlerMap.put(type, handler);
        typeMap.put(clazz, type);
    }

    public <V> FilterConfigBean createConfig(ValueProvider field, V value) {
        FilterConfigBean configBean = new FilterConfigBean();

        configBean.setField(field.getPath());

        if (value != null) {
            String strType = typeMap.get(value.getClass());
            FilterHandler<V> filterHandler = handlerMap.get(strType);

            configBean.setType(strType);
            configBean.setValue(filterHandler.convertToString(value));
        }
      /*  if (value instanceof String) {

            configBean.setType(STRING_TYPE);
            configBean.setValue(stringFilterHandler.convertToString((String) value));
        } else if (value instanceof Boolean) {

            configBean.setType(BOOLEAN_TYPE);
            configBean.setValue(booleanFilterHandler.convertToString((Boolean) value));
        } else if (value instanceof Float) {

            configBean.setType(FLOAT_TYPE);
            configBean.setValue(floatNumberFilterHandler.convertToString((Float) value));
        }
*/

        return configBean;
    }

    public <V> void appendTo(Collection<FilterConfigBean> collection, ValueProvider field, V value) {
        collection.add(createConfig(field, value));
    }

    public <V> V getValue(String type, String value) {
        FilterHandler filterHandler = handlerMap.get(type);
        if (filterHandler != null) {
            return (V) filterHandler.convertToObject(value);
        }
        /*if (STRING_TYPE.equals(type)) {
            return (V) stringFilterHandler.convertToObject(value);
        } else if (BOOLEAN_TYPE.equals(type)) {
            return (V) booleanFilterHandler.convertToObject(value);
        } else if (FLOAT_TYPE.equals(type)) {
            return (V) floatNumberFilterHandler.convertToObject(value);
        }*/
        return null;
    }
}
