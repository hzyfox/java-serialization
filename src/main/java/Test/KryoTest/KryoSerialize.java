package Test.KryoTest;

import Test.TargetClass;
import Test.TestWorkFlow;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * create with Test.KryoTest
 * USER: husterfox
 */
public class KryoSerialize extends TestWorkFlow {
    public Kryo kryo;
    Output output;
    ByteArrayOutputStream byteArrayOutputStream;
    ByteArrayInputStream byteArrayInputStream;
    Input input;

    @Override
    public void beforeSerialize() throws IOException {
        kryo = new Kryo();
        kryo.register(TargetClass.class);
        kryo.register(HashMap.class);
        byteArrayOutputStream = new ByteArrayOutputStream();
        output = new Output(byteArrayOutputStream);
    }

    @Override
    public void setSerializableObject() throws IOException {
        for(int i=0;i<TIMES;i++){
            Map<String, Integer> map = new HashMap<String, Integer>(2);
            map.put("origin1", i);
            map.put("origin2", i);
            kryo.writeObject(output,new TargetClass("fox"+i,(i+1),map));
        }
    }

    @Override
    public void afterSerialize() throws IOException {
        output.flush();
        output.close();
        byteArrayOutputStream.close();
        content = byteArrayOutputStream.toByteArray();
        getSize();
        byteArrayInputStream = new ByteArrayInputStream(content);
        input = new Input(byteArrayInputStream);
    }

    @Override
    public void getSerializableObject() throws IOException {
        TargetClass targetClass = null;
        for(int i=0;i<TIMES;i++){
            targetClass = (TargetClass)kryo.readObject(input,TargetClass.class);
            //System.out.println(targetClass.getAge() + "  " + targetClass.getName());
        }
    }

    @Override
    public void afterDeserialize() throws IOException {
        byteArrayInputStream.close();
        input.close();
    }

    public static void main(String[] args) throws IOException {
        new KryoSerialize().hotTest(100000).mainTest(1000000);
    }
}
