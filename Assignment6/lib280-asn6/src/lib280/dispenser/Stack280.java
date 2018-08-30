package lib280.dispenser;

import lib280.base.Dispenser280;
import lib280.exception.ContainerFull280Exception;
import lib280.exception.NoCurrentItem280Exception;
import lib280.list.SimpleList280;

public abstract class Stack280<I> implements Dispenser280<I> {

	protected SimpleList280<I> stackItems;
	

	@SuppressWarnings("unchecked")
	@Override
	public Stack280<I> clone() throws CloneNotSupportedException {
		return (Stack280<I>) super.clone();
	}

	@Override
	public void deleteItem() throws NoCurrentItem280Exception {
		stackItems.deleteFirst();
	}

	@Override
	public void insert(I x) throws ContainerFull280Exception {
		stackItems.insertFirst(x);
	}

	@Override
	public I item() throws NoCurrentItem280Exception {
		
		stackItems.goFirst();

		return stackItems.firstItem();
	}

	@Override
	public boolean itemExists() {
		return !this.isEmpty();
	}

	@Override
	public void clear() {
		stackItems.clear();
	}

	@Override
	public boolean isEmpty() {
		return stackItems.isEmpty();
	}

	@Override
	public boolean isFull() {
		return stackItems.isFull();
	}


	@Override
	public String toString() {
		return "Stack starting with top item: " + stackItems;
	}


}
