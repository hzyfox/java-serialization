package Test.Origin;

import Test.TargetClass;
import Test.TestWorkFlow;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * create with Test.Origin
 * USER: husterfox
 */
public class JavaBuiltInSerialize extends TestWorkFlow {
    public ByteArrayOutputStream byteArrayOutputStream;
    public ObjectOutputStream objectOutputStream;
    public ObjectInputStream objectInputStream;
    public ByteArrayInputStream byteArrayInputStream;

    public static void main(String[] args) throws IOException {
        new JavaBuiltInSerialize().hotTest(1000000).mainTest(1000000);
    }

    public void setSerializableObject() throws IOException {

        for (int i = 0; i < TIMES; i++) {
            Map<String, Integer> map = new HashMap<String, Integer>(2);
            map.put("origin1", i);
            map.put("origin2", i);
            objectOutputStream.writeObject(new TargetClass("fox" + i, (i + 1), map));
        }

    }

    public void beforeSerialize() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();

        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream); //byte array will add 4

    }

    public void afterSerialize() throws IOException {
        objectOutputStream.flush();
        content = byteArrayOutputStream.toByteArray();
        System.out.println("byteArray length is " +byteArrayOutputStream.size());
        getSize();
        objectOutputStream.close();
        byteArrayOutputStream.close();
        byteArrayInputStream = new ByteArrayInputStream(content);
        objectInputStream = new ObjectInputStream(byteArrayInputStream);
    }

    public void getSerializableObject() throws IOException {

        TargetClass targetClass = null;
        try {
            for (int i = 0; i < TIMES; i++) {
                targetClass = (TargetClass) objectInputStream.readObject();
                //System.out.println(targetClass.getAge() + "  " + targetClass.getName());
            }
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

    }

    @Override
    public void afterDeserialize() throws IOException {
        objectInputStream.close();
        byteArrayInputStream.close();
    }

}
