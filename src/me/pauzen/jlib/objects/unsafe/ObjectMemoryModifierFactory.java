package me.pauzen.jlib.objects.unsafe;

public final class ObjectMemoryModifierFactory {

    private ObjectMemoryModifierFactory() {
    }

    public static ObjectMemoryModifier newModifier(Object object) {
        return new ObjectMemoryModifierImpl<>(object);
    }

}
