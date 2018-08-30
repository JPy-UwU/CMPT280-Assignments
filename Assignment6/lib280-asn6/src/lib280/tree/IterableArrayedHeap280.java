package lib280.tree;

import lib280.exception.InvalidArgument280Exception;

public class IterableArrayedHeap280<I extends Comparable<? super I>> extends ArrayedHeap280<I> {

	public IterableArrayedHeap280(int cap) {
		super(cap);
	}

	public ArrayedBinaryTreeIterator280<I> iterator() {
		return new ArrayedBinaryTreeIterator280<I>(this);
	}
	
	public void deleteAtPosition(ArrayedBinaryTreeIterator280<I> pos) throws InvalidArgument280Exception {
		// Delete the item at pos.currentNode by moving in the last item.
		// If there is more than one item, and we aren't deleting the last item,
		// copy the last item in the array to the current position.
		if( !pos.itemExists() )
			throw new InvalidArgument280Exception("The provided position to delete is not valid.");
		
		if( this.count > 1 && this.itemExists() ) {
			this.items[pos.currentNode] = this.items[count];
		}		
		this.count--;

		// If we deleted the last remaining item, make the the current item invalid, and we're done.
		if( this.count == 0) { 
			this.currentNode = 0;
			return;
		}

		// Propagate the copied item down the heap.
		int n = pos.currentNode;

		// While offset n has a left child...
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
