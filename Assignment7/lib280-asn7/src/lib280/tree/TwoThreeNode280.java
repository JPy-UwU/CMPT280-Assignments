package lib280.tree;

import lib280.base.Keyed280;

public abstract class TwoThreeNode280<K extends Comparable<? super K>, I extends Keyed280<K>> {
			
	public TwoThreeNode280() {}


	
	/**
	 * Determine if the node is internal.
	 * @return true if the node is internal, false if it is a leaf node.
	 */
	public abstract boolean isInternal();
	

	/**
	 * Is there a right subtree of this node?
	 * @return true if there is a right subtree, false otherwise.
	 */
	public abstract boolean isRightChild();
	
	/**
	 * Get the item stored in the node.
	 * @precond this.isInternal() must be false.
	 * @return the key element pair in the node.
	 */
	public abstract I getData();
	
	/**
	 * Get the first key of an internal node, or the key of the item stored in a leaf node.
	 * @return The first key of a node.
	 */
	public abstract K getKey1();
	
	/**
	 * Get the second key of an internal node.
	 * @precond this.isInternal() must be true.
	 * @return The second key of a node.
	 */
	public abstract K getKey2();
	
	/**
	 * Change the first key of an internal node.
	 * @param key1 The new value of the first key.
	 */
	public abstract void setKey1(K key1);
	
	/**
	 * Change the second key of an internal node.
	 * @param key2 The new value of the first key.
	 */
	public abstract void setKey2(K key2);
	
	/**
	 * Obtain the left subtree.
	 * @precond this.isInternal() must be true.
	 * @return Reference to the root of the left subtree.
	 */
	public abstract TwoThreeNode280<K,I> getLeftSubtree();
	
	
	/**
	 * Obtain the middle subtree.
	 * @precond this.isInternal() must be true.
	 * @return Reference to the root of the middle subtree.
	 */
	public abstract TwoThreeNode280<K,I> getMiddleSubtree();
	
	
	/**
	 * Obtain the right subtree.
	 * @precond this.isInternal() must be true.
	 * @return Reference to the root of the right subtree.
	 */
	public abstract TwoThreeNode280<K,I> getRightSubtree();
	
	
	/**
	 * Change the left subtree.
	 * @precond this.isInternal() must be true.
	 */
	public abstract void setLeftSubtree(TwoThreeNode280<K,I> leftSubtree);
	 
	/**
	 * Change the middle subtree.
	 * @precond this.isInternal() must be true.
	 */
	public abstract void setMiddleSubtree(TwoThreeNode280<K,I> middleSubtree);
	
	/**
	 * Change the right subtree.
	 * @precond this.isInternal() must be true.
	 */
	public abstract void setRightSubtree(TwoThreeNode280<K,I> rightSubtree);
	
	
}
