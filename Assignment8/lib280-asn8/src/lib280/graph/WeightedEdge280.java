package lib280.graph;


public class WeightedEdge280<V extends Vertex280> extends Edge280<V> implements Comparable<WeightedEdge280<V>>{

	/**
	 * Weight of this edge.
	 */
	protected double weight;
	
	public WeightedEdge280(V v1, V v2) {
		super(v1, v2);
	}

	/**
	 * @return Obtain the weight of this edge.
	 */
	public double getWeight() {
		return weight;
	}
	
	
	/**
	 * Set the weight of this edge.
	 * @param weight The new weight for the edge.
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public int compareTo(WeightedEdge280<V> o) {
		return Double.compare(this.weight, o.weight); 
	}
	
	
	

}
