package com.ifree.common.gwt.client.ui.lists;

import com.ifree.common.gwt.client.actions.Action;
import com.ifree.common.gwt.client.gwtbootstrap3.ExtendedAnchorListItem;

/**
* Created by alex on 23.04.14.
*/
class ListItemActionBuilder<M> implements UIActionBuilder<M, ExtendedAnchorListItem> {
    @Override
    public ExtendedAnchorListItem build(final Action action) {
        return new ExtendedAnchorListItem();
    }

}
