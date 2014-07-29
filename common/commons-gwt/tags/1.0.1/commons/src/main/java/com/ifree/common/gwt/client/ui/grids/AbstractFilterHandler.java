package com.ifree.common.gwt.client.ui.grids;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.ifree.common.gwt.client.ui.BaseFilter;
import com.ifree.common.gwt.shared.ValueProvider;
import com.ifree.common.gwt.shared.loader.FilterConfigBean;
import com.ifree.common.gwt.shared.loader.FilterHandler;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by alex on 22.04.14.
 */
public abstract class AbstractFilterHandler<T extends BaseFilter> extends FilterHandler<T> implements BaseFilterConfigBuilder.CustomFilterAppender<T> {


    public static final Function<FilterConfigBean, String> CONFIG_BEAN_STRING_FUNCTION = new FilterConfigBeanStringFunction();


    public static final Predicate<String> NOT_NULL = new Predicate<String>() {
        @Override
        public boolean apply(@Nullable String input) {
            return input != null;
        }

    };
    protected final ValueProvider<T, ?>[] providers;

    @Inject
    protected BaseFilterHelper filterHelper;

    public AbstractFilterHandler(ValueProvider<T, ?> ... providers) {
        this.providers = providers;
    }

    @Override
    public T convertToObject(String str) {

        T result = createObj();



        Iterable<String> split = Splitter.on(";").split(str);

        for (String s : split) {

            if (!s.isEmpty()) {

                FilterConfigBean configBean = parse(s);

                ValueProvider provider = findProvider(configBean.getField());

                provider.setValue(result, filterHelper.getValue(configBean.getType(), configBean.getValue()));
            }



        }

        return result;
    }

    private FilterConfigBean parse(String s) {
        String[] keyvalue = s.split("=");

        FilterConfigBean bean = new FilterConfigBean();

        String[] fieldType = keyvalue[0].split("/");

        if (fieldType.length > 1 && keyvalue.length > 1) {

            bean.setField(fieldType[0]);
            bean.setType(fieldType[1]);
            bean.setValue(keyvalue[1]);

        } else {
            int i = 0;
        }


        return bean;
    }

    protected ValueProvider<T,?> findProvider(String field){
        for (ValueProvider<T, ?> tValueProvider : providers) {

            if (tValueProvider.getPath().equals(field)) {
                return tValueProvider;
            }
        }
        return null;
    }

    protected abstract T createObj() ;

    @Override
    public String convertToString(T object) {

        List<FilterConfigBean> beanList = Lists.newArrayList();
        for (ValueProvider<T, ?> provider : providers) {
            Object value = provider.getValue(object);
            if (value != null) {
                beanList.add(filterHelper.createConfig(provider, value));
            }
        }

        List<String> transform = Lists.transform(beanList, CONFIG_BEAN_STRING_FUNCTION);
        return Joiner.on(";").join(Collections2.filter(transform, NOT_NULL));
    }

    public BaseFilterConfigBuilder<T> createConfigBuilder() {
        return new BaseFilterConfigBuilder<T>(this);
    }


    private static class FilterConfigBeanStringFunction implements Function<FilterConfigBean, String> {
        @Nullable
        @Override
        public String apply(@Nullable FilterConfigBean input) {
            return input != null && input.getValue() != null ? input.getField() + "/" + input.getType() + "=" + input.getValue() : null;
        }
    }

    @Override
    public final void addCustomFields(T filter, List<FilterConfigBean> filterConfigs) {
        for (ValueProvider<T, ?> provider : providers) {
            filterHelper.appendTo(filterConfigs, provider, provider.getValue(filter));
        }
    }


}
