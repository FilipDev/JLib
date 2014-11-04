package me.pauzen.jlib.classes;

import me.pauzen.jlib.objects.Objects;
import me.pauzen.jlib.reflection.Reflect;
import me.pauzen.jlib.unsafe.UnsafeProvider;
import sun.misc.SharedSecrets;
import sun.misc.Unsafe;
import sun.reflect.ConstantPool;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Classes {

    private static Map<String, Class> CREATED_CLASSES = new HashMap<>();
    private static Unsafe             unsafe          = UnsafeProvider.getUnsafe();
    private static Map<Class, Long>   SIZED_CLASSES   = new HashMap<>();

    private Classes() {
    }

    /**
     * Returns a Class that had been loaded into memory.
     *
     * @param name The name of the Class.
     * @return The found Class object.
     */
    public static Class getClass(String name) {
        if (CREATED_CLASSES.containsKey(name)) return CREATED_CLASSES.get(name);
        else throw new NullPointerException("Class has not been loaded into memory.");
    }

    /**
     * Loads a class into memory given its contents.
     *
     * @param name The canonical name of the Class. Must be the original canonical name.
     * @param data Byte array of the contents of the class file.
     * @return The loaded Class object.
     */
    public static Class toClass(String name, byte[] data) {
        Class clazz = unsafe.defineClass(name, data, 0, data.length);
        CREATED_CLASSES.put(name, clazz);
        return clazz;
    }

    /**
     * Loads a class file into memory.
     *
     * @param name The canonical name of the Class. Must be the original canonical name.
     * @param file The Class file to convert to byte array.
     * @return The loaded Class object.
     * @throws IOException
     */
    public static Class toClass(String name, File file) throws IOException {
        if (file.exists())
            if (file.getName().split("\\.")[1].equals("class")) return toClass(name, Files.readAllBytes(file.toPath()));
        throw new IllegalArgumentException("Incorrect file.");
    }

    /**
     * Returns the ConstantPool for a class.
     *
     * @param clazz The class to get the Constant Pool of.
     * @return The retrieved Constant Pool instance.
     */
    public static ConstantPool getConstantPool(Class clazz) {
        return SharedSecrets.getJavaLangAccess().getConstantPool(clazz);
    }

    /**
     * Gets String at an index in the CP.
     *
     * @param constantPool The Constant Pool to retrieve the String from.
     * @param index        The index at which the String is located in the CP.
     * @return The String at the location in the CP.
     */
    public static String getString(ConstantPool constantPool, int index) {
        return constantPool.getStringAt(index);
    }

    /**
     * Gets String at an index in the CP.
     *
     * @param clazz The class to get the Constant Pool of.
     * @param index The index at which the String is located in the CP.
     * @return The String at the location in the CP.
     */
    public static String getString(Class clazz, int index) {
        return getString(getConstantPool(clazz), index);
    }

    /**
     * Gets the CP location of a String.
     *
     * @param constantPool The Constant Pool the String is located in.
     * @param string       The String to get the index of in the Constant Pool.
     * @return The found index. -1 if the String is not found.
     */
    public static int getStringIndex(ConstantPool constantPool, String string) {
        for (int index = 0; index <= constantPool.getSize(); index++)
            try {
                if (constantPool.getStringAt(index).equals(string))
                    return index;
            } catch (IllegalArgumentException ignored) {
            }
        return -1;
    }

    /**
     * Get the CP location of a String.
     *
     * @param clazz  The class to get the Constant Pool of.
     * @param string The String to get the index of in the Constant Pool.
     * @return The found index. -1 if the String is not found.
     */
    public static int getStringIndex(Class clazz, String string) {
        return getStringIndex(getConstantPool(clazz), string);
    }

    /**
     * Returns a Map of all of the Strings in the CP.
     *
     * @param constantPool The Constant Pool to retrieve the Strings from.
     * @return A Map of the Strings.
     */
    public static Map<Integer, String> getStrings(ConstantPool constantPool) {
        Map<Integer, String> strings = new HashMap<>();
        for (int index = 0; index <= constantPool.getSize(); index++)
            try {
                strings.put(index, constantPool.getStringAt(index));
            } catch (IllegalArgumentException ignored) {
            }
        return strings;
    }

    /**
     * Returns a Map of all of the Strings in the Class's CP.
     *
     * @param clazz The class to get the Constant Pool of.
     * @return A map of the Strings.
     */
    public static Map<Integer, String> getStrings(Class clazz) {
        return getStrings(getConstantPool(clazz));
    }

    /**
     * Gets the shallow size of the Class.
     *
     * @param clazz Class to get the shallow size of.
     * @return The size of the Object.
     */
    public static long getShallowSize(Class clazz) {
        if (SIZED_CLASSES.containsKey(clazz))
            return SIZED_CLASSES.get(clazz);
        Set<Field> fields = new HashSet<>();
        for (Field field : Reflect.getFieldsHierarchic(clazz))
            if (!Modifier.isStatic(field.getModifiers())) fields.add(field);

        long size = 0;
        for (Field field : fields) {
            long offset = unsafe.objectFieldOffset(field);
            size = Math.max(size, offset);
        }

        size = ((size >> 2) + 1) << 2; // ADDS PADDING
        SIZED_CLASSES.put(clazz, size);
        return size;
    }

    /**
     * Get shallow size of Object.
     *
     * @param object Object to get the shallow size of.
     * @return The shallow size of the Object.
     */
    public static long getShallowSize(Object object) {
        return getShallowSize(object.getClass());
    }

    /**
     * Gets internal Object class value.
     *
     * @param clazz Class to get the internal value of.
     * @return The internal value.
     */
    public static int getInternalClassValue(Class clazz) {
        return getInternalClassValue(Objects.createObject(clazz));
    }

    /**
     * Gets internal Object class value.
     *
     * @param object Object to get the internal class value of.
     * @return The internal class value.
     */
    public static int getInternalClassValue(Object object) {
        return unsafe.getInt(object, unsafe.addressSize());
    }

}
