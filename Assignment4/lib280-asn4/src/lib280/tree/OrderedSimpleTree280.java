package lib280.tree;

import lib280.base.Dispenser280;
import lib280.base.Searchable280;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.NoCurrentItem280Exception;

public class OrderedSimpleTree280<I extends Comparable<? super I>> extends LinkedSimpleTree280<I>
			implements Dispenser280<I>, Searchable280<I>
{
	/**	The current node as set by search. */
	protected BinaryNode280<I> cur;

	/**	The parent node of the current node as set by search. */
	protected BinaryNode280<I> parent;

	/**	Do searches continue?. */
	protected boolean searchesContinue = false;

	/**	Are equality comparisons done using object reference comparisons?. */
	protected boolean objectReferenceComparison = false;

	/**	Create an empty lib280.tree. <br>
		Analysis: Time = O(1) */
	public OrderedSimpleTree280()
	{
		super();
	}

	/**	Create a lib280.tree with lt, r, and rt being the left subtree, root item, and right subtree
		respectively (lt and/or rt can be null for an empty subtree). <br>
		Analysis: Time = O(1) <br>
		PRECONDITION: <br>
		<ul>
			All items in lt are less than or equal to r <br>
			All items in rt are greater than or equal to r <br>
			(These conditions are not checked because of their time complexity.) 
		</ul> 
		@param lt lib280.tree to initialize as the left subtree
		@param r item to initialize as the root
		@param rt lib280.tree to initialize as the right subtree */
	public OrderedSimpleTree280(OrderedSimpleTree280<I> lt, I r, OrderedSimpleTree280<I> rt)
	{
		super(lt, r, rt);
	}

	/**	Is there a current node?. <br>
		Analysis : Time = O(1) */
	public boolean itemExists()
	{
		return cur != null;
	}

	/**	Contents of the current node. <br>
		Analysis : Time = O(1)  <br>
		PRECONDITION: <br>
		<ul>
			itemExists() 
		</ul> */
	public I item() throws NoCurrentItem280Exception
	{
		if (!itemExists())
			throw new NoCurrentItem280Exception("Cannot access item when it does not exist");

		return cur.item();
	}

	/**	Set contents of the root to x. <br>
		Analysis: Time = O(1) <br>
		PRECONDITION: <br>
		<ul>
			!isEmpty() <br>
			value of x is between those in left subtree and right subtree <br>
			(The second condition of the precondition isn't checked as it is too time consuming.
			Don't use this operation on this class unless the condition is known to hold.) 
		</ul> */
	public void setRootItem(I x) throws ContainerEmpty280Exception
	{
		if (isEmpty())
			throw new ContainerEmpty280Exception("Cannot set the root of an empty lib280.tree.");

		rootNode.setItem(x);
	}

	/**	Go to item x, if it is in the lib280.tree.  If searchesContinue, continue in the right subtree. <br>
		Analysis : Time = O(h) worst case, where h = height of the lib280.tree */
	public void search(I x)
	{
		boolean found = false;
		if (!searchesContinue || above())
		{
			parent = null;
			cur = rootNode;
		}
		else if (!below())
		{
			parent = cur;
			cur = cur.rightNode();
		}
		while (!found && itemExists())
		{
			if (x.compareTo(item()) < 0)
			{
				parent = cur;
				cur = parent.leftNode();
			}
			else if (x.compareTo(item()) > 0)
			{
				parent = cur;
				cur = parent.rightNode();
			}
			else
				found = true;
		}
	}

	/**	Does the lib280.tree contain x?. <br>
		Analysis : Time = O(h) worst case, where h = height of the lib280.tree */
    public boolean has(I x) {
        // save cursor state
        BinaryNode280<I> saveParent = parent;
        BinaryNode280<I> saveCur = cur;
        boolean saveSearchesContinue = this.searchesContinue;

        // Always start at the root
        this.parent = null;
        this.cur = this.rootNode;
        this.restartSearches();

        // Search
        this.search(x);
        boolean result = itemExists();

        // Restore cursor state
        this.parent = saveParent;
        this.cur = saveCur;
        this.searchesContinue = saveSearchesContinue;

        return result;
    }



    /**	Insert x into the lib280.tree. <br>
		Analysis : Time = O(h) worst case, where h = height of the lib280.tree */
	public void insert(I x)
	{
		if (isEmpty())
			rootNode = createNewNode(x);
		else if (x.compareTo(rootItem()) < 0)
		{
			OrderedSimpleTree280<I> leftTree = rootLeftSubtree();
			leftTree.insert(x);
			setRootLeftSubtree(leftTree);
		}
		else
		{
			OrderedSimpleTree280<I> rightTree = rootRightSubtree();
			rightTree.insert(x);
			setRootRightSubtree(rightTree);
		}
	}

	/**	Delete all items from the data structure. <br>
		Analysis : Time = O(1) */
	public void clear()
	{
		super.clear();
		parent = null;
		cur = null;
	}

	/**	Delete the current item, making its replacement the current item. <br>
		Analysis : Time = O(h) worst case, where h = height of the structure <br>
		PRECONDITION: <br>
		<ul>
			itemExists() 
		</ul> */
	public void deleteItem() throws NoCurrentItem280Exception
	{
		if(!itemExists())
			throw new NoCurrentItem280Exception("No current item to delete");

		boolean foundReplacement = false;
		BinaryNode280<I> replaceNode = null;

		/*	Test if there is only one child so it can replace the root. */
		if (cur.rightNode() == null)
		{
			replaceNode = cur.leftNode();
			foundReplacement = true;
		}
		else if (cur.leftNode() == null)
		{
			replaceNode = cur.rightNode();
			foundReplacement = true;
		}
		else
			foundReplacement = false;

		if (foundReplacement)
		{
			/*	Set parent node to refer to the replacement node. */
			if (parent == null)
				setRootNode(replaceNode);
			else if (parent.leftNode() == cur)
				parent.setLeftNode(replaceNode);
			else
				parent.setRightNode(replaceNode);
			cur = replaceNode;
		}
		else
		{
			/*	Replace the current item by its inorder successor and
				then delete the inorder successor from its original place. */

			/*	Find the position (replaceParent and replaceCur) of the inorder successor. */
			BinaryNode280<I> replaceParent = cur;
			BinaryNode280<I> replaceCur = replaceParent.rightNode();
			while (replaceCur.leftNode() != null)
			{
				replaceParent = replaceCur;
				replaceCur = replaceParent.leftNode();
			}

			/*	Replace the current item (to be deleted) by the inorder successor. */
			cur.setItem(replaceCur.item());
			/*	Delete the inorder successor from its original place. */
			BinaryNode280<I> saveParent = parent;
			BinaryNode280<I> saveCur = cur;
			parent = replaceParent;
			cur = replaceCur;
			deleteItem();
			parent = saveParent;
			cur = saveCur;
		}
	}

	/**	String representation via inorder traversal. <br>
		Analysis : Time = O(n), where n = number of items */
	public String toString()
	{
		return this.toStringByLevel();
	}

	/**	Restart searches each time search is called. <br>
		Analysis: Time = O(1) */
	public void restartSearches()
	{
		searchesContinue = false;
	}

	/**	Resume searches after each call to search. <br>
		Analysis: Time = (1) */
	public void resumeSearches()
	{
		searchesContinue = true;
	}

	/**	Test whether x equals y using the current comparison mode. <br>
		Analysis: Time = O(1) */
	public boolean membershipEquals(I x, I y)
	{
		if (objectReferenceComparison)
			return x == y;
		else if ((x instanceof Comparable) && (y instanceof Comparable))
			return 0 == x.compareTo(y);
		else if (x.equals(y))
			return true;
		else 
			return false;
	}

	/**	Set comparison operations to use ==. <br>
		Analysis: Time = O(1) */
	public void compareObjectReferences()
	{
		objectReferenceComparison = true;
	}

	/**	Set comparison operations to use equal() or compareTo().  <br>
		Analysis: Time = O(1) */
	public void compareContents()
	{
		objectReferenceComparison = false;
	}

	/**	Set the left subtree to t (set isEmpty if t == null).  <br>
		Analysis: Time = O(1)  <br>
		PRECONDITION: <br>
		<ul>
			!isEmpty() <br>
			values in the new left subtree are less than rootItem() <br>

			(The second condition of the precondition isn't checked as it is too time consuming.
			Don't use this operation on this class unless the condition is known to hold.) 
		</ul> */
	public void setRootLeftSubtree(LinkedSimpleTree280<I> t) throws ContainerEmpty280Exception
	{
		if (isEmpty())
			throw new ContainerEmpty280Exception("Cannot set subtree of an empty lib280.tree.");

		if (t != null)
			rootNode.setLeftNode(t.rootNode());
		else
			rootNode.setLeftNode(null);
	}

	/**	Set the right subtree to t (set isEmpty if t == null).  <br>
		Analysis: Time = O(1)  <br>
		PRECONDITION: <br>
		<ul>
			!isEmpty() <br>
			values in the new right subtree are greater than rootItem() <br>

			(The second condition of the precondition isn't checked as it is too time consuming.
			Don't use this operation on this class unless the condition is known to hold.) 
		</ul> */
	public void setRootRightSubtree(LinkedSimpleTree280<I> t) throws ContainerEmpty280Exception
	{
		if (isEmpty())
			throw new ContainerEmpty280Exception("Cannot set subtree of an empty lib280.tree.");

		if (t != null)
			rootNode.setRightNode(t.rootNode());
		else
			rootNode.setRightNode(null);
	}

	/**	Is the current position below the bottom of the lib280.tree?. <br>
		Analysis: Time = O(1) */
	protected boolean below()
	{
		return (cur == null) && (parent != null || isEmpty());
	}

	/**	Is the current position above the root of the lib280.tree?. <br>
		Analysis: Time = O(1) */
	protected boolean above()
	{
		return (parent == null) && (cur == null);
	}

	/**	Left subtree of the root. <br>
		Analysis: Time = O(1) <br>
		PRECONDITION: <br>
		<ul>
			!isEmpty() 
		</ul> */
	public OrderedSimpleTree280<I> rootLeftSubtree() throws ContainerEmpty280Exception
	{
		return (OrderedSimpleTree280<I>) super.rootLeftSubtree();
	}

	/**	Right subtree of the root. <br>
		Analysis: Time = O(1) <br>
		PRECONDITION: <br>
		<ul>
			!isEmpty() 
		</ul> */
	public OrderedSimpleTree280<I> rootRightSubtree() throws ContainerEmpty280Exception
	{
		return (OrderedSimpleTree280<I>) super.rootRightSubtree();
	}

	/**	A shallow clone of this lib280.tree. <br>
		Analysis: Time = O(1) */
	public OrderedSimpleTree280<I> clone()
	{
		return (OrderedSimpleTree280<I>) super.clone();
	}

	protected String toStringInorder(BinaryNode280<I> N) {
		String result = "";
		if( N.leftNode() != null ) result += toStringInorder(N.leftNode());
		result += N.toString() + "\n";
		if( N.rightNode() != null ) result += toStringInorder(N.rightNode());
		return result;
	}
	
	public String toStringInorder() {
		if( !this.isEmpty() )
			return toStringInorder(this.rootNode());
		else {
			return "<Empty>";
		}
	}
	
	/**	
	 * Return the number of items that need to be examined to position the cursor on the item
	 * x (or determine that no such item exists). 
	 */
	public int searchCount(I x)
	{
		BinaryNode280<I> N = this.rootNode();
		
		boolean found = false;
		int count = 0;
		
		while (!found && N != null)
		{
			count ++;
			if (x.compareTo(N.item()) < 0)
			{
				N = N.leftNode();
			}
			else if (x.compareTo(N.item()) > 0)
			{
				N = N.rightNode();
			}
			else
				found = true;
		}
		
		return count;
	}
	public static void main(String args[]) {
		OrderedSimpleTree280<Integer> T = new OrderedSimpleTree280<Integer>();
		T.insert(50);
		T.insert(16);
		T.insert(67);
		T.insert(81);
		T.insert(22);
		T.insert(5);
		T.insert(17);
		T.insert(66);
		T.insert(42);
		
		System.out.println(T.toStringByLevel());
		
		T.search(50);
		if(T.itemExists()) {
			T.deleteItem();
			System.out.println("\n\nAfter deleting 50:\n" + T);
		}
		else System.out.println("\n\nThere was no element 50 in the lib280.tree to delete.");

		// Test to make sure that has() always starts at the root.
		T.search(42);
		if(!T.itemExists())
			System.out.println("\n\n Error: should have found 42 with search() but didn't.");

		if(T.item() != 42)
			System.out.println("\n\n Error: current item should be 42 but it isn't.");


		T.resumeSearches();
		if( !T.has(42) ) {

			System.out.println("\n\n Error: tree has 42, but has() says it doesn't.");
		}
		T.restartSearches();



		T.search(42);
		if(T.itemExists()) {
			T.deleteItem();
			System.out.println("\n\nAfter deleting 42:\n" + T);
		}
		else System.out.println("\n\nThere was no element 42 in the lib280.tree to delete.");


		T.search(16);
		if(T.itemExists()) {
			T.deleteItem();
			System.out.println("\n\nAfter deleting 16:\n" + T);
		}
		else System.out.println("\n\nThere was no element 16 in the lib280.tree to delete.");
		
		T.search(99);
		if(T.itemExists()) {
			T.deleteItem();
			System.out.println("\n\nAfter deleting 99:\n" + T);
		}
		else System.out.println("\n\nAs expected, there was no element 99 in the lib280.tree to delete.");

	}
	
}
