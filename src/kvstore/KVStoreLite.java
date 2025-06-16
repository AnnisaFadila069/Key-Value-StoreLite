// KVStoreLite.java
package kvstore;

import java.util.List;
import java.util.ArrayList;

public class KVStoreLite {

    private final List<ShardNode> shardNodes;
    private final ShardManager shardManager;
    private final ReplicaManager replicaManager;

    public KVStoreLite() {
        this.shardNodes = new ArrayList<>();
        for (int i = 0; i < Config.NUM_SHARDS; i++) {
            shardNodes.add(new ShardNode(i));
        }
        this.shardManager = new ShardManager(Config.NUM_SHARDS);
        this.replicaManager = new ReplicaManager(shardNodes);
    }

    /**
     * Menyimpan pasangan key-value ke hot & cold storage, dan replikasi ke replica.
     * Gunakan default mode replikasi (sync/non-sync) dari konfigurasi.
     */
    public void put(String key, String value) {
        put(key, value, Config.DEFAULT_REPLICATION_SYNC);
    }

    public ShardManager getShardManager() {
        return this.shardManager;
    }    

    /**
     * Overload dengan parameter sync: true untuk sinkron, false untuk asinkron.
     */
    public void put(String key, String value, boolean syncReplica) {
        int shardId = shardManager.getShardId(key);
        ShardNode primaryShard = shardNodes.get(shardId);
        primaryShard.put(key, value);

        if (syncReplica) {
            // Simulasi mode sync
            if (Config.ENABLE_LOGGING) {
                System.out.println("[INFO] Menyimpan data dengan replikasi sinkron.");
            }
            replicaManager.replicate(key, value, shardId);
        } else {
            // Simulasi mode async saja
            if (Config.ENABLE_LOGGING) {
                System.out.println("[INFO] Menyimpan data dengan replikasi asinkron.");
            }
            // Hanya jalankan async
            replicaManager.replicate(key, value, shardId);
        }
    }

    /**
     * Mengambil nilai dari hot storage (fallback ke cold jika tidak ada).
     */
    public String get(String key) {
        int shardId = shardManager.getShardId(key);
        ShardNode primaryShard = shardNodes.get(shardId);
        return primaryShard.get(key);
    }

    /**
     * Tambahan: Mengecek apakah suatu key ada di hot storage (untuk eksperimen / CLI).
     */
    public boolean containsKey(String key) {
        int shardId = shardManager.getShardId(key);
        ShardNode primaryShard = shardNodes.get(shardId);
        return primaryShard.containsKey(key);
    }

    public void shutdown() {
        replicaManager.shutdown(); // hentikan executor async
    }
    
}
 