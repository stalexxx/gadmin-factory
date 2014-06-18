package com.ifree.common.gwt.client.ui.widgets;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SuggestOracle;
import org.gwtbootstrap3.client.ui.LinkedGroupItem;

/**
 * Created by alex on 18.06.14.
 */
public class SuggestionMenuItem extends LinkedGroupItem {

    private static final String STYLENAME_DEFAULT = "item";

    private SuggestOracle.Suggestion suggestion;

    public SuggestionMenuItem(SuggestOracle.Suggestion suggestion, boolean asHTML) {
        super();

        setText(suggestion.getDisplayString());
        // Each suggestion should be placed in a single row in the suggestion
        // menu. If the window is resized and the suggestion cannot fit on a
        // single row, it should be clipped (instead of wrapping around and
        // taking up a second row).
    //    getElement().getStyle().setProperty("whiteSpace", "nowrap");
       // setStyleName(STYLENAME_DEFAULT);
        setSuggestion(suggestion);
    }

    public SuggestOracle.Suggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(SuggestOracle.Suggestion suggestion) {
        this.suggestion = suggestion;
    }

    public void setScheduledCommand(final Scheduler.ScheduledCommand scheduledCommand) {
        addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                scheduledCommand.execute();

            }
        });
    }
}
