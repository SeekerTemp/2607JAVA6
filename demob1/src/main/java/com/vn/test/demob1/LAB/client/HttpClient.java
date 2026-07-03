package com.vn.test.demob1.LAB.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * LAB 5 - Bài 1: Lớp tiện ích tương tác với web server qua HttpURLConnection.
 */
public class HttpClient {

    /**
     * Mở kết nối.
     *
     * @param method web method (GET, POST, PUT, DELETE)
     * @param url    địa chỉ URL của REST API
     */
    public static HttpURLConnection openConnection(String method, String url) throws IOException {
        var connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.setRequestMethod(method);
        return connection;
    }

    /**
     * Đọc dữ liệu phản hồi từ server và đóng kết nối.
     */
    public static byte[] readData(HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() == 200) {
            var out = new ByteArrayOutputStream();
            InputStream is = connection.getInputStream();
            byte[] block = new byte[4 * 1024];
            while (true) {
                int n = is.read(block);
                if (n <= 0) {
                    break;
                }
                out.write(block, 0, n);
            }
            connection.disconnect();
            return out.toByteArray();
        }
        connection.disconnect();
        throw new IOException("No response from server!");
    }

    /**
     * Gửi dữ liệu lên server và đọc dữ liệu phản hồi từ server.
     */
    public static byte[] writeData(HttpURLConnection connection, byte[] data) throws IOException {
        connection.setDoOutput(true);
        connection.getOutputStream().write(data);
        return readData(connection);
    }
}
