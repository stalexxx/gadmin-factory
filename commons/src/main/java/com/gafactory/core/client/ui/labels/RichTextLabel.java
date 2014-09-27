package com.gafactory.core.client.ui.labels;

import com.google.gwt.text.shared.AbstractRenderer;

/**
 * Created by alex on 15.07.14.
 */
public class RichTextLabel extends RenderedLabel<String> {
    public RichTextLabel() {
        super(new AbstractRenderer<String>() {
            @Override
            public String render(String object) {
                return object != null ? object : null;
            }

        });
    }

    @Override
    public void setValue(String value) {
        label.setHTML(renderer.render(value));
    }
}
