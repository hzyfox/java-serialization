package Fox.Primitive;

import Fox.Serialization;
import Fox.SerializedInput;
import Fox.SerializedOutput;

import java.io.IOException;

/**
 * create with Fox.Primitive
 * USER: husterfox
 */
public class LongSerialization extends Serialization<Long> {
    @Override
    public Long deserialize(SerializedInput input) throws IOException {
        return input.readLong();
    }

    @Override
    public void serialize(Long object, SerializedOutput output) throws IOException {
        output.writeLong(object);
    }

    @Override
    public Serialization<Long> clone() {
        return new LongSerialization();
    }
}
