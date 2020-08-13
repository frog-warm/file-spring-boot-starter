package com.github.tuzuyong.file.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * 请输入类注释 TODO
 *
 * @author tuzy
 */
@Data
public class FtpProperties {

    private String host;
    private int port;
    private String userName;
    private String password;
    private String localPath;
    private String remotePath;

    public String toString() {
        return String.format("host: %s, port: %s, userName: %s, localPath: %s, remotePath: %s", host, port, userName, localPath, remotePath);
    }
}
