package com.ifree.common.gwt.client.ui.grids;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.ifree.common.gwt.shared.loader.*;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Created by alex on 27.06.14.
 */
public class BaseDataProvider<T> extends AsyncDataProvider<T> implements LoadHandler<FilterPagingLoadConfig,PagingLoadResult<T>> {

    private PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>> loader;

    protected ProvidesKey<T> providesKey;

    private List<T> currentData;

    public BaseDataProvider(PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>> loader, ProvidesKey<T> providesKey) {
        this.loader = loader;
        this.providesKey = providesKey;
    }

    public void updateModel(T model) {
        if (currentData != null) {
            int i = currentData.indexOf(model);
            if (i != -1) {
                updateRowData(i, Collections.singletonList(model));
            }
        }

    }

    @Nullable
    public T findModel(Object key) {
        if (currentData != null) {
            for (T t : currentData) {
                if (providesKey.getKey(t).equals(key)) {
                    return t;
                }
            }
        }

        return null;
    }

    @Override
    protected void onRangeChanged(HasData display) {
        Range visibleRange = display.getVisibleRange();
        loader.load(visibleRange.getStart(), visibleRange.getLength());
    }

    @Override
    public void onLoad(LoadEvent<FilterPagingLoadConfig, PagingLoadResult<T>> event) {
        PagingLoadResult<T> result = event.getLoadResult();


        if (result != null) {
            currentData = result.getData();

            updateRowCount(result.getTotalLength(), true);
            updateRowData(result.getOffset(), currentData);

        }
    }
}
