/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.gafactory.core.client.ui.grids;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gwt.cell.client.*;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.*;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gafactory.core.client.data.IntegerProperty;
import com.gafactory.core.client.data.StorageService;
import com.gafactory.core.client.ui.application.Filter;
import com.gafactory.core.client.ui.constants.BaseNameTokes;
import com.gafactory.core.client.ui.constants.BaseTemplates;
import com.gafactory.core.shared.ModelKeyProvider;
import com.gafactory.core.shared.SortDir;
import com.gafactory.core.shared.SortInfoBean;
import com.gafactory.core.shared.ValueProvider;
import com.gafactory.core.shared.loader.FilterPagingLoader;
import com.gafactory.core.shared.loader.LoadHandler;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.ValueListBox;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Styles;
import org.gwtbootstrap3.client.ui.gwt.CellTable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 08.07.13
 */
@SuppressWarnings({"UnusedDeclaration", "SpringJavaAutowiringInspection"})
public abstract class BaseListGrid<T, _Filter extends Filter> extends Composite implements
        SelectionChangeEvent.HasSelectionChangedHandlers, ProvidesKey<T>, ColumnSortEvent.Handler {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final int PAGE_SIZE_UNLIMIT = 1000000;
    public static final String EMPTY_STRING = "";
    public static final int PSIZE_15 = 5;
    public static final int PSIZE_25 = 25;
    public static final int PSIZE_50 = 50;
    public static final int PSIZE_100 = 100;
    public static final int PSIZE_200 = 200;
    public static final Integer PSIZE_ALL = PAGE_SIZE_UNLIMIT;
    public static final IntegerProperty PAGE_SIZE = new IntegerProperty("pageSize");
    public static final String TABLE_LINK_SELECTED = "table-link-selected";

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    protected SingleSelectionModel<T> selectionModel;

    protected CellTable<T> dataGrid;
    protected ValueProvider<T, ?> secondSortingField;

    private com.google.gwt.user.cellview.client.CellTable.Resources resources;
    private final ModelKeyProvider<T> keyProvider;
    private final BaseDataProvider<T> dataProvider;
    private final FilterPagingLoader<T, _Filter> loader;


    private int defaultPageSize;

    protected BasePager pager;
    protected ValueListBox<Integer> itemsPerPage;

    @Inject
    protected EventBus eventBus;
    @Inject
    protected BaseTemplates templates;

    @Inject
    protected StorageService storageService;

    /*===========================================[ CONSTRUCTORS ]=================*/

    protected BaseListGrid(CellTable.Resources resources,
                           ModelKeyProvider<T> key,
                           BaseDataProxy<T> dataProxy,
                           //   StorageService storageService,
                           Integer defaultPageSize) {
        this.resources = resources;
        this.keyProvider = key;
        this.defaultPageSize = defaultPageSize;

        loader = new FilterPagingLoader<T, _Filter>(dataProxy);
        dataProvider = new BaseDataProvider<T>(loader, keyProvider);

        init();
    }


    protected BaseListGrid(CellTable.Resources resources,
                           ModelKeyProvider<T> key,
                           BaseDataProxy<T> dataProxy) {
        this(resources, key, dataProxy, PSIZE_ALL);
    }


    @PostConstruct
    public void postConstruct() {
        int pageSize = pageSize();
        if (itemsPerPage != null) {
            itemsPerPage.setValue(pageSize);
            dataGrid.setPageSize(pageSize);
        }

    }

    private String getViewName() {
        return getClass().getSimpleName();
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


    protected Column<T, SafeHtml> addTrustedHtmlColumn(final ValueProvider<T, String> valueProvider, String header, int width, boolean sortable) {
        return addColumn(new Column<T, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(T object) {
                return object != null ? SafeHtmlUtils.fromTrustedString(valueProvider.getValue(object)) : SafeHtmlUtils.EMPTY_SAFE_HTML;
            }
        }, header, width, sortable, valueProvider.getPath());
    }


    protected Column<T, String> addFormattedNumberColumn(final ValueProvider<T, ? extends Number> valueProvider, String header, int width, boolean sortable) {
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

    protected Column<T, String> addNumberColumn(final ValueProvider<T, ? extends Number> valueProvider, String header, int width, boolean sortable) {
        return addColumn(new TextColumn<T>() {
            @Override
            public String getValue(T object) {
                if (object != null) {
                    Number value = valueProvider.getValue(object);
                    if (value != null) {
//                        return NUMBER_FORMAT.format(value);
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


    private TextColumn<T> dateColumn(final ValueProvider<T, Date> provider, final DateTimeFormat.PredefinedFormat predefinedFormat) {
        return dateColumn(provider, DateTimeFormat.getFormat(predefinedFormat));
    }

    private TextColumn<T> dateColumn(final ValueProvider<T, Date> provider, final DateTimeFormat format) {
        return new TextColumn<T>() {
            @Override
            public String getValue(T object) {
                if (object != null) {

                    Date date = provider.getValue(object);
                    if (date != null) {
                        return format.format(date);
                    }
                }
                return null;
            }
        };
    }

    protected Column<T, String> addDateColumn(final ValueProvider<T, Date> provider, String header, int width, boolean sortable) {
        return addDateColumn(provider, DateTimeFormat.PredefinedFormat.DATE_SHORT, header, width, sortable);

    }

    protected Column<T, String> addDateColumn(final ValueProvider<T, Date> provider, DateTimeFormat.PredefinedFormat format, String header, int width, boolean sortable) {
        return addColumn(dateColumn(provider, format),
                header, width, sortable, sortable ? provider.getPath() : null);
    }

    protected Column<T, String> addDateColumn(final ValueProvider<T, Date> provider, DateTimeFormat format, String header, int width, boolean sortable) {
        return addColumn(dateColumn(provider, format),
                header, width, sortable, sortable ? provider.getPath() : null);
    }

    protected Column<T, String> addDateTimeColumn(final ValueProvider<T, Date> provider, String header, int width, boolean sortable) {
        return addDateColumn(provider, DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT, header, width, sortable);
    }

    protected Column<T, T> addBooleanColumn(final ValueProvider<T, Boolean> field,
                                            final IconType yes, final IconType no, String header, int width, boolean sortable) {
        return addSafeHtmlColumn(new AbstractSafeHtmlRenderer<T>() {
            @Override
            public SafeHtml render(T object) {
                Boolean b = field.getValue(object);
                if (b != null) {
                    boolean selected = isSelected(object);
                    String color;

                    if (selected) {
                        color = TABLE_LINK_SELECTED;
                    } else {
                        color = b ? "icon-yes" : "icon-no";
                    }
                    return templates.icon(Styles.FONT_AWESOME_BASE,
                            b ? yes.getCssName() : no.getCssName(), color);

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


    protected <R> Column<T, R> addSafeHtmlColumn(final ValueProvider<T, R> provider, SafeHtmlRenderer<R> renderer, String header, int width, boolean sortable) {
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
        return addColumn(column, header, width, sortable, sortable ? provider.getPath() : null);
    }

    protected <R> Column<T, R> addSafeHtmlColumn(final ValueProvider<T, R> provider, SafeHtmlRenderer<R> renderer, String header, int width) {
        return addSafeHtmlColumn(provider, renderer, header, width, false);
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

    protected <C> Column<T, C> addColumn(Column<T, C> column, String header, int width) {
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
            dataGrid = new CellTable<T>(pageSize(), resources, this, null);
        } else {
            dataGrid = new CellTable<T>(this);
        }
        dataGrid.setAutoHeaderRefreshDisabled(true);
        dataGrid.setAutoFooterRefreshDisabled(true);

        dataGrid.setStriped(false);
        dataGrid.setBordered(true);
        dataGrid.setCondensed(true);

        dataGrid.setEmptyTableWidget(createEmptyWidget());
        dataGrid.setLoadingIndicator(createLoadingWidget());


        //     if (pageSize() != PAGE_SIZE_UNLIMIT) {
        pager = new BasePager();
        pager.addStyleName(Styles.PULL_LEFT);
        pager.setDisplay(dataGrid);

        itemsPerPage = new ValueListBox<Integer>(new AbstractRenderer<Integer>() {
            @Override
            public String render(Integer object) {
                if (object != null) {
                    if (!Objects.equals(object, PSIZE_ALL)) {
                        return String.valueOf(object.toString());
                    } else {
                        return "Все";
                    }
                }
                return "null";
            }
        });

        itemsPerPage.setValue(defaultPageSize);
        itemsPerPage.setAcceptableValues(Lists.newArrayList(PSIZE_15, PSIZE_25, PSIZE_50, PSIZE_100, PSIZE_200, PSIZE_ALL));
        itemsPerPage.addValueChangeHandler(new ValueChangeHandler<Integer>() {
            @Override
            public void onValueChange(ValueChangeEvent<Integer> event) {
                Integer value = event.getValue();
                if (value != null) {
                    dataGrid.setVisibleRange(new Range(0, value));
                }
                storageService.putValue(PAGE_SIZE, value, getViewName());

            }
        });

        selectionModel = new SingleSelectionModel<T>(this);

        dataGrid.setSelectionModel(selectionModel);
        dataGrid.addColumnSortHandler(this);
        dataProvider.addDataDisplay(dataGrid);
        loader.addLoadHandler(dataProvider);

        return dataGrid;
    }

    private Widget createLoadingWidget() {
        Icon icon = new Icon(IconType.SPINNER);
        icon.setSpin(true);
        icon.setSize(IconSize.TIMES5);
        return icon;
    }

    private Widget createEmptyWidget() {
        return new org.gwtbootstrap3.client.ui.Label("Тут ничего нет");
    }

    protected int pageSize() {

        if (storageService != null) {
            Integer loadedPageSize = storageService.getValue(PAGE_SIZE, getViewName());
            return loadedPageSize != null ? loadedPageSize : defaultPageSize;
        } else {
            return defaultPageSize;
        }
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


    public void setDefaultSortingColumn(Column<?, ?> column, boolean isAsc) {
        column.setDefaultSortAscending(isAsc);

        ColumnSortList columnSortList = dataGrid.getColumnSortList();
        columnSortList.push(column);
        ColumnSortEvent.fire(this, columnSortList);

        loader.addSortInfo(new SortInfoBean(column.getDataStoreName(), column.isDefaultSortAscending() ? SortDir.ASC : SortDir.DESC));
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

    public Widget getPageSizeWidget() {
        return itemsPerPage;
    }

    public BaseDataProvider<T> getDataPrivider() {
        return dataProvider;
    }

    public void setFilter(_Filter filter) {
        if (pager != null) {
            pager.setPageStart(0);
        }
        loader.setFilter(filter);
    }

    public void setFilterConfigBuilder(BaseFilterConfigBuilder<_Filter> configBuilder) {
        loader.setConfigBuilder(configBuilder);
    }

    public void setSecondSortingField(ValueProvider<T, ?> secondSortingField) {
        this.secondSortingField = secondSortingField;
    }

    public void scrollToSelected() {

        Scheduler.get().scheduleDeferred(new Command() {
            @Override
            public void execute() {
                int visibleSelectedIndex = getVisibleSelectedIndex();
                if (visibleSelectedIndex != -1) {
                    TableRowElement rowElement = dataGrid.getRowElement(visibleSelectedIndex);
                    rowElement.scrollIntoView();
                    rowElement.focus();
                }

                dataGrid.setFocus(true);
            }
        });


    }

    private int getVisibleSelectedIndex() {
        int visibleItemCount = dataGrid.getVisibleItemCount();
        for (int i = 0; i < visibleItemCount; i++) {
            T visibleItem = dataGrid.getVisibleItem(i);
            if (selectionModel.isSelected(visibleItem)) {
                return i;
            }

        }
        return -1;
    }

    public HandlerRegistration addLoadHandler(LoadHandler handler) {
        return loader.addLoadHandler(handler);
    }


    protected class LinkRenderer<V, ID extends Serializable> extends AbstractSafeHtmlRenderer<T> {
        private final PlaceManager placeManager;
        private final Renderer<V> renderer;
        private final ValueProvider<T, V> provider;
        private final ValueProvider<V, ID> idProvider;
        private final String nameToken;

        public LinkRenderer(Renderer<V> renderer, PlaceManager placeManager, ValueProvider<T, V> provider, ValueProvider<V, ID> idProvider, String nameToken) {
            this.renderer = renderer;
            this.placeManager = placeManager;
            this.provider = provider;
            this.idProvider = idProvider;
            this.nameToken = nameToken;
        }

        @Override
        public SafeHtml render(T object) {

            boolean isSelected = selectionModel.isSelected(object);

            V value = provider.getValue(object);
            if (value != null) {
                PlaceRequest pr = new PlaceRequest.Builder().nameToken(nameToken).with(BaseNameTokes.ID_PARAM, String.valueOf(idProvider.getValue(value))).build();
                String token = placeManager.buildHistoryToken(pr);


                String rendered = renderer.render(value);
                if (rendered == null) {
                    rendered = "???";
                }
                return templates.historyTokenHref(token, rendered, isSelected ? TABLE_LINK_SELECTED : "");
            }

            return SafeHtmlUtils.EMPTY_SAFE_HTML;
        }
    }


    public HandlerRegistration addRowCountChangeHandler(RowCountChangeEvent.Handler handler) {
        return dataGrid.addRowCountChangeHandler(handler);
    }
}
