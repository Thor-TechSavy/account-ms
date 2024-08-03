package com.quicktransfer.account.config;

import feign.Response;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import org.apache.coyote.BadRequestException;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients({"com.quicktransfer.account.client"})
public class FeignConfig {


//    @Bean
//    public Encoder multipartFormEncoder() {
//        return new SpringFormEncoder(new SpringEncoder(() -> new HttpMessageConverters(new RestTemplate().getMessageConverters())));
//    }
//
//    @Bean
//    public FeignErrorDecoder customErrorDecoder() {
//        return new FeignErrorDecoder();
//    }
}
