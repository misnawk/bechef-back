package com.example.bechef.status;

public enum ResultStatus {
    SUCCESS("SUCCESS"),
    FAIL("FAIL");



    private String result;

    ResultStatus(String result) {

        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
