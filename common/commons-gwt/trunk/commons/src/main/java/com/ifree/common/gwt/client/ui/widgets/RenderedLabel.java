package com.ifree.common.gwt.client.ui.widgets;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

/**
 * Created by alex on 04.07.14.
 */
public class RenderedLabel<T> extends Composite implements LeafValueEditor<T> {

    private final HTML label;

    private final Renderer<T> renderer;


    public RenderedLabel(Renderer<T> renderer) {
        this.renderer = renderer;


        initWidget(label = new HTML());


    }

    @Override
    public void setValue(T value) {
        label .setHTML(renderer.render(value));
    }

    @Override
    public T getValue() {
        return null;
    }
}
