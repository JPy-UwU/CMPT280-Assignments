// Mark Cantuba
// MJC862
// 11214496

package lib280.tree;

import lib280.base.Cursor280;
import lib280.base.CursorPosition280;
import lib280.dictionary.Dict280;
import lib280.exception.*;

public class ArrayedBinaryTreeWithCursors280<I> extends
		ArrayedBinaryTree280<I> implements Dict280<I>, Cursor280<I> {

	protected boolean searchesRestart;

	public ArrayedBinaryTreeWithCursors280(int cap) {
		super(cap);
		searchesRestart = true;
	}

	@Override
	public I obtain(I y) throws ItemNotFound280Exception {
		CursorPosition280 saved = this.currentPosition();
		this.goFirst();
		while(this.itemExists()) {
			if( membershipEquals(this.item(), y)) {
				I found = this.item();
				this.goPosition(saved);
				return found;
			}
			this.goForth();
		}
		this.goPosition(saved);
		throw new ItemNotFound280Exception("The given item could not be found.");
	}

	@Override
	public void insert(I x) throws ContainerFull280Exception, DuplicateItems280Exception {
		// If the tree reached its maxed capacity, throw a container full exception
		if (this.capacity == this.count) {
			throw new ContainerFull280Exception("Container is full and cannot insert anymore items");
		}
		// If the item user wishes to insert already exist, throw Duplicate exception
//		if (this.has(x)) {
//			throw new DuplicateItems280Exception("Item you wish to insert already exist!");
//		}
		this.count += 1;		// Increase counter by 1 first, because we need to avoid using index 0 in array tree
		this.items[count] = x;	// Insert the item on the left most side of the tree.
	}

	@Override
	public void deleteItem() throws NoCurrentItem280Exception {
		// if the cursor is pointing to a null value, throw a no current item exception
		if (!this.itemExists()) {
			throw new NoCurrentItem280Exception("Cursor is not pointing to an item!");
		}
		// if the current cursor is pointed at the end oof the list
		if (currentNode == count) {
			items[currentNode] = null;	// set the last item in the node to null
			currentNode -= 1;			// move the cursor one step back
		}
		else {
			this.items[currentNode] = this.items[count];	// set the current node to be the last value in the tree
		}
		count -= 1;					// decrement tree counter by 1
	}

	@Override
	public void delete(I x) throws ItemNotFound280Exception {
		// If the tree doesn't contain item x, then throw an item not found exception
		if (!this.has(x)) {
			throw new ItemNotFound280Exception("Item doesn't exist!");
		}
		this.goFirst();				// set the cursor position to point at the 1st item of the tree
		while (this.item() != x) {  // while the cursor is not pointing at the value user wishes to delete
			this.goForth();			// keep going forward
		}
		this.deleteItem();			// once the cursor is pointing at the value, delete the item
	}


	@Override
	public boolean has(I y) {
		// if the tree is empty, item y doesn't exist
		if (this.isEmpty()) {
			return false;
		}
		// Iterates through the tree
		this.goFirst();
		while (this.itemExists()) {
			// If item is found, terminate the loop, return true.
			if (this.item() == y) {
				return true;
			}
			this.goForth();		// otherwise, continue going forward
		}
		return false;
	}

	@Override
	public boolean membershipEquals(I x, I y) {
	    return x.equals(y);
	}

	@Override
	public void search(I x) {
		// If searchesRestart is true, start at the before position
		if (searchesRestart) {
			currentNode = 1;
		}
		this.goForth();	//	goForth before traversing through the tree to avoid checking the value
						//  the cursor is pointing at, since we want to move the cursor to the next occurrence
						// of the current value
		while (this.item() != x && this.itemExists()) {
			this.goForth();
		}
	}


	@Override
	public boolean before() {
		return currentNode == 0; // is the cursor on the before position? (Before position of an array tree)
	}

	@Override
	public boolean after() {
		return currentNode == count + 1; // is the cursor on the after position?
	}

	@Override
	public void goForth() throws AfterTheEnd280Exception {
		// The cursor should not be in teh after position
		if (after()) {
			throw new AfterTheEnd280Exception("Cursor is already pointing at after position!");
		}
		currentNode += 1;	// increment current node by 1


	}

	@Override
	public void goFirst() throws ContainerEmpty280Exception {
		// List cannot be empty!
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Container is empty!");
		}
		this.currentNode = 1;	// sets the current cursor position to the first item in the node
	}

	@Override
	public void goBefore() {
		this.currentNode = 0;	// sets the cursor position to point the before position (at index 0 because of array tree rules)

	}

	@Override
	public void goAfter() {
		this.currentNode = count + 1;	// sets the cursor to point at the after position

	}

	@Override
	public void restartSearches() {
		this.searchesRestart = true;
	}

	@Override
	public void resumeSearches() {
		this.searchesRestart = false;
	}

    @Override
	public CursorPosition280 currentPosition() {
		return new ArrayedBinaryTreePosition280(this.currentNode);
	}

	@Override
	public void goPosition(CursorPosition280 c) {
		if (!(c instanceof ArrayedBinaryTreePosition280))
			throw new InvalidArgument280Exception("The cursor position parameter"
					    + " must be a ArrayedBinaryTreePosition280<I>");

		this.currentNode = ((ArrayedBinaryTreePosition280)c).currentNode;
	}

	/**
	 * Move the cursor to the parent of the current node.
	 * @precond Current node is not the root.
	 * @throws InvalidState280Exception when the cursor is on the root already.
	 */
	public void parent() throws InvalidState280Exception {
		// Current cursor position can't be at the root
		if (this.currentNode == 1) {
			throw new InvalidState280Exception("Cursor is already pointing at the root!");
		}
		this.currentNode = findParent(currentNode);	// set the current position to be the parent of the current node

	}

	/**
	 * Move the cursor to the left child of the current node.
	 *
	 * @precond The tree must not be empty and the current node must have a left child.
	 * @throws ContainerEmpty280Exception if the tree is empty.
	 * @throws InvalidState280Exception if the current node has no left child.
	 */
	public void goLeftChild()  throws InvalidState280Exception, ContainerEmpty280Exception {
		// The list can't be empty!
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Container is empty!");
		}
		// The currentNode * 2 cannot be greater than capacity nor count
		if (findLeftChild(currentNode) > capacity || findLeftChild(currentNode) > count) {
			throw new InvalidState280Exception("Current node has no left child!");
		}
		this.currentNode = findLeftChild(currentNode);	// set current node to be 2 * itself
	}

	/**
	 * Move the cursor to the right child of the current node.
	 *
	 * @precond The tree must not be empty and the current node must have a right child.
	 * @throws ContainerEmpty280Exception if the tree is empty.
	 * @throws InvalidState280Exception if the current item has no right child.
	 */
	public void goRightChild() throws InvalidState280Exception, ContainerEmpty280Exception {
		// List cannot be empty
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Container is empty!");
		}
		// currentNode * 2 + 1 cannot be greater than capacity or count
		if (findRightChild(currentNode) > capacity || findRightChild(currentNode) > count) {
			throw new InvalidState280Exception("Current node has no right child!");
		}
		this.currentNode = findRightChild(currentNode);	// set current node to be 2*itself + 1
	}

	/**
	 * Move the cursor to the sibling of the current node.
	 *
	 * @precond The current node must have a sibling.  The tree must not be empty.
	 * @throws ContainerEmpty280Exception if the tree is empty.
	 * @throws InvalidState280Exception if the current item has no sibling.
	 */
	public void goSibling() throws InvalidState280Exception, ContainerEmpty280Exception {
		// List cannot be empty
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Container is empty!");
		}
		// if the current node is pointing at root, it doesn't have a sibling
		if (currentNode == 1) {
			throw new InvalidState280Exception("Cursor is pointed at the root node!");
		}
		// If the current cursor is pointed at a left position
		if (currentNode % 2 == 0) {
			// If the right value is null, it doesn't have a child
			if (this.items[currentNode+1] == null) {
				throw new InvalidState280Exception("This node has no right sibling!");
			}
			this.currentNode += 1;
		}
		// if cursor is pointing at a right node
		else {
			// if the left child is null, it's a single child
			if (this.items[currentNode - 1] == null) {
				throw new InvalidState280Exception("This node doesn't have a left sibling!");
			}
			this.currentNode -= 1;
		}
	}

	/**
	 * Move the cursor to the root of the tree.
	 *
	 * @precond The tree must not be empty.
	 * @throws ContainerEmpty280Exception if the tree is empty.
	 */
	public void root() throws ContainerEmpty280Exception {
        // The list cannot be empty
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("The Cursor cannot point to anything if list is empty!");
		}
		// Set the cursor to point at the root node of the tree.
		this.currentNode = 1;
	}


	public static void main(String[] args) {
		ArrayedBinaryTreeWithCursors280<Integer> T = new ArrayedBinaryTreeWithCursors280<Integer>(10);

		// IsEmpty on empty tree.
		if(!T.isEmpty()) System.out.println("Test of isEmpty() on empty tree failed.");

		// Test root() on empty tree.
		Exception x = null;
		try {
			T.root();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to root of empty tree.  Got none.");
		}

		// test goFirst() on empty tree
		x = null;
		try {
			T.goFirst();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to first elelement of empty tree.  Got none.");
		}



		// Test goLeftChild() on empty tree.
		x = null;
		try {
			T.goLeftChild();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to left child in empty tree.  Got none.");
		}

		// Test goLeftChild() on empty tree.
		x = null;
		try {
			T.goRightChild();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to right child in empty tree.  Got none.");
		}


		// Check itemExists on empty tree
		if(T.itemExists() ) System.out.println("itemExists() returned true on an empty tree.");

		// Insert on empty tree.
		T.insert(1);

		// Check ItemExists on tree with one element.
		T.root();
		if(!T.itemExists() ) System.out.println("itemExists() returned false on a tree with one element with cursor at the root.");

		// isEmpty on tree with 1 element.
		if(T.isEmpty()) System.out.println("Test of isEmpty() on non-empty tree failed.");

		// Insert on tree with 1 element
		T.insert(2);

		// Insert some more elements
		for(int i=3; i <= 10; i++) T.insert(i);

		if(T.count() != 10  ) System.out.println("Expected tree count to be 10, got "+ T.count());


		// Test for isFull on a full tree.
		if(!T.isFull()) System.out.println("Test of isFull() on a full tree failed.");

		// Test insert on a full tree
		x = null;
		try {
			T.insert(11);
		}
		catch(ContainerFull280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception inserting into a full tree.  Got none.");
		}

		// Test positioning methods

		// Test root()
		T.root();
		if( T.item() != 1 ) System.out.println("Expected item at root to be 1, got " + T.item());

		T.goLeftChild();


		if( T.item() != 2 ) System.out.println("Expected current item to be 2, got " + T.item());

		T.goRightChild();
		if( T.item() != 5 ) System.out.println("Expected current item to be 5, got " + T.item());


		T.goLeftChild();
		if( T.item() != 10 ) System.out.println("Expected current item to be 10,  got " + T.item());

		// Current node now has no children.
		x = null;
		try {
			T.goLeftChild();
		}
		catch( InvalidState280Exception e ) {
			x = e;
		}
		finally {
			if( x == null) System.out.println("Expected exception moving to left child of a leaf.  Got none.");
		}

		x = null;
		try {
			T.goRightChild();
		}
		catch( InvalidState280Exception e ) {
			x = e;
		}
		finally {
			if( x == null) System.out.println("Expected exception moving to right child of a leaf.  Got none.");
		}

		// Remove the last item ( a leaf)
		T.deleteItem();
		if( T.item() != 9 ) System.out.println("Expected current item to be 9, got " + T.item());
		T.parent();

		// Remove a node with 2 children.  The right child 9 gets promoted.
		T.deleteItem();
		if( T.item() != 9 ) System.out.println("Expected current item to be 9, got " + T.item());

		// Remove a node with 1 child.  The left child 8 gets promoted.
		T.deleteItem();
		if( T.item() != 8 ) System.out.println("Expected current item to be 8, got " + T.item());

		// Remove the root successively.  There are 7 items left.
		T.root();
		T.deleteItem();
		if( T.item() != 7 ) System.out.println("Expected root to be 7, got " + T.item());

		T.deleteItem();
		if( T.item() != 6 ) System.out.println("Expected root to be 6, got " + T.item());

		T.deleteItem();
		if( T.item() != 5 ) System.out.println("Expected root to be 5, got " + T.item());

		T.deleteItem();
		if( T.item() != 8 ) System.out.println("Expected root to be 8, got " + T.item());

		T.deleteItem();
		if( T.item() != 3 ) System.out.println("Expected root to be 3, got " + T.item());

		T.deleteItem();
		if( T.item() != 2 ) System.out.println("Expected root to be 2, got " + T.item());

		// Tree has one item.  Try parent() on one item.
		x = null;
		try {
			T.parent();
		}
		catch( InvalidState280Exception e ) {
			x = e;
		}
		finally {
			if( x == null) System.out.println("Expected exception moving to parent of root.  Got none.");
		}


		// Try to go to the sibling
		x = null;
		try {
			T.goSibling();
		}
		catch(InvalidState280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to sibling when at the root.  Got none.");
		}



		T.deleteItem();


		// Tree should now be empty
		if(!T.isEmpty()) System.out.println("Expected empty tree.  isEmpty() returned false.");

		if(T.capacity() != 10) System.out.println("Expected capacity to be 10, got "+ T.capacity());

		if(T.count() != 0  ) System.out.println("Expected tree count to be 0, got "+ T.count());

		// Remove from empty tree.
		x = null;
		try {
			T.deleteItem();
		}
		catch(NoCurrentItem280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception deleting from an empty tree.  Got none.");
		}



		// Try to go to the sibling
		x = null;
		try {
			T.goSibling();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to sibling in empty tree tree.  Got none.");
		}


		T.insert(1);
		T.root();

		// Try to go to the sibling when there is no child.
		x = null;
		try {
			T.goSibling();
		}
		catch(InvalidState280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to sibling of node with no sibling.  Got none.");
		}

		T.goBefore();
		if(!T.before()) System.out.println("Error: Should be in 'before' position, but before() reports otherwise.");
		if(T.after()) System.out.println("Error: T.after() reports cursor in the after position when it should not be.");

		T.goForth();
		if(T.before()) System.out.println("Error: T.before() reports cursor in the before position when it should not be.");
		if(T.after()) System.out.println("Error: T.after() reports cursor in the after position when it should not be.");

		T.goForth();
		if(!T.after()) System.out.println("Error: Should be in 'after' position, but after() reports otherwise.");
		if(T.before()) System.out.println("Error: T.before() reports cursor in the before position when it should not be.");

		x=null;
		try {
			T.goForth();
		}
		catch(AfterTheEnd280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception advancing cursor when already after the end.  Got none.");
		}


		int y=-1;
		T.goBefore();
		try {
			y =  T.obtain(1);
		}
		catch( ItemNotFound280Exception e ) {
			System.out.println("Error: Unexpected exception occured when attempting T.obtain(1).");
		}
		finally {
			if(y != 1 ) System.out.println("Obtained item should be 1 but it isn't.");
			if(!T.before()) System.out.println("Error: cursor should still be in the before() position after T.obtain(1), but it isn't.");
		}

		if(!T.has(1)) System.out.println("Error: Tree has element 1, but T.has(1) reports that it does not.");


		System.out.println("Regression test complete.");

	}

}
