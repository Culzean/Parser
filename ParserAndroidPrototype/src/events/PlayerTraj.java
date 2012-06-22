package events;

import android.R.color;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Shader;

public class PlayerTraj{
	
	private Paint paint;
	private Vector2D trajVector = null;
	
	final int ALPHA_SLECT = 155;
	final int ALPHA_NORM = 125;
	
	private int heartX;
	private int heartY;
	
	//common value. ratio between the player's current input and max possible
	// player (vecX + vecY) divided by the maximum possible vecX + vecY (heartX + heartY//for centred heart!)
	//value between 0-1
	private static float spread = 0.1f;
	private final static float RAND_FACTOR = 1000;
	private final float SPREAD_MOD = 0.65f;
	private static RandomNumGen rNumb = new RandomNumGen();
	
	private boolean inPlay = false;
	
	public PlayerTraj(int cenX, int cenY)
	{
		heartX = cenX;		heartY = cenY;
		rNumb.Randomize();
		paint = new Paint();
		trajVector = new Vector2D(-7,-3);
		paint.setColor(color.darker_gray);
		paint.setAlpha(ALPHA_SLECT);
		paint.setStrokeWidth(12);
		paint.setShader(new Shader());
		paint.setStrokeCap(Cap.ROUND);
	}
	
	public void draw( Canvas g )
	{
		if(inPlay)
			g.drawLine(heartX, heartY, (float)(heartX + trajVector.x), (float)(heartY + trajVector.y), paint);
	}
	
	public void setVector(int playerX, int playerY)
	{
		trajVector.x = playerX;			trajVector.y = playerY;
		setInPlay(true);
	}
	
	public Vector2D colVector()
	{
		setSelected(true);
		if(Math.abs(trajVector.x) > Math.abs(trajVector.y))
			spread = ( 1 - (float) (Math.abs(trajVector.x) / heartX) ) * SPREAD_MOD;
		else
			spread = ( 1 - (float) (Math.abs(trajVector.y) / heartY) ) * SPREAD_MOD;
		return trajVector;
	}
	
	public void setSelected(boolean select)
	{
		if(select)
			paint.setAlpha(ALPHA_SLECT);
		else
			paint.setAlpha(ALPHA_NORM);
	}

	public boolean isInPlay() {
		return inPlay;
	}

	public void setInPlay(boolean inPlay) {
		this.inPlay = inPlay;
	}

	public float getSpread() {
		return spread;
	}

	public void setSpread(float spread) {
		spread = spread;
	}
	
	public void setSpread( int playerX, int playerY ){
		spread = ( Math.abs(playerX + playerY ) / Math.abs( heartX + heartY ) );
	}
	
	public static Vector2D createSpread( Vector2D rawVec )
	{
		if(spread == 0)
			return rawVec;
		
		rawVec.x = applySpread((float) rawVec.x);
		rawVec.y = applySpread((float) rawVec.y);
		
		/*if(rawVec.x < -1 )
		{
			rawVec.y = -1 * rawVec.y;
			rawVec.x = -1;
		}
		else if(rawVec.x > 1)
		{
			rawVec.y = -1 * rawVec.y;
			rawVec.x = 1;
		}
		
		if(rawVec.y < -1 )
		{
			rawVec.x = -1 * rawVec.x;
			rawVec.y = -1;
		}
		else if(rawVec.y > 1)
		{
			rawVec.x = -1 * rawVec.x;
			rawVec.y = 1;
		}*/
		
		return rawVec;
	}
	
	private static float applySpread( float rawVal )
	{
		//take the standard value. create a random spread
		//do we add or minus this?
		//check for any wrap
		//return value
		float offSet = rNumb.Random((int) (spread * RAND_FACTOR));
		offSet /= RAND_FACTOR;
		if( rNumb.Random(100) > 50 )
			rawVal = ( rawVal - offSet );
		else
			rawVal = ( rawVal + offSet );
		return rawVal;
	}

}
