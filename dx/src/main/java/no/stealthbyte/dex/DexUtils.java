package no.stealthbyte.dex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DexUtils {

    private byte[] bytes;
    private int index = 0;

    private final ByteArrayOutputStream baos;

    public DexUtils() {
        this.bytes = new byte[0];
        this.baos = new ByteArrayOutputStream();
    }

    public DexUtils(ByteArrayOutputStream baos) {
        this.bytes = new byte[0];
        this.baos = baos;
    }

    public DexUtils(byte[] bytes) throws IOException {
        this.bytes = bytes;
        this.baos = new ByteArrayOutputStream();
        this.baos.write(bytes);
    }

    public void putByte(byte b, boolean updateBytes) throws IOException {
        baos.write(new byte[] { b });
        if (updateBytes)
            bytes = baos.toByteArray();
    }

    public void putInt(int i, boolean updateBytes) throws IOException {
        baos.write(ByteBuffer.allocate(4).putInt(i).array());
        if (updateBytes)
            bytes = baos.toByteArray();
    }

    public void putBytes(byte[] data, boolean updateBytes) throws IOException {
        putInt(data.length, false);
        baos.write(data);
        if (updateBytes)
            bytes = baos.toByteArray();
    }

    public void putString(String str, boolean updateBytes) throws IOException {
        putBytes(str.getBytes(StandardCharsets.UTF_8), false);
        if (updateBytes)
            bytes = baos.toByteArray();
    }

    public byte readByte() {
        return bytes[index++];
    }

    public int readInt() {
        byte[] dest = new byte[4];
        System.arraycopy(bytes, index, dest, 0, 4);
        index += 4;
        return ByteBuffer.wrap(dest).getInt();
    }

    public byte[] readBytes() {
        int len = readInt();
        byte[] dest = new byte[len];
        System.arraycopy(bytes, index, dest, 0, len);
        index += len;
        return dest;
    }

    public String readString() {
        return new String(readBytes(), StandardCharsets.UTF_8);
    }

    public boolean hasData() {
        return index < bytes.length;
    }

    public void updateBytes() {
        bytes = baos.toByteArray();
    }

    public byte[] all() {
        return bytes;
    }

}
