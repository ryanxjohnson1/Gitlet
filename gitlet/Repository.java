package gitlet;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.Serializable;

/**
 * Repo class.
 *
 * @author Ryan Johnson
 */
public class Repository implements Serializable {
    /**
     * Repo constructor. Constructs all of the instance variables for the repository.
     * Initializes the staging area, branches, and list of commits.
     */
    public Repository() {
        File dir = new File(".gitlet/");
        dir.mkdir();
        File dirCommit = new File(".gitlet/commits/");
        dirCommit.mkdir();
        File dirBlob = new File(".gitlet/blobs/");
        dirBlob.mkdir();
        _branches = new HashMap<String, String>();
        _staging = new HashMap<String, String>();
        _remove = new ArrayList<String>();
        _allCommits = new ArrayList<String>();
        Commit first = new Commit(this, "", "initial commit", null);
        _allCommits.add(first.getSha1());
        _branches.put("master", first.getSha1());
        _currentBranch = "master";
        _headCommit = first.getSha1();
        File file = new File(".gitlet/" + "repo");
        try {
            file.createNewFile();
        } catch (java.io.IOException x) {
            assert true;
        }
        Utils.writeObject(file, this);
    }

    /**
     * Gets head commit, Return String.
     */
    public String getHeadCommit() {
        return _headCommit;
    }

    /**
     * New Commit SHA1, ELEM.
     */
    public void updateNewCommit(String sha1, Commit elem) {
        _allCommits.add(sha1);
        _branches.replace(_currentBranch, sha1);
        _headCommit = sha1;
    }

    /**
     * Add to staging NAME, BLOB.
     */
    public void stagingAdd(String name, String blob) {
        _staging.put(name, blob);
    }

    /**
     * Add to staging, Return ArrayList<String>.
     */
    public ArrayList<String> getAllCommits() {
        return _allCommits;
    }

    /**
     * Get staging, Return HashMap<String, String>.
     */
    public HashMap<String, String> getStaging() {
        return _staging;
    }

    /**
     * Get remove, Return ArrayList<String>.
     */
    public ArrayList<String> getRemove() {
        return _remove;
    }

    /**
     * Get branches, Return HashMap<String, String>.
     */
    public HashMap<String, String> getBranches() {
        return _branches;
    }

    /**
     * Get current branches, Return String.
     */
    public String getCurrentBranch() {
        return _currentBranch;
    }

    /**
     * Get move branches ID.
     */
    public void moveCurrentBranchHead(String id) {
        _branches.replace(_currentBranch, id);
        _headCommit = id;
    }

    /**
     * Get set branch, NEWBRANCH.
     */
    public void setCurrentBranch(String newBranch) {
        _currentBranch = newBranch;
        _headCommit = _branches.get(newBranch);
    }

    /**
     * Get set head commit, N.
     */
    public void setHeadCommit(String n) {
        _headCommit = n;
    }

    /**
     * List of all commits.
     */
    private ArrayList<String> _allCommits;
    /**
     * Hashmap _branches.
     */
    private HashMap<String, String> _branches;
    /**
     * Hashmap _staging.
     */
    private HashMap<String, String> _staging;
    /**
     * List _remove.
     */
    private ArrayList<String> _remove;
    /**
     * String _headCommit.
     */
    private String _headCommit;
    /**
     * String _currentBranch.
     */
    private String _currentBranch;
}
