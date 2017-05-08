package Fox.SerializationClass;

import Fox.Serialization;
import Fox.SerializedInput;
import Fox.SerializedOutput;
import Fox.Target.Simple;

import java.io.IOException;

/**
 * create with Fox.Serialization
 * USER: husterfox
 */
public class SimpleSerialization extends Serialization<Simple> {

    public SimpleSerialization() {
        super(Simple.class);
    }

    @Override
    public Simple deserialize(SerializedInput input) throws IOException {
        int targetInt = input.readInt();
        System.out.println("input readint is: " + targetInt);
        return new Simple(targetInt + 1);
    }

    @Override
    public void serialize(Simple object, SerializedOutput output) throws IOException {
        System.out.println("writeint is: " + object.getTargetInt());
        output.writeInt(object.getTargetInt());
    }

    @Override
    public Serialization<Simple> clone() {
        return new SimpleSerialization();
    }
}
