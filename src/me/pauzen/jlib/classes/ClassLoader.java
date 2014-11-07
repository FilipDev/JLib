package me.pauzen.jlib.classes;

import me.pauzen.jlib.unsafe.UnsafeProvider;
import sun.misc.Unsafe;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class ClassLoader {

    private static Unsafe              unsafe = UnsafeProvider.getUnsafe();
    //public         Map<String, Map<String, Class>> CREATED_CLASSES = new HashMap<>();
    private        Map<String, byte[]> loaded = new HashMap<>();

    /**
     * Returns a Class that had been loaded into memory.
     *
    // * @param name The name of the Class.
     * @return The found Class object.
     */
    /*public Class getLoadedClass(String name) {
        Map<String, Class> map;
        if ((map = CREATED_CLASSES.get(toPackageName(name))) != null)
            if (map.containsKey(toSimpleName(name)))
                return CREATED_CLASSES.get(toPackageName(name)).get(toSimpleName(name));
        throw new NullPointerException("Class has not been loaded into memory.");
    }

    public boolean isLoaded(String name) {
        return CREATED_CLASSES.containsKey(name);
    }*/
    public Map<String, Set<Class>> loadJar(File file) throws IOException, ClassNotFoundException {
        Map<String, Set<Class>> classes = new HashMap<>();
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> entries = jarFile.entries();
        for (JarEntry entry = entries.nextElement(); entries.hasMoreElements(); entry = entries.nextElement()) {
            loadEntry(jarFile, entry);
        }
        for (Map.Entry<String, byte[]> entry : loaded.entrySet()) {
            //if (!isLoaded(entry.getKey())) {
            try {
                Class clazz = tryLoad(entry.getValue());
                String packageName = getPackageName(clazz);
                if (!classes.containsKey(packageName))
                    classes.put(packageName, new HashSet<Class>());
                classes.get(packageName).add(clazz);
            } catch (LinkageError ignored) {
            }
            //}
        }
        return classes;
    }

    private String toSimpleName(String string) {
        String[] split;
        split = string.split("\\.");
        String className = split[split.length - 1];
        if (className.contains("$"))
            split = className.split("$");
        return split[split.length - 1];
    }

    private String toPackageName(String string) {
        string = string.replace("$", ".");
        String[] split = string.split("\\.");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < split.length - 1; i++) {
            stringBuilder.append(split[i]);
            if (i != split.length - 2) stringBuilder.append(".");
        }
        return stringBuilder.toString();
    }

    private String getPackageName(Class clazz) {
        return toPackageName(clazz.getName());
    }

    private Class tryLoad(byte[] value) {
        try {
            return loadClass(value);
        } catch (NoClassDefFoundError e) {
            //System.out.println(e.getMessage());
            //if (loaded.get(e.getMessage()) == null) System.out.println(loaded);
            return tryLoad(loaded.get(e.getMessage()));
        }
    }

    private Class loadEntry(JarFile jarFile, JarEntry entry) throws IOException, ClassNotFoundException {
        System.out.println(entry.getName());
        if (entry.getName().equals("org/bukkit/craftbukkit/v1_7_R4/entity/CraftOcelot.class")) {
            System.out.println("found it!");
            System.out.println(isClass(entry.getName()));
            System.out.println(entry.isDirectory());
        }
        if (!entry.isDirectory() && isClass(entry.getName())) {
            String name = entry.getName().substring(0, entry.getName().indexOf(".class"));
            /*try {
                Class.forName(name);
                return null;
            } catch (ClassNotFoundException ignored) {
            }*/
            DataInputStream dataInputStream = new DataInputStream(jarFile.getInputStream(entry));
            byte[] bytes = new byte[dataInputStream.available()];
            try {
                dataInputStream.readFully(bytes);
            } catch (EOFException ignored) {
            }
            dataInputStream.close();
            loaded.put(name, bytes);
        }
        return null;
    }

    private Class loadClass(String name, byte[] data) {
        Class clazz = unsafe.defineClass(name, data, 0, data.length);
        /*String packageName = getPackageName(clazz);
        Map<String, Class> map;
        if ((map = CREATED_CLASSES.get(packageName)) == null)
            CREATED_CLASSES.put(packageName, new HashMap<String, Class>());
        if (map != null) map.put(toSimpleName(clazz.getName()), clazz);*/
        return clazz;
    }

    /**
     * Loads a class into memory given its contents.
     *
     * @param data Byte array of the contents of the class file.
     * @return The loaded Class object.
     */
    public Class loadClass(byte[] data) {
        return loadClass(null, data);
    }

    /**
     * Checks whether a string is the name of a class.
     *
     * @param name The string to check.
     * @return Whether or not the string is the name of a class.
     */
    private boolean isClass(String name) {
        try {
            return name.split("\\.")[1].equals("class");
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Loads a class file into memory.
     *
     * @param file The Class file to convert to byte array.
     * @return The loaded Class object.
     * @throws IOException
     */
    public Class loadClass(File file) throws IOException {
        if (file.exists())
            if (isClass(file.getName())) return loadClass(Files.readAllBytes(file.toPath()));
        throw new IllegalArgumentException("Incorrect file.");
    }
}
