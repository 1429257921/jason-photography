package com.jason.common.core.util;

import cn.hutool.core.io.FileTypeUtil;
import com.jason.common.core.exception.BizException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 自定义IO工具类
 *
 * @author gzc
 * @since 2023/6/12
 **/
@SuppressWarnings("unused")
public abstract class JpIoUtil {

    /**
     * 流转换
     *
     * @param baos 字节数组输出流
     * @return 输入流
     */
    public static InputStream getInputStream(ByteArrayOutputStream baos) {
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * 流转换
     *
     * @param baos 字节数组输出流
     * @return 字节数组输入流
     */
    public static ByteArrayInputStream getByteArrayInputStream(ByteArrayOutputStream baos) {
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * 流转换
     *
     * @param bytes 字节数组
     * @return 输入流
     */
    public static ByteArrayInputStream getInputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    /**
     * 流转和
     *
     * @param inputStream 输入流
     * @return 字节数组
     */
    public static byte[] getBytes(InputStream inputStream) {
        return cloneInputStream(inputStream).toByteArray();
    }

    /**
     * 流转换
     *
     * @param bytes 字节数组
     * @return 字节数组输出流
     */
    public static ByteArrayOutputStream getOutputStream(byte[] bytes) {
        return cloneInputStream(getInputStream(bytes));
    }

    /**
     * 流转换
     *
     * @param input 输入流
     * @return 字节数组输出流
     */
    public static ByteArrayOutputStream cloneInputStream(InputStream input) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos;
        } catch (IOException e) {
            throw new BizException("流转换异常", e);
        }
    }


    /**
     * 获取文件类型
     *
     * @param outputStream 字节数组输出流
     * @return 文件类型
     */
    public static String getFileType(ByteArrayOutputStream outputStream) {
        return FileTypeUtil.getType(getInputStream(outputStream));
    }

    /**
     * 获取文件类型
     *
     * @param inputStream 输入流
     * @return 文件类型
     */
    public static String getFileType(InputStream inputStream) {
        return FileTypeUtil.getType(inputStream);
    }

    /**
     * 获取文件类型
     *
     * @param fileBytes 字节数组
     * @return 文件类型
     */
    public static String getFileType(byte[] fileBytes) {
        return getFileType(new ByteArrayInputStream(fileBytes));
    }

}
