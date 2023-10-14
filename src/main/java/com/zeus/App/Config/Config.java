package com.zeus.App.Config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Config {
    /**
     * @JsonProperty("target") maps the target field in the Config class to the "target" property in JSON.
     */
    @JsonProperty("target")
    String target = null;
    @JsonProperty("properties")
    Properties properties = null;

    /**
     * getter Target.
     * @return target.
     */
    public String getTarget() {
        return target;
    }

    /**
     * getter properties.
     * @return properties.
     */
    public Properties getProperties() {
        return properties;
    }
}
