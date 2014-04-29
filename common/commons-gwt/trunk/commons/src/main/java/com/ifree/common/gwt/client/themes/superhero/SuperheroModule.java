package com.ifree.common.gwt.client.themes.superhero;

import com.google.gwt.inject.client.AbstractGinModule;
import com.ifree.common.gwt.client.themes.CellTableResources;

/**
 * Created by alex on 29.04.14.
 */
public class SuperheroModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(CellTableResources.class).to(SuperheroCellTableResources.class);
    }
}
