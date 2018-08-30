package lib280.tree;

import lib280.base.Keyed280;

public class LinkedLeafTwoThreeNode280<K extends Comparable<? super K>, I extends Keyed280<K>> extends LeafTwoThreeNode280<K, I> {

	// References to the successor and predecessor leaf nodes respectively.
	LinkedLeafTwoThreeNode280<K,I> next;
	LinkedLeafTwoThreeNode280<K,I> prev;
	
	/**
	 * @return the next
	 */
	public LinkedLeafTwoThreeNode280<K, I> next() {
		return next;
	}

	/**
	 * @param next the next to set
	 */
	public void setNext(LinkedLeafTwoThreeNode280<K, I> next) {
		this.next = next;
	}

	/**
	 * @return the prev
	 */
	public LinkedLeafTwoThreeNode280<K, I> prev() {
		return prev;
	}

	/**
	 * @param prev the prev to set
	 */
	public void setPrev(LinkedLeafTwoThreeNode280<K, I> prev) {
		this.prev = prev;
	}

	public LinkedLeafTwoThreeNode280(I data) {
		super(data);
	}

}
