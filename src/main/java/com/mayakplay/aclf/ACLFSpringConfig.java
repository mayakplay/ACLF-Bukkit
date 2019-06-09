package com.mayakplay.aclf;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class ACLFSpringConfig {

    @Bean
    public Gson gson() {
        return new Gson();
    }

}
