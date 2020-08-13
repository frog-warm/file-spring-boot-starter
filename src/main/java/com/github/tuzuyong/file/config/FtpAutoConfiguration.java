package com.github.tuzuyong.file.config;

import com.github.tuzuyong.file.service.FtpFileService;
import com.github.tuzuyong.file.service.impl.FtpFileServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * ftp自动注入器
 *
 * @author tuzy
 */
@Configuration
@ConditionalOnProperty({"file.ftp.host"})
public class FtpAutoConfiguration {

    @Bean({"ftpProperties"})
    @ConfigurationProperties(prefix = "file.ftp")
    @Primary
    public FtpProperties ftpProperties() {
        return new FtpProperties();
    }

    @Bean
    @Primary
    public FtpFileService ftpFileService(FtpProperties ftpProperties) {
        return new FtpFileServiceImpl(ftpProperties);
    }
}
