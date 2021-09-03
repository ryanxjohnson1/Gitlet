package gitlet;

import java.io.File;
import java.io.Serializable;

/**
 * Blob class.
 *
 * @author Ryan Johnson
 */
public class Blob implements Serializable {
    /**
     * Blob constructor NAME.
     * Initializes all of the instance variables for the blob class.
     * A Blob consists of the contents and name of a file.
     */
    public Blob(String name) {
        _name = name;
        File userFile = new File(_name);
        _contents = Utils.readContentsAsString(userFile);
        _sha1 = gitlet.Utils.sha1(Utils.serialize(_contents));
        File file = new File(".gitlet/blobs/" + _sha1);
        try {
            file.createNewFile();
        } catch (java.io.IOException x) {
            assert true;
        }
        Utils.writeObject(file, this);
    }

    /**
     * Gets sha1, Return String.
     */
    public String getSha1() {
        return _sha1;
    }

    /**
     * Gets contents, Return String.
     */
    public String getContents() {
        return _contents;
    }

    /**
     * Gets _contents, Return String.
     */
    public String toString() {
        return _contents;
    }

    /**
     * Gets _name.
     */
    private String _name;
    /**
     * Gets _sha1.
     */
    private String _sha1;
    /**
     * Gets _contents.
     */
    private String _contents;
}
