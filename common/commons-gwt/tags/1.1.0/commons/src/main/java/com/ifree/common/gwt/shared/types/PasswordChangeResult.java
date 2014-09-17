package com.ifree.common.gwt.shared.types;

import java.io.Serializable;

/**
 * Created by alex on 15.08.14.
 */
public enum PasswordChangeResult implements Serializable {
    OK,
    BAD_OLD_PWD,
    DOESNT_MEET_REQUERMENTS,
    UNKNOWN
}
