package com.example.ws_cert.configuration;

import com.example.ws_cert.utils.EjbTLSConnectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;

@Configuration
@RequiredArgsConstructor
public class HttpConfig {
    private final EjbTLSConnectionUtils ejbTLSConnectionUtils;

    @Bean
    public SSLContext sslContext() throws Exception {
        return ejbTLSConnectionUtils.createSSLContext();
    }
}
