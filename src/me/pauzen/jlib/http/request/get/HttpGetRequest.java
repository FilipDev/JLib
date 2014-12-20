package me.pauzen.jlib.http.request.get;

import me.pauzen.jlib.io.files.Files;
import me.pauzen.jlib.http.headers.Header;
import me.pauzen.jlib.http.request.HttpRequest;
import me.pauzen.jlib.http.result.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpGetRequest extends HttpRequest {

    private HttpURLConnection httpURLConnection;
    private StringBuilder     url;
    private List<Header> headers = new ArrayList<>();

    public HttpGetRequest(String url) throws MalformedURLException {
        this.url = new StringBuilder(url);
        this.url.append("?");
    }

    private HttpGetRequest sendRequest() throws IOException {
        this.httpURLConnection.connect();
        return this;
    }

    /**
     * Adds a part to the URL.
     *
     * @param key The key of the part to add.
     * @param value The value of the part.
     * @return Request for further action.
     */
    public HttpGetRequest field(String key, String value) {
        this.url.append("&");
        this.url.append(key);
        this.url.append("=");
        this.url.append(value);
        return this;
    }

    /**
     * Closes the HttpURLConnection.
     *
     * @return Request for further action.
     */
    public HttpGetRequest close() {
        this.httpURLConnection.disconnect();
        return this;
    }

    /**
     * Sends a request.
     *
     * @return Request for further action.
     */
    @Override
    public HttpGetRequest send() {
        try {
            return sendRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Creates the connection, but does not send it.
     *
     * @return Request for further action.
     */
    public HttpGetRequest form() {
        try {
            this.httpURLConnection = (HttpURLConnection) new URL(url.toString()).openConnection();
            for (Header header : headers) this.httpURLConnection.setRequestProperty(header.getHeader().getKey(), header.getHeader().getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Applies a request property such as the user agent.
     *
     * @param header The header to apply.
     * @return Request for further action.
     */
    @Override
    public HttpGetRequest header(Header header) {
        this.headers.add(header);
        return this;
    }

    /**
     * Gets the Result object.
     *
     * @return The Result object.
     */
    @Override
    public Result getResult() {
        try {
            ArrayList<String> result = Files.readBuffer(new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream())));
            return new Result(result, this.httpURLConnection.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the HttpURLConnection.
     *
     * @return The HttpURLConnection object.
     */
    @Override
    protected HttpURLConnection getConnection() {
        return httpURLConnection;
    }
}
