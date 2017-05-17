package Test.Fox;

import Fox.SerializationCache;
import Fox.SerializationRegistry;
import Fox.SerializedInputStream;
import Fox.SerializedOutputStream;
import Fox.util.ByteBufferInputStream;
import Test.TargetClass;
import Test.TestWorkFlow;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * create with Test.Fox
 * USER: husterfox
 */
public class KryoTargetSerialize extends TestWorkFlow {
    public  SerializationRegistry serializationRegistry;
    public  SerializationCache serializationCache;
    public  ByteArrayOutputStream byteArrayOutputStream;
    public  SerializedOutputStream serializedOutputStream;

    public  ByteBufferInputStream byteBufferInputStream;
    public  SerializedInputStream serializedInputStream;


    public static void main(String[] args) throws IOException {
        new KryoTargetSerialize().hotTest(1000000).mainTest(1000000);
    }

    public  void setSerializableObject() throws IOException {
        for(int i=0; i<TIMES;i++){
            Map<String, Integer> map = new HashMap<String, Integer>(2);
            map.put("origin1", i);
            map.put("origin2", i);
            serializedOutputStream.writeObject(new TargetClass("fox"+i,(i+1),map));
        }
    }
    public  void afterSerialize() throws IOException {
        content = byteArrayOutputStream.toByteArray();
        getSize();
        byteArrayOutputStream.close();
        serializedOutputStream.close();
        ByteBuffer byteBuffer = ByteBuffer.wrap(content);
        byteBufferInputStream = new ByteBufferInputStream(byteBuffer);
        serializedInputStream = new SerializedInputStream(byteBufferInputStream,serializationCache,serializationRegistry.newKryo());
    }

    public void getSerializableObject() throws IOException {
        TargetClass targetClass = null;
        for(int i=0; i<TIMES;i++){
            targetClass = (TargetClass) serializedInputStream.readObject();
            //System.out.println(targetClass.getAge() + "  " + targetClass.getName());
        }
    }

    public  void afterDeserialize() throws IOException {
        byteBufferInputStream.close();
        serializedInputStream.close();
    }

    public  void beforeSerialize() {
        serializationRegistry = new SerializationRegistry();
        serializationRegistry.registerKryo(Map.class);
        serializationRegistry.registerKryo(HashMap.class);
        serializationRegistry.registerOrigin(new KryoTargetSerialization());
        serializationCache = new SerializationCache(serializationRegistry);
        byteArrayOutputStream = new ByteArrayOutputStream();
        serializedOutputStream =
                new SerializedOutputStream(byteArrayOutputStream, serializationCache, serializationRegistry.newKryo());

    }

}
