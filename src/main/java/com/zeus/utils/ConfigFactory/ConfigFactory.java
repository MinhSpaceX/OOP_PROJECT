package com.zeus.utils.ConfigFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeus.utils.config.Config;
import com.zeus.utils.log.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to handle {@link Config} objects.
 */
public class ConfigFactory {
    private final Map<String, Config> configs = new HashMap<>();

    /**
     * Constructor with given path of config. It will load the configs
     * in file into {@link Config} objects then store in a map with config's name
     * as key and config's {@link com.zeus.utils.config.Properties} as value.
     *
     * @param path The path to config file.
     */
    public ConfigFactory(String path) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Config> configList = objectMapper.readValue(new File(path), objectMapper.getTypeFactory().constructCollectionType(List.class, Config.class));
            for (Config config : configList) {
                configs.put(config.getTarget(), config);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
    }

    /**
     * Get the {@link Config}.
     *
     * @param target Config name.
     * @return {@link Config}
     */
    public Config getConfig(String target) {
        return configs.get(target);
    }
}
