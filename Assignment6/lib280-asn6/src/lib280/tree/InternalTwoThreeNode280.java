package lib280.tree;

import lib280.base.Keyed280;
import lib280.exception.InvalidArgument280Exception;
import lib280.exception.InvalidState280Exception;
import lib280.exception.ItemNotFound280Exception;


public class InternalTwoThreeNode280<K extends Comparable<? super K>, I extends Keyed280<K>> extends TwoThreeNode280<K,I> {
	/**
	 * First key.
	 */
	K key1;
	
	/**
	 * Second key.
	 */
	K key2;
	
	/**
	 * First subtree.
	 */
	TwoThreeNode280<K,I> leftSubtree;
	
	/**
	 * Second subtree.
	 */
	TwoThreeNode280<K,I> middleSubtree;
	
	/**
	 * Third subtree.
	 */
	TwoThreeNode280<K,I> rightSubtree;

	
	/**
	 * Create a new internal node for a 2-3 lib280.tree.
	 * 
	 * @param key1 First key for this node.
	 * @param key2 Second key for this node.
	 * @param leftSubtree Left subtree for this node.
	 * @param middleSubtree Middle subtree for this node.
	 * @param rightSubtree Right subtree for this node.
	 * @precond leftSubtree must not be null
	 * @precond middleSubtree must not be null
	 */
	public InternalTwoThreeNode280(TwoThreeNode280<K,I> leftSubtree, K key1,
                                   TwoThreeNode280<K,I> middleSubtree, K key2, TwoThreeNode280<K,I> rightSubtree) {
		super();
		
		if( leftSubtree == null || middleSubtree == null )
			throw new InvalidArgument280Exception("A left and middle subtree must be specified.");
		
		this.key1 = key1;
		this.key2 = key2;
		this.leftSubtree = leftSubtree;
		this.middleSubtree = middleSubtree;
		this.rightSubtree = rightSubtree;
	}

	
	@Override
	public boolean isInternal() {
		return true;
	}


	/**
	 * Is there a right subtree of this node?
	 * @return true if there is a right subtree, false otherwise.
	 */
	public boolean isRightChild() {
		return this.rightSubtree != null;
	}
	
	
	/**
	 * Obtain the first key.
	 * @return The first key.
	 */
	public K getKey1() {
		return this.key1;
	}

	/**
	 * Change the first key.
	 * @param key1 The new value of the first key.
	 */
	public void setKey1(K key1) {
		this.key1 = key1;
	}

	/**
	 * Obtain the second key.
	 * @precond The right subtree must exist.
	 * @return The value of the second key.
	 */
	public K getKey2() {
		if( !isRightChild() ) throw new ItemNotFound280Exception("Cannot obtain the second key when there is no right subtree.");
		return this.key2;
	}

	/**
	 * Change the second key.
	 * @precond The right subtree must exist.
	 * @param key2 The new value of the second key.
	 */
	public void setKey2(K key2) {
		if( !isRightChild() ) throw new ItemNotFound280Exception("Cannot modify the second key when there is no right subtree.");

		this.key2 = key2;
	}

	/**
	 * Obtain the left subtree.
	 * @return Reference to the root node of the left subtree.
	 */
	public TwoThreeNode280<K,I> getLeftSubtree() {
		return this.leftSubtree;
	}

	/** Change the left subtree.
	 * 
	 * @param leftSubtree Root node of the new left subtree.
	 */
	public void setLeftSubtree(TwoThreeNode280<K,I> leftSubtree) {
		this.leftSubtree = leftSubtree;
	}

	/**
	 * Obtain the middle subtree.
	 * @return Reference to the root node of the middle subtree.
	 */
	public TwoThreeNode280<K,I> getMiddleSubtree() {
		return this.middleSubtree;
	}

	/**
	 * Change the middle subtree.
	 * @param middleSubtree Root node of the new middle subtree.
	 */
	public void setMiddleSubtree(TwoThreeNode280<K,I> middleSubtree) {
		this.middleSubtree = middleSubtree;
	}

	/**
	 * Obtain the right subtree.
	 * @return Reference to the root node of the right subtree.
	 */
	public TwoThreeNode280<K,I> getRightSubtree() {
		return this.rightSubtree;
	}

	/**
	 * Change the right subtree.
	 * @param rightSubtree Root node of the new right subtree.
	 */
	public void setRightSubtree(TwoThreeNode280<K,I> rightSubtree) {
		this.rightSubtree = rightSubtree;
	}


	@Override
	public I getData() {
		throw new InvalidState280Exception("This method cannot be called on an internal node.");
	}

	

	
	
}
