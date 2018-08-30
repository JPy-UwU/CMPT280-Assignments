package lib280.graph;

//import java.io.File;
//import java.io.IOException;
//import java.util.Scanner;

import lib280.base.Pair280;
import lib280.exception.InvalidArgument280Exception;

import java.util.InputMismatchException;
import java.util.Scanner;


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
     * fileName
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
        final Double inf = Double.POSITIVE_INFINITY;    // A representation of infinity for our tentative distances
        double[] tentativeDistance = new double[this.numVertices + 1];  // an array that stores all the tentative distances
        int[] predecessor = new int[this.numVertices + 1];  // int array to store the indexes of the predecessor of the parallel vertex
        boolean[] visited = new boolean[this.numVertices + 1];  // boolean to determine if the vertex is still yet to be visited or not

        // Initializes all the items in our set of arrays.
        for (int i = 0; i < visited.length; i++) {
            tentativeDistance[i] = inf; // Initialize all the tentative distances to infinity
            predecessor[i] = 0; // Set all the predecessor nodes to 0
            visited[i] = false; // Set all visited items to be false (there are no visited nodes initially).
        }

        tentativeDistance[startVertex] = 0; // Set the tentative distance at the starting vertex to be 0
        int visitCounter = 0;   // Visit counter for counting how many nodes we've visited so far
        while (visitCounter < visited.length) {
            // Finding the current node that carries the minimum tentative weight
            int idx = 1;    // Starting index
            // Skips each initial visited nodes.
            for (int i = 1 ; visited[i] && i < this.numVertices; i++) {
                idx++;  // incrementing the soon-be-current node
            }
            int cur = idx;  // Index of the current vertex to be inspected
            for (int i = cur; i <= this.numVertices() ; i++) {  //  For each vertex to the right of the current node
                if (!visited[i] && tentativeDistance[i] < tentativeDistance[cur]) { // If the node hasn't been visited and the tentative weight of i is less than the current node's
                    cur = i;    // Set the current node to be i
                }
            }

            visited[cur] = true;    // The node has been visited, therefore set it to true
            visitCounter++; // Increment one to our visit counter since we've visited a vertex

            // Update tentative distances for adjacent vertices if needed
            this.eGoFirst(this.vertex(cur));    // Go to the edge that holds the vertex on the current index
            while (this.eItemExists()) {    // while the item exists
                int z = eItem.secondItem().index(); // Get the 2nd item of the current edge
                if (!visited[z] && tentativeDistance[z] > tentativeDistance[cur] + eItem.getWeight()) { // If vertex z hasn't been visited, and the weight is greater than the current + the weight of the path
                    tentativeDistance[z] = tentativeDistance[cur] + eItem.getWeight();  // set the tentative distance of z to be the sum of the current weight and edge weight
                    predecessor[z] = cur;   // Set predecessor of z to be the current node
                }
                this.eGoForth();
            }
        }

        return new Pair280<>(tentativeDistance, predecessor);
    }

    // Given a predecessors array output from this.shortestPathDijkatra, return a string
    // that represents a path from the start node to the given destination vertex 'destVertex'.
    private static String extractPath(int[] predecessors, int destVertex) {
        String promptMessage = "The Path to " + destVertex + " is: ";   // Prompt message
        String result = Integer.toString(destVertex);   // Result string that stores the path. Initially contains destVertex
        if (predecessors[destVertex] == 0) {    // If the destination vertex is 0, it means that there is a cycle to itself.
            return "Not Reachable"; // Vertex is not reachable, or we got a negative weight?
        }
        int holder = destVertex;    // Initial counter
        while (predecessors[holder] > 0) {  // While the predecessor item at holder is not 0, there still exist a path
            holder = predecessors[holder];  // Set the holder to be the predecessor item
            result = holder + ", " + result;
        }
        return promptMessage + result;  // Return the results
    }

    // Regression Test
    public static void main(String args[]) {
        NonNegativeWeightedGraphAdjListRep280<Vertex280> G = new NonNegativeWeightedGraphAdjListRep280<Vertex280>(1, false);

        if( args.length == 0)
            G.initGraphFromFile("lib280-asn8/src/lib280/graph/weightedtestgraph.gra");
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
