package Test;

import java.io.IOException;

/**
 * create with Test
 * USER: husterfox
 */
public abstract class TestWorkFlow {
    public int TIMES =0;
    public int HOT_TIMES=0;
    public byte[] content;

    public abstract void beforeSerialize() throws IOException;

    public abstract void setSerializableObject() throws IOException;

    public abstract void afterSerialize() throws IOException;

    public abstract void getSerializableObject() throws IOException;

    public abstract void afterDeserialize() throws IOException;

    public void setTIMES(int times){
        TIMES = times;
    }
    public void setHOT_TIMES(int hot_times){
        HOT_TIMES = hot_times;
    }

    public void getSize() {
        System.out.println("序列化 " + TIMES + "次的流长度的为 " + content.length + " 字节");
    }

    public void mainTest(int times) throws IOException {
        setTIMES(times);
        beforeSerialize();
        long start = System.currentTimeMillis();
        setSerializableObject();
        System.out.println("java原生序列化时间:" + (System.currentTimeMillis() - start) + " ms");
        afterSerialize();
        start = System.currentTimeMillis();
        getSerializableObject();
        System.out.println("java原生反序列化时间:" + (System.currentTimeMillis() - start) + " ms");
        afterDeserialize();
    }
    public TestWorkFlow hotTest(int hotTimes) throws IOException{
        setHOT_TIMES(hotTimes);
        beforeSerialize();
        setSerializableObject();
        afterSerialize();
        getSerializableObject();
        afterDeserialize();
        return this;
    }
}
