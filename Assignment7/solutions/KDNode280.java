package lib280.tree;


import lib280.base.NDPoint280;


// Make this class a specialization of BinaryNode280

public class KDNode280 extends BinaryNode280<NDPoint280> {

	public KDNode280(NDPoint280 x) {
		super(x);
	}
	
	public KDNode280(NDPoint280 x, KDNode280 left, KDNode280 right) {
		super(x);
		this.leftNode = left;
		this.rightNode = right;
	}

	@Override
	public KDNode280 leftNode() {
		return (KDNode280) this.leftNode;
	}
	
	@Override
	public KDNode280 rightNode() {
		return (KDNode280) this.rightNode;
	}
	
}


