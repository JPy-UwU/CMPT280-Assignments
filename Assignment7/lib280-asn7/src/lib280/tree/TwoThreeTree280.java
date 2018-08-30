package lib280.tree;

import lib280.base.Container280;
import lib280.base.Keyed280;
import lib280.base.Pair280;
import lib280.dictionary.KeyedBasicDict280;
import lib280.exception.DuplicateItems280Exception;
import lib280.exception.InvalidState280Exception;
import lib280.exception.ItemNotFound280Exception;

import java.util.Random;


public class TwoThreeTree280<K extends Comparable<? super K>, I extends Keyed280<K>> implements KeyedBasicDict280<K,I>, Container280 {
	TwoThreeNode280<K,I> rootNode;
	
	/**
	 * Create an empty 2-3 lib280.tree.
	 */
	public TwoThreeTree280() {
		this.rootNode = null;
	}
	
	@Override
	public boolean isEmpty() {
		return rootNode == null;
	}
	
	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public void clear() {
		this.rootNode = null;
	}	
	
	protected LeafTwoThreeNode280<K,I> createNewLeafNode(I newItem) {
		return new LeafTwoThreeNode280<K,I>(newItem);
	}
	
	protected InternalTwoThreeNode280<K,I> createNewInternalNode(TwoThreeNode280<K,I> left, K key1, TwoThreeNode280<K,I> middle, K key2, TwoThreeNode280<K,I> right) {
		return new InternalTwoThreeNode280<K,I>(left, key1, middle, key2, right);
	}
	
	
	/**
	 * Determine the height of the lib280.tree.
	 * @timing O(log n), n is the number of elements in the lib280.tree.
	 * @return The height of the this 2-3 lib280.tree.
	 */
	public int height() {
		TwoThreeNode280<K,I> cur = this.rootNode;
		int height = 1;
		
		if(this.isEmpty()) return 0;
		
		while( cur.isInternal() ) {
			cur = cur.getLeftSubtree();
			height = height + 1;
		}
		return height;
		
	}
	

	/**
	 * Recursive helper for the public insert() method.
	 * @param root Root of the (sub)lib280.tree into which we are inserting.
	 * @param newItem The item to be inserted.
	 */
	protected Pair280<TwoThreeNode280<K,I>, K> insert(TwoThreeNode280<K,I> root,
                                                      I newItem) {
				
		if( !root.isInternal() ) {
			// If root is a leaf node, then it's time to create a new
			// leaf node for our new element and return it so it gets linked
			// into root's parent.
			Pair280<TwoThreeNode280<K,I>, K> newLeaf;
			
			// If the new element is smaller than root, copy root's element to
			// a new leaf node, put new element in existing leaf node, and
			// return new leaf node.
			if( newItem.key().compareTo(root.getKey1()) < 0) {
				newLeaf = new Pair280<TwoThreeNode280<K,I>, K>(createNewLeafNode(root.getData()), root.getKey1());
				((LeafTwoThreeNode280<K,I>)root).setData(newItem);
			}
			else {
				// Otherwise, just put the new element in a new leaf node
				// and return it.
				newLeaf = new Pair280<TwoThreeNode280<K,I>, K>(createNewLeafNode(newItem), newItem.key());
			}
			return newLeaf;
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
		

	
	/**
	 * Obtain an item given it's key.
	 * @param searchKey Key of the item being sought.
	 * @return The key-item pair with key 'searchKey' if such a key exists in the lib280.tree,
	 * 		   otherwise, null is returned.
	 */
	protected I find(K searchKey) throws ItemNotFound280Exception {
		TwoThreeNode280<K,I> itemNode = find(this.rootNode, searchKey);
		if( itemNode == null ) return null;
		else return itemNode.getData();	
	}
	
	/**
	 * Obtain the node containing an item given it's key.
	 * @param root Root of lib280.tree to search.
	 * @param searchKey Key of the item being sought.
	 * @return The key-item pair with key 'searchKey' if such a key exists in the lib280.tree,
	 * 		   otherwise, null is returned.
	 */
	protected LeafTwoThreeNode280<K,I> find(TwoThreeNode280<K,I> root, K searchKey) {
		if( root == null ) return null;
		
		// If we're at an internal node, check the keys to see which way to branch.
		if( root.isInternal() ) {
			// If searchKey is less than k1, go left.
			if( searchKey.compareTo(root.getKey1()) < 0 )
				return find(root.getLeftSubtree(), searchKey);
			// If there is no right subtree or searchKey is less than k2, go middle.
			else if( !root.isRightChild() || searchKey.compareTo(root.getKey2()) < 0 )
				return find(root.getMiddleSubtree(), searchKey);
			// Otherwise, go right.
			else return find(root.getRightSubtree(), searchKey);
		}
		// If we're at a leaf node, see if the key matches, and return the pair if it does.
		else {
			if( searchKey.compareTo(root.getKey1()) == 0) 
				return (LeafTwoThreeNode280<K, I>) root;
			else return null;
		}
	}
	
	@Override
	public boolean has(K searchKey) {
		return find(this.rootNode, searchKey) != null;
	}
	
	@Override
	public I obtain(K searchKey) throws ItemNotFound280Exception {
		I result = find(searchKey);
		if( result == null ) throw new ItemNotFound280Exception("The item with key " + searchKey + " was not found in the lib280.tree.");
		else return result;
	}
	
	

	
	@Override
	public void insert(I newItem) {
		
		if( this.has(newItem.key()) ) 
			throw new DuplicateItems280Exception("Key already exists in the lib280.tree.");
		
		// If the lib280.tree is empty, just make a leaf node.
		if( this.isEmpty() ) {
			this.rootNode = createNewLeafNode(newItem);
		}
		// If the lib280.tree has one node, make an internal node, and make it the parent
		// of both the existing leaf node and the new leaf node.
		else if( !this.rootNode.isInternal() ) {
			LeafTwoThreeNode280<K,I> newLeaf = createNewLeafNode(newItem);
			LeafTwoThreeNode280<K,I> oldRoot = (LeafTwoThreeNode280<K,I>)rootNode;
			InternalTwoThreeNode280<K,I> newRoot;
			if( newItem.key().compareTo(oldRoot.getKey1()) < 0) {
				// New item's key is smaller than the existing item's key...
				newRoot = createNewInternalNode(newLeaf, oldRoot.getKey1(), oldRoot, null, null);				
			}
			else {
				// New item's key is larger than the existing item's key. 
				newRoot = createNewInternalNode(oldRoot, newItem.key(), newLeaf, null, null);
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
	 * Helper method for printing of the lib280.tree.
	 * @param i Current level of the lib280.tree.
	 * @param root Root of the current subtree.
	 * @return A string representation of the lib280.tree.
	 */
	protected String toStringByLevel(TwoThreeNode280<K,I> root, int i)
	{
		// This is the toStringByLevel() method we saw in the lecture notes.
		
		StringBuffer blanks = new StringBuffer((i - 1) * 5);
		for (int j = 0; j < i - 1; j++)
			blanks.append("     ");
	  
		String result = new String();
		
		if(!root.isInternal())
			return result += "\n" + blanks + i + ":" + root.getKey1();
		
		if (root.isRightChild()) {
			result += toStringByLevel(root.getRightSubtree(), i+1);
			result += "\n" + blanks + i + ":K2:" + root.getKey2() ;
		}
		
		result += toStringByLevel(root.getMiddleSubtree(), i+1);
	
		result += "\n" + blanks + i + ":K1:" + root.getKey1() ;

		result += toStringByLevel(root.getLeftSubtree(), i+1);
		
		return result;
	}

	/**
	 * 	String representation of the lib280.tree, level by level.
	 */
	public String toString()
	{
		return toStringByLevel();
	}
	
	/** 
	 * String representation of the lib280.tree, level by level.
	 * @return string representation of the lib280.tree, level by level.
	 */
	public String toStringByLevel() {
		if(this.isEmpty()) return "Empty Tree";
		else return toStringByLevel(this.rootNode, 1); 
	}
	
	
	@Override
	public void delete(K keyToDelete) {
		if( this.isEmpty() ) return;
		
		if( !this.rootNode.isInternal()) {
			if( this.rootNode.getKey1() == keyToDelete )
				this.rootNode = null;
			}
		else {
			delete(this.rootNode, keyToDelete);	
			// If the root only has one child, replace the root with its
			// child.
			if( this.rootNode.getMiddleSubtree() == null) {
				this.rootNode = this.rootNode.getLeftSubtree();
			}
		}
	}
	
	
	protected boolean stealRight(TwoThreeNode280<K,I> root,
			                     TwoThreeNode280<K,I> singleton) {
		TwoThreeNode280<K,I> singletonRightSibling;
		if( singleton == root.getLeftSubtree() ) {
			singletonRightSibling = root.getMiddleSubtree();
			if( singletonRightSibling.getRightSubtree() == null) return false;
						
			singleton.setKey1(root.getKey1());
			root.setKey1(singletonRightSibling.getKey1());	
		}
		else if(singleton == root.getMiddleSubtree() ) {
			singletonRightSibling = root.getRightSubtree();
			if( singletonRightSibling == null) return false;
			if( singletonRightSibling.getRightSubtree() == null) return false;
			
			singleton.setKey1(root.getKey2());
			root.setKey2(singletonRightSibling.getKey1());
		}
		else return false;
		
		singleton.setMiddleSubtree(singletonRightSibling.getLeftSubtree());
		singletonRightSibling.setLeftSubtree(singletonRightSibling.getMiddleSubtree());
		singletonRightSibling.setMiddleSubtree(singletonRightSibling.getRightSubtree());
		singletonRightSibling.setKey1(singletonRightSibling.getKey2());
		singletonRightSibling.setKey2(null);
		singletonRightSibling.setRightSubtree(null);
		return true;			
	}
	
	protected boolean stealLeft(TwoThreeNode280<K,I> root,
			                     TwoThreeNode280<K,I> singleton) {
		TwoThreeNode280<K,I> singletonLeftSibling;
		if( singleton == root.getMiddleSubtree()) {
			singletonLeftSibling = root.getLeftSubtree();
			if(singletonLeftSibling.getRightSubtree() == null) return false;
			
			singleton.setKey1(root.getKey1());
			root.setKey1(singletonLeftSibling.getKey2());
		}
		else if (singleton == root.getRightSubtree()) {
			singletonLeftSibling = root.getMiddleSubtree();
			if(singletonLeftSibling.getRightSubtree() == null) return false;
			
			singleton.setKey1(root.getKey2());
			root.setKey2(singletonLeftSibling.getKey2());
			
		}
		else {
			return false;
		}
		
		singleton.setMiddleSubtree(singleton.getLeftSubtree());
		singleton.setLeftSubtree(singletonLeftSibling.getRightSubtree());
		singletonLeftSibling.setKey2(null);	
		singletonLeftSibling.setRightSubtree(null);
		return true;
	}
	
	protected boolean giveRight(TwoThreeNode280<K,I> root,
            TwoThreeNode280<K,I> singleton) {
		
		TwoThreeNode280<K,I> singletonRightSibling;
		
		if( singleton == root.getLeftSubtree() ) {
			singletonRightSibling = root.getMiddleSubtree();
			
			singletonRightSibling.setRightSubtree(singletonRightSibling.getMiddleSubtree());
			singletonRightSibling.setMiddleSubtree(singletonRightSibling.getLeftSubtree());
			singletonRightSibling.setLeftSubtree(singleton.getLeftSubtree());
			singletonRightSibling.setKey2(singletonRightSibling.getKey1());
			singletonRightSibling.setKey1(root.getKey1());
			if( root.getRightSubtree() != null ) {
				root.setKey1(root.getKey2());
				root.setKey2(null);
			}
			else root.setKey1(null);
			root.setLeftSubtree(singletonRightSibling);
			root.setMiddleSubtree(root.getRightSubtree());
			root.setRightSubtree(null);
		}
		else if( singleton == root.getMiddleSubtree()) {
			singletonRightSibling = root.getRightSubtree();
			if( singletonRightSibling == null) return false;
			
			singletonRightSibling.setRightSubtree(singletonRightSibling.getMiddleSubtree());
			singletonRightSibling.setMiddleSubtree(singletonRightSibling.getLeftSubtree());
			singletonRightSibling.setLeftSubtree(singleton.getLeftSubtree());
			singletonRightSibling.setKey2(singletonRightSibling.getKey1());
			singletonRightSibling.setKey1(root.getKey2());
			root.setKey2(null);
			root.setMiddleSubtree(singletonRightSibling);
			root.setRightSubtree(null);
		}
		else {
			return false;
		}
		return true;
	}
	
	protected boolean giveLeft(TwoThreeNode280<K,I> root,
            TwoThreeNode280<K,I> singleton) {
		
		TwoThreeNode280<K,I> singletonLeftSibling;

		if(singleton == root.getMiddleSubtree()) {
			singletonLeftSibling = root.getLeftSubtree();
			singletonLeftSibling.setRightSubtree(singleton.getLeftSubtree());
			singletonLeftSibling.setKey2(root.getKey1());
			if(root.getRightSubtree() != null) { 
				root.setKey1(root.getKey2());
				root.setKey2(null);
			}
			else root.setKey1(null);
			root.setMiddleSubtree(root.getRightSubtree());
			root.setRightSubtree(null);
		}
		else if(singleton == root.getRightSubtree()) {
			singletonLeftSibling = root.getMiddleSubtree();
			singletonLeftSibling.setRightSubtree(singleton.getLeftSubtree());
			singletonLeftSibling.setKey2(root.getKey2());
			root.setKey2(null);
			root.setRightSubtree(null);
		}
		else return false;
		
		return true;
	}
	
	
	
	/**
	 * Given a key, delete the corresponding key-item pair from the lib280.tree.
	 * @param root root of the current lib280.tree
	 * @param keyToDelete The key to be deleted, if it exists.
	 */
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
				root.setKey2(null);
				root.setRightSubtree(null);
			}
			else {
				// key to delete does not exist in lib280.tree.
			}
		}		
	}	
	
	public static void main(String args[]) {
		
		// Once again, an hackish unit test because I just didn't have time.  Sorry again.
		// It does appear to work though.
		
		class myItem implements Keyed280<Integer> {
			int num;
			
			@Override
			public Integer key() {
				return num;
			}
			
			myItem(int i) {
				num = i;
			}
			
		}
		
		
		TwoThreeTree280<Integer, myItem> T = new TwoThreeTree280<Integer, myItem>();
		int numEl = 15;
		int maxVal = 1000;
		int maxReps = 1;
		int randomVal[] = new int[numEl];
		for(int reps = 0; reps < maxReps; reps++) {
		Random RNG = new Random((long) (Math.random()*1000000));
		for(int i=0; i < numEl; i++) {
			randomVal[i] = (int) Math.rint(RNG.nextDouble()*maxVal);
			System.out.println("Inserting: " + randomVal[i]);
			try {
			T.insert(new myItem(randomVal[i]));
			}
			catch(DuplicateItems280Exception e) {
				
			}
			System.out.println(T);
			// Occasionally alternate some fixed insertions and deletions.
			if( i == 5) {
				try {
				T.insert(new myItem(234));
				T.insert(new myItem(549));
				T.insert(new myItem(1000));
				T.insert(new myItem(0));
				}catch(DuplicateItems280Exception e) {
					
				}
				T.delete(234);
				T.delete(549);
				T.delete(1000);
				T.delete(0);
			}
		}
		
		System.out.println(T);
		
		for(int i=0; i <  numEl; i++) {
			// delete each element of randomVal and check the result...
			System.out.println("\nDeleting " + randomVal[i] );
			T.delete(randomVal[i]);
			System.out.println(T);
		}
		
	
		}
	}

	@Override
	public void set(I x) throws ItemNotFound280Exception {
		LeafTwoThreeNode280<K,I> theNode = find(this.rootNode, x.key());
		if( theNode == null ) throw new ItemNotFound280Exception("There is no item with key " + x.key() + " to replace.");
		else theNode.setData(x);
	}


		
}
