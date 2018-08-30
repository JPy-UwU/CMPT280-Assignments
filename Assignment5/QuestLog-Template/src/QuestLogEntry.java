import lib280.base.Keyed280;

public class QuestLogEntry implements Cloneable, Comparable<QuestLogEntry>, Keyed280<String> {

	protected String questName;
	protected String questArea;
	protected int recommendedMinLevel;
	protected int recommendedMaxLevel;
	
	/**
	 * Constructor
	 * 
	 * @param questName Name of the quest
	 * @param questArea Area of the world in which the quest takes place
	 * @param recommendedMinLevel Recommended minimum character level to attempt quest
	 * @param recommendedMaxLevel Recommended maximum character level to attempt quest.
	 */
	public QuestLogEntry(String questName, String questArea,
			int recommendedMinLevel, int recommendedMaxLevel) {
		super();
		this.questName = questName;
		this.questArea = questArea;
		this.recommendedMinLevel = recommendedMinLevel;
		this.recommendedMaxLevel = recommendedMaxLevel;
	}

	
	/**
	 * @return the questName
	 */
	public String getQuestName() {
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
	public String getQuestArea() {
		return questArea;
	}

	/**
	 * @param questArea the questArea to set
	 */
	public void setQuestArea(String questArea) {
		this.questArea = questArea;
	}

	/**
	 * @return the recommendedMinLevel
	 */
	public int getRecommendedMinLevel() {
		return recommendedMinLevel;
	}

	/**
	 * @param recommendedMinLevel the recommendedMinLevel to set
	 */
	public void setRecommendedMinLevel(int recommendedMinLevel) {
		this.recommendedMinLevel = recommendedMinLevel;
	}

	/**
	 * @return the recommendedMaxLevel
	 */
	public int getRecommendedMaxLevel() {
		return recommendedMaxLevel;
	}

	/**
	 * @param recommendedMaxLevel the recommendedMaxLevel to set
	 */
	public void setRecommendedMaxLevel(int recommendedMaxLevel) {
		this.recommendedMaxLevel = recommendedMaxLevel;
	}

	public QuestLogEntry clone() throws CloneNotSupportedException {
		return (QuestLogEntry)super.clone();
	}

	@Override
	public int compareTo(QuestLogEntry o) {
		return this.questName.compareTo(o.questName);
	}
	

	@Override
	public String key() {
		return this.questName;
	}
	
	@Override
	public String toString() {
		return this.questName + " : " + this.questArea + 
				", Level Range: " + this.recommendedMinLevel + 
				"-" + this.recommendedMaxLevel;
	}
}
