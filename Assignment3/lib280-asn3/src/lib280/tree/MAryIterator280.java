/* MAryIterator280.java
 * ---------------------------------------------
 * Copyright (c) 2004 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */
 
package lib280.tree;


import lib280.base.Cursor280;
import lib280.exception.NoCurrentItem280Exception;

/**	An Iterator for an m-ary lib280.tree.  It includes functions to test for
	above and below, and to move to a subtree. */
public class MAryIterator280<I> implements Cursor280<I>
{
	/**	Internal representation of the structure. */
	protected BasicMAryTree280<I> tree;
  
	/**	The current node. */
	protected MAryNode280<I> cur;

	/**	The parent node. */
	protected MAryNode280<I> parent;
  
	/**	The index of cur in parent node. */
	protected int index;

	/**	Access to the index of cur in parent node. */
	public int index()
	{
		return index;
	}
  
	/**	
	 * Construct a new cursor with corresponding values. 
	 * @timing O(1) 
	 * @param newTree lib280.tree to be iterated
	 * @param newParent parent of the node to create the iterator at
	 * @param newIndex position to set index to
	 * @param newCur current node to create the iterator at 
	 */
	public MAryIterator280(BasicMAryTree280<I> newTree, MAryNode280<I> newParent,
                           int newIndex, MAryNode280<I> newCur)
	{
		tree = newTree;
		parent = newParent;
		cur = newCur;
		index = newIndex;
	}

	/**	
	 * Is there a current item?. 
	 * @timing O(1) 
	 */
	public boolean itemExists()
	{
		return !above() && !below();
	}

	/**	
	 * Obtain the contents of the current node. 
	 * @timing O(1) 
	 * @precond itemExists() 
	 */
	public I item() throws NoCurrentItem280Exception
	{
		if (!itemExists())
			throw new NoCurrentItem280Exception("A current item must exist");  
		
		return cur.item();
	}

	/**	
	 * Is the current position above the root of the lib280.tree?.
	 * @timing O(1) 
	 */
	public boolean above()
	{
		return (cur==null) && (parent==null);
	}

	/**	
	 * Is the current position below a leaf of the lib280.tree?.
	 * @timing O(1) 
	 */
	public boolean below()
	{
		return cur==null && (parent!=null || tree.isEmpty());
	}

	/**	
	 * Move to above the root item.
	 * @timing O(1) 
	 */
	public void goAbove()
	{
		parent = null;
		cur = null;
	}

	/**	
	 * Go to root item of the data strucuture (if it exists).
	 * @timing O(1) 
	 */
	public void goRoot()
	{
		cur = tree.rootNode;
		parent = null;
	}

	/**	
	 * Advance to the root of the ith subtree. 
	 * @timing O(1)
	 * @param i subtree to advance to 
	 */
	public void goSubtree(int i)
	{
		parent = cur;
		index = i;
		cur = cur.subnode(i);
	}

	/**	
	 * Return string representation of the current item. 
	 * @timing O(1) 
	 */
	public String toString()
	{
		return tree.toString();
	}

	/**
	 * A shallow clone of this object. 
	 * @timing O(1) 
	 */
	@SuppressWarnings("unchecked")
	public MAryIterator280<I> clone()
	{
		try
		{
			return (MAryIterator280<I>) super.clone();
		} catch (CloneNotSupportedException e)
		{
			// Should not occur: this is a Cursor280, which implements Cloneable 
			e.printStackTrace();
			return null;
		}
	}
} 
