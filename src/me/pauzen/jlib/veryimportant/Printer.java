package me.pauzen.jlib.veryimportant;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Printer {

    public static void print(String stringToPrint) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        Field outField = getOutField();
        if (!isNull(outField)) {
            PrintStream out = (PrintStream) outField.get(null);
            Method method = getPrintLineMethod();
            callPrintLineMethodInvokeMethod(method, stringToPrint);
        }
    }

    public static Field getOutField() throws NoSuchFieldException {
        return System.class.getDeclaredField("out");
    }

    public static Method getPrintLineMethod() throws NoSuchMethodException {
        return PrintStream.class.getDeclaredMethod("println", String.class);
    }

    public static void callPrintLineMethodInvokeMethod(Method method, String argument) throws InvocationTargetException, IllegalAccessException {
        method.invoke(argument);
    }

    public static boolean isNull(Object object) {
        return object == null;
    }
}
