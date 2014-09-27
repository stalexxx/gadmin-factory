package com.gafactory.core.client.ui.lists;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.gafactory.core.client.events.PerformFilterEvent;
import com.gafactory.core.client.ui.BaseFilter;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Styles;

/**
 * Created by alex on 23.04.14.
 */
public abstract class BaseFilterPanel<F extends BaseFilter, E extends Editor<? super F>> extends Composite implements TakesValue<F>, Editor<F> {

    public final SimpleBeanEditorDriver<F, E> driver = createDriver();

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

        buttonGroup.addStyleName(Styles.CLEARFIX);

        buttonGroup.add(initReset());

    }

    @SuppressWarnings("unchecked")
    protected void addChangeHandlers(HasValueChangeHandlers... hasHandlerEditors) {
        ValueChangeHandler handler = new ValueChangeHandler() {
            @Override
            public void onValueChange(ValueChangeEvent event) {
                onApplyFilter();
            }
        };
        for (HasValueChangeHandlers<?> editor : hasHandlerEditors) {
            editor.addValueChangeHandler(handler);
        }
    }

    protected abstract UiBinder<HTMLPanel, E> createBinder();

    private Button initReset() {
        Button reset = new Button("Сбросить", IconType.TIMES, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.onPerformFilter(new PerformFilterEvent(null));
                BaseFilterPanel.this.setValue(createEmpty());
                onReset();
            }
        });
        reset.setType(ButtonType.PRIMARY);
        return reset;

    }

    protected void onReset() {

    }

    private Button initApply() {
        Button apply = new Button("Применить", IconType.BELL, new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onApplyFilter();
            }
        });
        apply.setType(ButtonType.PRIMARY);
        apply.addStyleName(Styles.PULL_LEFT);
        return apply;

    }

    protected void onApplyFilter() {
        uiHandlers.onPerformFilter(new PerformFilterEvent(getFilter()));
    }

    protected abstract SimpleBeanEditorDriver<F, E> createDriver();

    protected abstract F createEmpty();

    public F getFilter() {
        return driver.flush();
    }

    @Override
    public void setValue(F value) {
        driver.edit(value != null ? value : createEmpty());
    }

    @Override
    public F getValue() {
        return getFilter();
    }

    public void setUiHandlers( PerformFilterEvent.PerformFilterHandler uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
}
