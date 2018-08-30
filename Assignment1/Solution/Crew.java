/**
 * This class stores information about one of Tractor Jack's pirate crew.
 */

public class Crew {
    /**
     * The crew member's rank
     */
    protected int rank;

    /**
     * The crew member's guaranteed annual pay (number golden guineas)
     */
    protected double pay;

    /**
     * The number of sacks of grain plundered by this crew member this year.
     */
    protected int grainSacks;

    public Crew(int rank, double pay, int grainSacks) {
        this.rank = rank;
        this.pay = pay;
        this.grainSacks = grainSacks;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }

    public int getGrainSacks() {
        return grainSacks;
    }

    public void setGrainSacks(int grainSacks) {
        this.grainSacks = grainSacks;
    }

    public String toString() {
        return "Rank: " + new Integer(this.rank) + ",  Pay: " + new Double(this.pay) + ", Plunder: " + new Integer(this.grainSacks);
    }
}
