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

    /**
     * 创建文件夹
     *
     * @param pathname 文件夹路径
     * @return 执行结果
     */
    default boolean makeDir(String pathname) {
        return execute(client -> client.makeDirectory(pathname));
    }

    /**
     * 删除文件夹
     *
     * @param pathname 文件夹路径
     * @return 执行结果
     */
    default boolean removeDir(String pathname) {
        return execute(client -> client.removeDirectory(pathname));
    }

    /**
     * 下载远程文件
     *
     * @param remoteFile 远程文件
     * @return 返回流
     */
    default InputStream retrieveFile(String remoteFile) {
        return execute(client -> client.retrieveFileStream(remoteFile));
    }

    /**
     * 下载远程文件
     *
     * @param remoteFile 远程文件
     * @param out        下载流
     * @return 命令执行结果
     */
    default boolean retrieveFile(String remoteFile, OutputStream out) {
        return execute(client -> client.retrieveFile(remoteFile, out));
    }

    /**
     * 下载远程文件
     *
     * @param remoteFile 远程文件
     * @param file       下载写入文件
     * @return 命令执行结果
     */
    default boolean retrieveFile(String remoteFile, File file) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            return this.retrieveFile(remoteFile, out);
        }
    }

    /**
     * 下载远程文件
     *
     * @param remoteFile 远程文件
     * @param localFile  下载写入文件
     * @return 命令执行结果
     */
    default boolean retrieveFile(String remoteFile, String localFile) throws IOException {
        try (FileOutputStream out = new FileOutputStream(localFile)) {
            return this.retrieveFile(remoteFile, out);
        }
    }

    /**
     * 执行ftp文件命令
     *
     * @param action 执行器
     * @param <R>    执行返回类型
     * @return 执行结果
     */
    <R> R execute(FtpExecute<R> action);

    /**
     * 设置字符集
     *
     * @param charset 字符集
     * @return this
     */
    FtpFileService setCharset(Charset charset);


    /**
     * 退出登录
     */
    void logout();

    /**
     * 获取临时文件大小
     *
     * @return 未创建返回 0
     */
    long getTempFileSize();

    /**
     * 关闭清理
     */
    default void close() {
        logout();
    }
}
