package com.ifree.common.gwt.client.ui.widgets;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;

import java.util.Collection;
import java.util.List;

/**
 * Created by alex on 18.06.14.
 */
public class SuggestionDisplayImpl extends SuggestBox.SuggestionDisplay implements HasAnimation{


    private final SuggestionMenu suggestionMenu;
    private final PopupPanel suggestionPopup;
    private final List<HandlerRegistration> handlerRegistrations = Lists.newArrayList();

    /**
     * We need to keep track of the last {@link SuggestBox} because it acts as
     * an autoHide partner for the {@link PopupPanel}. If we use the same
     * display for multiple {@link SuggestBox}, we need to switch the autoHide
     * partner.
     */
    private SuggestBox lastSuggestBox = null;

    /**
     * Sub-classes making use of {@link } to add
     * elements to the suggestion popup _may_ want those elements to show even
     * when there are 0 suggestions. An example would be showing a "No
     * matches" message.
     */
    private boolean hideWhenEmpty = true;

    /**
     * Object to position the suggestion display next to, instead of the
     * associated suggest box.
     */
    private UIObject positionRelativeTo;

    /**
     * Construct a new {@link DefaultSuggestionDisplay}.
     */
    public SuggestionDisplayImpl() {
        suggestionMenu = new SuggestionMenu();
        suggestionPopup = createPopup();
        suggestionPopup.setWidget(decorateSuggestionList(suggestionMenu));
    }

    public void hideSuggestions() {
        suggestionPopup.hide();
    }

    public boolean isAnimationEnabled() {
        return suggestionPopup.isAnimationEnabled();
    }

    /**
     * Check whether or not the suggestion list is hidden when there are no
     * suggestions to display.
     *
     * @return true if hidden when empty, false if not
     */
    public boolean isSuggestionListHiddenWhenEmpty() {
        return hideWhenEmpty;
    }

    /**
     * Check whether or not the list of suggestions is being shown.
     *
     * @return true if the suggestions are visible, false if not
     */
    public boolean isSuggestionListShowing() {
        return suggestionPopup.isShowing();
    }

    public void setAnimationEnabled(boolean enable) {
        suggestionPopup.setAnimationEnabled(enable);
    }

    /**
     * Sets the style name of the suggestion popup.
     *
     * @param style the new primary style name
     * @see UIObject#setStyleName(String)
     */
    public void setPopupStyleName(String style) {
        suggestionPopup.setStyleName(style);
    }

    /**
     * Sets the UI object where the suggestion display should appear next to.
     *
     * @param uiObject the uiObject used for positioning, or null to position
     *     relative to the suggest box
     */
    public void setPositionRelativeTo(UIObject uiObject) {
        positionRelativeTo = uiObject;
    }

    /**
     * Set whether or not the suggestion list should be hidden when there are
     * no suggestions to display. Defaults to true.
     *
     * @param hideWhenEmpty true to hide when empty, false not to
     */
    public void setSuggestionListHiddenWhenEmpty(boolean hideWhenEmpty) {
        this.hideWhenEmpty = hideWhenEmpty;
    }

    /**
     * Create the PopupPanel that will hold the list of suggestions.
     *
     * @return the popup panel
     */
    protected PopupPanel createPopup() {
        PopupPanel p = new DecoratedPopupPanel(true, false);
        p.setStyleName("gwt-SuggestBoxPopup");
        p.setPreviewingAllNativeEvents(true);
        //p.setAnimationType(PopupPanel.AnimationType.ROLL_DOWN);
        return p;
    }

    /**
     * Wrap the list of suggestions before adding it to the popup. You can
     * override this method if you want to wrap the suggestion list in a
     * decorator.
     *
     * @param suggestionList the widget that contains the list of suggestions
     * @return the suggestList, optionally inside of a wrapper
     */
    protected Widget decorateSuggestionList(Widget suggestionList) {
        return suggestionList;
    }

    @Override
    protected SuggestOracle.Suggestion getCurrentSelection() {
        if (!isSuggestionListShowing()) {
            return null;
        }
        SuggestionMenuItem item = suggestionMenu.getSelectedItem();
        return item == null ? null : item.getSuggestion();
    }

    /**
     * Get the {@link PopupPanel} used to display suggestions.
     *
     * @return the popup panel
     */
    protected PopupPanel getPopupPanel() {
        return suggestionPopup;
    }

    @Override
    protected void moveSelectionDown() {
        // Make sure that the menu is actually showing. These keystrokes
        // are only relevant when choosing a suggestion.
        if (isSuggestionListShowing()) {
            // If nothing is selected, getSelectedItemIndex will return -1 and we
            // will select index 0 (the first item) by default.
            suggestionMenu.selectItem(suggestionMenu.getSelectedItemIndex() + 1);
        }
    }

    @Override
    protected void moveSelectionUp() {
        // Make sure that the menu is actually showing. These keystrokes
        // are only relevant when choosing a suggestion.
        if (isSuggestionListShowing()) {
            // if nothing is selected, then we should select the last suggestion by
            // default. This is because, in some cases, the suggestions menu will
            // appear above the text box rather than below it (for example, if the
            // text box is at the bottom of the window and the suggestions will not
            // fit below the text box). In this case, users would expect to be able
            // to use the up arrow to navigate to the suggestions.
            if (suggestionMenu.getSelectedItemIndex() == -1) {
                suggestionMenu.selectItem(suggestionMenu.getNumItems() - 1);
            } else {
                suggestionMenu.selectItem(suggestionMenu.getSelectedItemIndex() - 1);
            }
        }
    }

    /**
     * <b>Affected Elements:</b>
     * <ul>
     * <li>-popup = The popup that appears with suggestions.</li>
     * <li>-item# = The suggested item at the specified index.</li>
     * </ul>
     *
     * @see UIObject#onEnsureDebugId(String)
     */
    @Override
    protected void onEnsureDebugId(String baseID) {
        suggestionPopup.ensureDebugId(baseID + "-popup");
        // todo suggestionMenu.setMenuItemDebugIds(baseID);
    }

    @Override
    protected void showSuggestions(SuggestBox suggestBox, Collection<? extends SuggestOracle.Suggestion> suggestions, boolean isDisplayStringHTML, boolean isAutoSelectEnabled, final SuggestBox.SuggestionCallback callback) {




        // Hide the popup if there are no suggestions to display.
        boolean anySuggestions = (suggestions != null && suggestions.size() > 0);
        if (!anySuggestions && hideWhenEmpty) {
            hideSuggestions();
            return;
        }

        // Hide the popup before we manipulate the menu within it. If we do not
        // do this, some browsers will redraw the popup as items are removed
        // and added to the menu.
        if (suggestionPopup.isAttached()) {
            suggestionPopup.hide();
        }

        suggestionMenu.clear();
        for (HandlerRegistration handlerRegistration : handlerRegistrations) {
            handlerRegistration.removeHandler();
        }
        handlerRegistrations.clear();


        for (final SuggestOracle.Suggestion curSuggestion : suggestions) {
            final SuggestionMenuItem menuItem = new SuggestionMenuItem(
                    curSuggestion, isDisplayStringHTML);
            handlerRegistrations.add(menuItem.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    callback.onSuggestionSelected(curSuggestion);

                }
            }));


            suggestionMenu.addItem(menuItem);
        }

        if (isAutoSelectEnabled && anySuggestions) {
            // Select the first item in the suggestion menu.
            suggestionMenu.selectItem(0);
        }

        // Link the popup autoHide to the TextBox.
        if (lastSuggestBox != suggestBox) {
            // If the suggest box has changed, free the old one first.
            if (lastSuggestBox != null) {
                suggestionPopup.removeAutoHidePartner(lastSuggestBox.getElement());
            }
            lastSuggestBox = suggestBox;
            suggestionPopup.addAutoHidePartner(suggestBox.getElement());
        }

        // Show the popup under the TextBox.
        suggestionPopup.showRelativeTo(positionRelativeTo != null
                ? positionRelativeTo : suggestBox);
    }



}
