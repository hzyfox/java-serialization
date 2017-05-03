package Serialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * create with Serialization
 * USER: husterfox
 */
public abstract class Serialization<T> implements Serializer<T>, Deserializer<T> {
    static final Logger log = LoggerFactory.getLogger(Serialization.class);
    private final Class<?> serializationClass;
    int cacheIndex = -1;


    public Serialization(Class<?> serializationClass) {
        if (serializationClass == null) {
            throw new NullPointerException();
        }
        this.serializationClass = serializationClass;
    }

    public Serialization() {
        this.serializationClass = null;
    }


    final Serialization<T> clonedCopy() {
        final Serialization<T> clone = (Serialization<T>) clone();
        clone.cacheIndex = cacheIndex;
        return clone;
    }


    public final int getCacheIndex() {
        return cacheIndex;
    }


    @Override
    public final Class<?> getSerializableClass() {
        return serializationClass;
    }

    public boolean isSerializable(Class<?> targetClass) {
        return serializationClass == null ? false : targetClass == serializationClass;
    }


    @Override
    public final Class<?> getDeserializableClass() {
        return serializationClass;
    }

    public boolean isDeserializable(Class<?> targetClass) {
        return serializationClass == null ? false : targetClass == serializationClass;
    }


    @Override
    public abstract Serialization<T> clone();
}

