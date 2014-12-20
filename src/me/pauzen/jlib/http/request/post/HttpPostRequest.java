package me.pauzen.jlib.http.request.post;

import me.pauzen.jlib.http.headers.Header;
import me.pauzen.jlib.http.request.HttpRequest;
import me.pauzen.jlib.http.result.Result;
import me.pauzen.jlib.io.files.Files;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HttpPostRequest extends HttpRequest {

    public final static class RequestHeader {

        private RequestHeader() {
        }

        public static final Header URL_ENCODED = new Header("Content-Type", "application/x-www-form-urlencoded");
        public static final Header MULTIPART = new Header("Content-Type", "multipart/form-data");
    }

    private HttpHeader header = new HttpHeader();
    private HttpBody   body   = new HttpBody();
    private URL        url    = null;
    private HttpURLConnection connection;
    private boolean sent = false;
    private int responseCode;

    /**
     * Used when only the POST request is required.
     *
     * @param url The URL of the server.
     * @throws IOException
     */
    public HttpPostRequest(String url) throws IOException {
        this.url = new URL(url);
    }

    {
        header(RequestHeader.URL_ENCODED);
    }

    /**
     * Used when the connection is already made.
     *
     * @param connection Already connected URLConnection.
     */
    public HttpPostRequest(HttpURLConnection connection) {
        this.connection = connection;
    }

    /**
     * Adds information to the header of the request.
     *
     * @param headerAndValue A String with the key and value separated by ": ".
     * @return Request for further action.
     */
    public HttpPostRequest header(String headerAndValue) {
        getHeader().addContent(headerAndValue);
        return this;
    }

    /**
     * Adds information to the body of the request.
     *
     * @param keyAndValue A String with the key and value separated by ": ".
     * @return Request for further action.
     */
    public HttpPostRequest field(String keyAndValue) {
        getBody().addContent(keyAndValue);
        return this;
    }

    /**
     * Adds information to the header of the request.
     *
     * @param headerEntry A map entry that is used to get the key and value, which will be put into the header.
     * @return Request for further action.
     */
    public HttpPostRequest header(Map.Entry<String, String> headerEntry) {
        getHeader().addContent(headerEntry);
        return this;
    }

    /**
     * Adds information to the body of the request.
     *
     * @param bodyEntry A map entry that is used to get the key and value, which will be put into the body.
     * @return Request for further action.
     */
    public HttpPostRequest field(Map.Entry<String, String> bodyEntry) {
        getBody().addContent(bodyEntry);
        return this;
    }

    /**
     * Adds information to the header of the request.
     *
     * @param header The header key put in the header map.
     * @param value  The value given to the header.
     * @return Request for further action.
     */
    public HttpPostRequest header(String header, String value) {
        getHeader().addContent(header, value);
        return this;
    }

    /**
     * Adds information to the body of the request.
     *
     * @param key   The key used in the body.
     * @param value The value given to the key.
     * @return Request for further action.
     */
    public HttpPostRequest field(String key, Object value) {
        getBody().addContent(key, value);
        return this;
    }

    public HttpPart getBody() {
        return body;
    }

    public HttpPart getHeader() {
        return header;
    }

    /**
     * Opens the connection to the server, then sets the header. Builds the body after.
     *
     * @return Request for further action.
     */
    @Override
    public HttpPostRequest form() {
        try {
            if (this.connection == null)
                this.connection = (HttpURLConnection) url.openConnection();

            for (Map.Entry<String, String> headerEntry : header.getContents().entrySet())
                connection.setRequestProperty(headerEntry.getKey(), headerEntry.getValue());

            buildBody();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Builds the body of the request.
     *
     * @return Request for further action.
     */
    public HttpPostRequest buildBody() {
        body.build();
        return this;
    }

    /**
     * Builds request if not already done so, then sends the POST request.
     *
     * @return Returns the object, to retrieve the result.
     * @throws IOException
     */
    private HttpPostRequest sendRequest() throws IOException {
        if (!body.isBuilt()) form();

        this.connection.setRequestMethod("POST");
        this.connection.setDoOutput(true);

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

        outputStream.writeBytes(body.getValue());
        outputStream.flush();
        outputStream.close();

        this.responseCode = this.connection.getResponseCode();
        this.sent = true;

        return this;
    }

    /**
     * Closes the connection preventing any further sending. Use this when you are finished with this object.
     *
     * @return The HttpPostRequest object for reading information.
     * @throws IOException
     */
    @Override
    public HttpPostRequest close() throws IOException {
        connection.disconnect();

        return this;
    }

    @Override
    public HttpPostRequest send() {
        try {
            return sendRequest();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public HttpPostRequest header(Header header) {
        getHeader().addContent(header.getHeader());
        return this;
    }

    /**
     * Sends the request if not already done so. Returns a new Result object combining the response code and reading the information returned from the server.
     *
     * @return Returns the information sent back by the server, including the response code.
     */
    @Override
    public Result getResult() {
        try {
            if (!this.sent)
                sendRequest();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            List<String> result = Files.readBuffer(reader);

            return new Result(result, responseCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected HttpURLConnection getConnection() {
        return connection;
    }
}