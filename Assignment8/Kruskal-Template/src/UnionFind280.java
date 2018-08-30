

import lib280.base.CursorPosition280;
import lib280.graph.Edge280;
import lib280.graph.GraphAdjListRep280;
import lib280.graph.Vertex280;


public class UnionFind280 {
	GraphAdjListRep280<Vertex280, Edge280<Vertex280>> G;

	/**
	 * Create a new union-find structure.
	 *
	 * @param numElements Number of elements (numbered 1 through numElements, inclusive) in the set.
	 * @postcond The structure is initialized such that each element is in its own subset.
	 */
	public UnionFind280(int numElements) {
		G = new GraphAdjListRep280<Vertex280, Edge280<Vertex280>>(numElements, true);
		G.ensureVertices(numElements);
	}

	/**
	 * Return the representative element (equivalence class) of a given element.
	 * @param id The elements whose equivalence class we wish to find.
	 * @return The representative element (equivalence class) of the element 'id'.
	 */
	public int find(int id) {
		GraphAdjListRep280 holder = G;	// Holder for the graph so we do not need to alter the actual Graph's curso position

		holder.goIndex(id);	// Move the vertex cursor to the vertex located in location id
		holder.eGoFirst(G.item());	// Move the edge cursor to the first edge attached to vertex id
		while (holder.eItemExists()) {	// While the item exist
			holder.goVertex(holder.eItemAdjacentVertex());	// Move the vertex to the vertex adjacent to the current vertex
			holder.eGoFirst(holder.item());	// Move edge cursor to the first edge of the vertex cursor
		}

		return holder.itemIndex();	// Result
	}

	/**
	 * Merge the subsets containing two items, id1 and id2, making them, and all of the other elemnets in both sets, "equivalent".
	 * @param id1 First element.
	 * @param id2 Second element.
	 */
	public void union(int id1, int id2) {
		int root_v1 = find(id1); // Find the representative of the first items
		int root_v2 = find(id2);	// Find the representative of the second item

		if (root_v1 != root_v2) {	// If the 2 items are not equal
			G.addEdge(root_v1, root_v2);	// Add an edge from root1 --> root2 (Union)
		}
	}

	public static void main(String[] args) {
		UnionFind280 uf = new UnionFind280(8);
		uf.G.addEdge(1,2);
		uf.G.addEdge(3,2);
		uf.G.addEdge(7,3);
		uf.G.addEdge(4,6);
		uf.G.addEdge(6,5);

		System.out.println("Representative that is carrying item 7 is: " + uf.find(7));
		System.out.println("Representative that is carrying item 4 is: " + uf.find(4));
		System.out.println("Representative that is carrying item 6 is: " + uf.find(6));
		System.out.println("Representative that is carrying item 1 is: " + uf.find(1));
		System.out.println("Representative that is carrying item 8 is: " + uf.find(8));
	}

}
