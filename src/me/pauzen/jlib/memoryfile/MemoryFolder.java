package me.pauzen.jlib.memoryfile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryFolder {

    private String path;
    private Map<String, MemoryFile> files = new HashMap<>();

    public MemoryFolder(String path) {
        this.path = path;
    }

    public void addFile(MemoryFile file) {
        this.files.put(file.getFile().getName(), file);
    }

    public void removeFile(MemoryFile file) {
        this.files.remove(file.getFile().getName());
    }

    public String getPath() {
        return this.path;
    }

    public Map<String, MemoryFile> getFiles() {
        return this.files;
    }

    public Collection<MemoryFile> getFileList() {
        return files.values();
    }

    @Override
    public String toString() {
        return this.files.toString();
    }
}

