package com.ifree.common.gwt.client.ui.widgets;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.RadioButton;
import org.gwtbootstrap3.client.ui.constants.Toggle;


/**
 * Created by alex on 25.04.14.
 */
public class BooleanEditor extends Composite implements LeafValueEditor<Boolean> {

    private final RadioButton yes;
    private final RadioButton no;
    private final RadioButton doesntMatter;

    public BooleanEditor() {
        ButtonGroup group = new ButtonGroup();
        group.setDataToggle(Toggle.BUTTONS);
        doesntMatter = new RadioButton("Не важно");
        doesntMatter.setActive(true);

        group.add(doesntMatter);
        yes = new RadioButton("Да");
        group.add(yes);
        no = new RadioButton("Нет");
        group.add(no);

        initWidget(group);

        /*doesntMatter.addClickHandler(this);
        yes.addClickHandler(this);
        no.addClickHandler(this);*/
    }

    @Override
    public void setValue(Boolean value) {

            doesntMatter.setActive(value == null);

            yes.setActive(value == Boolean.TRUE);

            no.setActive(value == Boolean.FALSE);
    }

    @Override
    public Boolean getValue() {
        if (doesntMatter.isActive()) {
            return null;
        } else if (yes.isActive()) {
            return true;
        } else {
            return false;
        }
    }


}
