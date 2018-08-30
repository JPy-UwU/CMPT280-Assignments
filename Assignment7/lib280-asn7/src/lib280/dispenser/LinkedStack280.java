package lib280.dispenser;


import lib280.exception.ContainerEmpty280Exception;
import lib280.list.LinkedList280;

public class LinkedStack280<I> extends Stack280<I> {

	
	@Override
	public LinkedStack280<I> clone() throws CloneNotSupportedException {
		return (LinkedStack280<I>)super.clone();
	}


	public LinkedStack280() {
		stackItems = new LinkedList280<I>();
	}
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LinkedStack280<Integer> S = new LinkedStack280<Integer>();
		
		// Delete from empty stack.
		System.out.println("Popping from empty stack.");
		try {
			S.deleteItem();
			System.out.println("ERROR: exception should have been thrown, but wasn't.");			
		}
		catch( ContainerEmpty280Exception e ) {
			System.out.println("Caught exception. OK!");
		}		
		
		System.out.print("isFull() should be false....");
		if( !S.isFull() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		
		S.insert(1);
		System.out.println(S);

		S.insert(2);
		System.out.println(S);
		
		S.insert(3);
		System.out.println(S);
		
		S.insert(4);
		System.out.println(S);
	
		
		S.insert(5);
		System.out.println(S);

		System.out.print("isFull() should be false....");
		if( !S.isFull() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		
		
		System.out.println("             Stack should be: 5, 4, 3, 2, 1");
		System.out.println(S);

		System.out.print("itemExists() should be true...");
		if( S.itemExists() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		System.out.print("isEmpty() should be false...");
		if( !S.isEmpty()) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
	
		System.out.print("item() should be 5...");
		if( S.item() == 5) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		
		S.deleteItem();
		System.out.print("item() should be 4...");
		if( S.item() == 4) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		S.deleteItem();
		System.out.print("item() should be 3...");
		if( S.item() == 3) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		S.deleteItem();
		System.out.print("item() should be 2...");
		if( S.item() == 2) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		S.deleteItem();
		System.out.print("item() should be 1...");
		if( S.item() == 1) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		S.deleteItem();
		System.out.print("isEmpty() should be true...");
		if( S.isEmpty()) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
	
		S.insert(5);
		S.insert(4);
		S.clear();
		
		System.out.print("isEmpty() should be true...");
		if( S.isEmpty()) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		
	}

}
