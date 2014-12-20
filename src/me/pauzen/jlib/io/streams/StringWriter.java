package me.pauzen.jlib.io.streams;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class StringWriter {

    private Writer writer;

    public StringWriter(Writer writer) {
        this.writer = writer;
    }

    public void write(String string) {
        try {
            writer.write(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(List<String> strings) {
        for (String string : strings) write(string + System.lineSeparator());
    }

    public void cleanWrite(String string) {
        write(string);
        clean();
    }

    public void cleanWrite(List<String> strings) {
        write(strings);
        clean();
    }

    public void clean() {
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(BufferedWriter writer, List<String> strings) {
        new StringWriter(writer).cleanWrite(strings);
    }

    public static void write(BufferedWriter writer, String string) {
        new StringWriter(writer).cleanWrite(string);
    }
}
