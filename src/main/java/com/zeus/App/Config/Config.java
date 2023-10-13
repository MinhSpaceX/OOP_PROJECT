package com.zeus.App.Config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Config {
    @JsonProperty("target")
    String target = null;
    @JsonProperty("properties")
    Properties properties = null;

    public String getTarget() {
        return target;
    }

    public Properties getProperties() {
        return properties;
    }
}
