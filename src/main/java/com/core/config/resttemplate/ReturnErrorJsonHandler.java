package com.core.config.resttemplate;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 自定义Response错误处理器
 * @Author: QiuQiang
 * @Date: 2020-11-11
 */
public class ReturnErrorJsonHandler extends DefaultResponseErrorHandler {

    // 调用父类 hasError 方法
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return super.hasError(response);
    }

    // 重写该方法自定义返回数据格式
    private String getErrorMessage(@Nullable byte[] responseBody, @Nullable Charset charset) {
        if (ObjectUtils.isEmpty(responseBody)) {
            return "[no body]";
        } else {
            charset = charset == null ? StandardCharsets.UTF_8 : charset;
            int maxChars = 200;
            if (responseBody.length < maxChars * 2) {
                // 数据格式转换
                String message = new String(responseBody, charset);
                // 将 Unicode 格式字符转换为中文
                return UnicodeToString(message);
            } else {
                try {
                    Reader reader = new InputStreamReader(new ByteArrayInputStream(responseBody), charset);
                    CharBuffer buffer = CharBuffer.allocate(maxChars);
                    reader.read(buffer);
                    reader.close();
                    buffer.flip();
                    return buffer.toString() + "... (" + responseBody.length + " bytes)";
                } catch (IOException var9) {
                    throw new IllegalStateException(var9);
                }
            }
        }
    }

    public String UnicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    // 重写handleError, 调用 getErrorMessage 方法
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = HttpStatus.resolve(response.getRawStatusCode());
        if (statusCode == null) {
            byte[] body = this.getResponseBody(response);
            String message = this.getErrorMessage(body, this.getCharset(response));
            throw new UnknownHttpStatusCodeException(message, response.getRawStatusCode(), response.getStatusText(), response.getHeaders(), body, this.getCharset(response));
        } else {
            this.handleError(response, statusCode);
        }
    }

    // 重写handleError, 调用 getErrorMessage 方法
    protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
        String statusText = response.getStatusText();
        HttpHeaders headers = response.getHeaders();
        byte[] body = this.getResponseBody(response);
        Charset charset = this.getCharset(response);
        String message = this.getErrorMessage(body, charset);
        switch (statusCode.series()) {
            case CLIENT_ERROR:
                throw HttpClientErrorException.create(message, statusCode, statusText, headers, body, charset);
            case SERVER_ERROR:
                throw HttpServerErrorException.create(message, statusCode, statusText, headers, body, charset);
            default:
                throw new UnknownHttpStatusCodeException(message, statusCode.value(), statusText, headers, body, charset);
        }
    }

}