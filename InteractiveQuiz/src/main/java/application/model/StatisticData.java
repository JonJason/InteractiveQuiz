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
	
	public void incCorrect() {
		answeredCorrectly++;
	}
	
	public int getAnsweredIncorrectly() {
		return answeredIncorrectly;
	}
	
	public void incIncorrect() {
		answeredIncorrectly++;
	}
	
	public int getGivenUpTime() {
		return givenUpTime;
	}
	
	public void incGivenupTime() {
		givenUpTime++;
	}
	
	public String toString() {
		return getClass().getSimpleName() + " [" +
		"answeredCorrectly=" + answeredCorrectly + ", " +
		"answeredIncorrectly=" + answeredIncorrectly + ", " +
		"givenUpTime=" + givenUpTime + "]";
	}
}
