/**
 */
package com.ifree.common.gwt.shared.loader;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * A {@link FilterHandler} that provides support for <code>Date</code> values.
 */
public class StringListFilterHandler extends FilterHandler<List<String>> {

    public static final String DELIM = ",";


    @Override
    public List<String> convertToObject(String value) {
        Iterable<String> split = Splitter.on(DELIM).split(value);
        return Lists.newArrayList(split);
    }

    @Override
    public String convertToString(List<String> object) {
        return Joiner.on(DELIM).join(object);
    }
}
