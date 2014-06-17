package com.ifree.common.gwt.client.ui.widgets;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import org.gwtbootstrap3.extras.summernote.client.ui.Summernote;
import org.gwtbootstrap3.extras.summernote.client.ui.base.Toolbar;

import java.text.ParseException;

/**
 * Created by alex on 29.05.14.
 */
public class MySummernote extends Summernote implements LeafValueEditor<String> {
    public MySummernote() {
        setToolbar(new Toolbar().toggleAll(true)
                        .setShowCodeViewButton(false)
                        .setShowFullScreenButton(false)
                        .setShowHelpButton(false)
                        .setShowInsertVideoButton(false)
                        .setShowLineHeightButton(false)
                        .setShowInsertPictureButton(false)
                        );
    }

    @Override
    public void setValue(String value, boolean fireEvents) {
        String oldValue = fireEvents ? this.getValue() : null;
        setCode(value);

        reconfigure();

        if (fireEvents) {
            String newValue = this.getValue();
            ValueChangeEvent.fireIfNotEqual(this, oldValue, newValue);
        }
    }


    @Override
    public String getValue() {
        return getCode();
    }

    @Override
    public String getValueOrThrow() throws ParseException {
        return getValue();
    }

}
