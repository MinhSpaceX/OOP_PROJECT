package com.zeus.utils.config;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Property class.
 */
public class Properties {
    private final Map<String, Object> properties = new HashMap<>();

    /**
     * Get the map contains properties with property's name as
     * key and property's value as map's value.
     *
     * @return The map contains properties.
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * Add the pair of key and value from JsonNode format to the Map.
     *
     * @param key   The key.
     * @param value The value.
     */
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
