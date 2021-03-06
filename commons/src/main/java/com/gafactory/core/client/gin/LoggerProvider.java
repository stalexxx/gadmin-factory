package com.gafactory.core.client.gin;

import com.google.inject.Provider;

import java.util.logging.Logger;

/**
* Created by alex on 24.06.14.
*/
public class LoggerProvider implements Provider<Logger> {

    @Override
    public Logger get() {
        return Logger.getLogger("com.gafactory.core.remote");
    }
}
