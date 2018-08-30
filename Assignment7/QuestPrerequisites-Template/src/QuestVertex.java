import lib280.graph.Vertex280;


public class QuestVertex extends Vertex280 {
	protected Quest quest;
	
	public QuestVertex(int i) {
		super(i);
	}

	/**
	 * @return the quest
	 */
	public Quest quest() {
		return quest;
	}

	/**
	 * @param quest the quest to set
	 */
	public void setQuest(Quest quest) {
		this.quest = quest;
	}
	
	public String toString() {
		return this.quest.toString() ;
	}
	
	
}
