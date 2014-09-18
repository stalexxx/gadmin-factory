package com.ifree.common.gwt.shared;

import java.io.Serializable;

/**
 * Created by alex on 20.05.14.
 */
public class RemovingResult implements Serializable {
    private boolean removed;
    private String errorMessage;


    public RemovingResult(String errorMessage) {
        this.removed = false;
        this.errorMessage = errorMessage;
    }

    public RemovingResult() {
        removed = true;

    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
