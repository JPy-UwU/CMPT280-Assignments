package lib280.tree;


import lib280.base.Dispenser280;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.ContainerFull280Exception;
import lib280.exception.NoCurrentItem280Exception;

public class ArrayedHeap280<I extends Comparable<? super I>> extends ArrayedBinaryTree280<I> implements Dispenser280<I> {

	/**
	 * Create a new heap.
	 * @param cap The maximum capacity of the heap.
	 */
	@SuppressWarnings("unchecked")
	public ArrayedHeap280(int cap) {
		super(cap);
		items = (I[]) new Comparable[capacity+1];  // Override the constructor's implementation of items
	}
	
	/**
	 * Insert a new item into the heap.
	 * 
	 * @param item The item to be inserted.
	 * @throws ContainerFull280Exception if the heap is full.
	 */
	public void insert(I item) throws ContainerFull280Exception {
		// Insert normally
		if( this.isFull() ) throw new ContainerFull280Exception("Cannot add item to a lib280.tree that is full.");
		else {
			count ++;
			items[count] = item;
		}
		this.currentNode = 1;
		
		if( count == 1 ) return;
		
		// Then propagate the new item up the lib280.tree.
		int n = count;
				
		// As long as the items[n] is bigger than its parent...
		while(n > 1 && items[n].compareTo(items[findParent(n)]) > 0)  {
			// find the offset of the parent
			int p = findParent(n);
			
			// Swap items[n] with its parent
			I temp = items[p];
			items[p] = items[n];
			items[n] = temp;
			
			// Move to the parent and check again.
			n = p;
		}
	}
	
	/**
	 * Removes the item at the top of the heap.
	 * 
	 * @throws ContainerEmpty280Exception if the heap is empty.
	 * 
	 */
	
	public void deleteItem() throws ContainerEmpty280Exception, NoCurrentItem280Exception {
		if(this.isEmpty())
			throw new ContainerEmpty280Exception("Cannot delete an item from an empty heap.");

		// Delete the root by moving in the last item.
		// If there is more than one item, and we aren't deleting the last item,
		// copy the last item in the array to the current position.
		
		if( this.count > 1 ) {
			this.currentNode = 1;
			this.items[currentNode] = this.items[count];
		}		
		this.count--;
		
		// If we deleted the last remaining item, make the the current item invalid, and we're done.
		if( this.count == 0) { 
			this.currentNode = 0;
			return;
		}
		
		// Propagate the new root down the lib280.tree.
		int n = 1;
		
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
		
		ArrayedHeap280<Integer> H = new ArrayedHeap280<Integer>(10);
		
		// Empty heap should have the heap property.
		if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");

		// Insert items 1 through 10, checking after each insertion that 
		// the heap property is retained, and that the top of the heap is correctly i.
		for(int i = 1; i <= 10; i++) {
			H.insert(i);
			if(H.item() != i) System.out.println("Expected current item to be " + i + ", got " + H.item());
			if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");
		}
		
		// Remove the elements 10 through 1 from the heap, chekcing
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
