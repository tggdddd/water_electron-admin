package com.ruoyi.business.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.mail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailProperties {
    /*登录账号*/
    String username;
    String nickname;
}
