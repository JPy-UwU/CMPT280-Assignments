package lib280.tree;
import lib280.base.CursorPosition280;
import lib280.base.Keyed280;


public class TwoThreeTreePosition280<K extends Comparable<? super K>, I extends Keyed280<K>> implements CursorPosition280 {
	LinkedLeafTwoThreeNode280<K,I> cursor, prev;
	
	TwoThreeTreePosition280(LinkedLeafTwoThreeNode280<K,I> cursor, LinkedLeafTwoThreeNode280<K,I> prev) {
		this.cursor = cursor;
		this.prev = prev;
	}
}
