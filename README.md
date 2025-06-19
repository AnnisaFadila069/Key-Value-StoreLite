# Final Project Sistem Data Intensif - KVStoreLite
## Deskripsi Proyek
📦 Sistem penyimpanan key-value sederhana dengan konsep hot & cold storage, partisi, dan replikasi.

## Fitur Utama

- ✅ Penyimpanan key-value tanpa database eksternal
- 🔥 Hot storage: data aktif di memori
- ❄️ Cold storage: data jarang diakses dalam file
- 🔁 Replikasi antar shard
- 🧩 Partisi berdasarkan hash key
- 💾 Penyimpanan dalam format biner (encoded)

## Struktur Direktori
```
KVStoreLite/
├── src/
│   ├── kvstore/                    # Paket utama penyimpanan
│   │   ├── KVStoreLite.java        # Class utama (API: put/get)
│   │   ├── ShardManager.java       # Mengelola pembagian key ke shard
│   │   ├── ShardNode.java          # Representasi satu shard (hot & cold)
│   │   ├── ReplicaManager.java     # Menangani replikasi antar shard
│   │   ├── BinaryEncoder.java      # Untuk encoding dan decoding ke biner
│   │   └── Config.java             # Konfigurasi jumlah shard, path file, dll
│   │
│   ├── Main.java    # Untuk menjalankan eksperimen (put/get dsb)
│                   
│
├── data/                           # Folder penyimpanan file cold storage
│   ├── colddata-shard0.dat
│   ├── colddata-shard1.dat
│   └── ...                         # Sesuai jumlah shard
│
├── test/                           # Folder pengujian manual/unit test
│   ├── KVStoreTest.java
│   └── PerformanceTest.java        # pengujian performa troughout
│   └── LatencyTest.java            # pengujian performa latency
│   └── FaultToleranceTest.java     # pengujian performa ketahanan
│
├── README.md                       # Dokumentasi proyek
├── .gitignore                      # Abaikan *.class, /data, dll
```
## Cara Menjalankan KVStoreLite
1. Git Clone repository ini
2. Buka terminal dan pastikan sudah berada pada directory KVStoreLite
3. Ketik perintah berikut pada terminal:
   ``` find src -name "*.java" > sources.txt ```
5. Ketik perintah berikut pada terminal:
   ``` javac -d out @sources.txt ```
7. Ketik perintah berikut pada terminal:
   ``` java -cp out Main ```

Gunakan perintah interaktif di CLI seperti:
```
put <key> <value>       - Menyimpan pasangan key-value
get <key>               - Mengambil nilai berdasarkan key
check <key>             - Mengecek apakah key ada di hot storage
hexdump <shardId>       - Melihat isi file biner pada shard tertentu
testshard <key1> ...    - Menampilkan ke shard mana key akan masuk
exit                    - Keluar dari aplikasi
```
