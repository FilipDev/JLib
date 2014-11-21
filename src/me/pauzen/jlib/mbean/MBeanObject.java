package me.pauzen.jlib.mbean;

import me.pauzen.jlib.reflection.ReflectionFactory;

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
     * Invokes an operation in the MBeanServer.
     *
     * @param method The method to invoke.
     * @param args   Arguments to use.
     * @return Returned object from calling operation.
     */
    public Object invoke(Method method, Object... args) {
        return MBeanServerWrapper.invoke(objectName, method, args, ReflectionFactory.toStringArray(method.getParameterTypes()));
    }

    /**
     * Invokes an operation given its name.
     *
     * @param methodName Name of the operation to invoke.
     * @param args       Arguments to use.
     * @return Returned object from calling operation.
     */
    public Object invoke(String methodName, Object... args) {
        return MBeanServerWrapper.invoke(objectName, methodName, args, ReflectionFactory.toStringArray(ReflectionFactory.toClassArray(args)));
    }
}
