/**
 */
package com.ifree.common.gwt.shared.loader;

import com.ifree.common.gwt.shared.types.DateInterval;

import java.util.Date;

/**
 * A {@link com.ifree.common.gwt.shared.loader.FilterHandler} that provides support for <code>Date</code> values.
 */
public class DateIntervalFilterHandler extends FilterHandler<DateInterval> {

    public static final String DELIM = "-";

    @Override
  public DateInterval convertToObject(String value) {
      String[] strings = value.split(DELIM);
      String from = strings.length > 0 ? strings[0] : null;
      String to = strings.length > 1 ? strings[1] : null;

      return new DateInterval(from != null ? new Date(Long.parseLong(from)) : null,
              to != null ? new Date(Long.parseLong(to)) : null
      );
  }

    @Override
    public String convertToString(DateInterval object) {
        String from = object.getFrom() != null ? object.getFrom().getTime() + "" : "";
        String to = object.getTo() != null ? object.getTo().getTime() + "" : "";

        return from + DELIM + to;
    }

}
