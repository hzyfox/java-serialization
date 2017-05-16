package Test.Fox;

import Fox.Serialization;
import Fox.SerializedInput;
import Fox.SerializedOutput;
import Test.TargetClass;

import java.io.IOException;
import java.util.Map;

/**
 * create with Test.Fox
 * USER: husterfox
 */
public class TargetSerialization extends Serialization<TargetClass> {
    public TargetSerialization(){
        super(TargetClass.class);
    }
    @Override
    public TargetClass deserialize(SerializedInput input) throws IOException {
        String name = (String)input.readObject();
        int age = input.readInt();
        Map<String,Integer> map = (Map<String, Integer>) input.readObject();//need to register map
        return new TargetClass(name,age,map);
    }

    @Override
    public void serialize(TargetClass object, SerializedOutput output) throws IOException {
        output.writeObject(object.getName());
        output.writeInt(object.getAge());
        output.writeObject(object.getMap());
    }

    @Override
    public Serialization<TargetClass> clone() {
        return new TargetSerialization();
    }
}
