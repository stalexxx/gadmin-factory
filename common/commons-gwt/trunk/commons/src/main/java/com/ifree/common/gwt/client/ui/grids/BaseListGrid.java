/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.grids;

import com.google.common.base.Function;
import com.google.gwt.cell.client.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.ifree.common.gwt.client.ui.constants.BaseTemplates;
import com.ifree.common.gwt.shared.ModelKeyProvider;
import com.ifree.common.gwt.shared.ValueProvider;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Styles;

import javax.inject.Inject;
import java.util.Date;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 08.07.13
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class BaseListGrid<T> extends Composite implements SelectionChangeEvent.HasSelectionChangedHandlers, ProvidesKey<T> {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final int PAGE_SIZE_UNLIMIT = 1000000;

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    protected SingleSelectionModel<T> selectionModel;

    protected CellTable<T> cellTable;
    private com.google.gwt.user.cellview.client.CellTable.Resources resources;
    private ModelKeyProvider<T> keyProvider;
    private Integer pageSize;
    protected SimplePager pager;

    @Inject
    protected BaseTemplates templates;
    /*===========================================[ CONSTRUCTORS ]=================*/

    protected BaseListGrid(CellTable.Resources resources, ModelKeyProvider<T> key, Integer pageSize) {
        this.resources = resources;
        keyProvider = key;
        this.pageSize = pageSize;
    }


    protected BaseListGrid(CellTable.Resources resources, ModelKeyProvider<T> key) {
        this(resources, key, null);
    }

    protected void addTextEditColumn(CellTable<T> dataGrid,
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


    protected void addIdentityColumn(CellTable<T> dataGrid, AbstractCell<T> abstractCell, String header, int width, boolean sortable, String dataStore) {
        Column<T, T> column = new IdentityColumn<T>(abstractCell);


        dataGrid.addColumn(column, header);
        dataGrid.setColumnWidth(column, width, Style.Unit.PX);
        column.setSortable(sortable);
        column.setDataStoreName(dataStore);
    }

    protected void addTextColumn(CellTable<T> dataGrid, Column<T, String> column, String header, int width, boolean sortable, String dataStore) {
        addColumn(dataGrid, column, header, width, sortable, dataStore);
    }

    protected void addTextColumn(CellTable<T> dataGrid, final ValueProvider<T, String> valueProvider, String header, int width, boolean sortable) {
        addColumn(dataGrid, new TextColumn<T>() {
            @Override
            public String getValue(T object) {
                return valueProvider.getValue(object);
            }
        }, header, width, sortable, valueProvider.getPath());
    }


    protected void addIntegerColumn(CellTable<T> dataGrid, final ValueProvider<T, ? extends Number> valueProvider, String header, int width, boolean sortable) {
        addColumn(dataGrid, new TextColumn<T>() {
            @Override
            public String getValue(T object) {
                return String.valueOf(valueProvider.getValue(object));
            }
        }, header, width, sortable, valueProvider.getPath());
    }

    protected <V> void addRenderedColumn(CellTable<T> dataGrid, final ValueProvider<T, V> valueProvider, final Renderer<V> renderer,
                                         String header, int width, boolean sortable) {
        addColumn(dataGrid, new TextColumn<T>() {
            @Override
            public String getValue(T object) {
                return renderer.render(valueProvider.getValue(object));
            }
        }, header, width, sortable, valueProvider.getPath());
    }


    protected void addDateColumn(CellTable<T> dataGrid, final ValueProvider<T, Date> provider, String header, int width, boolean sortable) {
        addColumn(dataGrid, new TextColumn<T>() {
            @Override
            public String getValue(T object) {
                return String.valueOf(provider.getValue(object));
            }
        }, header, width, sortable, provider.getPath());
    }

    protected void addBooleanColumn(CellTable<T> dataGrid, final ValueProvider<T, Boolean> field,
                                    final IconType yes, final IconType no, String header, int width, boolean sortable) {
        addSafeHtmlColumn(dataGrid, new AbstractSafeHtmlRenderer<T>() {
            @Override
            public SafeHtml render(T object) {
                Boolean b = field.getValue(object);
                if (b != null) {
                    return templates.icon(Styles.FONT_AWESOME_BASE,
                            b ? yes.getCssName() : no.getCssName(), b ? "green" : "red");

                } else {
                    return SafeHtmlUtils.EMPTY_SAFE_HTML;
                }
            }
        }, header, width, sortable, field.getPath());
    }


    protected void addBooleanColumn(CellTable<T> dataGrid, final ValueProvider<T, Boolean> field,
                                    String header, int width, boolean sortable) {

        addBooleanColumn(dataGrid, field, IconType.CHECK,IconType.TIMES, header, width, sortable);
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
            cellTable = new CellTable<T>(pageSize(), resources, this, new Label());
        } else {
            cellTable = new CellTable<T>(pageSize(), this);
        }
        cellTable.setAutoHeaderRefreshDisabled(true);
        cellTable.setAutoFooterRefreshDisabled(true);

        cellTable.setStriped(false);
        cellTable.setBordered(true);
        cellTable.setCondensed(true);
//        cellTable.setHover(true);


        if (pageSize() != PAGE_SIZE_UNLIMIT) {
            SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
            pager = new SimplePager(SimplePager.TextLocation.CENTER);
            //pager = new NumberedPager();
            pager.addStyleName(Styles.PULL_LEFT);
            pager.setDisplay(cellTable);
        }


        selectionModel = new SingleSelectionModel<T>(this);

        cellTable.setSelectionModel(selectionModel);

        initColumns(cellTable);

        return cellTable;
    }

    protected int pageSize() {
        return pageSize != null ? pageSize : PAGE_SIZE_UNLIMIT;
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
        return pager;
    }

    public boolean isSelected(T item) {
        return selectionModel.isSelected(item);
    }

    public void setSelection(T newSelection) {
        selectionModel.setSelected(newSelection, true);
    }

    @Override
    public Object getKey(T item) {
        return keyProvider.getKey(item);
    }


}
