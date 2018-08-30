package lib280.tree;

import lib280.base.NDPoint280;
import lib280.exception.InvalidState280Exception;

/**
 * KDNode class that will serve as a root node for our KDTree class
 */
public class KDNode280<I extends Comparable<? super I>> extends BinaryNode280<NDPoint280> {


    /**
     * Constructor that will create our KDNode280 class.
     * @param dimension: Dimension of the node we are making
     */
    public KDNode280(int dimension) {
        super(new NDPoint280(dimension));
        this.leftNode = null;
        this.rightNode = null;
    }

    /**
     * Constructor using a set of doubles
     * @param set: a set containing N dimension points
     */
    public KDNode280(Double[] set) {
        super(new NDPoint280(set));  // set node point to be a new NDPoint with dimension n
        this.leftNode = null;
        this.rightNode = null;
    }

    @Override
    public NDPoint280 item() {
        return super.item();
    }

    /**
     * Set the coordinate point to the initialized empty node Point object in this Node
     * @param coordinates: a double containing coordinates of N dimension
     * @throws InvalidState280Exception: The size of the double has to correspond to the dimension if the initialized coordinate points
     */
    public void setCoordinates(Double[] coordinates) throws InvalidState280Exception {
        // Check if the coordinate of the double being set is not equal to the initialized NDPoint dimension
        if (coordinates.length != this.getDimension()) {
            throw new InvalidState280Exception("Coordinates of the array being set has to be the same as the given dimension!");
        }
        this.item().setPoint(coordinates);
    }


    /**
     * Get the dimension of the node Point stored in this KDNode
     * @return dimension of the kd node
     */
    public int getDimension() {
        return this.item().dim();
    }

    /**
     * To string method that prints out each individual points stored in the KDNode
     * @return String representation of a single node (RootNode)
     */
    public String toString() {
        return this.item().toString();
    }

    /**
     * Set the left child of this root node
     * @param lnode: Value to be set as the left child of the root node
     */
    protected void setLeftNode(KDNode280<I> lnode) {
        this.leftNode = lnode;
    }

    /**
     * Sets the right node of the given node
     * @param rnode: Value to be set as the right child of this root node
     */
    protected void setRightNode(KDNode280<I> rnode) {
        this.rightNode = rnode;
    }

    /**
     * Getter method that returns the right node of this root node
     * @return right node
     */
    protected KDNode280<I> getRightNode() {
        return (KDNode280<I>) rightNode;
    }

    /**
     * Getter method to get the left node of this root node
     * @return left node
     */
    protected KDNode280<I> getLeftNode() {
        return (KDNode280<I>) leftNode;
    }

    protected boolean isEmpty() {
        return this.item == null;
    }

    /**
     * To string representation of our linked KD node. Method copied from toStringByLevel from linkedSimpleTree
     * and modified to print in nodes in node class instead of rootSubtrees in KDTree since creating individual
     * KDTree containing a node is more complicated in Kd tree compared to simpleTree
     * @param i: Starting level
     * @return to string representation of our KDTree
     */
    protected String toStringByLevel(int i) {
        StringBuffer blanks = new StringBuffer((i - 1) * 5);
        for (int j = 0; j < i - 1; j++)
            blanks.append("     ");

        String result = "";
        if (this.item() != null && this.getRightNode() != null)
            result += getRightNode().toStringByLevel(i+1);
        result += "\n" + blanks + i + ": " ;
        if (item == null)
            result += "-";
        else
        {
            result += this;
            if (this.getLeftNode() != null)
                result += getLeftNode().toStringByLevel(i+1);
        }
        return result;
    }
}
