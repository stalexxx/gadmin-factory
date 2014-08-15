/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.suggestions;

import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.ifree.common.gwt.client.utils.StringUtils;


/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 03.04.13
 */
public class EmphasizedSuggestion<T> implements Suggestion<T> {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private final T value;
    private final Renderer<T> renderer;
    private final RegExp pattern;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public EmphasizedSuggestion(T value, Renderer<T> renderer, RegExp pattern) {
        this.value = value;
        this.renderer = renderer;
        this.pattern = pattern;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public String getDisplayString() {
        return emphasize(renderer.render(value));
    }

    protected String emphasize(String value) {
        return pattern != null ? StringUtils.markFound(value, pattern) : value;
    }

    @Override
    public String getReplacementString() {
        return renderer.render(value);
    }

    /*===========================================[ GETTER/SETTER ]================*/

    public T getValue() {
        return value;
    }
}
