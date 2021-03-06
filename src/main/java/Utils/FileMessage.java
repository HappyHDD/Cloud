package Utils;

import java.io.Serializable;

public class FileMessage implements Serializable {
    public String filename;
    public int partNumber;
    public int partsCount;
    public byte[] data;

    public FileMessage(String filename, int partNumber, int partsCount, byte[] data) {
        this.filename = filename;
        this.partNumber = partNumber;
        this.partsCount = partsCount;
        this.data = data;
    }
}
