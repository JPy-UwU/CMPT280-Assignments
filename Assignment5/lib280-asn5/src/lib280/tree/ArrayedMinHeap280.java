package lib280.tree;


import lib280.base.Dispenser280;
import lib280.exception.ContainerFull280Exception;
import lib280.exception.InvalidState280Exception;
import lib280.exception.NoCurrentItem280Exception;


public class ArrayedMinHeap280<I extends Comparable<? super I>> extends ArrayedBinaryTree280<I> implements Dispenser280<I> {

	@SuppressWarnings("unchecked")
	public ArrayedMinHeap280(int cap) {
		super(cap);
		items = (I[]) new Comparable[capacity+1];  // Override the constructor's implementation of items
	}
	
	@Override
	public I item() throws NoCurrentItem280Exception {
		if( this.currentNode == 0 ) throw new NoCurrentItem280Exception("The heap is empty.");
		else return this.items[this.currentNode];
	}
		
	/**
	 * Insert a new item into the heap.
	 * 
	 * @param item The item to be inserted.
	 * 
	 */
	public void insert(I item) throws InvalidState280Exception {
		// Insert normally
		if( this.isFull() ) throw new ContainerFull280Exception("Cannot add item to a lib280.tree that is full.");
		else {
			count ++;
			items[count] = item;
		}
		this.currentNode = 1;
		
		if( count == 1 ) return;		
		// Then propagate up the lib280.tree.
		int n = count;
				
		while(n > 1 && items[n].compareTo(items[findParent(n)]) < 0)  {
			int p = findParent(n);
			I temp = items[p];
			items[p] = items[n];
			items[n] = temp;
			n = n / 2;
		}
	}
	
	/**
	 * Removes the item at the top of the heap.
	 * 
	 * 
	 */
	
	public void deleteItem() {
		// Delete the root.  This swaps the root with the last item.
		// Delete the root by moving in the last item.
		// If there is more than one item, and we aren't deleting the last item,
		// copy the last item in the array to the current position.
		if( this.count > 1 ) {
			this.items[currentNode] = this.items[count];
		}		
		this.count--;
		
		// If we deleted the last remaining item, make the the current item invalid, and we're done.
		if( this.count == 0) { 
			this.currentNode = 0;
			return;
		}
				
		// Propagate the new root down.
		int n = 1;
		while( findLeftChild(n) <= count ) {  // While n has a left child...
			// Select the left child.
			int child = findLeftChild(n);
			
			// If the right child exists and is smaller, select it instead.
			if( child + 1 <= count && items[child].compareTo(items[child+1]) > 0 ) 
				child++;
			
			// If the parent is larger than the root...
			if( items[n].compareTo(items[child]) > 0 ) {
				// Swap them.
				I temp = items[n];
				items[n] = items[child];
				items[child] = temp;
				n = child;
			}
			else return;
			
		}
	}
	
	
	// Sift up by finding an item based on reference comparison and sift it up if it needs to be.
	public void siftUp(I item) {
			
		for(int i = 1; i <= count; i++) {
			if( items[i] == item) {
				int current = i;
				while( current > 1 && items[current].compareTo(items[current/2]) < 0) {
					I temp = items[current/2];
					items[current/2] = items[current];
					items[current] = temp;
					current = current / 2;
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
				// ... and i is larger than either of them, , then the heap property is violated.
				if( items[i].compareTo(items[findRightChild(i)]) > 0 ) return false;
				if( items[i].compareTo(items[findLeftChild(i)]) > 0 ) return false;
			}
			else if( findLeftChild(i) <= count ) {  // if n has one child...
				// ... and i is larger than it, then the heap property is violated.
				if( items[i].compareTo(items[findLeftChild(i)]) > 0 ) return false;
			}
			else break;  // Neither child exists.  So we're done.	
		}
		return true;
	}
	
	
	/**
	 * Regression test.
	 */
	public static void main(String[] args) {
		
		ArrayedMinHeap280<Integer> H = new ArrayedMinHeap280<Integer>(10);
		
		// Empty heap should have the heap property.
		if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");

		for(int i = 10; i >= 1; i--) {
			H.insert(i);
			if(H.item() != i) System.out.println("Expected current item to be " + i + ", got " + H.item());
			if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");
		}		
		
		for(int i = 1; i <= 10; i++) {
			if(H.item() != i) System.out.println("Expected current item to be " + i + ", got " + H.item());
			H.deleteItem();
			if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");
		}
			
		System.out.println("Regression Test Complete.");
		
	}
	
}


