package graph;

import DataStructures.MagitCommitNode;
import com.fxgraph.cells.AbstractCell;
import com.fxgraph.graph.Graph;
import com.fxgraph.graph.IEdge;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import magitObjects.Commit;
import magitUI.MagitController;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class CommitNode extends AbstractCell  implements Serializable {

    private String timestamp;
    private String committer;
    private String message;
    private String commitSha1;
    private CommitNodeController commitNodeController;
    private String branchesNamesForLable;
    private Commit commit;
    private MagitController magitController;
    private Boolean isHead;
    private boolean isBold;
    private boolean xValueWasSet = false;
    private int xVal;
    private int yVal;


    public CommitNode(MagitCommitNode commitNode, MagitController magitController, boolean isNodeBold) {
        this.magitController = magitController;
        commit = commitNode.getCommit();
        this.timestamp = commit.getmCreatedDate();
        this.committer = commit.getmCreatedBy();
        this.message = commit.getmMessage();
        this.commitSha1 = commit.getSha1();
        // set the branches pointer by this commit
        addBranchesPointedByCommitToLable(commitNode);
        this.isHead = false;
        this.isBold = isNodeBold;
    }

    public void setIsHead(Boolean isHead) {
        this.isHead = isHead;
    }

    @Override
    public Region getGraphic(Graph graph) {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("commitNode.fxml");
            fxmlLoader.setLocation(url);
            GridPane root = fxmlLoader.load(url.openStream());

            commitNodeController = fxmlLoader.getController();
            commitNodeController.setCommitMessage(message);
            commitNodeController.setCommitter(committer);
            commitNodeController.setCommitTimeStamp(timestamp);
            commitNodeController.setCommitSha1Message(commitSha1);
            commitNodeController.setCommitBranchesTop(branchesNamesForLable);
            commitNodeController.setCommit(commit);
            commitNodeController.setMainController(magitController);
            if (isHead)
            {
                commitNodeController.setHeadColor();
            }
            if (isBold)
            {
                // should bold the node, fill with orange
                commitNodeController.boldCircle();
            }
            return root;
        } catch (IOException e) {
            return new Label("Error when tried to create graphic node !");
        }
    }

    @Override
    public DoubleBinding getXAnchor(Graph graph, IEdge edge) {
        final Region graphic = graph.getGraphic(this);
        return graphic.layoutXProperty().add(commitNodeController.getCircleRadius());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommitNode that = (CommitNode) o;

        return timestamp != null ? timestamp.equals(that.timestamp) : that.timestamp == null;
    }

    @Override
    public int hashCode() {
        return timestamp != null ? timestamp.hashCode() : 0;
    }

    public void addBranchesPointedByCommitToLable( MagitCommitNode commitNode)
    {
        this.branchesNamesForLable = "";
        int numberOfBranches = commitNode.getBranchesThatHasMe().size();

        if (numberOfBranches > 0)
        {
            this.branchesNamesForLable = "<-- ";
            // add first branch name
            this.branchesNamesForLable += commitNode.getBranchesThatHasMe().get(0);

            for (int i = 1 ; i < numberOfBranches ; i++)
            {
                this.branchesNamesForLable += ", " + commitNode.getBranchesThatHasMe().get(i);
            }
        }
    }

    // compare dates of commits to show it in the graph correctly
    public static Comparator<CommitNode> dateComparator = new Comparator<CommitNode>() {
        public int compare(CommitNode node1, CommitNode node2) {
            try
            {
                // we parse the date string into a date and compare the dates
                String dateOfFirst = node1.getTimestamp();
                String dateOfSecond = node2.getTimestamp();
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss:SSS");

                Date dateOfFirstDate = format.parse(dateOfFirst);
                Date dateOfSecondDate = format.parse(dateOfSecond);

                return (dateOfSecondDate.compareTo(dateOfFirstDate));

            }
            catch (ParseException e)
            {
                // if the parsing fails for some reason, we use strings compare instead
                // this is not optimal situation but comparator must know to deal with this case,
                // since compare is an overriding method, and doesn't throw any exception in the parent class.
                return ( node2.getTimestamp().compareTo(node1.getTimestamp()));
            }

        }
    };

    public String getTimestamp() {
        return timestamp;
    }

    // boolean flag to tell if the layout already set a correct x valaue to the node
    public void setxValueWasSet() {
        this.xValueWasSet = true;
    }
    public void setxVal(int xVal) {
        this.xVal = xVal;
    }

    public void setyVal(int yVal) {
        this.yVal = yVal;
    }

    public int getxVal() {
        return xVal;
    }

    public int getyVal() {
        return yVal;
    }
    public boolean isxValueWasSet() {
        return xValueWasSet;
    }
}
