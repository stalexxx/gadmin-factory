package com.gafactory.core.client.ui.lists;

import com.gafactory.core.client.actions.Action;
import com.gafactory.core.client.gwtbootstrap3.ExtendedAnchorListItem;

/**
* Created by alex on 23.04.14.
*/
class ListItemActionBuilder<M> implements UIActionBuilder<M, ExtendedAnchorListItem> {
    @Override
    public ExtendedAnchorListItem build(final Action action) {
        return new ExtendedAnchorListItem();
    }

}
