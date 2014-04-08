/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.ifree.common.gwt.shared.loader;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Aggregating handler interface for:
 * 
 * <dl>
 * <dd>{@link BeforeLoadEvent}</b></dd>
 * <dd>{@link LoadExceptionEvent}</b></dd>
 * <dd>{@link LoadEvent}</b></dd>
 * </dl>
 * 
 * @param <C> the type of config to request the data
 * @param <M> the type of data to be loaded
 */
public interface LoaderHandler<C, M> extends BeforeLoadEvent.BeforeLoadHandler<C>, LoadExceptionEvent.LoadExceptionHandler<C>, LoadHandler<C, M> {

  /**
   * A loader that implements this interface is a public source of all
   * {@link Loader} events, {@link BeforeLoadEvent}, {@link LoadEvent}, and
   * {@link LoadExceptionEvent}.
   * 
   * @param <C> the type of config to request the data
   * @param <M> the type of data to be loaded
   */
  public interface HasLoaderHandlers<C, M> extends HasLoadHandlers<C, M>, LoadExceptionEvent.HasLoadExceptionHandlers<C>,
          BeforeLoadEvent.HasBeforeLoadHandlers<C> {

    /**
     * Adds a {@link LoadEvent} handler.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addLoaderHandler(LoaderHandler<C, M> handler);
  }
}
