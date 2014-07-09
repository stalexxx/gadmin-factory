package com.ifree.common.gwt.client.ui.modals;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.gwt.HTMLPanel;

/**
* Created by alex on 09.07.14.
*/
public abstract class TextAreaModal extends Modal {
    public TextAreaModal() {

        ModalHeader header = new ModalHeader();
        header.setClosable(true);
        header.add(new HTMLPanel("Ответ на комментарий"));

        add(header);

        ModalBody body = new ModalBody();
        final TextArea area = new TextArea();
        area.setVisibleLines(5);

        area.setText(getInitText());

        body.add(area);

        add(body);

        Button close = new Button("Закрыть", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });



        Button save = new Button("Сохранить", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();

                doSave(area.getText());

            }
        });
        save.setType(ButtonType.PRIMARY);
        ModalFooter footer = new ModalFooter();
        footer.add(save);
        footer.add(close);
        add(footer);

    }

    protected abstract String getInitText();

    protected abstract void doSave(String text);
}
