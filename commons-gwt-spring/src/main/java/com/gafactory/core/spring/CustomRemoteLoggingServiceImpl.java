/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.gafactory.core.spring;

import com.google.gwt.logging.server.RemoteLoggingServiceImpl;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.File;
import java.util.logging.LogManager;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 23.05.13
 */
public class CustomRemoteLoggingServiceImpl extends RemoteLoggingServiceImpl {


    private static final long serialVersionUID = 2471029272551816601L;

    @SuppressWarnings("ConstantConditions")
    public CustomRemoteLoggingServiceImpl() {
        try {
            String path = Thread.currentThread().getContextClassLoader().getResource("logback.xml").getPath();
            String preffix = new File(path).getParentFile().getParentFile().getAbsolutePath();
            setSymbolMapsDirectory(preffix + "/deploy/project/symbolMaps/");
        } catch (Exception e) {
            //IGNORE
        }
        setLoggerNameOverride("com.gafactory.core.remote");

        if (!SLF4JBridgeHandler.isInstalled()) {
            LogManager.getLogManager().reset();
            SLF4JBridgeHandler.install();
        }


    }

}
