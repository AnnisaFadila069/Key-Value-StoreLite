package test;

import kvstore.KVStoreLite;

public class PerformanceTest {
    public static void main(String[] args) {
        KVStoreLite store = new KVStoreLite();

        int ops = 10000; // jumlah operasi yang ingin diuji

        // 1️⃣ Uji throughput untuk PUT
        long startPut = System.nanoTime();
        for (int i = 0; i < ops; i++) {
            store.put("key" + i, "value" + i);
        }
        long endPut = System.nanoTime();
        double timePutSec = (endPut - startPut) / 1_000_000_000.0;
        System.out.printf("PUT Throughput: %.2f ops/sec\n", ops / timePutSec);

        // 2️⃣ Uji throughput untuk GET
        long startGet = System.nanoTime();
        for (int i = 0; i < ops; i++) {
            store.get("key" + i); // ambil dari hot storage
        }
        long endGet = System.nanoTime();
        double timeGetSec = (endGet - startGet) / 1_000_000_000.0;
        System.out.printf("GET Throughput (hot): %.2f ops/sec\n", ops / timeGetSec);
    }
}
