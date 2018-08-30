import lib280.graph.Edge280;
import lib280.graph.Vertex280;
import lib280.graph.WeightedEdge280;
import lib280.graph.WeightedGraphAdjListRep280;
import lib280.tree.ArrayedMinHeap280;
import lib280.tree.IterableArrayedHeap280;
import lib280.tree.OrderedSimpleTree280;

public class Kruskal {

	public static WeightedGraphAdjListRep280<Vertex280> minSpanningTree(WeightedGraphAdjListRep280<Vertex280> G) {
		WeightedGraphAdjListRep280 minST = new WeightedGraphAdjListRep280(G.numVertices(), false);	// Undirected weighted graph that contains the same items as G, without edges
		minST.ensureVertices(G.numVertices());	// Ensures that we have the correct number of vertices in our graph, and avoid duplicates.
		UnionFind280 UF = new UnionFind280(minST.numVertices());	// Union find data structure containing the node set of G
		ArrayedMinHeap280 sortedEdges = new ArrayedMinHeap280(G.numEdges() * 2);	// Heap holding all the possible edges of G, ordered from smallest to largest.
		// Since this is an undirected graph, the capacity of items will be the # of Edges * 2, since we can go both back and forth in an undirected graph

		G.goFirst();	// move the vertex cursor to the first vertex in our graph
		while (G.itemExists()) {	// While item exist
			G.eGoFirst(G.item());	// Move the edge cursor to the first edge attached to the current vertex
			while (G.eItemExists()) {	// While the edge vertex exist, go through every possible edge we can go into
				sortedEdges.insert(G.eItem());	// Insert the edge into the heap
				G.eGoForth();	// Move to the next edge
			}
			G.goForth();	// Move to the next vertex
		}



		while (!sortedEdges.isEmpty()) {	// While we havent iterated through all items in our heap
			WeightedEdge280 currentEdge = (WeightedEdge280) sortedEdges.item();	// Get the edge on top of the heap
			int a = ((Vertex280) currentEdge.firstItem()).index();	// Get the index of the first vertex the current edge is attached to
			int b = ((Vertex280) currentEdge.secondItem()).index();	// Get the index of the 2nd index adjacent to a
			if (UF.find(a) != UF.find(b)) {	// If the representative of a and b are not the same, they must be on a different subset
				minST.addEdge(a,b);	// add the edge a, b to our new minSpanningTree
				minST.setEdgeWeight(a, b, G.getEdgeWeight(a,b));	// Set the weight of these edges to edge weight of a and b from G
				UF.union(a, b);	// Set a union between a and b

			}
			sortedEdges.deleteItem();	// Delete the edge from the heap
		}

		return minST;
	}


	public static void main(String args[]) {
		WeightedGraphAdjListRep280<Vertex280> G = new WeightedGraphAdjListRep280<Vertex280>(1, false);
		// If you get a file not found error here and you're using eclipse just remove the
		// 'Kruskal-template/' part from the path string.
		G.initGraphFromFile("Kruskal-template/mst.graph");

		System.out.println(G);

		WeightedGraphAdjListRep280<Vertex280> minST = minSpanningTree(G);

		System.out.println(minST);

	}
}


