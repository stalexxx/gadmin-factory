package com.ifree.common.gwt.shared.types;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by alex on 08.07.14.
 */
public class DateInterval implements Serializable{
    Date from;
    Date to;

    public DateInterval(Date fromValue, Date toValue) {
        from = fromValue;
        to = toValue;
    }

    public DateInterval() {
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }
}
