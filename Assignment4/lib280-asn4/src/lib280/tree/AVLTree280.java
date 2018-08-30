// Mark Cantuba
// MJC862
// 11214496

package lib280.tree;

import lib280.base.Dispenser280;
import lib280.base.Searchable280;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.DuplicateItems280Exception;
import lib280.exception.ItemNotFound280Exception;

/**
 * AVL Tree is a self balancing tree, that guarantees a time complexity of O(logN). This class
 * supports insertion, deletion and finding items within the AVL Tree
 * @param <I> Generic type. Can be any data type, and is also comparable.
 */
public class AVLTree280<I extends Comparable<? super I>> extends OrderedSimpleTree280<I> implements
        Searchable280<I>, Dispenser280<I> {

    /**
     * Constructor Method that creates the AVL Tree
     */
    public AVLTree280() {
        super();
    }

    /**
     * Insert method for user to use. Encapsulates the actual code that makes up insert
     * @param item item to be inserted
     */
    @Override
    public void insert(I item) {
        this.rootNode = insert(item, this.rootNode());
    }

    public void delete(I item) {
        this.rootNode = delete(item, this.rootNode());
    }


    /**
     * Overrides the method that returns the root of the tree, to instead return an AVL Node
     * @return root AVL Node of the given tree
     */
    @Override
    protected AVLNode280<I> rootNode() {
        return (AVLNode280<I>) super.rootNode();
    }



    /**
     * Method that performs a left rotation to a given rootNode to fix RR imbalance
     * @param root rootNode to be rotated
     * @return rotated root
     *  ie:
     *   2                     3
     *    \\                 // \\
     *      3       ==>    2      4
     *       \\
     *        4
     */
    private AVLNode280<I> rotateLeft(AVLNode280<I> root) {
        AVLNode280<I> holder = root.rightNode();  // Get the right node of the given node
        root.setRightNode(holder.leftNode()); // set the current Node's right node to be the root's right node's left node.
        holder.setLeftNode(root);   // set the root's right node's left node to be the new, modified rootNode

        // Update the root's height to be holder's the max node between the root's left and right height
        root.setHeight(Math.max(getHeight(root.leftNode()), getHeight(root.rightNode())) + 1);
        // Set the replacement's node height to be the bigger height between the left and right node.
        holder.setHeight(Math.max(getHeight(holder.leftNode()), getHeight(holder.rightNode())) + 1);

        return holder;  // Return the rotated node
    }

    /**
     * Rotates the given node to the right, to fix LL imbalance
     * @param root rootNode to be rotated
     * @return the rotated node
     * ie
     *          3
     *        //                        2
     *      2           ==>          //  \\
     *    //                       1       3
     *   1
     */
    private AVLNode280<I> rotateRight(AVLNode280<I> root) {
        AVLNode280<I> holder = root.leftNode();  // Store the left node of root into holder
        root.setLeftNode(holder.rightNode());    // set the root node's left node to be the holder's right node
        holder.setRightNode(root);  // set the holder's right node to be the modified root node

        // Set the root's new height to be the bigger height between its left and right node
        root.setHeight(Math.max(getHeight(root.leftNode()), getHeight(root.rightNode())) + 1);
        // Set the rotated node's new height to be the bigger height between its left and right node
        holder.setHeight(Math.max(getHeight(holder.leftNode()), getHeight(holder.rightNode())) + 1);

        return holder;  // Return the rotated node
    }



    /**
     * Restores the tree's imbalance according to the balance factors of each nodes
     * @param root Critical node that is imbalanced
     * @return Fixed critical node that is restored
     */
    private AVLNode280<I> restoreAVLProperty(AVLNode280<I> root) {
        int imbalance = getImbalance(root);     // Get the imbalance of the root node
        if (Math.abs(imbalance) <= 1) return root;  // if the imbalance = 1 or = 0, AVL Property holds
        if (imbalance == 2) {   // If the imbalance == 2, the node is Left heavy
            if (getImbalance(root.leftNode()) >= 0) { // If the left node's balance factor >= 0, it has a
                // Left-Left imbalance
                return rotateRight(root);   // rotate to the right
            }
            else {  // Else, the node has and Left-Right imbalance.
                root.leftNode = rotateLeft(root.leftNode());  // perform a left rotation on root's left node
                return rotateRight(root);   // perform a right rotation on the rotated root node
            }
        }
        else {  // Otherwise, the tree is right heavy
            if (getImbalance(root.rightNode()) <= 0) {    // the node has a Right-Right imbalance
                return rotateLeft(root);    // Perform a left rotation
            }
            else {  // Otherwise, the tree is Right-Left imbalanced
                root.rightNode = rotateRight(root.rightNode());   // Perform a right rotation on the right node of the rootNode
                return rotateLeft(root);    /// perform a left rotation on the rotated root node.
            }
        }
    }

    /**
     * Inserts the given item into the
     * @param item item to be inserted to our tree
     * @param root rootNode of our AVL tree
     * @return The tree, in its balanced form (AVL Format)
     * @throws lib280.exception.DuplicateItems280Exception item already exist!
     */
    private AVLNode280<I> insert(I item, AVLNode280<I> root) throws DuplicateItems280Exception {
        if (root == null) {                 // If the current node is null, put the item here.
            root = new AVLNode280<>(item);  // set the root to be a new node with height 1.
        }
        else if (root.item == item) {       // if the item = curNode.item, throw a new  Duplicate item exception.
            throw new DuplicateItems280Exception("Item already exist! Cannot insert item that is already in a tree!");
        }
        else {
            if (root.item.compareTo(item) > 0) {    // If the item is greater than the current item
                root.leftNode = insert(item, root.leftNode());   // Recurse into the left side of the tree
            }
            else {  // Otherwise, if the item is greater than the current node item
                root.rightNode = insert(item, root.rightNode()); // Recurse to the right side of the tree.
            }
        }
        // Update the height of each node after the insertion occurs as recursion unwinds.
        root.setHeight(Math.max(getHeight(root.leftNode()), getHeight(root.rightNode())) + 1);
        return restoreAVLProperty(root);    // Restore the avl property, and return the balanced version of tree
    }

    /**
     * Deletes an item off the AVL Tree
     * @param item item to be deleted
     * @param root rootNode of the AVL tree
     * @throws ItemNotFound280Exception Item is not found
     */
    private AVLNode280<I> delete(I item, AVLNode280<I> root) throws ItemNotFound280Exception {
        if (root == null) {     // If the root is null, the item user wishes to delete doesn't exist
            throw new ItemNotFound280Exception("Item not Found!");
        }
        if (root.item == item) {    // If the item is found, do one of the 4 things
            if (isLeaf(root)) {     //  if the current root is null, return null.
                return null;        // this will set the current node position to be now null
            }
            else if (root.leftNode == null) {   // If the node has 1 child, located on the right side of the node
                return root.rightNode();  // Set the current node to be the right node of this node.
            }
            else if (root.rightNode == null) {  // If the node has 1 child, located on the left side of this node.
                return root.leftNode(); // Set the current node to be the left child of this node
            }
            else {  // Otherwise, if the node have 2 children
                I successor = findSuccessor(root);  // Find the in-order successor of the root
                root.setItem(successor);    // Set the current root node's item to be the in-order successor's item
                root.setRightNode(delete(successor,  root.rightNode())); // Set the right node of this root
                // To be the value returned by this
                //  method after deletion of in order successor
            }
        }
        else if (root.item.compareTo(item) > 0) {    // If the item is less than the current item
            root.leftNode = delete(item, root.leftNode());   // Recurse into the left side of the tree
        }
        else {  // Otherwise, if the item is greater than the current node item
            root.rightNode = delete(item,  root.rightNode()); // Recurse to the right side of the tree.
        }

        // Recalculate all the heights of the tree
        root.setHeight(Math.max(getHeight( root.leftNode()), getHeight(root.rightNode())) + 1);
        return restoreAVLProperty(root);    // Restore the AVL Property, and return restored tree.
    }

    /**
     * Get the height of the given node
     * @param node a node.
     * @return height of the node
     */
    private int getHeight(AVLNode280<I> node) {
        if (node == null) { // If the node == null, the node is empty
            return 0;
        }
        return node.height; // if node is not null, get its height
    }


    /**
     * Get the left subtree of the tree
     * @return the left subtree of the given tree
     * @throws ContainerEmpty280Exception tree cannot be empty
     * Overridden to return an AVLTree instead of OrderedSimpleTree.
     */
    @Override
    public AVLTree280<I> rootLeftSubtree() throws ContainerEmpty280Exception {
        return (AVLTree280<I>) super.rootLeftSubtree();
    }

    /**
     * Check if the given node is a leaf (lNode and rNode are both null)
     * @param anode node to be checked
     * @return true if the given node is a leaf
     * @throws NullPointerException If the given node is null, throw this exception
     */
    private boolean isLeaf(AVLNode280<I> anode) throws NullPointerException {
        if (anode == null) {
            throw new NullPointerException("Given node is null, and does not contain any items");
        }
        return anode.leftNode == null && anode.rightNode == null;
    }

    /**
     * Get the right subtree of the tree
     * @return the right subtree of the given tree
     * @throws ContainerEmpty280Exception tree cannot be empty
     * Overridden to return an AVLTree instead of OrderedSimpleTree.
     */
    @Override
    public AVLTree280<I> rootRightSubtree() throws ContainerEmpty280Exception {
        return (AVLTree280<I>) super.rootRightSubtree();
    }

    /**
     * Gets the imbalance of the given node
     * @param node a node
     * @return the imbalance of the node (Balance Factor)
     */
    private int getImbalance(AVLNode280<I> node) {
        return getHeight(node.leftNode()) - getHeight(node.rightNode());
    }

    /**
     * Check if the tree contains an element x. Since the tree is always balanced, it has a time complexity of
     * O(logN) on the worst case
     * @param x item to be checked
     * @return True if item x is in the tree.
     */
    @Override
    public boolean has(I x) {
        return super.has(x);
    }

    /**
     * Prints out the tree in pre-order format
     */
    public void preOrder() {
        preOrder(this.rootNode());
    }

    /**
     * Finds the in-order Successor of the given node
     * @param anode a node
     * @pre-condition The node has at least 2 children
     */
    private I findSuccessor(AVLNode280<I> anode) {
        AVLNode280<I> rightNode = anode.rightNode();  // Step one node to the right
        if (isLeaf(rightNode)) {    // If the right node doesn't have any children, get the item of this node
            return rightNode.item;
        }
        else {
            while (rightNode.leftNode != null) {    // While the left side of the right node is not null
                rightNode =  rightNode.leftNode(); // Keep stepping to the left.
            }
            return rightNode.item;  // Once it hits the last node, we got our InOrder successor.
        }
    }

    /**
     * preOrder method that contains the code for encapsulation
     * @param root root node of the tree
     */
    private void preOrder(AVLNode280<I> root) {
        if (root == null) {
            return;
        }
        else {
            System.out.println(root);   // Visit node
            preOrder((AVLNode280<I>)root.leftNode); // Traverse left
            preOrder((AVLNode280<I>) root.rightNode);   // Traverse right
        }
    }

    public static void main(String[] args) {
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("                        **** Testing insert method ****");
        System.out.println("--------------------------------------------------------------------------------------------");
        AVLTree280<Integer> tree = new AVLTree280<>();
        if (!tree.isEmpty()) {
            System.out.println("Tree should be empty!");
        }
        tree.insert(10);
        if (tree.rootNode().item != 10) {
            System.out.println("Item should now be 10!");
        }
        System.out.println("****Inserting to form an RR Imbalance on root Node****");
        tree.insert(20);
        tree.insert(42);
        if (tree.rootLeftSubtree().rootNode().item != 10) {
            System.out.println("LL Rotation should set the left subtree to now be 10");
        }
        if (tree.rootRightSubtree().rootNode().item != 42) {
            System.out.println("LL Rotation should set the right subtree to now be 42");
        }
        if (tree.rootNode().item != 20) {
            System.out.println("LL Rotation should set the right subtree to now be 20");
        }

        // Displaying Pre-Order tree to check heights
        tree.preOrder();
        // Displaying current tree
        System.out.println(tree);
        System.out.println("--------------------------------------------------------------------------------------------");

        System.out.println("**** Performing Series of insertion that results to Left-Right imbalance ****");
        tree.insert(32);
        tree.insert(40);

        // Displaying Pre-Order tree to check heights
        tree.preOrder();
        // Displaying current tree
        System.out.println(tree);
        System.out.println("--------------------------------------------------------------------------------------------");

        System.out.println("**** Performing Series of insertion that results to Left-Left imbalance ****");
        tree.insert(5);
        tree.insert(2);

        // Displaying Pre-Order tree to check heights
        tree.preOrder();
        // Displaying current tree
        System.out.println(tree);
        System.out.println("--------------------------------------------------------------------------------------------");

        System.out.println("**** Performing Series of insertion that results to Right-Left imbalance ****");
        tree.insert(4);
        tree.insert(3);

        // Displaying Pre-Order tree to check heights
        tree.preOrder();
        // Displaying current tree
        System.out.println(tree);
        System.out.println("--------------------------------------------------------------------------------------------");
        AVLTree280<Integer> RightDegenerate = new AVLTree280<>();
        System.out.println("**** Performing Series of insertion that will create a right degenerate tree ****");
        for (int i = 1; i < 10; i++) {
            RightDegenerate.insert(i);
        }

        // Displaying Pre-Order tree to check heights
        RightDegenerate.preOrder();
        // Displaying current tree
        System.out.println(RightDegenerate);
        System.out.println("--------------------------------------------------------------------------------------------");
        AVLTree280<Integer> LeftDegenerate = new AVLTree280<>();
        System.out.println("**** Performing Series of insertion that will create a Left degenerate tree ****");
        for (int i = 10; i > 1; i--) {
            LeftDegenerate.insert(i);
        }

        // Displaying Pre-Order tree to check heights
        LeftDegenerate.preOrder();
        // Displaying current tree
        System.out.println(LeftDegenerate);
        System.out.println("--------------------------------------------------------------------------------------------");

        System.out.println("**** Performing insertion to which item to be inserted is already in the tree ****");
        try {
            LeftDegenerate.insert(5);
        } catch (DuplicateItems280Exception d) {
            System.out.println();
        }

        // Displaying Pre-Order tree to check heights
        LeftDegenerate.preOrder();
        // Displaying current tree
        System.out.println(LeftDegenerate);

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("                    **** Testing Delete and has() methods ****");
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("**** Testing deletion on nodes with no children (Using leftDegenerate Tree) ****");
        LeftDegenerate.delete(2);
        if (LeftDegenerate.has(2)) {
            System.out.println("2 Should now be deleted!");
        }
        // Displaying Pre-Order tree to check heights
        LeftDegenerate.preOrder();
        // Displaying current tree
        System.out.println(LeftDegenerate);
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("**** Testing deletion on nodes with 1 child on the right node (Using leftDegenerate Tree) ****");
        LeftDegenerate.delete(3);
        if (LeftDegenerate.has(3)) {
            System.out.println("3 Should now be deleted!");
        }
        // Displaying Pre-Order tree to check heights
        LeftDegenerate.preOrder();
        // Displaying current tree
        System.out.println(LeftDegenerate);
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("**** Testing deletion on nodes with 2 child on the left node (Using leftDegenerate Tree) ****");
        LeftDegenerate.delete(10);
        LeftDegenerate.delete(9);
        if (LeftDegenerate.has(9) || LeftDegenerate.has(10)) {
            System.out.println("9 and 10 Should now be deleted!");
        }
        // Displaying Pre-Order tree to check heights
        LeftDegenerate.preOrder();
        // Displaying current tree
        System.out.println(LeftDegenerate);
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("**** Testing deletion on nodes with 2 children (Using rightDegenerate Tree) ****");
        RightDegenerate.delete(4);
        if (RightDegenerate.has(4)) {
            System.out.println("4 should now be deleted!");
        }

        RightDegenerate.delete(8);
        if (RightDegenerate.has(8)) {
            System.out.println("8 Should now be deleted!");
        }

        RightDegenerate.delete(5);
        if (RightDegenerate.has(5)) {
            System.out.println("5 should now be deleted");
        }
        // Displaying Pre-Order tree to check heights
        RightDegenerate.preOrder();
        // Displaying current tree
        System.out.println(RightDegenerate);
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("**** Deleting all items on the left (Using rightDegenerate Tree) ****");
        RightDegenerate.delete(2);
        if (RightDegenerate.has(2)) {
            System.out.println("2 should now be deleted!");
        }

        RightDegenerate.delete(3);
        if (RightDegenerate.has(3)) {
            System.out.println("8 Should now be deleted!");
        }

        RightDegenerate.delete(1);
        if (RightDegenerate.has(1)) {
            System.out.println("1 should now be deleted");
        }


        // Displaying Pre-Order tree to check heights
        RightDegenerate.preOrder();
        // Displaying current tree
        System.out.println(RightDegenerate);
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("**** Deleting all items (Using rightDegenerate Tree) ****");
        RightDegenerate.delete(7);
        if (RightDegenerate.has(7)) {
            System.out.println("7 should now be deleted!");
        }

        RightDegenerate.delete(9);
        if (RightDegenerate.has(9)) {
            System.out.println("9 Should now be deleted!");
        }

        RightDegenerate.delete(6);
        if (RightDegenerate.has(6)) {
            System.out.println("6 should now be deleted");
        }

        // Displaying Pre-Order tree to check heights
        RightDegenerate.preOrder();
        // Displaying current tree
        System.out.println(RightDegenerate);

        if (!RightDegenerate.isEmpty()) {
            System.out.println("Tree should now be empty!");
        }
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("**** Performing deletion to item that isn't in the tree ****");
        try {
            LeftDegenerate.delete(42);
        } catch (ItemNotFound280Exception i) {
            System.out.println();
        }
        // Displaying Pre-Order tree to check heights
        LeftDegenerate.preOrder();
        // Displaying current tree
        System.out.println(LeftDegenerate);

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("                    **** Regression Test Complete ****");
        System.out.println("--------------------------------------------------------------------------------------------");


    }
}
