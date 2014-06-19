package com.ifree.common.gwt.client.ui.suggestions;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
* Created by alex on 13.05.14.
*/
public class ValuedSuggestion<T> implements Suggestion<T>, IsSerializable {
    private T value;
    private String displayString;
    private String replacementString;

    /**
     * Constructor used by RPC.
     */
    public ValuedSuggestion() {
    }

    /**
     *
     * @param replacementString the string to enter into the SuggestBox's text
     *          box if the suggestion is chosen
     * @param displayString the display string
     */
    public ValuedSuggestion(T value, String replacementString, String displayString) {
        this.value = value;
        this.replacementString = replacementString;
        this.displayString = displayString;
    }

    public String getDisplayString() {
        return displayString;
    }

    public String getReplacementString() {
        return replacementString;
    }

    public T getValue() {
        return value;
    }
}
