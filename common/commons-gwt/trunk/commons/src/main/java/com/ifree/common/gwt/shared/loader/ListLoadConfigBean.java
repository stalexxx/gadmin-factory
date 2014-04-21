/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.ifree.common.gwt.shared.loader;

import com.ifree.common.gwt.shared.SortInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Default <code>ListLoadConfig</code> implementation.
 * 
 * @see ListLoadConfig
 */
@SuppressWarnings("serial")
public class ListLoadConfigBean implements ListLoadConfig {

  private List<SortInfoBean> sortInfo = new ArrayList<SortInfoBean>();

  /**
   * Create a new load config instance.
   */
  public ListLoadConfigBean() {

  }

  /**
   * Create a new load config instance.
   */
  public ListLoadConfigBean(SortInfoBean info) {
    getSortInfo().add(info);
  }

  /**
   * Creates a new load config instance.
   * 
   * @param info the sort information
   */
  public ListLoadConfigBean(List<SortInfoBean> info) {
    setSortInfo(info);
  }

  @Override
  public List<SortInfoBean> getSortInfo() {
    return sortInfo;
  }

  @Override
  public void setSortInfo(List<SortInfoBean> info) {
    sortInfo.clear();
    for (SortInfoBean i : info) {
      if (i instanceof SortInfoBean) {
        sortInfo.add((SortInfoBean) i);
      } else {
        sortInfo.add(new SortInfoBean(i.getSortField(), i.getSortDir()));
      }
    }
  }


}
