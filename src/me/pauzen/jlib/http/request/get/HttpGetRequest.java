package me.pauzen.jlib.http.request.get;

import me.pauzen.jlib.files.Files;
import me.pauzen.jlib.http.headers.Header;
import me.pauzen.jlib.http.request.HttpRequest;
import me.pauzen.jlib.http.result.Result;
import me.pauzen.jlib.misc.Entry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HttpGetRequest extends HttpRequest {

    HttpURLConnection httpURLConnection;
    private StringBuilder url;

    public HttpGetRequest(String url) throws MalformedURLException {
        this.url = new StringBuilder(url);
        this.url.append("?");
    }

    public HttpGetRequest sendRequest() throws IOException {
        this.httpURLConnection = (HttpURLConnection) new URL(url.toString()).openConnection();
        this.httpURLConnection.connect();
        return this;
    }

    public HttpGetRequest addPart(String key, String value) {
        this.url.append("&");
        this.url.append(key);
        this.url.append("=");
        this.url.append(value);
        return this;
    }

    public void closeConnection() {
        this.httpURLConnection.disconnect();
    }

    @Override
    public HttpGetRequest send() {
        try {
            return sendRequest();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public HttpGetRequest applyHeader(Header header) {
        Entry entry = header.getHeader();
        this.httpURLConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
        return this;
    }

    @Override
    public Result getResult() throws IOException {
        ArrayList<String> result = Files.readBuffer(new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream())));
        return new Result(result, this.httpURLConnection.getResponseCode(), httpURLConnection.getHeaderFields());
    }
}
