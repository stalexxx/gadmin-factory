package com.ifree.common.gwt.client.ui.grids;

import com.google.common.collect.Maps;
import com.google.gwt.text.shared.Parser;
import com.ifree.common.gwt.shared.ValueProvider;
import com.ifree.common.gwt.shared.loader.*;
import com.ifree.common.gwt.shared.types.DateInterval;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 18.04.14.
 */
public abstract class BaseFilterHelper {

    public static final String BOOLEAN_TYPE = "boolean";
    public static final String STRING_TYPE = "string";
    public static final String FLOAT_TYPE = "float";
    public static final String INTEGER_TYPE = "integer";
    public static final String LONG_TYPE = "long";
    public static final String DATE_INTERVAL_TYPE = "dateint";
    public static final String STRING_LIST_TYPE = "string_list_type";

    private static BooleanFilterHandler booleanFilterHandler;

    private static FilterHandler<String> stringFilterHandler;

    private static NumberFilterHandler<Float> floatNumberFilterHandler;
    private static NumberFilterHandler<Integer> integerFilterHandler;
    private static NumberFilterHandler<Long> longFilterHandler;

    private Map<String, FilterHandler> handlerMap = Maps.newHashMap();
    private Map<Class, String> typeMap = Maps.newHashMap();

    private static DateIntervalFilterHandler dateIntervalFilterHandler;
    public static StringListFilterHandler stringListFilterHandler;

    static {
        booleanFilterHandler = new BooleanFilterHandler();
        stringFilterHandler = new FilterHandler<String>() {
            @Override
            public String convertToObject(String value) {
                return value == null || value.isEmpty() ? null : value;
            }

            @Override
            public String convertToString(String object) {
                return object == null || object.isEmpty() ? null : object;
            }
        };
        floatNumberFilterHandler = new NumberFilterHandler<Float>(new Parser<Float>() {
            @Override
            public Float parse(CharSequence text) throws ParseException {
                return Float.parseFloat(text.toString());
            }
        });
        integerFilterHandler = new NumberFilterHandler<Integer>(new Parser<Integer>() {
            @Override
            public Integer parse(CharSequence text) throws ParseException {
                return Integer.parseInt(text.toString());
            }
        });
        longFilterHandler = new NumberFilterHandler<>(new Parser<Long>() {
            @Override
            public Long parse(CharSequence text) throws ParseException {
                return Long.parseLong(text.toString());
            }
        });
        dateIntervalFilterHandler = new DateIntervalFilterHandler();
        stringListFilterHandler = new StringListFilterHandler();
    }

    protected BaseFilterHelper()
    {
        handlerMap.put(STRING_TYPE, stringFilterHandler);
        handlerMap.put(BOOLEAN_TYPE, booleanFilterHandler);
        handlerMap.put(FLOAT_TYPE, floatNumberFilterHandler);
        handlerMap.put(INTEGER_TYPE, integerFilterHandler);
        handlerMap.put(LONG_TYPE, longFilterHandler);
        handlerMap.put(DATE_INTERVAL_TYPE, dateIntervalFilterHandler);
        handlerMap.put(STRING_LIST_TYPE, stringListFilterHandler);

        typeMap.put(String.class, STRING_TYPE);
        typeMap.put(Boolean.class, BOOLEAN_TYPE);
        typeMap.put(Float.class, FLOAT_TYPE);
        typeMap.put(Integer.class, INTEGER_TYPE);
        typeMap.put(Long.class, LONG_TYPE);
        typeMap.put(DateInterval.class, DATE_INTERVAL_TYPE);
        typeMap.put(List.class, STRING_LIST_TYPE);

        registerCustom();
    }

    protected abstract void registerCustom();

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
        return null;
    }
}
