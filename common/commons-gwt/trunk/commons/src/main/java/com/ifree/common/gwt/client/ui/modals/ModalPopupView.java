package com.ifree.common.gwt.client.ui.modals;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PopupViewCloseHandler;
import com.gwtplatform.mvp.client.ViewImpl;
import org.gwtbootstrap3.client.shared.event.ModalHideEvent;
import org.gwtbootstrap3.client.shared.event.ModalHideHandler;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;

/**
 * Created by alex on 14.07.14.
 */
public abstract class ModalPopupView extends ViewImpl implements PopupView {

    private Modal modal;
    private HandlerRegistration hideHandler;

    public ModalPopupView(String title) {
        this.modal = new Modal();

        modal.add(createBody());
        modal.add(createFooter());
        modal.setClosable(true);
        modal.setTitle(title);
    }

    private IsWidget createFooter() {
        ModalFooter footer = new ModalFooter();
        Button button = new Button("Закрыть", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });
        footer.add(button);

        return footer;
    }

    private ModalBody createBody() {
        ModalBody body = new ModalBody();
        body.add(createContent());


        return body;
    }

    protected abstract IsWidget createContent();

    @Override
    public void center() {
        modal.show();
    }

    @Override
    public void hide() {
        modal.hide();
    }

    @Override
    public void setAutoHideOnNavigationEventEnabled(boolean autoHide) {

    }

    @Override
    public void setCloseHandler(final PopupViewCloseHandler popupViewCloseHandler) {
        if (hideHandler != null) {
            hideHandler.removeHandler();
        }

        hideHandler = modal.addHideHandler(new ModalHideHandler() {
            @Override
            public void onHide(ModalHideEvent evt) {
                if (popupViewCloseHandler != null) {
                    popupViewCloseHandler.onClose();
                }
            }
        });
    }

    @Override
    public void setPosition(int left, int top) {
        //ignore
    }

    @Override
    public void show() {
        modal.show();
    }
}
