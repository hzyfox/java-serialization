package Fox;


import java.io.IOException;

public interface Deserializer<T> extends Cloneable {

    public T deserialize(SerializedInput input) throws IOException;

    public Class<?> getDeserializableClass();

    public boolean isDeserializable(Class<?> targetClass);

    public Deserializer<T> clone();
}
