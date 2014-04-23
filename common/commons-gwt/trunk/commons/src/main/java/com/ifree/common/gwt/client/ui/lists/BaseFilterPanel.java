package com.ifree.common.gwt.client.ui.lists;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Composite;
import com.ifree.common.gwt.client.events.PerformFilterEvent;
import com.ifree.common.gwt.client.ui.BaseFilter;
import com.ifree.common.gwt.client.ui.application.Filter;
import org.gwtbootstrap3.client.ui.Button;

/**
 * Created by alex on 23.04.14.
 */
public abstract class BaseFilterPanel<F extends BaseFilter, E extends Editor<? super F>> extends Composite implements TakesValue<F>, Editor<F> {

    public final SimpleBeanEditorDriver<F, E> driver = createDriver();


    protected PerformFilterEvent.PerformFilterHandler uiHandlers;


    protected void initReset(Button button) {
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.onPerformFilter(new PerformFilterEvent(null));
                BaseFilterPanel.this.setValue(createEmpty());
            }
        });
    }

    protected void initApply(Button button) {
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.onPerformFilter(new PerformFilterEvent(getFilter()));
            }
        });
    }

    protected abstract SimpleBeanEditorDriver<F, E> createDriver();

    protected abstract F createEmpty();

    public F getFilter() {
        return driver.flush();
    }

    @Override
    public void setValue(F value) {
        driver.edit(value);
    }

    @Override
    public F getValue() {
        return getFilter();
    }

    public void setUiHandlers( PerformFilterEvent.PerformFilterHandler uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
}
