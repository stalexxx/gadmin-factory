package com.gafactory.core.client.ui.fields;

import com.gafactory.core.shared.ValueProvider;

/**
 * Created by alex on 21.04.14.
 */
public interface BaseField<T, V> extends ValueProvider<T, V> {

    boolean isDefaultField();


}
