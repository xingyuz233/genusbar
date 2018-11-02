package com.ecolab.mike.genusbar_sdk.utils;

import java.util.UUID;

public class UUIDGenerator {
    private UUIDGenerator() {
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String[] getUUID(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = getUUID();
        }
        return ss;
    }
}
