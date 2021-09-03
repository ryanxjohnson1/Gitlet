# Gitlet Design Document

**Name**:

## Classes and Data Structures
### Repository

This class stores the gitlet Repository

** Hashmap <String, BRANCH>_Branches **
##### -branch name to commit ID that leads the branch
##### headCommit = last commit
##### Hashmap _StagingArea (Name, BLOB)
##### String ArrayList _remove
##### String _currentBranch
##### ArrayList<Commit> _AllComits
##### Boolean _CreatedRepo;

### BRANCH

#####Hashmap<branches, commit> _Split
#####_Commit SHA1

### COMMIT(Parent, Message)

Commit _parent
String _Message
String _Timestamp
Hashmap _Data  (Name, BLOB)
	-Copies all Files from parent 
-removes all Files with file name in _stagingArea
-copies all files in _stagingarea
-removes all files with file name in _remove
_SHA1
TOSTRING Method
Add to _AllComits

###BLOB(name of File)

## Algorithms

ADD(Args)
Repository.StagingAreaAdd()

RM(Args)
 -check if the file is in _StagingArea, if so remove it
 -check if the file is in _Data of head commit, if so add it to _remove

LOG
	-Start with Head and iterate back to initial commit, call commit.toString on each one

GLOBAL-LOG
	-Iterate through _AllComits, call commit.toString on each one

FIND(Args)
-Iterate through _AllComits and call commit.toString on everything that has the same
Message

STATUS
	-staging area
	-remove
	-keys of _branches
	_currentBranch

CHECKOUT(ARGS)
OPTION 1
Puts the given file in the head commit into directory

OPTION 2
Puts the given file in the given commit into directory

OPTION 3
The staging area is cleared, unless the checked-out branch is the current branch
Remove all files in directory
Puts the files in the head commit of branch into directory
Current Branch = args
	
BRANCH(ARGS)
	-New key:value pair in _branches
	-Add split to all branches

RM-BRANCH(ARGS)
	-Delete key:value pair in _branches
	-remove the split in all branches - maybe

RESET(ARGS)
	-remove all files in directory
	-Checkout commit ID all files in commit
	-change hashmap _branches Name: branch, Value: SHA1 of ARGS


## Persistence

-Commits
-Blob
-Repository

REPOSITORY
Hashmap <String, BRANCH>_Branches
-branch name to commit ID that leads the branch
headCommit = last commit
Hashmap _StagingArea (Name, BLOB)
String ArrayList _remove
String _currentBranch
ArrayList<Commit> _AllComits
Boolean _CreatedRepo;

BRANCH
	Hashmap<branches, commit> _Split
	_Commit SHA1

COMMIT(Parent, Message)
Commit _parent
String _Message
String _Timestamp
Hashmap _Data  (Name, BLOB)
	-Copies all Files from parent 
-removes all Files with file name in _stagingArea
-copies all files in _stagingarea
-removes all files with file name in _remove
_SHA1
TOSTRING Method
Add to _AllComits

BLOB(name of File)
	String _serialized = serialized file
	_SHA1

ADD(Args)
Repository.StagingAreaAdd()

RM(Args)
 -check if the file is in _StagingArea, if so remove it
 -check if the file is in _Data of head commit, if so add it to _remove

LOG
	-Start with Head and iterate back to initial commit, call commit.toString on each one

GLOBAL-LOG
	-Iterate through _AllComits, call commit.toString on each one

FIND(Args)
-Iterate through _AllComits and call commit.toString on everything that has the same
Message

STATUS
	-staging area
	-remove
	-keys of _branches
	_currentBranch

CHECKOUT(ARGS)
Puts the given file in the head commit into directory
Puts the given file in the given commit into directory
OPTION 3
The staging area is cleared, unless the checked-out branch is the current branch
Remove all files in directory
Puts the files in the head commit of branch into directory
Current Branch = args
	
BRANCH(ARGS)
	-New key:value pair in _branches
	-Add split to all branches

RM-BRANCH(ARGS)
	-Delete key:value pair in _branches
	-remove the split in all branches - maybe

RESET(ARGS)
	-remove all files in directory
	-Checkout commit ID all files in commit
	-change hashmap _branches Name: branch, Value: SHA1 of ARGS

MERGE()
	
REPOSITORY
Hashmap <String, BRANCH>_Branches
-branch name to commit ID that leads the branch
headCommit = last commit
Hashmap _StagingArea (Name, BLOB)
String ArrayList _remove
String _currentBranch
ArrayList<Commit> _AllComits
Boolean _CreatedRepo;

BRANCH
	Hashmap<branches, commit> _Split
	_Commit SHA1

COMMIT(Parent, Message)
Commit _parent
String _Message
String _Timestamp
Hashmap _Data  (Name, BLOB)
	-Copies all Files from parent 
-removes all Files with file name in _stagingArea
-copies all files in _stagingarea
-removes all files with file name in _remove
_SHA1
TOSTRING Method
Add to _AllComits

BLOB(name of File)
	String _serialized = serialized file
	_SHA1

ADD(Args)
Repository.StagingAreaAdd()

RM(Args)
 -check if the file is in _StagingArea, if so remove it
 -check if the file is in _Data of head commit, if so add it to _remove

LOG
	-Start with Head and iterate back to initial commit, call commit.toString on each one

GLOBAL-LOG
	-Iterate through _AllComits, call commit.toString on each one

FIND(Args)
-Iterate through _AllComits and call commit.toString on everything that has the same
Message

STATUS
	-staging area
	-remove
	-keys of _branches
	_currentBranch

CHECKOUT(ARGS)
Puts the given file in the head commit into directory
Puts the given file in the given commit into directory
OPTION 3
The staging area is cleared, unless the checked-out branch is the current branch
Remove all files in directory
Puts the files in the head commit of branch into directory
Current Branch = args
	
BRANCH(ARGS)
	-New key:value pair in _branches
	-Add split to all branches

RM-BRANCH(ARGS)
	-Delete key:value pair in _branches
	-remove the split in all branches - maybe

RESET(ARGS)
	-remove all files in directory
	-Checkout commit ID all files in commit
	-change hashmap _branches Name: branch, Value: SHA1 of ARGS

MERGE()
	
REPOSITORY
Hashmap <String, BRANCH>_Branches
-branch name to commit ID that leads the branch
headCommit = last commit
Hashmap _StagingArea (Name, BLOB)
String ArrayList _remove
String _currentBranch
ArrayList<Commit> _AllComits
Boolean _CreatedRepo;

BRANCH
	Hashmap<branches, commit> _Split
	_Commit SHA1

COMMIT(Parent, Message)
Commit _parent
String _Message
String _Timestamp
Hashmap _Data  (Name, BLOB)
	-Copies all Files from parent 
-removes all Files with file name in _stagingArea
-copies all files in _stagingarea
-removes all files with file name in _remove
_SHA1
TOSTRING Method
Add to _AllComits

BLOB(name of File)
	String _serialized = serialized file
	_SHA1

ADD(Args)
Repository.StagingAreaAdd()

RM(Args)
 -check if the file is in _StagingArea, if so remove it
 -check if the file is in _Data of head commit, if so add it to _remove

LOG
	-Start with Head and iterate back to initial commit, call commit.toString on each one

GLOBAL-LOG
	-Iterate through _AllComits, call commit.toString on each one

FIND(Args)
-Iterate through _AllComits and call commit.toString on everything that has the same
Message

STATUS
	-staging area
	-remove
	-keys of _branches
	_currentBranch

CHECKOUT(ARGS)
Puts the given file in the head commit into directory
Puts the given file in the given commit into directory
OPTION 3
The staging area is cleared, unless the checked-out branch is the current branch
Remove all files in directory
Puts the files in the head commit of branch into directory
Current Branch = args
	
BRANCH(ARGS)
	-New key:value pair in _branches
	-Add split to all branches

RM-BRANCH(ARGS)
	-Delete key:value pair in _branches
	-remove the split in all branches - maybe

RESET(ARGS)
	-remove all files in directory
	-Checkout commit ID all files in commit
	-change hashmap _branches Name: branch, Value: SHA1 of ARGS

MERGE()
	
