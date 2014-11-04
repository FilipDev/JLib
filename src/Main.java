import me.pauzen.jlib.random.RandomRandom;

import javax.management.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws MalformedObjectNameException, MBeanException, InstanceNotFoundException, ReflectionException, IntrospectionException, AttributeNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstanceAlreadyExistsException, IOException, NotCompliantMBeanException, ClassNotFoundException {
        RandomRandom randomRandom = new RandomRandom(200);
        System.out.println(randomRandom.nextInt());
        //USED FOR TESTING
    }

}
