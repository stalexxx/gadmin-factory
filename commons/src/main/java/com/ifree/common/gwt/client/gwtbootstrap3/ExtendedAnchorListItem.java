package com.ifree.common.gwt.client.gwtbootstrap3;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.base.HasTarget;

/**
 * Created by alex on 08.08.14.
 */
public class ExtendedAnchorListItem extends AnchorListItem implements HasTarget{


    @Override
    public void setTarget(String target) {
        anchor.setTarget(target);
    }

    @Override
    public String getTarget() {
        return anchor.getTarget();
    }

}
