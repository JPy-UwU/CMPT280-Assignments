public class Quest implements Cloneable, Comparable<Quest> {

	protected String questName;
	protected String questArea;
	protected Integer experienceValue;
	protected Integer id;
	



	/**
	 * @param questName Name of the quest
	 * @param questArea Area of the word in which the quest takes place
	 * @param experienceValue Experience value of the quest once completed.
	 */
	public Quest(Integer id, String questName, String questArea, Integer experienceValue) {
		super();
		this.questName = questName;
		this.questArea = questArea;
		this.experienceValue = experienceValue;
		this.id = id;
	}


	/**
	 * @return the id
	 */
	public Integer id() {
		return id;
	}




	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}



	/**
	 * @return the questName
	 */
	public String questName() {
		return questName;
	}




	/**
	 * @param questName the questName to set
	 */
	public void setQuestName(String questName) {
		this.questName = questName;
	}




	/**
	 * @return the questArea
	 */
	public String questArea() {
		return questArea;
	}




	/**
	 * @param questArea the questArea to set
	 */
	public void setQuestArea(String questArea) {
		this.questArea = questArea;
	}




	/**
	 * @return the experienceValue
	 */
	public int experienceValue() {
		return experienceValue;
	}




	/**
	 * @param experienceValue the experienceValue to set
	 */
	public void setExperienceValue(int experienceValue) {
		this.experienceValue = experienceValue;
	}




	@Override
	public String toString() {
		return this.id + ", " + this.questName + ", " + this.questArea + 
				", XP: " + this.experienceValue;
	}


	@Override
	public int compareTo(Quest o) {		
		return experienceValue.compareTo(o.experienceValue);
	}
}
