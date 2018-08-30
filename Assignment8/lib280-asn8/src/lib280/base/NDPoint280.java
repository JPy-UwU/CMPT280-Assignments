package lib280.base;

import lib280.exception.InvalidArgument280Exception;


public class NDPoint280 implements Comparable<NDPoint280> {
	protected Double coords[];
	protected int dim;
	
	/**
	 * Create a new N-dimensional point at the origin (0,0,..., 0).
	 * 
	 * @param dim Dimensionality of the point.
	 */
	public NDPoint280(int dim) {
		if( dim < 1 ) throw new IllegalArgumentException("NDPoint: dimension of point must be at least 1.");
		this.dim = dim;
		coords = new Double[dim];
	}
	
	/**
	 * Create a point from a coordinate in an array of Double.  The dimensionality of 
	 * the point is equal to the length of pt.
	 * 
	 * @param pt Coordinates of the new point.  
	 */
	public NDPoint280(Double pt[]) {
		this.dim = pt.length;
		if( this.dim < 1 ) throw new IllegalArgumentException("NDPoint: dimension of point must be at least 1.");
		
		coords = new Double[this.dim];
		this.copyArray(pt);
	}
	
	/**
	 * Create a point from a coordinate in array of double.  The dimensionality of
	 * the point is equal to the length of pt.
	 * 
	 * @param pt Coordinates of the new point.
	 */
	public NDPoint280(double pt[]) {
		this.dim = pt.length;
		if( this.dim < 1 ) throw new IllegalArgumentException("NDPoint: dimension of point must be at least 1.");
		
		coords = new Double[this.dim];
		this.copyArray(pt);
	}
	
	
	/**
	 * Use the coordinates in the given array for this point.
	 * @param pt New coordinates for this point.
	 * @precond pt must be equal to this.dim()
	 * @throws InvalidArgument280Exception if the length of pt is not equal to the the dimensionality of this N-dimensional point.
	 */
	protected void copyArray(Double pt[]) {
		if( this.dim() != pt.length ) throw new InvalidArgument280Exception("Array length must equal point dimensionality ("+this.dim+")");
		
		for(int i=0; i < this.dim; i++) {
			coords[i] = pt[i];
		}		
	}

	/**
	 * Use the coordinates in the given array for this point.
	 * @param pt New coordinates for this point.
	 * @precond pt must be equal to this.dim()
	 * @throws InvalidArgument280Exception if the length of pt is not equal to the the dimensionality of this N-dimensional point.
	 */
	protected void copyArray(double pt[]) {
		if( this.dim() != pt.length ) throw new InvalidArgument280Exception("Array length must equal point dimensionality ("+this.dim+")");

		for(int i=0; i < this.dim; i++) {
			coords[i] = pt[i];
		}		
	}

	
	/**
	 * Get the dimensionality of the point.
	 * @return The dimensionality of the point.
	 */
	public int dim() { return this.dim; }
	
	/**
	 * Set the coordinates of this point.
	 * 
	 * @param pt The point to store represented as an array of double.
	 * @precond Length of pt must be at least 1 and be equal to this.dim()
	 */
	public void setPoint(Double pt[]) {
		if( this.dim != pt.length) { 
			this.dim = pt.length;
			if( this.dim < 1 ) throw new IllegalArgumentException("NDPoint: dimension of point must be at least 1.");
			coords = new Double[this.dim];
		}
		this.copyArray(pt);
	}
	
	
	/**
	 * Obtain the i-th coordinate of the point.
	 * @param i Desired coordinate of the point.
	 * @precond i is less than the point's dimensionality
	 * @return The value of the i-th coordinate of the point.
	 * @throws InvalidArgument280Exception if i is not a valid dimension index for this n-diensional point (i.e. if i >= n)
	 */
	public double idx(int i) {
		if( i >= this.dim ) throw new InvalidArgument280Exception("Requested coordinate index exceeds point dimensionality.");
		return coords[i];
	}
	
	@Override
	public String toString() {
		String out = "(" + coords[0];
		for(int i=1; i < this.dim; i++) {
			out += ", " + coords[i];
			
		}
		out += ")";
		return out;
	}
	
	/**
	 * Compares the i-th coordinate of two NDPoint objects.
	 * @param i Index of the coordinate to compare.
	 * @param other The point to which to compare this point.
	 * @precond i is less than this point's dimensionality
	 * @precond this.dim() must equal other.dim()
	 * @return -1 if the i-th coordinate of this point is smaller than that of 'other';
	 *         0 if the i-th coordinate of this point and 'other' are equal; or
	 *         1 if the i-th coordinate of this point is greater than that of 'other'.
	 */
	public int compareByDim(int i, NDPoint280 other) {
		if( other.dim() != this.dim ) {
			throw new IllegalArgumentException("NDPoint: comparing two points of different dimension");
		}
		if( i >= this.dim ) {
			throw new IllegalArgumentException("NDPoint: comparing dimension: " + i + ", but point only has dimension " + this.dim);
		}
				
		return coords[i].compareTo(other.coords[i]);
	}

	/**
	 * Compares this point to 'o' based on the first non-equal dimension (i.e. lexographic ordering).
	 * @precond this.dim() must equal o.dim()
	 * @param o The point to which this point is to be compared.
	 */
	public int compareTo(NDPoint280 o) {
		if( this.dim != o.dim ) 
			throw new IllegalArgumentException("NDPoint: comparing two points of different dimension");
		
	
		for(int i=0; i < this.dim; i++) {
			if( this.coords[i].compareTo(o.coords[i]) != 0 ) {
				return this.coords[i].compareTo(o.coords[i]);
			}
		}
		
		return 0;
	}
	
	public static void main(String args[]) {
		// test first constructor, test dim()
		NDPoint280 p = new NDPoint280(5);
		if( p.dim() != 5) System.out.println("Newly created point should have dimension 5, but has dimension " + p.dim());
		
		// test second constructor, test dim()
		Double pt3d[] = {1.0, 2.0, 3.0};
		p = new NDPoint280(pt3d);
		if( p.dim() != 3) System.out.println("Newly created point should have dimension 3, but has dimension " + p.dim());
		if( p.idx(0) != 1.0 || p.idx(1) != 2.0 || p.idx(2) != 3.0 )
			System.out.println("Point should be (1.0, 2.0, 3.0) but it is: " + p);
		
		// test setPoint() and idx()
		Double newpt3d[] = {3.0, 2.0, 1.0};
		p.setPoint(newpt3d);
		if( p.idx(0) != 3.0 || p.idx(1) != 2.0 || p.idx(2) != 1.0 )
			System.out.println("Point should be (3.0, 2.0, 1.0) but it is: " + p);
		
		// test toString()
		String ptAsString = p.toString();
		if( ptAsString.compareTo("(3.0, 2.0, 1.0)") != 0 ) 
			System.out.println("Sting representation of point should be \"(3.0, 2.0, 1.0)\" but it is: \"" + ptAsString + "\"");
		
		// test compareTo() when the first coordinate is different
		NDPoint280 q = new NDPoint280(pt3d);
		if( p.compareTo(p) != 0 )
			System.out.println("The point is not equal to itself!");
		if( p.compareTo(q) != 1 ) 
			System.out.println("Point p should be greater than point q, but it isn't.");
		if( q.compareTo(p) != -1 ) 
			System.out.println("Point q should be less than point p, but it isn't.");
		
		// test compareTo() when the third coordinate is different
		Double anotherPoint[] = {1.0, 2.0, 4.0};
		p.setPoint(anotherPoint);
		if( p.compareTo(q) != 1 )
			System.out.println("Point p should be greater than point q, but it isn't.");
		if( q.compareTo(p) != -1 ) 
			System.out.println("Point q should be less than point p, but it isn't.");
		
		// test compareByDim()
		if( p.compareByDim(1,  q) != 0 ) 
			System.out.println("Coordinate 0 of p and q should be equal but they are reportedly not.");
		if( p.compareByDim(1,  q) != 0 ) 
			System.out.println("Coordinate 1 of p and q should be equal but they are reportedly not.");
		if( p.compareByDim(2,  q) != 1 ) 
			System.out.println("Coordinate 2 of p should be larger than coordinate 2 of q, but it is reportedly not.");
		
		System.out.println("Unit test complete.");
	}
	
}
