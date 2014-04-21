package com.ifree.common.gwt.client.ui.fields;

import com.ifree.common.gwt.shared.ValueProvider;

/**
 * Created by alex on 21.04.14.
 */
public interface BaseField<T, V> extends ValueProvider<T, V> {

    boolean isDefaultField();


}
