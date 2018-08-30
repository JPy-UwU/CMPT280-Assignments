package lib280.tree;

import lib280.base.CursorPosition280;
import lib280.base.Keyed280;
import lib280.base.Pair280;
import lib280.dictionary.KeyedDict280;
import lib280.exception.*;


public class IterableTwoThreeTree280<K extends Comparable<? super K>, I extends Keyed280<K>> extends TwoThreeTree280<K, I> implements KeyedDict280<K,I> {

	// References to the leaf nodes with the smallest and largest keys.
	LinkedLeafTwoThreeNode280<K,I> smallest, largest;

	// These next two variables represent the cursor which
	// the methods inherited from KeyedLinearIterator280 will
	// manipulate.  The cursor may only be positioned at leaf
	// nodes, never at internal nodes.

	// Reference to the leaf node at which the cursor is positioned.
	LinkedLeafTwoThreeNode280<K,I> cursor;

	// Reference to the predecessor of the node referred to by 'cursor' 
	// (or null if no such node exists).
	LinkedLeafTwoThreeNode280<K,I> prev;


	protected LinkedLeafTwoThreeNode280<K,I> createNewLeafNode(I newItem) {
		return new LinkedLeafTwoThreeNode280<K,I>(newItem);
	}


	@Override
	public void insert(I newItem) {

		if( this.has(newItem.key()) )
			throw new DuplicateItems280Exception("Key already exists in the tree.");

		// If the tree is empty, just make a leaf node. 
		if( this.isEmpty() ) {
			this.rootNode = createNewLeafNode(newItem);
			// Set the smallest and largest nodes to be the one leaf node in the tree.
			this.smallest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
			this.largest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
		}
		// If the tree has one node, make an internal node, and make it the parent
		// of both the existing leaf node and the new leaf node.
		else if( !this.rootNode.isInternal() ) {
			LinkedLeafTwoThreeNode280<K,I> newLeaf = createNewLeafNode(newItem);
			LinkedLeafTwoThreeNode280<K,I> oldRoot = (LinkedLeafTwoThreeNode280<K,I>)rootNode;
			InternalTwoThreeNode280<K,I> newRoot;
			if( newItem.key().compareTo(oldRoot.getKey1()) < 0) {
				// New item's key is smaller than the existing item's key...
				newRoot = createNewInternalNode(newLeaf, oldRoot.getKey1(), oldRoot, null, null);
				newLeaf.setNext(oldRoot);
				oldRoot.setPrev(newLeaf);

				// There was one leaf node, now there's two.  Update smallest and largest nodes.
				this.smallest = newLeaf;
				this.largest = oldRoot;
			}
			else {
				// New item's key is larger than the existing item's key. 
				newRoot = createNewInternalNode(oldRoot, newItem.key(), newLeaf, null, null);
				oldRoot.setNext(newLeaf);
				newLeaf.setPrev(oldRoot);

				// There was one leaf node, now there's two.  Update smallest and largest nodes.
				this.smallest = oldRoot;
				this.largest = newLeaf;
			}
			this.rootNode = newRoot;
		}
		else {
			Pair280<TwoThreeNode280<K,I>, K> extra = this.insert((InternalTwoThreeNode280<K,I>)this.rootNode, newItem);

			// If extra returns non-null, then the root was split and we need
			// to make a new root.
			if( extra != null ) {
				InternalTwoThreeNode280<K,I> oldRoot = (InternalTwoThreeNode280<K,I>)rootNode;

				// extra always contains larger keys than its sibling.
				this.rootNode = createNewInternalNode(oldRoot, extra.secondItem(), extra.firstItem(), null, null);
			}
		}
	}


	/**
	 * Recursive helper for the public insert() method.
	 * @param root Root of the (sub)tree into which we are inserting.
	 * @param newItem The item to be inserted.
	 */
	@SuppressWarnings("all")
	protected Pair280<TwoThreeNode280<K,I>, K> insert(TwoThreeNode280<K,I> root,
													  I newItem) {

		if( !root.isInternal() ) {
			// If root is a leaf node, then it's time to create a new
			// leaf node for our new element and return it so it gets linked
			// into root's parent.
			Pair280<TwoThreeNode280<K,I>, K> extraNode;

			LinkedLeafTwoThreeNode280<K,I> oldLeaf = (LinkedLeafTwoThreeNode280<K, I>) root;

			// If the new element is smaller than root, copy root's element to
			// a new leaf node, put new element in existing leaf node, and
			// return new leaf node.
			if( newItem.key().compareTo(root.getKey1()) < 0) {
				extraNode = new Pair280<TwoThreeNode280<K,I>, K>(createNewLeafNode(root.getData()), root.getKey1());
				((LeafTwoThreeNode280<K,I>)root).setData(newItem);
			}
			else {
				// Otherwise, just put the new element in a new leaf node
				// and return it.
				extraNode = new Pair280<TwoThreeNode280<K,I>, K>(createNewLeafNode(newItem), newItem.key());
			}
			LinkedLeafTwoThreeNode280<K,I> newLeaf= (LinkedLeafTwoThreeNode280<K, I>) extraNode.firstItem();
			// No matter what happens above, the node 'newLeaf' is a new leaf node that is
			// immediately to the right of the node 'oldLeaf'.

			// get the next node of the old leaf node
			LinkedLeafTwoThreeNode280<K,I> oldNext = oldLeaf.next();	// Get the next node after oldLeaf, if any
			oldLeaf.setNext(newLeaf);	// Set the old leaf's next node to be the new leaf
			newLeaf.setPrev(oldLeaf);	// Set the new leaf's previous node to be the old leaf

			if (oldNext != null) {	// if the next node of the past old leaf is not null
				newLeaf.setNext(oldNext);	// set new Leaf's next node to be the oldNext
				oldNext.setPrev(newLeaf);	// set the old next's previous node to be the  new leaf
			}
			// If the new item inserted's key is bigger than the current largest value
			if (newLeaf.data.key().compareTo(this.largest.data.key()) > 0) {
				this.largest = newLeaf;  // Set the largest node to be the new largest node
			}
			return extraNode;
		}
		else { // Otherwise, recurse! 
			Pair280<TwoThreeNode280<K,I>, K> extra;
			TwoThreeNode280<K,I> insertSubtree;

			if( newItem.key().compareTo(root.getKey1()) < 0 ) {
				// decide to recurse left
				insertSubtree = root.getLeftSubtree();
			}
			else if(!root.isRightChild() || newItem.key().compareTo(root.getKey2()) < 0 ) {
				// decide to recurse middle
				insertSubtree = root.getMiddleSubtree();
			}
			else {
				// decide to recurse right
				insertSubtree = root.getRightSubtree();
			}

			// Actually recurse where we decided to go.
			extra = insert(insertSubtree, newItem);

			// If recursion resulted in a new node needs to be linked in as a child
			// of root ...
			if( extra != null ) {
				// Otherwise, extra.firstItem() is an internal node... 
				if( !root.isRightChild() ) {
					// if root has only two children.  
					if( insertSubtree == root.getLeftSubtree() ) {
						// if we inserted in the left subtree...
						root.setRightSubtree(root.getMiddleSubtree());
						root.setMiddleSubtree(extra.firstItem());
						root.setKey2(root.getKey1());
						root.setKey1(extra.secondItem());
						return null;
					}
					else {
						// if we inserted in the right subtree...
						root.setRightSubtree(extra.firstItem());
						root.setKey2(extra.secondItem());
						return null;
					}
				}
				else {
					// otherwise root has three children
					TwoThreeNode280<K, I> extraNode;
					if( insertSubtree == root.getLeftSubtree()) {
						// if we inserted in the left subtree
						extraNode = createNewInternalNode(root.getMiddleSubtree(), root.getKey2(), root.getRightSubtree(), null, null);
						root.setMiddleSubtree(extra.firstItem());
						root.setRightSubtree(null);
						K k1 = root.getKey1();
						root.setKey1(extra.secondItem());
						return new Pair280<TwoThreeNode280<K,I>, K>(extraNode, k1);
					}
					else if( insertSubtree == root.getMiddleSubtree()) {
						// if we inserted in the middle subtree
						extraNode = createNewInternalNode(extra.firstItem(), root.getKey2(), root.getRightSubtree(), null, null);
						root.setKey2(null);
						root.setRightSubtree(null);
						return new Pair280<TwoThreeNode280<K,I>, K>(extraNode, extra.secondItem());
					}
					else {
						// we inserted in the right subtree
						extraNode = createNewInternalNode(root.getRightSubtree(), extra.secondItem(), extra.firstItem(), null, null);
						K k2 = root.getKey2();
						root.setKey2(null);
						root.setRightSubtree(null);
						return new Pair280<TwoThreeNode280<K,I>, K>(extraNode, k2);
					}
				}
			}
			// Otherwise no new node was returned, so there is nothing extra to link in.
			else return null;
		}
	}


	@Override
	public void delete(K keyToDelete) {
		if( this.isEmpty() ) return;

		if( !this.rootNode.isInternal()) {
			if( this.rootNode.getKey1() == keyToDelete ) {
				this.rootNode = null;
				this.smallest = null;
				this.largest = null;
			}
		}
		else {
			delete(this.rootNode, keyToDelete);
			// If the root only has one child, replace the root with its
			// child.
			if( this.rootNode.getMiddleSubtree() == null) {
				this.rootNode = this.rootNode.getLeftSubtree();
				if( !this.rootNode.isInternal() ) {
					this.smallest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
					this.largest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
				}
			}
		}
	}


	/**
	 * Given a key, delete the corresponding key-item pair from the tree.
	 * @param root root of the current tree
	 * @param keyToDelete The key to be deleted, if it exists.
	 */
	@SuppressWarnings("all")
	protected void delete(TwoThreeNode280<K, I> root, K keyToDelete ) {
		if( root.getLeftSubtree().isInternal() ) {
			// root is internal, so recurse.
			TwoThreeNode280<K,I> deletionSubtree;
			if( keyToDelete.compareTo(root.getKey1()) < 0){
				// recurse left
				deletionSubtree = root.getLeftSubtree();
			}
			else if( root.getRightSubtree() == null || keyToDelete.compareTo(root.getKey2()) < 0 ){
				// recurse middle
				deletionSubtree = root.getMiddleSubtree();
			}
			else {
				// recurse right
				deletionSubtree = root.getRightSubtree();
			}

			delete(deletionSubtree, keyToDelete);

			// Do the first possible of:
			// steal left, steal right, merge left, merge right
			if( deletionSubtree.getMiddleSubtree() == null)
				if(!stealLeft(root, deletionSubtree))
					if(!stealRight(root, deletionSubtree))
						if(!giveLeft(root, deletionSubtree))
							if(!giveRight(root, deletionSubtree))
								throw new InvalidState280Exception("This should never happen!");

		}
		else {
			// children of root are leaf nodes
			if( root.getLeftSubtree().getKey1().compareTo(keyToDelete) == 0 ) {
				// leaf to delete is on left
				// Get the left subtree of the node
				LinkedLeafTwoThreeNode280<K,I> left = (LinkedLeafTwoThreeNode280<K,I>) root.getLeftSubtree();

				// If the prev() node of the left node is null
				if (left.prev() == null) {
					if (this.cursor.getData().key().compareTo(keyToDelete) == 0) {
						this.goBefore();	// If the cursor is pointed at the minimum item and it is deleted, go to the before position.
					}
					this.smallest = left.next();  // set the smallest node to be the left node's next node
					left.next().setPrev(null);	// unlink the past smallest node
				}
				else {	// Otherwise, if the item to be deleted is in between 2 nodes
					if (this.cursor.getData().key().compareTo(keyToDelete) == 0) {
						this.cursor = left.prev();	// move the cursor to the next item after the min item
					}
					left.prev().setNext(left.next());	// set left.prev() node's next to be left's next node
					left.next().setPrev(left.prev()); // Set left.next's previous ndoe to be left's previous node
				}

				// Proceed with deletion of leaf from the 2-3 tree.
				root.setLeftSubtree(root.getMiddleSubtree());
				root.setMiddleSubtree(root.getRightSubtree());
				if(root.getMiddleSubtree() == null)
					root.setKey1(null);
				else
					root.setKey1(root.getKey2());
				if( root.getRightSubtree() != null) root.setKey2(null);
				root.setRightSubtree(null);
			}
			else if( root.getMiddleSubtree().getKey1().compareTo(keyToDelete) == 0 ) {
				// leaf to delete is in middle
				// get the middle node to be deleted
				LinkedLeafTwoThreeNode280<K,I> middle = (LinkedLeafTwoThreeNode280<K,I>) root.getMiddleSubtree();
				if (this.cursor.getData().key().compareTo(keyToDelete) == 0) {
					this.cursor = middle.prev();	// move the cursor to the item before the max item
				}
				if (middle.next() == null) { // If the middle node to be deleted is the largest item
					this.largest = middle.prev();	// Set the largest node to be the deleted node's previous item
					middle.prev().setNext(null);	// Set the middle's prev() node's next node to null
				}
				else {	// Otherwise, if this item to be deleted is in between 2 nodes
					middle.prev().setNext(middle.next());	// set the middle's previous node's next node to be middle's next node
					middle.next().setPrev(middle.prev());	// Set middle's next node's previous node to be middle's prev() node
				}

				// Proceed with deletion from the 2-3 tree.
				root.setMiddleSubtree(root.getRightSubtree());
				if(root.getMiddleSubtree() == null)
					root.setKey1(null);
				else
					root.setKey1(root.getKey2());

				if( root.getRightSubtree() != null) {
					root.setKey2(null);
					root.setRightSubtree(null);
				}
			}
			else if( root.getRightSubtree() != null && root.getRightSubtree().getKey1().compareTo(keyToDelete) == 0 ) {
				// leaf to delete is on the right
				// get the right subtree containing the item to be deleted
				LinkedLeafTwoThreeNode280<K,I> right = (LinkedLeafTwoThreeNode280<K,I>) root.getRightSubtree();
				if (this.cursor.getData().key().compareTo(keyToDelete) == 0) {
					this.cursor = right.prev();	// move the cursor to the item before the max item
				}
				// If the item to the right of the item to be deleted is null, we are deleting the largest item in our tree
				if (right.next() == null) {
					this.largest = right.prev();	// Set the largest to be the item before the deleted item
					right.prev().setNext(null);
					// If the cursor is pointed at the item to be deleted, and is the max item.
				}
				// Otherwise, if item to be deleted is in between 2 nodes
				else {
					right.prev().setNext(right.next());  // Set the previous node of the item to be deleted to be its right node
					right.next().setPrev(right.prev());  // Set the item to be deleted next node's previous node to be its previous node
				}

				// Proceed with deletion of the node from the 2-3 tree.
				root.setKey2(null);
				root.setRightSubtree(null);
			}
			else {
				// key to delete does not exist in tree.
			}
		}
	}


	@Override
	public K itemKey() throws NoCurrentItem280Exception {
		if (this.before() || this.after() || !this.itemExists()) {
			throw new NoCurrentItem280Exception("There is no current item from which to obtain its key.");
		}
		return this.cursor.getData().key();
	}


	@Override
	public Pair280<K, I> keyItemPair() throws NoCurrentItem280Exception {
		// Return a pair consisting of the key of the item
		// at which the cursor is positioned, and the entire
		// item in the node at which the cursor is positioned.
		if( !itemExists() )
			throw new NoCurrentItem280Exception("There is no current item from which to obtain its key.");
		return new Pair280<K, I>(this.itemKey(), this.item());
	}


	@Override
	public I item() throws NoCurrentItem280Exception {
		if (this.before() || this.after() || !this.itemExists()) {
			throw new NoCurrentItem280Exception("Cursor is currently not pointed to an item!");
		}
		return this.cursor.getData();
	}


	@Override
	public boolean itemExists() {
		return this.cursor != null;
	}


	@Override
	public boolean before() {
		return this.cursor == null && this.prev == null;
	}


	@Override
	public boolean after() {
		return (this.cursor == null && this.prev != null) || isEmpty();
	}


	@Override
	public void goForth() throws AfterTheEnd280Exception {
		if( this.after() ) throw new AfterTheEnd280Exception("Cannot advance the cursor past the end.");
		if( this.before() ) this.goFirst();
		else {
			this.prev = this.cursor;
			this.cursor = this.cursor.next();
		}
	}


	@Override
	public void goFirst() throws ContainerEmpty280Exception {
		this.prev = null;
		this.cursor = this.smallest;
	}


	@Override
	public void goBefore() {
		this.prev = null;
		this.cursor = null;
	}


	@Override
	public void goAfter() {
		this.prev = this.largest;
		this.cursor = null;
	}


	@Override
	public CursorPosition280 currentPosition() {
		return new TwoThreeTreePosition280<K,I>(this.cursor, this.prev);
	}


	@SuppressWarnings("unchecked")
	@Override
	public void goPosition(CursorPosition280 c) {
		if(c instanceof TwoThreeTreePosition280 ) {
			this.cursor = ((TwoThreeTreePosition280<K,I>) c).cursor;
			this.prev = ((TwoThreeTreePosition280<K,I>) c).prev;
		}
		else {
			throw new InvalidArgument280Exception("The provided position was not a TwoThreeTreePosition280 object.");
		}
	}


	public void search(K k) {
		// use super method find() to find the position of the item containing the item k
		LinkedLeafTwoThreeNode280<K, I> found =(LinkedLeafTwoThreeNode280<K, I>) find(this.rootNode, k);
		if (found == null) {	// If the found is null, it means that the key we're looking for doesn't exist
			this.goAfter();	// Set the cursor to be set to the after position
		}
		else {
			this.cursor = found;	// Otherwise, set the cursor to the leaf node containing key k
		}
	}


	@Override
	public void searchCeilingOf(K k) {
		// Position the cursor at the smallest item that
		// has key at least as large as 'k', if such an
		// item exists.  If no such item exists, leave 
		// the cursor in the after position.

		// This one is easier to do with a linear search.
		// Could make it potentially faster but the solution is
		// not obvious -- just use linear search via the cursor.

		// If it's empty, do nothing; itemExists() will be false.
		if( this.isEmpty() )
			return;

		// Find first item item >= k.  If there is no such item,
		// cursor will end up in after position, and that's fine
		// since itemExists() will be false.
		this.goFirst();
		while(this.itemExists() && this.itemKey().compareTo(k) < 0) {
			this.goForth();
		}

	}

	@Override
	public void setItem(I x) throws NoCurrentItem280Exception,
			InvalidArgument280Exception {
		if (this.before() || this.after() || !this.itemExists()) {	// If the item is in the before or after position, or the item doesnt exist
			throw new NoCurrentItem280Exception("Cursor is currently not pointing at an item!");	// Throw an exception
		}
		else if (this.cursor.data.key() != x.key()) {	// If the current key the cursor is pointing at is not equal to the key of the item to be replaced
			throw new InvalidArgument280Exception("Item x does not have the same key as the current item in current cursor position");	// Throw an exception
		}
		this.cursor.data = x;	// Set the data of the item to be the item x if the key are the same
	}


	@Override
	public void deleteItem() throws NoCurrentItem280Exception {
		if (this.before() || this.after() || !this.itemExists()) {	// If the item is in the before or after position, or item doesn't exist
			throw new NoCurrentItem280Exception("Cursor is currently not pointed at an item!");	// Throw an exception
		}
		LinkedLeafTwoThreeNode280<K,I> successor = this.cursor.next();	// Set cursor to be the next item after the deleted item
		this.delete(this.cursor.getKey1());	// Delete the current item the cursor is pointing at
		this.cursor = successor;	// Set the cursor to be the next item after the deleted item
	}

	@Override
	public String toStringByLevel() {
		String s = super.toStringByLevel();

		s += "\nThe Linear Ordering is: ";
		CursorPosition280 savedPos = this.currentPosition();
		this.goFirst();
		while(this.itemExists()) {
			s += this.itemKey() + ", ";
			this.goForth();
		}
		this.goPosition(savedPos);

		if( smallest != null)
			s += "\nSmallest: " + this.smallest.getKey1();
		if( largest != null ) {
			s += "\nLargest: " + this.largest.getKey1();
		}
		return s;
	}

	public static void main(String args[]) {

		// A class for an item that is compatible with our
		// 2-3 Tree class.  It has to implement Keyed280
		// as required by the class header of the 2-3 tree.
		// Keyed280 just requires that the item have a method
		// called key() that returns its key.

		// You must test your tree using Loot objects.

		class Loot implements Keyed280<String> {
			protected int goldValue;
			protected String key;

			@Override
			public String key() {
				return key;
			}

			@SuppressWarnings("unused")
			public int itemValue() {
				return this.goldValue;
			}

			Loot(String key, int i) {
				this.goldValue = i;
				this.key = key;
			}

		}
		// Create a tree to test with. 
		IterableTwoThreeTree280<String, Loot> T =
				new IterableTwoThreeTree280<String, Loot>();

		// An example of instantiating an item. (you can remove this if you wish)
		Loot sampleItem = new Loot("Keyblade", 1);
		Loot sampleItem1 = new Loot("Buster Sword", 5999);
		Loot sampleItem2 = new Loot("Flame Baton", 40);
		Loot sampleItem3 = new Loot("Tidal Blade", 1000);
		Loot sampleItem4 = new Loot("Elixir", 10);
		Loot sampleItem5 = new Loot("Alucard's Sword", 12000);
		Loot sampleItem6 = new Loot("Master Sword", 709);
		Loot sampleItem7 = new Loot("Goron Sword", 2000);
		Loot sampleItem8 = new Loot("Phoenix Down", 900);
		Loot sampleItem9 = new Loot("Chocobo Whistle", 42);
		Loot sampleItem10 = new Loot("Moogle's Charm", 4);

		T.insert(sampleItem);	// Insert the first item

		T.goForth();	// move cursor to the next item
		if (T.item().key().compareTo(sampleItem.key()) != 0) {
			System.out.println("Current cursor position should now be positioned at KeyBlade");
		}

		System.out.println("------------------------------------------------------------------------------------------");
		System.out.println(T.toStringByLevel());	// Print the current state of the tree
		System.out.println("------------------------------------------------------------------------------------------");

		T.deleteItem();	// Delete the item the cursor is currently pointing at
		try {
			if (T.item().key().compareTo(sampleItem.key()) == 0) {
				System.out.println("Keyblade should now be deleted and the item should now be null since tree is empty!");
			}
		} catch (NoCurrentItem280Exception c) {}

		System.out.println("------------------------------------------------------------------------------------------");
		System.out.println(T.toStringByLevel());
		System.out.println("------------------------------------------------------------------------------------------");

		// Insert a series of items into our Iterable two three tree, including reinsertion of Keyblade
		T.insert(sampleItem);
		T.insert(sampleItem1);
		T.insert(sampleItem2);
		T.insert(sampleItem3);
		T.insert(sampleItem4);
		T.insert(sampleItem5);
		T.insert(sampleItem6);
		T.insert(sampleItem7);
		T.insert(sampleItem8);
		T.insert(sampleItem9);
		T.insert(sampleItem10);

		System.out.println("------------------------------------------------------------------------------------------");
		System.out.println(T.toStringByLevel());
		System.out.println("------------------------------------------------------------------------------------------");

		T.search("Potato gun");
		if (!T.after()) {
			System.out.println("Cursor should be in after position since key Potato gun doesn't exist!");
		}

		T.search("Elixir");
		if (T.item().key().compareTo("Elixir") != 0) {
			System.out.println("Cursor should be pointing at item containing 'Elixir' as key!");
		}
		T.goForth();
		if (T.item().key().compareTo("Flame Baton") != 0 ) {
			System.out.println("The cursor should now be pointed at item containing 'Flame Baton' as key!");
		}
		T.deleteItem();
		if (T.itemKey().compareTo("Goron Sword") != 0) {
			System.out.println("Previous item the cursor is pointing at, Flame Baton, should now be deleted and the " +
					"cursor should now be on the next item after it.");
		}
		T.goForth();
		if (T.itemKey().compareTo("Keyblade") != 0) {
			System.out.println("Item after Goron Sword should be the Keyblade!");
		}
		if (T.keyItemPair().secondItem() != sampleItem) {
			System.out.println("Cursor should be pointed at key item pair containing keyblade!");
		}

		System.out.println("------------------------------------------------------------------------------------------");
		System.out.println(T.toStringByLevel());
		System.out.println("------------------------------------------------------------------------------------------");

		T.goForth();
		T.goForth();
		T.goForth();
		T.goForth();
		T.deleteItem();	// Deleting item that is positioned at the last item of the leaf
		if (T.prev.getData().key().compareTo("Phoenix Down") != 0) {
			System.out.println("The last item should now be Phoenix Down!");
		}
		if (T.largest.getData().key().compareTo("Phoenix Down") != 0) {
			System.out.println("The largest item should now be Phoenix Down!");
		}

		T.goFirst();
		T.deleteItem();
		if (T.item().key().compareTo("Buster Sword") != 0) {
			System.out.println("Alucard's sword is now deleted, and the first item should now be Buster Sword!");
		}
		if (T.smallest.getData().key().compareTo("Buster Sword") != 0) {
			System.out.println("New smallest item should now be Buster Sword!");
		}
		T.deleteItem();
		if (T.item().key().compareTo("Chocobo Whistle") != 0) {
			System.out.println("The first item should now be Chocobo Whistle!");
		}
		if (T.smallest.getData().key().compareTo("Chocobo Whistle") != 0) {
			System.out.println("New smallest item should now be Chocobo Whistle!");
		}

		System.out.println("------------------------------------------------------------------------------------------");
		System.out.println(T.toStringByLevel());
		System.out.println("------------------------------------------------------------------------------------------");

		T.search("Phoenix Down");
		T.deleteItem();
		T.search("Phoenix Down");
		if (!T.after()) {
			System.out.println("Phoenix Down is deleted and doesn't exist anymore. Therefore, cursor should be on after position");
		}

		T.searchCeilingOf("Elixir");
		if (T.itemKey().compareTo("Elixir") != 0) {
			System.out.println("Cursor should be pointed at elixir!");
		}
		T.goFirst();
		System.out.println("** Displaying Linked Leaf Nodes in ascending order **");
		while (T.itemExists()) {
			System.out.println("Key: " + T.itemKey() + " | Gold Value: " + T.item().goldValue);
			T.goForth();
		}
		System.out.println("------------------------------------------------------------------------------------------");
		System.out.println(T.toStringByLevel());
		System.out.println("------------------------------------------------------------------------------------------");
		T.goFirst();
		Loot setItem = new Loot("Chocobo Whistle", 40);
		T.setItem(setItem);
		if (T.item().goldValue != 40) {
			System.out.println("The new data item for Chocobo Whistle should now be 40");
		}
		try {
			T.setItem(new Loot("Potato gun", 9));
		} catch (InvalidArgument280Exception s) {
		}

		System.out.println("------------------------------------------------------------------------------------------");
		System.out.println(T.toStringByLevel());
		System.out.println("------------------------------------------------------------------------------------------");
		T.goFirst();
		System.out.println("Current key of the item cursor is pointing at is: " + T.itemKey());
		T.delete("Chocobo Whistle");
		if (T.cursor != null && !T.before()) {
			System.out.println("Tree cursor should now be pointed at the before position");
		}
		System.out.println("------------------------------------------------------------------------------------------");
		System.out.println(T.toStringByLevel());
		System.out.println("------------------------------------------------------------------------------------------");
		T.search("Keyblade");
		System.out.println("Current key of the item cursor is pointing at is: " + T.itemKey());
		T.delete("Keyblade");
		if (T.itemKey().compareTo("Goron Sword") != 0) {
			System.out.println("Cursor should now be pointed at Goron Sword!");
		}
		System.out.println("------------------------------------------------------------------------------------------");
		System.out.println(T.toStringByLevel());
		System.out.println("------------------------------------------------------------------------------------------");
		T.search("Moogle's Charm");
		System.out.println("Current key of the item cursor is pointing at is: " + T.itemKey());
		T.delete("Moogle's Charm");
		if (T.itemKey().compareTo("Master Sword") != 0) {
			System.out.println("Cursor should now be pointed at itemKey Master Sword");
		}
		T.goFirst();
		while (T.itemExists()) {
			T.deleteItem();
		}
		if (!T.isEmpty()) {
			System.out.println("Tree should now be empty!");
		}
		if (T.itemExists()) {
			System.out.println("Cursor should not be pointing at anything due to tree being empty!");
		}
		System.out.println("------------------------------------------------------------------------------------------");
		System.out.println(T.toStringByLevel());
		System.out.println("------------------------------------------------------------------------------------------");


		System.out.println("Regression Test Complete!");
	}
}
