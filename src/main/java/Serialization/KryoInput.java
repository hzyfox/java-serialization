package Serialization;

/**
 * create with Serialization
 * USER: husterfox
 */
public class KryoInput extends com.esotericsoftware.kryo.io.Input {
    public KryoInput() {
        super();
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
