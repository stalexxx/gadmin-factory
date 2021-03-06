/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.gafactory.core.client.ui.lists;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.gafactory.core.client.actions.Action;
import com.gafactory.core.client.events.PerformFilterEvent;
import com.gafactory.core.client.gwtbootstrap3.ExtendedAnchorListItem;
import com.gafactory.core.client.ui.BaseFilter;
import com.gafactory.core.client.ui.BaseToolbar;
import com.gafactory.core.client.ui.grids.BaseListGrid;
import com.gafactory.core.client.ui.grids.BasePager;
import com.gafactory.core.shared.loader.LoadHandler;
import org.gwtbootstrap3.client.ui.constants.IconType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

import static com.gafactory.core.client.actions.Action.ACTION_TYPE.*;

/**
 * @author Alexander Ostrovskiy (alex)
 * @since 03.09.13
 */
@SuppressWarnings("GWTStyleCheck")
public abstract class BaseListView<
        T,
        _Filter extends BaseFilter,
        _Handler extends ListUiHandler<T, _Filter>
        >
        extends ViewWithUiHandlers<_Handler> implements ListView<T, _Filter> {

    protected final BaseListGrid<T, _Filter> dataGrid;
    protected final BaseFilterPanel<_Filter, ? extends BaseFilterPanel> filterPanel;
    protected final BaseToolbar toolbar;

    private UIActionBuilder<T, ExtendedAnchorListItem> actionBuilder = new ListItemActionBuilder<T>();
    private Map<Action<T>, ExtendedAnchorListItem> actionMap = Maps.newHashMap();

    protected BaseListView(BaseListGrid<T, _Filter> dataGrid) {
        this(dataGrid, null);
    }

    protected BaseListView(@Nonnull BaseListGrid<T, _Filter> grid,
                           @Nullable BaseFilterPanel<_Filter, ? extends BaseFilterPanel> filterPanel) {
        Preconditions.checkNotNull(grid);

        dataGrid = grid;
        dataGrid.addStyleName("gridAfterFixedBar");
        this.filterPanel = filterPanel;


        toolbar = new BaseToolbar();
        toolbar.setExtendSearch(false);
        if (filterPanel != null) {
            toolbar.hideSearch();
        }


        toolbar.addPerformFilterHandler(new PerformFilterEvent.PerformFilterHandler() {
            @Override
            public void onPerformFilter(PerformFilterEvent event) {
                getUiHandlers().onPerformFilter(event);
            }
        });

        BaseViewPanel<T, _Filter> viewPanel =
                new BaseViewPanel<T, _Filter>(dataGrid, toolbar, filterPanel);

        viewPanel.addPager(dataGrid.getPager());
        viewPanel.addPageSizeWidget(dataGrid.getPageSizeWidget());

        dataGrid.addRowCountChangeHandler(new RowCountChangeEvent.Handler() {
            @Override
            public void onRowCountChange(RowCountChangeEvent event) {
                int newRowCount = event.getNewRowCount();
                toolbar.setRowCount(newRowCount);

            }
        });


        initWidget(viewPanel);
    }


    @Override
    public void setUiHandlers(_Handler uiHandlers) {
        super.setUiHandlers(uiHandlers);
        if (filterPanel != null) {
            this.filterPanel.setUiHandlers(getUiHandlers());
        }

    }

    @Override
    public T getSelectedObject() {
        return dataGrid.getSelection();
    }

    @Override
    public boolean isSelected(T item) {
        return dataGrid.isSelected(item);
    }


    @Override
    public void setupRoles(List<String> roles) {

    }

    @Override
    public Object getKey(T item) {
        return dataGrid.getKey(item);
    }

    @Override
    public void setSelection(T newSelection) {
        dataGrid.setSelection(newSelection);
    }

    @Override
    public final HandlerRegistration addSelectionChangeHandler(SelectionChangeEvent.Handler handler) {
        return dataGrid.addSelectionChangeHandler(handler);
    }

    @Override
    public HandlerRegistration addLoadHandler(LoadHandler handler) {
        return dataGrid.addLoadHandler(handler);
    }

    @Override
    public void updateToolbar() {
        final int offsetHeight = toolbar.getOffsetHeight();
        dataGrid.getElement().getStyle().setTop(offsetHeight, Style.Unit.PX);
    }

    @Override
    public void firstPage() {
        BasePager pager = dataGrid.getPager();
        if (pager != null) {
            pager.firstPage();
        }
    }

    @Override
    public void updateHeader(String displayHeader) {
        toolbar.setHeader(displayHeader);
    }

    @Override
    public void setFilter(_Filter filter) {
        TakesValue<_Filter> filterPeer = filterPanel!= null ? filterPanel : getBaseFilterPeer();

        if (filterPeer != null) {
            filterPeer.setValue(filter);
        }

        getGrid().setFilter(filter);
    }

    @Override
    public void clearFilter() {
        setFilter(null);
    }

    private TakesValue<_Filter> getBaseFilterPeer() {
        return new TakesValue<_Filter>() {
            @Override
            public void setValue(_Filter value) {
                toolbar.setSearchFieldText(value != null ? value.getName() : null);
            }

            @Override
            public _Filter getValue() {
                return null;
            }
        };
    }

    @Override
    public <W extends HasClickHandlers> W addAction(final Action<T> action) {
        final ExtendedAnchorListItem actionWidget = actionBuilder.build(action);
        actionMap.put(action, actionWidget);
        toolbar.addAction(action, actionWidget);

        return (W) actionWidget;
    }

    @Override
    public void updateAction(Action<T> action, boolean enabled, boolean visible, String displayText, IconType displayIcon) {
        ExtendedAnchorListItem listItem = actionMap.get(action);
        if (listItem != null) {
            listItem.setEnabled(enabled);
            listItem.setText(displayText);
            listItem.setVisible(visible);
            listItem.setIcon(displayIcon);

            if (visible && enabled) {
                Action.ACTION_TYPE type = action.getType();
                if (HISTORY_TOKEN.equals(type)) {
                    listItem.setTargetHistoryToken(action.actualHistoryTokenOrLink(getSelectedObject()));
                }

                if (LINK.equals(type) || type.equals(LINK_BLANK)) {
                    listItem.setHref(action.actualHistoryTokenOrLink(getSelectedObject()));
                }

                if (LINK_BLANK.equals(type)) {
                    listItem.setTarget("_blank");
                }
            }


        }
    }

    @Override
    public void focusFilter(Character symbol) {
        toolbar.focusSearchBySymbol(symbol);
    }

    @Override
    public ColumnSortList getColumnSortList() {
        return dataGrid.getColumnSortList();
    }

    @Override
    public BaseListGrid<T, _Filter> getGrid() {
        return dataGrid;
    }

    @Override
    public void postConstruct() {
        dataGrid.postConstruct();
    }

    @Override
    public void scrollToSelected() {
        dataGrid.scrollToSelected();
    }
}
