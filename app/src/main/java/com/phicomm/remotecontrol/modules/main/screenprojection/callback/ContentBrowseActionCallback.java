package com.phicomm.remotecontrol.modules.main.screenprojection.callback;

import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.container.Container;

/**
 * Created by kang.sun on 2017/8/22.
 */
public abstract class ContentBrowseActionCallback extends Browse {
    public ContentBrowseActionCallback(Service service, Container container) {
        super(service, container.getId(), BrowseFlag.DIRECT_CHILDREN, "*", 0,
                null, new SortCriterion(true, "dc:title"));
    }

    @Override
    public void updateStatus(Status status) {
    }
}
