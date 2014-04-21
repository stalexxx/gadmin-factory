/*
 * Copyright (c) 2012, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */
package com.ifree.common.gwt.client.ui.application.security;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import java.util.Collections;
import java.util.List;

/**
 * This is a basic class that holds the privileges of the user currently logged in.
 *
 * @author Alexander Ostrovskiy
 * @since 1.0
 */
public class CurrentUser implements HasHandlers {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_MANAGER = "ROLE_MANAGER";

    private String userName;
    private List<String> roles;

    private final EventBus eventBus;

    @Inject
    public CurrentUser(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void setUser(String name, List<String> roles) {
        setUser(name, roles, false);

    }

    public void setUser(String name, List<String> roles, boolean fire) {
        this.userName = name;
        this.roles = roles != null ? Lists.newArrayList(roles) : null;


    }

    public static native String getPrincipal() /*-{
        return $wnd.user_principal;
    }-*/;

    public static native String getAuths() /*-{
        return $wnd.user_authorities;
    }-*/;


    private void initUserLoad() {
        String principal = getPrincipal();
        String auths = getAuths();

        List<String> roles = Lists.newArrayList(Splitter.on(", ").split(auths.substring(1, auths.length() - 1)));

        setUser(principal, roles, true);
    }


    public boolean isAdmin() {
        ensureUser();

        return hasRole(ROLE_ADMIN);
    }

    public boolean isManager() {
        ensureUser();

        return hasRole(ROLE_MANAGER);
    }

    private void ensureUser() {
        if (userName == null) {
            initUserLoad();
        }
    }

    public static boolean hasAdmin(List<String> roles) {
        return roles != null && roles.contains(ROLE_ADMIN);

    }


    private boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        eventBus.fireEvent(event);
    }

    public String getUserName() {
        ensureUser();

        return userName;
    }

    public List<String> getRoles() {
        ensureUser();

        return roles != null ? Lists.newArrayList(roles) : Collections.<String>emptyList();
    }

}
