package com.ifree.common.gwt.client.ui.modals;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.TakesValue;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.gwt.HTMLPanel;

/**
* Created by alex on 09.07.14.
*/
public abstract class TextAreaModal<ID> extends Modal implements TakesValue<String> {

    private final TextArea area;
    private ID itemId;

    public TextAreaModal() {

        ModalHeader header = new ModalHeader();
        header.setClosable(true);
        header.add(new HTMLPanel("Ответ на комментарий"));

        add(header);

        ModalBody body = new ModalBody();
        area = new TextArea();
        area.setVisibleLines(5);

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

                doSave(itemId, area.getValue());

            }
        });
        save.setType(ButtonType.PRIMARY);
        ModalFooter footer = new ModalFooter();
        footer.add(save);
        footer.add(close);
        add(footer);

    }


    @Override
    public void setValue(String value) {
        area.setValue(value);
    }

    @Override
    public String getValue() {
        return area.getValue();
    }

    protected abstract void doSave(ID itemId, String text);

    public void setItemId(ID itemId) {
        this.itemId = itemId;
    }

    public ID getItemId() {
        return itemId;
    }
}
