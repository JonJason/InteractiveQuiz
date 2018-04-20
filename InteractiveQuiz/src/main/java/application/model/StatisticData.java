package application.model;

public class StatisticData {
	private int answeredCorrectly;
	private int answeredIncorrectly;
	private int givenUpTime;

	public StatisticData() {
		answeredCorrectly = 0;
		answeredIncorrectly = 0;
		givenUpTime = 0;
	}
	
	public int getAnsweredCorrectly() {
		return answeredCorrectly;
	}
	
	public void setAnsweredCorrectly(int answeredCorrectly) {
		this.answeredCorrectly = answeredCorrectly;
	}
	
	public int getAnsweredIncorrectly() {
		return answeredIncorrectly;
	}
	
	public void setAnsweredIncorrectly(int answeredIncorrectly) {
		this.answeredIncorrectly = answeredIncorrectly;
	}
	
	public int getGivenUpTime() {
		return givenUpTime;
	}
	
	public void setGivenUpTime(int givenUpTime) {
		this.givenUpTime = givenUpTime;
	}
	
	public String toString() {
		return getClass().getSimpleName() + " [" +
		"answeredCorrectly=" + answeredCorrectly + ", " +
		"answeredIncorrectly=" + answeredIncorrectly + ", " +
		"givenUpTime=" + givenUpTime + "]";
	}
}
