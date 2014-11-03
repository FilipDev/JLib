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
    private Map<Entry<String, Class[]>, Method> methodCache = new HashMap<>();

    /**
     * Initializes Reflection object giving it an object value.
     *
     * @param object Object value to give to the Reflection object.
     */
    public Reflection(T object) {
        this.object = object;
        this.clazz = (Class<T>) object.getClass();
    }

    /**
     * Initialize without specifying the object to modify. Must use setObject to have any function other than interact with static members.
     *
     * @param clazz The class to give to the Reflection object.
     */
    public Reflection(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Gets the value of the Reflection object.
     *
     * @return The value of the Reflection object.
     */
    public T getObject() {
        return object;
    }

    /**
     * So that a single Reflection object can be used for multiple objects of the same type.
     *
     * @param object New object value to give to the Reflection object.
     */
    public void setObject(T object) {
        this.object = object;
    }

    /**
     * Returns Class value.
     *
     * @return Class value.
     */
    public Class<T> getClassValue() {
        return clazz;
    }

    /**
     * Gets the value of the field specified by the name.
     *
     * @param name Name of the field to get value of.
     * @return The retrieved field value. Null if field not found or if value is null.
     */
    public Object getValue(String name) {
        try {
            return Reflect.getField(clazz, name).get(object);
        } catch (IllegalAccessException ignored) {
        }
        return null;
    }

    /**
     * Sets the value of a field specified by the name.
     *
     * @param name   Name of the field to set value of.
     * @param object The new value of the field.
     */
    public void setValue(String name, Object object) {
        Field field = Reflect.getField(clazz, name);
        if (Modifier.isFinal(field.getModifiers())) Reflect.removeFinal(field);
        try {
            field.set(this.object, object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets static field value specified by the name.
     *
     * @param name Name of the field to get value of.
     * @return The value of the field.
     */
    public Object getStaticValue(String name) {
        try {
            return Reflect.getField(clazz, name).get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sets static field value specified by the name.
     *
     * @param name   Name of the field to set value of.
     * @param object The new value of the field.
     */
    public void setStaticValue(String name, Object object) {
        Field field = Reflect.getField(clazz, name);
        if (Modifier.isFinal(field.getModifiers())) Reflect.removeFinal(field);
        try {
            field.set(null, object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls a method given its name, the parameters it takes in, and the Objects to use as the argument.
     *
     * @param name       Name of the method to call.
     * @param paramTypes The classes of the parameters it accepts.
     * @param args       The arguments to call the method with.
     * @return The value returned from the method. Null if method return type is void.
     */
    public Object callMethod(String name, Class[] paramTypes, Object[] args) {
        Method method = Reflect.getMethod(clazz, name, paramTypes);
        if (method != null) {
            try {
                method.setAccessible(true);
                return method.invoke(args);
            } catch (IllegalAccessException | InvocationTargetException ignored) {
            }
        }
        return null;
    }

    /**
     * Calls a method given its name and the Objects to use as argument. Uses Objects' class values to determine which parameters types to use.
     *
     * @param name Name of the method to call.
     * @param args The arguments to call the method with, also used to get the parameter types of.
     * @return The value returned from the method. Null if method return type is void.
     */
    public Object callMethod(String name, Object... args) {
        return callMethod(name, Reflect.toClassArray(args), args);
    }
}
