package events;

public class ScoreEvent {
	
	private long timeStamp;
	private int event;
	
	//class to create a list of events
	//This can be check infrequently for effect on score
	//effect both from each event and accumulated events over a set time frame
	//the time stamp used to decide when to stop search
	
	public ScoreEvent()
	{
		timeStamp = 0;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getEvent() {
		return event;
	}

	public void setEvent(int event) {
		this.event = event;
	}

}
