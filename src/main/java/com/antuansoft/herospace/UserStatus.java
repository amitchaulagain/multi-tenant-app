package com.antuansoft.herospace;


import java.io.Serializable;

public enum UserStatus implements Serializable {

    INACTIVE("Inactive"), ACTIVE("Active"), ACCOUNT_BLOCKED("Account"), TRANSACTION_BLOCKED("Transaction"), DELETED("Deleted");

    private final String value;

    private UserStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public static UserStatus getEnum(String value) {
        if (value == null)
            throw new IllegalArgumentException();
        for (UserStatus v : values())
            if (value.equalsIgnoreCase(v.getValue()))
                return v;
        throw new IllegalArgumentException();
    }
}