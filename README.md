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
│   └── cli/                        # CLI atau main class untuk testing
│       └── Main.java               # Untuk menjalankan eksperimen (put/get dsb)
│
├── data/                           # Folder penyimpanan file cold storage
│   ├── colddata-shard0.dat
│   ├── colddata-shard1.dat
│   └── ...                         # Sesuai jumlah shard
│
├── test/                           # Folder pengujian manual/unit test
│   └── KVStoreTest.java
│
├── README.md                       # Dokumentasi proyek
├── .gitignore                      # Abaikan *.class, /data, dll
```