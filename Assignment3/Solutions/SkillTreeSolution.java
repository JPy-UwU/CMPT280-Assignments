import lib280.list.LinkedList280;
import lib280.tree.BasicMAryTree280;

public class SkillTreeSolution extends BasicMAryTree280<Skill> {

	/**	
	 * Create lib280.tree with the specified root node and
	 * specified maximum arity of nodes.  
	 * @timing O(1) 
	 * @param x item to set as the root node
	 * @param m number of children allowed for future nodes 
	 */
	public SkillTreeSolution(Skill x, int m)
	{
		super(x,m);
	}

	/**
	 * A convenience method that avoids typecasts.
	 * Obtains a subtree of the root.
	 * 
	 * @param i Index of the desired subtree of the root.
	 * @return the i-th subtree of the root.
	 */
	public SkillTreeSolution rootSubTree(int i) {
		return (SkillTreeSolution)super.rootSubtree(i);
	}
	
	
	/**
	 * Obtain the total skill point investment needed to
	 * get a given skill.
	 * @param skillName Name of a skill.
	 * @return The total skill point investment needed to obtain skillName
	 * @throws RuntimeException if skillName is not in the skill lib280.tree.
	 */
	public int skillTotalCost(String skillName) {
		LinkedList280<Skill> deps = new LinkedList280<Skill>();
		
		// If the skill isn't in the lib280.tree, then skillDependencies
		// will throw a RuntimeException.
		deps = skillDependencies(skillName);
		
		int totalCost = 0;
		deps.goFirst();
		while(deps.itemExists()) {
			totalCost += deps.item().getSkillCost();	
			deps.goForth();
		}
		return totalCost;
	}
	
	
	/**
	 * Return a list of skills that must be obtained before 
	 * you can use the specified skill 
	 * (note: this includes the specified skill!)
	 * 
	 * @param skillName - Skill name for which to obtain dependencies
	 * @return A list of the dependent skills (including skillName).
	 */
	public LinkedList280<Skill> skillDependencies(String skillName) {
		LinkedList280<Skill> deps = new LinkedList280<Skill>();
		if( skillDep(this, skillName, deps) )
			return deps;
		else throw new RuntimeException("That skill was not found!");
	}
	
	/**
	 * This is a recursive "helper" method that does the work for
	 * the skillDependencies() method.  It does a depth-first
	 * traversal of the lib280.tree until it finds the requested skill.
	 * If it returns true, it means that skillName was found in
	 * root, and that the caller should add the skill at the root
	 * of it's lib280.tree to the list.
	 * 
	 * @param root - lib280.tree to search for named skill
	 * @param skillName - skill for which we are searching for dependencies
	 * @param deps - List of skill dependencies found
	 * @return true if the named skill was found as an descendent of root,
	 *         false otherwise.
	 */
	protected boolean skillDep(SkillTreeSolution root, String skillName, LinkedList280<Skill> deps) {
		// Recursively search the lib280.tree for the named skill.
		if( root.isEmpty()) return false;
		
		if( root.rootItem().getSkillName().compareTo(skillName) == 0) {
			// Skill was found, so insert it into the list and return.
			deps.insert(root.rootItem());
			return true;
		}
		
		// Otherwise, check each nonempty subtree.
		for(int i = 1; i <= rootLastNonEmptyChild(); i++) {
			// Recursively search each child.
			// If the search found the skill, then it means that
			// this node is an ancestor of that skill, and therefore
			// is a dependency, so add it to the list and return.
			if( skillDep(root.rootSubTree(i), skillName, deps) ) {
				deps.insert(root.rootItem());
				return true;
			}
		}
		
		// If the recursive search from every subtree returned false,
		// then this node is not an ancestor of the named skill, so
		// return false.
		return false;
	}
	
	
	public static void main(String[] args) {
		System.out.println("My Skill Tree: ");
		
		// Define skills
		Skill ShieldBash = new Skill("Shield Bash", "Hit someone with your shield.", 1);
		Skill Slash = new Skill("Slash", "A standard sword attack.", 1);
		Skill MightyBlow = new Skill("Mighty Blow", "An all-out attack that leaves you defenceless for a moment.", 2);
		Skill Cleave  = new Skill("Cleave", "Attack multiple targets in an arc in front of you.", 2);
		Skill ShieldCharge = new Skill("Shield Charge", "Run at your target and knock them backwards with your shield.", 2);
		Skill ShieldWall = new Skill("Shield Wall", "Passive skill that grants additional protection to nearby allies.", 4);
		Skill Whirldwind = new Skill("Whirlwind", "Spin in place, attacking all adjacent targets.", 3);
		Skill Mobility = new Skill("Mobility", "A passive skill that increases your movement rate.", 1);
		Skill Parry = new Skill("Parry", "Chance to deflect melee attacks.", 2);
		Skill Berzerk = new Skill("Berzerk", "Massive bonus to attack strength and attack speed and great reduction in defence for 10 seconds.", 5);
	
		
		// Set up the root and it's children.
		SkillTreeSolution T = new SkillTreeSolution(Slash, 4);
		T.setRootSubtree(new SkillTreeSolution(MightyBlow, 4), 1);
		T.setRootSubtree(new SkillTreeSolution(ShieldBash, 4), 2);
		T.setRootSubtree(new SkillTreeSolution(Cleave, 4), 3);
		T.setRootSubtree(new SkillTreeSolution(Mobility, 4), 4);
		
		// Get the node for ShieldBash and set up its children
		SkillTreeSolution temp = T.rootSubTree(2);
		temp.setRootSubtree(new SkillTreeSolution(ShieldCharge, 4), 1);
		temp.setRootSubtree(new SkillTreeSolution(Parry, 4), 2);
		
		SkillTreeSolution temp2 = temp.rootSubTree(2);
		temp2.setRootSubtree(new SkillTreeSolution(ShieldWall,  4), 1);
		
		// Get the node for Cleave and set up its children
		temp = T.rootSubTree(3);
		temp.setRootSubtree(new SkillTreeSolution(Whirldwind, 4), 1);
		
		temp = temp.rootSubTree(1);
		temp.setRootSubtree(new SkillTreeSolution(Berzerk, 4), 1);
		
		System.out.println(T.toStringByLevel());
		
		// Search for skill dependencies for "Shield Wall"
		System.out.println("Dependencies for Shield Wall: ");
		System.out.println(T.skillDependencies("Shield Wall"));
		
		System.out.println("Dependencies for Mobility: ");
		System.out.println(T.skillDependencies("Mobility"));
	
		System.out.println("Dependencies for Slash: ");
		System.out.println(T.skillDependencies("Slash"));
		
		try {
			System.out.println("Dependencies for FakeSkill: ");
			System.out.println(T.skillDependencies("FakeSkill"));
		}
		catch( RuntimeException e) {
			System.out.println("FakeSkill not found.");
		}

		int whirlwindCost = T.skillTotalCost("Whirlwind");
		System.out.println("To get Whirlwind you must invest " + whirlwindCost + " points." );
		
		int mightyblowCost = T.skillTotalCost("Mighty Blow");
		System.out.println("To get Mighty Blow you must invest " + mightyblowCost + " points." );

		int fakeskillCost=0;
		try {
			fakeskillCost = T.skillTotalCost("FakeSkill");
			System.out.println("To get Fake Skill you must invest " + fakeskillCost + " points." );
		}
		catch (RuntimeException e) {
			System.out.println("FakeSkill not found.");
		}	
		
	}

}
