package com.ifree.common.gwt.client.ui.grids;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.Range;

/**
 * Created by alex on 03.06.14.
 */
public class Pager extends SimplePager {

    private HasRows display;

    public Pager() {
    }

    public Pager(TextLocation textLocation) {
        super(textLocation);
    }

    @Override
    public void setDisplay(HasRows display) {
        this.display = display;
        super.setDisplay(display);
    }

    @Override
    public void setPageStart(int index) {
        if (display != null) {
            Range range = display.getVisibleRange();
            int pageSize = range.getLength();

            index = Math.max(0, index);
            if (index != range.getStart()) {
                display.setVisibleRange(index, pageSize);
            }
        }
    }
}
