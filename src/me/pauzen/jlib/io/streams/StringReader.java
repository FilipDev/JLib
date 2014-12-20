package me.pauzen.jlib.io.streams;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StringReader {

    private final List<String> read = new ArrayList<>();

    public StringReader(InputStream is) {
        readAll(new BufferedReader(new InputStreamReader(is)));
    }

    public StringReader(BufferedReader bufferedReader) {
        readAll(bufferedReader);
    }

    public StringReader(InputStreamReader inputStreamReader) {
        readAll(new BufferedReader(inputStreamReader));
    }

    private void readAll(BufferedReader bufferedReader) {
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) read.add(line + System.lineSeparator());

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static StringReader read(BufferedReader bufferedReader) {
        return new StringReader(bufferedReader);
    }

    public static StringReader read(InputStream is) {
        return new StringReader(is);
    }

    public static StringReader read(InputStreamReader inputStreamReader) {
        return new StringReader(inputStreamReader);
    }

    public String getString(int index) {
        return this.read.get(index);
    }

    public List<String> getAll() {
        return this.read;
    }

    public void write(BufferedWriter bufferedWriter) {
        for (String string : read)
            try {
                bufferedWriter.write(string);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
