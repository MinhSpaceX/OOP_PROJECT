package com.zeus.utils.ConfigFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeus.utils.config.Config;
import com.zeus.utils.log.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigFactory {
    private final Map<String, Config> configs = new HashMap<>();

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

    public Config getConfig(String target) {
        return configs.get(target);
    }
}
