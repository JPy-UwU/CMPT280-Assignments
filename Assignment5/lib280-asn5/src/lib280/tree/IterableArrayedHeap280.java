package lib280.tree;

import lib280.exception.NoCurrentItem280Exception;

public class IterableArrayedHeap280<I extends Comparable<? super I>> extends ArrayedHeap280<I> {

    /**
     * Create an iterable heap with a given capacity.
     * @param cap The maximum number of elements that can be in the heap.
     */
    public IterableArrayedHeap280(int cap) {
        super(cap);
    }


    /**
     * Create a new iterator for our IterableArrayedHeap280 class
     * @return a new ArrayedBinaryTreeIterator280 object
     */
    public ArrayedBinaryTreeIterator280<I> iterator() {
        return new ArrayedBinaryTreeIterator280<>(this);
    }

    /**
     * deleteAtPosition deletes the item in which iterator is currently positioned at
     * @param iter an ArrayedBinaryTreeIterator280 object, positioned at the item we wish to delete
     * @throws NoCurrentItem280Exception the parameter has to be pointing at an item. Cannot be positioned at before(), after() or this cannot be empty.
     */
    public void deleteAtPosition(ArrayedBinaryTreeIterator280<I> iter) throws NoCurrentItem280Exception {
        if (iter.before() || iter.after() || !iter.itemExists()) {
            throw new NoCurrentItem280Exception("Cursor is not pointing at a valid item!");
        }

        this.items[iter.currentNode] = this.items[this.count];  // Delete the item in the current iter.currentPosition as normal

        this.count--;   // Decrement counter by 1
        if (this.count == 0) {  // if the tree is empty
            iter.currentNode = 0;   // set iterator's current node to be 0
            return;
        }

        // Reheapify our heap like usual, but instead of starting at index 0, we have to start at the position in which
        // the item is deleted on (iter.currentNode). This was copied from the original delete method from arrayedHeap280(),
        // Since the methodology is the same, with a slight change on the starting n counter.
        int n = iter.currentNode;
        while( findLeftChild(n) <= count ) {
            // Select the left child.
            int child = findLeftChild(n);

            // If the right child exists and is larger, select it instead.
            if( child + 1 <= count && items[child].compareTo(items[child+1]) < 0 )
                child++;

            // If the parent is smaller than the root...
            if( items[n].compareTo(items[child]) < 0 ) {
                // Swap them.
                I temp = items[n];
                items[n] = items[child];
                items[child] = temp;
                n = child;
            }
            else return;
        }
    }
}
