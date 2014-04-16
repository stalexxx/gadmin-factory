/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.grids;

import com.google.common.base.Function;
import com.google.gwt.cell.client.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.ifree.common.gwt.client.ui.constants.BaseTemplates;
import org.gwtbootstrap3.client.ui.CellTable;
import org.gwtbootstrap3.client.ui.constants.ElementTags;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Styles;

import javax.inject.Inject;
import java.util.Date;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 08.07.13
 */
public abstract class BaseListGrid<T> extends Composite implements SelectionChangeEvent.HasSelectionChangedHandlers, ProvidesKey<T> {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final int PAGE_SIZE = 1000000;

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    protected SingleSelectionModel<T> selectionModel;

    protected CellTable cellTable;
    private com.google.gwt.user.cellview.client.CellTable.Resources resources;
    //protected SimplePager pager;

    @Inject
    protected BaseTemplates templates;
    /*===========================================[ CONSTRUCTORS ]=================*/

    protected BaseListGrid(CellTable.Resources resources) {
        this.resources = resources;
    }

    protected static <T> void addTextEditColumn(CellTable<T> dataGrid,
                                                FieldUpdater<T, String> updater,
                                                final Function<T, String> valueGetter,
                                                String header,
                                                int width,
                                                boolean sortable,
                                                String dataStore) {
        Column<T, String> column = new Column<T, String>(new EditTextCell()) {

            @Override
            public String getValue(T object) {
                return valueGetter.apply(object);
            }
        };
        column.setFieldUpdater(updater);

        dataGrid.addColumn(column, header);
        if (width != 0) {
            dataGrid.setColumnWidth(column, width, Style.Unit.PX);
        }
        column.setSortable(sortable);
        column.setDataStoreName(dataStore);

    }


        /**
         * @param dataGrid
         * @param abstractCell
         * @param header
         * @param width
         * @param sortable
         * @param dataStore    used if sortable true. Means sorting column (in DB) na,e
         */
    protected static <T> void addIdentityColumn(CellTable<T> dataGrid, AbstractCell<T> abstractCell, String header, int width, boolean sortable, String dataStore) {
        Column<T, T> column = new IdentityColumn<T>(abstractCell);


        dataGrid.addColumn(column, header);
        dataGrid.setColumnWidth(column, width, Style.Unit.PX);
        column.setSortable(sortable);
        column.setDataStoreName(dataStore);
    }

    /**
     * @param dataGrid
     * @param column
     */
    protected static <T> void addTextColumn(CellTable<T> dataGrid, Column<T, String> column, String header, int width, boolean sortable, String dataStore) {
        addColumn(dataGrid, column, header, width, sortable, dataStore);
    }

    protected static <T> void addDateColumn(CellTable<T> dataGrid, Column<T, Date> column, String header, int width, boolean sortable, String dataStore) {
        addColumn(dataGrid, column, header, width, sortable, dataStore);
    }

    protected <T> void addBooleanColumn(CellTable<T> dataGrid, final Function<T, Boolean> column,
                                        final IconType yes, final IconType no, String header, int width, boolean sortable, String dataStore) {
        addSafeHtmlColumn(dataGrid, new AbstractSafeHtmlRenderer<T>() {
            @Override
            public SafeHtml render(T object) {
                Boolean b = column.apply(object);
                if (b != null) {
                    return templates.icon(Styles.FONT_AWESOME_BASE,
                            b ? yes.getCssName() : no.getCssName());

                } else {
                    return SafeHtmlUtils.EMPTY_SAFE_HTML;
                }
            }
        }, header, width, sortable, dataStore);
    }


    protected static <T> void addSafeHtmlColumn(CellTable<T> dataGrid, SafeHtmlRenderer<T> renderer, String header, int width, boolean sortable, String dataStore) {

        final IdentityColumn<T> column = new IdentityColumn<T>(new AbstractSafeHtmlCell<T>(renderer) {
            @Override
            protected void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
                sb.append(data);
            }
        });

        addColumn(dataGrid, column, header, width, sortable, dataStore);
    }


    protected static <T, C> void addColumn(CellTable<T> dataGrid, Column<T, C> column, String header, int width, boolean sortable, String dataStore) {
        dataGrid.addColumn(column, header);
        dataGrid.setColumnWidth(column, width, Style.Unit.PX);
        column.setSortable(sortable);
        column.setDataStoreName(dataStore);
    }


    protected void init() {

        cellTable = createDataGrid();

        FlowPanel panel = new FlowPanel();

        panel.add(cellTable);

        initWidget(panel);
    }

    /*===========================================[ CLASS METHODS ]================*/

    public HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler) {
        return cellTable.addColumnSortHandler(handler);
    }


    public T getSelection() {
        return selectionModel.getSelectedObject();
    }


    protected CellTable<T> createDataGrid() {
        if (resources != null) {
            cellTable = new CellTable<T>(pageSize(), resources, this);
        } else {
            cellTable = new CellTable<T>(pageSize(), this);
        }
        cellTable.setAutoHeaderRefreshDisabled(true);
        cellTable.setAutoFooterRefreshDisabled(true);

        cellTable.setStriped(false);
        cellTable.setBordered(true);
        cellTable.setCondensed(true);
//        cellTable.setHover(true);


        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        /*pager = new CustomPager(pagerResources);
        //pager = new NumberedPager();
        pager.addStyleName(Styles.PULL_LEFT);
        pager.setDisplay(cellTable);
*/
        selectionModel = new SingleSelectionModel<T>(this);

        cellTable.setSelectionModel(selectionModel);

        initColumns(cellTable);

        return cellTable;
    }

    protected int pageSize() {
        return PAGE_SIZE;
    }

    protected abstract void initColumns(CellTable<T> dataGrid);

    public HasData<T> getDisplay() {
        return cellTable;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public HandlerRegistration addSelectionChangeHandler(SelectionChangeEvent.Handler handler) {
        return selectionModel.addSelectionChangeHandler(handler);
    }


    public SimplePager getPager() {
        return null;
    }

    public int getGridBottom() {
        int absoluteTop = getAbsoluteTop();
        int offsetHeight = getOffsetHeight();
        int absBottom = absoluteTop + offsetHeight;
        return absBottom;
    }

    public boolean isSelected(T item) {
        return selectionModel.isSelected(item);
    }

    public void setSelection(T newSelection) {
        selectionModel.setSelected(newSelection, true);
    }


}
