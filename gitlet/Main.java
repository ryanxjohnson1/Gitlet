package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Driver class for Gitlet, the version-control system.
 *
 * @author Ryan Johnson
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND> ....
     * Creates or deserializes the repo, handles teh terminal user input and then reserializes the repo
     * and all of the additional methods.
     */
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        if (args[0].equals("init")) {
            try {
                deSerializeRepo();
            } catch (java.lang.IllegalArgumentException x) {
                assert true;
            }
            if (_initialized) {
                System.out.println("A Gitlet "
                        + "version-control system already exists "
                        + "in the current directory.");
                System.exit(0);
            }
            _repo = new Repository();
            _initialized = true;
            serializeRepo();
            System.exit(0);
        }
        try {
            deSerializeRepo();
        } catch (java.lang.IllegalArgumentException x) {
            assert true;
        }
        if (!_initialized) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        mainHelper(args);
        serializeRepo();
    }

    /**
     * Helper for main ARGS. This helper determines the terminal user input and calls the proper method.
     */
    public static void mainHelper(String[] args) {
        if (args[0].equals("add")) {
            add(args[1]);
        } else if (args[0].equals("commit")) {
            commit(args[1]);
        } else if (args[0].equals("rm")) {
            rm(args[1]);
        } else if (args[0].equals("log")) {
            log();
        } else if (args[0].equals("global-log")) {
            globalLog();
        } else if (args[0].equals("find")) {
            find(args[1]);
        } else if (args[0].equals("status")) {
            status();
        } else if (args[0].equals("checkout")) {
            mainHelper2(args);
        } else if (args[0].equals("branch")) {
            branch(args[1]);
        } else if (args[0].equals("rm-branch")) {
            if (!_repo.getBranches().containsKey(args[1])) {
                System.out.println("A branch with that name does "
                        + "not exist.");
                System.exit(0);
            } else if (_repo.getCurrentBranch().equals(args[1])) {
                System.out.println("Cannot remove the current branch.");
                System.exit(0);
            }
            _repo.getBranches().remove(args[1]);
            for (String elem : _repo.getBranches().values()) {
                File getFile = new File(".gitlet/commits/"
                        + elem);
                Commit c = Utils.readObject(getFile, Commit.class);
                c.getAncestry().remove(args[1]);
                c.getMulBranch().remove(args[1]);
                Utils.writeObject(getFile, c);
            }
        } else if (args[0].equals("reset")) {
            reset(args[1]);
        } else if (args[0].equals("merge")) {
            merge(args[1], null);
        } else if (args[0].equals("add-remote")) {
            addRemote(args[1], args[2]);
        } else if (args[0].equals("rm-remote")) {
            rmRemote(args[1]);
        } else if (args[0].equals("push")) {
            push(args[1], args[2]);
        } else if (args[0].equals("fetch")) {
            fetch(args[1], args[2]);
        } else if (args[0].equals("pull")) {
            pull(args[1], args[2]);
        } else if (args.length > 0) {
            System.out.println("No command with that name exists.");
            System.exit(0);
        }
    }

    /**
     * Helper for main ARGS. This helper was needed because the length of mainhelper1 was too long and broke style guidelines.
     */
    public static void mainHelper2(String[] args) {
        if (args.length == 3) {
            if (!args[1].equals("--")) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            checkOut1(args[2]);
        } else if (args.length == 4) {
            if (!args[2].equals("--")) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            checkOut2(args[1], args[3]);
        } else if (args.length == 2) {
            checkOut3(args[1]);
        }
    }

    /**
     * Used for debugging purposes. Not part of the working program.
     */
    public static void check() {
        System.out.println();
        System.out.println();
        System.out.println("Check");
        System.out.println("Curr Branch "
                + _repo.getCurrentBranch());
        System.out.println("Staging Size: "
                + _repo.getStaging().size());
        System.out.println("remove size "
                + _repo.getRemove().size());
        System.out.println("AllCommits "
                + _repo.getAllCommits());
        System.out.println("Head Commit "
                + _repo.getHeadCommit());
        File getFile = new File(".gitlet/commits/"
                + _repo.getHeadCommit());
        System.out.println("Data size of head commit "
                + Utils.readObject(getFile,
                Commit.class).getData().size());
        System.out.println();
        System.out.println();
    }

    /**
     * Used to serialize Repo.
     */
    public static void serializeRepo() {
        File file = new File(".gitlet/" + "repo");
        try {
            file.createNewFile();
        } catch (java.io.IOException x) {
            assert true;
        }
        Utils.writeObject(file, _repo);
        File bfile = new File(".gitlet/" + "initialized");
        try {
            bfile.createNewFile();
        } catch (java.io.IOException x) {
            assert true;
        }
        Utils.writeObject(bfile, _initialized);
    }

    /**
     * Used to deserialize Repo.
     */
    public static void deSerializeRepo() {
        File getFile = new File(".gitlet/repo");
        _repo = Utils.readObject(getFile, Repository.class);
        File getBFile = new File(".gitlet/initialized");
        _initialized = Utils.readObject(getBFile, Boolean.class);
    }

    /**
     * Add NAME to staging area.
     */
    public static void add(String name) {
        File getFile = new File(".gitlet/commits/"
                + _repo.getHeadCommit());
        Commit head = Utils.readObject(getFile, Commit.class);
        Blob elem = null;
        try {
            elem = new Blob(name);
        } catch (java.lang.IllegalArgumentException x) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        if (_repo.getRemove().contains(name)) {
            _repo.getRemove().remove(name);
        } else if (!head.getData().containsKey(name)
                || (head.getData().containsKey(name)
                && !head.getData().get(name).equals(
                elem.getSha1()))) {
            _repo.stagingAdd(name, elem.getSha1());
        }
    }

    /**
     * Used to commit MESSAGE.
     */
    public static void commit(String message) {
        if (_repo.getStaging().size() == 0
                && _repo.getRemove().size() == 0) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        } else if (message == null || message.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        Commit newCommit = new Commit(_repo, _repo.getHeadCommit(),
                message, null);
        _repo.updateNewCommit(newCommit.getSha1(), newCommit);
    }

    /**
     * Used to commit MESSAGE in merge, PARENT1, PARENT2.
     */
    public static void mergedCommit(String message, String parent1,
                                    String parent2) {
        if (_repo.getStaging().size() == 0 && _repo.getRemove().size() == 0) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        } else if (message == null || message.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        Commit newCommit = new Commit(_repo, parent1,
                message, parent2);
        _repo.updateNewCommit(newCommit.getSha1(), newCommit);
    }

    /**
     * Used to remove NAME from satging.
     */
    public static void rm(String name) {
        File getFile = new File(".gitlet/commits/"
                + _repo.getHeadCommit());
        Commit head = Utils.readObject(getFile, Commit.class);
        if (_repo.getStaging().containsKey(name)) {
            _repo.getStaging().remove(name);
        } else if (head.getData().keySet().contains(name)) {
            _repo.getRemove().add(name);
            Utils.restrictedDelete(name);
        } else {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
    }

    /**
     * Used to create new branch NAME.
     */
    public static void branch(String name) {
        if (_repo.getBranches().containsKey(name)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        _repo.getBranches().put(name, _repo.getHeadCommit());
        File getFile = new File(".gitlet/commits/"
                + _repo.getHeadCommit());
        Commit head = Utils.readObject(getFile, Commit.class);
        head.setMulBranch(name);
        head.setMulBranch(_repo.getCurrentBranch());
        Utils.writeObject(getFile, head);
    }

    /**
     * Used to create log. Prints local branch commit data.
     */
    public static void log() {
        File getFile = new File(".gitlet/commits/"
                + _repo.getHeadCommit());
        Commit temp = Utils.readObject(getFile, Commit.class);
        while (true) {
            System.out.println("===");
            System.out.println("commit " + temp.getSha1());
            System.out.println("Date: " + temp.getTimeStamp());
            System.out.println(temp.getMessage());
            System.out.println();
            if (temp.getMessage().equals("initial commit")) {
                break;
            }
            getFile = new File(".gitlet/commits/"
                    + temp.getParent());
            temp = Utils.readObject(getFile, Commit.class);
        }
    }

    /**
     * Used to create globallog. Prints global commit data.
     */
    public static void globalLog() {
        for (int i = _repo.getAllCommits().size() - 1; i >= 0; i--) {
            String elem = _repo.getAllCommits().get(i);
            File getFile = new File(".gitlet/commits/" + elem);
            Commit temp = Utils.readObject(getFile, Commit.class);
            System.out.println("===");
            System.out.println("commit " + elem);
            System.out.println("Date: " + temp.getTimeStamp());
            System.out.println(temp.getMessage());
            System.out.println();
        }
    }

    /**
     * Used to find commit with meassage X.
     */
    public static void find(String x) {
        int check = 0;
        String message = x;
        for (int i = _repo.getAllCommits().size() - 1; i >= 0; i--) {
            String elem = _repo.getAllCommits().get(i);
            File getFile = new File(".gitlet/commits/" + elem);
            Commit temp = Utils.readObject(getFile, Commit.class);
            if (temp.getMessage().equals(message)) {
                System.out.println(elem);
                check = 1;
            }
        }
        if (check == 0) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
    }

    /**
     * Used to get status prints commit history and staging data..
     */
    public static void status() {
        File getFile = new File(".gitlet/commits/"
                + _repo.getHeadCommit());
        Commit head = Utils.readObject(getFile, Commit.class);
        File f = new File(".");
        ArrayList<String> pathNames = new ArrayList<String>();
        for (String x : f.list()) {
            File g = new File(x);
            if (g.isFile() && x.contains(".txt")) {
                pathNames.add(x);
            }
        }
        Collections.sort(pathNames);
        System.out.println("=== Branches ===");
        Object[] branches = _repo.getBranches().keySet().toArray();
        Arrays.sort(branches);
        for (Object elem : branches) {
            if (elem.equals(_repo.getCurrentBranch())) {
                System.out.print("*");
            }
            System.out.print(elem);
            System.out.println();
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        Object[] staging = _repo.getStaging().keySet().toArray();
        Arrays.sort(staging);
        for (Object elem : staging) {
            System.out.println(elem);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        Object[] remove = _repo.getRemove().toArray();
        Arrays.sort(remove);
        for (Object elem : remove) {
            System.out.println(elem);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        statusHelper(head, pathNames);
        System.out.println("=== Untracked Files ===");
        for (String elem : pathNames) {
            if (!head.getData().containsKey(elem)
                    && !_repo.getStaging().containsKey(elem)) {
                System.out.println(elem);
            } else if (_repo.getRemove().contains(elem)) {
                System.out.println(elem);
            }
        }
        System.out.println();
    }

    /**
     * Help Status HEAD PATHNAMES.
     */
    public static void statusHelper(Commit head, ArrayList<String> pathNames) {
        for (String elem : pathNames) {
            Blob e = new Blob(elem);
            if (head.getData().containsKey(elem)
                    && !_repo.getStaging().containsKey(elem)
                    && !head.getData().get(elem).equals(e.getSha1())) {
                System.out.println(elem + " (modified)");
            } else if (_repo.getStaging().containsKey(elem)
                    && !_repo.getStaging().get(elem).equals(e.getSha1())) {
                System.out.println(elem + " (modified)");
            }
        }
        for (String elem : _repo.getStaging().keySet()) {
            File e = new File(elem);
            if (!e.isFile()) {
                System.out.println(elem + " (deleted)");
            }
        }
        for (String elem : head.getData().keySet()) {
            File e = new File(elem);
            if (!_repo.getRemove().contains(elem) && !e.isFile()) {
                System.out.println(elem + " (deleted)");
            }
        }
        System.out.println();
    }

    /**
     * Used to checkout FILENAME.
     */
    public static void checkOut1(String fileName) {
        checkOut2(_repo.getHeadCommit(), fileName);
    }

    /**
     * Used to checkout FILENAME in commit ID.
     */
    public static void checkOut2(String id, String fileName) {
        for (String abb : _repo.getAllCommits()) {
            int i = 0;
            char z = id.charAt(i);
            char a = abb.charAt(i);
            while (a == z) {
                i += 1;
                if (i == id.length()) {
                    id = abb;
                    break;
                }
                z = id.charAt(i);
                a = abb.charAt(i);
            }
        }
        File getFile = new File(".gitlet/commits/" + id);
        Commit c = null;
        try {
            c = Utils.readObject(getFile, Commit.class);
        } catch (java.lang.IllegalArgumentException x) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        String blobSha1 = c.getData().get(fileName);
        File getFileBlob = new File(".gitlet/blobs/" + blobSha1);
        Blob b = null;
        try {
            b = Utils.readObject(getFileBlob, Blob.class);
        } catch (java.lang.IllegalArgumentException x) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        File fileWrite = new File(fileName);
        Utils.writeContents(fileWrite, b.getContents());
    }

    /**
     * Used to checkout BRANCHNAME.
     */
    public static void checkOut3(String branchName) {
        if (!_repo.getBranches().containsKey(branchName)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        } else if (_repo.getCurrentBranch().equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        File getFileOld = new File(".gitlet/commits/"
                + _repo.getHeadCommit());
        Commit oldHead = Utils.readObject(getFileOld, Commit.class);
        File getFile = new File(".gitlet/commits/"
                + _repo.getBranches().get(branchName));
        Commit id = Utils.readObject(getFile, Commit.class);
        for (String name : id.getData().keySet()) {
            if (!oldHead.getData().containsKey(name)) {
                File temp = new File(name);
                if (temp.isFile()) {
                    System.out.println("There is an untracked file in "
                            + "the way; delete it, or add and commit "
                            + "it first.");
                    System.exit(0);
                }
            }
        }
        if (!branchName.equals(_repo.getCurrentBranch())) {
            _repo.getStaging().clear();
        }
        _repo.setCurrentBranch(branchName);
        for (String f : id.getData().keySet()) {
            checkOut1(f);
        }
        for (String name : oldHead.getData().keySet()) {
            if (!id.getData().containsKey(name)) {
                Utils.restrictedDelete(name);
            }
        }

    }

    /**
     * Used to reset with ID.
     */
    public static void reset(String id) {
        File getFile = new File(".gitlet/commits/" + id);
        Commit idCommit = null;
        try {
            idCommit = Utils.readObject(getFile, Commit.class);
        } catch (java.lang.IllegalArgumentException x) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        File getFileOld = new File(".gitlet/commits/"
                + _repo.getHeadCommit());
        Commit oldHead = Utils.readObject(getFileOld, Commit.class);
        for (String name : idCommit.getData().keySet()) {
            if (!oldHead.getData().containsKey(name)) {
                File temp = new File(name);
                if (temp.isFile()) {
                    System.out.println("There is an untracked file in the way; "
                            + "delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
        }
        _repo.moveCurrentBranchHead(id);
        _repo.getStaging().clear();
        _repo.getRemove().clear();
        for (String f : idCommit.getData().keySet()) {
            checkOut2(id, f);
        }
        for (String name : oldHead.getData().keySet()) {
            if (!idCommit.getData().containsKey(name)) {
                Utils.restrictedDelete(name);
            }
        }
    }

    /**
     * Used to merge BRANCH with optional split U.
     */
    public static void merge(String branch, Commit u) {
        if (!_repo.getRemove().isEmpty() || !_repo.getStaging().isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        } else if (!_repo.getBranches().containsKey(branch)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        } else if (branch.equals(_repo.getCurrentBranch())) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        File getFile = new File(".gitlet/commits/" + _repo.getHeadCommit());
        Commit head = Utils.readObject(getFile, Commit.class);
        File getFileBranch = new File(".gitlet/commits/"
                + _repo.getBranches().get(branch));
        Commit branchHead = Utils.readObject(getFileBranch, Commit.class);
        for (String name : branchHead.getData().keySet()) {
            if (!head.getData().containsKey(name)) {
                File temp = new File(name);
                if (temp.isFile()) {
                    System.out.println("There is an untracked file in the way; "
                            + "delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
        }
        Commit split = null;
        if (head.getAncestry().containsKey(branch)) {
            String s = head.getAncestry().get(branch);
            File getFileSplit = new File(".gitlet/commits/" + s);
            split = Utils.readObject(getFileSplit, Commit.class);
        } else if (branchHead.getAncestry().containsKey(
                _repo.getCurrentBranch())) {
            String s = branchHead.getAncestry().get(_repo.getCurrentBranch());
            File getFileSplit = new File(".gitlet/commits/" + s);
            split = Utils.readObject(getFileSplit, Commit.class);
        }
        if (split == null) {
            split = u;
        }
        if (split.getSha1().equals(branchHead.getSha1())) {
            System.out.println("Given branch is an "
                    + "ancestor of the current branch.");
            System.exit(0);
        } else if (split.getSha1().equals(head.getSha1())) {
            checkOut3(branch);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
        mergeHelperUnite(split, head, branchHead, branch, getFileBranch);
    }

    /**
     * Used to help merge, SPLIT, HEAD, BRANCHHEAD, BRANCH, GETFILEBRANCH.
     */
    public static void mergeHelperUnite(Commit split, Commit head,
                                        Commit branchHead, String branch,
                                        File getFileBranch) {
        Boolean conflict = false;
        mergeHelper2(split, head, branchHead, branch);
        mergeHelper3(split, head, branchHead, branch, getFileBranch, conflict);
    }

    /**
     * Used to help merge, KEY, HEAD, BRANCHHEAD.
     */
    public static void mergeHelper(String key, Commit head, Commit branchHead) {
        File conflict = new File(key);
        File h = new File(".gitlet/blobs/" + head.getData().get(key));
        File b = new File(".gitlet/blobs/" + branchHead.getData().get(key));
        String x = "";
        try {
            Blob hBlob = Utils.readObject(h, Blob.class);
            x = hBlob.getContents();
        } catch (java.lang.IllegalArgumentException e) {
            assert true;
        }
        String y = "";
        try {
            Blob bBlob = Utils.readObject(b, Blob.class);
            y = bBlob.getContents();
        } catch (java.lang.IllegalArgumentException e) {
            assert true;
        }
        Utils.writeContents(conflict, "<<<<<<< HEAD"
                + "\n" + x + "=======" + "\n" + y + ">>>>>>>" + "\n");
        add(key);
    }

    /**
     * Used to help merge, SPLIT, HEAD, BRANCHHEAD, BRANCH.
     */
    public static void mergeHelper2(Commit split, Commit head,
                                    Commit branchHead, String branch) {
        for (String key : branchHead.getData().keySet()) {
            if (!head.getData().containsKey(key)
                    || !split.getData().containsKey(key)) {
                assert true;
            } else if (!branchHead.getData().get(key).equals(
                    split.getData().get(key))) {
                if (head.getData().get(key).equals(split.getData().get(key))) {
                    checkOut2(_repo.getBranches().get(branch), key);
                    add(key);
                }
            }
        }
        for (String key : branchHead.getData().keySet()) {
            if (!split.getData().containsKey(key)) {
                if (!head.getData().containsKey(key)) {
                    checkOut2(_repo.getBranches().get(branch), key);
                    add(key);
                }
            }
        }
        for (String key : split.getData().keySet()) {
            if (!head.getData().containsKey(key)) {
                assert true;
            } else if (head.getData().get(key).equals(
                    split.getData().get(key))) {
                if (!branchHead.getData().containsKey(key)) {
                    Utils.restrictedDelete(key);
                    rm(key);
                }
            }
        }
        for (String key : split.getData().keySet()) {
            if (!branchHead.getData().containsKey(key)) {
                assert true;
            } else if (branchHead.getData().get(key).equals(
                    split.getData().get(key))) {
                if (!head.getData().containsKey(key)) {
                    _repo.getRemove().add(key);
                }
            }
        }
    }

    /**
     * Used to help merge, SPLIT, HEAD, BRANCHHEAD, BRANCH, GETFILEBRANCH
     * CONFLICT.
     */
    public static void mergeHelper3(Commit split, Commit head,
                 Commit branchHead, String branch,
                                    File getFileBranch, Boolean conflict) {
        for (String key : split.getData().keySet()) {
            if (head.getData().containsKey(key)
                    && branchHead.getData().containsKey(key)
                    && !head.getData().get(key).equals(split.getData().get(key))
                    && !branchHead.getData().get(key).equals(
                            split.getData().get(key))
                    && !branchHead.getData().get(key).equals(
                    head.getData().get(key))) {
                mergeHelper(key, head, branchHead);
                conflict = true;
            }
            if (head.getData().containsKey(key)
                    && !head.getData().get(key).equals(split.getData().get(key))
                    && !branchHead.getData().containsKey(key)) {
                mergeHelper(key, head, branchHead);
                conflict = true;
            }
            if (branchHead.getData().containsKey(key)
                    && !branchHead.getData().get(key).equals(
                    split.getData().get(key))
                    && !head.getData().containsKey(key)) {
                mergeHelper(key, head, branchHead);
                conflict = true;
            }
        }
        for (String key : branchHead.getData().keySet()) {
            if (!split.getData().containsKey(key)
                    && head.getData().containsKey(key)) {
                if (!branchHead.getData().get(key).equals(
                        head.getData().get(key))) {
                    mergeHelper(key, head, branchHead);
                    conflict = true;
                }
            }
        }
        if (!split.getSha1().equals(head.getSha1())
                && !split.getSha1().equals(branchHead.getSha1())) {
            mergedCommit("Merged " + branch + " into "
                            + _repo.getCurrentBranch() + ".", head.getSha1(),
                    branchHead.getSha1());
            if (!branchHead.getAncestry().containsKey(
                    _repo.getCurrentBranch())) {
                branchHead.getAncestry().put(_repo.getCurrentBranch(),
                        branchHead.getSha1());
            } else {
                branchHead.getAncestry().replace(_repo.getCurrentBranch(),
                        branchHead.getSha1());
            }
            try {
                getFileBranch.createNewFile();
            } catch (java.io.IOException x) {
                assert true;
            }
            Utils.writeObject(getFileBranch, branchHead);
        }
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    /**
     * Used to deserialize remote.
     */
    public static void deSerializeRemote() {
        File getBFile = new File(".gitlet/remoteMap");
        _remoteMap = Utils.readObject(getBFile, HashMapMod.class);
    }

    /**
     * Used to serialize remote.
     */
    public static void serializeRemote() {
        File bfile = new File(".gitlet/remoteMap");
        try {
            bfile.createNewFile();
        } catch (java.io.IOException x) {
            assert true;
        }
        Utils.writeObject(bfile, _remoteMap);
    }

    /**
     * Used to add remote NAME with PATH.
     */
    public static void addRemote(String name, String path) {
        try {
            deSerializeRemote();
        } catch (java.lang.IllegalArgumentException x) {
            _remoteMap = new HashMapMod();
        }
        if (_remoteMap.containsKey(name)) {
            System.out.println("A remote with that name already exists.");
            System.exit(0);
        }
        String newPath = "";
        for (int i = 0; i < path.length(); i++) {
            char elem = path.charAt(i);
            if (elem == '/') {
                newPath += java.io.File.separator;
            } else {
                newPath += elem;
            }
        }
        _remoteMap.put(name, newPath);
        serializeRemote();
    }

    /**
     * Used to rm remote with NAME.
     */
    public static void rmRemote(String name) {
        try {
            deSerializeRemote();
        } catch (java.lang.IllegalArgumentException x) {
            System.out.println("A remote with that name does not exist.");
            System.exit(0);
        }
        if (!_remoteMap.containsKey(name)) {
            System.out.println("A remote with that name does not exist.");
            System.exit(0);
        }
        _remoteMap.remove(name);
        serializeRemote();
    }

    /**
     * Used to push NAME with BRANCHNAME.
     */
    public static void push(String name, String branchName) {
        deSerializeRemote();
        if (!_remoteMap.containsKey(name)) {
            System.out.println("Remote directory not found.");
            System.exit(0);
        }
        Repository newRepo = null;
        try {
            File getFile = new File(_remoteMap.get(name) + "/repo");
            newRepo = Utils.readObject(getFile, Repository.class);
        } catch (java.lang.IllegalArgumentException x) {
            System.out.println("Remote directory not found.");
            System.exit(0);
        }
        File headFile = new File(".gitlet/commits/" + _repo.getHeadCommit());
        Commit head = Utils.readObject(headFile, Commit.class);
        File branchFile = new File(_remoteMap.get(name)
                + "/commits/" + newRepo.getBranches().get(branchName));
        Commit branchHead = Utils.readObject(branchFile, Commit.class);
        ArrayList<String> toAdd = new ArrayList<String>();
        Commit temp = head;
        pushHelper(branchHead, temp, name, toAdd);
        newRepo.getAllCommits().addAll(toAdd);
        newRepo.setHeadCommit(head.getSha1());
        File sFile = new File(_remoteMap.get(name) + "/repo");
        try {
            sFile.createNewFile();
        } catch (java.io.IOException x) {
            assert true;
        }
        Utils.writeObject(sFile, newRepo);
        serializeRemote();
    }

    /**
     * Push helper, BRANCHHEAD, TEMP, NAME, TOADD.
     */
    public static void pushHelper(Commit branchHead,
                                  Commit temp, String name,
                                  ArrayList<String> toAdd) {
        while (true) {
            if (branchHead.getSha1().equals(temp.getSha1())) {
                break;
            } else {
                toAdd.add(temp.getSha1());
                File file = new File(_remoteMap.get(name)
                        + "/commits/" + temp.getSha1());
                try {
                    file.createNewFile();
                } catch (java.io.IOException x) {
                    assert true;
                }
                Utils.writeObject(file, temp);
                for (String elem : temp.getData().keySet()) {
                    File bFile = new File(".gitlet/blobs/"
                            + temp.getData().get(elem));
                    Blob x = Utils.readObject(bFile, Blob.class);
                    File f = new File(_remoteMap.get(name)
                            + "/blobs/" + temp.getSha1());
                    try {
                        bFile.createNewFile();
                    } catch (java.io.IOException y) {
                        assert true;
                    }
                    Utils.writeObject(f, x);
                }

            }
            if (temp.getMessage().equals("initial commit")) {
                System.out.println("Please pull "
                        + "down remote changes before pushing.");
                System.exit(0);
            }
            File t = new File(".gitlet/commits/" + temp.getParent());
            temp = Utils.readObject(t, Commit.class);
        }
    }

    /**
     * Used to fetch NAME from BRANCHNAME, Return Commit.
     */
    public static Commit fetch(
            String name, String branchName) {
        Repository newRepo = null;
        deSerializeRemote();
        if (!_remoteMap.containsKey(name)) {
            System.out.println("Remote directory not found.");
            System.exit(0);
        }
        try {
            File getFile = new File(_remoteMap.get(name) + "/repo");
            newRepo = Utils.readObject(getFile, Repository.class);
        } catch (java.lang.IllegalArgumentException x) {
            System.out.println("Remote directory not found.");
            System.exit(0);
        }
        if (!newRepo.getBranches().containsKey(branchName)) {
            System.out.println("That remote does not have that branch.");
            System.exit(0);
        }
        File branchFile = new File(_remoteMap.get(name)
                + "/commits/" + newRepo.getBranches().get(branchName));
        Commit branchHead = Utils.readObject(branchFile, Commit.class);
        Commit temp = branchHead;
        File t = new File(_remoteMap.get(name) + "/commits/" + temp.getSha1());
        Commit l = null;
        while (true) {
            if (!_repo.getAllCommits().contains(temp.getSha1())) {
                _repo.getAllCommits().add(temp.getSha1());
                File file = new File(".gitlet/commits/" + temp.getSha1());
                try {
                    file.createNewFile();
                } catch (java.io.IOException x) {
                    assert true;
                }
                Utils.writeObject(file, temp);
                fetchHelper(name, temp);
            } else {
                l = temp;
            }
            temp = Utils.readObject(t, Commit.class);
            if (temp.getMessage().equals("initial commit")) {
                break;
            }
            t = new File(_remoteMap.get(name)
                    + "/commits/" + temp.getParent());
        }
        if (!_repo.getBranches().containsKey(name + "/" + branchName)) {
            branch(name + "/" + branchName);
        }
        _repo.getBranches().replace(name + "/"
                + branchName, branchHead.getSha1());
        return l;
    }

    /**
     * Fetch helper NAME TEMP.
     */
    public static void fetchHelper(String name, Commit temp) {
        for (String elem : temp.getData().keySet()) {
            File bFile = new File(_remoteMap.get(name)
                    + "/blobs/" + temp.getData().get(elem));
            Blob x = Utils.readObject(bFile, Blob.class);
            File f = new File(".gitlet/blobs/"
                    + temp.getData().get(elem));
            try {
                bFile.createNewFile();
            } catch (java.io.IOException y) {
                assert true;
            }
            Utils.writeObject(f, x);
        }
    }

    /**
     * Used to pull NAME, BRANCHNAME.
     */
    public static void pull(String name, String branchName) {
        Commit x = fetch(name, branchName);
        merge(name + "/" + branchName, x);
    }

    /**
     * Repository.
     */
    private static Repository _repo;
    /**
     * Tells if repo is initialized..
     */
    private static boolean _initialized;
    /**
     * Makes _remoteMap.
     */
    private static HashMapMod _remoteMap;

}
