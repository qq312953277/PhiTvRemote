package com.phicomm.remotecontrol.modules.main.screenprojection.entity;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.DescMeta;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.WriteStatus;
import org.fourthline.cling.support.model.item.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang.sun on 2017/8/21.
 */
public class MItem extends Item implements Serializable {
    private String filePath;
    private String id;

    public MItem() {
    }

    public MItem(Item other) {
        super(other);
        setRefID(other.getRefID());
    }

    public MItem(String id, String parentID, String title, String creator, boolean restricted, WriteStatus writeStatus, Class clazz, List<Res> resources, List<Property> properties, List<DescMeta> descMetadata) {
        super(id, parentID, title, creator, restricted, writeStatus, clazz, resources, properties, descMetadata);
    }

    public MItem(String id, String parentID, String title, String creator, boolean restricted, WriteStatus writeStatus, Class clazz, List<Res> resources, List<Property> properties, List<DescMeta> descMetadata, String refID) {
        super(id, parentID, title, creator, restricted, writeStatus, clazz, resources, properties, descMetadata);
        this.refID = refID;
    }

    public MItem(String id, String parentID, String title, String creator, String filePath, DIDLObject.Class clazz) {
        this(id, parentID, title, creator, false, null, clazz, new ArrayList(), new ArrayList(), new ArrayList());
        this.filePath = filePath;
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getId() {
        return id;
    }
}