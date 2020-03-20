package graph;//package graph.layout;

import com.fxgraph.graph.Graph;
import com.fxgraph.graph.ICell;
import com.fxgraph.layout.Layout;
import magitObjects.Commit;
//import Com;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class CommitTreeLayout implements Layout {

    // we have to change it so it will show the commits according to their branches
    // so all of a branch commits will be placed in a parallel line
    @Override
    public void execute(Graph graph) {
        final List<ICell> cells = graph.getModel().getAllCells();
        ArrayList<CommitNode> sortedNodesByDated = new ArrayList<>();
        for (ICell cell : cells)
        {
            sortedNodesByDated.add((CommitNode)cell);
        }
        //sort the nodes by the date string
        Collections.sort(sortedNodesByDated, CommitNode.dateComparator);

        HashSet<Integer> xValuesInUse = new HashSet<>();

        int startX = Constants.firstDistanceFromTopX;
        int startY = Constants.firstDistanceFromTopY;

        for (CommitNode node : sortedNodesByDated)
        {
            if (node.getCellChildren().size() == 0)
            {
                // cell has no children, is a a leaf
                int xVal = findMininalFreeXValue(xValuesInUse);
                node.setxVal(xVal);
                node.isxValueWasSet();
                xValuesInUse.add(xVal);
                graph.getGraphic(node).relocate(xVal * 50, startY);
            }
            else
            {
                // not a leaf, so my parents already set my x value
                if(node.isxValueWasSet())  // sanity check
                {
                    graph.getGraphic(node).relocate(node.getxVal() *50, startY);
                }
            }
            // not the root commit ( first ever commit)
            if (node.getCellParents().size() != 0)
            {
                CommitNode parent1 = (CommitNode)node.getCellParents().get(0);
                // x value of the parent noe set yet
                if (! parent1.isxValueWasSet())
                {
                    parent1.setxValueWasSet();
                    parent1.setxVal(node.getxVal());
                }
                // if have also a second parent, this node is a result of a merge
                if (node.getCellParents().size() == 2)
                {
                    CommitNode parent2 = (CommitNode)node.getCellParents().get(1);
                    if (! parent2.isxValueWasSet())
                    {
                        parent2.setxValueWasSet();
                        int nextFreeXVal = findMininalFreeXValue(xValuesInUse);
                        parent2.setxVal(nextFreeXVal);
                        xValuesInUse.add(nextFreeXVal);
                    }
                }
            }
            startY += 50;
        }
    }

    private int findMininalFreeXValue(HashSet<Integer> setOfXValues)
    {
        int i = 0;
        boolean foundMin = false;
        while (! foundMin)
        {
            if (!setOfXValues.contains(i))
            {
                foundMin = true;
            }
            else
                i++;
        }
        return i;
    }

//    private CommitNode getCommitNodeBySha1(ArrayList<CommitNode> sortedNodesByDated, )
//    {
//        for (CommitNode node : sortedNodesByDated)
//        {
//            if
//        }
//    }
}
