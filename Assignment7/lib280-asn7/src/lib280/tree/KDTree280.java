package lib280.tree;
import lib280.base.NDPoint280;
import lib280.exception.DuplicateItems280Exception;
import lib280.list.LinkedList280;

/**
 * KDTree is a type of tree that supports range searching, and is an abstraction of a tree class
 */
public class KDTree280<I extends Comparable<? super I>> extends OrderedSimpleTree280 {
    private KDNode280<I> rootNode;  // Root node stored in the current tree
    private int dimensions; // Dimension of this KDTree

    /**
     * Constructor method that creates our KDTree280. This function calls in the helper method to maintain encapsulation
     * @param pointArray: Array that contains KDNodes to be inputted into our KDTree
     */
    public KDTree280(KDNode280[]pointArray) {
        // If the array provided is empty, do nothing and return nothing
        if (pointArray.length == 0) {
            return;
        }
        // Otherwise, call in our helper method that will construct our KDTree280
        this.setRootNode(KDTree280(pointArray, 0, pointArray.length - 1, 0));
    }

    /**
     * Main KDTree method that will construct our KDTree representation. This will create our KDTree according to the
     * given KDNode280[] pointArray.
     * @param pointArray: an array of KDNodes280 that are of dimension N
     * @param left: offset of start of the sub-array from which to build kd-tree
     * @param right: offset of the end of sub-array from which to build a kd-tree
     * @param depth: Current of the current tree. rootNode is not the same as height! Depth of rootNode=0.
     * @return Constructed tree from the given set of pointArray
     */
    private KDNode280<I> KDTree280(KDNode280[] pointArray, int left, int right, int depth) {
        this.dimensions = pointArray[0].getDimension();     // Set the dimension of this tree to be the dimension of the stored node in the array
        // If right < left, it means we have successfully inserted all the elements from our tree, and we can now finish recursion
        if (right < left) {
            return null;
        }
        // Depth to be compared to based on the value being compared. If dimension = 1, we only have an x value,
        // so we compare each level depth % 1. If dimension = 2, every 2nd depth, including 0 are based on the x axis,
        // and depths 1,3,5,7... are based on y value. Pattern continues for Dimension > 2
        int d = depth % this.getDimensions();
        // Middle offset of the point array.
        int midOffset = (left+right)/2;

        // Set the middle element of pointArray to its appropriate position
        jSmallest(pointArray, left, right, d, midOffset);

        // Create a new node, and construct the subtree based on the outcome of jSmallest
        KDNode280<I> newNode = new KDNode280<>(this.dimensions);
        newNode.setItem(pointArray[midOffset].item()); // Set the item in the middle of the pointArray to be the nodePoint stored in our newly created node
        // move on the next depth level, and move offset back by 1, and set it as the right value. Construct left side of the tree
        newNode.setLeftNode(KDTree280(pointArray, left, midOffset - 1, depth + 1));
        // Construct the right side of the tree. Set the left value to be midOffSet+1.
        newNode.setRightNode(KDTree280(pointArray, midOffset + 1, right, depth + 1));
        return newNode; // Return the constructed tree
    }


    /**
     * Attempt to position the smallest nodePoints in our kd list to its appropriate position
     * @param kdList: A List containing KDNodes to be inserted into our KDTree
     * @param left: Value of the left most offset in kdList. Starts at 0
     * @param right: Value of th right most off set of KDList. Starts at kdList.len - 1
     * @param depth: Starting depth (level - 1) within the tree.
     * @param j: index in which we want to update for index j to hold the appropriate value.
     * @Pre-Conditions: 1) left <= j <= right
     *                  2) all elements in the list have to be unique. 2 set of points cannot be exactly the same
     * @Post-Condition: Element x (points at offset j) if list is sorted. Elements < x are placed on the left side of index j, and
     *                  Elements > x are placed on the right of index j
     */
    private void jSmallest(KDNode280[] kdList, int left, int right, int depth, int j) {
        int d = depth % this.getDimensions();   // Get the value for d. Depending on dimension of KDTree
        // If the right value greater than left
        if (right > left) {
            // Call in partition method
            int pivot = partition(kdList, left, right, d);
            //  If j > pivot, the smallest element should be around between pivot + 1 and right
            if (j > pivot) {
                jSmallest(kdList, pivot + 1, right,depth,j);
            }
            // Otherwise, the smallest points is somewhere around pivot-1 and left
            else if (j < pivot) {
                jSmallest(kdList, left, pivot-1, depth, j);
            }
        }
    }

    /**
     * Partition a sub-array using its last element as a pivot.
     * @param kdList: a list containing nodes to be partitioned
     * @param left: Offset of the left most value of the kdList
     * @param right" Right most offset of KDList
     * @param d: index of the value to be compared,
     * @Pre-condition: all items has to be unique. No 2 points should be exactly the same.
     * @Post-condition: points smaller than pivot are on the left, and larger values are on right of pivot
     * @return: Offset in which the element ended up in
     */
    private int partition(KDNode280[] kdList, int left, int right, int d) {
        KDNode280 pivot = kdList[right];    // Start the pivot on the right most value.
        int swapOffset = left;     // Start the swap offset at the left most offset
        for (int i = left; i < right; i++) {
            // If the pivot point is exactly the same as the element at index i, then there is a duplicate. Throw an Exception
            if (kdList[i].item().compareTo(pivot.item()) == 0) {
                throw new DuplicateItems280Exception("Each point has to be unique and cannot have duplicates for any coordinates!");
            }
            // If the pivot item is greater than or equal to item in index i (by Dimension)
            if (pivot.item().compareByDim(d, kdList[i].item()) >= 0) {
                // Swap the item in index i with the current swapOffset item
                swap(kdList, i, swapOffset);
                swapOffset += 1;    // Move swap offSet 1 to the right
            }
        }
        // Switch the right item with the item contained in index swapOffset
        swap(kdList, right, swapOffset);
        return swapOffset;  // Return the offset where pivot ended up in
    }


    /**
     * Swaps value in index with value in swapOffset
     * @param kdList list containing the items
     * @param index index of item
     * @param swapOffset index of item in which index is to be swapped with
     * @Post-Condition: Swaps item at index index with item in index swapOffset
     */
    private void swap(KDNode280[] kdList, int index, int swapOffset) {
        KDNode280 holder = kdList[index];
        kdList[index] = kdList[swapOffset];
        kdList[swapOffset] = holder;
    }

    /**
     * Public version of SearchRange to preserve encapsulation of the actual code
     * @param hi Starting range.
     * @param lo End range.
     * @return a String showing all the values between lo and hi
     */
    public String searchRange(KDNode280<I> lo, KDNode280<I> hi) {
        LinkedList280<KDNode280> range = new LinkedList280<>(); // Create a new linked list that will hold all our items
        searchRange(this.rootNode, lo, hi, 0, range);   //  Performs the range search
        // Convert stuff in the linked list into string
        range.goFirst();
        String result = "";
        while (range.itemExists()) {
            result += range.item() + "\n";
            range.goForth();
        }
        return result;  // Return the result in string format
    }

    /**
     * searchRange method that performs the actual range search. Contains all the code to encapsulate the actual code
     * used for the implementation.
     * @param root: Root of the node to be inspected
     * @param lo: lower bound value of the first set of nodes
     * @param hi: Higher bound value of the second set of node
     * @param depth: Current level being inspected
     * @param range: A linked list to store the results in
     */
    private void searchRange(KDNode280<I> root,KDNode280<I> lo,KDNode280<I> hi, int depth,LinkedList280<KDNode280> range) {
        // If the root is null, return the empty set
        if (root == null) {
            return;
        }
        int currDepth = depth % dimensions;  // get which point is to be inspected. if 0, then the point being inspected is the x value
        Double splitValue = root.item().idx(currDepth);  // get the nth point stored in the NDPoint of the root
        Double min = root.item().idx(currDepth); // Get the lower bound value of the root
        Double max = root.item().idx(currDepth); // Get the upper bound of the value in the node
        if (min >= splitValue) { // if the lower bound value is greater than the split value, then recurse to the left
           searchRange(root.getLeftNode(),lo,hi,depth + 1, range);
        }
        if (max <= splitValue) { // If the high bound is less than or equal the split value, recurse right
           searchRange(root.getRightNode(),lo,hi,depth + 1, range);
        }
        // If the all the points in root are  lo[currDepth] <= root[currDepth] <= hi[currDepth], then currRoot
        // is within bounds. Insert it to our linked list.
        if (withinRange(lo.item(), root.item(), hi.item())) {
           range.insert(root);
       }
    }

    /**
     * Checks if b is within range of a and c
     * @param a 1st point
     * @param b 2nd point
     * @param c 3rd point
     * @return true if all points of b are within range
     */
    private
    boolean withinRange(NDPoint280 a, NDPoint280 b, NDPoint280 c) {
        boolean withInRange = true;
        for (int i = 0; i < this.dimensions; i++) {
            if (a.compareByDim(i, b) <= 0 && b.compareByDim(i, c) <= 0) {
                withInRange = true;
            }
            else {
                withInRange = false;
            }
            if (!withInRange) {break;}
        }
        return withInRange;
    }
    /**
     * Set the root node of this KDTree
     * @param KDnode: Node to be set as this.rootNode
     */
    private void setRootNode(KDNode280<I> KDnode) {
        this.rootNode = KDnode;
    }

    /**
     * get the dimension stored in the KDTree
     * @return dimension of the KDTree
     */
    private int getDimensions() {
        return this.dimensions;
    }

    /**
     * To String method that will print our KDTree
     * @return to string representation of our tree. To string by level is inside kdNode.
     */
    @Override
    public String toString() {
        return this.rootNode.toStringByLevel(1);
    }

    public static void main(String[] args) {
        System.out.println("*************************************************************");
        System.out.println("*** Testing Creation of KDTree using 2 dimensional points ***");
        System.out.println("*************************************************************");

        KDNode280[] KD2d = new KDNode280[12];
        Double[][] setOfDoubles = {{5.0, 2.0}, {9.0, 10.0}, {11.0, 1.0},{4.0, 3.0},
                {2.0, 12.0}, {3.0, 7.0}, {1.0, 5.0}, {42.0, 11.0},{6.0, 3.3},{7.0, 52.1} ,{2.1, 1.2} ,{8.1, 22.0} };
        // Initialize all the nodes contained in our list to kd nodes of dimension 2
        for (int i = 0; i < KD2d.length; i++) {
            KD2d[i] = new KDNode280<>(setOfDoubles[i]);
        }
        System.out.println("***********************");
        System.out.println("*** Input 2D Points ***");
        System.out.println("***********************");
        for (int i = 0; i<12;i++) {
            System.out.println(i+1 + "-> " + KD2d[i]);
        }
        System.out.println("**********************************************");
        System.out.println("*** The 2D tree built from these points is ***");
        System.out.println("**********************************************");

        KDTree280 tree = new KDTree280(KD2d);
        System.out.println(tree);
        System.out.println("*****************************************************************************************");

        System.out.println("*************************************************************");
        System.out.println("*** Sample 2 KDTree using 2 dimensional points ***");
        System.out.println("*************************************************************");

        KDNode280[] KD2d2 = new KDNode280[7];
        Double[][] setOfDoubles2 = {{5.0, 2.0}, {9.0, 10.0}, {11.0, 1.0},{4.0, 3.0},
                {2.0, 12.0}, {3.0, 7.0}, {1.0, 5.0}};
        // Initialize all the nodes contained in our list to kd nodes of dimension 2
        for (int i = 0; i < KD2d2.length; i++) {
            KD2d2[i] = new KDNode280<>(setOfDoubles2[i]);
        }
        System.out.println("***********************");
        System.out.println("*** Input 2D Points ***");
        System.out.println("***********************");
        for (int i = 0; i<7;i++) {
            System.out.println(i+1 + "-> " + KD2d2[i]);
        }
        System.out.println("**********************************************");
        System.out.println("*** The 2D tree built from these points is ***");
        System.out.println("**********************************************");

        KDTree280 tree2 = new KDTree280(KD2d2);
        System.out.println(tree2);
        System.out.println("*****************************************************************************************");

        System.out.println("*************************************************************");
        System.out.println("*** Testing Creation of KDTree using 3 dimensional points ***");
        System.out.println("*************************************************************");
        KDNode280[] KD3d = new KDNode280[8];
        Double[][] setOf3Doubles = {{1.0, 12.0, 1.0},{18.0, 1.0, 2.0}, {2.0, 12.0, 16.0},{7.0, 3.0, 3.0}, {3.0, 7.0, 5.0},
                                    {16.0, 4.0, 4.0}, {4.0, 6.0, 1.0}, {5.0, 5.0, 17.0}};
        for (int i = 0; i < KD3d.length; i++) {
            KD3d[i] = new KDNode280<>(setOf3Doubles[i]);
        }

        System.out.println("***********************");
        System.out.println("*** Input 3D Points ***");
        System.out.println("***********************");
        for (int i = 0; i<8;i++) {
            System.out.println(i+1 + "-> " + KD3d[i]);
        }
        System.out.println("**********************************************");
        System.out.println("*** The 3D tree built from these points is ***");
        System.out.println("**********************************************");

        KDTree280 treeD = new KDTree280(KD3d);
        System.out.println(treeD);

        System.out.println("**************************************************************************");
        System.out.println("*** Looking for points between (0.0 , 1.0 , 0.0) and (4.0 , 6.0 , 3.0) ***");
        System.out.println("**************************************************************************");
        Double[] lo = {0.0 , 1.0 , 0.0};
        Double[] hi = {4.0 , 6.0 , 3.0};
        KDNode280 loNode = new KDNode280(lo);
        KDNode280 hiNode = new KDNode280(hi);
        System.out.println(treeD.searchRange(loNode, hiNode));

        System.out.println("**************************************************************************");
        System.out.println("*** Looking for points between (0.0 , 1.0 , 0.0) and (8.0 , 7.0 , 4.0). **");
        System.out.println("**************************************************************************");
        Double[] hi2 = {8.0, 7.0, 4.0};
        KDNode280 newhi = new KDNode280(hi2);
        System.out.println(treeD.searchRange(loNode, newhi));

        System.out.println("****************************************************************************");
        System.out.println("*** Looking for points between (0.0 , 1.0 , 0.0) and (17.0 , 9.0 , 10.0). **");
        System.out.println("****************************************************************************");
        Double[] hi3 = {17.0, 9.0, 10.0};
        KDNode280 newhi1 = new KDNode280(hi3);
        System.out.println(treeD.searchRange(loNode, newhi1));


        System.out.println("*****************************************************************");
        System.out.println("*** Testing Creation of 2nd KDTree using 3 dimensional points ***");
        System.out.println("******************************************************************");
        KDNode280[] KD3d2 = new KDNode280[8];
        Double[][] setOf3Doubles2 = {{1.0, 12.0, 0.0},{18.0, 1.0, 2.0}, {2.0, 13.0, 16.0},{7.0, 3.0, 3.0}, {3.0, 7.0, 5.0},
                {16.0, 4.0, 4.0}, {4.0, 6.0, 1.0}, {5.0, 5.0, 17.0}};
        for (int i = 0; i < KD3d2.length; i++) {
            KD3d2[i] = new KDNode280<>(setOf3Doubles2[i]);
        }

        System.out.println("***********************");
        System.out.println("*** Input 3D Points ***");
        System.out.println("***********************");
        for (int i = 0; i<8;i++) {
            System.out.println(i+1 + "-> " + KD3d2[i]);
        }
        System.out.println("**********************************************");
        System.out.println("*** The 3D tree built from these points is ***");
        System.out.println("**********************************************");

        KDTree280 treeD2 = new KDTree280(KD3d2);
        System.out.println(treeD2);

        System.out.println("**************************************************************************");
        System.out.println("*** Looking for points between (0.0 , 1.0 , 0.0) and (4.0 , 6.0 , 3.0) ***");
        System.out.println("**************************************************************************");
        Double[] lo2 = {0.0 , 1.0 , 0.0};
        Double[] hi4 = {4.0 , 6.0 , 3.0};
        KDNode280 loNode2 = new KDNode280(lo2);
        KDNode280 hiNode2 = new KDNode280(hi4);
        System.out.println(treeD.searchRange(loNode2, hiNode2));

        System.out.println("**************************************************************************");
        System.out.println("*** Looking for points between (0.0 , 1.0 , 0.0) and (8.0 , 7.0 , 4.0). **");
        System.out.println("**************************************************************************");
        Double[] hi5 = {8.0, 7.0, 4.0};
        KDNode280 newhi2 = new KDNode280(hi5);
        System.out.println(treeD.searchRange(loNode2, newhi2));

        System.out.println("****************************************************************************");
        System.out.println("*** Looking for points between (0.0 , 1.0 , 0.0) and (17.0 , 9.0 , 10.0). **");
        System.out.println("****************************************************************************");
        Double[] hi6 = {17.0, 9.0, 10.0};
        KDNode280 newhi3 = new KDNode280(hi6);
        System.out.println(treeD.searchRange(loNode2, newhi3));

    }

}
