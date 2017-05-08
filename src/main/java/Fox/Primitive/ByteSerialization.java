package Fox.Primitive;

import Fox.Serialization;
import Fox.SerializedInput;
import Fox.SerializedOutput;

import java.io.IOException;

/**
 * create with Fox.Primitive
 * USER: husterfox
 */

public class ByteSerialization extends Serialization<Byte> {
    public ByteSerialization() {
        super(Byte.class);
    }

    @Override
    public void serialize(Byte object, SerializedOutput output) throws IOException {
        output.write(object.byteValue());
    }

    @Override
    public Byte deserialize(SerializedInput input) throws IOException {
        return new Byte(input.readByte());
    }

    @Override
    public ByteSerialization clone() {
        return new ByteSerialization();
    }
}
