/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.ifree.common.gwt.shared.loader;

import java.util.List;

/**
 * A {@link PagingLoadConfig} with support for filters.
 */
public interface FilterPagingLoadConfig extends PagingLoadConfig {

  /**
   * Returns the list of filters for this load config.
   * 
   * @return the list of filters
   */
  List<FilterConfig> getFilters();

  /**
   * Sets the list of filters for this load config.
   * 
   * @param filters the list of filters
   */
  void setFilters(List<FilterConfig> filters);
}
