package lib280.tree;

public class AVLTreeNode280<I extends Comparable<? super I>> extends BinaryNode280<I> {

	protected int ltHeight;
	protected int rtHeight;
	
	public AVLTreeNode280(I x) {
		super(x);
		ltHeight = 0;
		rtHeight = 0;
	}

	public int getLtHeight() {
		return ltHeight;
	}

	public void setLtHeight(int ltHeight) {
		this.ltHeight = ltHeight;
	}

	public int getRtHeight() {
		return rtHeight;
	}

	public void setRtHeight(int rtHeight) {
		this.rtHeight = rtHeight;
	}

	@Override
	public AVLTreeNode280<I> leftNode() {
		return (AVLTreeNode280<I>)super.leftNode();
	}
	
	@Override
	public AVLTreeNode280<I> rightNode() {
		return (AVLTreeNode280<I>) super.rightNode();
	}
}
