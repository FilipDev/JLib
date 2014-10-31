package me.pauzen.jlib.mbean.hotspot;

import me.pauzen.jlib.mbean.MBeanObject;
import me.pauzen.jlib.mbean.MBeanServerHelper;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;

public final class HotSpotDiagnostic extends MBeanObject {

    private static final ObjectName  objectName;

    static {
        ObjectName objectName1 = null;
        try {
            objectName1 = new ObjectName("com.sun.management", "type", "HotSpotDiagnostic");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        objectName = objectName1;
    }

    public HotSpotDiagnostic() {
        super(objectName);
    }

    public boolean useCompressedOops() {
        return Boolean.parseBoolean(getVMOption("UseCompressedOops"));
    }

    public int getAlignment() {
        return Integer.parseInt(getVMOption("ObjectAlignmentInBytes"));
    }

    public String getVMOption(String optionKey) {
        CompositeDataSupport compositeDataSupport = (CompositeDataSupport) MBeanServerHelper.invoke(this, "getVMOption", optionKey);
        return (String) compositeDataSupport.get("value");
    }
}
