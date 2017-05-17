package Test.Fox;

import Fox.Primitive.StringSerialization;
import Fox.SerializationCache;
import Fox.SerializationRegistry;
import Fox.SerializedOutputStream;
import Test.TestWorkFlow;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * create with Test.Fox
 * USER: husterfox
 */
public class MannualSerialize extends TargetSerialize {
    @Override
    public void beforeSerialize() {
        serializationRegistry = new SerializationRegistry();
        serializationRegistry.registerOrigin(new MannualTargetSerialization());
        serializationRegistry.registerOrigin(new StringSerialization());
        serializationCache = new SerializationCache(serializationRegistry);
        byteArrayOutputStream = new ByteArrayOutputStream();
        serializedOutputStream =
                new SerializedOutputStream(byteArrayOutputStream, serializationCache, serializationRegistry.newKryo());
    }
    public static void main(String[] args) throws IOException {
        new MannualSerialize().hotTest(500000).mainTest(1000000);
    }
}
