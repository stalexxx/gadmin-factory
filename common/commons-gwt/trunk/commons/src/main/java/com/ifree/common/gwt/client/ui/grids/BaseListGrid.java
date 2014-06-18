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
import com.google.gwt.i18n.client.NumberFormat;
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
import com.google.gwt.view.client.*;
import com.google.web.bindery.event.shared.EventBus;
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
    public static final String EMPTY_STRING = "";

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    protected SingleSelectionModel<T> selectionModel;

    protected CellTable<T> dataGrid;
    private com.google.gwt.user.cellview.client.CellTable.Resources resources;
    private ModelKeyProvider<T> keyProvider;
    private Integer pageSize;
    protected BasePager pager;

    @Inject
    protected EventBus eventBus;
    @Inject
    protected BaseTemplates templates;
    /*===========================================[ CONSTRUCTORS ]=================*/

    protected BaseListGrid(CellTable.Resources resources, ModelKeyProvider<T> key, Integer pageSize) {
        this.resources = resources;
        keyProvider = key;
        this.pageSize = pageSize;

        init();
    }


    protected BaseListGrid(CellTable.Resources resources, ModelKeyProvider<T> key) {
        this(resources, key, null);
    }

    protected Column<T, String> addTextEditColumn(CellTable<T> dataGrid,
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

        return column;

    }


    protected Column<T, T> addIdentityColumn(CellTable<T> dataGrid, AbstractCell<T> abstractCell, String header, int width, boolean sortable, String dataStore) {
        Column<T, T> column = new IdentityColumn<T>(abstractCell);


        dataGrid.addColumn(column, header);
        dataGrid.setColumnWidth(column, width, Style.Unit.PX);
        column.setSortable(sortable);
        column.setDataStoreName(dataStore);
        return column;

    }

    protected Column<T, String> addTextColumn(CellTable<T> dataGrid, Column<T, String> column, String header, int width, boolean sortable, String dataStore) {
        return addColumn(dataGrid, column, header, width, sortable, dataStore);

    }

    protected Column<T, String> addTextColumn(CellTable<T> dataGrid, final ValueProvider<T, String> valueProvider, String header, int width, boolean sortable) {
        return addColumn(dataGrid, new TextColumn<T>() {
            @Override
            public String getValue(T object) {
                return object != null ? valueProvider.getValue(object) : EMPTY_STRING;
            }
        }, header, width, sortable, valueProvider.getPath());
    }


    protected Column<T, String> addNumberColumn(CellTable<T> dataGrid, final ValueProvider<T, ? extends Number> valueProvider, String header, int width, boolean sortable) {
        return addColumn(dataGrid, new TextColumn<T>() {
            @Override
            public String getValue(T object) {
                return object != null ? NumberFormat.getDecimalFormat().format(valueProvider.getValue(object)) : EMPTY_STRING;
            }
        }, header, width, sortable, valueProvider.getPath());
    }

    protected <V> Column<T, String> addRenderedColumn(CellTable<T> dataGrid, final ValueProvider<T, V> valueProvider, final Renderer<V> renderer,
                                         String header, int width, boolean sortable) {
        return addColumn(dataGrid, new TextColumn<T>() {
            @Override
            public String getValue(T object) {
                return object != null ? renderer.render(valueProvider.getValue(object)) : EMPTY_STRING;
            }
        }, header, width, sortable, valueProvider.getPath());
    }


    protected Column<T, String> addDateColumn(CellTable<T> dataGrid, final ValueProvider<T, Date> provider, String header, int width, boolean sortable) {
        return addColumn(dataGrid, new TextColumn<T>() {
            @Override
            public String getValue(T object) {
                return object != null ? String.valueOf(provider.getValue(object)) : null;
            }
        }, header, width, sortable, provider.getPath());
    }

    protected Column<T, T> addBooleanColumn(CellTable<T> dataGrid, final ValueProvider<T, Boolean> field,
                                    final IconType yes, final IconType no, String header, int width, boolean sortable) {
        return addSafeHtmlColumn(dataGrid, new AbstractSafeHtmlRenderer<T>() {
            @Override
            public SafeHtml render(T object) {
                Boolean b = field.getValue(object);
                if (b != null) {
                    return templates.icon(Styles.FONT_AWESOME_BASE,
                            b ? yes.getCssName() : no.getCssName(), b ? "icon-yes" : "icon-no");

                } else {
                    return SafeHtmlUtils.EMPTY_SAFE_HTML;
                }
            }
        }, header, width, sortable, field.getPath());
    }


    protected Column<T, T> addBooleanColumn(CellTable<T> dataGrid, final ValueProvider<T, Boolean> field,
                                    String header, int width, boolean sortable) {

        return addBooleanColumn(dataGrid, field, IconType.CHECK,IconType.TIMES, header, width, sortable);
    }


    protected static <T, R> Column<T, R> addSafeHtmlColumn(final CellTable<T> dataGrid, final ValueProvider<T, R> provider, SafeHtmlRenderer<R> renderer, String header, int width) {
        final Column<T, R> column = new Column<T, R>(new AbstractSafeHtmlCell<R>(renderer) {
            @Override
            protected void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
                if (data != null) {
                    sb.append(data);
                }
            }
        }) {
            @Override
            public R getValue(T object) {
                return object != null ? provider.getValue(object) : null;
            }

        };
        return addColumn(dataGrid, column, header, width);
    }


    protected static <T> Column<T, T> addSafeHtmlColumn(final CellTable<T> dataGrid, SafeHtmlRenderer<T> renderer, String header, int width, boolean sortable, String dataStore) {

        final IdentityColumn<T> column = new IdentityColumn<T>(new AbstractSafeHtmlCell<T>(renderer) {
            @Override
            protected void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
                if (data != null) {
                    sb.append(data);
                }
            }
        });

        return addColumn(dataGrid, column, header, width, sortable, dataStore);
    }


    protected static <T, C> Column<T, C> addColumn(CellTable<T> dataGrid, Column<T, C> column, String header, int width, boolean sortable, String dataStore) {
        dataGrid.addColumn(column, header);
        dataGrid.setColumnWidth(column, width, Style.Unit.PX);
        column.setSortable(sortable);
        column.setDataStoreName(dataStore);
        return column;

    }

    protected static <T, C> Column<T, C> addColumn(CellTable<T> dataGrid, Column<T, C> column, String header, int width) {
        return addColumn(dataGrid, column, header, width, false, null);
    }


    private void init() {

        dataGrid = createDataGrid();

        FlowPanel panel = new FlowPanel();

        panel.add(dataGrid);

        initWidget(panel);
    }

    /*===========================================[ CLASS METHODS ]================*/

    public HandlerRegistration addColumnSortHandler(ColumnSortEvent.Handler handler) {
        return dataGrid.addColumnSortHandler(handler);
    }


    public T getSelection() {
        return selectionModel.getSelectedObject();
    }


    protected CellTable<T> createDataGrid() {
        if (resources != null) {
            dataGrid = new CellTable<T>(pageSize(), resources, this, new Label());
        } else {
            dataGrid = new CellTable<T>(pageSize(), this);
        }
        dataGrid.setAutoHeaderRefreshDisabled(true);
        dataGrid.setAutoFooterRefreshDisabled(true);

        dataGrid.setStriped(false);
        dataGrid.setBordered(true);
        dataGrid.setCondensed(true);
//        dataGrid.setHover(true);


        if (pageSize() != PAGE_SIZE_UNLIMIT) {
            SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
            pager = new BasePager();
            //pager = new NumberedPager();
            pager.addStyleName(Styles.PULL_LEFT);
            pager.setDisplay(dataGrid);
        }


        selectionModel = new SingleSelectionModel<T>(this);

        dataGrid.setSelectionModel(selectionModel);

        initColumns(dataGrid);

        return dataGrid;
    }

    protected int pageSize() {
        return pageSize != null ? pageSize : PAGE_SIZE_UNLIMIT;
    }

    protected final void initColumns(CellTable<T> dataGrid){}

    public HasData<T> getDisplay() {
        return dataGrid;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public HandlerRegistration addSelectionChangeHandler(SelectionChangeEvent.Handler handler) {
        return selectionModel.addSelectionChangeHandler(handler);
    }


    public BasePager getPager() {
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

    public void setDefaultSortingColumn(Column<?, ?> column) {
        ColumnSortList columnSortList = dataGrid.getColumnSortList();
        columnSortList.push(column);
        ColumnSortEvent.fire(this, columnSortList);

    }


    public ColumnSortList getColumnSortList() {
        return dataGrid.getColumnSortList();
    }
}
