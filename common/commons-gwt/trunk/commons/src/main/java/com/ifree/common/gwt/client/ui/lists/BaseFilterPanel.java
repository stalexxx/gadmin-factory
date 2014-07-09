package com.ifree.common.gwt.client.ui.lists;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ifree.common.gwt.client.events.PerformFilterEvent;
import com.ifree.common.gwt.client.ui.BaseFilter;
import com.ifree.common.gwt.client.ui.application.Filter;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 * Created by alex on 23.04.14.
 */
public abstract class BaseFilterPanel<F extends BaseFilter, E extends Editor<? super F>> extends Composite implements TakesValue<F>, Editor<F> {

    public final SimpleBeanEditorDriver<F, E> driver = createDriver();

    private Button reset;
    private Button apply;

    @UiField
    public Form form;

    protected PerformFilterEvent.PerformFilterHandler uiHandlers;

    protected BaseFilterPanel() {

    }

    protected void init() {
        initWidget(createBinder().createAndBindUi((E) this));

        driver.initialize((E) this);
        driver.edit(createEmpty());

        ButtonGroup buttonGroup = new ButtonGroup();

        form.add(buttonGroup);

        buttonGroup.add(apply = initApply());
        buttonGroup.add(reset = initReset());
    }

    protected abstract UiBinder<HTMLPanel, E> createBinder();

    private Button initReset() {
        return new Button("Сбросить", IconType.REFRESH, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.onPerformFilter(new PerformFilterEvent(null));
                BaseFilterPanel.this.setValue(createEmpty());
            }
        });

    }

    private Button initApply() {
        Button apply = new Button("Применить", IconType.BELL, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.onPerformFilter(new PerformFilterEvent(getFilter()));
            }
        });
        apply.setType(ButtonType.PRIMARY);
        return apply;

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
