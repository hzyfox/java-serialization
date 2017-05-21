package Fox;

import java.io.IOException;

public interface Serializer<T> extends Cloneable {

    public void serialize(T object, SerializedOutput output) throws IOException;

    public Class<?> getSerializableClass();

    public boolean isSerializable(Class<?> targetClass);

    public Serializer<T> clone();
}
