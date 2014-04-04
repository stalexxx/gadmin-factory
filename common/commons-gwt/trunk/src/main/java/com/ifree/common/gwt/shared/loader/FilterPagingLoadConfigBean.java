/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.ifree.common.gwt.shared.loader;

import com.ifree.common.gwt.shared.SortInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link PagingLoadConfigBean} with support for filters.
 */
public class FilterPagingLoadConfigBean extends PagingLoadConfigBean implements FilterPagingLoadConfig {

  private List<FilterConfig> filterConfigs = new ArrayList<FilterConfig>();
  
  @Override
  public List<FilterConfig> getFilters() {
    return filterConfigs;
  }

  @Override
  public void setFilters(List<FilterConfig> filters) {
    this.filterConfigs = filters;
  }

    @Override
    public void setSortInfo(List<? extends SortInfo> info) {

    }
}
