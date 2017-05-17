package Fox.Primitive;

import Fox.Serialization;
import Fox.SerializedInput;
import Fox.SerializedOutput;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * create with Fox.Primitive
 * USER: husterfox
 */
public class StringSerialization extends Serialization<String>{
    public static final Charset ENCODING = StandardCharsets.UTF_8;
    public StringSerialization() {
        super(String.class);
    }

    @Override
    public String deserialize(SerializedInput input) throws IOException {
        final int totalBytes = input.readInt();
        if (totalBytes > 0) {
            final byte[] bytes = new byte[totalBytes];
            input.readFully(bytes);
            final String result = new String(bytes, ENCODING);
            return result;
        }
        return new String();
    }

    @Override
    public void serialize(String object, SerializedOutput output) throws IOException {
        if (object != null) {
            final byte[] bytes = object.getBytes(ENCODING);
            output.writeInt(bytes.length);
            output.write(bytes);
        } else {
            output.writeInt(0);
        }
    }

    @Override
    public Serialization<String> clone() {
        return new StringSerialization();
    }
}
