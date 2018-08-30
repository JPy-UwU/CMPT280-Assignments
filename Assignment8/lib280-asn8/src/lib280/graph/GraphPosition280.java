/* GraphPosition280.java
 * -------------------------------------------------
 * Copyright (c) 2004 University of Saskatchewan 
 * All Rights Reserved
 * ------------------------------------------------- */

package lib280.graph;

import lib280.base.CursorPosition280;

/** The current position in a graph. */
public class GraphPosition280<V extends Vertex280, E extends Edge280<V>>
		implements CursorPosition280 {
	/** The index of the current vertex for searches and vertex iteration. */
	public int itemIndex;

	/** The index of the vertex being edge searched or iterated. */
	public int iterationIndex;

	/** The current vertex for a search or vertex iteration. */
	public V item;

	/** The current edge for a search or edge iteration. */
	public E eItem;

	/** The index of current adjacent vertex during edge iteration */
	public int adjIndex;
	
	/** Cursor position of current adjacency list (if used with adjacency list graph) */
	public CursorPosition280 edgeIterationPosition;

	/**
	 * Construct an object to store the current graph position. <br>
	 * Analysis: Time = O(1)
	 * 
	 * @param curItem
	 *            The current vertex
	 * @param curItemIndex
	 *            The index of the current vertex
	 * @param curIterationIndex
	 *            The index of the vertex whose edges are being iterated
	 * @param curEItem
	 *            The current edge
	 * @param curAdjIndex
	 *            The index of the vertex at the other end of the current edge
	 */
	public GraphPosition280(V curItem, int curItemIndex,
			int curIterationIndex, E curEItem, int curAdjIndex, CursorPosition280 edgeIterationPosition) {
		this.item = curItem;
		this.itemIndex = curItemIndex;
		this.iterationIndex = curIterationIndex;
		this.eItem = curEItem;
		this.adjIndex = curAdjIndex;
		this.edgeIterationPosition = edgeIterationPosition;
	}
}
