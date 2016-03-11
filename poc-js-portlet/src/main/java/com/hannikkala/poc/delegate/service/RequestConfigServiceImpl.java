package com.hannikkala.poc.delegate.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hannikkala.poc.delegate.config.RequestConfig;
import com.hannikkala.poc.delegate.config.RequestConfigList;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: bleed
 * Date: 11/03/16
 * Time: 13:59
 */
@Service
public class RequestConfigServiceImpl implements InitializingBean {


    private List<Map<String, String>> mappings;

    private final Set<RequestConfig> configSet = new HashSet<>();

    public RequestConfig findConfiguration(String path) {
        for(RequestConfig config : configSet) {
            if(config.matches(path)) {
                return config;
            }
        }
        throw new RuntimeException("No mapping found for path: " + path);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Resource jsonFile = new ClassPathResource("application.json");
        ObjectMapper mapper = new ObjectMapper();
        RequestConfigList list = mapper.readValue(jsonFile.getFile(), RequestConfigList.class);
        configSet.addAll(list.getConfigurations());

        /*for(Map<String, String> mapping : mappings) {
            for (Map.Entry<String, String> entry : mapping.entrySet()) {
                configSet.add(new RequestConfig(entry.getKey(), entry.getValue()));
            }
        }*/
    }
}
