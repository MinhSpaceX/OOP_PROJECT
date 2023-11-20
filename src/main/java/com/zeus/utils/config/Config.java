package com.zeus.utils.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeus.utils.log.Logger;

public class Config {
    /**
     * @JsonProperty("target") maps the target field in the Config class to the "target" property in JSON.
     */
    @JsonProperty("target")
     String target = null;
    @JsonProperty("properties")
    Properties properties = null;

    /**
     * Get target.
     * @return target.
     */
    public String getTarget() {
        return target;
    }

    /**
     * Get properties.
     * @return properties.
     */
    public <T> T getProperty(String property, Class<T> typeClass) {
        Object o = properties.getProperties().get(property);
        if (typeClass.isInstance(o)) {
            return typeClass.cast(o);
        }
        Logger.warn("Property doesn't exist. Re-check property type or create new property.");
        return null;
    }
}
