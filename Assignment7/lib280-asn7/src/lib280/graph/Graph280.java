package lib280.graph;

import lib280.base.Container280;
import lib280.exception.ContainerFull280Exception;
import lib280.exception.DuplicateItems280Exception;
import lib280.exception.InvalidArgument280Exception;
import lib280.exception.ItemNotFound280Exception;

/**
 * A graph which can accommodate custom edge and vertex objects. Vertices can be
 * referenced by an index. Indexes must be between 1 and some capacity n.
 * 
 * @author eramian
 * 
 * @param <V>
 *            The Vertex object type.
 * @param <E>
 *            The Edge object type.
 */
public abstract class Graph280<V extends Vertex280, E extends Edge280<V>>
		implements Container280 {

	//////////// Instance Variables //////////////
	
	/**
	 * Array that stores each vertex in the position corresponding to its index.
	 */
	protected V[] vertexArray;

	/** Is the graph directed? Defaults to undirected. */
	protected boolean directed = false;

	/** Number of vertices. */
	protected int numVertices;

	/** Number of edges */
	protected int numEdges;

	/** Type name for vertex objects */
	protected String edgeTypeName;

	/** Type name of edge objects */
	protected String vertexTypeName;

	// /////// ABSTRACT METHODS //////////////
	/**
	 * Determine whether two given node indexes are adjacent.
	 * 
	 * @param srcIdx
	 *            Index of a the first node.
	 * @param dstIdx
	 *            Index of the second node.
	 * @precond The vertices with the given indicies must exist.
	 * @return True if there is an edge from the node with index srcIdx to the node with index dstIdx.
	 * @throws ItemNotFound280Exception if either vertex with the given indices do not exist.
	 */
	public abstract boolean isAdjacent(int srcIdx, int dstIdx) throws ItemNotFound280Exception;
	
	
	/**
	 * Determine whether two given Vertex objects are adjacent.
	 * 
	 * @param v1 Source vertex.
	 * @param v2 Destination vertex.
	 * @return Returns true if there is an edge from v1 to v2, false otherwise.
	 */
	public abstract boolean isAdjacent(V v1, V v2);
	
	/**
	 * Add an edge from vertex v1 to vertex v2.  If the edge already exists, it is not added again.
	 * @param v1 Source vertex for new edge.
	 * @param v2 Destination vertex for new edge.
	 */
	public abstract void addEdge(V v1, V v2);

	/** Initialize the data structure to hold the edges of the graph. */
	protected abstract void createEdgeDataStructure();

	// /////// Internal Methods ///////////////

	/**
	 * Internal method for creating a new vertex object (using the stored vertex
	 * type name) with a specific index.  T
	 * 
	 * @param The
	 *            index of the new vertex.
	 */
	@SuppressWarnings("unchecked")
	protected V createNewVertex(int id) {
		
		// Note we can't just say "return new V(id)" because V is not known until runtime and is not allowed by Java.
		// thus we have to do it this way.
		
		@SuppressWarnings("rawtypes")
		Class[] CONSTRUCTOR_PARAMETERS = new Class[] {int.class};

		try {
			return (V) Class.forName(this.vertexTypeName).getDeclaredConstructor(CONSTRUCTOR_PARAMETERS).newInstance(id);
		} catch (Exception e) {
			throw new InvalidArgument280Exception(
					"Invalid argument--vertex type " 
							+ "in graph constructor, \nor arguments for vertex constructor."
							+ "\nRecall that the graph constructor must have a String parameter "
							+ "with the fully qualified name (specifying the package) for a "
							+ "vertex type, if it is not \"lib280.graph.Vertex280\"." 
							+ "Internal name currently is: " + this.vertexTypeName + "\n" 
							+ e.getMessage());
		}

	}

	/**
	 * Internal method for creating a new edge object (using the stored edge
	 * type name) between two vertices v1 and v2.
	 * 
	 * @param v1
	 *            Source vertex for the edge.
	 * @param v2
	 *            Destination vertex for the edge.
	 */
	@SuppressWarnings("unchecked")
	protected E createNewEdge(V v1, V v2) throws InvalidArgument280Exception {
		
		// Note we can't just say "return new E(v1, v2)" because E is not known until runtime and is not allowed by Java.
		// thus we have to do it this way.

		try {
			return (E) Class.forName(this.edgeTypeName)
					.getDeclaredConstructors()[0].newInstance(v1, v2);
		} catch (Exception e) {
			throw new InvalidArgument280Exception(
					"Invalid argument--edge type "
							+ "in graph constructor (qualified name of type E), "
							+ "\n or arguments for edge contructor (two vertices of type V)."
							+ "\nRecall that the graph constructor must have a String parameter "
							+ "with the fully qualified name (specifying the package) for an "
							+ "edge type, if it is not lib280.graph.Edge280.");
		}
	}

	/**
	 * Set the ith vertex of the graph.
	 * 
	 * @timing O(1)
	 * @param v
	 *            The vertex to stored as the ith vertex, when they are number 1
	 *            through n
	 * @param i
	 *            The index of the vertex, when they are number 1 through n
	 */
	protected void vertexArraySetItem(V v, int i) {
		/* Vertices 1 through n are stored in 0 through n - 1. */
		vertexArray[i - 1] = v;
	}


	// /////// Constructors /////////////
	/**
	 * Create a new graph where the vertices are Vertex280 objects and
	 * the edges are Edge280 objects.  For custom edges, and vertices
	 * use the alternate constructor and pass in the names of the
	 * desired edge and vertex classes as strings.
	 * 
	 * @param cap
	 *            Maximum number of nodes in the graph.
	 * @param d
	 *            True if the graph is to be directed, false otherwise.
	 */
	Graph280(int cap, boolean d) {
		this(cap, d, "lib280.graph.Vertex280", "lib280.graph.Edge280");
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
	@SuppressWarnings("unchecked")
	Graph280(int cap, boolean d, String vertexTypeName, String edgeTypeName) {
		this.vertexArray = (V[]) new Vertex280[cap];
		this.directed = d;

		this.vertexTypeName = vertexTypeName;
		this.edgeTypeName = edgeTypeName;

		this.createEdgeDataStructure();
	}

	// ////// Other Methods ///////////

	/**
	 * Number of vertices.
	 * 
	 * @timing O(1)
	 * @return The number of vertices in the graph.
	 */
	public int numVertices() {
		return this.numVertices;
	}

	/**
	 * Number of edges.
	 * 
	 * @timing O(1)
	 * @return The number of edges in the graph.
	 */
	public int numEdges() {
		return this.numEdges;
	}

	/**
	 * Is the graph directed?.
	 * 
	 * @timing O(1)
	 */
	public boolean directed() {
		return this.directed;
	}

	/**
	 * Maximum number of vertices for the graph.
	 * 
	 * @timing O(1)
	 */
	public int capacity() {
		return this.vertexArray.length;
	}
	
	/**
	 * Access the ith vertex of the graph.
	 * @timing O(1)
	 * 
	 * @param i
	 *            The index of the vertex to be obtained, when they are number 1
	 *            through n
	 * @precond i must be between 1 and this.capacity()
	 */
	public V vertex(int i) {
//		if( i < 1 || i > this.capacity() )
//			throw new InvalidArgument280Exception("The given vertex ID is not a valid vertex.");
//		
		/* Vertices 1 through n are stored in 0 through n - 1. */
		return vertexArray[i - 1];
	}

	public void addVertex(int idx) throws ContainerFull280Exception,
			DuplicateItems280Exception {
		if (this.isFull())
			throw new ContainerFull280Exception(
					"Cannot add another vertex since " + "graph is full.");
		if (this.vertex(idx) != null)
			throw new DuplicateItems280Exception("Cannot create vertex since "
					+ "index id is already used");

		V newItem = this.createNewVertex(idx);
		this.vertexArraySetItem(newItem, idx);
		this.numVertices++;
	}
	
	/**
	 * Adds all vertices with index between 1 and a specified index to the graph, preserving nodes
	 * that may already exist.
	 */
	public void ensureVertices(int maxIdx) {
		for(int i=1; i <= maxIdx; i++) {
			if( this.vertex(i) == null )
				this.addVertex(i);
		}
	}

	
	/**
	 * Adds an edge between two specified vertex indicies if the vertices exist.  If the edge already exists, do not add it.
	 * @param srcIdx Index of source vertex.
	 * @param dstIdx Index of destination vertex.
	 * @throws ItemNotFound280Exception
	 */
	public void addEdge(int srcIdx, int dstIdx) throws ItemNotFound280Exception {
		if( this.vertex(srcIdx) == null ) throw new ItemNotFound280Exception("Trying to addEdge() with non-existant source node.");
		if( this.vertex(dstIdx) == null ) throw new ItemNotFound280Exception("Trying to addEdge() with non-existant destination node.");
		
		this.addEdge( this.vertex(srcIdx), this.vertex(dstIdx) );
	}
	
	/**
	 * A shallow clone of this object.
	 * 
	 * @timing O(1)
	 */
	@SuppressWarnings("unchecked")
	public Graph280<V, E> clone() {
		try {
			return (Graph280<V, E>) super.clone();
		} catch (CloneNotSupportedException e) {
			// Should not occur: this is a Container280, which implements
			// Cloneable
			e.printStackTrace();
			return null;
		}
	}

		
	@Override
	public void clear() {
		numVertices = 0;
		numEdges = 0;
		for (int i = 1; i <= this.capacity(); i++)
			vertexArraySetItem(null, i);

	}

	@Override
	public boolean isEmpty() {
		return this.numVertices == 0;
	}

	@Override
	public boolean isFull() {
		return this.numVertices == this.capacity();
	}

}
