package Test.Origin;

import Test.TargetClassWithWriteRead;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * create with Test.Origin
 * USER: husterfox
 */
public class JavaManualSerialize extends JavaBuiltInSerialize {

    public static void main(String[] args) throws IOException {
        new JavaManualSerialize().hotTest(1000000).mainTest(1000000);
    }
    @Override
    public void setSerializableObject() throws IOException {
        for (int i = 0; i < TIMES; i++) {
            Map<String, Integer> map = new HashMap<String, Integer>(2);
            map.put("origin1", i);
            map.put("origin2", i);
            objectOutputStream.writeObject(new TargetClassWithWriteRead("fox" + i, (i + 1), map));
        }
    }

    @Override
    public void getSerializableObject() throws IOException {
        TargetClassWithWriteRead targetClassWithWriteRead = null;
        try {
            for (int i = 0; i < TIMES; i++) {
                targetClassWithWriteRead = (TargetClassWithWriteRead) objectInputStream.readObject();
                //System.out.println(targetClass.getAge()+ "  " + targetClass.getName());
            }
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

    }
}
