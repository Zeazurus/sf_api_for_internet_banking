package org.bankapi.sql;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OperationEntity {
    @JsonProperty
    String date;
    @JsonProperty
    int operationTypeId;
    @JsonProperty
    double amount;

    public OperationEntity(String dateTime, int type, double amount) {
        this.date = dateTime;
        this.operationTypeId = type;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getOperationTypeId() {
        return operationTypeId;
    }

    public void setOperationTypeId(int operationTypeId) {
        this.operationTypeId = operationTypeId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
