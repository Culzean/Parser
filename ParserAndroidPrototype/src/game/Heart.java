////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//
//Game Engine and game by Daniel Waine and Raphael Chappuis 
//
//for Mobile and Web Games Development
//
//Banner IDs B00226804 - B00218930
//
////////////////////////////////////////////////////////////////////////

package game;

import events.Vector2D;
import game.test.R;
import gameEngine.Sprite;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Heart
{
	private Vector2D position;
	private Sprite sprite;
	private int radius, tempRad;
	private int scale;
	private GameModel gameModel;
	private int runCount;
	static final int RUN_LIMIT = 3;
	static final Paint heartColor = new Paint();
	private int newHeartWidth, newHeartHeight, hearWidth, heartHeight;
	
	public Heart(GameModel imodel, double d, double e, double f)
	{
		gameModel = imodel;
		radius = (int) f;
		position = new Vector2D(d, e);
		tempRad = 0;
		runCount = 0;
		scale = 1;
		heartColor.setColor(Color.RED);
		heartColor.setAlpha(80);
		Bitmap bitmapSprite = BitmapFactory.decodeResource(gameModel.viewRes, R.drawable.heart);
		sprite = new Sprite(bitmapSprite, 1, 1, 1, 1);
		newHeartWidth = hearWidth = sprite.getWidth();
		newHeartHeight = heartHeight = sprite.getHeight();
	}
	
	//heart update function
	//returns true if it's time to introduce a new cell
	//not sure if this is best place for this logic
	public boolean update()
	{
		boolean addCell = false;
		
		float beat = gameModel.incBeat();
		int newScale = (int)-(beat * 100);
		
		if(beat > 0.05)
			beat = 0.05f;
		
		tempRad = radius - newScale;
		newHeartWidth = (int)(hearWidth + (hearWidth*beat));
		newHeartHeight = (int)(heartHeight + (heartHeight*beat));
		
		if(tempRad > (radius * 2))
		{
			////////////
			//later will implement different test depending on the cell type selected
			int testRad = radius - ((int)-(gameModel.getNextBeat() * 100));
			tempRad = radius * 2;
			addCell = true;
			//clear the current trajectory only if the wave is finished
			if(testRad < (radius * 2) && gameModel.getPlayerIn() != null)
				{
					gameModel.getPlayerIn().setInPlay(false);
					gameModel.setPlayerIn(null);
				}
		}
			

		if(runCount > RUN_LIMIT)
		{
			runCount = 0;
			gameModel.fasterHeart();
		}
		
		return addCell;
	}
	
	public void draw(Canvas g)
	{
		//check scale and draw oval
		g.drawCircle((int)position.x, (int)position.y, tempRad, heartColor);
		sprite.setLocation((int)position.x-sprite.getWidth()/2, (int)position.y-sprite.getHeight()/2);
		sprite.resize(newHeartWidth, newHeartHeight);
		sprite.draw(g);
	}
	
	public int getX(){ return (int)position.x; }
	public int getY(){ return (int)position.y; }
	public Vector2D getPosition() { return position; }
	public int getRadius() { return radius; }
	public int getCurRad() { return radius * scale; }
	public int getRun() { return runCount; }
	public void incrRun() {	runCount++;	}
}

