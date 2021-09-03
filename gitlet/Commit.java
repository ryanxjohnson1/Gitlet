package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;

/**
 * Commit class.
 *
 * @author Ryan Johnson
 */
public class Commit implements Serializable {
    /**
     * Commit class, takes REPO, PARENT, MESSAGE, PARENT2.
     * A comit consists of multiple blobs, the commits parent, a user inputed message and the universal repo.
     */
    public Commit(Repository repo, String parent,
                  String message, String parent2) {
        _repo = repo;
        _parent = parent;
        _parent2 = parent2;
        _message = message;
        _sha1 = gitlet.Utils.sha1(Utils.serialize(this));
        _mulBranch = new ArrayList<String>();
        setData();
        File file = new File(".gitlet/commits/" + _sha1);
        try {
            file.createNewFile();
        } catch (java.io.IOException x) {
            assert true;
        }
        Utils.writeObject(file, this);
    }

    /**
     * Setdata.
     * Data contains the blob data in a particular commit.
     * Ancestry is the chain of parents for a commit.
     */
    public void setData() {
        if (_parent.equals("")) {
            _data = new HashMap<String, String>();
            _ancestry = new HashMap<String, String>();
        } else {
            File getFile = new File(".gitlet/commits/" + _parent);
            Commit p = Utils.readObject(getFile, Commit.class);
            _data = new HashMap<String, String>(p.getData());
            _ancestry = new HashMap<String, String>(p.getAncestry());
            if (p.getMulBranch().size() >= 2) {
                splitMethod(p.getMulBranch());
            }
            if (_parent2 != null) {
                File getFile2 = new File(".gitlet/commits/" + _parent2);
                Commit p2 = Utils.readObject(getFile2, Commit.class);
                for (String x : p2.getData().keySet()) {
                    if (!_data.containsKey(x)) {
                        _data.put(x, p2.getData().get(x));
                    }
                }
                if (p2.getMulBranch().size() >= 2) {
                    splitMethod2(p2.getMulBranch());
                }
            }
        }
        for (String key : _data.keySet()) {
            if (_repo.getStaging().get(key) != null
                    && _data.get(key).equals(_repo.getStaging().get(key))) {
                _repo.getStaging().remove(key);
            }
        }
        for (String key : _repo.getStaging().keySet()) {
            if (_data.get(key) != null) {
                _data.remove(key);
            }
        }
        for (String key : _repo.getStaging().keySet()) {
            _data.put(key, _repo.getStaging().get(key));
        }
        for (String elem : _repo.getRemove()) {
            _data.remove(elem);
        }
        _repo.getStaging().clear();
        _repo.getRemove().clear();
    }

    /**
     * SplitMethod PARENTLIST. Adds or replaces an already existing parent in the ancestry chain.
     */
    public void splitMethod(ArrayList<String> parentList) {
        for (String elem : parentList) {
            if (!elem.equals(_repo.getCurrentBranch())) {
                if (!_ancestry.containsKey(elem)) {
                    _ancestry.put(elem, _parent);
                } else {
                    _ancestry.replace(elem, _parent);
                }

            }
        }
    }

    /**
     * SplitMethod2 PARENTLIST. List of previous parents.
     */
    public void splitMethod2(ArrayList<String> parentList) {
        for (String elem : parentList) {
            if (!elem.equals(_repo.getCurrentBranch())) {
                if (!_ancestry.containsKey(elem)) {
                    _ancestry.put(elem, _parent2);
                } else {
                    _ancestry.replace(elem, _parent2);
                }

            }
        }
    }

    /**
     * Gets the sha1, Return String.
     */
    public String getSha1() {
        return _sha1;
    }

    /**
     * Gets the data, Return HashMap<String, String>.
     */
    public HashMap<String, String> getData() {
        return _data;
    }

    /**
     * Gets the timestamp, Return String.
     */
    public String getTimeStamp() {
        return _timestamp;
    }

    /**
     * Gets the message, Return String.
     */
    public String getMessage() {
        return _message;
    }

    /**
     * Gets the parent, Return String.
     */
    public String getParent() {
        return _parent;
    }

    /**
     * Gets the ancestry, Return HashMap<String, String>.
     */
    public HashMap<String, String> getAncestry() {
        return _ancestry;
    }

    /**
     * Adds NAME to branch.
     */
    public void setMulBranch(String name) {
        _mulBranch.add(name);
    }

    /**
     * Gets mulbranch, Return ArrayList<String>.
     */
    public ArrayList<String> getMulBranch() {
        return _mulBranch;
    }

    /**
     * Gets ancestry.
     */
    private HashMap<String, String> _ancestry;
    /**
     * Gets data.
     */
    private HashMap<String, String> _data;
    /**
     * Gets _parent1.
     */
    private String _parent;
    /**
     * Gets _parent2.
     */
    private String _parent2;
    /**
     * Gets _message.
     */
    private String _message;
    /**
     * Gets _timestamp.
     */
    private String _timestamp = "Thu Nov 9 20:00:05 2017 -0800";
    /**
     * Gets _sha1.
     */
    private String _sha1;
    /**
     * Gets _repo.
     */
    private Repository _repo;
    /**
     * Gets _mulBranch.
     */
    private ArrayList<String> _mulBranch;
}
