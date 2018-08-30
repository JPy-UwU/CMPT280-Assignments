/* LinkedSimpleTree280.java
 * ---------------------------------------------
 * Copyright (c) 2004 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */

package lib280.tree;

import lib280.exception.ContainerEmpty280Exception;

/**	An implementation of the SimpleTree280 interface with functions to access and 
	set the root node and the root subtrees.  It also has functions to 
	access the root item, test for empty, and to wipe out all the items. */
public class LinkedSimpleTree280<I extends Comparable<? super I>> implements SimpleTree280<I>
{
	/**	Root node of the lib280.tree. */
	protected BinaryNode280<I> rootNode;

	/**	Create an empty lib280.tree. <br>
		Analysis: Time = O(1) */
	public LinkedSimpleTree280()
	{
		rootNode = null;
	}

	/**	Create a lib280.tree from a root and two subtrees. <
		Analysis: Time = O(1) 
		@param lt lib280.tree to initialize as the left subtree.  If null, the left subtree is empty.
		@param r item to initialize as the root item
		@param rt lib280.tree to initialize as the right subtree.  If null, the right subtree is empty. */
	public LinkedSimpleTree280(LinkedSimpleTree280<I> lt, I r, LinkedSimpleTree280<I> rt)
	{
		rootNode = createNewNode(r);
		setRootLeftSubtree(lt);
		setRootRightSubtree(rt);
	}

	/**	Create a new node that is appropriate to this lib280.tree.  This method should be
		overidden for classes that extend this class and need a specialized node,
		i.e., a descendant of BinaryNode280. 
		Analysis: Time = O(1) 
		@param item    The item to be placed in the new node */
	protected BinaryNode280<I> createNewNode(I item)
	{
		return new BinaryNode280<I>(item);
	}

	/**	Is the lib280.tree empty?.
		Analysis: Time = O(1)  */
	public boolean isEmpty()
	{
		return rootNode == null;
	}

	/**	Is the lib280.tree full?.
		Analysis: Time = O(1) */
	public boolean isFull()
	{
		return false;
	}

	/**	Return the root node. 
		Analysis: Time = O(1) */
	protected BinaryNode280<I> rootNode()
	{
		return rootNode;
	}

	/**	Set root node to new node.
		Analysis: Time = O(1) 
		@param newNode node to become the new root node */
	protected void setRootNode(BinaryNode280<I> newNode)
	{
		rootNode = newNode;
	}

	/**	Contents of the root item. 
		Analysis: Time = O(1) 
		@precond !isEmpty()
	 */
	public I rootItem() throws ContainerEmpty280Exception
	{
		if (isEmpty()) 
			throw new ContainerEmpty280Exception("Cannot access the root of an empty lib280.tree.");
		
		return rootNode.item();
	}

	/**	Set contents of the root to x. 
		Analysis: Time = O(1) 
		@precond !isEmpty() 
		@param x item to become the new root item 
	  */
	public void setRootItem(I x) throws ContainerEmpty280Exception
	{
		if (isEmpty())
			throw new ContainerEmpty280Exception("Cannot set the root of an empty lib280.tree.");
		
		rootNode.setItem(x);
	}

	/**	Left subtree of the root. 
		Analysis: Time = O(1) 
		@precond !isEmpty() 
	  */
	public LinkedSimpleTree280<I> rootLeftSubtree() throws ContainerEmpty280Exception
	{
		if (isEmpty())
			throw new ContainerEmpty280Exception("Cannot return a subtree of an empty lib280.tree.");
		
		LinkedSimpleTree280<I> result = this.clone();
		result.clear();
		result.setRootNode(rootNode.leftNode());
		return result;
	}

	/**	Right subtree of the root. 
		Analysis: Time = O(1) 
		@precond !isEmpty() 
	  */
	public LinkedSimpleTree280<I> rootRightSubtree() throws ContainerEmpty280Exception
	{
		if (isEmpty())
			throw new ContainerEmpty280Exception("Cannot return a subtree of an empty lib280.tree.");
		
		LinkedSimpleTree280<I> result = this.clone();
		result.clear();
		result.setRootNode(rootNode.rightNode());
		return result;
	}

	/**	Set the left subtree to t (set isEmpty if t == null). 
		Analysis: Time = O(1) 
		@precond !isEmpty() 
		@param t lib280.tree to become the rootLeftSubtree()
	  */
	public void setRootLeftSubtree(LinkedSimpleTree280<I> t) throws ContainerEmpty280Exception
	{
		if (isEmpty())
			throw new ContainerEmpty280Exception("Cannot set subtree of an empty lib280.tree.");
		
		if (t != null)
			rootNode.setLeftNode(t.rootNode);
		else
			rootNode.setLeftNode(null);
	}

	/**	Set the right subtree to t (set isEmpty if t == null). 
		Analysis: Time = O(1) 
		@precond !isEmpty() 
		@param t lib280.tree to become the rootRightSubtree()
	  */
	public void setRootRightSubtree(LinkedSimpleTree280<I> t) throws ContainerEmpty280Exception
	{
		if (isEmpty())
			throw new ContainerEmpty280Exception("Cannot set subtree of an empty lib280.tree.");
		
		if (t != null)
			rootNode.setRightNode(t.rootNode);
		else
			rootNode.setRightNode(null);
	}

	/**	Remove all items from the lib280.tree.
		Analysis: Time = O(1) */
	public void clear()
	{
		setRootNode(null);
	}

	/**	Form a string representation that includes level numbers. 
		Analysis: Time = O(n), where n = number of items in the (sub)lib280.tree
		@param i the level for the root of this (sub)lib280.tree
	  */
	protected String toStringByLevel(int i) 
	{
		StringBuffer blanks = new StringBuffer((i - 1) * 5);
		for (int j = 0; j < i - 1; j++)
			blanks.append("     ");
	  
		String result = new String();
		if (!isEmpty() && (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty()))
			result += rootRightSubtree().toStringByLevel(i+1);
		
		result += "\n" + blanks + i + ": " ;
		if (isEmpty())
			result += "-";
		else 
		{
			result += rootItem();
			if (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty())
				result += rootLeftSubtree().toStringByLevel(i+1);
		}
		return result;
	}

	/**	String representation of the lib280.tree, level by level. <br>
		Analysis: Time = O(n), where n = number of items in the lib280.tree
	  */
	public String toStringByLevel()
	{
		return toStringByLevel(1);
	}

	/**	String containing an inorder list of the items of the lib280.tree.
		Analysis: Time = O(n), where n = number of items in the lib280.tree
	  */
	public String toString()
	{
		if (isEmpty())
			return "";
		else
			return rootNode().toString();
	}

	/**	A shallow clone of this lib280.tree.
		Analysis: Time = O(1)
	  */
	@SuppressWarnings("unchecked")
	public LinkedSimpleTree280<I> clone()
	{
		try
		{
			return (LinkedSimpleTree280<I>) super.clone();
		} catch(CloneNotSupportedException e)
		{
			/*	Should not occur because Container280 extends Cloneable */
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String args[]) {
		LinkedSimpleTree280<Double> T = new LinkedSimpleTree280<Double>(null, 42.0, null);
		
		LinkedSimpleTree280<Double> T2 = T.clone();
		
		System.out.println(T);
		System.out.println(T2);
		
	}
}
