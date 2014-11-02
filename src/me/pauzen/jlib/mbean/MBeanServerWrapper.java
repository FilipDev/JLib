package me.pauzen.jlib.mbean;

import com.sun.jmx.mbeanserver.JmxMBeanServer;
import me.pauzen.jlib.reflection.Reflect;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;

public final class MBeanServerWrapper {

    private static final JmxMBeanServer server = (JmxMBeanServer) ManagementFactory.getPlatformMBeanServer();

    /**
     * Prevents initialization.
     */
    private MBeanServerWrapper() {
    }

    //TEST METHOD: IGNORE
    /*public static void testMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, ClassNotFoundException {
        ObjectName objectName1 = HotSpotDiagnostic.getInstance().getObjectName();

        Method method = MXBeanLookup.class.getDeclaredMethod("lookupFor", MBeanServerConnection.class);
        method.setAccessible(true);
        MXBeanLookup lookup = (MXBeanLookup) method.invoke(null, server);
        Reflection<MXBeanLookup> lookupReflection = new Reflection<>(lookup);

        Class weakIdentityHashMapClass = Class.forName("com.sun.jmx.mbeanserver.WeakIdentityHashMap");

        Field field = Reflect.getField(weakIdentityHashMapClass, "map");

        Map map = (Map) field.get(weakIdentityHashMapClass.cast(lookupReflection.getValue("mxbeanToObjectName")));

        for (Object entry : map.entrySet()) {
            System.out.println(((WeakReference) ((Map.Entry) entry).getKey()).get());
        }
    }*/


    /**
     * Invokes operation in MBeanServer.
     *
     * @param objectName ObjectName to invoke operation in.
     * @param method The operation to invoke in Method form.
     * @param args Object args used to invoke operation.
     * @return Object returned by invoking operation.
     */
    public static Object invoke(ObjectName objectName, Method method, Object... args) {
        try {
            return server.invoke(objectName, method.getName(), args, Reflect.toStringArray(method.getParameterTypes()));
        } catch (ReflectionException | MBeanException | InstanceNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Invokes operation in MBeanServer.
     *
     * @param objectName ObjectName to invoke operation in.
     * @param methodName The operation name to invoke.
     * @param args Object args used to invoke operation.
     * @return Object returned by invoking operation.
     */
    public static Object invoke(ObjectName objectName, String methodName, Object... args) {
        try {
            return server.invoke(objectName, methodName, args, Reflect.toStringArray(Reflect.toClassArray(args)));
        } catch (InstanceNotFoundException | MBeanException | ReflectionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
