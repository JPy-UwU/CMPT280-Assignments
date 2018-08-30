package lib280.tree;

import lib280.base.LinearIterator280;
import lib280.exception.AfterTheEnd280Exception;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.NoCurrentItem280Exception;

public class ArrayedBinaryTreeIterator280<I> extends ArrayedBinaryTreePosition280 implements LinearIterator280<I> {

	// This is a reference to the tree that created this iterator object.
	ArrayedBinaryTree280<I> tree;

	// An integer that represents the cursor position is inherited from
	// ArrayedBinaryTreePosition280.

	/**
	 * Create a new iterator from a given heap.
	 * @param t The heap for which to create a new iterator.
	 */
	public ArrayedBinaryTreeIterator280(ArrayedBinaryTree280<I> t) {
		super(t.currentNode);
		this.tree = t;
	}

	/**
	 * Check if iterator pointer is in the before position
	 * @return true if pointer is in the before position
	 */
	@Override
	public boolean before() {
		return this.currentNode == 0;	// Check if the current node is = 0. if it is, it is in the before position
	}

	/**
	 * Check if the current node is in the after position
	 * @return true if the current node is in the after position
	 */
	@Override
	public boolean after() {
		return this.currentNode > tree.count || tree.isEmpty();
	}

	/**
	 * Move our iterator to the next position in our tree
	 * @throws AfterTheEnd280Exception If the tree is the after position, we cannot move any further
	 */
	@Override
	public void goForth() throws AfterTheEnd280Exception {
		if (this.after()) {	// If the item is already in the after position
			throw new AfterTheEnd280Exception("Cursor is already in the after position");	// Throw an exception
		}
		this.currentNode++; // Move forward
	}

	/**
	 * Move the iterator to the first item in the heap
	 * @throws ContainerEmpty280Exception Tree cannot be empty
	 */
	@Override
	public void goFirst() throws ContainerEmpty280Exception {
		if (tree.isEmpty()) {	// If tree is empty, throw an exception
			throw new ContainerEmpty280Exception("Container is empty. Cannot move cursor to a position in an empty tree.");
		}
		this.currentNode = 1;	// place iterator to the first item in the heap
	}

	/**
	 * Place the iterator to the before position
	 */
	@Override
	public void goBefore() {
		this.currentNode = 0;
	}

	/**
	 * Place the cursor in the after position
	 */
	@Override
	public void goAfter() {
		if (tree.isEmpty()) {	// if the tree is empty, after position = 0
			this.currentNode = 0;
		}
		else {	// Otherwise, place iterator to point at position count+1
			this.currentNode = tree.count + 1;
		}

	}

	/**
	 * Get the item contained in the current iterator position
	 * @return Item stored at iterator's currentNode position
	 * @throws NoCurrentItem280Exception iterator has to be pointing at something within array range
	 */
	@Override
	public I item() throws NoCurrentItem280Exception {
		if (!itemExists()) {	// If iterator is not pointing at an item, throw an exception
			throw new NoCurrentItem280Exception("Cursor is currently not pointing to a position!");
		}
		return tree.items[this.currentNode];	// else, return the item at current iterator position
	}

	/**
	 * Check if the iterator is pointing at a position
	 * @return true if there is an item at iterator.current position
	 */
	@Override
	public boolean itemExists() {
		// If the tree is not empty and the current node is not at before or after
		return tree.count > 0 && !this.before() && !this.after();
	}


}
