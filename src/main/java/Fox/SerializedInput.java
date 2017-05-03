package Fox;

import java.io.DataInput;
import java.io.IOException;


public interface SerializedInput extends DataInput {

    public <T> T read(Class<T> objectClass) throws IOException;

    public Object readObject() throws IOException;
}
