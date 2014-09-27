package com.ifree.common.gwt.shared;

import java.io.Serializable;

/**
 * Created by alex on 20.05.14.
 */
public class SavingResult<T extends Serializable> implements Serializable {
    private T id;
    private boolean saved;
    private String errorMessage;

    public SavingResult() {
    }

    public SavingResult(String errorMessage) {
        this.id = null;
        this.saved = false;
        this.errorMessage = errorMessage;
    }

    public SavingResult(T id) {
        this.id = id;
        saved = true;

    }
    public SavingResult(boolean saved, T id) {
        this.saved = saved;
        this.id = id;

    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
