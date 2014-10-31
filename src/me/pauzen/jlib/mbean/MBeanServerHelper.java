package me.pauzen.jlib.mbean;

import me.pauzen.jlib.reflection.Reflect;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;

public final class MBeanServerHelper {

    private static final MBeanServer server = ManagementFactory.getPlatformMBeanServer();

    private MBeanServerHelper() {
    }

    public static Object invoke(MBeanObject mBeanObject, Method method, Object... args) {
        try {
            ObjectName objectName = mBeanObject.getObjectName();

            return server.invoke(objectName, method.getName(), args, Reflect.toStringArray(method.getParameterTypes()));
        } catch (ReflectionException | MBeanException | InstanceNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invoke(MBeanObject mBeanObject, String methodName, Object... args) {
        try {
            ObjectName objectName = mBeanObject.getObjectName();

            return server.invoke(objectName, methodName, args, Reflect.toStringArray(Reflect.toClassArray(args)));
        } catch (InstanceNotFoundException | MBeanException | ReflectionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
