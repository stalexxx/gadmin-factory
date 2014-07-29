package com.ifree.common.gwt.client.ui.grids;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.AbstractPager;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.Range;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Pagination;
import org.gwtbootstrap3.client.ui.constants.PaginationSize;

/**
 * Created by alex on 03.06.14.
 */
public class BasePager extends AbstractPager {

    private HasRows display;

    private final Pagination pagination;

    public BasePager() {
        pagination = new Pagination(PaginationSize.SMALL);
        initWidget(pagination);
    }



    @Override
    public void setDisplay(HasRows display) {
        this.display = display;
        super.setDisplay(display);
    }

    @Override
    protected void onRangeOrRowCountChanged() {
        rebuild(pagination);
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


    public void rebuild(final Pagination pagination) {
        pagination.clear();

        if (this.getPageCount() == 0) {
            return;
        }

        final AnchorListItem prev = pagination.addPreviousLink();
        prev.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                previousPage();
            }
        });
        prev.setEnabled(hasPreviousPage());

        for (int i = 0; i < getPageCount(); i++) {
            final int display = i + 1;
            final AnchorListItem page = new AnchorListItem(String.valueOf(display));
            page.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(final ClickEvent event) {
                    setPage(display - 1);
                }
            });

            if (i == getPage()) {
                page.setActive(true);
            }

            pagination.add(page);
        }

        final AnchorListItem next = pagination.addNextLink();
        next.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                nextPage();
            }
        });
        next.setEnabled(hasNextPage());
    }

    @Override
    public void firstPage() {
        super.firstPage();
    }
}
