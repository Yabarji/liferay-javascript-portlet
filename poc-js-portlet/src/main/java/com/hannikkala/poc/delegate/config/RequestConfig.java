package com.hannikkala.poc.delegate.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.AntPathMatcher;

/**
 * User: bleed
 * Date: 11/03/16
 * Time: 13:53
 */
public class RequestConfig {

    private String pattern;
    private String location;

    @JsonCreator
    public RequestConfig(@JsonProperty("pattern") String pattern, @JsonProperty("location") String baseURL) {
        this.pattern = pattern;
        this.location = baseURL;
    }

    public boolean matches(String uri) {
        return new AntPathMatcher().match(pattern, uri);
    }

    public String getPattern() {
        return pattern;
    }

    public String getLocation() {
        return location;
    }
}
