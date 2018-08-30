package lib280.tree;

import lib280.base.LinearIterator280;
import lib280.exception.AfterTheEnd280Exception;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.NoCurrentItem280Exception;

public class ArrayedBinaryTreeIterator280<I> extends ArrayedBinaryTreePosition280 implements LinearIterator280<I> {

	ArrayedBinaryTree280<I> tree;
	
	public ArrayedBinaryTreeIterator280(ArrayedBinaryTree280<I> t) {
		super(t.currentNode);
		this.tree = t;
	}

	@Override
	public boolean before() {
		return this.currentNode == 0;
	}

	@Override
	public boolean after() {
		return this.currentNode > tree.count || tree.isEmpty();
	}

	@Override
	public void goForth() throws AfterTheEnd280Exception {
		if(this.after()) throw new AfterTheEnd280Exception("Cannot advance cursor in the after position.");
		this.currentNode++;		
	}

	@Override
	public void goFirst() throws ContainerEmpty280Exception {
		if( tree.isEmpty() ) throw new ContainerEmpty280Exception("Cannot move to first item of an empty lib280.tree.");
		this.currentNode = 1;
	}

	@Override
	public void goBefore() {
		this.currentNode = 0;		
	}

	@Override
	public void goAfter() {
		if( tree.isEmpty() ) this.currentNode = 0;
		else this.currentNode = tree.count + 1;	}

	@Override
	public I item() throws NoCurrentItem280Exception {
		if( !this.itemExists() )  throw new NoCurrentItem280Exception("There is no current item.");
		return tree.items[this.currentNode];
	}

	@Override
	public boolean itemExists() {
		return tree.count > 0 && (this.currentNode > 0 && this.currentNode <= tree.count);
	}
}
