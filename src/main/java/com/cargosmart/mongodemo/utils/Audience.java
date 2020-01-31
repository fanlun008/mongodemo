package com.cargosmart.mongodemo.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author licankun
 */
@Component
@ConfigurationProperties(prefix = "audience")
@Data
public class Audience {
    private String clientId;
    private String base64Secret;
    private String issuer;
    private int expiresSecond;
}
