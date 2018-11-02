package com.ecolab.mike.genusbar_sdk.utils;

public class OAuth {
    public static final String KEY_TOKEN = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";




    // debug 使用
    private static boolean debug_remove_auto_token = false;

    public static boolean getRemoveAutoTokenState() {
        return debug_remove_auto_token;
    }
}
