package com.example.ws_cert.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ejbca")
public class EjbcaProperties {
}
