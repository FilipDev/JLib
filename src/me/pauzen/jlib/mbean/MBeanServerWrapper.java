package me.pauzen.jlib.mbean;

import com.sun.jmx.mbeanserver.JmxMBeanServer;
import com.sun.jmx.mbeanserver.MXBeanLookup;
import me.pauzen.jlib.mbean.hotspot.HotSpotDiagnostic;
import me.pauzen.jlib.reflection.Reflect;
import me.pauzen.jlib.reflection.Reflection;

import javax.management.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class MBeanServerWrapper {

    private static final JmxMBeanServer server = (JmxMBeanServer) ManagementFactory.getPlatformMBeanServer();

    private MBeanServerWrapper() {
    }


    public static void testMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, ClassNotFoundException {
        ObjectName objectName = new ObjectName("me.pauzen.classes:type=InternalClass");
        ObjectName objectName1 = HotSpotDiagnostic.getInstance().getObjectName();

        Method method = MXBeanLookup.class.getDeclaredMethod("lookupFor", MBeanServerConnection.class);
        method.setAccessible(true);
        MXBeanLookup lookup = (MXBeanLookup) method.invoke(null, server);
        Reflection<MXBeanLookup> lookupReflection = new Reflection<>(lookup);

        System.out.println(lookupReflection.callMethod("objectNameToMXBean", objectName1, sun.management.HotSpotDiagnostic.class));

        /*Class weakIdentityHashMapClass = Class.forName("com.sun.jmx.mbeanserver.WeakIdentityHashMap");

        Field field = Reflect.getField(weakIdentityHashMapClass, "map");

        Map map = (Map) field.get(weakIdentityHashMapClass.cast(lookupReflection.getValue("mxbeanToObjectName")));

        for (Object entry : map.entrySet()) {
            System.out.println(((WeakReference) ((Map.Entry) entry).getKey()).get());
        }*/
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
