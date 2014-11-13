package me.pauzen.jlib.objects;

import me.pauzen.jlib.classes.Classes;
import me.pauzen.jlib.hotspot.HotSpotDiagnostic;
import me.pauzen.jlib.reflection.Reflect;
import me.pauzen.jlib.unsafe.UnsafeProvider;
import sun.misc.Unsafe;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class Objects {

    private static final Unsafe   unsafe            = UnsafeProvider.getUnsafe();
    private static final int      ADDRESS_SIZE      = UnsafeProvider.getAddressSize();
    private static final Object[] objects           = new Object[2];
    private static final int      ARRAY_BASE_OFFSET = unsafe.arrayBaseOffset(Object[].class);

    /**
     * Primitive array class names.
     */
    private static Map<String, Class> CLASS_NAMES = new HashMap<>();

    static {
        CLASS_NAMES.put("[I", int[].class);
        CLASS_NAMES.put("[C", char[].class);
        CLASS_NAMES.put("[D", double[].class);
        CLASS_NAMES.put("[F", float[].class);
        CLASS_NAMES.put("[J", long[].class);
        CLASS_NAMES.put("[S", short[].class);
        CLASS_NAMES.put("[Z", boolean[].class);
        CLASS_NAMES.put("[B", byte[].class);
    }

    /**
     * Prevents instantiation.
     */
    private Objects() {
    }

    /**
     * Changes the class of an Object (this includes methods). Fields and their values remain unchanged.
     *
     * @param object1 The Object to change the class of.
     * @param object2 The Object of the desired class type of the first Object.
     */
    public static void setClass(Object object1, Object object2) {
        replaceAtOffset(object1, object2, ADDRESS_SIZE);
    }

    /**
     * Changes the class of an Object (this includes methods). Fields and their values remain unchanged.
     *
     * @param object1 The Object to change the class of.
     * @param clazz   The desired class to change the Object to.
     */
    public static void setClass(Object object1, Class clazz) {
        int value = unsafe.getInt(clazz, HotSpotDiagnostic.isArchitecture32() ? 64L : 84L);
        unsafe.putInt(object1, ADDRESS_SIZE, value);
    }

    public static void setClass(Object object, int value) {
        unsafe.putInt(object, ADDRESS_SIZE, value);
    }

    /**
     * Returns String in a style similar to JSON format.
     *
     * @param object Object to run toString on.
     * @return The String containing the Object fields and values.
     */
    public static String toString(Object object) {
        Class clazz = object.getClass();
        StringBuilder string = new StringBuilder(clazz.getName());
        string.append("{ ");
        Set<Field> fields = Reflect.getFields(clazz);
        int max = fields.size();
        int curr = 1;
        for (Field field : fields) {
            field.setAccessible(true);
            string.append(field.getName());
            string.append(" : ");
            try {
                string.append(field.get(object));
            } catch (IllegalAccessException e) {
                string.append("null");
            }
            if (curr != max) string.append(" , ");
        }
        string.append(" }");
        return string.toString();
    }

    /**
     * Instantiates an Object of the type given without calling the constructor nor any blocks. Allocates all memory needed for the Object.
     *
     * @param clazz The class of the Object.
     * @param <T>   The instantiated object type.
     * @return The new instantiated Object.
     */
    public static <T> T createObject(Class<T> clazz) {
        try {
            return (T) unsafe.allocateInstance(clazz);
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converts the Object to an int ID.
     *
     * @param object Object to get the ID of.
     * @return int of the Object's ID
     */
    public static int toIntID(Object object) {
        objects[0] = object;
        return unsafe.getInt(objects, ARRAY_BASE_OFFSET);
    }

    /**
     * Converts the Object to a long ID.
     *
     * @param object Object to get the ID of.
     * @return long of the Object's ID
     */
    public static long toLongID(Object object) {
        objects[0] = object;
        return unsafe.getLong(objects, ARRAY_BASE_OFFSET);
    }

    /**
     * Converts the int ID back to an Object.
     *
     * @param id int ID to get the Object from.
     * @return The object at the ID.
     */
    public static Object toObject(int id) {
        unsafe.putInt(objects, ARRAY_BASE_OFFSET, id);
        return objects[0];
    }

    /**
     * Converts the long ID back to an Object.
     *
     * @param id long ID to get the Object from.
     * @return The object at the ID.
     */
    public static Object toObject(long id) {
        unsafe.putLong(objects, ARRAY_BASE_OFFSET, id);
        return objects[0];
    }

    /**
     * Maps the Object's memory.
     *
     * @param object Object to map.
     * @return The String of the mapped Object.
     */
    public static String mapObject(Object object) {
        return mapObject(object, Classes.getShallowSize(object));
    }

    /**
     * Maps the Object's memory to a specified amount of bytes.
     *
     * @param object Object to map.
     * @param bytes  Amount of bytes to map.
     * @return The String of the mapped Object.
     */
    public static String mapObject(Object object, long bytes) {
        StringBuilder sb = new StringBuilder();
        sb.append(Long.toHexString(toLongID(object) * 8L));
        sb.append("\n     ");
        for (int x = 0; x <= 15; x++) sb.append(String.format("%1$02d", x, x)).append(" ");
        sb.append("\n\n");
        String hexByte;
        for (long x = 0; x < bytes; x++) {
            hexByte = (x % 16 == 15) ? "%2$02X" : "%2$02X ";
            hexByte = (x % 16 == 0) ? "%1$03d  %2$02X " : hexByte;
            sb.append(String.format(hexByte, x, unsafe.getByte(object, x)));
            if (x % 16 == 15 && x != bytes - 1) sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Reads directly from Object's memory.
     *
     * @param object Object to read.
     * @return The byte array value of the read Object.
     */
    public static byte[] readObject(Object object) {
        byte[] bytes = new byte[(int) Classes.getShallowSize(object)];
        for (int i = 0; i < bytes.length; i++) bytes[i] = unsafe.getByte(object, i);
        return bytes;
    }

    /**
     * Lists the Object's memory model in int form.
     *
     * @param object Object to print.
     * @return The int array of the printed Object.
     */
    public static int[] printInternals(Object object) {
        int ints = (int) Classes.getShallowSize(object);
        int[] values = new int[ints];
        for (int i = 0, x = 0; i < 256; i += 4, x++)
            System.out.println(i + " " + (values[x] = unsafe.getInt(object, i)));
        return values;
    }

    /**
     * Writes directly to Object's memory.
     *
     * @param object Object to write to.
     * @param bytes  The byte array value to write to the Object.
     */
    public static void writeObject(Object object, byte[] bytes) {
        for (int i = 0; i <= Classes.getShallowSize(object); i++) unsafe.putByte(object, i, bytes[i]);
    }

    /**
     * Replaces an int value at an object's offset with another object's at the same offset.
     *
     * @param object1 The Object to set the value of at the offset.
     * @param object2 The Object to get the value of the offset from.
     * @param offset  The offset to get and set the value at.
     */
    private static void replaceAtOffset(Object object1, Object object2, long offset) {
        unsafe.putInt(object1, offset, unsafe.getInt(object2, offset));
    }

    /**
     * Checks if an Object is a singleton, by getting the Object's class, getting its static fields,
     * then checks if any of those fields' values are equivalent to the Object.
     *
     * @param object Object to check for singleton values.
     * @return Whether or not the Object is a singleton.
     */
    public static boolean isSingleton(Object object) {
        try {
            Set<Field> fields1 = Reflect.getStaticFieldsHierarchic(object.getClass());
            for (Field field1 : fields1) {
                field1.setAccessible(true);
                if (field1.get(object) == object) return true;
            }
        } catch (IllegalAccessException ignored) {
            ignored.printStackTrace();
        }
        return false;
    }

    public static boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    /**
     * Clones the Object whether or not it implements Cloneable.
     * Reads the memory from input Object and writes to new Object.
     *
     * @param object The Object to clone.
     * @param <T>    The type of the Object.
     * @return The cloned Object.
     */
    public static <T> T shallowClone(T object) {
        Object newObject = createObject(object.getClass());
        if (newObject == null) {
            Class clazz = CLASS_NAMES.get(object.getClass().getName());
            int length = Array.getLength(object);

            if (clazz == null) newObject = ((Object[]) object).clone();
            else newObject = Array.newInstance(clazz, length);
            //TODO: Support for multi-dimensional arrays.
        }
        for (int x = 0; x <= Classes.getShallowSize(object); x += 4) replaceAtOffset(newObject, object, x);
        return (T) newObject;
    }

    /**
     * Clones the Object, all of its fields, all of the fields' fields, etc. No memory addresses are shared with the original Object.
     * The Object's data stays the exact same, but a new Object is made.
     *
     * @param object The Object to deep clone.
     * @param <T>    The type of the Object.
     * @return The deep cloned Object.
     */
    public static <T> T deepClone(T object) {
        Object newObject = createObject(object.getClass());
        for (Field field : Reflect.getFieldsHierarchic(object.getClass())) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                field.set(newObject, value == null ? null : (isSingleton(value) || isStatic(field)) ? value : shallowClone(value));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return (T) newObject;
    }
}
