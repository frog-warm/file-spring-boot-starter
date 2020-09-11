
# FTP文件操作
根据配置自动注入ftp操作类

# 配置样例
- 当只需要使用一个FTP服务时，添加以下配置，会默认注入
```
file.ftp.host=192.168.50.20
file.ftp.port=21
file.ftp.userName=test
file.ftp.password=test
#file.ftp.localPath
#file.ftp.remotePath
```
- 当需要多个FTP文件服务时，需要手动注入。xxx可以是任意值
1. 添加配置文件
```
file.ftp.xxx.host=192.168.50.20
file.ftp.xxx.port=222
file.ftp.xxx.userName=test
file.ftp.xxx.password=test
```
2. 添加Spring Bean
~~~java
    @Bean({"xxxFtpProperties"})
    @ConfigurationProperties(prefix = "file.ftp.xxx")
    public FtpProperties xxxFtpProperties() {
        return new FtpProperties();
    }

    @Bean("xxxFtpFileService")
    public FtpFileService xxxFtpFileService(@Qualifier("xxxFtpProperties") FtpProperties xxxFtpProperties) {
        return new FtpFileServiceImpl(xxxFtpProperties);
    }
~~~
# 使用样例
~~~java
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

    @Setter(onMethod_ = {@Autowired, @Qualifier("xxxFtpFileService")})
    private FtpFileService xxxFtpFileService;

    @Test
    void listNames() {
        System.out.println(Arrays.toString(ftpFileService.listNames()));
    }

    @Test
    void listNames2() {
        System.out.println(Arrays.toString(xxxFtpFileService.listNames()));
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
~~~
