// ShardManager.java
package kvstore;

import java.util.Objects;

public class ShardManager {
    private final int numShards;

    public ShardManager(int numShards) {
        this.numShards = numShards;
    }

    /**
     * Menghasilkan ID shard berdasarkan hash key
     * Pastikan hasilnya konsisten dan terdistribusi
     */
    public int getShardId(String key) {
        return Math.floorMod(Objects.hashCode(key), numShards);
    }

    /**
     * Debug: Lihat hasil distribusi
     */
    public void printShardDistribution(String[] sampleKeys) {
        for (String key : sampleKeys) {
            int shard = getShardId(key);
            System.out.printf("Key '%s' masuk ke Shard %d\n", key, shard);
        }
    }
}