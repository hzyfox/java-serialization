package Test.Fox;

import Fox.Serialization;
import Fox.SerializedInput;
import Fox.SerializedOutput;
import Test.TargetClass;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * create with Test.Fox
 * USER: husterfox
 */
public class MannualTargetSerialization extends Serialization<TargetClass> {
    public MannualTargetSerialization(){
        super(TargetClass.class);
    }

    @Override
    public TargetClass deserialize(SerializedInput input) throws IOException {
        String name = (String)input.readObject();
        int age = input.readInt();
        int size = input.readInt();
        Map<String, Integer> map = new HashMap<String, Integer>(size);
        for(int i=0;i<size;i++){
            map.put((String) input.readObject(),input.readInt());
        }
        return new TargetClass(name,age,map);
    }

    @Override
    public void serialize(TargetClass object, SerializedOutput output) throws IOException {
        output.writeObject(object.getName());
        output.writeInt(object.getAge());
        Map map = object.getMap();
        output.writeInt(map.size());
        for (Object o : map.entrySet()) {
            Map.Entry<String,Integer>entry = ( Map.Entry<String,Integer>)o;
            output.writeObject(entry.getKey());
            output.writeInt(entry.getValue());
        }
    }

    @Override
    public Serialization<TargetClass> clone() {
        return new MannualTargetSerialization();
    }
}
