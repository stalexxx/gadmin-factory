package com.ifree.common.gwt.client.ui.lists;

import com.ifree.common.gwt.client.actions.Action;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.ListItem;

/**
* Created by alex on 23.04.14.
*/
class ListItemActionBuilder<M> implements UIActionBuilder<M, AnchorListItem> {
    @Override
    public AnchorListItem build(final Action action) {

        AnchorListItem listItem = new AnchorListItem();
        /*Scheduler.get().scheduleDeferred();
        listItem.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                action.perform(getSelecte);
            }
        });*/
        return listItem;
    }

}
