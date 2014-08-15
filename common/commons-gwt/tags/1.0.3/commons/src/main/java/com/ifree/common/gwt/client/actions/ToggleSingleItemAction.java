package com.ifree.common.gwt.client.actions;

import javax.annotation.Nullable;

/**
 * Created by alex on 25.04.14.
 */
public abstract class ToggleSingleItemAction<T> extends SingleItemAction<T> {
    protected String falseText;

    public ToggleSingleItemAction( String trueText, String falseText) {
        super(trueText, false);
        this.falseText = falseText;
    }

    public ToggleSingleItemAction( String trueText, String falseText, boolean additional) {
        super(trueText, additional);
        this.falseText = falseText;
    }



    @Override
    public boolean isVisible(@Nullable T item) {
        return item != null;
    }


    @Override
    public String getDisplayText(T item) {
        return isToggled(item) ? text : falseText;
    }

    protected abstract boolean isToggled(T item);


}
