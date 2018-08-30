package lib280.graph;

import lib280.base.CursorPosition280;
import lib280.exception.AfterTheEnd280Exception;
import lib280.exception.InvalidArgument280Exception;
import lib280.exception.ItemNotFound280Exception;
import lib280.exception.NoCurrentItem280Exception;
import lib280.list.LinkedList280;


public class GraphAdjListRep280<V extends Vertex280, E extends Edge280<V>> extends GraphWithCursors280<V, E> {

	
	///////////// Instance variables ////////////
	
	// An array of adjacency lists, indexed by vertex.
	protected LinkedList280<E> adjLists[];
	
	// Current node in the iteration of the adjacency list of the current vertex.
	//protected LinkedIterator280<E> curEdgeNode;
	
	
	/**
	 * Create a new graph.
	 * 
	 * @param cap
	 *            Maximum number of nodes in the graph.
	 * @param d
	 *            True if the graph is to be directed, false otherwise.
	 */
	public GraphAdjListRep280(int cap, boolean d) {
		super(cap, d);
	}

	/**
	 * Create a new graph with custom vertex and edge types.
	 * 
	 * @param cap
	 *            Maximum number of nodes in the graph.
	 * @param d
	 *            True if the graph is to be directed, false otherwise.
	 * @param vertexTypeName
	 *            Type name to use for Vertex objects (must be subclass of
	 *            {@link Vertex280})
	 * @param edgeTypeName
	 *            Type name to use for edge objects (must be a subclass of
	 *            {@link Edge280})
	 */
	public GraphAdjListRep280(int cap, boolean d, String vertexTypeName,
			String edgeTypeName) {
		super(cap, d, vertexTypeName, edgeTypeName);
	}

	@Override
	public CursorPosition280 currentPosition() {
		// Return the cursor positions
		return new GraphPosition280<V, E>(this.item, this.itemIndex,
				this.iterationIndex, this.eItem, this.adjIndex, this.adjLists[this.iterationIndex].currentPosition());	
	}

	@Override
	public void goPosition(CursorPosition280 pos) {
		// Restore cursor positions
		if (!(pos instanceof GraphPosition280))
			throw new InvalidArgument280Exception(
					"The cursor position must be an instance of GraphMatrixRepPosition280<V, E>");
		@SuppressWarnings("unchecked")
		GraphPosition280<V, E> matrixPos = (GraphPosition280<V, E>) pos;
		this.item = matrixPos.item;
		this.itemIndex = matrixPos.itemIndex;
		this.iterationIndex = matrixPos.iterationIndex;
		this.eItem = matrixPos.eItem;
		this.adjIndex = matrixPos.adjIndex;	
		this.adjLists[iterationIndex].goPosition(matrixPos.edgeIterationPosition);
	}

	@Override
	public void eSearch(V u, V v) {
		// Start at the beginning of the adjacency list for u
		this.eGoFirst(u);
		
		// Step through the list until we find an edge with destination node v
		while(this.eItemExists() && this.adjLists[this.iterationIndex].item().secondItem() != v) {
			this.eGoForth();
		}
	}

	@Override
	public void deleteEItem() throws NoCurrentItem280Exception {
		// If no edge at the edge cursor, no edge can be deleted.
		if( !this.eItemExists() ) 
				throw new NoCurrentItem280Exception("Cannot delete an item that does not exist.");
		
		// Delete the edge		
		this.adjLists[this.iterationIndex].deleteItem();
		
		// If the graph is undirected, delete the symmetric edge.
		if( !this.directed ) {
			CursorPosition280 p = this.currentPosition();
			this.eSearch(eItem.secondItem(), eItem.firstItem());
			if( !this.eItemExists() ) throw new NoCurrentItem280Exception("Symmetric edge was not found when deleting edge.");
			this.adjLists[this.iterationIndex].deleteItem();
			this.goPosition(p);
		}
		
		// Check the current cursor position in the list.  If it's at an item, make
		// the edge cursor point to that item. Otherwise, set eItem to null to put
		// the edge cursor in the "after" position.
		if( this.adjLists[this.iterationIndex].itemExists() ) 
			this.eItem = this.adjLists[this.iterationIndex].item();
		else this.eItem = null;
		
		this.numEdges = this.numEdges - 1;
		
	}

	@Override
	public void eGoFirst(V v) {
		// Set the vertex whose edges are being iterated.
		this.iterationIndex = v.index();
				
		// Set up the edge cursor
		this.adjLists[this.iterationIndex].goBefore();
		
		// Advance to the first item unless the edge list is empty.
		if(!this.adjLists[this.iterationIndex].isEmpty() ) 
			this.eGoForth();
		else {
			this.eItem = null;
			this.adjIndex = 0;
			this.adjLists[this.iterationIndex].goAfter();
		}
	}

	@Override
	public void eGoForth() throws AfterTheEnd280Exception {
		
		// If we're already after, we can't go any further.
		if (this.eAfter())
			throw new AfterTheEnd280Exception(
					"Cannot go to next edge since already after.");

		this.adjLists[this.iterationIndex].goForth();
		
		// If we end up after, set eItem to null so that eItemExists() will return false
		if (this.eAfter()) {
			this.eItem = null;
			this.adjIndex = 0;
		}
		else {
			
			// Otherwise, set the current edge, and the current adjacent vertex.
			this.eItem = this.adjLists[this.iterationIndex].item();
			this.adjIndex = this.adjLists[this.iterationIndex].item().secondItem().index();
		}
	}

	@Override
	public boolean eAfter() {
		return this.adjLists[this.iterationIndex].after();
	}

	@Override
	public boolean isAdjacent(int srcIdx, int dstIdx)
			throws ItemNotFound280Exception {
		return this.isAdjacent(this.vertex(srcIdx), this.vertex(dstIdx));
	}

	@Override
	public boolean isAdjacent(V v1, V v2) {
		// Save cursor position
		CursorPosition280 p = this.currentPosition();
		
		// Check to see if the edge (v1, v2) exists
		this.eSearch(v1, v2);
		boolean result = this.eItemExists();
		
		// Restore the cursor
		this.goPosition(p);
		
		// Return the result.
		return result;
	}

	@Override
	public void addEdge(V x, V y) {
		
		V v1 = null, v2 = null;
		
		// This is a safeguard in case x and y are not vertex objects in this graph.
		// This might cause an IndexOutOfBoundsException if x.index() or y.index() exceeds this.capacity()
		try {
			v1 = this.vertex(x.index());
		}
		catch( IndexOutOfBoundsException e ) {
			throw new InvalidArgument280Exception("Index of source vertex exceeds capacity of this graph.  Did you pass in a vertex from a different graph object?");
		}
		
		try {
			v2 = this.vertex(y.index());
		}
		catch( IndexOutOfBoundsException e ) {
			throw new InvalidArgument280Exception("Index of destination vertex exceeds capacity of this graph.  Did you pass in a vertex from a different graph object?");
		}
		
		if( isAdjacent(v1, v2) ) return;

		// Add the edge (v1, v2) to the adjacency list for vertex v1.
		E newEdge = this.createNewEdge(v1, v2);
		this.adjLists[v1.index()].insert(newEdge);
		
		// If the graph is undirected, add the edge (v2, v1) to the adjacency list for vertex v2.
		if( !this.directed && v1 != v2) {
			E symmetricEdge = this.createNewEdge(v2, v1);
			this.adjLists[v2.index()].insert(symmetricEdge);
		}
		
		this.numEdges = this.numEdges + 1;
	}
	
	
	@Override
	public void clear() {
		super.clear();
		for(int i=0; i < this.capacity(); i++) 
			this.adjLists[i].clear();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void createEdgeDataStructure() {
		this.adjLists = new LinkedList280[vertexArray.length+1];

		for(int i=0; i < vertexArray.length+1; i++) {
			this.adjLists[i] =  new LinkedList280<E>();
		}
	} 
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GraphAdjListRep280<Vertex280, Edge280<Vertex280>> G = new GraphAdjListRep280<Vertex280, Edge280<Vertex280>>(10, false);

		G.initGraphFromFile("lib280-asn8/src/lib280/graph/testgraph.gra");
		
		System.out.println(G);	
		if( G.numEdges != 10 ) {
			System.out.println("Error: Graph should have 10 edges, but it has " + G.numEdges + ".");
		}
		
		// Testing eSearch()
		G.eSearch(G.vertex(6), G.vertex(5));
		if( !G.eItemExists()) System.out.println("Error: edge (6,5) was not found, but it should have been.");
		if( G.eItem().firstItem().index() != 6 || G.eItem().secondItem().index() != 5) 
			System.out.println("Error: should have located edge (6,5) but edge " + G.eItem() + " was located instead");
		
		
		System.out.println(G);
		System.out.println("Deleting: ");
		System.out.println(G.eItem());
		// Testing deleteItem()
		G.deleteEItem();
		if(G.numEdges != 9) {
			System.out.println("Error: Graph should have 9 edges after deletion of [6,5] but it has " + G.numEdges + ".");
		}
		if( !G.eItemExists() ) System.out.println("Error: edge cursor should be on edge [6,4] but it is not on any edge.");
		else if( G.eItem().firstItem().index( )!= 6 && G.eItem().secondItem().index() != 4 ) 
			System.out.println("Error: Edge cursor should be on [6,4] but it is on some other edge.");
		System.out.println(G);	
		System.out.println(G.eItem());

		G.eSearch(G.vertex(6), G.vertex(5));	
		if( G.eItemExists() ) System.out.println("Error: edge (6,5) was found, but it should not have been.");		
		
		// Testing eGoFirst()
		G.eGoFirst(G.vertex(3));
		if( !G.eItemExists() ) System.out.println("Error: Current edge should be (3,5) but no edge was found.");
		if( G.eItem().firstItem().index() != 3 || G.eItem().secondItem().index() != 5) 
			System.out.println("Error: Current edge should be (3,5) but edge " + G.eItem() + " was is the current edge instead");

		G.eGoFirst(G.vertex(4));
		if( !G.eItemExists() ) System.out.println("Error: Current edge should be (4,8) but no edge was found.");
		if( G.eItem().firstItem().index() != 4 || G.eItem().secondItem().index() != 8) 
			System.out.println("Error: Current edge should be (4,8) but edge " + G.eItem() + " was is the current edge instead");

		G.eGoFirst(G.vertex(10));
		if( G.eItemExists() ) System.out.println("Error: there should be no current edge, but " + G.eItem() + "is the current edge.");
		
		
		// Testing eGoForth() and eAfter()
		G.eGoFirst(G.vertex(3));
		if( G.eItem().firstItem().index() != 3 || G.eItem().secondItem().index() != 5 ) System.out.println("Expected edge (3,5), but found " + G.eItem());
		G.eGoForth();
		if( G.eItem().firstItem().index() != 3 || G.eItem().secondItem().index() != 1 ) System.out.println("Expected edge (3,1), but found " + G.eItem());
		G.eGoForth();
		if( G.eItem().firstItem().index() != 3 || G.eItem().secondItem().index() != 2 ) System.out.println("Expected edge (3,2), but found " + G.eItem());
		G.eGoForth();
		if( !G.eAfter() ) System.out.println("Edge cursor should be 'after', but it is not.");
		
		if( !G.isAdjacent(1,1) ) System.out.println("Error: expected (1,1) to be adjacent but it wasn't.");
		if( !G.isAdjacent(2,3) ) System.out.println("Error: expected (2,3) to be adjacent but it wasn't.");
		if( !G.isAdjacent(6,4) ) System.out.println("Error: expected (6,4) to be adjacent but it wasn't.");
		if( !G.isAdjacent(1,2) ) System.out.println("Error: expected (1,2) to be adjacent but it wasn't.");
		if( G.isAdjacent(10, 2) ) System.out.println("Error: (10,2) should not be adjacent but they were.");
		if( G.isAdjacent(7, 4) ) System.out.println("Error: (7,4) should not be adjacent but they were.");
		if( G.isAdjacent(1, 7) ) System.out.println("Error: (1,7) should not be adjacent but they were.");
		
		// Test addEdge()
		G.addEdge(1, 7);
		if( !G.isAdjacent(1,7) ) System.out.println("Error: expected (1,7) to be adjacent but it wasn't.");
		G.addEdge(4, 2);
		if( !G.isAdjacent(4,2) ) System.out.println("Error: expected (4,2) to be adjacent but it wasn't.");
		G.addEdge(10, 5);
		if( !G.isAdjacent(10,5) ) System.out.println("Error: expected (10,5) to be adjacent but it wasn't.");
		G.addEdge(10, 5);
		if( !G.isAdjacent(10,5) ) System.out.println("Error: expected (10,5) to be adjacent but it wasn't.");

		
		System.out.println(G);

		// Testing eGoBefore/eGoForth
		G.goBefore();
		try {
			G.goForth();
			if( G.item().index() != 1) {
				System.out.println("Current vertex should be 1, but it is not. It is: "+G.item().index());
			}
		}
		catch(Exception e) {
			System.out.println("Error: failed to progress from 'before' position to first position via goForth().\n");
			e.printStackTrace();
		}
		
		
		//////// TEST DIRECTED GRAPH /////////////
		
		G = new GraphAdjListRep280<Vertex280, Edge280<Vertex280>>(10, true);

		G.initGraphFromFile("lib280-asn8/src/lib280/graph/testgraph.gra");
		
		System.out.println(G);	
		if( G.numEdges != 10 ) {
			System.out.println("Error: Graph should have 10 edges, but it has " + G.numEdges + ".");
		}
		
		// Testing eSearch()
		G.eSearch(G.vertex(6), G.vertex(4));
		if( !G.eItemExists()) System.out.println("Error: edge (6,4) was not found, but it should have been.");
		if( G.eItem().firstItem().index() != 6 || G.eItem().secondItem().index() != 4) 
			System.out.println("Error: should have located edge (6,4) but edge " + G.eItem() + " was located instead");
		
		// Testing deleteItem()
		G.deleteEItem();
		if(G.numEdges != 9) {
			System.out.println("Error: Graph should have 9 edges after deletion of [6,4] but it has " + G.numEdges + ".");
		}
		G.eSearch(G.vertex(6), G.vertex(4));	
		if( G.eItemExists() ) System.out.println("Error: edge (6,4) was found, but it should not have been.");
		System.out.println(G);	

		// Testing eGoFirst()
		G.eGoFirst(G.vertex(3));
		if( !G.eItemExists() ) System.out.println("Error: Current edge should be (3,5) but no edge was found.");
		if( G.eItem().firstItem().index() != 3 || G.eItem().secondItem().index() != 5) 
			System.out.println("Error: Current edge should be (3,5) but edge " + G.eItem() + " was is the current edge instead");

		G.eGoFirst(G.vertex(4));
		if( !G.eItemExists() ) System.out.println("Error: Current edge should be (4,8) but no edge was found.");
		if( G.eItem().firstItem().index() != 4 || G.eItem().secondItem().index() != 8) 
			System.out.println("Error: Current edge should be (4,8) but edge " + G.eItem() + " was is the current edge instead");

		G.eGoFirst(G.vertex(10));
		if( G.eItemExists() ) System.out.println("Error: there should be no current edge, but " + G.eItem() + "is the current edge.");
		
		
		// Testing eGoForth() and eAfter()
		G.eGoFirst(G.vertex(3));
		if( G.eItem().firstItem().index() != 3 || G.eItem().secondItem().index() != 5 ) System.out.println("Expected edge (3,5), but found " + G.eItem());
		G.eGoForth();
		if( G.eItem().firstItem().index() != 3 || G.eItem().secondItem().index() != 1 ) System.out.println("Expected edge (3,1), but found " + G.eItem());
		G.eGoForth();
		if( !G.eAfter() ) System.out.println("Edge cursor should be 'after', but it is not.");
		
		if( !G.isAdjacent(1,1) ) System.out.println("Error: expected (1,1) to be adjacent but it wasn't.");
		if( !G.isAdjacent(2,3) ) System.out.println("Error: expected (2,3) to be adjacent but it wasn't.");
		if( !G.isAdjacent(5,6) ) System.out.println("Error: expected (6,5) to be adjacent but it wasn't.");
		if( !G.isAdjacent(1,2) ) System.out.println("Error: expected (1,2) to be adjacent but it wasn't.");
		if( G.isAdjacent(10, 2) ) System.out.println("Error: (10,2) should not be adjacent but they were.");
		if( G.isAdjacent(7, 4) ) System.out.println("Error: (7,4) should not be adjacent but they were.");
		if( G.isAdjacent(1, 7) ) System.out.println("Error: (1,7) should not be adjacent but they were.");
		
		// Test addEdge()
		G.addEdge(1, 7);
		if( !G.isAdjacent(1,7) ) System.out.println("Error: expected (1,7) to be adjacent but it wasn't.");
		G.addEdge(4, 2);
		if( !G.isAdjacent(4,2) ) System.out.println("Error: expected (4,2) to be adjacent but it wasn't.");
		G.addEdge(10, 5);
		if( !G.isAdjacent(10,5) ) System.out.println("Error: expected (10,5) to be adjacent but it wasn't.");
		G.addEdge(10, 5);
		if( !G.isAdjacent(10,5) ) System.out.println("Error: expected (10,5) to be adjacent but it wasn't.");

		
		System.out.println(G);
		
		// test save/restore cursor position
		G.eGoFirst(G.vertex(1));
		G.eGoForth();
		G.goFirst();
		G.goForth();
		G.goForth();
		CursorPosition280 gPos = G.currentPosition();
		System.out.println("Current vertex: " + G.item().index());
		System.out.println("Current edge: " + G.eItem());
		
		G.goForth(); G.goForth(); G.eGoForth();
		System.out.println("Current vertex: " + G.item().index());
		System.out.println("Current edge: " + G.eItem());
		G.eGoFirst(G.vertex(9));
		System.out.println("Current vertex: " + G.item().index());
		System.out.println("Current edge: " + G.eItem());

		G.goPosition(gPos);
		System.out.println("Current vertex: " + G.item().index());
		System.out.println("Current edge: " + G.eItem());
		G.eGoForth();
		System.out.println("Current edge: " + G.eItem());
		
		// test Vertex cursor deleteItem
		System.out.println(G);
		G.goIndex(9);
		G.deleteItem();
		G.addVertex(9);
		G.addEdge(9,1);
		System.out.println(G);


		// Testing eGoBefore/eGoForth
		G.goBefore();
		try {
			G.goForth();
			if( G.item().index() != 1) {
				System.out.println("Current vertex should be 1, but it is not. It is: "+G.item().index());
			}
		}
		catch(Exception e) {
			System.out.println("Error: failed to progress from 'before' position to first position via goForth().\n");
			e.printStackTrace();
		}

		// test clear()
		G.clear();
		System.out.println(G);
		G.ensureVertices(5);
		System.out.println(G);
		
	}


}
