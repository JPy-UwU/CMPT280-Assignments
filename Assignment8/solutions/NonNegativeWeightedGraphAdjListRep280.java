 package lib280.graph;

//import java.io.File;
//import java.io.IOException;
//import java.util.Scanner;

import java.util.InputMismatchException;
import java.util.Scanner;

import lib280.base.Pair280;
import lib280.exception.InvalidArgument280Exception;
import lib280.graph.Vertex280;


public class NonNegativeWeightedGraphAdjListRep280<V extends Vertex280> extends
		WeightedGraphAdjListRep280<V> {

	public NonNegativeWeightedGraphAdjListRep280(int cap, boolean d,
			String vertexTypeName) {
		super(cap, d, vertexTypeName);
	}

	public NonNegativeWeightedGraphAdjListRep280(int cap, boolean d) {
		super(cap, d);
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
	 * @precond The weights on the edges in the data file fileName are non negative.
	 * @throws RuntimeException
	 *             if the file format is incorrect, or an edge appears more than
	 *             once in the input.
	 */
	
	
	@Override
	public void setEdgeWeight(V v1, V v2, double weight) {	
		// Overriding this method to throw an exception if a weight is negative will cause
		// super.initGraphFromFile to throw an exception when it tries to set a weight to
		// something negative.
		
		// Verify that the weight is non-negative
		if(weight < 0) throw new InvalidArgument280Exception("Specified weight is negative.");
		
		// If it is, then just set the edge weight using the superclass method.
		super.setEdgeWeight(v1, v2, weight);
	}
	
	@Override
	public void setEdgeWeight(int srcIdx, int dstIdx, double weight) {
		// Get the vetex objects associated with each index and pass off to the
		// version of setEdgeWEight that accepts vertex objects.
		this.setEdgeWeight(this.vertex(srcIdx), this.vertex(dstIdx), weight);
	}
	
	
	/**
	 * Implementation of Dijkstra's algorithm.
	 * @param startVertex Start vertex for the single-source shortest paths.
	 * @return An array of size G.numVertices()+1 in which offset k contains the shortest 
	 *         path from startVertex to k.  Offset 0 is unused since vertex indices start
	 *         at 1.
	 */
	public Pair280<double[], int[]> shortestPathDijkstra(int startVertex) {
		// visited[v] represents v.visisted in the given algorithm pseudocode.  
		boolean visited[] = new boolean[this.numVertices+1];
		
		// tentativeDistance[v] represents v.tentativeDistance in the given algorithm pseudocode.
		double tentativeDistance[] = new double[this.numVertices+1];
		
		// predecessorNode[v] represents v.predecessorNode in the given pseudocode.
		// predecessorNode[v] is the index of the vertex that that appears before v on the
		// shortest path from s to v.
		int predecessorNode[] = new int[this.numVertices+1];
				
		// This represents the number if nodes *not* in the set Q from the algorithm.  When this value
		// equals this.numVerticies, then Q is empty.  Here we are counting the number of nodes that
		// have been processed by Dijkstra's algorithm and for which we know the actual shortest path length
		// rather than just a temporary distance.
		double numProcessed = 0;
		
		// Initialize the shortest path distances -- initially we know nothing and set all
		// shortest path distances to "infinity".
		for(int i=0; i <= this.numVertices; i++) {
			tentativeDistance[i] = Double.MAX_VALUE;
			predecessorNode[i] = -1;
		}
				
		// The tentative distance to the start vertex is 0.
		tentativeDistance[startVertex] = 0;
		
		while(numProcessed <= this.numVertices) {
			
			// NOTE: My solution here is actually not very good -- i use a linear
			// search to find vertex with the smallest tentativeDistance.  Thus
			// the time complexity is not optimal.
			
			// Find the first unprocessed vertex (first vertex in Q).
			int i=1;
			while(visited[i] && i < this.numVertices) i++;
			int cur = i;
			
			// Find the next closest vertex using single-edge extensions.
			// (Find the vertex in Q which has the smallest tentative distance)
			// Note: closestIndex represents 'v' in the algorithm.
			while( i <= this.numVertices ) {
				if(!visited[i] && tentativeDistance[i] < tentativeDistance[cur]) {
					cur = i;
				}
				i++;
			}
			
			// Accept the tentative distance for closestIndex to be the actual shortest path
			// distance.  (Remove closestIndex from Q -- it is now "processed")
			visited[cur] = true;
			numProcessed++;
			
			// Find all vertices adjacent to closestIndex and update tentative distances.
			for(this.eGoFirst(this.vertexArray[cur-1]); !this.eAfter(); this.eGoForth()) {
				int z = this.eItem.secondItem().index();

				if(!visited[z] && tentativeDistance[z] > this.eItem.getWeight() + tentativeDistance[cur]) {
					tentativeDistance[z] = this.eItem.getWeight() + tentativeDistance[cur];
					predecessorNode[z] = cur;
				}
			}
			
		}
		
		return new Pair280<double[], int[]>(tentativeDistance, predecessorNode);
		
	}
	
	// Given a predecessors array output from this.shortestPathDijkatra, return a string
	// that represents a path from the start node to the given destination vertex 'destVertex'.
	private static String extractPath(int[] predecessors, int destVertex) {
		if( predecessors[destVertex] == -1 )
			return("Not reachable.");
		
		String result = "" + destVertex;
		int i = destVertex;			
		
		while(predecessors[i] > 0) {
			result = predecessors[i] + ", " + result;
			i = predecessors[i];
		}
		
		result = "The path to " + destVertex + " is: " + result;
		return result;
	}
	
	// Regression Test
	public static void main(String args[]) {
		NonNegativeWeightedGraphAdjListRep280<Vertex280> G = new NonNegativeWeightedGraphAdjListRep280<Vertex280>(1, false);

		if( args.length == 0) 
			G.initGraphFromFile("src/lib280/graph/weightedtestgraph.gra");
		else
			G.initGraphFromFile(args[0]);
		
		System.out.println("Enter the number of the start vertex: ");
		Scanner in = new Scanner(System.in);
		int startVertex;
		try {
			startVertex = in.nextInt();
		}
		catch(InputMismatchException e) {
			in.close();
			System.out.println("That's not an integer!");
			return;
		}
		
		if( startVertex < 1 || startVertex > G.numVertices() ) {
			in.close();
			System.out.println("That's not a valid vertex number for this graph.");
			return;
		}
		in.close();
		
		
		Pair280<double[], int[]> dijkstraResult = G.shortestPathDijkstra(startVertex); 
		double[] finalDistances = dijkstraResult.firstItem();
		//double correctDistances[] = {-1, 0.0, 1.0, 3.0, 23.0, 7.0, 16.0, 42.0, 31.0, 36.0};
		int[] predecessors = dijkstraResult.secondItem();
		
		for(int i=1; i < G.numVertices() +1; i++) {
			System.out.println("The length of the shortest path from vertex " + startVertex + " to vertex " + i + " is: " + finalDistances[i]);
//			if( correctDistances[i] != finalDistances[i] ) 
//				System.out.println("Length of path from to vertex " + i + " is incorrect; should be " + correctDistances[i] + ".");
//			else {
				System.out.println(extractPath(predecessors, i));
//			}
		}		
	}
	
}
