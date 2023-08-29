package com.hyts.assemble.gencode.util;

import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Objects;


public class HttpUtils {

    /**
     * Http get 请求
     * @param url 请求 URL
     * @return 返回内容
     */
//    public static String get(String url) {
//        var request = HttpRequest.newBuilder()
//                .uri(URI.create(url))
//                .build();
//        var client = HttpClient.newHttpClient();
//        HttpResponse<String> response = null;
//        try {
//            response = client.send(
//                    request,
//                    HttpResponse.BodyHandlers.ofString(Charset.defaultCharset()));
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        if (Objects.nonNull(response)
//                && response.statusCode() == HttpStatus.OK.value()) {
//            return response.body();
//        }
//        return "";
//    }

}
