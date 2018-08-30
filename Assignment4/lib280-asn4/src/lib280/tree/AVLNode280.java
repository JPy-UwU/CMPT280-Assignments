// Mark Cantuba
// 11214496
// MJC862
package lib280.tree;

/**
 * AVL Node that extends a binary node, to add the functionality of keeping the height of each individual nodes
 */
public class AVLNode280<I extends Comparable<? super I>> extends BinaryNode280<I> {
    int height;

    /**
     * Creates a new AVL Node. Height defaults to 1
     * @param item item that is to be stored into the root node
     */
    public AVLNode280(I item) {
        super(item);
        height = 1;
    }

    /**
     * Set height of the current node
     * @param x new height
     */
    protected void setHeight(int x) {
        this.height = x;
    }

    /**
     * Gets the left height of the root node
     * @return height of the left sub node
     */
    protected int getLHeight() {
        if (this.leftNode == null) {
            return 0;
        }
        return ((AVLNode280<I>) this.leftNode).height;
    }

    /**
     * gets the right height of the root node
     * @return height of right sub node
     */
    protected int getRHeight() {
        if (this.rightNode == null) {
            return 0;
        }
        return ((AVLNode280<I>) this.rightNode).height;
    }

    /**
     * Gets the left AVL Node of this node
     * @return left node
     */
    @Override
    public AVLNode280<I> leftNode() {
        return (AVLNode280<I>) super.leftNode();
    }

    /**
     * gets the right node of this avl node
     * @return right avl node
     */
    @Override
    public AVLNode280<I> rightNode() {
        return (AVLNode280<I>) super.rightNode();
    }

    /**
     * Modified to string method to display information regarding the current node's height
     * left height and right heights
     * @return String containing crucial information regarding the node to be printedB
     */
    @Override
    public String toString() {
        return "Item: " + this.item + "  | Height: " + this.height + " | Left Height: " +
                this.getLHeight() + " | Right Height: " + this.getRHeight();
    }
}
