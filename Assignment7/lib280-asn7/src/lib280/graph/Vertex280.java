package lib280.graph;

/** Vertex in a graph where the vertex has a unique index. */
public class Vertex280 implements Cloneable {
	protected int index;

	/**
	 * Construct a new vertex with id as its index. <br>
	 * Analysis: Time = O(1)
	 * 
	 * @param id
	 *            index of the new vertex
	 */
	public Vertex280(int id) {
		index = id;
	}

	/**
	 * The index of this vertex. <br>
	 * Analysis: Time = O(1)
	 */
	public int index() {
		return index;
	}

	/**
	 * String representation of the vertex. <br>
	 * Analysis: Time = O(1)
	 */
	public String toString() {
		return String.valueOf(index);
	}

	/**
	 * A shallow clone of this vertex. <br>
	 * Analysis: Time = O(1)
	 */
	public Vertex280 clone() {
		try {
			return (Vertex280) super.clone();
		} catch (CloneNotSupportedException e) {
			/* Should not occur: implements Cloneable. */
			e.printStackTrace();
			return null;
		}
	}
}
