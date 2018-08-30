// Mark Cantuba
// MJC862
// 11214496

import lib280.list.LinkedList280;
import lib280.tree.BasicMAryTree280;
import lib280.tree.MAryNode280;

import javax.security.auth.login.FailedLoginException;

public class SkillTree extends BasicMAryTree280<Skill> {

	/**	
	 * Create lib280.tree with the specified root node and
	 * specified maximum arity of nodes.  
	 * @timing O(1) 
	 * @param x item to set as the root node
	 * @param m number of children allowed for future nodes 
	 */
	public SkillTree(Skill x, int m)
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
	public SkillTree rootSubTree(int i) {
		return (SkillTree)super.rootSubtree(i);
	}


	/**
	 * This function returns the skill dependencies for the given skillName
	 * @param skillName: name of the skill
	 * @return: a Linked List containing the skillName's dependencies
	 * @throws RuntimeException: The tree must contain skill with the given skillName
	 */
	public LinkedList280<Skill> skillDependencies(String skillName) throws RuntimeException {
		LinkedList280<Skill> dependList = new LinkedList280<>();
		skillDependencies(skillName, this.rootNode, dependList);
		if (dependList.isEmpty()) {
			throw new RuntimeException("Skill not found!");
		}
		return dependList;
	}

	/**
	 * Helper method that allows the encapsulation of the node data.
	 * This method traverses through the given skillTree, and add the dependency of the given skill name to the list
	 * @param skillName: Name of the skill
	 * @param skill: current node being checked
	 * @param myList: container for the skillName's dependencies
	 * @return: returns the current node
	 */
	private MAryNode280<Skill> skillDependencies(String skillName, MAryNode280<Skill> skill, LinkedList280<Skill> myList) {

		// Loop for the depth first traversal
		for (int i = 1 ; i<skill.count(); i++) {
			// If the recursive step returns a node, insert current item into the Linked List, then return the node
			if (skillDependencies(skillName, skill.subnode(i), myList) != null) {
				myList.insert(skill.item());
				return skill;
			}
		}
		// If the item being searched for is found, insert that item to the list, and return the current node
		if (skill.item().getSkillName().equals(skillName)) {
			myList.insertLast(skill.item());
			return skill;
		}
		return null;
	}

	/**
	 * Calculates the total required skill points to obtain skill given.
	 * @param skillName: The name of the skill
	 * @throws RuntimeException: Throws a runtime exception if the item is not found
	 * @return: return an integer giving the total skill count value for the skill
	 */
    public int skillTotalCost(String skillName) throws RuntimeException {
		LinkedList280<Skill> list = skillDependencies(skillName); // get the list of dependencies obtained from skillDependencies
		// if the list is empty, it means the item wasn't found, then throw an exception
		if (list.isEmpty()) {
			throw new RuntimeException("skillName given doesn't exist!");
		}
		// Loop through the entire list, get each skill cost, and add it to total cost.
		int totalCost = 0;
		list.goFirst();
		while (list.itemExists()) {
			totalCost += list.item().getSkillCost();
			list.goForth();
		}
	    return totalCost; // return the total skill count for the tree.
    }




	public static void main(String[] args) {
		System.out.println("***BATTLE MAGE SKILL TREE***");
		SkillTree basicMelee = new SkillTree(new Skill("Smash!", "Strikes enemy with a mighty blow!",
				1), 6);

		SkillTree Enrage = new SkillTree(new Skill("Enrage", "Unleashes your fury, granting +50 atk dmg" +
				" for 10 seconds, but increases vulnerability by 20%.", 2), 1);

		SkillTree OmniSlash = new SkillTree(new Skill("OmniSlash", "5 combo slash attack, with a chance of " +
				" inflicting 2 burn damage overtime.", 4), 3);
		SkillTree HellSlash = new SkillTree(new Skill("HellSlash", "Deals melee/fire damage to the enemy, " +
				" and inflict 6 burn damage overtime.", 6), 3);
		SkillTree MeteorFall = new SkillTree(new Skill("MeteorFall", "Summons a meteor, dealing heavy area " +
				"of effect damage to all surrounding enemies within 30 yards", 6), 0);
		SkillTree Ultima = new SkillTree(new Skill("Ultima", "Unleashes your most powerful attack, causing " +
				" 1500 damage to all enemies.", 100), 0);
		SkillTree Apocalypse = new SkillTree(new Skill("Apocalypse", "Destroys all enemies that are lower leveled " +
				" than your character, within 50 yards.", 20), 0);
		OmniSlash.setRootSubtree(Ultima, 1);
		HellSlash.setRootSubtree(MeteorFall, 1);
		HellSlash.setRootSubtree(Apocalypse, 2);
		OmniSlash.setRootSubtree(HellSlash, 2);


		SkillTree Thunder = new SkillTree(new Skill("Thunder", "Cast a single target lightning spell dealing " +
				" 3 damage every 2s.", 2), 2);
		SkillTree LightningRod = new SkillTree(new Skill("LightningRod", "Empowers weapon with a powerful " +
				" static charge, granting chance to inflict paralysis to enemy for 10s.", 5), 2);
		SkillTree Zeus = new SkillTree(new Skill("Zeus", "Summon god of thunder, Zeus, empowering all your " +
				" lightning abilities significantly for 1min., and dealing heavy AoE damage", 20), 0);
		LightningRod.setRootSubtree(Zeus, 1);
		Thunder.setRootSubtree(LightningRod, 1);


		SkillTree Blizzard = new SkillTree(new Skill("Blizzard", "Hurl a ball of ice towards enemy dealing " +
				" 10 frost damage", 3), 2);
		SkillTree FrostBlade = new SkillTree(new Skill("FrostBlade", "Empowers weapon with ice magic, granting " +
				" +10 extra frost damage to each melee attack.", 4), 2);
		SkillTree SnowFall = new SkillTree(new Skill("SnowFall", "Freezes all enemies and inflicts frost bite " +
				" to all enemies within 40 yards", 18), 0);
		FrostBlade.setRootSubtree(SnowFall, 1);
		Blizzard.setRootSubtree(FrostBlade, 1);


		SkillTree Fire = new SkillTree(new Skill("Fire", "Cast and hurl a fireball towards enemy, dealing  " +
				" 10 fire damage depending on level.", 3), 2);
		SkillTree Ifrit = new SkillTree(new Skill("Ifrit", "Summon hell demon, Ifrit, dealing heavy AoE fire " +
				" damage to all enemies, grants significant fire spell power and grants fire shield for 1min.", 30), 0);
		Fire.setRootSubtree(Ifrit, 1);

		basicMelee.setRootSubtree(Enrage,1);
		basicMelee.setRootSubtree(OmniSlash,2);
		basicMelee.setRootSubtree(Thunder,3);
		basicMelee.setRootSubtree(Blizzard,4);
		basicMelee.setRootSubtree(Fire,5);

		// Printing my skill tree using toStringByLevel.
		System.out.println(basicMelee.toStringByLevel());

		// Testing skillDependencies method
		System.out.println("❆❆❆ Dependencies for Shiva ❆❆❆");
		try {
			System.out.println(basicMelee.skillDependencies("Shiva"));
		} catch (RuntimeException r) {
			System.out.println("Shiva not Found!");
		}

		System.out.println("♞♞♞ Dependencies for Smash! ♞♞♞");
		try {
			System.out.println(basicMelee.skillDependencies("Smash!"));
		} catch (RuntimeException r) {
			System.out.println("Smash should exist!");
		}

		System.out.println("♛♛♛ Dependencies for Apocalypse ♛♛♛");
		try {
			System.out.println(basicMelee.skillDependencies("Apocalypse"));
		} catch (RuntimeException r) {
			System.out.println("Apocalypse exist in the tree!");
		}

		System.out.println("♚♚♚ Dependencies for SnowFall ♚♚♚");
		try {
			System.out.println(basicMelee.skillDependencies("SnowFall"));
		} catch (RuntimeException r) {
			System.out.println("SnowFall should exist in the tree!");
		}

		System.out.println("♟♟♟ Dependencies for LightningRod ♟♟♟");
		try {
			System.out.println(basicMelee.skillDependencies("LightningRod"));
		} catch (RuntimeException r) {
			System.out.println("LightningRod should exist in the tree!");
		}

		System.out.println("\n❆❆❆ Skill Cost for Shiva ❆❆❆");
		try {
			System.out.println(basicMelee.skillTotalCost("Shiva"));
		} catch (RuntimeException r) {
			System.out.println("Shiva not Found!");
		}
		System.out.println("♞♞♞ Skill Cost for Smash! ♞♞♞");
		try {
			if (basicMelee.skillTotalCost("Smash!") != 1) {
				System.out.println("Total cost expected for Smash! is 1");
			}
			System.out.println("You need a total of " + basicMelee.skillTotalCost("Smash!") + " SP to unlock Smash!");
		} catch (RuntimeException r) {
			System.out.println("Smash should exist!");
		}
		System.out.println("♛♛♛ Skill Cost for Apocalypse ♛♛♛");
		try {
			if (basicMelee.skillTotalCost("Apocalypse") != 31) {
				System.out.println("Total cost expected for Apocalypse is 31");
			}
			System.out.println("You need a total of " + basicMelee.skillTotalCost("Apocalypse") + " SP to unlock Apocalypse");
		} catch (RuntimeException r) {
			System.out.println("Apocalypse should exist!");
		}
		System.out.println("♚♚♚ Skill Cost for SnowFall ♚♚♚");
		try {
			if (basicMelee.skillTotalCost("SnowFall") != 26) {
				System.out.println("Total cost expected for Smash! is 26");
			}
			System.out.println("You need a total of " + basicMelee.skillTotalCost("SnowFall") + " SP to unlock SnowFall");
		} catch (RuntimeException r) {
			System.out.println("SnowFall should exist!");
		}
		System.out.println("♟♟♟ Skill Cost for LightningRod ♟♟♟");
		try {
			if (basicMelee.skillTotalCost("LightningRod") != 8) {
				System.out.println("Total cost expected for LightningRod is 8");
			}
			System.out.println("You need a total of " + basicMelee.skillTotalCost("LightningRod") + " SP to unlock LightningRod");
		} catch (RuntimeException r) {
			System.out.println("LightningRod should exist!");
		}


	}
	

}
