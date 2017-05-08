package Fox.Primitive;

import Fox.NullReference;
import Fox.Serialization;
import Fox.SerializedInput;
import Fox.SerializedOutput;

import java.io.IOException;

/**
 * create with Fox.Primitive
 * USER: husterfox
 */
public final class NullReferenceSerialization extends Serialization<NullReference> {
    public NullReferenceSerialization() {
        super(NullReference.class);
    }

    @Override
    public final void serialize(NullReference object, SerializedOutput output) throws IOException {
        output.writeByte(0);
    }

    @Override
    public final NullReference deserialize(SerializedInput input) throws IOException {
        input.readByte();
        return null;
    }
    @Override
    public final NullReferenceSerialization clone() {
        return new NullReferenceSerialization();
    }
}