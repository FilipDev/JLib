package me.pauzen.jlib.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public final class UnsafeProvider {

    private static final int    ADDRESS_SIZE;
    private static final Unsafe UNSAFE;

    static {
        Unsafe unsafe = null;
        try {
            Field theUnsafeField = Unsafe.class.getDeclaredFields()[0]; //GETS THE theUnsafe FIELD FROM Unsafe CLASS
            theUnsafeField.setAccessible(true);                         //MAKES IT ACCESSIBLE
            unsafe = (Unsafe) theUnsafeField.get(null);                 //GETS THE VALUE OF THE FIELD
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        UNSAFE = unsafe;

        assert UNSAFE != null;
        ADDRESS_SIZE = UNSAFE.addressSize();
    }

    /**
     * Prevents instantiation.
     */
    private UnsafeProvider() {
    }

    /**
     * Gets the address size.
     *
     * @return The normal address size for the JVM.
     */
    public static int getAddressSize() {
        return ADDRESS_SIZE;
    }

    /**
     * Gets the Unsafe instance.
     *
     * @return The Unsafe instance that was retrieved through reflection.
     */
    public static Unsafe getUnsafe() {
        return UNSAFE;
    }

}
