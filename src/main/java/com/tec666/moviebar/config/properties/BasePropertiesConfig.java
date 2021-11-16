package com.tec666.moviebar.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: longge93
 * @Date: 2021/6/6 19:42
 */
@Component
@ConfigurationProperties(prefix = "base")
@Data
public class BasePropertiesConfig {

    public String serverIp;

}
