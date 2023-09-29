package com.apinizer.apitest.apinizerrequest;

public enum EnumBodyPartType {
    TEXT, BINARY;

    public boolean isText() {
        return this.equals(TEXT);
    }

    public boolean isBinary() {
        return this.equals(BINARY);
    }
}