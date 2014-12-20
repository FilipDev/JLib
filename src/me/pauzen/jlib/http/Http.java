package me.pauzen.jlib.http;

import me.pauzen.jlib.http.request.get.HttpGetRequest;
import me.pauzen.jlib.http.request.post.HttpPostRequest;

import java.io.IOException;
import java.net.MalformedURLException;

public final class Http {

    private Http() {
    }

    public static HttpPostRequest post(String url) {
        try {
            return new HttpPostRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HttpGetRequest get(String url) {
        try {
            return new HttpGetRequest(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
