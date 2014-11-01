package me.pauzen.jlib.reflection;

import me.pauzen.jlib.collections.Entry;
import me.pauzen.jlib.unsafe.UnsafeProvider;
import sun.misc.Unsafe;
import sun.reflect.Reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public final class Reflect {

    private static Unsafe                               unsafe                         = UnsafeProvider.getUnsafe();
    private static Map<Class, Set<Field>>               HIERARCHIC_CACHED_CLASS_FIELDS = new HashMap<>();
    private static Map<Class, Set<Field>>               CACHED_CLASS_FIELDS            = new HashMap<>();
    private static Map<Map.Entry<Class, String>, Field> CACHED_FIELDS                  = new HashMap<>();

    private Reflect() {
    }

    public static void removeFinal(Field field) {
        try {
            Field modifier = Field.class.getDeclaredField("modifiers");
            modifier.setAccessible(true);
            modifier.set(field, field.getModifiers() & ~Modifier.FINAL);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Class[] toClassArray(Object[] objects) {
        Class[] classes = new Class[objects.length];
        for (int i = 0; i < objects.length; i++) classes[i] = objects[i].getClass();
        return classes;
    }

    public static String[] toStringArray(Class[] classes) {
        String[] strings = new String[classes.length];
        for (int i = 0; i < classes.length; i++) strings[i] = classes[i].getCanonicalName();
        return strings;
    }

    /**
     * Finds the field in the class hierarchy.
     *
     * @param clazz The class where to start the search.
     * @param name  The name of the field to find.
     * @return Either the found field, or a null value if one is not found.
     */
    public static Field getField(Class clazz, String name) {
        if (CACHED_FIELDS.containsKey(new Entry<>(clazz, name))) return CACHED_FIELDS.get(new Entry<>(clazz, name));
        for (Field field : getFieldsHierarchic(clazz))
            if (field.getName().equals(name)) {
                field.setAccessible(true);
                CACHED_FIELDS.put(new Entry<>(clazz, name), field);
                return field;
            }
        return null;
    }

    /**
     * Returns all fields in the class and all its super classes.
     *
     * @param clazz The class to return all found fields from.
     * @return A Set of all found fields in the class.
     */
    public static Set<Field> getFieldsHierarchic(Class clazz) {
        if (HIERARCHIC_CACHED_CLASS_FIELDS.containsKey(clazz)) return HIERARCHIC_CACHED_CLASS_FIELDS.get(clazz);
        Set<Field> fields = new HashSet<>();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Set<Field> fields1 = getFields(clazz);
            Collections.addAll(fields, fields1.toArray(new Field[fields1.size()]));
        }
        HIERARCHIC_CACHED_CLASS_FIELDS.put(clazz, fields);
        return fields;
    }

    public static Set<Field> getFields(Class clazz) {
        if (CACHED_CLASS_FIELDS.containsKey(clazz)) return CACHED_CLASS_FIELDS.get(clazz);
        Set<Field> fields = new HashSet<>();
        Collections.addAll(fields, clazz.getDeclaredFields());
        CACHED_CLASS_FIELDS.put(clazz, fields);
        return fields;
    }

    /**
     * Returns all static fields within a class and all its super classes.
     *
     * @param clazz The class to search for static fields.
     * @return A Set of the static fields in the class.
     */
    public static Set<Field> getStaticFieldsHierarchic(Class clazz) {
        Set<Field> fields = new HashSet<>();
        for (Field field : getFieldsHierarchic(clazz))
            if (Modifier.isStatic(field.getModifiers())) fields.add(field);
        return fields;
    }

    public static Set<Field> getStaticFields(Class clazz) {
        Set<Field> fields = new HashSet<>();
        for (Field field : getFields(clazz))
            if (Modifier.isStatic(field.getModifiers())) fields.add(field);
        return fields;
    }

    public static Class[] getCallerClasses() {
        ArrayList<Class> classes = new ArrayList<>();
        Class currentClass = Reflect.class;
        for (int i = 2; currentClass != null; i++) {
            currentClass = Reflection.getCallerClass(i);
            classes.add(currentClass);
        }
        return classes.toArray(new Class[classes.size()]);
    }
}
