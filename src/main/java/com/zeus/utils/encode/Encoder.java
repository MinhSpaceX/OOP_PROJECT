package com.zeus.utils.encode;

import java.util.UUID;

/**
 * Class handle encoding.
 */
public class Encoder {
    /**
     * Encode the word target into a unique string
     * (UUID - Universal Unique Identifier).
     *
     * @param word Word target to encode.
     * @return Encoded word target.
     */
    public static String encode(String word) {
        return UUID.nameUUIDFromBytes(word.getBytes()).toString();
    }
}
