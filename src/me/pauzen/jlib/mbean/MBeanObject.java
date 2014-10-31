package me.pauzen.jlib.mbean;

import javax.management.ObjectName;

public abstract class MBeanObject {

    private final ObjectName objectName;

    public MBeanObject(ObjectName objectName) {
        this.objectName = objectName;
    }

    public ObjectName getObjectName() {
        return objectName;
    }
}
