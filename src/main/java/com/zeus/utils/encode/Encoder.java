package com.zeus.utils.encode;

import java.util.UUID;

public class Encoder {
    public static String encode(String word) {
        return UUID.nameUUIDFromBytes(word.getBytes()).toString();
    }
}
