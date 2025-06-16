package kvstore;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ShardNode {
    private final int shardId;
    private final Map<String, String> hotStorage;
    private final File coldStorageFile;

    public ShardNode(int shardId) {
        this.shardId = shardId;
        this.hotStorage = new HashMap<>();
        this.coldStorageFile = new File(Config.getShardDataFile(shardId));
        coldStorageFile.getParentFile().mkdirs();
    }

    public void put(String key, String value) {
        hotStorage.put(key, value);
        try (FileOutputStream fos = new FileOutputStream(coldStorageFile, true)) {
            BinaryEncoder.encode(fos, key, value);
        } catch (IOException e) {
            if (Config.ENABLE_LOGGING) {
                System.err.println("[ERROR] Gagal menyimpan ke cold storage: " + e.getMessage());
            }
        }
    }

    public String get(String key) {
        if (hotStorage.containsKey(key)) {
            return hotStorage.get(key);
        }

        // Jika tidak ditemukan di hot, cari di cold dan promote ke hot
        try (FileInputStream fis = new FileInputStream(coldStorageFile)) {
            Map<String, String> decoded = BinaryEncoder.decodeAll(fis);
            if (decoded.containsKey(key)) {
                String val = decoded.get(key);
                hotStorage.put(key, val); // promote ke hot
                return val;
            }
        } catch (IOException e) {
            if (Config.ENABLE_LOGGING) {
                System.err.println("[ERROR] Gagal membaca dari cold storage: " + e.getMessage());
            }
        }
        return null;
    }

    public boolean containsKey(String key) {
        return hotStorage.containsKey(key);
    }
}
