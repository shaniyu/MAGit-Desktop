package magitUI;

public class UserMessages {

    public static final String NOT_A_REPO = "This is not a magit repository.";
    public static final String ALREADY_A_REPO = "The path in which you want to create the repository is already a magit repository\n";
    public static final String CHECK_NULL_COMMIT = "Error, this branch doesn't point to any commit.\nCan't checkout.";
    public static final String CHECK_OPEN_CHANGES = "Error, could not check for open changes";
    public static final String REPO_CREATED = "Repository was created and loaded.";
    public static final String EMPTY_REPO_NAME = "Enter the name of the repository you want to create";
    public static final String EMPTY_FOLDER_NAME = "Enter the name of the folder in which you want to create the repository";
    public static final String INVALID_FOLDER_NAME = "Folder name cannot contain \\ or /";
    public static final String EMPTY_PATH = "Path must be not empty";
    public static final String EMPTY_NAME_ = "Name must be not empty.\nTry again..";
    public static final String EMPTY_NAME = "The user name you entered is empty.\nTry again..";
    public static final String UNINIAITLIZED_REPO = "No repository was loaded yet. Please load a repository before committing\n";
    public static final String UNINIAITLIZED_REPO_OPEN_CHANGES = "No repository was loaded yet. Please load a repository before viewing open changes\n";
    public static final String ERROR_CREATE_COMMIT = "Error, could not create a commit";
    public static final String ERROR_CHANGES_BEFORE_COMMIT = "Error, could not check for open changes before commit";
    public static final String DIR_EXIST = "There is already a directory in this path, which is not a magit repository, therefor can't load this file.";
    public static final String COMMIT_MSG_EMPTY = "Message must be not empty.\nTry again..";
    public static final String XML_REPOSITORY_PATH_EXISTS = "There is already a repository in this location\n" +
            "You can do one of the following:\n"
            + "Override the repository in this location with the data in the xml\nor load repository from this location and discard the xml";
    public static final String NOT_VALID_BRANCH_NAME = "You can't use 'HEAD' or 'commits' as branch name, it is a magit saved name.";
    public static final String OPEN_CHANGES_ERROR = "You have some open changes on this branch, can't checkout now.\nYou can ask for status to see the changes";
    public static final String ALREADY_ON_THIS_REPO = "You are already on this repository";
    public static final String NO_COMMITS_YET = "There are no commits in you repository yet.\nThis operation can only be performed when there are commits";
    public static final String ERROR_SHOW_SHA1_OF_COMMITS = "Error while looking for sha1 of all the commits";
    public static final String CHOOSE_SHA1 = "You must choose a sha1 first.";
    public static final String OPEN_CHANGES = "You have some open changes made to files on this branch";
    public static final String DEAL_WITH_OPEN_CHANGES_RESET_HEAD = "Do you want to commit them later and not reset, or delete them and reset anyway?";
    public static final String DEAL_WITH_OPEN_CHANGES_CHECKOUT = "Do you want to commit them later and not checkout, or delete them and checkout anyway?";
    public static final String RESET_OPEN_CHANGES_WARNING = "Warning!\nIf you delete the changes, you will lost those it and could not restore it.";
    public static final String NO_HEAD_COMMIT = "There is no commit in the head branch.";
    public static final String MERGE_ON_OPEN_CHANGES = "You can't merge now, since there are open changes on the head branch.";
    public static final String MERGE_NOT_CHOSEN = "You must select branch to merge with.";
    public static final String CANT_CLONE = "Error, Can't clone.";
    public static final String ILEGAL_CHARS_IN_NAME = "Name can't contain file seperator";
    public static final String NO_REMOTE = "This repository doesn't have any remote";
    public static final String NOT_RTB = "The head branch doesn't track any remote branch.\nCan't pull.";
    public static final String PULL_OPEN_CHANGES = "You can't pull now, since there are open changes on the head branch.";
    public static final String REPO_BEHIND_REMOTE = "Your remote repository has new commits you don't have, perform pull first.";
    public static final String OPEN_CHANGES_ON_REMOTE = "The remote of this repository have open changes, can't push.";
}