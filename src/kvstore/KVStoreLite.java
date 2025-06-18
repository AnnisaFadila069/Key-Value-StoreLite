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
     * Menyimpan pasangan key-value ke hot & cold storage, dan replikasi ke 2 replica (sync + async).
     */
    public void put(String key, String value) {
        int shardId = shardManager.getShardId(key);
        ShardNode primaryShard = shardNodes.get(shardId);
        primaryShard.put(key, value);
        replicaManager.replicate(key, value, shardId); // selalu jalankan sync + async
    }

    /**
     * Overload agar kompatibel dengan pemanggilan yang masih menggunakan parameter syncReplica.
     * Meskipun parameter ini diabaikan, tetap kompatibel dengan signature sebelumnya.
     */
    public void put(String key, String value, boolean syncReplica) {
        this.put(key, value);
    }

    public ShardManager getShardManager() {
        return this.shardManager;
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
     * Tambahan: Mengecek apakah suatu key ada di hot storage.
     */
    public boolean containsKey(String key) {
        int shardId = shardManager.getShardId(key);
        ShardNode primaryShard = shardNodes.get(shardId);
        return primaryShard.containsKey(key);
    }

    /**
     * Digunakan untuk menghentikan thread async sebelum keluar CLI.
     */
    public void shutdown() {
        replicaManager.shutdown();
    }
}
