package me.pauzen.jlib.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class Reflection<T> {

    private T        object;
    private Class<T> clazz;
    private Map<String, Field>                       fieldCache  = new HashMap<>();
    private Map<Map.Entry<String, Object[]>, Method> methodCache = new HashMap<>();

    public Reflection(T object) {
        this.object = object;
        this.clazz = (Class<T>) object.getClass();
    }

    public T getObject() {
        return object;
    }

    public Class<T> getClassValue() {
        return clazz;
    }

    public Object getValue(String name) {
        if (fieldCache.containsKey(name))
            return fieldCache.get(name);
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            fieldCache.put(name, field);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setValue(String name, Object object) {
        Field field = null;
        if (fieldCache.containsKey(name))
            field = fieldCache.get(name);
        else {
            try {
                field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                if (Modifier.isFinal(field.getModifiers()))
                    Reflect.removeFinal(field);
                fieldCache.put(name, field);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        if (field != null) {
            try {
                field.set(this.object, object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Object callMethod(String name, Object... args) {
        Method method = null;
        AbstractMap.SimpleEntry entry = new AbstractMap.SimpleEntry<>(name, Reflect.toClassArray(args));
        if (methodCache.containsKey(entry)) method = methodCache.get(entry);
        else {
            try {
                method = clazz.getDeclaredMethod(name, (Class<?>[]) entry.getValue());
                method.setAccessible(true);
                methodCache.put(entry, method);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        if (method != null) {
            try {
                return method.invoke(args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
