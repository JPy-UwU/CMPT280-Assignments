package lib280.graph;

import lib280.base.Pair280;
import lib280.exception.ItemNotFound280Exception;

public class Edge280<V extends Vertex280> extends Pair280<V, V> {

	public Edge280(V v1, V v2) {
		super(v1, v2);
	}

	/**
	 * Determine if v is one of the endpoints of the edge.
	 * @timing (1)
	 * 
	 * @param v vertex to determine whether it belongs to the edge
	 */
	public boolean has(V v) {
		return ((v == firstItem) || (v == secondItem));
	}

	/**
	 * The other item of the edge. 
	 * @timing O(1) 
	 * @precond has(v)
	 * 
	 * @param v Vertex on one end of the edge.
	 * @return vertex adjacent to v via this edge
	 */
	public V other(V v) throws ItemNotFound280Exception {
		if (!has(v))
			throw new ItemNotFound280Exception(
					"Cannot return the other item since this vertex does not exist.");

		if (firstItem == v)
			return secondItem;
		else
			// must have (secondItem == v)
			return firstItem;
	}

	/**
	 * String representation of the edge for output of v's adjacency list in a
	 * graph.
	 * @timing O(1)
	 */
	public String toStringGraphIO(V v) {
		return Integer.toString(other(v).index());
	}

	/**
	 * A shallow clone of this edge. 
	 * @timing O(1)
	 */
	public Edge280<V> clone() {
		return (Edge280<V>) super.clone();
	}
}
