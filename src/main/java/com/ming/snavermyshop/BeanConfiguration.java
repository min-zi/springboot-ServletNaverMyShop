package com.ming.snavermyshop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ProductRepository productRepository() {
        String dbUrl = "jdbc:h2:mem:snavermyshopdb";
        String dbId = "sa";
        String dbPassword = "";

        return new ProductRepository(dbUrl, dbId, dbPassword);
    }
}