package me.pauzen.jlib.http.result;

import java.util.ArrayList;

public class Result {

    private ArrayList<String> file;
    private int               responseCode;

    public Result(ArrayList<String> file, int responseCode) {
        this.responseCode = responseCode;
        this.file = file;
    }

    /**
     * Gives information sent from server.
     *
     * @return Lines of String information returned from server.
     */
    public ArrayList<String> getFile() {
        return file;
    }

    @Override
    public String toString() {
        return "Result{" +
                "file=" + file +
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
}