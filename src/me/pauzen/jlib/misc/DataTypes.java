package me.pauzen.jlib.misc;

import java.nio.ByteBuffer;

public final class DataTypes {

    private DataTypes() {
    }

    /**
     * Converts a byte array to an int. An int is 4 bytes.
     *
     * @param bytes Byte array to convert to int.
     * @return Int value of bytes.
     */
    public static int toInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }



}
