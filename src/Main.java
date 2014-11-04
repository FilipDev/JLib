import me.pauzen.jlib.reflection.Reflection;

import javax.management.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;

public class Main {

    public static void main(String[] args) throws MalformedObjectNameException, MBeanException, InstanceNotFoundException, ReflectionException, IntrospectionException, AttributeNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstanceAlreadyExistsException, IOException, NotCompliantMBeanException, ClassNotFoundException {
        byte[] bytes = Files.readAllBytes(new File("C:\\Users\\Administrator\\Desktop\\Updater.class").toPath());
        Reflection<ClassLoader> reflection = new Reflection<>(ClassLoader.getSystemClassLoader());
        Class c = (Class) reflection.callMethod("defineClass", bytes, 0, bytes.length);
        System.out.println(c);
        //USED FOR TESTING
    }

}
