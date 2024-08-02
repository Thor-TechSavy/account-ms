package com.quicktransfer.account.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients({"com.quicktransfer.account.client"})
public class FeignConfig {


//    @Bean
//    public Encoder multipartFormEncoder() {
//        return new SpringFormEncoder(new SpringEncoder(() -> new HttpMessageConverters(new RestTemplate().getMessageConverters())));
//    }

//    @Bean
//    public FeignErrorDecoder customErrorDecoder() {
//        return new FeignErrorDecoder();
//    }
}
