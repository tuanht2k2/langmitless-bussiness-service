package com.kma.engfinity.enums;

public enum EAccountStatus {

    ONLINE("User online"),
    OFFLINE("User offline"),
    IN_CALL("User is in a call"),
    BLOCKED("User is blocked");

    private final String status;

    private EAccountStatus(String status) {
        this.status = status;
    }

    private String getStatus () {
        return status;
    }

}
