package com.gafactory.core.client.events;

import com.gwtplatform.dispatch.annotation.GenEvent;
import com.gwtplatform.dispatch.annotation.Order;
import com.gafactory.core.client.ui.BaseFilter;

/**
 * Created by alex on 03.04.14.
 */
@GenEvent
public class PerformFilter {
    @Order(0)
    BaseFilter filter;

}
