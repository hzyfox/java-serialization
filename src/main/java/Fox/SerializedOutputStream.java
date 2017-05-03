package Fox;

import com.esotericsoftware.kryo.Kryo;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;



public class SerializedOutputStream extends DataOutputStream implements SerializedOutput {
    final SerializationCache serializationCache;
    private final Kryo kryo;

    public SerializedOutputStream(OutputStream out) {
        this(out, new SerializationCache());
    }

    public SerializedOutputStream(OutputStream out, SerializationCache serializationCache) {
        this(out, serializationCache, null);
    }

    public SerializedOutputStream(OutputStream out, SerializationCache serializationCache, Kryo kryo) {
        super(out);
        this.serializationCache = serializationCache;
        this.kryo = kryo;
    }

    @Override
    public <T extends Object> void writeObject(T object) throws IOException {
        if (serializationCache == null) {
            throw new IllegalStateException();
        }

        if (object == null) {
            final Serialization<NullReference> serialization = serializationCache.getSerialization(NullReference.class);
            writeByte(serialization.getCacheIndex());
            serialization.serialize(null, this);
            return;
        }

        @SuppressWarnings("unchecked")
        final Serialization<T> serialization = serializationCache.getSerialization((Class<T>) object.getClass());

        if (serialization == null) {
            throw new UnsupportedOperationException();
        } else {
            writeByte(serialization.getCacheIndex());
            serialization.serialize(object, this);
        }
    }

    @Override
    public <T> void write(Object object, Class<T> clazz) throws IOException {
        if (serializationCache == null) {
            throw new IllegalStateException();
        }
        final Serialization<T> serialization = serializationCache.getSerialization(clazz);
        if (serialization == null) {
            throw new UnsupportedOperationException();
        } else {
            serialization.serialize(clazz.cast(object), this);
        }
    }

    public final SerializationCache getSerializationCache() {
        return serializationCache;
    }

    public final Kryo getKryo() {
        return kryo;
    }
}

