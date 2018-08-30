

import lib280.graph.Vertex280;

public class UFVertex280 extends Vertex280 {

	// Rank of the node -- used to maintain good balance of the trees that are embedded in the graph.
	int rank;
	
	public UFVertex280(int id) {
		super(id);
		this.rank = 0;
	}

	/**
	 * @return the rank of the node
	 */
	public int rank() {
		return rank;
	}

	/**
	 * @param rank the new rank for the node.
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

}