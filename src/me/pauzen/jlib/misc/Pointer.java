package me.pauzen.jlib.misc;

import me.pauzen.jlib.objects.Objects;
import me.pauzen.jlib.unsafe.UnsafeProvider;
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

    public int getAddress() {
        return Objects.toIntID(object);
    }

    public String getAddressString() {
        return Long.toHexString(getAddress());
    }
}
