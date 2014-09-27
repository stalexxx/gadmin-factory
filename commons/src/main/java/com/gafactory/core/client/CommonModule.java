package com.gafactory.core.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.gafactory.core.client.data.StorageService;
import com.gafactory.core.client.ui.application.security.CurrentUser;
import com.gafactory.core.client.ui.constants.BaseMessages;
import com.gafactory.core.client.ui.constants.BaseTemplates;

/**
 * Created by alex on 16.04.14.
 */
public class CommonModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(BaseMessages.class).asEagerSingleton();
        bind(BaseTemplates.class).asEagerSingleton();

        bind(CurrentUser.class).in(Singleton.class);
        bind(StorageService.class).in(Singleton.class);

        //resolvers
        //bind(ViewHeaderResolver.class).asEagerSingleton();
    }
}
