package me.pauzen.jlib.http.request;

import me.pauzen.jlib.http.headers.Header;
import me.pauzen.jlib.http.headers.UserAgent;
import me.pauzen.jlib.http.result.Result;

import java.io.IOException;

public abstract class HttpRequest {

    private UserAgent userAgent;

    public UserAgent getUserAgent() {
        return this.userAgent;
    }

    public HttpRequest setUserAgent(String userAgent) {
        this.userAgent = new UserAgent(userAgent);
        return this;
    }

    public HttpRequest setUserAgent(UserAgent userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public abstract HttpRequest send();

    public abstract HttpRequest applyHeader(Header header);

    public abstract Result getResult() throws IOException;


}
