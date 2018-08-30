/**
 * 
 */
package lib280.graph;

import lib280.base.CursorPosition280;
import lib280.base.CursorSaving280;
import lib280.base.LinearIterator280;
import lib280.exception.AfterTheEnd280Exception;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.ItemNotFound280Exception;
import lib280.exception.NoCurrentItem280Exception;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * A graph with a vertex cursor for iterating over vertices and an edge cursor
 * for iterating over all of the edges of a vetex.
 * 
 * @author eramian
 * 
 */
public abstract class GraphWithCursors280<V extends Vertex280, E extends Edge280<V>>
		extends Graph280<V, E> implements LinearIterator280<V>, CursorSaving280 {
	// ///////// Instance Variables /////////

	/** The current item at the vertex cursor. */
	protected V item;

	/** The index of the current item at the vertex cursor */
	protected int itemIndex;

	/**
	 * The current item at the edge cursor - used to iterate over edges incident
	 * on a vertex.
	 */
	protected E eItem;

	/** The index of the vertex over whose edges eItem is iterating. */
	protected int iterationIndex;

	/** The index of the adjacent vertex during edge iteration */
	protected int adjIndex;

	
	/**
	 * @param cap
	 * @param d
	 * @param vertexTypeName
	 * @param edgeTypeName
	 */

	// ///////// Constructors //////////////
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
	public GraphWithCursors280(int cap, boolean d, String vertexTypeName,
                               String edgeTypeName) {
		super(cap, d, vertexTypeName, edgeTypeName);
	}

	/**
	 * Create a new graph.
	 * 
	 * @param cap
	 *            Maximum number of nodes in the graph.
	 * @param d
	 *            True if the graph is to be directed, false otherwise.
	 */
	public GraphWithCursors280(int cap, boolean d) {
		super(cap, d);
	}

	// ///////// Abstract Methods ////////////

	/**
	 * Determine if there is an edge from vertex u to vertex v and locate the
	 * edge cursor there. If there is no such edge, the edge cursor is not
	 * located at any edge.
	 * 
	 * @param u
	 *            Source vertex of desired edge.
	 * @param v
	 *            Destination vertex of desired edge.
	 * 
	 */
	public abstract void eSearch(V u, V v);

	/**
	 * Delete the edge at the edge cursor and move to the next edge for the
	 * vertex over whose edges we are iterating.
	 */
	public abstract void deleteEItem();

	/**
	 * Set the edge cursor to the first edge incident on a given vertex (prepare
	 * to iterate over edges of vertex v)
	 * 
	 * @param v
	 *            Vertex over whose edges to iterate.
	 */
	public abstract void eGoFirst(V v);

	/**
	 * Go to the next edge of the edge list over which the edge cursor is
	 * iterating.
	 * 
	 * @precond !eAfter()
	 * @throws AfterTheEnd280Exception
	 */
	public abstract void eGoForth() throws AfterTheEnd280Exception;

	/**
	 * Are we after the end of the edge list being iterated?
	 * 
	 * @return true if the edge cursor is past the end of the edge list, false
	 *         otherwise.
	 */
	public abstract boolean eAfter();

	// ///////// Internal Methods ///////////
	/** Find the next nonempty location in the vertex array. */
	protected int nextNonNullVertexIndex() {
		int i = this.itemIndex() + 1;
		while (i <= this.capacity() && this.vertex(i) == null)
			i++;
		return i;
	}

	// ///////// Public Methods /////////////

	@Override
	public boolean after() {
		return this.itemIndex > this.capacity();
	}

	@Override
	public boolean before() {
		return itemIndex < 1;
	}

	@Override
	public void goAfter() {
		this.itemIndex = this.capacity() + 1;
		this.item = null;
	}

	@Override
	public void goBefore() {
		this.itemIndex = 0;
		this.item = null;
	}

	@Override
	public void goFirst() throws ContainerEmpty280Exception {
		this.itemIndex = 0;
		this.goIndex(this.nextNonNullVertexIndex());
		if(this.itemExists())
			this.item = this.vertex(this.itemIndex);
	}

	@Override
	public void goForth() throws AfterTheEnd280Exception {
		if (this.after())
			throw new AfterTheEnd280Exception(
					"Cannot advance the vertex cursor, it is already after the last vertex.");

		if(this.before())
			this.goFirst();
		else
			this.goIndex(this.nextNonNullVertexIndex());
	}

	@Override
	public V item() throws NoCurrentItem280Exception {
		if (!this.itemExists())
			throw new NoCurrentItem280Exception(
					"The vertex cursor is not at a vertex.");

		return this.item;
	}

	/**
	 * Get the current edge at the edge cursor.
	 * 
	 * @return The edge at the edge cursor.
	 */
	public E eItem() {
		if (!this.eItemExists())
			throw new NoCurrentItem280Exception(
					"The edge cursor is not at an edge.");

		return this.eItem;
	}

	@Override
	public boolean itemExists() {
		return this.item != null;
	}

	/**
	 * Check if there is the edge cursor is on an edge.
	 * 
	 * @return Returns true if the edge cursor is at an edge, false otherwise.
	 */
	public boolean eItemExists() {
		return this.eItem != null;
	}

	/**
	 * Obtain the index of the vertex at the vertex cursor.
	 * 
	 * @return The index of the vertex at the vertex cursor.
	 */
	public int itemIndex() {
		return this.itemIndex;
	}

	/**
	 * Position the vertex cursor at the specified vertex.
	 * 
	 * @param newVertex
	 *            The vertex at which the vertex cursor is to be positioned.
	 */
	public void goVertex(V newVertex) {
		this.item = newVertex;
		this.itemIndex = newVertex.index();
	}

	/**
	 * Position the vertex cursor at the vertex identified by the given index.
	 * 
	 * @param idx
	 *            Index of the vertex at which to position the cursor.
	 */
	public void goIndex(int idx) throws ItemNotFound280Exception {

		this.itemIndex = idx;
		if (idx >= 1 && idx <= this.capacity()) {
			if (this.vertex(idx) == null)
				throw new ItemNotFound280Exception(
						"Vertex with index idx does not exist.");
			this.item = this.vertex(idx);
		} else
			this.item = null;
	}

	public void deleteItem() throws NoCurrentItem280Exception {
		if (!this.itemExists())
			throw new NoCurrentItem280Exception(
					"There is no vertex at the cursor to delete.");

		CursorPosition280 savePosition = currentPosition();

		// Search the graph to delete any edges that are incident on the vertex
		// being deleted.
		V delItem = this.item;
		this.goFirst();
		while (!this.after()) {
			if (this.isAdjacent(delItem, this.item)) {
				this.eSearch(delItem, this.item);
				this.deleteEItem();
			}
			if (this.isAdjacent(this.item, delItem)) {
				this.eSearch(this.item, delItem);
				this.deleteEItem();
			}
			this.goForth();
		}

		// Restore the vertex cursor to the deleted vertex, remove the node from
		// the vertex array.
		this.goPosition(savePosition);
		this.vertexArraySetItem(null, this.itemIndex);
		this.goIndex(nextNonNullVertexIndex());
		this.numVertices--;
	}

	/**
	 * Obtain the vertex over whose edges the edge cursor is iterating.
	 * 
	 * @return The index of the vertex over whose edges the edge cursor is
	 *         iterating.
	 */
	public int eIterationIndex() {
		return this.iterationIndex;
	}

	/**
	 * Get the vertex at the other end of the current edge.
	 * 
	 * @precond this.eItemExists()
	 * @return The vertex at the other end of the current edge.
	 * @throws NoCurrentItem280Exception
	 */
	public V eItemAdjacentVertex() throws NoCurrentItem280Exception {
		if (!this.eItemExists())
			throw new NoCurrentItem280Exception(
					"There is no current edge: there is no adjacent vertex if there is no current edge.");

		return this.eItem.other(this.vertex(iterationIndex));
	}

	/**
	 * Obtain the index of the of the vertex adjacent to the current vertex
	 * along the current edge.
	 * 
	 * @precond this.eItemExists()
	 * @return The index of the vertex at the other end of the current edge.
	 * @throws NoCurrentItem280Exception
	 */
	public int eItemAdjacentIndex() throws NoCurrentItem280Exception {
		if (!this.eItemExists())
			throw new NoCurrentItem280Exception(
					"There is no current edge: there is no adjacent vertex if there is no current edge.");

		return this.eItem.other(this.vertex(iterationIndex)).index();
	}

	/**
	 * Replaces the current graph with a graph read from a data file.
	 * 
	 * File format is a sequence of integers. The first integer is the total
	 * number of nodes which will be numbered between 1 and n.
	 * 
	 * Remaining integers are treated as ordered pairs of (source, destination)
	 * indicies defining graph edges.
	 * 
	 * @param fileName
	 *            Name of the file from which to read the graph.
	 * @throws RuntimeException
	 *             if the file format is incorrect, or an edge appears more than
	 *             once in the input.
	 */
	@SuppressWarnings("unchecked")
	public void initGraphFromFile(String fileName) {

		// Erase the current graph.
		this.clear();

		// Try to open the data file.
		Scanner inFile = null;
		try {
			inFile = new Scanner(new File(fileName)); 
		} catch (IOException e) {
			throw new RuntimeException("Error opening the file with name "
					+ fileName);
		}

		if (!inFile.hasNextInt()) {
			inFile.close();
			throw new RuntimeException(
					"Did not find number of nodes, illegal file format or unexpected end of file.");
		}

		// Get the number of nodes in the graph.
		int numV = inFile.nextInt();

		// Add more capacity if needed.
		if (numV > this.capacity()) {
			this.vertexArray = (V[]) new Vertex280[numV];
			this.createEdgeDataStructure();
		}

		// Make sure all needed vertices exist. (this also updates
		// this.numVertices because it adds each vertex with this.addVertex())
		this.ensureVertices(numV);

		// Read the adjacency list until there are no more tokens available.
		while (inFile.hasNext()) {

			// Get source vertex. If there is a next token and it's not an
			// integer, issue error.
			if (!inFile.hasNextInt()) {
				inFile.close();
				throw new RuntimeException(
						"Illegal file format or unexpected end of file.");
			}
			int srcIdx = inFile.nextInt();

			// Get destination vertex. If there is a next token and it's not an
			// integer, issue error.
			if (!inFile.hasNextInt()) {
				inFile.close();
				throw new RuntimeException(
						"Illegal file format or unexpected end of file.");
			}
			int dstIdx = inFile.nextInt();

			if (this.isAdjacent(srcIdx, dstIdx)) {
				inFile.close();
				throw new RuntimeException("Edge (" + srcIdx + ", " + dstIdx
						+ ") appears multiple times in the data file.");
			}

			this.addEdge(srcIdx, dstIdx);
		}
		inFile.close();
	}

	/**
	 * String representation of the graph for output.
	 * 
	 * @timing = O(max(numVertices, numEdges))
	 */
	public String toString() {
		CursorPosition280 position = this.currentPosition();
		StringBuffer result = new StringBuffer();
		result.append(this.numVertices);
		if (directed)
			result.append("   directed");
		else
			result.append("   undirected");
		
		this.goFirst();
		if( !itemExists() ) {
			// Graph contains no nodes.
			result.append("\nThis graph contains zero nodes.");
			return new String(result);
		}
		
		while (!this.after()) {
			result.append("\n" + item() + " : ");
			this.eGoFirst(this.item());
			while (!this.eAfter()) {
				result.append(" " + this.eItem().toStringGraphIO(item()));
				this.eGoForth();
			}
			// result.append(" 0");
			this.goForth();
		}
		this.goPosition(position);
		return new String(result);
	}
}
