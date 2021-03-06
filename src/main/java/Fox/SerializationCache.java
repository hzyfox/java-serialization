package Fox;

import Fox.util.GrowableObjectArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

//引入Cache，不引入Cache每次只能从注册类中new一个出来，至于为什么不引用注册类中的对象，见注册类注释
public class SerializationCache {
    static final Logger log = LoggerFactory.getLogger(SerializationCache.class);
    final HashMap<Class<?>, Serialization<?>> serializations = new HashMap<Class<?>, Serialization<?>>();
    final GrowableObjectArray<Serialization<?>>
            serializationIndex =
            new GrowableObjectArray<Serialization<?>>(Serialization.class);
    SerializationRegistry serializationRegistry;

    public SerializationCache(){
        serializationRegistry = new SerializationRegistry();
    }

    public SerializationCache(SerializationRegistry serializationRegistry) {
        if (serializationRegistry == null) {
            this.serializationRegistry = new SerializationRegistry();
        } else {
            this.serializationRegistry = serializationRegistry;
        }
    }

    public final <T> Serialization<T> getSerialization(final Class<T> targetClass) {
        // we do a contains check to distinguish between nulls in the cases where
        // either a) we have not checked for targetClass, and b) we did check for targetClass
        // but no serialization is registered for it
        Serialization<?> serialization = serializations.get(targetClass);
        if (serialization == null) {
            log.trace("{}: retrieving serialization: {}", this, targetClass);
            serialization = serializationRegistry.newSerialization(targetClass);

            assert (serialization != null && serialization.getCacheIndex() >= 0);

            cache(serialization, targetClass);
        }
        @SuppressWarnings("unchecked")
        final Serialization<T> typedSerialization = (Serialization<T>) serialization;
        return typedSerialization;
    }

    public final Serialization<?> getSerialization(final int id) {
        Serialization<?> serialization = null;
        if (0 <= id && id < serializationIndex.size()) {
            serialization = serializationIndex.get(id);
        }
        if (serialization == null) {
            log.trace("{}: retrieving serialization: {}", this, id);
            serialization = serializationRegistry.newSerialization(id);
            cache(serialization, null);
        }
        return serialization;
    }

    private final <T> void cache(final Serialization<?> serialization, final Class<T> targetClass) {
        if (serialization == null) {
            throw new IllegalArgumentException();
        } else {
            serializationIndex.set(serialization.getCacheIndex(), serialization);
            if (targetClass != null) {
                serializations.put(targetClass, serialization);
            }
            log.trace("{}: cached {}: id: {} serialization: {}", this, targetClass,
                    serialization.getCacheIndex(),
                    serialization);
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(this.getClass().getSimpleName());
        string.append(".");
        return string.toString();
    }
}

