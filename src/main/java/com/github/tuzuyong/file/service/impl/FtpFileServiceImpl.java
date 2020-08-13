package com.github.tuzuyong.file.service.impl;

import com.github.tuzuyong.file.config.FtpProperties;
import com.github.tuzuyong.file.service.FtpFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.*;
import org.springframework.beans.factory.DisposableBean;

import javax.naming.AuthenticationException;
import java.io.*;
import java.lang.management.ThreadInfo;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

/**
 * 请输入类注释 TODO
 *
 * @author tuzy
 */
@Slf4j
public class FtpFileServiceImpl implements FtpFileService, DisposableBean {

    private final FTPClient ftpClient;
    private ThreadLocal<BufferedWriter> writer;
    private ThreadLocal<BufferedReader> reader;
    private ThreadLocal<File> tempFile;
    private Charset charset = StandardCharsets.UTF_8;
    private final FtpProperties config;

    public FtpFileServiceImpl(FtpProperties properties) {
        this.config = properties;
        this.ftpClient = new FTPClient();
    }


    @Override
    public void write(String line, boolean lineBreak) {
        if (this.writer == null) {
            this.newWriter();
        }
        try {
            this.writer.get().write(line);
            if (lineBreak) {
                this.writer.get().newLine();
            }
        } catch (IOException e) {
            log.error("写入失败", e);
        }
    }

    @Override
    public boolean upload(String remoteFileName) {
        if (this.writer == null) {
            throw new RuntimeException("无临时文件信息，请先写入文件");
        }
        try (InputStream file = new FileInputStream(tempFile.get())) {
            this.writer.get().flush();
            return this.upload(file, remoteFileName);
        } catch (IOException e) {
            log.error("上传临时文件失败。", e);
        }
        return false;
    }


    @Override
    public FtpFileService setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    @Override
    public void logout() {
        log.info("登出FTP {}", this.config);
        if (ftpClient.isConnected()) {
            try {
                ftpClient.logout();
            } catch (FTPConnectionClosedException e) {
                log.warn("logout fail . {}", e.getMessage());
            } catch (IOException e) {
                log.error("logout fail", e);
            }
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                log.error("disconnect fail", e);
            }
        }
        if (this.writer != null) {
            try {
                this.writer.get().close();
            } catch (IOException e) {
                log.error("writer close fail", e);
            }
            this.writer = null;
        }
        if (this.reader != null) {
            try {
                this.reader.get().close();
            } catch (IOException e) {
                log.error("reader close fail", e);
            }
            this.reader = null;
        }
        if (this.tempFile != null && !this.tempFile.get().delete()) {
            log.debug("删除本地临时文件失败");
        }
    }

    @Override
    public long getTempFileSize() {
        if (tempFile == null) {
            return 0;
        }
        return tempFile.get().length();
    }

    public <R> R execute(FtpExecute<R> action) {
        if (!ftpClient.isConnected()) {
            connect();
        }
        try {
            return action.doInFtp(ftpClient);
        } catch (IOException e) {
            log.error("执行失败", e);
            return null;
        }
    }

    /**
     * 连接FTP 设置编码
     */
    private void connect() {
        if (!ftpClient.isConnected()) {
            log.debug("connect [{}]", config);
            try {
                ftpClient.connect(config.getHost(), config.getPort());
                ftpClient.login(config.getUserName(), config.getPassword());
            } catch (IOException e) {
                log.error("ftp连接失败。", e);
            }
        }
        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            log.error("ftp认证失败：{} ", config);
            throw new RuntimeException("ftp认证失败！");
        } else {
            //ftp中文编码设置
            ftpClient.setControlEncoding(charset.displayName());
            FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
            conf.setServerLanguageCode("zh");
            ftpClient.configure(conf);
            try {
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            } catch (IOException ignored) {
            }
            ftpClient.enterLocalPassiveMode();
        }
        if (this.config.getRemotePath() != null) {
            this.changeDir(this.config.getRemotePath());
        }
    }


    private void newWriter() {
        try {
            tempFile = new ThreadLocal<>();
            tempFile.set(File.createTempFile("ftp", "temp"));
        } catch (IOException e) {
            throw new RuntimeException("创建本地临时文件失败！", e);
        }
        try {
            FileOutputStream tempOut = new FileOutputStream(tempFile.get(), false);
            this.writer = new ThreadLocal<>();
            this.writer.set(new BufferedWriter(new OutputStreamWriter(tempOut, this.charset)));
        } catch (IOException e) {
            throw new RuntimeException("创建本地文件失败！", e);
        }

    }

    @Override
    public void destroy() {
        this.logout();
    }
}
