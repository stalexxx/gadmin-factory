package com.ifree.common.gwt.client.ui.lists;

import com.google.gwt.user.client.ui.IsWidget;
import com.ifree.common.gwt.client.actions.Action;

/**
 * Created by alex on 23.04.14.
 */
public interface UIActionBuilder<M, I extends IsWidget> {

    I build(Action<M> action);
}
