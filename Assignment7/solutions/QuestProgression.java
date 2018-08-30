import lib280.base.CursorPosition280;
import lib280.graph.Edge280;
import lib280.graph.GraphMatrixRep280;
import lib280.list.LinkedList280;
import lib280.tree.ArrayedHeap280;

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
		
		CursorPosition280 p = G.currentPosition();
		boolean hasNoEdge = true;
		for(int i=1; i <= G.numVertices() && hasNoEdge; i++) {
			G.eSearch(G.vertex(i), G.vertex(v));
			hasNoEdge = hasNoEdge && !G.eItemExists();
		}
		G.goPosition(p);
		
		return hasNoEdge;
	}
	
	
	/**
	 * Perform a topological sort of the quests in the quest prerequisite graph G, with priority given
	 * to the highest experience value among the available quests.
	 * @param G The graph on which to perform a topological sort.
	 * @return A list of quests that is the result of the topological sort, that is, the order in which the quests should be done if we always pick the available quest with the largest XP reward first.
	 */
	public static LinkedList280<Quest> questProgression(GraphMatrixRep280<QuestVertex,Edge280<QuestVertex>> G) {
		// Initialize output list
		LinkedList280<Quest> L = new LinkedList280<Quest>();
		
		// Initialize the heap of available quests (corresponding to nodes with no incoming edges)
		ArrayedHeap280<Quest> noIncomingEdgeNodes = new ArrayedHeap280<Quest>(G.numVertices());
		
		// Initialize the heap with quests whose corresponding nodes have no incoming edges.
		for(int i=1; i <= G.numVertices(); i++) {
			if(hasNoIncomingEdges(G, i))
				noIncomingEdgeNodes.insert(G.vertex(i).quest());			
		}
		
		// While there are quests whose corresponding nodes have no incoming edges...
		while(!noIncomingEdgeNodes.isEmpty()) {
			// Get from the heap the do-able quest with the highest XP reward and add it to the output
			Quest qCur = noIncomingEdgeNodes.item();
			noIncomingEdgeNodes.deleteItem();
			
			// Add it to the end of the ordered list of quests.
			L.insertLast(qCur);
			
			// Delete all of the outgoing edges from this quest's graph node.
			G.eGoFirst(G.vertex(qCur.id()));
			while(G.eItemExists()) {
				int targetVertex = G.eItemAdjacentIndex();
				G.deleteEItem();
				
				// If the destination node of the deleted edge no longer has any incoming edges,
				// added its quest to the heap of do-able quests.
				if( hasNoIncomingEdges(G, targetVertex) )
					noIncomingEdgeNodes.insert(G.vertex(targetVertex).quest());
			}
		}
		
		// If the graph has edges remaining, then return error (there was a cycle)
		if(G.numEdges() > 0 ) throw new RuntimeException("Error: there's a cycle in the graph.");
		
		return L;
	}
	
	public static void main(String args[]) {
		// Read the quest data and construct the graph.
		
		// If you get an error reading the file here and you're using Eclipse, 
		// remove the 'QuestPrerequisites-Template/' portion of the filename.
		GraphMatrixRep280<QuestVertex,Edge280<QuestVertex>> questGraph = readQuestFile("QuestPrerequisites/quests16.txt");
		
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
