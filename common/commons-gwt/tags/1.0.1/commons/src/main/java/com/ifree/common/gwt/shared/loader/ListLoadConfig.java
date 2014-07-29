/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.ifree.common.gwt.shared.loader;

import com.ifree.common.gwt.shared.SortInfoBean;

import java.io.Serializable;
import java.util.List;

/**
 * Load config interface for list based data. Adds support for sort information.
 */
public interface ListLoadConfig extends Serializable {


  /**
   * Returns the sort info.
   */
  List<SortInfoBean> getSortInfo();

  /**
   * Sets the sort info.
   */
  void setSortInfo(List<SortInfoBean> info);
}
