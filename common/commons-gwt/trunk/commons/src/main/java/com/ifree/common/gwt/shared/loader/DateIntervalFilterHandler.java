/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.ifree.common.gwt.shared.loader;

import com.ifree.common.gwt.shared.types.DateInterval;

import java.util.Date;

/**
 * A {@link com.ifree.common.gwt.shared.loader.FilterHandler} that provides support for <code>Date</code> values.
 */
public class DateIntervalFilterHandler extends FilterHandler<DateInterval> {
  @Override
  public DateInterval convertToObject(String value) {
      String[] strings = value.split(";");
      String from = strings[0];
      String to = strings[1];

      return new DateInterval(from != null ? new Date(Long.parseLong(from)) : null,
              to != null ? new Date(Long.parseLong(to)) : null
      );
  }

    @Override
    public String convertToString(DateInterval object) {
        String from = object.getFrom() != null ? object.getFrom().getTime() + "" : "";
        String to = object.getFrom() != null ? object.getFrom().getTime() + "" : "";

        return from + ";" + to;
    }

}
