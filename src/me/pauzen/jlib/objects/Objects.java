package me.pauzen.jlib.objects;

import me.pauzen.jlib.unsafe.UnsafeProvider;
import sun.misc.Unsafe;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public final class Objects {

    private static final Unsafe   unsafe            = UnsafeProvider.getUnsafe();
    private static final int      ADDRESS_SIZE      = UnsafeProvider.getAddressSize();
    private static final Object[] objects           = new Object[2];
    private static final int      ARRAY_BASE_OFFSET = unsafe.arrayBaseOffset(Object[].class);

    /*
    EXPERIMENTAL CODE
    private static boolean COMPRESSED_OOPS = false;
    private static int     OOP_SIZE        = unsafe.addressSize();
    private static int     SHIFT           = 1;

    static {
        long offset1 = unsafe.objectFieldOffset(getField(CompressedOOPContainer.class, "object1"));
        long offset2 = unsafe.objectFieldOffset(getField(CompressedOOPContainer.class, "object2"));
        OOP_SIZE = (int) Math.abs(offset1 - offset2);

        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            ObjectName mbean = new ObjectName("com.sun.management:type=HotSpotDiagnostic");
            CompositeDataSupport compressedOopsValue = (CompositeDataSupport) server.invoke(mbean, "getVMOption", new Object[]{"UseCompressedOops"}, new String[]{"java.lang.String"});
            COMPRESSED_OOPS = Boolean.valueOf(compressedOopsValue.get("value").toString());

            for (int x = 3; (x >>= 1) != 0; SHIFT++);
        } catch (MalformedObjectNameException | InstanceNotFoundException | ReflectionException | MBeanException e) {
            e.printStackTrace();
        }
    }*/
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

    private static Map<Class, Set<Field>> CACHED_FIELDS = new HashMap<>();

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
    public static void replaceObjectType(Object object1, Object object2) {
        unsafe.putInt(object1, ADDRESS_SIZE, unsafe.getInt(object2, ADDRESS_SIZE));
    }

    /**
     * Changes the class of an Object (this includes methods). Fields and their values remain unchanged.
     *
     * @param object1 The Object to change the class of.
     * @param clazz   The desired class to change the Object to.
     */
    public static void replaceObjectType(Object object1, Class clazz) {
        try {
            unsafe.putInt(object1, ADDRESS_SIZE, unsafe.getInt(createObject(clazz, false), ADDRESS_SIZE));
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns String in standard IntelliJ Idea toString method format.
     *
     * @param object Object to run toString on.
     * @return The String containing the Object fields and values.
     */
    public static String toString(Object object) {
        Class clazz = object.getClass();
        StringBuilder string = new StringBuilder(clazz.getName());
        string.append("{");
        Field[] fields = clazz.getDeclaredFields();
        int max = fields.length;
        int curr = 1;
        for (Field field : fields) {
            field.setAccessible(true);
            string.append(field.getName());
            string.append("=");
            try {
                string.append(field.get(object));
            } catch (IllegalAccessException e) {
                string.append("null");
            }
            if (curr != max) string.append(", ");
        }
        string.append("}");
        return string.toString();
    }

    /**
     * Instantiates an Object of the type given without calling the constructor nor any blocks. Allocates all memory needed for the Object.
     *
     * @param clazz The class of the Object.
     * @param <T>   The instantiated object.
     * @return The new instantiated Object.
     * @throws InstantiationException
     */
    public static <T> T createObject(Class<T> clazz, boolean callConstructor) throws InstantiationException, IllegalAccessException {
        return callConstructor ? clazz.newInstance() : (T) unsafe.allocateInstance(clazz);
    }

    /**
     * Sets the value of a field in an Object, regardless of modifiers.
     *
     * @param object Object where the value change should be applied.
     * @param field  The field where the value should be put.
     * @param value  The value to place at the Field in the Object.
     */
    public static void setField(Object object, Field field, Object value) {
        unsafe.putObject(object, unsafe.objectFieldOffset(field), value);
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
    public static Object getObjectFromID(int id) {
        unsafe.putInt(objects, ARRAY_BASE_OFFSET, id);
        return objects[0];
    }

    /**
     * Converts the long ID back to an Object.
     *
     * @param id long ID to get the Object from.
     * @return The object at the ID.
     */
    public static Object getObjectFromID(long id) {
        unsafe.putLong(objects, ARRAY_BASE_OFFSET, id);
        return objects[0];
    }

    /**
     * Finds the field in the class hierarchy.
     *
     * @param clazz The class where to start the search.
     * @param name  The name of the field to find.
     * @return Either the found field, or a null value if one is not found.
     */
    public static Field getField(Class clazz, String name) {
        Class currentClass = clazz;
        for (; currentClass != Object.class; currentClass = currentClass.getSuperclass()) {
            for (Field field : currentClass.getDeclaredFields())
                if (field.getName().equals(name)) return field;
        }
        return null;
    }

    /**
     * Returns all fields in the class and all its super classes.
     *
     * @param clazz The class to return all found fields from.
     * @return A Set of all found fields in the class.
     */
    public static Set<Field> getFields(Class clazz) {
        Set<Field> fields = new HashSet<>();
        Class currentClass = clazz;
        if (CACHED_FIELDS.containsKey(currentClass))
            return CACHED_FIELDS.get(currentClass);
        for (; currentClass != Object.class; currentClass = currentClass.getSuperclass()) {
            Collections.addAll(fields, currentClass.getDeclaredFields());
            CACHED_FIELDS.put(currentClass, fields);
        }
        return fields;
    }

    /**
     * Returns all static fields within a class and all its super classes.
     *
     * @param clazz The class to search for static fields.
     * @return A Set of the static fields in the class.
     */
    public static Set<Field> getStaticFields(Class clazz) {
        Set<Field> fields = new HashSet<>();
        for (Field field : getFields(clazz))
            if (Modifier.isStatic(field.getModifiers())) fields.add(field);
        return fields;
    }

    /**
     * Maps the Object's memory to 256 bytes.
     *
     * @param object Object to map.
     * @return The String of the mapped Object.
     */
    public static String mapObject(Object object) {
        return mapObject(object, 256);
    }

    /**
     * Maps the Object's memory to a specified amount of bytes.
     *
     * @param object Object to map.
     * @param bytes  Amount of bytes to map.
     * @return The String of the mapped Object.
     */
    public static String mapObject(Object object, int bytes) {
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
     * Lists the Object's memory model in int form.
     *
     * @param object Object to print.
     * @param ints   Amount of ints to print.
     * @return The int array of the printed Object.
     */
    public static int[] printInternals(Object object, int ints) {
        int[] values = new int[ints];
        for (int i = 0, x = 0; i < ints; i += 4, x++) values[x] = unsafe.getInt(object, i);
        return values;
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
        Object newObject = null;
        try {
            newObject = createObject(object.getClass(), false);
        } catch (InstantiationException e) {
            Class clazz = CLASS_NAMES.get(object.getClass().getName());
            int length = Array.getLength(object);

            if (clazz == null) newObject = ((Object[]) object).clone();
            else newObject = Array.newInstance(clazz, length);
            //TODO: Support for multi-dimensional arrays.
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (int x = 0; x <= getShallowSize(object); x += 4) unsafe.putInt(newObject, x, unsafe.getInt(object, x));
        return (T) newObject;
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
            Set<Field> fields1 = getStaticFields(object.getClass());
            for (Field field1 : fields1) {
                field1.setAccessible(true);
                if (field1.get(object) == object)
                    return true;
            }
        } catch (IllegalAccessException ignored) {
        }
        return false;
    }

    /**
     * Clones the Object, all of its fields, all of the fields' fields, etc. No memory addresses are shared with the original Object.
     * The Object's data stays the exact same, but a new Object is made.
     *
     * @param object The Object to deep clone.
     * @param <T>    The type of the Object.
     * @return The deep cloned Object.
     * @throws InstantiationException Instantiation of the new Object may err. Very unlikely.
     */
    public static <T> T deepClone(T object) throws InstantiationException, IllegalAccessException {
        Object newObject = createObject(object.getClass(), false);
        for (Field field : getFields(object.getClass())) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                field.set(newObject, isSingleton(value) ? value : shallowClone(value));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return (T) newObject;
    }

    /**
     * Gets the shallow size of the Object.
     *
     * @param object Object to get the shallow size of.
     * @return The size of the Object.
     */
    public static long getShallowSize(Object object) {
        Class currentClass = object.getClass();
        Set<Field> fields = new HashSet<>();
        for (; currentClass != Object.class; currentClass = currentClass.getSuperclass())
            for (Field field : getFields(currentClass)) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    fields.add(field);
                }
            }

        long size = 0;
        for (Field field : fields) {
            long offset = unsafe.objectFieldOffset(field);
            size = Math.max(size, offset);
        }

        return ((size >> 2) + 1) << 2; // ADDS PADDING
    }

    /*
    private static long toNativeAddress(long address) {
        return COMPRESSED_OOPS ? address << SHIFT : address;
    }

    private static long revertNativeAddress(long address) {
        return COMPRESSED_OOPS ? address >> SHIFT : address;
    }

    private static long normalize(int value) {
        if (value >= 0) return value;
        return (~0L >>> 32) & value;
    }

    private static long getAddress(Object object) {
        return getAddress(object, 0);
    }

    private static long getAddress(Object object, int offset) {
        objects[0] = object;

        long maxUnsignedInt = 0xFFFFFFFF;
        long address = OOP_SIZE == 4 ? unsafe.getInt(objects, ARRAY_BASE_OFFSET & maxUnsignedInt + offset) : unsafe.getLong(objects, ARRAY_BASE_OFFSET + offset);
        return toNativeAddress(address);
    }

    private static class CompressedOOPContainer {
        Object object1 = new Object();
        Object object2 = new Object();
    }
    */

}
