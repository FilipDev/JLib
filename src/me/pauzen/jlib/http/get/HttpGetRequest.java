package me.pauzen.jlib.http.get;

import me.pauzen.jlib.files.Files;
import me.pauzen.jlib.http.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpGetRequest {

    private StringBuilder url;
    private Result result;

    public HttpGetRequest(String url) throws MalformedURLException {
        this.url = new StringBuilder(url);
        this.url.append("?");
    }

    public HttpGetRequest addPart(String key, String value) {
        this.url.append("&");
        this.url.append(key);
        this.url.append("=");
        this.url.append(value);
        return this;
    }

    public HttpGetRequest send() {
        try {
            URL url1 = new URL(url.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
            httpURLConnection.connect();
            this.result = new Result(Files.readBuffer(new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))), httpURLConnection.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Result getResult() {
        return this.result;
    }

}
