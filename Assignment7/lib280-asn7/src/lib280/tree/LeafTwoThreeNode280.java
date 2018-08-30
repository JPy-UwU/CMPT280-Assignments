package lib280.tree;

import lib280.base.Keyed280;
import lib280.exception.InvalidState280Exception;


public class LeafTwoThreeNode280<K extends Comparable<? super K>, I extends Keyed280<K>> extends TwoThreeNode280<K,I> {
	/**
	 * The data item stored in the leaf node.
	 */
	I data;

	
	/**
	 * Create a new leaf node containing a key-value pair for a 2-3 lib280.tree.
	 * @param data The item to store in this leaf node.
	 */
	public LeafTwoThreeNode280(I data) {
		this.data = data;
	}
	
	
	@Override
	public boolean isInternal() {
		return false;
	}

	/**
	 * Obtain the data in this node.
	 * @return The item stored in this node.
	 */
	public I getData() {
		return data;
	}

	/** 
	 * Change the data in this node.
	 * @param data The new data item for this node.
	 */
	public void setData(I data) {
		this.data = data;
	}


	@Override
	public K getKey1() {
		return data.key();
	}


	@Override
	public K getKey2() {
		throw new InvalidState280Exception("This method cannot be called on a leaf node.");
	}


	@Override
	public TwoThreeNode280<K,I> getLeftSubtree() {
		throw new InvalidState280Exception("This method cannot be called on a leaf node.");
	}

	@Override
	public TwoThreeNode280<K,I> getMiddleSubtree() {
		throw new InvalidState280Exception("This method cannot be called on a leaf node.");
}

	@Override
	public TwoThreeNode280<K,I> getRightSubtree() {
		throw new InvalidState280Exception("This method cannot be called on a leaf node.");
	}


	@Override
	public void setLeftSubtree(TwoThreeNode280<K,I> leftSubtree) {
		throw new InvalidState280Exception("This method cannot be called on a leaf node.");
		
	}

	@Override
	public void setMiddleSubtree(TwoThreeNode280<K,I> middleSubtree) {
		throw new InvalidState280Exception("This method cannot be called on a leaf node.");
		
	}

	@Override
	public void setRightSubtree(TwoThreeNode280<K,I> rightSubtree) {
		throw new InvalidState280Exception("This method cannot be called on a leaf node.");
	}


	@Override
	public boolean isRightChild() {
		return false;
	}


	@Override
	public void setKey1(K key1) {
		throw new InvalidState280Exception("This method cannot be called on a leaf node.");
	}


	@Override
	public void setKey2(K key2) {
		throw new InvalidState280Exception("This method cannot be called on a leaf node.");
	}
		
}
