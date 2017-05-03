package Fox.util;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * create with Fox.util
 * USER: husterfox
 */
public final class ByteBufferInputStream extends InputStream {
    private ByteBuffer byteBuffer;

    public ByteBufferInputStream(int bufferSize) {
        this(ByteBuffer.allocate(bufferSize));
        byteBuffer.flip();
    }

    public ByteBufferInputStream(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public ByteBufferInputStream(byte[] byteB) {
        byteBuffer = ByteBuffer.wrap(byteB);
    }

    public ByteBufferInputStream(byte[] byteB, int offset, int length) {
        byteBuffer = ByteBuffer.wrap(byteB, offset, length);
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

    @Override
    public int available() throws IOException {
        return byteBuffer.remaining();
    }

    @Override
    public int read() throws IOException {
        if (!byteBuffer.hasRemaining()) {
            return -1;
        }
        return byteBuffer.get() & 0xff;
    }

    @Override
    public int read(byte[] byteB, int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        int remaining = byteBuffer.remaining();
        if (len > remaining) {
            byteBuffer.get(byteB, off, remaining);
            return remaining;
        } else {
            byteBuffer.get(byteB, off, len);
            return len;
        }
    }

    public DataInput getDataInput() {
        return new DataInputStream(this);
    }

}
