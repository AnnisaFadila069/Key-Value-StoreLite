// ReplicaManager.java
package kvstore;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReplicaManager {

    private final List<ShardNode> primaryShards;
    private final ExecutorService asyncExecutor;

    public ReplicaManager(List<ShardNode> primaryShards) {
        this.primaryShards = primaryShards;
        this.asyncExecutor = Executors.newFixedThreadPool(Config.NUM_SHARDS * Config.NUM_REPLICAS);
    }

    /**
     * Lakukan replikasi data ke dua replica: satu sync, satu async
     */
    public void replicate(String key, String value, int shardId) {
        // Simulasi replica 1: async (langsung dikirim ke executor)
        asyncExecutor.submit(() -> {
            if (Config.ENABLE_LOGGING) {
                System.out.println("[ASYNC] Mereplikasi (key=" + key + ") ke replica async shard " + shardId);
            }
            simulateReplicaWrite(key, value, shardId, "async");
        });

        // Simulasi replica 2: sync (langsung jalankan dan tunggu selesai)
        if (Config.ENABLE_LOGGING) {
            System.out.println("[SYNC] Mereplikasi (key=" + key + ") ke replica sync shard " + shardId);
        }
        simulateReplicaWrite(key, value, shardId, "sync");
    }

    /**
     * Simulasi penulisan ke replica
     */
    private void simulateReplicaWrite(String key, String value, int shardId, String mode) {
        try {
            String path = Config.getReplicaDataFile(shardId, mode.equals("sync") ? 0 : 1);
            File file = new File(path);
            file.getParentFile().mkdirs(); // pastikan folder tersedia
            BinaryEncoder.encode(new FileOutputStream(file, true), key, value);
        } catch (Exception e) {
            if (Config.ENABLE_LOGGING) {
                System.err.println("[ERROR] Gagal menulis ke replica " + mode + ": " + e.getMessage());
            }
        }
    }

    public void shutdown() {
        asyncExecutor.shutdown();
    }
}
