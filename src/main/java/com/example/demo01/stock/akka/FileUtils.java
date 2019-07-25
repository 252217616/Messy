package com.example.demo01.stock.akka;

import java.io.*;

/**
 * 文件操作代码
 * 
 * @author cn.outofmemory
 * @date 2013-1-7
 */
public class FileUtils {
    /**
     * 将文本文件中的内容读入到buffer中
     *
     * @param buffer   buffer
     * @throws IOException 异常
     * @author cn.outofmemory
     * @date 2013-1-7
     */
    public static void readToBuffer(StringBuilder buffer, File file) throws IOException {
        InputStream is = new FileInputStream(file);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
    }
}