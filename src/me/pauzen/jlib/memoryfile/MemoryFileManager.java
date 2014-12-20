package me.pauzen.jlib.memoryfile;

import me.pauzen.jlib.memoryfile.exceptions.MemoryFileNotFoundException;
import me.pauzen.jlib.memoryfile.exceptions.MemoryFolderNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryFileManager {

    public static final String SEPARATOR = "/";

    private static Map<String, MemoryFolder> memoryObjectMap = new HashMap<>();

    public static Collection<MemoryFolder> getFolders() {
        return memoryObjectMap.values();
    }

    public static MemoryFile getMemoryFile(String path, String name) throws MemoryFolderNotFoundException, MemoryFileNotFoundException {
        MemoryFolder memoryFolder = memoryObjectMap.get(path);
        if (memoryFolder == null) throw new MemoryFolderNotFoundException(String.format("Folder %s not found.", path));
        MemoryFile memoryFile = memoryFolder.getFiles().get(name);
        if (memoryFile == null) throw new MemoryFileNotFoundException(String.format("File %s not found", name));
        return memoryFile;
    }

    public static MemoryFolder getMemoryFolder(String path) throws MemoryFolderNotFoundException {
        MemoryFolder memoryFolder = memoryObjectMap.get(path);
        if (memoryFolder == null) throw new MemoryFolderNotFoundException(String.format("Folder %s not found.", path));
        return memoryFolder;
    }

    public static void registerMemoryFile(MemoryFile memoryFile) {
        String lastFolder = memoryFile.getPath().substring(0, memoryFile.getPath().lastIndexOf(MemoryFileManager.SEPARATOR) + 1);
        StringBuilder buildingPath = new StringBuilder();

        for (String name : memoryFile.getPath().split(SEPARATOR)) {
            buildingPath.append(name);
            buildingPath.append(SEPARATOR);
            MemoryFolder currMemoryFolder = memoryObjectMap.get(buildingPath.toString());
            if (buildingPath.toString().equals(lastFolder))
                if ((currMemoryFolder) != null) currMemoryFolder.addFile(memoryFile);
                else {
                    currMemoryFolder = new MemoryFolder(buildingPath.toString());
                    memoryObjectMap.put(buildingPath.toString(), currMemoryFolder);
                    currMemoryFolder.addFile(memoryFile);
                }
        }
    }
}
