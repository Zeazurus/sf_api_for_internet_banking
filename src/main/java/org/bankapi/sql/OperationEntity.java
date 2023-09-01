package org.bankapi.sql;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OperationEntity {
    @JsonProperty
    String dateTime;
    @JsonProperty
    int type;
    @JsonProperty
    double amount;

    public OperationEntity(String dateTime, int type, double amount) {
        this.dateTime = dateTime;
        this.type = type;
        this.amount = amount;
    }
}
