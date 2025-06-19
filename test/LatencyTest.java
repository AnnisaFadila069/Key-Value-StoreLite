package test;

import kvstore.KVStoreLite;

public class LatencyTest {
    public static void main(String[] args) {
        KVStoreLite store = new KVStoreLite();

        // Simpan 1 data (akan langsung masuk ke hot storage)
        store.put("hotKey", "hotValue");

        // Simpan 1 data untuk cold (dengan cara restart store)
        store.put("coldKey", "coldValue");
        store = new KVStoreLite(); // simulasikan cold start (data pindah ke cold)

        // 1️⃣ Uji latency dari hot storage
        long startHot = System.nanoTime();
        String v1 = store.get("hotKey");
        long endHot = System.nanoTime();
        double latencyHot = (endHot - startHot) / 1_000_000.0; // ms

        // 2️⃣ Uji latency dari cold storage
        long startCold = System.nanoTime();
        String v2 = store.get("coldKey");
        long endCold = System.nanoTime();
        double latencyCold = (endCold - startCold) / 1_000_000.0; // ms

        // 🔍 Tampilkan hasil
        System.out.printf("Latency HOT  (from HashMap) : %.3f ms\n", latencyHot);
        System.out.printf("Latency COLD (from File)    : %.3f ms\n", latencyCold);
    }
}
