import me.pauzen.jlib.mbean.MBeanServerWrapper;
import me.pauzen.jlib.mbean.hotspot.HotSpotDiagnostic;

import javax.management.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws MalformedObjectNameException, MBeanException, InstanceNotFoundException, ReflectionException, IntrospectionException, AttributeNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstanceAlreadyExistsException, IOException, NotCompliantMBeanException, ClassNotFoundException {

        MBeanServerWrapper.testMethod();



        System.out.println(HotSpotDiagnostic.getInstance().getAlignment());

    }

}
