/* MAryNode280.java
 * ---------------------------------------------
 * Copyright (c) 2004 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */

package lib280.tree;


/**	A linked node with arbitrary arity for an m-ary lib280.tree.  It has
	contents of generic type, an array to store the subnodes, and a 
	feature to refer to the last non-empty subnode.  There is a 
	function to check the equality of two m-ary nodes, and the out
	function gives a pre-order string representation of the m-ary
	lib280.tree with the current node as the root. */
public class MAryNode280<I> implements Cloneable
{
	/**	Contents of the node. */
	protected I item;

	/**	References to children. */
	protected MAryNode280<I> subnode[];

	/**	Index of the last non-empty child of the node. */
	protected int lastNonEmptyChild;

	/**	
	 * Construct a node with initItem as the item and nodeArity possible children. <br>
	 * @timing O(nodeArity)
	 * @param initItem item new node is constructed with
	 * @param nodeArity number of children possible for the new node 
	 */
	
	@SuppressWarnings("unchecked")
	public MAryNode280(I initItem, int nodeArity)
	{
		setItem(initItem);
		subnode = new MAryNode280[nodeArity];
	}

	/**	
	 * Contents of the node.
	 * @timing O(1) 
	 */
	public I item()
	{
		return item;
	}

	/**	
	 * Number of children for this node (includes empty children). 
	 * @timing O(1) 
	 */
	public int count()
	{
		return subnode.length;
	}

	/**
	 * Obtain the i'th child for this node.
	 * 
	 * @timing O(1) 
	 * @param i index/offset of child node to be returned 
	 */
	public MAryNode280<I> subnode(int i)
	{
		return subnode[i-1];
	}

	/**	
	 * Index of the last non-empty child of the node. 
	 * @timing O(1) 
	 */
	public int lastNonEmptyChild()
	{
		return lastNonEmptyChild;
	}

	/**	
	 * Set item stored at this node to x.
	 * @timing O(1) 
	 * @param x  item to set as this node's item 
	 */
	public void setItem(I x)
	{
		item = x;
	}

	/**	
	 * Set child i to be a new node.
	 * @timing O(1) 
	 * @param i index/offst of child node to replace
	 * @param newNode new node to be set as the i'th child 
	 */
	protected void setSubnode(int i, MAryNode280<I> newNode)
	{
		subnode[i-1] = newNode;
	}


	/**	
	 * Set lastNonEmptyChild to newValue. 
	 * @timing O(1) 
	 * @param newValue position of the last non-empty child 
	 */
	public void setLastNonEmptyChild(int newValue)
	{
		lastNonEmptyChild = newValue;
	}

	/**
	 * Pre-order string representation suitable for output.
	 * 
	 * @timing O(count) 
	 */
	public String toString()
	{
		String result = item.toString() + " ";
		if (lastNonEmptyChild > 0)
			for (int i=0; i<subnode.length; i++)
			{
				if (subnode[i]!=null)
					result += subnode[i].toString();
			}
		return result;
	}

	/**	
	 * A shallow clone of this object. 
	 * @timing O(1) 
	 */
	@SuppressWarnings("unchecked")
	public MAryNode280<I> clone()
	{
		try
		{
			return (MAryNode280<I>) super.clone();
		} catch (CloneNotSupportedException e)
		{
			// Should not occur: this class implements Cloneable 
			e.printStackTrace();
			return null;
		}
	}

}
