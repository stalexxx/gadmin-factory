/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.application;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.ProgressBar;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;

import java.util.logging.Logger;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 09.05.13
 */
public class BlockingOverlay implements UIBlocker {
    private static final int PROGRESS_ADD_STARTING = 25;
    private static final int PROGRESS_DELAY = 2000;
    public static final int WIDTH = 200;

    private PopupPanel panel = new PopupPanel();
    private final ProgressBar bar;

    private int progress;
    private int progressAdditivity;

    @Inject
    private Logger logger;

    private Timer timer = new Timer() {
        @Override
        public void run() {
            if (progress < 100) {
                progress += progressAdditivity;
                if (progressAdditivity > 1) {
                    progressAdditivity /= 2;
                }
            } else {
                progress = 99;
            }
            bar.setPercent(progress);
        }
    };

    public BlockingOverlay() {
        panel.setAutoHideEnabled(false);
        panel.setGlassEnabled(true);
        panel.setModal(true);

        panel.setStyleName("blocking-panel");
        bar = new ProgressBar();

        bar.getElement().getStyle().setWidth(WIDTH, Style.Unit.PX);
        Icon icon = new Icon(IconType.ROTATE_RIGHT);
        icon.setSpin(true);
        icon.setSize(IconSize.TIMES4);
        panel.setWidget(icon);


    }

    @Override
    public void block() {
        progress = 0;
        progressAdditivity = PROGRESS_ADD_STARTING;
        bar.setPercent(progress);

        timer.scheduleRepeating(PROGRESS_DELAY);

        panel.center();
    }

    @Override
    public void unblock() {
        timer.cancel();

        panel.hide();
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

}
