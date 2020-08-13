package com.github.tuzuyong.file.service.impl;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

public interface FtpExecute<T> {

    /**
     * 在ftp客户端中执行
     *
     * @param client ftp connection
     * @return a result object or {@code null} if none
     * @throws IOException e
     */
    T doInFtp(FTPClient client) throws IOException;
}