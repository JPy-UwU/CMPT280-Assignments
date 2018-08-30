package lib280.list;


/**	A node containing an item and references to the next and 
	previous nodes.  It includes routines to set the item or set 
	either adjacent node. */
public class BilinkedNode280<I> extends LinkedNode280<I>
{
  
	/**	The previous node. */
	protected BilinkedNode280<I> previousNode;

	/**	Construct a new node with item x. <br>
		Analysis: Time = O(1) 
		@param x item placed in the node */
	public BilinkedNode280(I x)
	{
		super(x);      
	}

	/**	The previous node. <br>
		Analysis: Time = O(1) */
	public BilinkedNode280<I> previousNode()
	{
		return previousNode;
	}

	/**	Set the reference to the previous node. <br>
		Analysis: Time = O(1) 
		@param x node to be set as the new previous node */
	public void setPreviousNode(BilinkedNode280<I> x)
	{
		previousNode = x;
	}

	/**	A shallow clone of this object. <br> 
	Analysis: Time = O(1) */
	public BilinkedNode280<I> clone()
	{
		return (BilinkedNode280<I>) super.clone();
	}

} 
