import kvstore.KVStoreLite;
import kvstore.Config;
import kvstore.BinaryEncoder;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        KVStoreLite store = new KVStoreLite();
        Scanner scanner = new Scanner(System.in);

        printWelcome();

        while (true) {
            System.out.print("\u001B[36mKV > \u001B[0m");  // Cyan prompt
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+", 3);
            String cmd = parts[0].toLowerCase();

            switch (cmd) {
                case "put":
                    if (parts.length < 3) {
                        printWarn("Usage: put <key> <value>");
                        break;
                    }
                    store.put(parts[1], parts[2]);
                    printOk("Data dengan key '" + parts[1] + "' berhasil disimpan.");
                    break;

                case "get":
                    if (parts.length < 2) {
                        printWarn("Usage: get <key>");
                        break;
                    }
                    String val = store.get(parts[1]);
                    if (val != null)
                        printOk("Nilai dari key '" + parts[1] + "' adalah: " + val);
                    else
                        printErr("Key '" + parts[1] + "' tidak ditemukan.");
                    break;

                case "check":
                    if (parts.length < 2) {
                        printWarn("Usage: check <key>");
                        break;
                    }
                    boolean exists = store.containsKey(parts[1]);
                    System.out.println(exists
                            ? "[INFO] Key '" + parts[1] + "' tersedia di hot storage."
                            : "[INFO] Key '" + parts[1] + "' tidak ada di hot storage.");
                    break;

                case "hexdump":
                    if (parts.length < 2) {
                        printWarn("Usage: hexdump <shardId>");
                        break;
                    }
                    try {
                        int shardId = Integer.parseInt(parts[1]);
                        if (shardId < 0 || shardId >= Config.NUM_SHARDS) {
                            printErr("Shard ID tidak valid. Gunakan 0 - " + (Config.NUM_SHARDS - 1));
                            break;
                        }
                        System.out.println("[INFO] Isi file: " + Config.getShardDataFile(shardId));
                        BinaryEncoder.hexdump(Config.getShardDataFile(shardId));
                    } catch (NumberFormatException e) {
                        printErr("Shard ID harus berupa angka.");
                    }
                    break;

                case "testshard":
                    if (parts.length < 2) {
                        printWarn("Usage: testshard <key1> <key2> ...");
                        break;
                    }
                    String[] keys = line.substring("testshard".length()).trim().split("\\s+");
                    store.getShardManager().printShardDistribution(keys);
                    break;

                case "exit":
                    printOk("Terima kasih telah menggunakan KVStoreLite!");
                    store.shutdown(); // pastikan semua thread selesai
                    System.exit(0);    // akhiri program seperti Ctrl+C
                    break;
                

                default:
                    printErr("Perintah '" + cmd + "' tidak dikenali.");
            }
        }
    }

    private static void printWelcome() {
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║         Welcome to KVStoreLite        ║");
        System.out.println("╠═══════════════════════════════════════╣");
        System.out.println("║ Perintah tersedia:                    ║");
        System.out.println("║ • put <key> <value>    → simpan data  ║");
        System.out.println("║ • get <key>             → ambil data  ║");
        System.out.println("║ • check <key>           → cek di hot  ║");
        System.out.println("║ • hexdump <shardId>     → lihat file  ║");
        System.out.println("║ • testshard <key1> ...  → cek shard   ║");
        System.out.println("║ • exit                  → keluar CLI  ║");
        System.out.println("╚═══════════════════════════════════════╝");
    }

    private static void printOk(String msg) {
        System.out.println("\u001B[32m[OK] " + msg + "\u001B[0m");
    }

    private static void printErr(String msg) {
        System.out.println("\u001B[31m[X] " + msg + "\u001B[0m");
    }

    private static void printWarn(String msg) {
        System.out.println("\u001B[33m[!] " + msg + "\u001B[0m");
    }
}
