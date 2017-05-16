package Fox.Test;

import Fox.SerializationCache;
import Fox.SerializationClass.SimpleSerialization;
import Fox.SerializationRegistry;
import Fox.SerializedInputStream;
import Fox.SerializedOutputStream;
import Fox.Target.Simple;
import Fox.Target.TargetKryo;
import Fox.util.ByteBufferInputStream;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * create with Fox.Test
 * USER: husterfox
 */
public class SimpleSerializationTest {
    Logger logger = LoggerFactory.getLogger(SimpleSerializationTest.class);

    @Test
    public void testSimpleSerialization() throws IOException {
        Simple simple = new Simple(1);
        SerializationRegistry serializationRegistry = new SerializationRegistry();
        serializationRegistry.registerOrigin(new SimpleSerialization());
        SerializationCache serializationCache = new SerializationCache(serializationRegistry);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SerializedOutputStream serializedOutputStream = new SerializedOutputStream(byteArrayOutputStream, serializationCache);
        serializedOutputStream.writeObject(simple);
        byte[] content = byteArrayOutputStream.toByteArray();
        java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.wrap(content);
        ByteBufferInputStream byteBufferInputStream = new ByteBufferInputStream(byteBuffer);
        SerializedInputStream serializedInputStream = new SerializedInputStream(byteBufferInputStream, serializationCache);
        Simple simple1 = (Simple) serializedInputStream.readObject();
        System.out.println("targetInt is " + simple1.getTargetInt());
        Assert.assertTrue("serialization successs", simple1.getTargetInt() == 2);
    }

    @Test
    public void testKryoSerialization() throws IOException {
        TargetKryo targetKryo = new TargetKryo();
        SerializationRegistry serializationRegistry = new SerializationRegistry();
        serializationRegistry.registerKryo(TargetKryo.class);
        SerializationCache serializationCache = new SerializationCache(serializationRegistry);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SerializedOutputStream serializedOutputStream =
                new SerializedOutputStream(byteArrayOutputStream, serializationCache, serializationRegistry.newKryo());//kryo object donot forget
        serializedOutputStream.writeObject(targetKryo);
        byte[] content = byteArrayOutputStream.toByteArray();
        java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.wrap(content);
        ByteBufferInputStream byteBufferInputStream = new ByteBufferInputStream(byteBuffer);
        SerializedInputStream serializedInputStream =
                new SerializedInputStream(byteBufferInputStream, serializationCache, serializationRegistry.newKryo());//kryo obejct donnot forget
        TargetKryo targetKryo1 = (TargetKryo) serializedInputStream.readObject();
        System.out.println("targetInt is " + targetKryo1.getTargetInt());
        Assert.assertTrue("serialization successs", targetKryo1.getTargetInt() == 1);
    }
}
