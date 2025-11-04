package com.example.scrmbl;

public final class StorageUnit {
    String userText;

    public StorageUnit(String userText) {
        if (userText == null) {
            throw new IllegalArgumentException("Input is Required");
        }
        this.userText = userText;
    }

    public String getUserText() {
        return userText;
    }


}
