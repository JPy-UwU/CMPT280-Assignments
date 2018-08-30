

import lib280.exception.InvalidState280Exception;
import lib280.graph.Edge280;
import lib280.graph.GraphAdjListRep280;


public class UnionFindEfficient280 {
	GraphAdjListRep280<UFVertex280, Edge280<UFVertex280>> G;
	
	/**
	 * Create a new union-find structure.
	 * 
	 * @param numElements Number of elements (numbered 1 through numElements, inclusive) in the set.
	 * @postcond The structure is initialized such that each element is in its own subset.
	 */
	public UnionFindEfficient280(int numElements) {
		G = new GraphAdjListRep280<UFVertex280, Edge280<UFVertex280>>(numElements, true, 
				"UFVertex280", "lib280.graph.Edge280");
		G.ensureVertices(numElements);		
	}
	
	/**
	 * Return the representative element (equivalence class) of a given element.
	 * @param id The elements whose equivalence class we wish to find.
	 * @return The representative element (equivalence class) of the element 'id'.
	 */
	public int find(int id) {
		
		// Safety check -- make sure there is only one outgoing edge.
		G.eGoFirst(G.vertex(id));
		if( G.eItemExists() ) {
			G.eGoForth();
			if(G.eItemExists()) throw new InvalidState280Exception("Error: element " + id + "has two parents.");
		}

		G.eGoFirst(G.vertex(id));
		int parent = id;
		if ( G.eItemExists() ) {
			parent = G.eItemAdjacentIndex();			
			G.deleteEItem();
			parent = find(parent);
			G.addEdge(id, parent);
		}
		
		return parent;
	}
	
	/**
	 * Merge the subsets containing two items, id1 and id2, making them, and all of the other elemnets in both sets, "equivalent".
	 * @param id1 First element.
	 * @param id2 Second element.
	 */
	public void union(int id1, int id2) {
		int class1 = this.find(id1);
		int class2 = this.find(id2);
		
		// Safety check -- class1 and class2 should not have parents
		G.eGoFirst(G.vertex(class1));
		if( G.eItemExists() ) throw new InvalidState280Exception("Error: representative element should not have a parent!");
		G.eGoFirst(G.vertex(class2));
		if( G.eItemExists() ) throw new InvalidState280Exception("Error: representative element should not have a parent!");

		// If the two elements are already in the same set, do nothing.
		if( class1 == class2 ) return;
		
		// If class2's tree is deeper than class 1, then make the root of
		// class 2 the parent of class 1.
		if( G.vertex(class1).rank() < G.vertex(class2).rank() ) {
			G.addEdge(class1, class2);
		}
		// If class 1's tree is deeper than class 2, then make the root of
		// class 1 the parent of class 2.
		else if( G.vertex(class1).rank() > G.vertex(class2).rank() ) { 
			G.addEdge(class2, class1);
		}
		// Otherwise, make class1 the parent of class 2 and increase class 1's rank (because it got one deeper)
		else {
			G.addEdge(class2, class1);
			G.vertex(class1).setRank(G.vertex(class1).rank() + 1);
		}
	}
	
	
	public static void main(String args[]) {
		int numElements=10;
		UnionFindEfficient280 UF = new UnionFindEfficient280(numElements);
		
		// Initially, each element should be its own set:
		for(int i=1; i <= numElements; i++) {
			if( UF.find(i) != i ) System.out.println("Error: find(" + i + ") should result in " + i + ", but it didn't." );
		}
		
		UF.union(2, 4);
		if( UF.find(4) != 2 ) System.out.println("Error: find(4) should result in 2, but it didn't." );
		if( UF.find(2) != 2 ) System.out.println("Error: find(2) should result in 2, but it didn't." );

		UF.union(3,2);
		if( UF.find(4) != 2 ) System.out.println("Error: find(4) should result in 2, but it didn't." );
		if( UF.find(3) != 2 ) System.out.println("Error: find(3) should result in 2, but it didn't." );
		if( UF.find(2) != 2 ) System.out.println("Error: find(2) should result in 2, but it didn't." );
		
		UF.union(10,9);
		if( UF.find(10) != 10 ) System.out.println("Error: find(10) should result in 10, but it didn't." );
		if( UF.find(9) != 10 ) System.out.println("Error: find(9) should result in 10, but it didn't." );
	
		UF.union(9, 2);
		if( UF.find(3) != 10 ) System.out.println("Error: find(3) should result in 10, but it didn't." );
		if( UF.find(4) != 10 ) System.out.println("Error: find(4) should result in 10, but it didn't." );
		
		UF.union(5,6);
		UF.union(7,1);
		UF.union(6,1);
		UF.union(1,2);
		if( UF.find(1) != 5 ) System.out.println("Error: find(1) should result in 5, but it didn't." );
		if( UF.find(2) != 5 ) System.out.println("Error: find(2) should result in 5, but it didn't." );
		
		
		System.out.println(UF.G);		
	}
	
}
