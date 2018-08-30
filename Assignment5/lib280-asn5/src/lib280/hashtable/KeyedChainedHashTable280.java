package lib280.hashtable;

import lib280.base.CursorPosition280;
import lib280.base.Keyed280;
import lib280.base.Pair280;
import lib280.dictionary.HashTable280;
import lib280.dictionary.KeyedDict280;
import lib280.exception.*;
import lib280.list.LinkedIterator280;
import lib280.list.LinkedList280;

public class KeyedChainedHashTable280<K extends Comparable<? super K>, I extends Keyed280<K>> extends HashTable280<I> implements
		KeyedDict280<K, I> {

	/**	Array to store linked lists for separate chaining. */
	protected LinkedList280<I>[] hashArray;
  
	/**	Position of the current item in its list. */
	protected LinkedIterator280<I> itemListLocation;
	
	/** Default maximum load factor. */
	protected static final double defaultMaxLoadFactor = 1.5;
	
	/** Starting size of the hash table.	 */
	protected static final int defaultHashArrayLength = 32;
	 
	/** Actual maximum load factor for this instance */
	protected double maxLoadFactor;
	
	/**	
	 * Create a new hash list for a new chain.
	 * @timing  O(1)  
	 */
	protected LinkedList280<I> newChain()
	{
		return new LinkedList280<I>();
	}

	@SuppressWarnings("unchecked")
	public KeyedChainedHashTable280()
	{
		this.hashArray = new LinkedList280[KeyedChainedHashTable280.defaultHashArrayLength];
		this.count = 0;
		this.itemListLocation = null;
		this.maxLoadFactor = KeyedChainedHashTable280.defaultMaxLoadFactor;
	}

	@SuppressWarnings("unchecked")
	protected void expandHashArray() {
		LinkedList280<I>[] oldHashArray = this.hashArray;
		
		this.hashArray = new LinkedList280[this.capacity()*2];
		this.count = 0;
		for(int i = 0; i < oldHashArray.length; i++) {
			if( oldHashArray[i] != null && !oldHashArray[i].isEmpty() ) {
				oldHashArray[i].goFirst();
				while(oldHashArray[i].itemExists()) {
					this.insert(oldHashArray[i].item());
					oldHashArray[i].goForth();
				}
			}
		}
	}
	
	
	@Override
	public void insert(I x) throws ContainerFull280Exception,
			DuplicateItems280Exception {

		if( this.isFull() ) throw new ContainerFull280Exception("Error inserting item.  Table is full.");
		if( this.has(x.key()) ) throw new DuplicateItems280Exception("Error: table already has an item with key " + x.key());
		
		int itemHashLocation =  this.hashPos(x.key());
		if (hashArray[itemHashLocation]==null)
			hashArray[itemHashLocation] = newChain();
		hashArray[itemHashLocation].insert(x);
		count++;
		
		// Check if the load factor is too high, if it is, double the length of the hash
		// array and reinsert everything.
		if( this.loadFactor() > this.maxLoadFactor)
			this.expandHashArray();
	}

	@Override
	public void set(I x) throws ItemNotFound280Exception {
		if( !this.has(x.key()) ) {
			throw new ItemNotFound280Exception("No item with key x.key() was not found in the table.");
		}
		
		LinkedList280<I> L = hashArray[this.hashPos(x.key())];
		
		// We know that L contains an item with key x.key()
		L.goFirst();
		while(L.itemExists() && x.key().compareTo(L.item().key()) != 0) {
			L.goForth();
		}
		
		// Remove the old item
		L.deleteItem();
		
		// Add the new item
		L.insert(x);
		
	}

	@Override
	public boolean has(K k) {
		LinkedIterator280<I> saveListLocation;
		if(itemListLocation != null)
			saveListLocation = itemListLocation.clone();
		else
			saveListLocation = null;
		search(k);
		boolean result = itemExists();
		itemListLocation = saveListLocation;
		return result;
	}

	@Override
	public I obtain(K k) throws ItemNotFound280Exception {

		// Save cursor position
		LinkedIterator280<I> saveListLocation;
		if(itemListLocation != null)
			saveListLocation = itemListLocation.clone();
		else
			saveListLocation = null;
		
		// Look for the item with key k.
		search(k);
		
		// If it wasn't found, then error.
		if(!this.itemExists()) 
			throw new ItemNotFound280Exception("No item with the specified key exists in the table.");
		
		// Otherwise, restore the cursor position and return the item with key k.
		I result = this.item();
		itemListLocation = saveListLocation;

		return result;

	}

	@Override
	public void delete(K k) throws ItemNotFound280Exception {
		if( !this.has(k) ) {
			throw new ItemNotFound280Exception("No item with key k was not found in the table.");
		}
		
		LinkedList280<I> L = hashArray[this.hashPos(k)];
		
		// At this point, we know that L contains an item with key k
		L.goFirst();
		while(L.itemExists() && k.compareTo(L.item().key()) != 0) {
			L.goForth();
		}
		
		// Remove the existing item
		L.deleteItem();		
	}

	@Override
	public K itemKey() throws NoCurrentItem280Exception {
		if (!itemExists())
			throw new NoCurrentItem280Exception("There is no current item.");
		return itemListLocation.item().key();
	}

	@Override
	public Pair280<K, I> keyItemPair() throws NoCurrentItem280Exception {
		if (!itemExists())
			throw new NoCurrentItem280Exception("There is no current item.");

		return new Pair280<K,I>(itemListLocation.item().key(), itemListLocation.item());

	}

	@Override
	public I item() throws NoCurrentItem280Exception {
		if (!itemExists())
			throw new NoCurrentItem280Exception("Cannot return an item that does not exist.");
			
		return itemListLocation.item();
	}

	@Override
	public boolean itemExists() {
		return (itemListLocation!=null) && (itemListLocation.itemExists());
	}

	@Override
	public boolean before() {
		return (itemListLocation==null) || (itemListLocation.before());
	}

	@Override
	public boolean after() {
		return (itemListLocation!=null) && (itemListLocation.after());
	}

	
	/**	
	 * Go to the first item of the first non-empty list
	 * starting at index, or goAfter() if none found.  
	 * @timing O(capacity()) - worst case.
	 * @param index first hash value to search to find the next item 
	 */
	protected void findNextItem(int index)
	{
		int itemHashLocation = index;
		while ((itemHashLocation <= this.hashArray.length-1)
				&& ((this.hashArray[itemHashLocation] == null) || (this.hashArray[itemHashLocation].isEmpty())))
			itemHashLocation++;
		if (itemHashLocation < this.hashArray.length)
		{
			this.itemListLocation = this.hashArray[itemHashLocation].iterator();
			this.itemListLocation.goFirst();
		}
		else
			this.goAfter();
	}  

	
	@Override
	public void goForth() throws AfterTheEnd280Exception {
		if (this.after())
			throw new AfterTheEnd280Exception("Cannot goForth() when at the end of the table");

		if (this.itemListLocation==null || this.itemListLocation.before())
			this.goFirst();
		else
		{
			int itemHashLocation = this.hashPos(this.item().key());
			this.itemListLocation.goForth();
			if (this.itemListLocation.after())
				this.findNextItem(itemHashLocation + 1);
		}		
	}

	@Override
	public void goFirst() throws ContainerEmpty280Exception {
		findNextItem(0);
	}

	@Override
	public void goBefore() {
		itemListLocation.goBefore();
	}

	@Override
	public void goAfter() {
		if (hashArray[hashArray.length-1] == null)
			hashArray[hashArray.length-1] = newChain();
		itemListLocation = hashArray[hashArray.length-1].iterator();
		if( !hashArray[hashArray.length-1].isEmpty() ) 
			itemListLocation.goAfter();
	}

	@Override
	public CursorPosition280 currentPosition() {
		/* Return type is CursorPosition280 rather than LinkedIterator280<I> as the iterator
		   returned only iterates through one list rather than the whole container. */
		if (itemListLocation != null) 
			return itemListLocation.clone();
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void goPosition(CursorPosition280 pos) {
		if (pos != null  && !(pos instanceof LinkedIterator280))
			throw new InvalidArgument280Exception("The cursor position parameter" 
					    + " must be a LinkedIterator280<I>");
		if(pos != null) 
			itemListLocation = ((LinkedIterator280<I>) pos).clone();
		else
			itemListLocation = null;
		
	}

	/**
	 * Obtain the object matching y in the hash table.  This only succeeds if y is the
	 * exact object in the hash table, or the table contains an item that is 
	 * equal according to y's compareTo method (if it has one).  That is, if
	 * y is Comparable, than compareTo is used, otherwise reference comparison is used.
	 * 
	 * @precond y is in the hash table
	 * @throws ItemNotFound280Exception if y is not in the hash table.
	 * @return The matching object in the hash table if it exists.
	 * 
	 */
	public I obtain(I y) throws ItemNotFound280Exception {
		I existingItem = obtain(y.key());
		if( membershipEquals(existingItem, y) ) {
			return existingItem;
		}
		else {
			throw new ItemNotFound280Exception("The item in the table with key " + y.key() + " does not match y.");
		}
	}

	/**
	 * Delete the object matching y in the hash table.  This only succeeds if y is the
	 * exact object in the hash table, or the table contains an item that is 
	 * equal according to y's compareTo method (if it has one).  That is, if
	 * y is Comparable, than compareTo is used, otherwise reference comparison is used.
	 * 
	 * @precond y is in the hash table
	 * @throws ItemNotFound280Exception if y is not in the hash table.
	 * 
	 */
	public void delete(I y) throws ItemNotFound280Exception {
		I existingItem = this.obtain(y.key());
		if( this.membershipEquals(existingItem, y) ) {
			this.delete(existingItem.key());
		}
		else {
			throw new ItemNotFound280Exception("The item in the table with key " + y.key() + " does not match y.");
		}
		
		
	}

	/**
	 * Determine if there is an item  matching y in the hash table.  This only succeeds if y is the
	 * exact object in the hash table, or the table contains an item that is 
	 * equal according to y's compareTo method (if it has one).  That is, if
	 * y is Comparable, than compareTo is used, otherwise reference comparison is used.
	 * 
	 * @precond y is in the hash table
	 * @return true if y is in the hash table, false otherwise.
	 * 
	 */
	public boolean has(I y) {
		I existingItem;
		try {
			existingItem = this.obtain(y.key());
		}
		catch(ItemNotFound280Exception e) {
			return false;
		}

		return this.membershipEquals(existingItem, y);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean membershipEquals(I x, I y) {
		if ((x instanceof Comparable) && (y instanceof Comparable))
			return  0 == ((Comparable<I>) x).compareTo(y);
		else if (x.equals(y))
			return true;
		else 
			return false;
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
		this.hashArray = new LinkedList280[this.hashArray.length];
		this.count = 0;
		this.itemListLocation = null;
	}

	/**
	 * Place the cursor on the item in the hash table that matches y matching y.  
	 * This only succeeds if y is the
	 * exact object in the hash table, or the table contains an item that is 
	 * equal according to y's compareTo method (if it has one).  That is, if
	 * y is Comparable, than compareTo is used, otherwise reference comparison is used.
	 * 	  
	 */
	public void search(I y) {
		this.search(y.key());
		if( this.itemExists() ) {
			if( !this.membershipEquals(this.item(), y) ) 
				this.goAfter();
		}
	}

	@Override
	public void search(K k) {
		int itemHashLocation = this.hashPos(k);
		if (searchesContinue && itemListLocation!=null)
			goForth();
		else
		{
			if (hashArray[itemHashLocation]==null)
				hashArray[itemHashLocation] = newChain();
			itemListLocation = hashArray[itemHashLocation].iterator();
		}
		while (!itemListLocation.after() && k.compareTo(itemListLocation.item().key()) != 0 )
			itemListLocation.goForth();		
	}


	/**
	 * Searches the data structure for the first element with key at least as large as k.
	 * Searching begins from the first hash table bin, or the current bin if searches have been
	 * set to continue.
	 * 
	 * @param k The key value -- search will find items with key at least as large as k.
	 * 
	 */
	public void searchCeilingOf(K k) {
		
		int currentList=0;
		// If searches continue and we are currently at an item, move to the next one.
		if (searchesContinue && itemListLocation!=null) {
			currentList = this.hashPos(this.item().key());
			goForth();
		}
		else
		{
			// Otherwise start at the very first item in the first non-empty list.
			findNextItem(currentList);
		}
		while( currentList < hashArray.length  ) {
			while (!itemListLocation.after() ) {
				// If we find an item with key larger than k, return.
				if( itemListLocation.item().key().compareTo(k) >= 0 )
					return;
				itemListLocation.goForth();
			}
			// We reached the end of the current bucket, find the next item.
			currentList++;
			findNextItem(currentList);
		}
	}

	@Override
	public void deleteItem() throws NoCurrentItem280Exception {
		if( !this.itemExists() ) throw new NoCurrentItem280Exception("There is no current item to delete.");
		this.delete(this.item().key());
	}

	@Override
	public void setItem(I x) throws NoCurrentItem280Exception,
			InvalidArgument280Exception {
		if( !itemExists() ) {
			throw new NoCurrentItem280Exception("There is no current item to replace.");
		}
		if( x.key().compareTo(this.item().key()) != 0 ) {
			throw new InvalidArgument280Exception("The current item's key does not match that of x.");
		}
		
		this.delete(x.key());
		this.insert(x);
		
	}

	@Override
	public int capacity() {
		return hashArray.length;
	}

	
	
	@Override
	public int frequency(I i) {
		if( this.has(i.key())) return 1;
		else return 0;
	}

	@Override
	public String toString() {
		String result = "";
		for (int i=0; i<capacity(); i++)
			if (hashArray[i] != null)
				result += "\n" + i + ": " + hashArray[i].toString();
		return result;
	}
	
	public static void main( String args[] ) {
		
		class Skill implements Keyed280<String> {
			String name;
			private int skillCost;
			
			public Skill(String s, int c) {
				this.name = s;
				this.skillCost = c;
			}
			public String key() { return name; }
			
			public String toString() {
				return name + ":" + this.skillCost;
			}
			
		}
		
		KeyedChainedHashTable280<String, Skill> H = new KeyedChainedHashTable280<String, Skill>();
		
		// Test insert
		H.insert(new Skill("Shield Bash", 5));
		
		try {
			H.insert(new Skill("Shield Bash", 5));
			System.out.println("Error: expected duplicate items exception when inserting a duplicate item, but got none.");
		}
		catch(DuplicateItems280Exception e) {
			// Expected, do nothing.
		}
		
		

		// Test obtain(k)
		Skill existing;
		try {
			existing = H.obtain("Shield Bash");
		}
		catch(ItemNotFound280Exception e) {
			System.out.println("Error: caught unexpected exception when trying to obtain(\"Shield Bash\").");
		}
		finally {
			existing = H.obtain("Shield Bash");
		}
		
		try {
			System.out.println(H.obtain("Shield Crash"));
			System.out.println("Error: expected exception when trying to obtain(\"Shield Crash\"), got none.");
		}
		catch(ItemNotFound280Exception e) {
			
		}
		
		// testing obtain(x)
		try {
			H.obtain(existing);
		}
		catch(ItemNotFound280Exception e) {
			System.out.println("Error: unexpected exception when trying to obtain() an item that was just obtained by key.");
		}
		
		try {
			H.obtain(new Skill("Shield Bash", 5));
			System.out.println("Error: Expected exception when trying to obtain() an object not in the table, but got none.");
		}
		catch(ItemNotFound280Exception e) {
		}
		
		try {
			H.obtain(new Skill("Shield Crash", 5));
			System.out.println("Error: Expected exception when trying to obtain() an object not in the table, but got none.");
		}
		catch(ItemNotFound280Exception e) {
		}
		
		
		H.set(new Skill("Shield Bash", 77));
		if( H.obtain("Shield Bash").skillCost != 77 ) 
			System.out.println("Error: skill cost of Shield Bash should have been updated to 77 but it wasn't.");
		
		H.insert(new Skill("Ground Slam", 2));
		H.insert(new Skill("Heavy Strike", 5));
		H.insert(new Skill("Explosive Arrow", 11));
		H.insert(new Skill("Infernal Blow", 16));
		H.insert(new Skill("Lightning Strike", 6));
	
		// test delete(k)
		
		try {
			H.delete("Lightning Strike");
		}
		catch(ItemNotFound280Exception e) {
			System.out.println("Unexpected exception occured while deleting \"Lightning Strike\".");
		}
		
		try {
			H.delete("Ground Slam");
		}
		catch(ItemNotFound280Exception e) {
			System.out.println("Unexpected exception occured while deleting \"Lightning Strike\".");
		}
		
		try {
			H.delete("Fake Skill");
			System.out.println("Expected exception while deleting \"Fake Skill\" did not occur.");
		}
		catch(ItemNotFound280Exception e) {
		}
		
		
		// Test delete(x)
		
		Skill cleave = new Skill("Cleave", 1);
		Skill fakeCleave = new Skill("Cleave", 99);
		Skill sweep = new Skill("Sweep", 1);
		H.insert(cleave);
		
		try {
			H.delete(sweep);
			System.out.println("Expected exception while deleting sweep did not occur.");
		}
		catch(ItemNotFound280Exception e) {
			
		}
		
		try {
			H.delete(fakeCleave);
			System.out.println("Expected exception while deleting fakeCleave did not occur.");
		}
		catch(ItemNotFound280Exception e) {
			
		}
		
		try {
			H.delete(cleave);
		}
		catch(ItemNotFound280Exception e) {
			System.out.println("Unexpected exception while deleting cleave.");		
		}
		
		
		// test has(k)
		if( !H.has("Shield Bash") ) System.out.println("Error: has reports that table does not contain an item with key \"Sheild Bash\" but it does.");
		if( H.has("Shield Crash") ) System.out.println("Error: has reports that table contains an item with key \"Sheild Crash\" but it doesn't.");

		// test has(x)
		H.insert(cleave);
		if( !H.has(cleave) ) System.out.println("Error: has reports that table does not contain cleave object but it does.");
		if( H.has(fakeCleave) ) System.out.println("Error: has reports that table contains fakeCleave object but it doesn't.");
		if( H.has(sweep) ) System.out.println("Error: has reports that table contains sweep object but it doesn't.");

		// test search(x)
		H.search(cleave);
		if( !H.itemExists() ) 
				System.out.println("Error: search should have found cleave.");
		else 
			if( !H.membershipEquals(H.item(), cleave) )
				System.out.println("Error: cursor should be on cleave, but it isn't.");
		
		H.search(fakeCleave);
		if( H.itemExists() ) {
			System.out.println("Error: search should have failed, but it didn't.  It found: " + H.item());
			
		}
		
		// test search(k)
		
		H.search("Cleave");
		if( !H.itemExists() ) 
			System.out.println("Error: search should have found \"cleave\".");
		
		H.search("Unknown Skill");
		if( H.itemExists() ) {
			System.out.println("Error: search should have failed, but it didn't.  It found: " + H.item());
			
		}	
		

		// test deleteItem()
		try {
			H.deleteItem();
			System.out.println("Error: expected an exception calling deleteItem() when there is no current item.");
		}
		catch(NoCurrentItem280Exception e) {
		}
		
		H.search("Cleave");
		try {
			H.deleteItem();
		}
		catch(NoCurrentItem280Exception e) {
			System.out.println("Error: Tried to delete a valid current item, but got an unexpected exception.");
		}
		
		// Test setItem()
		// The current item should be cleave
		H.insert(cleave);
		H.search("Cleave");
		H.setItem(fakeCleave);
		try {
			if( H.obtain(fakeCleave.key()).skillCost != 99 )
				System.out.println("Error: replaced item should have cost 99, but it does not.");
		}
		catch(Exception e) {
			System.out.println("Error: unexpected exception.");
		}
		
		System.out.println(H);
		
		// test searchCeilingOf()
		H.restartSearches();
		H.searchCeilingOf("Icky");
		// cursor should stop on shield bash.
		if(!H.itemExists()) System.out.println("Error: cursor should be on Infernal Blow, but is not on any item.");
		else if (H.item().name.compareTo("Infernal Blow") != 0)
			System.out.println("Error: cursor shoud be on Infernal Blow, but it's on " + H.item().name + " instead.");
		
		H.searchCeilingOf("Puncture");
		// cursor should stop on heavy strike.
		if(!H.itemExists()) System.out.println("Error: cursor should be on Shield Bash, but is not on any item.");
		else if (H.item().name.compareTo("Shield Bash") != 0)
			System.out.println("Error: cursor should be on Shield Bash, but it's on " + H.item().name + " instead.");
	
		H.searchCeilingOf("Stab");
		// No item should be found.
		if(H.itemExists()) System.out.println("Error: cursor should not be on any item..");
	
		H.resumeSearches();
		H.goFirst();
		H.searchCeilingOf("Puncture");
		if(!H.itemExists()) System.out.println("Error: cursor should be on Shield Bash, but is not on any item.");
		else if (H.item().name.compareTo("Shield Bash") != 0)
			System.out.println("Error: cursor should be on Shield Bash, but it's on " + H.item().name + " instead.");

		H.searchCeilingOf("Consecrate");
		if(!H.itemExists()) System.out.println("Error: cursor should be on Explosive Arrow, but is not on any item.");
		else if (H.item().name.compareTo("Explosive Arrow") != 0)
			System.out.println("Error: cursor should be on Explosive Arrow, but it's on " + H.item().name + " instead.");
		
		H.searchCeilingOf("Dominating Blow");
		// No item should be found.
		if(H.itemExists()) System.out.println("Error: cursor should not be on any item..");
		
		
		// Use iterators to print the items forward.
		
		H.goFirst();
		while(H.itemExists()) {
			System.out.println(H.item());
			H.goForth();
		}
	
		
	
	}

	
	


}
