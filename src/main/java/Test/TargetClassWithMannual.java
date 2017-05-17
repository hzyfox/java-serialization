package Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * create with Test
 * USER: husterfox
 */
public class TargetClassWithMannual implements Serializable {
    private String name;
    private int age;
    private Map<String, Integer> map;

    public TargetClassWithMannual() {

    }

    public TargetClassWithMannual(String name, int age, Map<String, Integer> map) {
        this.name = name;
        this.age = age;
        this.map = map;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(name);
        out.writeInt(age);
        out.writeInt(map.size());
        for (Object o : map.entrySet()) {
            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) o;
            out.writeObject(entry.getKey());
            out.writeInt(entry.getValue());
        }
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        name = (String)in.readObject();
        age = in.readInt();
        int size = in.readInt();
        map = new HashMap<String,Integer>(size);
        for (int i = 0; i <size ; i++) {
            map.put((String)in.readObject(),in.readInt());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }
}
