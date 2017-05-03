package Serialization;


import com.esotericsoftware.kryo.Kryo;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SerializedInputStream extends DataInputStream implements SerializedInput {
    final SerializationCache serializationCache;
    private final Kryo kryo;
    private final InputStream in;

    public SerializedInputStream(InputStream in) {
        this(in, new SerializationCache());
    }

    public SerializedInputStream(InputStream in, SerializationCache serializationCache) {
        this(in, serializationCache, null);
    }

    public SerializedInputStream(InputStream in, SerializationCache serializationCache, Kryo kryo) {
        super(in);
        this.serializationCache = serializationCache;
        this.kryo = kryo;
        this.in = in;
    }

    public <T> T read(Class<T> objectClass) throws IOException {
        if (serializationCache == null) {
            throw new IllegalStateException();
        }
        final Serialization<T> serialization = serializationCache.getSerialization(objectClass);
        if (serialization == null) {
            throw new UnsupportedOperationException();
        } else {
            return serialization.deserialize(this);
        }
    }

    public Object readObject() throws IOException {
        if (serializationCache == null) {
            throw new IllegalStateException();
        }
        int serializationId = 0xff & (int) readByte();
        final Serialization<?> serialization = serializationCache.getSerialization(serializationId);
        if (serialization == null) {
            throw new UnsupportedOperationException();
        } else {
            return serialization.deserialize(this);
        }
    }

    public final SerializationCache getSerializationCache() {
        return serializationCache;
    }

    public final Kryo getKryo() {
        return this.kryo;
    }

    public final InputStream getInputStream() {
        return this.in;
    }
}

