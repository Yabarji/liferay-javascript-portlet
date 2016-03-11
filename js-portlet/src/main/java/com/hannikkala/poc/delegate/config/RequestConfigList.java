package com.hannikkala.poc.delegate.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * User: bleed
 * Date: 11/03/16
 * Time: 18:04
 */
public class RequestConfigList {
    @JsonProperty("rest")
    private List<RequestConfig> rest = new ArrayList<>();

    public List<RequestConfig> getConfigurations() {
        return rest;
    }
}
