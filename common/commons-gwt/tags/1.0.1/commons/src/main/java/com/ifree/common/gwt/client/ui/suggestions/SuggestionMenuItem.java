package com.ifree.common.gwt.client.ui.suggestions;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * Created by alex on 18.06.14.
 */
public class SuggestionMenuItem extends LinkedGroupItem {

    private static final String STYLENAME_DEFAULT = "item";

    private SuggestOracle.Suggestion suggestion;

    public SuggestionMenuItem(SuggestOracle.Suggestion suggestion, boolean asHTML) {
        super();

        setHTML(suggestion.getDisplayString());

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
