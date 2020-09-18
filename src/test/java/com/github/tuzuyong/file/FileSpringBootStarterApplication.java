package com.github.tuzuyong.file;

import com.github.tuzuyong.file.config.FtpProperties;
import com.github.tuzuyong.file.service.FtpFileService;
import com.github.tuzuyong.file.service.impl.FtpFileServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@SpringBootApplication
public class FileSpringBootStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileSpringBootStarterApplication.class, args);
    }


    @Bean({"xxxFtpProperties"})
    @ConfigurationProperties(prefix = "file.ftp.xxx")
    public FtpProperties xxxFtpProperties() {
        return new FtpProperties();
    }

    @Bean("xxxFtpFileService")
    public FtpFileService xxxFtpFileService(@Qualifier("xxxFtpProperties") FtpProperties xxxFtpProperties) {
        return new FtpFileServiceImpl(xxxFtpProperties);
    }

}
