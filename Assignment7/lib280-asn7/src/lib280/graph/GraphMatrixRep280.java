package lib280.graph;

/**
 * 
 */


import lib280.base.CursorPosition280;
import lib280.exception.AfterTheEnd280Exception;
import lib280.exception.InvalidArgument280Exception;
import lib280.exception.ItemNotFound280Exception;
import lib280.exception.NoCurrentItem280Exception;

/**
 * @author eramian
 * 
 */
public class GraphMatrixRep280<V extends Vertex280, E extends Edge280<V>>
		extends GraphWithCursors280<V, E> {

	// /////////// Instance variables ////////////

	/** The adjacency matrix 
	 *  Vertices 1 through n are stored in indices 0 through n-1 in this array.
	 * */
	protected E[][] adjMatrix;


	// /////////// Internal Methods /////////////
	
	/**
	 * Set the edge between the vertices with indices i and j.
	 */
	protected void adjMatrixSetEntry(E e, int i, int j) {
		/* Vertex indices 1 through n are mapped to 0 through n - 1. */
		adjMatrix[i - 1][j - 1] = e;
	}

	/**
	 * Next non null vertex after adjIndex
	 * 
	 * @return
	 */
	protected int nextNonNullAdjIndex() {
		int result = this.adjIndex + 1;
		while ((result <= this.capacity())
				&& (this.adjMatrixEntry(iterationIndex, result) == null)) {
			result++;
		}

		return result;
	}
	
	/**
	 * The edge between the vertices with indices i and j.
	 */
	protected E adjMatrixEntry(int i, int j) {
		/* Vertex indices 1 through n are mapped to 0 through n - 1. */
		return adjMatrix[i - 1][j - 1];
	}



	// ////////// Constructors //////////////

	/**
	 * Create a new graph.
	 * 
	 * @param cap
	 *            Maximum number of nodes in the graph.
	 * @param d
	 *            True if the graph is to be directed, false otherwise.
	 */
	public GraphMatrixRep280(int cap, boolean d) {
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
	public GraphMatrixRep280(int cap, boolean d, String vertexTypeName,
			String edgeTypeName) {
		super(cap, d, vertexTypeName, edgeTypeName);
	}

	
	
	//////////// Public Methods ///////////////
	
	@Override
	public CursorPosition280 currentPosition() {
		return new GraphPosition280<V, E>(this.item, this.itemIndex,
				this.iterationIndex, this.eItem, this.adjIndex, null);
	}

	
	
	@SuppressWarnings("unchecked")
	@Override
	public void goPosition(CursorPosition280 pos) {
		if (!(pos instanceof GraphPosition280))
			throw new InvalidArgument280Exception(
					"The cursor position must be an instance of GraphMatrixRepPosition280<V, E>");
		GraphPosition280<V, E> matrixPos = (GraphPosition280<V, E>) pos;
		this.item = matrixPos.item;
		this.itemIndex = matrixPos.itemIndex;
		this.iterationIndex = matrixPos.iterationIndex;
		this.eItem = matrixPos.eItem;
		this.adjIndex = matrixPos.adjIndex;
	}

	@Override
	public void deleteEItem() throws NoCurrentItem280Exception {
		// If there is no edge at the edge cursor, we can't delete an edge.
		if (!this.eItemExists())
			throw new NoCurrentItem280Exception(
					"Cannot delete an item that does not exist.");

		// Erase the edge
		this.adjMatrixSetEntry(null, this.eItem.firstItem().index(), this.eItem
				.secondItem().index());
		
		// If the graph is undirected, also erase the symmetric edge.
		if (!directed)
			this.adjMatrixSetEntry(null, this.eItem.secondItem().index(),
					this.eItem.firstItem().index());
		
		// Update edge count
		this.numEdges--;
		
		// Move the cursor to the next edge.
		this.eGoForth();
	}

	@Override
	public boolean eAfter() {
		return this.adjIndex > capacity();
	}

	@Override
	public void eGoFirst(V v) {
		// Record the vertex whose edges are about to be iterated
		this.iterationIndex = v.index();
		this.adjIndex = 0;
		
		// Move to the first edge.
		this.eGoForth();
	}

	@Override
	public void eGoForth() throws AfterTheEnd280Exception {
		if (this.eAfter())
			throw new AfterTheEnd280Exception(
					"Cannot go to next edge since already after.");

		// Find the next adjacent vertex
		this.adjIndex = this.nextNonNullAdjIndex();

		// If there wasn't another one, place the edge cursor in the 'after' position.
		if (this.eAfter()) {
			this.eItem = null;
		}
		else  // Otherwise, set the edge cursor to the next edge.
			this.eItem = this.adjMatrixEntry(this.iterationIndex, this.adjIndex);
	}

//	@Override
//	public int eItemAdjacentIndex() {
//		return this.adjIndex;
//	}

	@Override
	public void eSearch(V v1, V v2) {
		// Position the edge cursor at the specified edge.
		this.eItem = this.adjMatrixEntry(v1.index(), v2.index());
		this.iterationIndex = v1.index();
		this.adjIndex = v2.index();
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
		
		// Don't add an edge if it already exists.
		if( isAdjacent(v1, v2) ) return;
		
		// Otherwise, create a new edge object for this edge and put it in the adjacency matrix.
		this.adjMatrixSetEntry(this.createNewEdge(v1,v2), v1.index(), v2.index());
		
		// If the graph is undirected, create also the adjacent edge.
		if (!directed)
			this.adjMatrixSetEntry(this.createNewEdge(v2, v1), v2.index(), v1.index());
		
		// Update the number of edges.
		this.numEdges++;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void createEdgeDataStructure() {
		// Initialize the adjacency matrix.  NOTE: when creating the 2D array with 'new' we have to omit the 
		// type parameters because we don't actually know what class an edge is.
		this.adjIndex = 0;		
		this.adjMatrix = (E[][]) new Edge280[vertexArray.length][vertexArray.length];
	}

	@Override
	public boolean isAdjacent(int srcIdx, int dstIdx) throws ItemNotFound280Exception {
		return this.adjMatrixEntry(srcIdx, dstIdx) != null;
	}

	@Override
	public boolean isAdjacent(V v1, V v2) {
		return this.adjMatrixEntry(v1.index(), v2.index()) != null;
	}

	
	@Override
	public void clear() {
		super.clear();

		this.adjIndex = 0;
		for (int i = 1; i <= this.adjMatrix.length; i++)
			for (int j = 1; j <= this.adjMatrix.length; j++)
				this.adjMatrixSetEntry(null, i, j);
	}


	/**
	 * A shallow clone of this object. 
	 */
	public GraphMatrixRep280<V, E> clone() {
		return (GraphMatrixRep280<V, E>) super.clone();
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GraphMatrixRep280<Vertex280, Edge280<Vertex280>> G = new GraphMatrixRep280<Vertex280, Edge280<Vertex280>>(10, false);

		G.initGraphFromFile("lib280-asn7/src/lib280/graph/testgraph.gra");
		
		System.out.println(G);	
		
		if(G.numEdges != 10) {
			System.out.println("Error: Graph should have 10 edges but it has " + G.numEdges + ".");
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
		}G.eSearch(G.vertex(6), G.vertex(4));	
		if( G.eItemExists() ) System.out.println("Error: edge (6,4) was found, but it should not have been.");
		System.out.println(G);	

		// Testing eGoFirst()
		G.eGoFirst(G.vertex(3));
		if( !G.eItemExists() ) System.out.println("Error: Current edge should be (3,5) but no edge was found.");
		if( G.eItem().firstItem().index() != 3 || G.eItem().secondItem().index() != 1) 
			System.out.println("Error: Current edge should be (3,1) but edge " + G.eItem() + " was is the current edge instead");

		G.eGoFirst(G.vertex(4));
		if( !G.eItemExists() ) System.out.println("Error: Current edge should be (4,8) but no edge was found.");
		if( G.eItem().firstItem().index() != 4 || G.eItem().secondItem().index() != 8) 
			System.out.println("Error: Current edge should be (4,8) but edge " + G.eItem() + " was is the current edge instead");

		G.eGoFirst(G.vertex(10));
		if( G.eItemExists() ) System.out.println("Error: there should be no current edge, but " + G.eItem() + "is the current edge.");
		
		
		// Testing eGoForth() and eAfter()
		G.eGoFirst(G.vertex(3));
		if( G.eItem().firstItem().index() != 3 && G.eItem().secondItem().index() != 1 ) System.out.println("Expected edge (3,1), but found " + G.eItem());
		G.eGoForth();
		if( G.eItem().firstItem().index() != 3 && G.eItem().secondItem().index() != 2 ) System.out.println("Expected edge (3,2), but found " + G.eItem());
		G.eGoForth();
		if( G.eItem().firstItem().index() != 3 && G.eItem().secondItem().index() != 5 ) System.out.println("Expected edge (3,5), but found " + G.eItem());
		G.eGoForth();
		if( !G.eAfter() ) System.out.println("Edge cursor should be 'after', but it is not.");
		
		if( !G.isAdjacent(1,1) ) System.out.println("Error: expected (1,1) to be adjacent but it wasn't.");
		if( !G.isAdjacent(2,3) ) System.out.println("Error: expected (2,3) to be adjacent but it wasn't.");
		if( !G.isAdjacent(6,5) ) System.out.println("Error: expected (6,5) to be adjacent but it wasn't.");
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

		// Testing goBefore/goForth
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
		
		G = new GraphMatrixRep280<Vertex280, Edge280<Vertex280>>(10, true);

		G.initGraphFromFile("lib280-asn7/src/lib280/graph/testgraph.gra");
		
		System.out.println(G);	
		if(G.numEdges != 10) {
			System.out.println("Error: Graph should have 10 edges but it has " + G.numEdges + ".");
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
		}G.eSearch(G.vertex(6), G.vertex(4));	
		if( G.eItemExists() ) System.out.println("Error: edge (6,4) was found, but it should not have been.");
		System.out.println(G);	

		// Testing eGoFirst()
		G.eGoFirst(G.vertex(3));
		if( !G.eItemExists() ) System.out.println("Error: Current edge should be (3,1) but no edge was found.");
		if( G.eItem().firstItem().index() != 3 || G.eItem().secondItem().index() != 1) 
			System.out.println("Error: Current edge should be (3,1) but edge " + G.eItem() + " was is the current edge instead");

		G.eGoFirst(G.vertex(4));
		if( !G.eItemExists() ) System.out.println("Error: Current edge should be (4,8) but no edge was found.");
		if( G.eItem().firstItem().index() != 4 || G.eItem().secondItem().index() != 8) 
			System.out.println("Error: Current edge should be (4,8) but edge " + G.eItem() + " was is the current edge instead");

		G.eGoFirst(G.vertex(10));
		if( G.eItemExists() ) System.out.println("Error: there should be no current edge, but " + G.eItem() + "is the current edge.");
		
		
		
		// Testing eGoForth() and eAfter()
		G.eGoFirst(G.vertex(3));
		if( G.eItem().firstItem().index() != 3 || G.eItem().secondItem().index() != 1 ) System.out.println("Expected edge (3,1), but found " + G.eItem());
		G.eGoForth();
		if( G.eItem().firstItem().index() != 3 || G.eItem().secondItem().index() != 5 ) System.out.println("Expected edge (3,5), but found " + G.eItem());
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

		// Testing goBefore/goForth
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
