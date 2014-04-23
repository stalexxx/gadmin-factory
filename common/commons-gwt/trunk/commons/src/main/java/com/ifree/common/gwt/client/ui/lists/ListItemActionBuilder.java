package com.ifree.common.gwt.client.ui.lists;

import com.google.gwt.user.client.ui.IsWidget;
import org.gwtbootstrap3.client.ui.ListItem;

/**
* Created by alex on 23.04.14.
*/
class ListItemActionBuilder<M> implements UIActionBuilder<M, ListItem> {
    @Override
    public ListItem build(final Action action) {

        ListItem listItem = new ListItem(action.getDisplayText());
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
