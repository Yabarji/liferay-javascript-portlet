package com.hannikkala.poc.rest.config;

import com.hannikkala.poc.rest.filter.JwtFilter;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Tommi Hännikkälä tommi@hannikkala.com
 * Date: 25/02/16
 * Time: 12:21
 */
@EnableWebMvc
@EnableWebSecurity
@Configuration
@ComponentScan(basePackages = { "com.hannikkala.poc.rest" })
@PropertySource({"classpath:application.properties"})
public class RestConfig extends WebMvcConfigurerAdapter {

    @Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Configuration
    protected static class SecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(WebSecurity web) throws Exception {

        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.httpBasic().disable()
                    .sessionManagement().disable()
                    .authorizeRequests()
                    .anyRequest().permitAll()
                    .and()
                    .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
                    .csrf().disable();
        }
    }
}
