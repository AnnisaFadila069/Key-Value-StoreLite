// KVStoreTest.java
package test;

import kvstore.*;
import java.io.*;
import java.util.Map;

public class KVStoreTest {

    public static void main(String[] args) throws Exception {
        System.out.println("== TEST: Basic Put-Get ==");
        KVStoreLite store = new KVStoreLite();
        store.put("alpha", "123");
        store.put("beta", "456");

        assert store.get("alpha").equals("123");
        assert store.get("beta").equals("456");
        System.out.println("[PASS] Put-Get");

        System.out.println("\n== TEST: Cold Promotion ==");
        int shardId = store.getShardManager().getShardId("gamma");
        store.put("gamma", "cold-data");

        // Simulasi restart: buat store baru, key tidak ada di hot
        store = new KVStoreLite();
        String val = store.get("gamma");
        assert val.equals("cold-data");
        System.out.println("[PASS] Promosi cold ke hot");

        System.out.println("\n== TEST: Replikasi ==");
        File replicaFile = new File(Config.getReplicaDataFile(shardId, 0));
        assert replicaFile.exists() && replicaFile.length() > 0;
        System.out.println("[PASS] Replikasi sinkron tersimpan ke file");

        System.out.println("\n== TEST: Evolusi Skema (Versi >= 2) ==");
        Config.setCurrentEncodingVersion((byte) 2);;
        store.put("evolve1", "v2data");
        Map<String, String> decoded = BinaryEncoder.decodeAll(new FileInputStream(Config.getShardDataFile(store.getShardManager().getShardId("evolve1"))));
        assert decoded.get("evolve1").equals("v2data");
        System.out.println("[PASS] Skema evolusi v2 terbaca dengan benar");

        System.out.println("\n== TEST: Simulasi Failover (baca dari replica) ==");
        String fallbackPath = Config.getReplicaDataFile(shardId, 0);
        Map<String, String> replicaData = BinaryEncoder.decodeAll(new FileInputStream(fallbackPath));
        assert replicaData.get("gamma").equals("cold-data");
        System.out.println("[PASS] Data tetap tersedia di replica");
    }
}