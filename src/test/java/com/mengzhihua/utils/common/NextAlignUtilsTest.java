package com.mengzhihua.utils.common;


import java.nio.charset.StandardCharsets;

import com.mengzhihua.utils.common.codec.Bech32Util;
import com.mengzhihua.utils.common.concurrent.JumpHashUtil;
import com.mengzhihua.utils.common.crypto.AesKwUtil;
import com.mengzhihua.utils.common.crypto.Blake2bUtil;
import com.mengzhihua.utils.common.crypto.Ed25519Util;
import com.mengzhihua.utils.common.id.TsidUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.time.GanZhiUtil;
import com.mengzhihua.utils.common.validate.AbaRoutingUtil;
import com.mengzhihua.utils.common.validate.Iso6346Util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NextAlignUtilsTest {

    @Test
    void blake2bOfficial() {
        assertEquals("786a02f742015903c6c6fd852552d272912f4740e15847618a86e217f71f5419d25e1031afee585313896444934eb04b903a685b1448b755d56f701afe9be2ce",
                Blake2bUtil.hash(""));
        assertEquals("ba80a53f981c4d0d6a2797b69f12f6e94c212f14685ac4b74b12bb6fdbffa2d17d87c5392aab792dc252d5de4533cc9518d38aa8dbf1925ab92386edd4009923",
                Blake2bUtil.hash("abc"));
        assertEquals(64, Blake2bUtil.digest("abc".getBytes(StandardCharsets.UTF_8)).length);
    }

    @Test
    void aesKwRfc3394() {
        assertEquals("1FA68B0A8112B447AEF34BD8FB5A7B829D3E862371D2CFE5",
                AesKwUtil.wrapHex("000102030405060708090A0B0C0D0E0F", "00112233445566778899AABBCCDDEEFF"));
        assertEquals("00112233445566778899AABBCCDDEEFF",
                AesKwUtil.unwrapHex("000102030405060708090A0B0C0D0E0F",
                        "1FA68B0A8112B447AEF34BD8FB5A7B829D3E862371D2CFE5"));
    }

    @Test
    void ed25519Rfc8032Test1() {
        String seed = "9d61b19deffd5a60ba844af492ec2cc44449c5697b326919703bac031cae7f60";
        String pub = "d75a980182b10ab7d54bfed3c964073a0ee172f3daa62325af021a68f707511a";
        String signature = Ed25519Util.signHex(seed, "");
        assertEquals("e5564300c360ac729086e2cc806e828a84877f1eb8e5d974d873e065224901555fb8821590a33bacc61e39701cf9b46bd25bf5f0595bbe24655141438e7a100b",
                signature);
        assertTrue(Ed25519Util.verifyHex(pub, "", signature));
        assertFalse(Ed25519Util.verifyHex(pub, "x", signature));
    }

    @Test
    void tsidGanZhiJump() {
        long id = TsidUtil.next(1_700_000_000_000L, 7, 9);
        assertEquals(7, TsidUtil.node(id));
        assertEquals(9, TsidUtil.sequence(id));
        assertEquals(1_700_000_000_000L, TsidUtil.unixMillis(id));
        assertEquals(13, TsidUtil.encode(id).length());

        assertEquals("甲子", GanZhiUtil.year(1984));
        assertEquals("丙午", GanZhiUtil.year(2026));
        assertEquals("马", GanZhiUtil.animal(2026));
        assertEquals("甲辰", GanZhiUtil.year(2024));

        assertEquals(0, JumpHashUtil.jump(0, 1));
        int bucket = JumpHashUtil.jump(1, 100);
        assertTrue(bucket >= 0 && bucket < 100);
        assertEquals(bucket, JumpHashUtil.jump(1, 100));
    }

    @Test
    void iso6346AbaBech32m() {
        assertTrue(Iso6346Util.isValid("CSQU3054383"));
        assertEquals("3", String.valueOf(Iso6346Util.checkDigit("CSQU305438")));
        assertFalse(Iso6346Util.isValid("CSQU3054380"));
        assertTrue(AbaRoutingUtil.isValid("021000021"));
        assertFalse(AbaRoutingUtil.isValid("021000022"));
        assertTrue(RegexUtil.is("iso6346", "CSQU3054383"));
        assertTrue(RegexUtil.is("aba", "021000021"));

        String empty = Bech32Util.encodeM("a", new byte[0]);
        assertEquals("a1lqfn3a", empty);
        assertEquals("a", Bech32Util.decodeM(empty).hrp());
        String round = Bech32Util.encodeM("demo", "hello".getBytes(StandardCharsets.UTF_8));
        assertEquals("hello", new String(Bech32Util.decodeM(round).data(), StandardCharsets.UTF_8));
    }
}
