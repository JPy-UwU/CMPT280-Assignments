package lib280.tree;

import lib280.base.Container280;
import lib280.exception.NoCurrentItem280Exception;

/**
 * @author eramian
 *
 */
public abstract class ArrayedBinaryTree280<I> implements Container280 {

	protected int currentNode;		// Index of the node corresponding to the current cursor position.
	protected int capacity;			// Maximum number of elements in the lib280.tree.
	protected int count;			// Current number of elements in the lib280.tree.
	
	protected I items[];
	
	/**
	 * Constructor.
	 *
	 * @param cap Maximum number of elements that can be in the lib280.tree.
	 */
	@SuppressWarnings("unchecked")
	public ArrayedBinaryTree280(int cap) {
		capacity = cap;
		currentNode = 0;
		count = 0;
		items = (I[]) new Object[capacity+1];
	}
	
	/**
	 * Gets the index of the left child of the node at index 'node'.
	 */
	protected int findLeftChild(int node) {
		return node * 2;
	}
	
	/**
	 * Gets the index of the right child of the node at index 'node'.
	 */
	protected int findRightChild(int node) {
		return node * 2 + 1;
	}
	
	/**
	 * Gets the index of the parent of the node at index 'node'.
	 */
	protected int findParent(int node) {
		return node / 2;
	}

	/**
	 * Get the item at the cursor.
	 * 
	 * @precond The lib280.tree must not be empty.  The cursor position must be valid.
	 * @throws NoCurrentItem280Exception if the cursor is not positioned at an item.
	 * @return The item at the cursor.
	 */
	public I item() throws NoCurrentItem280Exception {
		
		if(!itemExists() ) throw new NoCurrentItem280Exception();
		else return items[currentNode];
	}
	
	/**
	 * Determines if an item exists.
	 * 
	 * @return true if there is an item at the cursor, false otherwise.
	 */
	public boolean itemExists(){
		return count > 0 && (currentNode > 0 && currentNode <= count);
	}
	
	
	/**
	 * Get the maximum capacity of the lib280.tree.
	 * 
	 * @return The maximum number of items the lib280.tree can store.
	 */
	public int capacity() {
		return capacity;
	}
	
	/** 
	 * Get the number of items in the lib280.tree.
	 * 
	 * @return The number of items in the lib280.tree.
	 * 
	 */
	public int count() {
		return count;
	}
	
	/** Empty the lib280.tree
	 * 
	 *  Remove all elements from the lib280.tree.
	 */
	public void clear() {
		count = 0;
		currentNode = 0;
	}
	
	/**
	 * Returns a shallow clone of the lib280.tree.  The new lib280.tree contains
	 * copies of item references, not new items.
	 * 
	 * @return A reference to the new copy.
	 */
	@SuppressWarnings("unchecked")
	public ArrayedBinaryTree280<I> clone() {
		ArrayedBinaryTree280<I> temp;
		try { 
			temp = (ArrayedBinaryTree280<I>) super.clone();
		}
		catch (CloneNotSupportedException e) {
			temp = null;
		}
		return temp;
	}
	
	
	/**
	 * Determine if the lib280.tree is empty.
	 * 
	 * @return true if the lib280.tree is empty.  false otherwise.
	 */
	public boolean isEmpty() {
		return count == 0 && currentNode == 0;
	}
	
	/**
	 * Determine if the lib280.tree is full.
	 * 
	 * @return true if the lib280.tree is full.  false otherwise.
	 */
	public boolean isFull() {
		return count == capacity;
	}
	

	public String toString() {
		String out = "";
		for(int i=1; i <= count; i++) {
			out += items[i] + ", ";
		}
		
		out += "\n" + "Cursor is on item with array index " + this.currentNode + " (item "+this.items[this.currentNode]+")";
		
		return out;
	}


}
