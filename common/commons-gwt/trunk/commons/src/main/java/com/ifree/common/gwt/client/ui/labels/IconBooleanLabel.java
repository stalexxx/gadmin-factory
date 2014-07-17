package com.ifree.common.gwt.client.ui.labels;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.user.client.ui.Composite;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 * Created by alex on 15.07.14.
 */
public class IconBooleanLabel extends Composite implements LeafValueEditor<Boolean> {
    protected final Icon label = new Icon();

    public IconBooleanLabel() {
        initWidget(label);
        label.setSize(IconSize.TIMES2);
    }

    @Override
    public void setValue(Boolean value) {
        label.setVisible(value != null);

        if (value != null) {
            if (value) {
                label.setType(IconType.CHECK);
                label.getElement().getStyle().setColor("green");

            } else {

                label.setType(IconType.TIMES);
                label.getElement().getStyle().setColor("red");

            }
        }
    }

    @Override
    public Boolean getValue() {
        return null;
    }
}
