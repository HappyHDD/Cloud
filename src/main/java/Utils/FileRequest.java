package Utils;

import java.io.Serializable;

public class FileRequest implements Serializable {
    String name;
    public FileRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
