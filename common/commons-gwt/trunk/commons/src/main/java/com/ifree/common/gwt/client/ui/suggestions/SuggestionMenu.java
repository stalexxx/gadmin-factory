package com.ifree.common.gwt.client.ui.suggestions;

import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.LinkedGroup;

import java.util.Iterator;

class SuggestionMenu extends LinkedGroup {

    public SuggestionMenu() {
      // Make sure that CSS styles specified for the default Menu classes
      // do not affect this menu
      setStyleName("");
    }

    public int getNumItems() {
      return getChildren().size();
    }

    /**
     * Returns the index of the menu item that is currently selected.
     *
     * @return returns the selected item
     */
    public int getSelectedItemIndex() {
        // The index of the currently selected item can only be
        // obtained if the menu is showing.
        Iterator<Widget> iterator = getChildren().iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Widget next = iterator.next();
            if (next instanceof LinkedGroupItem) {
                LinkedGroupItem item = (LinkedGroupItem) next;
                if (item.isActive()) {
                    return index;
                }
            }
            index += 1;

        }
        return -1;
    }

    /**
     * Selects the item at the specified index in the menu. Selecting the item
     * does not perform the item's associated action; it only changes the style
     * of the item and updates the value of SuggestionMenu.selectedItem.
     *
     * @param index index
     */
    public void selectItem(int index) {
        Iterator<Widget> iterator = getChildren().iterator();
        int i = 0;

        while (iterator.hasNext()) {
            Widget next = iterator.next();
            if (next instanceof SuggestionMenuItem) {
                SuggestionMenuItem item = (SuggestionMenuItem) next;
                item.setActive(index == i);
                i += 1;
            }
        }
    }

    public void addItem(SuggestionMenuItem menuItem) {
        add(menuItem);
    }

    public SuggestionMenuItem getSelectedItem() {
        for (Widget next : getChildren()) {
            if (next instanceof SuggestionMenuItem) {
                SuggestionMenuItem item = (SuggestionMenuItem) next;
                if (item.isActive()) {
                    return item;
                }
            }
        }

        return null;
    }
}