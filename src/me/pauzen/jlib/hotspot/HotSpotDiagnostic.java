package me.pauzen.jlib.hotspot;

import com.sun.management.VMOption;
import me.pauzen.jlib.mbean.MBeanObject;
import me.pauzen.jlib.unsafe.UnsafeProvider;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;

public final class HotSpotDiagnostic extends MBeanObject {

    private static final ObjectName        objectName;
    private static final HotSpotDiagnostic instance;
    private static boolean isArchitecture32 = UnsafeProvider.getAddressSize() == 4;
    private Integer alignment;
    private Boolean compressedOops;

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
        if (compressedOops == null) return compressedOops = Boolean.parseBoolean(getVMOption("UseCompressedOops"));
        return compressedOops;
    }

    /**
     * Object alignment in bytes.
     *
     * @return Object alignment in bytes.
     */
    public int getAlignment() {
        if (alignment == null) return alignment = Integer.parseInt(getVMOption("ObjectAlignmentInBytes"));
        return alignment;
    }

    /**
     * Gets VM option.
     *
     * @param optionKey Option key.
     * @return Option value.
     */
    public String getVMOption(String optionKey) {
        CompositeDataSupport compositeDataSupport = (CompositeDataSupport) invoke("getVMOption", optionKey);
        VMOption option = VMOption.from(compositeDataSupport);
        return option.getValue();
    }
}
