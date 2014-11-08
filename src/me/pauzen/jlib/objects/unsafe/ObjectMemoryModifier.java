package me.pauzen.jlib.objects.unsafe;

import me.pauzen.jlib.misc.Pointer;

public interface ObjectMemoryModifier<T> {

    public T getObject();

    public void setObject(T value);

    public void putInt(long offset, int value);

    public int getInt(long offset);

    public void putLong(long offset, long value);

    public long getLong(long offset);

    public long toAddress();

    public long getSize();

    public void setClass(Class clazz);

    public T deepClone();

    public Pointer toPointer();

    public Object get(long offset);

    public void put(long offset, Object object);

    public void copyMemory(long address);
}
