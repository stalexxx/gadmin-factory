/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui;

import com.ifree.common.gwt.client.ui.application.Filter;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 24.09.13
 */
public class BaseFilter implements Filter {
    private String name;

    public BaseFilter() {
    }

    public BaseFilter(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
