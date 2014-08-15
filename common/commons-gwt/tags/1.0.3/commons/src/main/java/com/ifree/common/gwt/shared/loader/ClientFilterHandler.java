/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.ifree.common.gwt.shared.loader;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Abstract base class for all filter handlers. It provides methods to convert
 * filter values between native and string representations.
 * 
 * @param <T> the type of the filter value
 */
public abstract class ClientFilterHandler<T> extends FilterHandler<T> {

  /**
   * Converts a filter value in string representation to native representation.
   * 
   * @param value the string representation of the filter value
   * @return the native representation of the filter value
   */
  public abstract void convertToObject(String value, AsyncCallback<T> callback);

  /**
   * Converts a filter value in native representation to string representation.
   * 
   * @param object the native representation of the filter value
   * @return the string representation of the filter value
   */
  public abstract String convertToString(T object);

    @Override
    public T convertToObject(String value) {
        return null;
    }
}
