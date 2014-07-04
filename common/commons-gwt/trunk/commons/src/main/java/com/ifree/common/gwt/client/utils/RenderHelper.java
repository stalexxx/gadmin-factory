package com.ifree.common.gwt.client.utils;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * Created by alex on 03.07.14.
 */
public class RenderHelper {

    public static SafeHtml multiline(String... list) {

        SafeHtmlBuilder builder = new SafeHtmlBuilder();

        if (list != null) {
            for (String line : list) {

                if (line != null) {

                    builder.append(SafeHtmlUtils.fromTrustedString("<div>" + line + "</div>"));
                }

            }
        }

        return builder.toSafeHtml();

    }
}
