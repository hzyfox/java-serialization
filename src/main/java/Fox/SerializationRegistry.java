package Fox;

import Fox.Primitive.KryoSerialization;
import Fox.Primitive.NullReferenceSerialization;
import com.esotericsoftware.kryo.Kryo;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Char;
import scala.Double;
import scala.Short;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * create with Serialization
 * USER: husterfox
 */
public final class SerializationRegistry implements Iterable<Serialization<?>> {
    static final Logger log = LoggerFactory.getLogger(SerializationRegistry.class);
    private final ConcurrentHashMap<Class<?>, Serialization<?>>
            registry = new ConcurrentHashMap<Class<?>, Serialization<?>>();
    private final ConcurrentHashMap<Class<?>, com.esotericsoftware.kryo.Serializer<?>>
            serializationClassToKryoSerializer = new ConcurrentHashMap<>();//为何不直接引用map中的内容？ 是私有的，不应该直接被引用
    private final CopyOnWriteArrayList<Serialization<?>> index = new CopyOnWriteArrayList<Serialization<?>>();
    private final Set<Class<?>> kryoSerializationClasses = Collections.synchronizedSet(new HashSet<Class<?>>());
    private final int kryoRegistrationOffset = new Kryo().getNextRegistrationId();

    public SerializationRegistry() {
        //need to complete;
        registerKryo(Integer.class);
        registerKryo(String.class);
        registerKryo(Float.class);
        registerKryo(Boolean.class);
        registerKryo(Byte.class);
        registerKryo(Char.class);
        registerKryo(Short.class);
        registerKryo(Long.class);
        registerKryo(Double.class);
        registerKryo(Void.class);
        registerOrigin(new NullReferenceSerialization());
    }

    public synchronized <T> void register(Serialization<T> serialization) {
        if (serialization == null) {
            throw new IllegalArgumentException();
        } else {
            index.add(serialization);
            Class<?> targetClass = serialization.getSerializableClass();
            if (targetClass != null) {
                registry.put(targetClass, serialization);
                serializationClassToKryoSerializer.remove(targetClass);
                kryoSerializationClasses.remove(targetClass);
            }
            serialization.cacheIndex = index.indexOf(serialization);
            log.trace("id: {} serialization: {}", serialization.getCacheIndex(),
                    serialization);
        }
    }


    public synchronized <T> void registerOrigin(Serialization<T> serialization) {
        register(serialization);
    }

    /**
     * Registers given class using default serializer in Kryo.
     *
     * @param serializationClass the target class you want to serialize using Kryo
     */
    public synchronized <T> void registerKryo(Class<T> serializationClass) {
        Objects.requireNonNull(serializationClass);
        Serialization<T> kryoSerialization = new KryoSerialization<>(serializationClass);
        registry.put(serializationClass, kryoSerialization);
        kryoSerializationClasses.add(serializationClass);
        serializationClassToKryoSerializer.remove(serializationClass);
        index.add(kryoSerialization);
        kryoSerialization.cacheIndex = index.indexOf(kryoSerialization);
        log.trace("Kryo registrationId: {}, serialization: {},  targetClass: {}, cacheIndex: {}",
                getKryoRegistrationId(serializationClass), kryoSerialization,
                serializationClass, kryoSerialization.cacheIndex);
    }

    /**
     * Registers given class using specific Kryo serializer.
     * <p>
     * <p>
     * <p>Only one Kryo serializer scheme may be registered at any given time. If a previous Kryo Serializer had existed
     * in the registry, it will be replaced with the supplied {@code kryoSerializer} on a successful call to this method.
     *
     * @param serializationClass the target class you want to serialize using Kryo.
     * @param kryoSerializer     the custom Kryo Serializer which will be registered in Kryo.
     */
    public synchronized <T> void registerKryo(Class<T> serializationClass,
                                              com.esotericsoftware.kryo.Serializer<T> kryoSerializer) {
        Objects.requireNonNull(serializationClass);
        Objects.requireNonNull(kryoSerializer);
        Serialization<T> kryoSerialization = new KryoSerialization<>(serializationClass);
        registry.put(serializationClass, kryoSerialization);
        serializationClassToKryoSerializer.put(serializationClass, kryoSerializer);
        kryoSerializationClasses.remove(serializationClass);
        index.add(kryoSerialization);
        kryoSerialization.cacheIndex = index.indexOf(kryoSerialization);
        log.trace("Kryo registrationId: {}, serialization: {},  targetClass: {}, cacheIndex: {}",
                getKryoRegistrationId(serializationClass), kryoSerialization, serializationClass, kryoSerialization.cacheIndex);
    }

    public boolean isRegistered(Class<?> targetClass) {
        if (registry.containsKey(targetClass)) {
            return true;
        } else {
            return getSerialization(targetClass) != null;
        }
    }

    int getRegistrationId(Class<?> targetClass) {
        Serialization<?> serialization = getSerialization(targetClass);
        return index.indexOf(serialization);
    }

    int getRegistrationId(Serializer<?> serialization) {
        return index.indexOf(serialization);
    }

    public int getKryoRegistrationId(Class<?> targetClass) {
        Serialization<?> serialization = getSerialization(targetClass);
        int index = this.index.indexOf(serialization);
        return index + kryoRegistrationOffset;
    }

    public int getKryoRegistrationOffset() {
        return kryoRegistrationOffset;
    }


    public Iterator<Serialization<?>> iterator() {
        return index.iterator();
    }


    public <T> Serialization<T> getSerialization(Class<T> targetClass) {
        @SuppressWarnings("unchecked")
        Serialization<T> serialization = (Serialization<T>) registry.get(targetClass);
        if (serialization == null) {
            final ListIterator<Serialization<?>> iterator = index.listIterator(index.size());
            while (serialization == null && iterator.hasPrevious()) {
                final Serialization<?> prototype = iterator.previous();
                if (prototype.isSerializable(targetClass)) {
                    @SuppressWarnings("unchecked")
                    final Serialization<T> prototypeCopy = (Serialization<T>) prototype.clonedCopy();
                    serialization = prototypeCopy;
                    log.trace("{}: using: {} (id: {})", targetClass,
                            serialization,
                            serialization.getCacheIndex());
                    registry.putIfAbsent(targetClass, prototypeCopy);
                }
            }
        }
        return serialization;
    }


    public <T> Serialization<T> newSerialization(Class<T> targetClass) {
        final Serialization<T> prototype = getSerialization(targetClass);
        final Serialization<T> clone = prototype == null ? null : prototype.clonedCopy();
        if (clone == null) {
            throw new RuntimeException("Unable to clone serialization prototype for class " + targetClass);
        }
        return clone;
    }

    Serialization<?> newSerialization(int index) {
        final Serialization<?> prototype = this.index.get(index);
        final Serialization<?> clone = prototype == null ? null : prototype.clonedCopy();
        if (clone == null) {
            throw new RuntimeException("Unable to clone serialization prototype for index " + index);
        }
        return clone;
    }

    public Kryo newKryo() {
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        kryo.setReferences(false);
        for (Map.Entry<Class<?>, com.esotericsoftware.kryo.Serializer<?>>
                serializationClassAndKryoSerializer : serializationClassToKryoSerializer
                .entrySet()) {
            final Class<?> serializationClass = serializationClassAndKryoSerializer.getKey();
            final com.esotericsoftware.kryo.Serializer<?> kryoSerializer = serializationClassAndKryoSerializer.getValue();
            kryo.register(serializationClass, kryoSerializer, getKryoRegistrationId(serializationClass));
        }
        for (Class<?> kryoSerializationClass : kryoSerializationClasses) {
            kryo.register(kryoSerializationClass, getKryoRegistrationId(kryoSerializationClass));
        }
        return kryo;
    }


}