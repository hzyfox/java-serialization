package Fox.Primitive;

import Fox.Serialization;
import Fox.SerializedInput;
import Fox.SerializedOutput;

import java.io.IOException;

/**
 * create with Fox.Primitive
 * USER: husterfox
 */
public class DoubleSerialization extends Serialization<Double> {
    @Override
    public Double deserialize(SerializedInput input) throws IOException {
        return input.readDouble();
    }

    @Override
    public void serialize(Double object, SerializedOutput output) throws IOException {
        output.writeDouble(object);
    }

    @Override
    public Serialization<Double> clone() {
        return new DoubleSerialization();
    }
}
