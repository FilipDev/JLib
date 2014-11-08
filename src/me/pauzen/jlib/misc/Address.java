package me.pauzen.jlib.misc;

import me.pauzen.jlib.hotspot.HotSpotDiagnostic;
import me.pauzen.jlib.unsafe.UnsafeProvider;

public final class Address {

    public static long normalize(int value) {
        if (value >= 0) return value;
        return (~0L >>> 32) & value;
    }

    public static long shiftWithCompressedOops(long value) {
        if (HotSpotDiagnostic.getInstance().useCompressedOops())
            return UnsafeProvider.getUnsafe().getAddress((value << 3));
        else return value;
    }

}
