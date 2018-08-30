
/* This class stores the data for a single skill in a 
 * skill lib280.tree for a hypothetical video game.
 */
public class Skill {
	String skillName;
	String skillDescription;
	int skillCost;
	
	
	Skill(String name, String desc, int cost) {
		this.skillName = name;
		this.skillDescription = desc;
		this.skillCost = cost;
	}
	
	public String toString() {
		return skillName + ", Cost: " + skillCost;
	}
	
	/**
	 * @return the skillName
	 */
	public String getSkillName() {
		return skillName;
	}
	/**
	 * @param skillName the skillName to set
	 */
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	/**
	 * @return the skillDescription
	 */
	public String getSkillDescription() {
		return skillDescription;
	}
	/**
	 * @param skillDescription the skillDescription to set
	 */
	public void setSkillDescription(String skillDescription) {
		this.skillDescription = skillDescription;
	}
	/**
	 * @return the skillCost
	 */
	public int getSkillCost() {
		return skillCost;
	}
	/**
	 * @param skillCost the skillCost to set
	 */
	public void setSkillCost(int skillCost) {
		this.skillCost = skillCost;
	}
	
	
}
