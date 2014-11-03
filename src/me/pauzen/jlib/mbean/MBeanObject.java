package me.pauzen.jlib.mbean;

import me.pauzen.jlib.reflection.Reflect;

import javax.management.ObjectName;
import java.lang.reflect.Method;

public abstract class MBeanObject {

    private final ObjectName objectName;

    public MBeanObject(ObjectName objectName) {
        this.objectName = objectName;
    }

    /**
     * Gets ObjectName value.
     *
     * @return ObjectName value.
     */
    public ObjectName getObjectName() {
        return objectName;
    }

    /**
     * Invokes a
     *
     * @param method
     * @param args
     * @return
     */
    public Object invoke(Method method, Object... args) {
        return MBeanServerWrapper.invoke(objectName, method, args, Reflect.toStringArray(method.getParameterTypes()));
    }

    public Object invoke(String methodName, Object... args) {
        return MBeanServerWrapper.invoke(objectName, methodName, args, Reflect.toStringArray(Reflect.toClassArray(args)));
    }
}
