package Test.Origin;

import Test.TargetClass;
import Test.TargetClassWithMannual;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * create with Test.Origin
 * USER: husterfox
 */
public class JavaMannualSerialize extends OriginSerialize {

    public static void main(String[] args) throws IOException {
        new JavaMannualSerialize().hotTest(500000).mainTest(1000000);
    }
    @Override
    public void setSerializableObject() throws IOException {
        for (int i = 0; i < TIMES; i++) {
            Map<String, Integer> map = new HashMap<String, Integer>(2);
            map.put("origin1", i);
            map.put("origin2", i);
            objectOutputStream.writeObject(new TargetClassWithMannual("fox" + i, (i + 1), map));
        }
    }

    @Override
    public void getSerializableObject() throws IOException {
        TargetClassWithMannual targetClass = null;
        try {
            for (int i = 0; i < TIMES; i++) {
                targetClass = (TargetClassWithMannual) objectInputStream.readObject();
                //System.out.println(targetClass.getAge()+ "  " + targetClass.getName());
            }
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

    }
}
