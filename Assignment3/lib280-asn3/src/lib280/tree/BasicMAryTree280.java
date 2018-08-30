/* BasicMAryTree280.java
 * ---------------------------------------------
 * Copyright (c) 2004 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */

package lib280.tree;

import lib280.base.Container280;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.InvalidArgument280Exception;

/**	M-ary lib280.tree class with operations on root node. */
public class BasicMAryTree280<I> implements Container280
{
	/**	The root of the lib280.tree. */
	protected MAryNode280<I> rootNode;
  
	/**	Number of children allowed for future nodes. */
	protected int futureArity;

	/**	
	 * Create an empty lib280.tree with specified maximum arity for nodes
	 * @timing O(1)
	 * @param m maximum number of children allowed for future nodes 
	 */
	public BasicMAryTree280(int m)
	{
		setRootNode(null);
		setFutureArity(m);
	}

	/**	
	 * Create lib280.tree with the specified root node and
	 * specified maximum arity of nodes. 
	 * @timing O(1) 
	 * @param x item to set as the root node
	 * @param m number of children allowed for future nodes 
	 */
	public BasicMAryTree280(I x, int m)
	{
		MAryNode280<I> root = new MAryNode280<I>(x, m);
		setRootNode(root);
		setFutureArity(m);
	}

	/**	
	 * Obtain the contents of the root node.
	 * @timing O(1) 
	 * @precond !isEmpty()
	 */
	public I rootItem() throws ContainerEmpty280Exception
	{
		if (isEmpty())
			throw new ContainerEmpty280Exception("Cannot obtain root of an empty lib280.tree");
		
		return rootNode.item();
	}

	/**	
	 * Is the lib280.tree empty?.
	 * @timing O(1) 
	 */
	public boolean isEmpty()
	{
		return rootNode==null;
	}

	/**	
	 * Is the lib280.tree full?.
	 * @timing O(1) 
	 */
	public boolean isFull()
	{
		return false;
	}

	/**	
	 * Obtain the maximum number of children possible for the root. 
	 * @timing O(1) 
	 * @precond !isEmpty() 
	 */
	public int rootArity() throws ContainerEmpty280Exception
	{
		if (isEmpty())
			throw new ContainerEmpty280Exception("Cannot call count() on an empty lib280.tree");
		
		return rootNode.count();
	}

	/**	
	 * Obtain the index of the last non-empty child of root. 
	 * @timing O(1) 
	 */
	public int rootLastNonEmptyChild()
	{
		return rootNode.lastNonEmptyChild();
	}

	/**	
	 * Obtain number of children allowed for future nodes. 
	 * @timing O(1) 
	 */
	public int futureArity()
	{
		return futureArity;
	}

	/**	
	 * Modify the number of children allowed for future nodes. 
	 * @timing O(1) 
	 * @param newArity new number of children allowed for future nodes 
	 */
	public void setFutureArity(int newArity)
	{
		futureArity = newArity;
	}

	/**	
	 * Obtain the i'th subtree of the root.
	 * @timing O(1)
	 * @precond !isEmpty() 
	 * @precond !((i<=0) || (i>rootArity())) 
	 * param i position of the subtree to be returned 
	 */
	public BasicMAryTree280<I> rootSubtree(int i) throws ContainerEmpty280Exception, InvalidArgument280Exception
	{
		if (isEmpty())
			throw new ContainerEmpty280Exception("Cannot perform a search for the i'th subtree on an empty lib280.tree");
		if ((i<=0) || (i>rootArity()))
			throw new InvalidArgument280Exception("Cannot access i'th subtree since i is an invalid location.");
		
		BasicMAryTree280<I> result = this.clone();
		result.setRootNode(rootNode.subnode(i));
		return result;
	}

	/**	
	 * A shallow clone of this lib280.tree.
	 * @timing O(1) 
	 */
	@SuppressWarnings("unchecked")
	public BasicMAryTree280<I> clone()
	{
		try
		{
			return (BasicMAryTree280<I>) super.clone();
		} catch (CloneNotSupportedException e) 
		{
			// Should not occur: BasicMAryTree280 implements Cloneable
			e.printStackTrace();
			return null;
		}
	}

	/**	
	 * Set contents of the root node to x. 
	 * @timing O(1) 
	 * @precond !isEmpty() 
	 * @param x item to be set as the root item 
	 */
	public void setRootItem(I x) throws ContainerEmpty280Exception
	{
		if (isEmpty())
			throw new ContainerEmpty280Exception("Cannot set root of an empty lib280.tree.");
		
		rootNode.setItem(x);
	}

	/**	
	 * Replace the root node of the lib280.tree.
	 * @timing O(1) 
	 * @param newRoot The node to be the new root of this lib280.tree.
	 */
	protected void setRootNode(MAryNode280<I> newRoot)
	{
		rootNode = newRoot;
	}

	/**	
	 * Insert x as new root item with old lib280.tree as the
	 * i'th subtree of the new root. 
	 * @timing O(1)
	 * @precond !((i<=0) || (i>rootArity())) 
	 * @param x item to become the new root item
	 * @param i old lib280.tree is to become the i-th subtree of the new root
	 */
	public void insertRoot(I x, int i) throws InvalidArgument280Exception
	{
		if ((i<=0) || (i>futureArity()))
			throw new InvalidArgument280Exception("Cannot set i'th subtree since i is an invalid location.");
		
		MAryNode280<I> temp = new MAryNode280<I>(x, futureArity);
		temp.setSubnode(i, rootNode);
		if (temp.subnode(i)!=null)
			temp.setLastNonEmptyChild(i);
		rootNode = temp;
	}

	/**	
	 * Delete the root of the lib280.tree keeping the non-empty subtree.
	 * 
	 * If there is more than one non-empty subtree, 
	 * replace the root item by the item
	 * specified by findReplacementItemPosition. 
	 * @timing O(1) when lib280.tree has size 1, otherwise
	 *         O(n) n = length of path to a leaf
	 * @precond !isEmpty() 
	 */
	public void deleteRoot() throws ContainerEmpty280Exception
	{
		if (isEmpty())
			throw new ContainerEmpty280Exception("Cannot delete from an empty lib280.tree.");
		
		MAryIterator280<I> curPos = new MAryIterator280<I>(this, null, 0, rootNode);
		deleteItemInPosition(curPos);
	}

	/**	
	 * Replace the i'th subtree of the root node. 
	 * @timing O(rootArity()) worst case (inserting empty lib280.tree)
	 * @precond !isEmpty()
	 * @precond !((i<=0) || (i>rootArity)) 
	 * @param t lib280.tree to be set as the new i'th subtree of the root
	 * @param i position of the subtree to set t to 
	 */
	public void setRootSubtree(BasicMAryTree280<I> t, int i) throws ContainerEmpty280Exception, InvalidArgument280Exception
	{
		if (isEmpty())
			throw new ContainerEmpty280Exception("Cannot set subtree of an empty lib280.tree.");
		if ((i<=0) || (i>rootArity()))
			throw new InvalidArgument280Exception("Cannot set t to i'th subtree since i is an invalid location.");
		
		rootNode.setSubnode(i, t.rootNode);
		if (!t.isEmpty() && i>rootNode.lastNonEmptyChild())
			rootNode.setLastNonEmptyChild(i);
		else if (t.isEmpty() && i==rootNode.lastNonEmptyChild())
		{
			int j=i-1;
			while (j>0 && rootNode.subnode(j)!=null)
				j--;
			rootNode.setLastNonEmptyChild(j);
		}
	}

	/**	
	 * Remove all items from the lib280.tree.
	 * @timing O(1) 
	 */
	public void clear()
	{
		setRootNode(null);
	}

	/**	
	 * String representation of the lib280.tree.
	 * @timing O(n), n = number of items in the lib280.tree
	 */
	public String toString()
	{
		if (isEmpty())
			return " ";
		else
			return rootNode.toString();
	}

	/**	
	 * String representation of the lib280.tree, level by level.
	 * @timing O(n), where n = number of items in the lib280.tree
	 */
	public String toStringByLevel()
	{
		return toStringByLevel(1);
	}

	/**	
	 * Form a string representation that includes level number. 
	 * @timing O(n), where n = number of items and empty subtrees 
	 */
	protected String toStringByLevel(int i)
	{
		String result = "\n";
		for (int j=1; j<i; j++)
			result += "     ";
		result += String.valueOf(i) + ": ";
		if (isEmpty())
			result += "- ";
		else
		{
			result += rootItem().toString();
			if (rootLastNonEmptyChild()>0)
			{
				for (int j=1; j<=rootNode.subnode.length; j++)
					result += rootSubtree(j).toStringByLevel(i+1);
			}
		}
		return result;
	}

	/**	
	 * Delete the item at the position specified by pos. 
	 * @timing Time = O(1) when it is a leaf. 
					  O(n) n = length of path to a leaf 
	 * @precond !isEmpty()
	 * @param pos iterator position of item to be deleted 
	 */
	protected void deleteItemInPosition(MAryIterator280<I> pos) throws ContainerEmpty280Exception
	{
		if (isEmpty())
			throw new ContainerEmpty280Exception("Cannot set subtree of an empty lib280.tree.");
	
		if (pos.cur.lastNonEmptyChild()==0)
		{
			if (pos.parent==null)
				clear();
			else
			{
				pos.parent.setSubnode(pos.index(), null);
				if (pos.parent.lastNonEmptyChild()==pos.index())
				{
					int j = pos.index()-1;
					while (j!=0 && pos.parent.subnode(j)==null)
						j--;
					pos.parent.setLastNonEmptyChild(j);
				}
			}
		}
		else
		{
			MAryIterator280<I> replacePos = findReplacementItemPosition(pos.cur);
			pos.cur.setItem(replacePos.cur.item());
			deleteItemInPosition(replacePos);
		}
	}

	/**	
	 * Find the position of the rightmost leaf of the currentPosition. 
	 * @timing O(n), where n = length of the path to the leaf 
	 * @param currentLocation node to find the replacement node for 
	 */
	protected MAryIterator280<I> findReplacementItemPosition(MAryNode280<I> currentLocation) throws ContainerEmpty280Exception
	{
		if (isEmpty())
			throw new ContainerEmpty280Exception("Cannot find rightmost leaf of an empty lib280.tree.");
		
		MAryNode280<I> prev = currentLocation;
		MAryNode280<I> cur = prev.subnode(prev.lastNonEmptyChild());
		while (cur.lastNonEmptyChild()!=0)
		{
			prev = cur;
			cur = prev.subnode(prev.lastNonEmptyChild());
		}
		return new MAryIterator280<I> (this, prev, prev.lastNonEmptyChild(), cur);
	}

} 
