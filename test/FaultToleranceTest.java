package test;

import kvstore.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

public class FaultToleranceTest {
    public static void main(String[] args) throws Exception {
        KVStoreLite store = new KVStoreLite();

        // 1️⃣ Simpan data ke shard (akan otomatis direplikasi)
        String key = "backupTest";
        String value = "tahanGagal";
        store.put(key, value, true); // pakai replikasi sinkron

        // 2️⃣ Cari tahu lokasi file shard utama
        int shardId = store.getShardManager().getShardId(key);
        String primaryPath = Config.getShardDataFile(shardId);
        File primaryFile = new File(primaryPath);

        // 3️⃣ Hapus file utama (simulasi kerusakan shard)
        if (primaryFile.exists()) {
            System.out.println("[TEST] Menghapus file utama: " + primaryPath);
            boolean deleted = primaryFile.delete();
            System.out.println(deleted ? "[OK] File utama dihapus." : "[X] Gagal menghapus file utama.");
        }

        // 4️⃣ Ambil data dari replica (misal replica0 = sync)
        String replicaPath = Config.getReplicaDataFile(shardId, 0); // sinkron
        File replicaFile = new File(replicaPath);
        if (!replicaFile.exists()) {
            System.out.println("[X] File replika tidak ditemukan.");
            return;
        }

        Map<String, String> data = BinaryEncoder.decodeAll(new FileInputStream(replicaFile));
        if (data.containsKey(key)) {
            System.out.println("[PASS] Data berhasil dibaca dari replica:");
            System.out.println("Key: " + key + " → Value: " + data.get(key));
        } else {
            System.out.println("[FAIL] Data tidak tersedia di replica.");
        }
    }
}
