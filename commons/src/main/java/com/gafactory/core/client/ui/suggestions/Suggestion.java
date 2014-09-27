package com.gafactory.core.client.ui.suggestions;

import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * Created by alex on 19.06.14.
 */
public interface Suggestion<T> extends SuggestOracle.Suggestion {
    T getValue();
}
