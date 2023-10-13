package com.zeus.App.Config;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Properties {
    private Map<String, Object> properties = new HashMap<>();

    public Map<String, Object> getProperties() {
        return properties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, JsonNode value) {
        if (value.isTextual()) {
            properties.put(name, value.textValue());
        } else if (value.isInt()) {
            properties.put(name, value.intValue());
        } else if (value.isBoolean()) {
            properties.put(name, value.booleanValue());
        }
    }
}
