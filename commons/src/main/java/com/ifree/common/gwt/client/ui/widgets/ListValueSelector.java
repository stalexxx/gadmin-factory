package com.ifree.common.gwt.client.ui.widgets;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasConstrainedValue;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 26.06.14.
 */
public class ListValueSelector<T> extends Composite implements LeafValueEditor<Collection<T>> {

    private final  Select select;
    private final Renderer<T> renderer;


    Map<String, T> map = Maps.newHashMap();
    List<Option> options = Lists.newArrayList();
    Map<T, Option> backMap = Maps.newHashMap();




    public ListValueSelector(Renderer<T> renderer) {
        this.renderer = renderer;
        select = new Select();
        select.setMultiple(true);

        initWidget(select);
    }


    public void setAcceptableValues(Collection<T> values) {

        options.clear();
        map.clear();
        backMap.clear();

        if (values != null) {
            for (T value : values) {
                Option option = new Option();
                options.add(option);

                //option.getValue()
                String render = renderer.render(value);
                map.put(render, value);
                backMap.put(value, option);

                option.setText(render);

                select.add(option);
            }
        }
    }



    @Override
    public void setValue(Collection<T> value) {


        if (value != null) {
            List<String> list = Lists.newArrayList();

            for (T t : value) {
                list.add(renderer.render(t));
            }

            select.setValues(list.toArray(new String[list.size()]));

            select.refresh();
        } else {
            select.setValues(new String[]{});
            select.refresh();

        }

    }

    @Override
    public Collection<T> getValue() {

        ArrayList<T> ts = Lists.newArrayList();

        List<String> values = select.getAllSelectedValues();

        for (String value : values) {
            T t = map.get(value);
            ts.add(t);
        }
        return ts;
    }

    @Override
    public void setWidth(String width) {
        select.setWidth(width);
    }
}
