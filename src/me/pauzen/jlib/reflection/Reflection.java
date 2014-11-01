package me.pauzen.jlib.reflection;

import me.pauzen.jlib.collections.Entry;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class Reflection<T> {

    private T        object;
    private Class<T> clazz;
    private Map<Map.Entry<String, Class[]>, Method> methodCache = new HashMap<>();

    public Reflection(T object) {
        this.object = object;
        this.clazz = (Class<T>) object.getClass();
    }

    public Reflection(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public Class<T> getClassValue() {
        return clazz;
    }

    public Object getValue(String name) {
        try {
            return Reflect.getField(clazz, name).get(object);
        } catch (IllegalAccessException ignored) {
        }
        return null;
    }

    public void setValue(String name, Object object) {
        Field field = Reflect.getField(clazz, name);
        if (Modifier.isFinal(field.getModifiers())) Reflect.removeFinal(field);
        try {
            field.set(this.object, object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Object getStaticValue(String name) {
        try {
            return Reflect.getField(clazz, name).get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setStaticValue(String name, Object object) {
        Field field = Reflect.getField(clazz, name);
        if (Modifier.isFinal(field.getModifiers())) Reflect.removeFinal(field);
        try {
            field.set(null, object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Object callMethod(String name, Class[] paramTypes, Object[] args) {
        Method method = null;
        Map.Entry<String, Class[]> entry = new Entry<>(name, paramTypes);
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

    public Object callMethod(String name, Object... args) {
        return callMethod(name, Reflect.toClassArray(args), args);
    }
}
