package org.bankapi.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseJSON
{
    @JsonProperty
    private double value;
    @JsonProperty
    private String text;

    public ResponseJSON(double value, String text) {
        this.value = value;
        this.text = text;
    }
}
