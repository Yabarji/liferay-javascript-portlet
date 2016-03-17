package com.hannikkala.poc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tommi Hännikkälä <tommi@hannikkala.com>
 * Date: 11/03/16
 * Time: 15:51
 */
@Configuration
@ComponentScan(basePackages = "com.hannikkala.poc.delegate")
public class TestConfig {
}
