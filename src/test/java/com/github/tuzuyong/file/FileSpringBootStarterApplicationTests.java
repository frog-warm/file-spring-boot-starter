package com.github.tuzuyong.file;

import com.github.tuzuyong.file.service.FtpFileService;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.UUID;

@SpringBootTest
class FileSpringBootStarterApplicationTests {
    @Setter(onMethod_ = @Autowired)
    FtpFileService ftpFileService;

    @Setter(onMethod_ = {@Autowired, @Qualifier("odsFtpFileService")})
    private FtpFileService odsFtpFileService;

    @Test
    void listNames() {
        System.out.println(Arrays.toString(ftpFileService.listNames()));
    }

    @Test
    void listNames2() {
        System.out.println(Arrays.toString(odsFtpFileService.listNames()));
    }

    @Test
    void upload() {
        ftpFileService.write(UUID.randomUUID().toString(), true);
        ftpFileService.write(UUID.randomUUID().toString(), true);
        ftpFileService.write(UUID.randomUUID().toString(), true);
        ftpFileService.write(UUID.randomUUID().toString(), true);
        ftpFileService.write(UUID.randomUUID().toString(), true);
        ftpFileService.write("测试数据", true);
        System.out.println(ftpFileService.upload("test.txt"));
        ftpFileService.logout();

    }

}
