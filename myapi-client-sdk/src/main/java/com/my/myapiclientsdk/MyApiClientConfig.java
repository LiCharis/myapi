package com.my.myapiclientsdk;

import com.my.myapiclientsdk.client.MyApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("myapi.client")
@Data
@ComponentScan
public class MyApiClientConfig {
    private String accessKey;
    private String secretKey;

    @Bean
    public MyApiClient myApiClient(){
        return new MyApiClient(accessKey,secretKey);
    }
}
