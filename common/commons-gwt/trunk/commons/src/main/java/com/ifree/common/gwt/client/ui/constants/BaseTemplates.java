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

    @Template("<i class=\"{0} {1} {2}\" ></i>" )
    SafeHtml icon(String fa, String icon, String color);

    @Template("<img class=\"{0}\" src=\"{1}\" style=\"height: 70px\"/>")
    SafeHtml image(String style, String url);

    @Template("<span class=\"label {1}\" title=\"{2}\">{0}</span>")
    SafeHtml label(String text, String color, String title);

}
