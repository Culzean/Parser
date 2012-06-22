package game;

import java.util.Observable;

import events.ScoreObserver;


public class GameScore extends Observable{

	private int tempOrgansCount =0;
	private int tempFatCount = 0;
	private int tempVirusCount =0;
	private ScoreObserver anObserver = null;
	
	GameModel model;
	
	//variables to manage the score change
	float score;
	float scoreVel;
	
	final float SCORE_VEL_DEFAULT =  0.07f; //points/ms
	int fibNumbs[] = new int[11];
	
	public GameScore(GameModel _model)
	{
		model = _model;
		scoreVel = SCORE_VEL_DEFAULT;
		composeFib();
	}
	
	public void update(long dt){
		score += (scoreVel * dt);
	}
	
	public void calScore( long timeStep )
	{
		//infrequent check on score. check status of game
		//increase or decrease increment for score(Never zero || < 0)
		//check is on game objects and delta of game objects
		
		int virusDelta =  model.getVirusCount() - tempVirusCount;
		int fatDelta = model.getFatCount() - tempFatCount;
		int organDelta = model.getOrganCount() - tempOrgansCount;
		
		if(model.getOrganCount() == 0)
		{
			scoreVel = SCORE_VEL_DEFAULT;
		}
		else if(organDelta < 0 && fatDelta < 2)
		{//busted several organs or they've been replaced by fat
			scoreVel *= 1.4;
		}
		else if(organDelta > 0)
		{//many organs have appeared
			scoreVel *= 0.94;
		}
		
		if(fatDelta > 0)
			scoreVel *= 0.7;
		else if(fatDelta < -1)
			scoreVel *= 2;
		
		int i = 0;
		while( model.getVirusCount() > fibNumbs[++i] ){
			
		}
		//some expense here, but once a turn, and will do nothing if i =0
		scoreVel = (float) ((float)  ( 1 / Math.pow(Math.E, i * 0.1) ));
		score += (scoreVel * timeStep);
		
		tempVirusCount = model.getVirusCount();
		tempFatCount = model.getFatCount();
		tempOrgansCount = model.getOrganCount();
		
		this.notifyObservers();
	}
	
	public void notifyObservers()
	{
		if(anObserver != null)
			anObserver.UpdateScore(this.score, this.scoreVel);
	}
	
	public void addObserver( ScoreObserver newObserver)
	{
		//currently just one, but expecting at least one more
		anObserver = newObserver;
	}
	
	private void composeFib()
	{
		int f0 = 0;
		int f1 = 1;

		for (int i = 0; i < 10; ++i) {

		fibNumbs[i] = f1;	
			
		final int temp = f1;
		f1 += f0;
		f0 = temp;
		}
		
		fibNumbs[10] = 610;//55 is a lot of viruses

	}
}
