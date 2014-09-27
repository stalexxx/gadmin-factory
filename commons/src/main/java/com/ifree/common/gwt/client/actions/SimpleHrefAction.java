package com.ifree.common.gwt.client.actions;

import org.gwtbootstrap3.client.ui.constants.IconType;

import javax.annotation.Nullable;

/**
 * Created by alex on 08.08.14.
 */
public class SimpleHrefAction<T> extends SimpleAction<T> {


    private final boolean openBlank;
    private final String link;

    public SimpleHrefAction(String text, String link, boolean openBlank) {
        this(text, null, link, openBlank);
    }
    public SimpleHrefAction(String text, IconType iconType, String link, boolean openBlank) {
        super(text);
        this.openBlank = openBlank;
        this.link = link;
        setIconType(iconType);
    }

    @Override
    public final ACTION_TYPE getType() {
        return openBlank ? ACTION_TYPE.LINK_BLANK : ACTION_TYPE.LINK;
    }

    @Override
    public final String actualHistoryTokenOrLink(@Nullable T item) {
        return link;
    }

    @Override
    public final void perform(@Nullable T item) {

    }
}
