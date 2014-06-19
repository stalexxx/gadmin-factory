package com.ifree.common.gwt.client.ui.suggestions;

import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * Created by alex on 19.06.14.
 */
public interface Suggestion<T> extends SuggestOracle.Suggestion {
    T getValue();
}
