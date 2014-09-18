/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.application;

import java.util.logging.Logger;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 13.05.13
 */
public interface UIBlocker {

    /*===========================================[ INTERFACE METHODS ]============*/

    void block();

    void unblock();

    Logger getLogger();
}
