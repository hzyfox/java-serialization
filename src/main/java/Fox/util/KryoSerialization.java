package Fox.util;

import Fox.*;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * create with Serialization.util
 * USER: husterfox
 */
public class KryoSerialization<T> extends Serialization<T> {
    private final Class<T> serializationClass;
    private final KryoOutput kryoOutput;
    private final KryoInput kryoInput;

    public KryoSerialization(Class<T> serializationClass) {
        super(serializationClass);
        this.serializationClass = serializationClass;
        //use same buffer size as Kryo's default buffer size, which is 4096 byte.
        kryoOutput = new KryoOutput(4096);

        this.kryoInput = new KryoInput();
    }

    @Override
    public T deserialize(SerializedInput input) throws IOException {
        final SerializedInputStream serializedInputStream = (SerializedInputStream) input;
        final Kryo kryo = serializedInputStream.getKryo();
        final ByteBufferInputStream byteBufferInputStream = (ByteBufferInputStream) serializedInputStream.getInputStream();
        final ByteBuffer byteBuffer = byteBufferInputStream.getByteBuffer();
        kryoInput.setInputStream(null);
        kryoInput.setBuffer(byteBuffer.array());
        kryoInput.setLimit(byteBuffer.arrayOffset() + byteBuffer.limit());
        kryoInput.setTotal(0);
        kryoInput.setCapacity(byteBuffer.arrayOffset() + byteBuffer.capacity());
        kryoInput.setPosition(byteBuffer.arrayOffset() + byteBuffer.position());
        final T readObject;
        try {
            readObject = kryo.readObject(kryoInput, serializationClass);
        } catch (KryoException exception) {
            throw new RuntimeException(
                    "Check whether " + serializationClass.getCanonicalName() + " has a zero argument constructor. "
                            + "Registered class in serialization will cause this error "
                            + "when it does not have a zero argument constructor. "
                            + "If caused by this issue, try to provide a zero argument constructor for "
                            + serializationClass.getCanonicalName()
                            + " or disable serialization.kryo.use-default-instantiator-strategy "
                            + "in Yita configuration file.", exception);
        }
        byteBuffer.position(kryoInput.position() - byteBuffer.arrayOffset());
        return readObject;
    }

    @Override
    public void serialize(T object, SerializedOutput output) throws IOException {
        final SerializedOutputStream serializedOutputStream = (SerializedOutputStream) output;
        final Kryo kryo = serializedOutputStream.getKryo();
        kryoOutput.setOutputStream(serializedOutputStream);
        kryo.writeObject(kryoOutput, object);
        kryoOutput.flush();
    }

    @Override
    public Serialization<T> clone() {
        return new KryoSerialization<>(serializationClass);
    }
}
