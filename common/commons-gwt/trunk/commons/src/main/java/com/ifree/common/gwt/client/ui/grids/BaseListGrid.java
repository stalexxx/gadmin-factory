/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.grids;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
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
import com.ifree.common.gwt.client.ui.application.Filter;
import com.ifree.common.gwt.client.ui.constants.BaseTemplates;
import com.ifree.common.gwt.shared.ModelKeyProvider;
import com.ifree.common.gwt.shared.SortDir;
import com.ifree.common.gwt.shared.SortInfoBean;
import com.ifree.common.gwt.shared.ValueProvider;
import com.ifree.common.gwt.shared.loader.*;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Styles;

import javax.inject.Inject;
import java.util.Date;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 08.07.13
 */
@SuppressWarnings({"UnusedDeclaration", "SpringJavaAutowiringInspection"})
public abstract class BaseListGrid<T, _Filter extends Filter> extends Composite implements SelectionChangeEvent.HasSelectionChangedHandlers, ProvidesKey<T>, ColumnSortEvent.Handler{

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final int PAGE_SIZE_UNLIMIT = 1000000;
    public static final String EMPTY_STRING = "";

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    protected SingleSelectionModel<T> selectionModel;

    protected CellTable<T> dataGrid;
    protected ValueProvider<T, ?> secondSortingField;

    private com.google.gwt.user.cellview.client.CellTable.Resources resources;
    private final ModelKeyProvider<T> keyProvider;
    private final BaseDataProvider<T> dataProvider;
    private final FilterPagingLoader<T, _Filter> loader;

    private Integer pageSize;
    protected BasePager pager;

    @Inject
    protected EventBus eventBus;
    @Inject
    protected BaseTemplates templates;
    /*===========================================[ CONSTRUCTORS ]=================*/

    protected BaseListGrid(CellTable.Resources resources,
                           ModelKeyProvider<T> key,
                           BaseDataProxy<T> dataProxy,
                           Integer pageSize) {
        this.resources = resources;
        this.keyProvider = key;
        this.pageSize = pageSize;

        loader = new FilterPagingLoader<T, _Filter>(dataProxy);
        dataProvider = new BaseDataProvider<T>(loader, keyProvider);

        init();
    }


    protected BaseListGrid(CellTable.Resources resources,
                           ModelKeyProvider<T> key,
                           BaseDataProxy<T> dataProxy) {
        this(resources, key, dataProxy, null);
    }

    protected Column<T, String> addTextEditColumn(FieldUpdater<T, String> updater,
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


    protected Column<T, T> addIdentityColumn(AbstractCell<T> abstractCell, String header, int width, boolean sortable, String dataStore) {
        Column<T, T> column = new IdentityColumn<T>(abstractCell);


        dataGrid.addColumn(column, header);
        dataGrid.setColumnWidth(column, width, Style.Unit.PX);
        column.setSortable(sortable);
        column.setDataStoreName(dataStore);
        return column;

    }

    protected Column<T, String> addTextColumn(Column<T, String> column, String header, int width, boolean sortable, String dataStore) {
        return addColumn(column, header, width, sortable, dataStore);

    }

    protected Column<T, String> addTextColumn(final ValueProvider<T, String> valueProvider, String header, int width, boolean sortable) {
        return addColumn(new TextColumn<T>() {
            @Override
            public String getValue(T object) {
                return object != null ? valueProvider.getValue(object) : EMPTY_STRING;
            }
        }, header, width, sortable, valueProvider.getPath());
    }


    protected Column<T, String> addNumberColumn(final ValueProvider<T, ? extends Number> valueProvider, String header, int width, boolean sortable) {
        return addColumn(new TextColumn<T>() {
            @Override
            public String getValue(T object) {
                if (object != null) {
                    Number value = valueProvider.getValue(object);
                    if (value != null) {
                        return NumberFormat.getDecimalFormat().format(value);
                    }
                }
                return EMPTY_STRING;
            }
        }, header, width, sortable, sortable ? valueProvider.getPath() : null);
    }

    protected <V> Column<T, String> addRenderedColumn(final ValueProvider<T, V> valueProvider, final Renderer<V> renderer,
                                                      String header, int width, boolean sortable) {
        return addColumn(new TextColumn<T>() {
            @Override
            public String getValue(T object) {
                return object != null ? renderer.render(valueProvider.getValue(object)) : EMPTY_STRING;
            }
        }, header, width, sortable, valueProvider.getPath());
    }


    protected Column<T, String> addDateColumn(final ValueProvider<T, Date> provider, String header, int width, boolean sortable) {
        return addColumn(new TextColumn<T>() {
            @Override
            public String getValue(T object) {
                return object != null ? String.valueOf(provider.getValue(object)) : null;
            }
        }, header, width, sortable, sortable ? provider.getPath() : null);
    }

    protected Column<T, T> addBooleanColumn(final ValueProvider<T, Boolean> field,
                                            final IconType yes, final IconType no, String header, int width, boolean sortable) {
        return addSafeHtmlColumn(new AbstractSafeHtmlRenderer<T>() {
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
        }, header, width, sortable, sortable ? field.getPath() : null);
    }


    protected Column<T, T> addBooleanColumn(final ValueProvider<T, Boolean> field,
                                            String header, int width, boolean sortable) {

        return addBooleanColumn(field, IconType.CHECK, IconType.TIMES, header, width, sortable);
    }


    protected < R> Column<T, R> addSafeHtmlColumn(final ValueProvider<T, R> provider, SafeHtmlRenderer<R> renderer, String header, int width) {
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
        return addColumn(column, header, width);
    }

    protected Column<T, T> addSafeHtmlColumn(SafeHtmlRenderer<T> renderer, String header, int width, boolean sortable, String dataStore) {

        final IdentityColumn<T> column = new IdentityColumn<T>(new AbstractSafeHtmlCell<T>(renderer) {
            @Override
            protected void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
                if (data != null) {
                    sb.append(data);
                }
            }
        });

        return addColumn(column, header, width, sortable, dataStore);
    }


    protected <C> Column<T, C> addColumn(Column<T, C> column, String header, int width, boolean sortable, String dataStore) {
        dataGrid.addColumn(column, header);
        dataGrid.setColumnWidth(column, width, Style.Unit.PX);
        column.setSortable(sortable);
        column.setDataStoreName(dataStore);
        return column;

    }

    protected < C> Column<T, C> addColumn(Column<T, C> column, String header, int width) {
        return addColumn(column, header, width, false, null);
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


        if (pageSize() != PAGE_SIZE_UNLIMIT) {
            SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
            pager = new BasePager();
            pager.addStyleName(Styles.PULL_LEFT);
            pager.setDisplay(dataGrid);
        }

        selectionModel = new SingleSelectionModel<T>(this);

        dataGrid.setSelectionModel(selectionModel);
        dataGrid.addColumnSortHandler(this);
        dataProvider.addDataDisplay(dataGrid);
        loader.addLoadHandler(dataProvider);

        return dataGrid;
    }

    protected int pageSize() {
        return pageSize != null ? pageSize : PAGE_SIZE_UNLIMIT;
    }


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

        loader.addSortInfo(new SortInfoBean(column.getDataStoreName(), column.isDefaultSortAscending() ? SortDir.ASC :SortDir.DESC));
    }


    public ColumnSortList getColumnSortList() {
        return dataGrid.getColumnSortList();
    }

    @Override
    public void onColumnSort(ColumnSortEvent event) {
        loader.clearSortInfo();
        ColumnSortList sorting = event.getColumnSortList();
        for (int i = 0; i < sorting.size(); i++) {
            ColumnSortList.ColumnSortInfo columnSortInfo = sorting.get(i);
            String field = columnSortInfo.getColumn().getDataStoreName();

            Preconditions.checkNotNull(field);

            SortInfoBean sortInfoBean = new SortInfoBean(field, columnSortInfo.isAscending() ? SortDir.ASC : SortDir.DESC);
            loader.addSortInfo(sortInfoBean);

        }

        if (secondSortingField != null) {
            loader.addSortInfo(new SortInfoBean(secondSortingField, SortDir.ASC));
        }

        loader.load();
    }

    public void reload() {
        loader.load();
    }

    public BaseDataProvider<T> getDataPrivider() {
        return dataProvider;
    }

    public void setFilter(_Filter filter) {
        loader.setFilter(filter);
    }

    public void setFilterConfigBuilder(BaseFilterConfigBuilder<_Filter> configBuilder) {
        loader.setConfigBuilder(configBuilder);
    }

    public void setSecondSortingField(ValueProvider<T,? > secondSortingField) {
        this.secondSortingField = secondSortingField;
    }

    public HandlerRegistration addLoadHandler(LoadHandler handler) {
        return loader.addLoadHandler(handler);
    }
}
