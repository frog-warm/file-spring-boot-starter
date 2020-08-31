package com.github.tuzuyong.file.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * ftp配置类
 *
 * @author tuzy
 */
@Data
public class FtpProperties {

    /**
     * 主机地址
     */
    private String host;
    /**
     * 端口
     */
    private int port;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 本地默认文件夹
     */
    private String localPath;
    /**
     * ftp默认文件夹
     */
    private String remotePath;

    public String toString() {
        return String.format("host: %s, port: %s, userName: %s, localPath: %s, remotePath: %s", host, port, userName, localPath, remotePath);
    }
}
