package me.pauzen.jlib.mbean.hotspot;

import me.pauzen.jlib.mbean.MBeanObject;
import me.pauzen.jlib.unsafe.UnsafeProvider;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;

public final class HotSpotDiagnostic extends MBeanObject {

    private static final ObjectName        objectName;
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

    private static boolean isArchitecture32 = UnsafeProvider.getAddressSize() == 4;

    private HotSpotDiagnostic() {
        super(objectName);
    }

    public static boolean isArchitecture64() {
        return !isArchitecture32;
    }

    public static boolean isArchitecture32() {
        return isArchitecture32;
    }

    public static HotSpotDiagnostic getInstance() {
        return instance;
    }

    /**
     * Return whether or not JVM uses compressed oops.
     *
     * @return Whether HotSpot uses compressed oops.
     */
    public boolean useCompressedOops() {
        return Boolean.parseBoolean(getVMOption("UseCompressedOops"));
    }

    /**
     * Object alignment in bytes.
     *
     * @return Object alignment in bytes.
     */
    public int getAlignment() {
        return Integer.parseInt(getVMOption("ObjectAlignmentInBytes"));
    }

    /**
     * Gets VM option.
     *
     * @param optionKey Option key.
     * @return Option value.
     */
    public String getVMOption(String optionKey) {
        CompositeDataSupport compositeDataSupport = (CompositeDataSupport) invoke("getVMOption", optionKey);
        return (String) compositeDataSupport.get("value");
    }
}
