package me.pauzen.jlib.objects;

public final class Testing {

    private Testing() {
    }

    /**
     * Tests whether an Object equals any other Object in array. Uses .equals() method.
     *
     * @param object  Object to test.
     * @param objects Objects to test against.
     * @return The Object equal to Object tested. Null is not found.
     */
    public static Object equalsAny(Object object, Object... objects) {
        for (Object testingObject : objects)
            if (testingObject.equals(object)) return testingObject;
        return null;
    }
}
