package Serialization;

import com.esotericsoftware.kryo.KryoException;

import java.io.IOException;

/**
 * create with Serialization
 * USER: husterfox
 */
public class KryoOutput extends com.esotericsoftware.kryo.io.Output {

    public KryoOutput(int bufferSize) {
        super(bufferSize);
    }

    @Override
    public void flush() {
        if (outputStream == null) {
            return;
        }
        try {
            outputStream.write(buffer, 0, position);
        } catch (IOException exception) {
            throw new KryoException(exception);
        }
        total += position;
        position = 0;
    }
}
