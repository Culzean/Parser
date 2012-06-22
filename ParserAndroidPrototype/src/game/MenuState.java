package game;

import events.Vector2D;
import gameEngine.ParserView;
import hud.CalibCircle;
import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.view.MotionEvent;

public class MenuState extends State
{
	//variables to make calibration
	Vector2D calbOffset = null;
	CalibCircle calib1 = null;
	CalibCircle calib2 = null;
	
	//what angle does the player want this game to be at?
	private final int TAKE_AV = 200;
	private int averageX[] = new int[TAKE_AV];
	private int averageY[] = new int[TAKE_AV];
	private int averageZ[] = new int[TAKE_AV];
	private int i = 0;
	private boolean ready = false;
	
	public MenuState(int width, int height, ParserView viewRef, GameModel model) 
	{
		super(width, height, viewRef, model);
		calib1 = new CalibCircle( (int)(this.getWindowWidth() * 0.5), (int) (this.getWindowHeight() * 0.5), (int) (this.getWindowWidth() * 0.1) );
		calib2 = new CalibCircle( (int)(this.getWindowWidth() * 0.5), (int) (this.getWindowHeight() * 0.5), (int) (this.getWindowWidth() * 0.04) );
		calbOffset = new Vector2D((int)(this.getWindowWidth() * 0.5),0);
	}

	@Override
	public void gameRender(long elapsed, Canvas dbImage) 
	{
		dbImage.drawText("Instructions: ", 20, 20, ParserView.textColor);
		dbImage.drawText(" - Once in the game, press one of the button to change the type of cell.", 20, 40, ParserView.textColor);
		dbImage.drawText(" - Use the accelerometer to aim the organs(blue circle) and increase your score", 20, 60, ParserView.textColor);
		dbImage.drawText(" - Switch to white cells in case of virus infection (green cells)", 20, 80, ParserView.textColor);
		dbImage.drawText(" - Take care, the more virus you've on the screen, the more it'll be hard to get rid of them", 20, 100, ParserView.textColor);
		dbImage.drawText("   and your score will decrease", 20, 120, ParserView.textColor);
		dbImage.drawText("Touch the screen to start playing", 20, 200, ParserView.textColor);
		
		calib1.draw(dbImage);
		calib2.draw(dbImage);
	}

	@Override
	public void init(){}
	@Override
	public void update(long elapsed){
		if(Math.abs(calbOffset.y) < 0.05 )
			calib2.changePaint();
		calib2.update( (int)(calbOffset.x * 20 + (this.getWindowWidth() * 0.5)),
				(int)(calbOffset.y * 20 + (this.getWindowHeight() * 0.5)) );
	}
	
	public int calibrateX()
	{
		int val = 0;
		for(int i =0; i< TAKE_AV ; ++i)
		{
			val += averageX[i];
		}
		return (val /= TAKE_AV);
	}
	
	public int calibrateY()
	{
		int val = 0;
		for(int i =0; i< TAKE_AV ; ++i)
		{
			val += averageY[i];
		}
		return (val /= TAKE_AV);
	}
	
	public int calibrateZ()
	{
		int val = 0;
		for(int i =0; i< TAKE_AV ; ++i)
		{
			val += averageZ[i];
		}
		return (val /= TAKE_AV);
	}

	@Override
	public boolean onTouch(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP)
			gameView.startGame(gameView);
		return false;
	}

	@Override
	public boolean onSensorChanged(SensorEvent event) {
		
		averageX[i] = (int) event.values[0];
		averageY[i] = (int) event.values[1];
		averageZ[i] = (int) event.values[2];

		if(++i >= TAKE_AV)
		{
			i =0;
			ready = true;
			//don't start the game till we know how you'll be holding this accele
		}
		
		calbOffset.x = (calibrateX() - event.values[0]);
		calbOffset.y = (calibrateY() - event.values[1]);
		return false;
	}

	@Override
	public void slowUpdate(long dt) {
		// TODO Auto-generated method stub
		
	}
}
