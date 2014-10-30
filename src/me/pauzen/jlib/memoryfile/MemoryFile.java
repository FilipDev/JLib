package me.pauzen.jlib.memoryfile;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;

public class MemoryFile {

    private final String     path;
    private       String     name;
    private       File       file;
    private       ByteBuffer value;

    public MemoryFile(String path, File file) {
        this.path = path;
        this.file = file;

        try {
            this.name = file.getName();
            this.value = ByteBuffer.wrap(Files.readAllBytes(file.toPath()));

            MemoryFileManager.registerMemoryFile(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MemoryFile(String path, String name, byte[] value) {
        this.path = path;
        this.name = name;

        this.value = ByteBuffer.wrap(value);
    }

    public MemoryFile(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public void rename(String name) {
        MemoryFolder folder = MemoryFileManager.getMemoryFolder(getPath());
        folder.getFiles().remove(this.getName());
        folder.getFiles().put(name, this);
        this.name = name;
    }

    public ByteBuffer getValue() {
        return this.value;
    }

    public void setValue(byte[] value) {
        this.value = ByteBuffer.wrap(value);
    }

    public byte[] toByteArray() {
        return this.value.array();
    }

    public String getPath() {
        return this.path;
    }

    public String getName() {
        return this.name;
    }

    public File getFile() {
        return this.file;
    }

    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(value.array())));
    }

    public BufferedWriter getWriter() {
        return new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream(value.array().length)));
    }
}
