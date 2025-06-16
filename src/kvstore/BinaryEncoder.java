// BinaryEncoder.java
package kvstore;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BinaryEncoder {

    // Tambahan versi 2: metadata
    // Tambahan versi 3: type flag

    public static void encode(OutputStream os, String key, String value) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);

        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] valBytes = value.getBytes(StandardCharsets.UTF_8);
        byte[] metaBytes = "default-meta".getBytes(StandardCharsets.UTF_8); // contoh

        byte typeFlag = 0x01; // 0x01 = String, 0x02 = Integer, dst.

        dos.writeByte(Config.getCurrentEncodingVersion()); // 1 byte

        if (Config.getCurrentEncodingVersion() >= 3) {
            dos.writeByte(typeFlag); // versi 3: type flag (1 byte)
        }

        dos.writeByte((byte) keyBytes.length);          // 1 byte
        dos.writeShort((short) valBytes.length);        // 2 bytes
        dos.write(keyBytes);                            // N bytes
        dos.write(valBytes);                            // M bytes

        if (Config.getCurrentEncodingVersion() >= 2) {
            dos.writeByte((byte) metaBytes.length);     // 1 byte (metaLen)
            dos.write(metaBytes);                       // metadata (versi 2+)
        }

        dos.writeLong(System.currentTimeMillis());       // 8 bytes timestamp
    }

    public static Map<String, String> decodeAll(InputStream is) throws IOException {
        Map<String, String> map = new HashMap<>();
        DataInputStream dis = new DataInputStream(is);

        while (dis.available() > 0) {
            byte version = dis.readByte();
            byte typeFlag = 0x00;
            if (version >= 3) {
                typeFlag = dis.readByte(); // abaikan dulu implementasi tipe dinamis
            }

            byte keyLen = dis.readByte();
            short valLen = dis.readShort();

            byte[] keyBytes = new byte[keyLen];
            dis.readFully(keyBytes);
            String key = new String(keyBytes, StandardCharsets.UTF_8);

            byte[] valBytes = new byte[valLen];
            dis.readFully(valBytes);
            String value = new String(valBytes, StandardCharsets.UTF_8);

            if (version >= 2) {
                byte metaLen = dis.readByte();
                byte[] metaBytes = new byte[metaLen];
                dis.readFully(metaBytes);
                // metadata tidak digunakan untuk sekarang
            }

            long timestamp = dis.readLong(); // bisa digunakan nanti untuk TTL

            map.put(key, value); // tetap gunakan key-value
        }
        return map;
    }

    public static void hexdump(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            int b;
            int count = 0;
            while ((b = fis.read()) != -1) {
                System.out.printf("%02X ", b);
                count++;
                if (count % 16 == 0) System.out.println();
            }
            if (count % 16 != 0) System.out.println();
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal hexdump: " + e.getMessage());
        }
    }
}
