package Fox.Primitive;

import Fox.Serialization;
import Fox.SerializedInput;
import Fox.SerializedOutput;


import java.io.IOException;

/**
 * create with Fox.Primitive
 * USER: husterfox
 */
public class IntegerSerialization extends Serialization<Integer> {
    public IntegerSerialization(){
        super(Integer.class);
    }
    @Override
    public Integer deserialize(SerializedInput input) throws IOException {
        return input.readInt();
    }

    @Override
    public void serialize(Integer object, SerializedOutput output) throws IOException {
        output.writeInt(object.intValue());
    }

    @Override
    public Serialization<Integer> clone() {
        return new IntegerSerialization();
    }
}
