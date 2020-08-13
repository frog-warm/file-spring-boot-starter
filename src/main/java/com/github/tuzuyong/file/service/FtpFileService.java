package com.github.tuzuyong.file.service;

import com.github.tuzuyong.file.service.impl.FtpExecute;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.nio.charset.Charset;

/**
 * FTP文件操作
 *
 * @author tuzy
 */
public interface FtpFileService extends Closeable {

    /**
     * 向当前文件对象写入数据
     *
     * @param line 数据
     */
    default void write(String line) {
        write(line, true);
    }

    /**
     * 向当前文件对象写入数据
     *
     * @param line      数据
     * @param lineBreak 末尾是否添加换行
     */
    void write(String line, boolean lineBreak);

    /**
     * 上传缓存对象中文件
     *
     * @return 是否成功
     */
    boolean upload(String remoteFileName);


    /**
     * 上传文件
     *
     * @param localFileName  本地文件
     * @param remoteFileName 远程文件名
     * @return 是否成功
     */
    default boolean upload(String localFileName, String remoteFileName) throws FileNotFoundException {
        return upload(new FileInputStream(localFileName), remoteFileName);
    }


    /**
     * 上传文件
     *
     * @param localFile      本地文件
     * @param remoteFileName 远程文件名
     * @return 是否成功
     */
    default boolean upload(InputStream localFile, String remoteFileName) {
        return execute(client -> client.storeFile(remoteFileName, localFile));
    }

    /**
     * 重命名远程文件
     *
     * @param srcName    原文件名
     * @param targetName 目标文件名
     * @return 是否成功
     */
    default boolean rename(String srcName, String targetName) {
        return execute(client -> client.rename(srcName, targetName));
    }

    /**
     * 改变工作目录
     *
     * @param remotePath 远程目录
     * @return 是否成功
     */
    default boolean changeDir(String remotePath) {
        return execute(client -> client.changeWorkingDirectory(remotePath));
    }

    /**
     * 列出文件名
     *
     * @return 文件名列表
     */
    default String[] listNames() {
        return execute(FTPClient::listNames);
    }

    /**
     * 列出文件名
     *
     * @return 文件名列表
     */
    default boolean deleteFile(String remoteFile) {
        return execute(client -> client.deleteFile(remoteFile));
    }


    <R> R execute(FtpExecute<R> action);

    FtpFileService setCharset(Charset charset);

    void logout();

    long getTempFileSize();

    default void close() {
        logout();
    }
}
