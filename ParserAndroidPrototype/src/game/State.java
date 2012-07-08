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

import gameEngine.ParserView;
import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.view.MotionEvent;

public abstract class State
{
	protected int windowWidth, windowHeight;
	protected int mouseX, mouseY;
	protected ParserView gameView;
	protected GameModel gameModel; 
	
	public State(int _windowWidth, int _windowHeight, ParserView gView, GameModel modelRef)
	{
		windowWidth = _windowWidth;
		windowHeight = _windowHeight;
		gameView = gView;
		gameModel = modelRef;
		//init();
	}
	
	public abstract void init();
	public abstract boolean update(long elapsed);
	public abstract void slowUpdate(long dt, int frCount);
	public abstract void gameRender(long elapsed, Canvas dbImage);
	public abstract boolean onTouch(MotionEvent event);
	public abstract boolean onSensorChanged(SensorEvent event);
	public abstract void switchCellType(int newCellType);
	
	public int getWindowWidth() { return windowWidth; }
	public int getWindowHeight() { return windowHeight; }
	public ParserView getGameView() { return gameView; }
	
}
