import lib280.graph.Vertex280;
import lib280.graph.WeightedEdge280;
import lib280.graph.WeightedGraphAdjListRep280;
import lib280.tree.ArrayedMinHeap280;

public class Kruskal {
	
	public static WeightedGraphAdjListRep280<Vertex280> minSpanningTree(WeightedGraphAdjListRep280<Vertex280> G) {
		WeightedGraphAdjListRep280<Vertex280> minST = 
				new WeightedGraphAdjListRep280<Vertex280>(G.capacity(), false);
		minST.ensureVertices(G.numVertices());
		
		// Initialize the union-find structure.
		UnionFind280 UF = new UnionFind280(G.numVertices());
		
		// Put all the edges on a min heap, (WeightedEdge280 objects are comparable by their weight)
		ArrayedMinHeap280<WeightedEdge280<Vertex280>> H =
				new ArrayedMinHeap280<WeightedEdge280<Vertex280>>(G.numVertices() * G.numVertices());
				
		// Iterate over all vertices.
		G.goFirst();
		while(G.itemExists()) {
			// For each vertex, iterate over all edges
			G.eGoFirst(G.item());
			while(G.eItemExists()) {
				H.insert(G.eItem());
				G.eGoForth();
			}
			G.goForth();
		}
		
		while(!H.isEmpty()) {
			// Remove next smallest edge.
			WeightedEdge280<Vertex280> e = H.item();
			H.deleteItem();
			int srcID = e.firstItem().index();
			int dstID = e.secondItem().index();
			if( UF.find(srcID) != UF.find(dstID) ) {
				minST.addEdge(srcID, dstID);
				minST.setEdgeWeight(srcID, dstID, e.getWeight());
				UF.union(srcID,  dstID);
			}
		}
		
		
		return minST;
	}
	
	
	public static void main(String args[]) {
		WeightedGraphAdjListRep280<Vertex280> G = new WeightedGraphAdjListRep280<Vertex280>(1, false);
		G.initGraphFromFile("mst.graph");
		System.out.println(G);
		
		WeightedGraphAdjListRep280<Vertex280> minST = minSpanningTree(G);
		
		System.out.println(minST);
	}
}


