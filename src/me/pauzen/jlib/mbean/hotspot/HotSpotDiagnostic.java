package me.pauzen.jlib.mbean.hotspot;

import me.pauzen.jlib.mbean.MBeanObject;
import me.pauzen.jlib.mbean.MBeanServerWrapper;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;

public final class HotSpotDiagnostic extends MBeanObject {

    private static final ObjectName objectName;

    private static final HotSpotDiagnostic instance;

    static {
        ObjectName objectName1 = null;
        try {
            objectName1 = new ObjectName("com.sun.management", "type", "HotSpotDiagnostic");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        objectName = objectName1;
        instance = new HotSpotDiagnostic();
    }

    private HotSpotDiagnostic() {
        super(objectName);
    }

    public static HotSpotDiagnostic getInstance() {
        return instance;
    }

    public boolean useCompressedOops() {
        return Boolean.parseBoolean(getVMOption("UseCompressedOops"));
    }

    public int getAlignment() {
        return Integer.parseInt(getVMOption("ObjectAlignmentInBytes"));
    }

    public String getVMOption(String optionKey) {
        CompositeDataSupport compositeDataSupport = (CompositeDataSupport) MBeanServerWrapper.invoke(this, "getVMOption", optionKey);
        return (String) compositeDataSupport.get("value");
    }
}
