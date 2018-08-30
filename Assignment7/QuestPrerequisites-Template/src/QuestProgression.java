import lib280.base.Pair280;
import lib280.exception.InvalidState280Exception;
import lib280.graph.Edge280;
import lib280.graph.GraphMatrixRep280;
import lib280.graph.Vertex280;
import lib280.list.LinkedList280;
import lib280.tree.ArrayedHeap280;
import lib280.tree.ArrayedMinHeap280;
import lib280.tree.IterableArrayedHeap280;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class QuestProgression {
	
	// File format for quest data:
	// First line: Number of quests N
	// Next N lines consist of the following items, separated by commas:
	//     quest ID, quest name, quest area, quest XP
	//     (Quest ID's must be between 1 and N, but the line for each quest IDs may appear in any order).
	// Remaining lines consist of a comma separated pair of id's i and j where i and j are quest IDs indicating
	// that quest i must be done before quest j (i.e. that (i,j) is an edge in the quest graph).
	
	/**
	 * Read the quest data from a text file and build a graph of quest prerequisites.
	 * @param filename Filename from which to read quest data.
	 * @return A graph representing quest prerequisites.  If quest with id i must be done before a quest with id j, then there is an edge in the graph from vertex i to vertex j.
	 */
	public static GraphMatrixRep280<QuestVertex, Edge280<QuestVertex>> readQuestFile(String filename) {
		Scanner infile;
		
		// Attempt to open the input filename.
		try {
			infile = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			System.out.println("Error: Unable to open" + filename);
			e.printStackTrace();
			return null;
		}
		
		// Set the delimiters for parsing to commas, and vertical whitespace.
		infile.useDelimiter("[,\\v]");

		// Read the number of quests for which there is data.
		int numQuests = infile.nextInt();
		
		// read the quest data for each quest.
		LinkedList280<Quest> questList = new LinkedList280<Quest>();
		for(int i=0; i < numQuests; i++) {
			int qId = infile.nextInt();
			String qName = infile.next();
			String qArea = infile.next();
			int qXp = infile.nextInt();		
			questList.insertLast(new Quest(qId, qName, qArea, qXp));
		}
	
		// Make a graph with the vertices we created from the quest data.
		GraphMatrixRep280<QuestVertex, Edge280<QuestVertex>> questGraph = 
				new GraphMatrixRep280<QuestVertex, Edge280<QuestVertex>> (numQuests, true, "QuestVertex", "lib280.graph.Edge280");
		
		// Add enough vertices for all of our quests.
		questGraph.ensureVertices(numQuests);
		
		// Store each quest in a different vertex.  The quest with id i gets stored vertex i.
		questList.goFirst();
		while(questList.itemExists()) {
			questGraph.vertex(questList.item().id()).setQuest(questList.item());
			questList.goForth();
		}
		
		// Continue reading the input file for the quest prerequisite informaion and add an edge to the graph
		// for each prerequisite.
		while(infile.hasNext()) {
			questGraph.addEdge(infile.nextInt(), infile.nextInt());
		}
				
		infile.close();
		
		return questGraph;
	}
	

	/**
	 * Test whether vertex v has incoming edges or not
	 * @param G A graph.
	 * @param v The integer identifier of a node in G (corresponds to quest ID)
	 * @return Returns true if v has no incoming edges.  False otherwise.
	 */
	public static boolean hasNoIncomingEdges(GraphMatrixRep280<QuestVertex,Edge280<QuestVertex>> G, int v) {

		 int counter = 1;	// Counter
		 while (counter < G.numVertices() + 1) {	// While counter is greater than the number of vertices
			 // If the vertex at index counter is adjacent to the vertex at index v, it means that they must be connected,
			 // and Vertex at index counter is pointed at the ones in index v, meaning that it must've have an indegree of at least 1.
			 if (G.isAdjacent(G.vertex(counter), G.vertex(v))) {
				 return false;	// Return false.
		 }
		 	counter++;	// Increment counter by 1.
		 }
		return true; // If this line is reached, vertex v does not have an incoming edge (indegree = 0)
	}
	
	
	/**
	 * Perform a topological sort of the quests in the quest prerequisite graph G, with priority given
	 * to the highest experience value among the available quests.
	 * @param G The graph on which to perform a topological sort.
	 * @return A list of quests that is the result of the topological sort, that is, the order in which the quests should be done if we always pick the available quest with the largest XP reward first.
	 */
	@SuppressWarnings("unchecked")
	public static LinkedList280<Quest> questProgression(GraphMatrixRep280<QuestVertex,Edge280<QuestVertex>> G) throws InvalidState280Exception {

		LinkedList280<Quest> questList = new LinkedList280();	//  List containing topologically sorted vertices
		ArrayedHeap280<Quest> heapList = new ArrayedHeap280(G.numVertices()); // Heap that will contain Vertex wit indegree = 0
		// For loop to insert all the vertices in G that has an indegree of 1.
		for (int i = 1; i < G.numVertices() + 1; i++) {
			if (hasNoIncomingEdges(G, i)) {
				heapList.insert(G.vertex(i).quest());
			}
		}
		// While all the items stored in heap list hasn't been moved to the LinkedList to be returned
		while (!heapList.isEmpty()) {
			int deletedId = heapList.item().id;	// Store the id of the item to be deleted so we can access it in our Graph
			questList.insertLast(heapList.item());	// Insert the item that is on top of our heap list to our return list
			heapList.deleteItem();	// delete the item that was inserted
			// move graph cursor to the first item in our graph
			G.goFirst();
			while (G.itemExists()) {	// While item exists
				int counter = 1;	// Set counter to 1
				// for each node m that has an edge from n --> m (n is the node from our heap, and m is the node from the graph)
				while (counter < G.numVertices() + 1) {
					G.eSearch(G.vertex(deletedId), G.vertex(counter));	// Search for the edge that lies between m and n
					if (G.isAdjacent(G.vertex(deletedId), G.vertex(counter))) {	// If m and n are adjacent
						G.deleteEItem();	// Delete the edge that lies between them
						// If m doesn't have anymore incoming edge, add it to our heap
						if (hasNoIncomingEdges(G, counter)) {
							heapList.insert(G.vertex(counter).quest());
						}
					}
					counter++;	// increment counter by 1
				}
				G.goForth();	// Move to the next vertex in our graph
			}
		}
		// If the graph doesn't have any more edges, then return our linked list
		if (G.numEdges() == 0) {
			return questList;
		}
		// Otherwise, the tree had at least 1 cycle
		throw new InvalidState280Exception("The graph had at least one cycle!");

	}
	
	public static void main(String args[]) {
		// Read the quest data and construct the graph.
		
		// If you get an error reading the file here and you're using Eclipse, 
		// remove the 'QuestPrerequisites-Template/' portion of the filename.
		GraphMatrixRep280<QuestVertex,Edge280<QuestVertex>> questGraph = readQuestFile("QuestPrerequisites-Template/quests16.txt");
		
		// Perform a topological sort on the graph.
		LinkedList280<Quest> questListForMaxXp = questProgression(questGraph);
		
		// Display the quests to be completed in the order determined by the topologial sort.
		questListForMaxXp.goFirst();
		while(questListForMaxXp.itemExists()) {
			System.out.println(questListForMaxXp.item());
			questListForMaxXp.goForth();
		}
				
	}
}
