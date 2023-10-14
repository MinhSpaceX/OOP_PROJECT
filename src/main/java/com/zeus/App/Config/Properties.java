package com.zeus.App.Config;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

public class Properties {
    private final Map<String, Object> properties = new HashMap<>();

    public Map<String, Object> getProperties() {
        return properties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String key, JsonNode value) {
        if (value.isTextual()) {
            properties.put(key, value.textValue());
        } else if (value.isInt()) {
            properties.put(key, value.intValue());
        } else if (value.isBoolean()) {
            properties.put(key, value.booleanValue());
        }
    }
}
