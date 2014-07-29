/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.ui.constants;

import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 21.05.13
 */
public interface BaseTemplates extends SafeHtmlTemplates {

    /**
     *
     * @param fa use Styles.FONT_AWESOME_BASE
     * @param icon
     * @param color
     * @return
     */
    @Template("<i class=\"{0} {1} {2}\"></i>" )
    SafeHtml icon(String fa, String icon, String color);

    @Template("<i class=\"{0} {1} {2} {3}\" title=\"{4}\"></i>" )
    SafeHtml icon(String fa, String icon, String size, String color, String title);

    @Template("<img class=\"{0}\" src=\"{1}\" style=\"height: 70px\"/>")
    SafeHtml image(String style, String url);

    @Template("<span class=\"label {1}\" title=\"{2}\" style=\"width: 100%\">{0}</span>")
    SafeHtml label(String text, String color, String title);

    @Template("<span class=\"badge\" style=\"margin: {1}px\">{0}</span>")
    SafeHtml badge(String text, int padding);

}
