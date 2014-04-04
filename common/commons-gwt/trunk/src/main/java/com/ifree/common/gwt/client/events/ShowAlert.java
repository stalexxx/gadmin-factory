/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.events;

import com.gwtplatform.dispatch.annotation.GenEvent;
import com.gwtplatform.dispatch.annotation.Order;
import org.gwtbootstrap3.client.ui.constants.AlertType;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 20.03.14
 */
@GenEvent
public class ShowAlert {
    @Order(0)
    String message;

    @Order(1)
    AlertType messageType;
}
