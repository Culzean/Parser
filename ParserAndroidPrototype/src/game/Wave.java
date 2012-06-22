package game;

public class Wave 
{
	private int score;
	private float duration;
	private long startedTime;
	private int waveDifficulty;
	private int waveNumber;
	
	boolean ended;
	
	public final int DIFFICULTY_EASY = 1;
	public final int DIFFICULTY_MEDIUM = 2;
	public final int DIFFICULTY_HARD = 3;
	
	Wave(int difficulty, int number)
	{
		startedTime = System.currentTimeMillis();
		score = 0;
		waveDifficulty = difficulty;
		waveNumber = number;
		switch(difficulty)
		{
			case DIFFICULTY_EASY: duration = 120000;
				break;
			case DIFFICULTY_MEDIUM: duration = 60000;
				break;
			case DIFFICULTY_HARD: duration = 30000;
				break;
		}
	}
	
	void update(float deltaTime)
	{
		long currentTime = System.currentTimeMillis();
		if(currentTime >= startedTime + duration)
			ended = true;
		
		
	}
	
	boolean hasEnded() { return ended; }
}
