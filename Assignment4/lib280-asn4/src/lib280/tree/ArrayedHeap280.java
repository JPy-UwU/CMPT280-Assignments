// Mark Cantuba
// 11214496
// MJC862

package lib280.tree;

import lib280.base.Dispenser280;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.ContainerFull280Exception;
import lib280.exception.DuplicateItems280Exception;
import lib280.exception.NoCurrentItem280Exception;
import lib280.list.LinkedList280;
import lib280.tree.ArrayedBinaryTree280;

import java.util.Collections;

/**
 * Arrayed heap extends ArrayedBinaryTree280. This type of arrayed heap will now be ordered. Each element of our array can
 * now be comparable to one another!
 */
public class ArrayedHeap280<I extends Comparable<? super I>> extends ArrayedBinaryTree280<I> implements Dispenser280<I> {

    /**
     * Constructor method that creates our heap.
     * @param cap: Capacity of the heap
     */
    @SuppressWarnings("unchecked")
    public ArrayedHeap280(int cap) {
        super(cap);
        items = (I[]) new Comparable[cap + 1];  // Casts our items list in order to make each elements within to be comparable
    }

    /**
     * Inserts item x into our heap
     * @param x item to be inserted into the data structure
     * @throws ContainerFull280Exception throw an exception if the container is full.
     */
    public void insert(I x) throws ContainerFull280Exception {
        // Check if the container is full
        if (this.isFull()) {
            throw new ContainerFull280Exception("Heap is full!");
        }
        // if the initial count before inserting is 0, then set the current Node pointer to be the root
        if (this.count == 0) {
            this.currentNode++;
        }
        count++;
        items[count] = x;       // To start off, insert the new item on the left most side of the tree
        int counter = count;    // Counter, starting at the last item of the list
        // Check where to insert the new item
        while (this.item().compareTo(x) != 0 && items[counter].compareTo(items[findParent(counter)]) > 0) {
            I holder = items[counter];  // Store the item stored at the left most side of the tree into a var.
            items[counter] = items[findParent(counter)];    // Set the value at index counter to be the value in parent
            items[findParent(counter)] = holder;        // Set the value at parent to be the value in holder
            counter = findParent(counter);              // Set counter to be the parent's index
        }

    }

    /**
     * Deletes the root value of our heap
     * @throws ContainerEmpty280Exception: If container is empty, there is nothing to delete
     */
    public void deleteItem() throws ContainerEmpty280Exception {
        if (this.isEmpty()) {       // If tree is empty, throw an exception since there is nothing to delete
            throw new ContainerEmpty280Exception("Tree is empty and there is no current node!");
        }
        items[1] = items[count];    // Delete the root node as usual
        count--;                    // Decrease heap count by 1
        if (count == 0) {           // If the tree is empty, then set the current node to the before position
            this.currentNode = 0;
        }
        int curr = 1;               // Start at the root node
        // While the left and right index is not greater than the number of nodes in our tree
        while (findRightChild(curr) <= count || findLeftChild(curr) <= count) {
            int prev = curr;    // Holder for the past current node index
            // If the right child item is greater than the left
            if (items[findRightChild(curr)].compareTo(items[findLeftChild(curr)]) > 0) {
                curr = findRightChild(curr);    // Set the current node to be the index of the right child
                if (items[prev].compareTo(items[curr]) < 0) {   // If the previous item is greater than current
                    I holder = items[prev];     // create holder for previous node
                    items[prev] = items[curr];  // set the previous node item to the current item
                    items[curr] = holder;       // Set the current item to be the right node's item
                }
            }
            else {
                curr = findLeftChild(curr);    // Else, set the current node to be the left index
                if (items[prev].compareTo(items[curr]) < 0) {   // If the previous item is less than left item
                    I holder = items[prev]; // Get the previous item, store it in a variable
                    items[prev] = items[curr];  // Set the previous node index to be the current node value
                    items[curr] = holder;       // Set the current position item to be the previous item.
                }
            }
        }
    }
    /**
     * Helper for the regression test.  Verifies the heap property for all nodes.
     */
    private boolean hasHeapProperty() {
        for(int i=1; i <= count; i++) {
            if( findRightChild(i) <= count ) {  // if i Has two children...
                // ... and i is smaller than either of them, , then the heap property is violated.
                if( items[i].compareTo(items[findRightChild(i)]) < 0 ) return false;
                if( items[i].compareTo(items[findLeftChild(i)]) < 0 ) return false;
            }
            else if( findLeftChild(i) <= count ) {  // if n has one child...
                // ... and i is smaller than it, then the heap property is violated.
                if( items[i].compareTo(items[findLeftChild(i)]) < 0 ) return false;
            }
            else break;  // Neither child exists.  So we're done.
        }
        return true;
    }

    /**
     * Regression test
     */
    public static void main(String[] args) {

        ArrayedHeap280<Integer> H = new ArrayedHeap280<>(10);

        // Empty heap should have the heap property.
        if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");

        // Insert items 1 through 10, checking after each insertion that
        // the heap property is retained, and that the top of the heap is correctly i.
        for(int i = 1; i <= 10; i++) {
            H.insert(i);
            if(H.item() != i) System.out.println("Expected current item to be " + i + ", got " + H.item());
            if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");
        }

        // Remove the elements 10 through 1 from the heap, checking
        // after each deletion that the heap property is retained and that
        // the correct item is at the top of the heap.
        for(int i = 10; i >= 1; i--) {
            // Remove the element i.
            H.deleteItem();

            // If we've removed item 1, the heap should be empty.
            if(i==1) {
                if( !H.isEmpty() ) System.out.println("Expected the heap to be empty, but it wasn't.");
            }
            else {

                // Otherwise, the item left at the top of the heap should be equal to i-1.
                if(H.item() != i-1) System.out.println("Expected current item to be " + i + ", got " + H.item());
                if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");
            }
        }

        System.out.println("Regression Test Complete.");
    }


}