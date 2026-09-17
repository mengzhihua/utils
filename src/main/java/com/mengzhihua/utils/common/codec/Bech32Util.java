package com.mengzhihua.utils.common.codec;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * BIP-173 Bech32 (Apache Commons Codec / Bitcoin addresses).
 */
public final class Bech32Util {

    private static final String CHARSET = "qpzry9x8gf2tvdw0s3jn54khce6mua7l";
    private static final int[] GEN = {0x3b6a57b2, 0x26508e6d, 0x1ea119fa, 0x3d4233dd, 0x2a1462b3};

    private Bech32Util() {
    }

    public static String encode(String hrp, byte[] data) {
        if (StringUtil.isBlank(hrp)) {
            throw new IllegalArgumentException("hrp is blank");
        }
        int[] values = convertBits(data == null ? new byte[0] : data, 8, 5, true);
        return encode5(hrp.toLowerCase(Locale.ROOT), values);
    }

    public static Decoded decode(String address) {
        if (StringUtil.isBlank(address)) {
            throw new IllegalArgumentException("bech32 is blank");
        }
        String value = address.trim();
        if (value.equals(value.toUpperCase(Locale.ROOT))) {
            value = value.toLowerCase(Locale.ROOT);
        } else if (!value.equals(value.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("mixed-case bech32");
        }
        int pos = value.lastIndexOf('1');
        if (pos < 1 || pos + 7 > value.length()) {
            throw new IllegalArgumentException("invalid bech32: " + address);
        }
        String hrp = value.substring(0, pos);
        String dataPart = value.substring(pos + 1);
        int[] data = new int[dataPart.length()];
        for (int i = 0; i < dataPart.length(); i++) {
            int idx = CHARSET.indexOf(dataPart.charAt(i));
            if (idx < 0) {
                throw new IllegalArgumentException("invalid bech32 character");
            }
            data[i] = idx;
        }
        if (polymod(cat(expandHrp(hrp), data)) != 1) {
            throw new IllegalArgumentException("invalid bech32 checksum");
        }
        int[] payload = new int[data.length - 6];
        System.arraycopy(data, 0, payload, 0, payload.length);
        byte[] decodedData;
        try {
            int[] eightBit = convertBits(toBytes(payload), 5, 8, false);
            decodedData = toBytes(eightBit);
        } catch (IllegalArgumentException ex) {
            decodedData = new byte[0];
        }
        return new Decoded(hrp, decodedData, value);
    }

    public static String encodeText(String hrp, String text) {
        return encode(hrp, text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeToString(String address) {
        return new String(decode(address).data(), StandardCharsets.UTF_8);
    }

    private static String encode5(String hrp, int[] values) {
        int[] checksum = createChecksum(hrp, values);
        int[] combined = cat(values, checksum);
        StringBuilder builder = new StringBuilder(hrp).append('1');
        for (int v : combined) {
            builder.append(CHARSET.charAt(v));
        }
        return builder.toString();
    }

    private static int[] createChecksum(String hrp, int[] values) {
        int[] expanded = cat(expandHrp(hrp), values);
        int[] withZeros = new int[expanded.length + 6];
        System.arraycopy(expanded, 0, withZeros, 0, expanded.length);
        int mod = polymod(withZeros) ^ 1;
        int[] checksum = new int[6];
        for (int i = 0; i < 6; i++) {
            checksum[i] = (mod >>> 5 * (5 - i)) & 31;
        }
        return checksum;
    }

    private static int[] expandHrp(String hrp) {
        int[] ret = new int[hrp.length() * 2 + 1];
        for (int i = 0; i < hrp.length(); i++) {
            int c = hrp.charAt(i);
            ret[i] = c >>> 5;
            ret[i + hrp.length() + 1] = c & 31;
        }
        return ret;
    }

    private static int polymod(int[] values) {
        int chk = 1;
        for (int v : values) {
            int b = chk >>> 25;
            chk = ((chk & 0x1ffffff) << 5) ^ v;
            for (int i = 0; i < 5; i++) {
                if (((b >>> i) & 1) != 0) {
                    chk ^= GEN[i];
                }
            }
        }
        return chk;
    }

    private static int[] convertBits(byte[] data, int from, int to, boolean pad) {
        int acc = 0;
        int bits = 0;
        int maxv = (1 << to) - 1;
        List<Integer> out = new ArrayList<>();
        for (byte b : data) {
            acc = (acc << from) | (b & 0xff);
            bits += from;
            while (bits >= to) {
                bits -= to;
                out.add((acc >>> bits) & maxv);
            }
        }
        if (pad) {
            if (bits > 0) {
                out.add((acc << (to - bits)) & maxv);
            }
        } else if (bits >= from || (bits > 0 && ((acc << (to - bits)) & maxv) != 0)) {
            throw new IllegalArgumentException("invalid padding in bech32 data");
        }
        return out.stream().mapToInt(Integer::intValue).toArray();
    }

    private static byte[] toBytes(int[] values) {
        byte[] bytes = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            bytes[i] = (byte) values[i];
        }
        return bytes;
    }

    private static int[] cat(int[] left, int[] right) {
        int[] out = new int[left.length + right.length];
        System.arraycopy(left, 0, out, 0, left.length);
        System.arraycopy(right, 0, out, left.length, right.length);
        return out;
    }

    public record Decoded(String hrp, byte[] data, String encoded) {
    }
}
