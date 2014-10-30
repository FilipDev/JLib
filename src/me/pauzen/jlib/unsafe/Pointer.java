package me.pauzen.jlib.unsafe;

import me.pauzen.jlib.objects.Objects;
import sun.misc.Unsafe;

public class Pointer {

    private static Unsafe unsafe = UnsafeProvider.getUnsafe();

    private final Object object;

    public Pointer(Object object) {
        this.object = object;
    }

    public static void putAddress(long address1, long address2) {
        unsafe.putLong(address1, address2);
    }

    public Object getObject() {
        return this.object;
    }

    public long getAddress() {
        return Objects.toLongID(object);
    }

    public String getAddressString() {
        return Long.toHexString(getAddress());
    }
}
