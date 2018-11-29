package com.ecolab.mike.genusbar_sdk.bean;

import java.io.Serializable;

public class Token implements Serializable {
    private String token;

    public Token(String token) {
        this.token = token;
    }

    public String getTokenString() {
        return token;
    }
}
