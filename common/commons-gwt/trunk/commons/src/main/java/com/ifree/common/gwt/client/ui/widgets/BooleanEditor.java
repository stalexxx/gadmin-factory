package com.ifree.common.gwt.client.ui.widgets;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.uibinder.client.UiConstructor;
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
        this("Не важно", "Да", "Нет");
    }

    //@UiConstructor
    public BooleanEditor(String doesntMatterText, String yesText,String noText) {
        ButtonGroup group = new ButtonGroup();
        group.setDataToggle(Toggle.BUTTONS);
        if (doesntMatterText != null) {
            doesntMatter = new RadioButton(doesntMatterText);
            doesntMatter.setActive(true);

            group.add(doesntMatter);
        } else {
            doesntMatter = null;
        }
        yes = new RadioButton(yesText);
        group.add(yes);
        no = new RadioButton(noText);
        group.add(no);

        initWidget(group);

    }


    @Override
    public void setValue(Boolean value) {
        if (doesntMatter != null) {
            doesntMatter.setValue(value == null);
        }

        yes.setValue(Boolean.TRUE.equals(value));
        no.setValue(Boolean.FALSE.equals(value));
    }

    @Override
    public Boolean getValue() {
        if (no.isActive()) {
            return false;
        } else if (yes.isActive()) {
            return true;
        } else {

            return null;
        }
    }


}
