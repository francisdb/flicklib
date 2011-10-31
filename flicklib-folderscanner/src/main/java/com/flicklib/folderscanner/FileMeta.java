package com.flicklib.folderscanner;


public class FileMeta {
    private final String name;

    private final MovieFileType type;

    private final long size;

    
    
    public FileMeta(String name, MovieFileType type, long size) {
        super();
        this.name = name;
        this.type = type;
        this.size = size;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type
     */
    public MovieFileType getType() {
        return type;
    }

    /**
     * @return the size
     */
    public long getSize() {
        return size;
    }

    
    
}
