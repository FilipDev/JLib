package me.pauzen.jlib.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class Reflect {

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
        for (int i = 0; i < objects.length; i++)
            classes[i] = objects[i].getClass();
        return classes;
    }
}
