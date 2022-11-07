package com.wxy.producer;

import io.netty.handler.codec.http2.Http2Connection;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpClient {

    public void send() throws IOException {
        URL url = new URL("");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();

    }
}
