package kvstore;

import java.nio.file.Paths;

public class Config {

    // 📦 Jumlah shard utama (primary)
    public static final int NUM_SHARDS = 2;

    // 🔁 Jumlah replica per shard (1 sync, 1 async jika diperluas)
    public static final int NUM_REPLICAS = 2;

    // 📁 Direktori penyimpanan cold storage
    public static final String DATA_DIR = "data";

    // 📄 Format nama file per shard 
    public static String getShardDataFile(int shardId) {
        return Paths.get(DATA_DIR, "colddata-shard" + shardId + ".dat").toString();
    }

    // 📄 Format nama file replica (jika dipisah per replica mode)
    public static String getReplicaDataFile(int shardId, int replicaId) {
        return Paths.get(DATA_DIR, "colddata-shard" + shardId + "-replica" + replicaId + ".dat").toString();
    }

    // 🧠 Encoding biner: versi saat ini (dapat diubah saat runtime untuk testing)
    private static byte CURRENT_ENCODING_VERSION = 2;
    public static byte getCurrentEncodingVersion() {
        return CURRENT_ENCODING_VERSION;
    }
    public static void setCurrentEncodingVersion(byte version) {
        CURRENT_ENCODING_VERSION = version;
    }

    // 🧠 Opsi schema evolusi: dukungan type flag
    public static final boolean SUPPORT_TYPE_FLAG = true;

    // 🔥 Hot Storage config: batas jumlah item sebelum flush
    public static final int HOT_STORAGE_CAPACITY = 1000;

    // ⏱ TTL (opsional) dalam ms untuk eksperimen skema evolusi lanjutan
    public static final long DEFAULT_TTL_MS = 5 * 60 * 1000; // 5 menit

    // 🔁 Default replikasi: asynchronous
    public static final boolean DEFAULT_REPLICATION_SYNC = false;

    // 🔍 Logging flag
    public static final boolean ENABLE_LOGGING = true;

    // 🧪 CLI display: hex mode atau tidak
    public static final boolean SHOW_HEX_DUMP = true;
}
