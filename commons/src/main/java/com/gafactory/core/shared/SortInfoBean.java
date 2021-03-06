/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.gafactory.core.shared;


import java.io.Serializable;

/**
 * Aggregates sort field and sort direction.
 */
public final class SortInfoBean implements Serializable {

  private String sortField;
  private SortDir sortDir = SortDir.ASC;

  /**
   * Creates a new sort field instance.
   */
  public SortInfoBean() {

  }

  /**
   * Creates a new sort info instance.
   * 
   * @param field the sort field
   * @param sortDir the sort direction
   */
  public SortInfoBean(String field, SortDir sortDir) {
    this.sortField = field;
    this.sortDir = sortDir;
  }

  /**
   * Creates a new sort info instance.
   * 
   * @param valueProvider the value provider for the sort field
   * @param sortDir the sort direction
   */
  public SortInfoBean(ValueProvider<?, ?> valueProvider, SortDir sortDir) {
    this.sortField = valueProvider.getPath();
    this.sortDir = sortDir;
  }

  /**
   * Returns the sort field.
   * 
   * @return the sort field
   */
  public String getSortField() {
    return sortField;
  }

  /**
   * Sets the sort field.
   * 
   * @param sortField the sort field
   */
  public void setSortField(String sortField) {
    this.sortField = sortField;
  }

  /**
   * Returns the sort direction.
   * 
   * @return the sort direction
   */
  public SortDir getSortDir() {
    return sortDir;
  }

  /**
   * Sets the sort direction.
   * 
   * @param sortDir the sort direction
   */
  public void setSortDir(SortDir sortDir) {
    this.sortDir = sortDir;
  }

}
