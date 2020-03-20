package DataStructures;

import magitObjects.Commit;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;


public class MagitCommitTree {

    // Sha1<->MagitCommitNode dictionary, is used to avoid duplicated nodes in the tree
    private Dictionary<String, MagitCommitNode> sha1ToNodesDictionary;
    private MagitCommitNode root;



    public MagitCommitTree()
    {
        // initialize the dictionary of nodes
        sha1ToNodesDictionary = new Hashtable<>();
    }
    public MagitCommitNode getRoot() {
        return root;
    }

    public void setRoot(MagitCommitNode root) {
        this.root = root;
    }

     public void addNodeToDictionary(String sha1, MagitCommitNode node)
     {
         sha1ToNodesDictionary.put(sha1, node);
     }

     public boolean isCommitSha1InTree(String sha1)
     {
         if (sha1ToNodesDictionary.get(sha1) == null )
             return false;
         else
             return true;
     }
    public Dictionary<String, MagitCommitNode> getSha1ToNodesDictionary() {
        return sha1ToNodesDictionary;
    }
}
