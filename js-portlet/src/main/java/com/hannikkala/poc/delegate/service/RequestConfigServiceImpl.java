package com.hannikkala.poc.delegate.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.hannikkala.poc.delegate.config.RequestConfig;
import com.hannikkala.poc.delegate.config.RequestConfigList;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Tommi Hännikkälä tommi@hannikkala.com
 * Date: 11/03/16
 * Time: 13:59
 */
@Service
public class RequestConfigServiceImpl implements InitializingBean {


    private List<Map<String, String>> mappings;

    private final Set<RequestConfig> configSet = new HashSet<>();

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private Environment env;

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
        String restResource = env.getProperty("jsportlet.rest.resource", "classpath:application.properties");
        Resource jsonFile = resourceLoader.getResource(restResource);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        RequestConfigList list = mapper.readValue(jsonFile.getFile(), RequestConfigList.class);
        configSet.addAll(list.getConfigurations());
    }
}
