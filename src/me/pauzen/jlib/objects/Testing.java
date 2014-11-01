package me.pauzen.jlib.objects;

public final class Testing {

    private Testing() {
    }

    public static Object equalsAny(Object object, Object... objects) {
        for (Object testingObject : objects)
            if (testingObject.equals(object)) return testingObject;
        return null;
    }
}
