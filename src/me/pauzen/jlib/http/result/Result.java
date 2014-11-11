package me.pauzen.jlib.http.result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Result {

    private ArrayList<String>         result;
    private int                       responseCode;
    private Map<String, List<String>> cookies;

    public Result(ArrayList<String> result, int responseCode, Map<String, List<String>> cookies) {
        this.responseCode = responseCode;
        this.result = result;
        this.cookies = cookies;
    }

    /**
     * Gives information sent from server.
     *
     * @return Lines of String information returned from server.
     */
    public ArrayList<String> getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "Result{" +
                "result=" + result +
                ", responseCode=" + responseCode +
                '}';
    }

    /**
     * Gives the HTTP response code.
     *
     * @return Http response code.
     */
    public int getResponseCode() {
        return responseCode;
    }

    public Map<String, List<String>> getCookies() {
        return cookies;
    }
}