package com.apinizer.apitest.apinizerrequest;

public enum EnumHttpRequestMethod {
    GET, POST, PUT, HEAD, OPTIONS, DELETE, PATCH, TRACE, ALL;

    public boolean isGet() {
        return this.equals(GET);
    }

    public boolean isPost() {
        return this.equals(POST);
    }

    public boolean isPut() {
        return this.equals(PUT);
    }

    public boolean isHead() {
        return this.equals(HEAD);
    }

    public boolean isOptions() {
        return this.equals(OPTIONS);
    }

    public boolean isDelete() {
        return this.equals(DELETE);
    }

    public boolean isPatch() {
        return this.equals(PATCH);
    }

    public boolean isTrace() {
        return this.equals(TRACE);
    }

    public boolean isAll() {
        return this.equals(ALL);
    }

    public boolean isRequestBodyDisabled() {
        return this.equals(OPTIONS);
    }
}