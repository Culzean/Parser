package game;

import events.Vector2D;
import gameEngine.ParserView;
import hud.CalibCircle;
import hud.HUD;
import hud.MenuHUD;
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
	private MenuHUD hud = null;
	private int startCellType;
	
	public MenuState(int width, int height, ParserView viewRef, GameModel model) 
	{
		super(width, height, viewRef, model);
		calib1 = new CalibCircle( (int)(this.getWindowWidth() * 0.5), (int) (this.getWindowHeight() * 0.54), (int) (this.getWindowWidth() * 0.1) );
		calib2 = new CalibCircle( (int)(this.getWindowWidth() * 0.5), (int) (this.getWindowHeight() * 0.54), (int) (this.getWindowWidth() * 0.04) );
		calbOffset = new Vector2D((int)(this.getWindowWidth() * 0.5),0);
		hud = new MenuHUD(this);
		startCellType = -1;
	}

	@Override
	public void gameRender(long elapsed, Canvas dbImage) 
	{
		dbImage.drawText("Instructions: ", 20, 60, ParserView.textColor);
		dbImage.drawText(" - The heart is a vital organ. And it's your job to keep this heart ticking over.", 20, 80, ParserView.textColor);
		dbImage.drawText(" - Use touch and drag controls to tell the heart where to send blood cells", 20, 100, ParserView.textColor);
		dbImage.drawText(" - Each blood cell has a different role", 20, 120, ParserView.textColor);
		dbImage.drawText(" - Watch out for viruses getting in your way and fat beginning to accumulate.", 20, 140, ParserView.textColor);
		dbImage.drawText("   Too much fat about and you'll suffer a heart attack!", 20, 160, ParserView.textColor);
		if(startCellType == -1)
			dbImage.drawText("Select starting blood cell type", 280, 380, ParserView.textColor);
		else
			dbImage.drawText("Touch screen to begin game!", 280, 300, ParserView.textColor);
		calib1.draw(dbImage);
		calib2.draw(dbImage);
		hud.draw(dbImage);
	}

	@Override
	public void init(){}
	@Override
	public boolean update(long elapsed){
		if(Math.abs(calbOffset.y) < 0.05 )
			calib2.changePaint();
		calib2.update( (int)(calbOffset.x * 20 + (this.getWindowWidth() * 0.5)),
				(int)(calbOffset.y * 20 + (this.getWindowHeight() * 0.5)) );
		return true;
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
			{
			if(!hud.interact((int)event.getX(), (int)event.getY()) )
				{	
				if(startCellType != -1)
					gameView.startGame(gameView, startCellType);
				}
			}
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
			//don't start the game till we know how you'll be holding this accele
		}
		
		calbOffset.x = (calibrateX() - event.values[0]);
		calbOffset.y = (calibrateY() - event.values[1]);
		return false;
	}

	@Override
	public void slowUpdate(long dt, int frCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void switchCellType(int newCellType) {
		startCellType = newCellType;
	}
}
