package Fox;


import java.io.DataOutput;
import java.io.IOException;


public interface SerializedOutput extends DataOutput {

    public <T> void write(Object object, Class<T> clazz) throws IOException;


    public <T extends Object> void writeObject(T object) throws IOException;
}
