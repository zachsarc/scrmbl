package com.example.scrmbl;// SubstitutionMap.java
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class SubstitutionMap {

    private SubstitutionMap() {}

    // Build a deterministic permutation from a passphrase and return encMap
    public static Map<Byte, Byte> generateMapFromPassphrase(String passphrase) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] seed = sha.digest(passphrase.getBytes(StandardCharsets.UTF_8));

        long longSeed = 0L;
        for (int i = 0; i < 8; i++) {
            longSeed = (longSeed << 8) | (seed[i] & 0xFFL);
        }
        Random rnd = new Random(longSeed); // deterministic

        byte[] vals = new byte[256];
        for (int i = 0; i < 256; i++) vals[i] = (byte) i;

        // Fisher–Yates
        for (int i = vals.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            byte tmp = vals[i];
            vals[i] = vals[j];
            vals[j] = tmp;
        }

        HashMap<Byte, Byte> map = new HashMap<>(256);
        for (int i = 0; i < 256; i++) {
            map.put((byte) i, vals[i]);
        }
        return map;
    }

    // Invert encMap to get decMap
    public static Map<Byte, Byte> invertMap(Map<Byte, Byte> enc) {
        HashMap<Byte, Byte> inv = new HashMap<>(256);
        for (Map.Entry<Byte, Byte> e : enc.entrySet()) {
            inv.put(e.getValue(), e.getKey());
        }
        return inv;
    }
}
