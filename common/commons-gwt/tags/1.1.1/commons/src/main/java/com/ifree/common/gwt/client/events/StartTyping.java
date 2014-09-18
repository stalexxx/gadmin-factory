package com.ifree.common.gwt.client.events;

import com.gwtplatform.dispatch.annotation.GenEvent;
import com.gwtplatform.dispatch.annotation.Order;

/**
 * Created by alex on 05.06.14.
 */
@GenEvent
public class StartTyping {

    @Order(0)
    Character symbol;

}
