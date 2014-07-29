package com.ifree.common.gwt.client.ui.widgets;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Styles;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import org.gwtbootstrap3.client.ui.html.Span;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 27.05.14.
 */
public class BagePanel<T> extends FlowPanel implements HasEnabled, HasValue<List<T>> {

    private Renderer<T> renderer;
    private boolean enabled = true;

    private List<T> list = Lists.newArrayList();

    private Map<String, T> replacementMap = Maps.newHashMap();


    public BagePanel(Renderer<T> renderer) {
        this.renderer = renderer;
        addStyleName("clearfix");
    }


    @Override
    public List<T> getValue() {
        return list != null ? Lists.newArrayList(list) : list;
    }

    public void setValue(List<T> value) {
        setValue(value, false);
    }

    @Override
    public void setValue(List<T> value, boolean fireEvents) {

        clear();
        list.clear();

        if (value != null) {
            list.addAll(value);
            for (T t : value) {
                addItem(t);
            }

        }

        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }

    public void addItem(T item) {
        FlowPanel panel = new FlowPanel();

        panel.addStyleName("pull-left");
        panel.addStyleName(Styles.BADGE);


        Span badge = new Span();

        Anchor anchor = new Anchor();
        setMargin(anchor, 3);
        anchor.setIcon(IconType.TIMES);

        final String rendered = renderer.render(item);
        replacementMap.put(rendered, item);


        badge.setText(rendered);

        anchor.addClickHandler(new RemoveClickHandler(rendered));

        panel.add(badge);
        panel.add(anchor);
        setMargin(panel, 2);

        add(panel);
    }


    private void setMargin(Widget anchor, int value) {
        anchor.getElement().getStyle().setMargin(value, Style.Unit.PX);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<T>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }


    private class RemoveClickHandler implements ClickHandler {
        private final String render;

        public RemoveClickHandler(String render) {
            this.render = render;
        }

        @Override
        public void onClick(ClickEvent event) {
            if (isEnabled()) {
                final T deleted = replacementMap.get(render);
                if (deleted != null) {
                    final Collection<T> ts = Sets.newHashSet(getValue());
                    ts.remove(deleted);
                    setValue(Lists.newArrayList(ts), true);
                }
            }
        }
    }
}
