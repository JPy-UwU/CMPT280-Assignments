package lib280.dispenser;

import lib280.list.ArrayedList280;

public class ArrayedStack280<I> extends Stack280<I> {

	/**
	 * Obtain the maximum number of items that can be in the stack.
	 * @return Maximum capacity of the stack.
	 */
	public int capacity() {
		return ((ArrayedList280<I>)this.stackItems).capacity();
	}
	
	/**
	 * Number of items in the stack.
	 * @return number of items in the stack.
	 */
	public int count() {
		return ((ArrayedList280<I>)this.stackItems).count();		
	}
	
	@Override
	public ArrayedStack280<I> clone() throws CloneNotSupportedException {
		return (ArrayedStack280<I>)super.clone();
	}


	public ArrayedStack280(int capacity) {
		stackItems = new ArrayedList280<I>(capacity);
	}
	

}
