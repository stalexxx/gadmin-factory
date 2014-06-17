/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.ifree.common.gwt.client.utils;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Alexander Ostrovskiy (a.ostrovskiy)
 * @since 26.04.13
 */
@SuppressWarnings("NativeMethod")
public class StringUtils {

    /*===========================================[ STATIC VARIABLES ]=============*/

    public static final String STR_EMPTY = "";
    public static final String STR_SPACE = " ";
    public static final String STR_DELIMITER = "/";
    public static final String STR_DOT = ".";
    private static final String NON_THIN = "[^iIl1\\.,']";
    @SuppressWarnings("HardcodedLineSeparator")
    public static final String LINE_SEPARATOR = "\n";

    /*===========================================[ CONSTRUCTORS ]=================*/

    private StringUtils() {
    }

    /*===========================================[ CLASS METHODS ]================*/

    public static String safeStr(String param) {
        return param != null ? param : "";

    }

    public static String ellipsize(final String text, final int max) {
        if (text == null) {
            throw new IllegalArgumentException("text==null");
        }

        if (textWidth(text) <= max) {
            return text;
        }

        // Start by chopping off at the word before max
        // This is an over-approximation due to thin-characters...
        int end = text.lastIndexOf(' ', max - 3);

        // Just one long word. Chop it off.
        if (end == -1) {
            return text.substring(0, max - 3) + "...";
        }

        // Step forward as long as textWidth allows.
        int newEnd = end;
        do {
            end = newEnd;
            newEnd = text.indexOf(' ', end + 1);

            // No more spaces.
            if (newEnd == -1) {
                newEnd = text.length();
            }
        } while (textWidth(text.substring(0, newEnd) + "...") < max);

        return text.substring(0, end) + "...";
    }

    private static int textWidth(String str) {
        return str.length() - str.replaceAll(NON_THIN, "").length() / 2;
    }


    public static RegExp prepareOrPattern(final String query) {
        if (query != null) {
            final String nonEscapedQuery = query.trim().toLowerCase();

            final List<String> split = new ArrayList<String>();

            for (String s : split(nonEscapedQuery)) {
                if (s != null && !s.isEmpty()) {
                    split.add(escape(s));
                }
            }

            if (split.isEmpty()) {
                return null;
            } else if (split.size() > 1) {
                return prepareOrPattern(split);
            } else {
                return prepareSimplePattern(escape(nonEscapedQuery));
            }
        } else {
            return null;
        }
    }

    public static List<String> split(final String template) {
        final List<String> result = new ArrayList<String>();

        for (final String s : template.split("\\s+")) {
            result.add(s.toLowerCase());
        }
        return result;
    }


    public static RegExp prepareSimplePattern(final String query) {
        return RegExp.compile(query, "i");
    }

    private static native String escape(String query) /*-{
        return query.replace(/[-[\]{}()*+?.,^$|#\s]/g, "\\$&");
    }-*/;

    public static RegExp prepareOrPattern(final List<String> query) {
        final StringBuilder sb = collect(query, "|", false);
        return RegExp.compile(sb.toString(), "i"); //don't use g flag here, because used in #markedFound
    }

    /**
     * Skips empty parts.
     *
     * @param query     List of "words". Can contain empty strings
     * @param separator
     * @param group
     * @return StringBuilder inst with patterns
     */
    private static StringBuilder collect(final List<String> query, final String separator, final boolean group) {


        final StringBuilder sb = new StringBuilder();
        final Iterator<String> iterator = query.iterator();

        while (iterator.hasNext()) {
            final String next = iterator.next();

            if (!next.isEmpty()) {
                if (group) {
                    sb.append('(').append(next).append(')');
                } else {
                    sb.append(next);
                }

                if (iterator.hasNext()) {
                    sb.append(separator);
                }
            }
        }
        return sb;
    }


    public static RegExp prepareAndPattern(final String query) {
        String q = query.trim().toLowerCase();


        if (q.isEmpty()) {
            return null;
        }

        final List<String> split = split(q);

        return split.size() > 1 ? prepareAndPattern(split) : prepareSimplePattern(q);
    }

    public static RegExp prepareAndPattern(final List<String> query) {
        final StringBuilder sb = collect(query, ".*", true);
        return RegExp.compile(sb.toString(), "i");
    }

    //gwt does not support StringBuilder.reverse() method
    public static String reverse(String str) {
        StringBuilder result = new StringBuilder();
        int i = str.length();
        while (--i >= 0) {
            result.append(str.charAt(i));
        }
        return result.toString();
    }


    public static String markFound(final String str, final RegExp pattern) {
        final StringBuilder sb = new StringBuilder();

        String substring = str;
        MatchResult exec = pattern.exec(str);

        while (exec != null) {
            final int start = exec.getIndex();

            sb.append(substring.substring(0, start)).append("<b>");
            final String occurance = exec.getGroup(0);
            sb.append(occurance).append("</b>");

            substring = substring.substring(exec.getIndex() + occurance.length());
            exec = pattern.exec(substring);
        }

        sb.append(substring);

        return sb.toString();
    }

    public static boolean isWordType(String ext) {
        if (ext == null) {
            return false;
        }

        try {
            WordType.valueOf(ext.toUpperCase());
            return true;
        } catch (IllegalArgumentException iAExc) {
            return false;
        }
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    public static String getLastWordAfterDot(String input) {
        String output = STR_EMPTY;
        if (input != null && input.contains(STR_DOT)) {
            try {
                output = input.substring(input.lastIndexOf(STR_DOT) + 1);
            } catch (StringIndexOutOfBoundsException ignore) {
            }
        }
        return output.equals(STR_EMPTY) ? input : output;
    }

    /*===========================================[ ENUMERATIONS ]=================*/

    enum WordType {
        DOCX, //Word Document
        DOCM, //Word Macro-Enabled Document
        DOC,  //Word 97-2003 Document
        DOTX, //Word Template
        DOTM, //Word Macro-Enabled Template
        DOT,  //Word 97-2003 Template
        MHT,  //Single File Web Page
        MHTML,//Single File Web Page
        HTM,  //Web Page
        HTML, //Web Page
        RTF,  //Rich Text Format
        XML,  //Word XML Document
        ODT,  //OpenDocument Text
        TXT,  //Plain Text
    }
}