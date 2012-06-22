package game;

import game.CellType.Cell;
import game.CellType.FatCell;
import game.test.R;
import gameEngine.CollisionControl;
import gameEngine.ParserView;
import gameEngine.Sprite;
import hud.HUD;

import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.view.MotionEvent;
import events.RandomNumGen;
import events.Vector2D;

public class GameState extends State 
{
	private Heart heart;
	private HeartLine line;
	private PlayerUI input;
	private Vector2D cellVector; // to set the direction of the cells
	private GameScore scoreCal;
	
	//Random number generator
	private RandomNumGen rNum;
	
	//Virus attributes
	private Timer virusChecker;
	public int virusCount;
	
	//Score attributes
	private double score;
	
	//Current cell type
	private int currentCellType;
	
	private HUD hud;
	private Sprite background;
	
	public GameState(int width, int height, ParserView ref, GameModel model) 
	{
		super(width, height, ref, model);
		score = 10;
	}

	public void init()
	{
		//Timer to check and call a function to decrease the score if there are virus
		TimerTask virusCheckerTask = new TimerTask(){
			@Override
			public void run() {	checkVirus(); }
		};
		virusChecker = new Timer();
		virusChecker.scheduleAtFixedRate(virusCheckerTask, new Date(), 2000);
		
		heart = new Heart(gameModel, windowWidth * 0.5, windowHeight * 0.5, windowHeight * 0.18);
		line = new HeartLine(windowWidth, windowHeight, gameModel, 2);
		input = new PlayerUI(gameModel, heart);
		scoreCal = new GameScore(gameModel);
		
		rNum = new RandomNumGen(); //initilize RNG
		rNum.Randomize();
		
		hud = new HUD(this, scoreCal);
		
		gameModel.addOrgan(rNum.Random((int)ParserView.windowWidth), rNum.Random((int)ParserView.windowHeight), 15, 500);
		Bitmap backgroundSprite = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.bg);
		background = new Sprite(backgroundSprite, 1, 1, 1, 1);
		background.resize(windowWidth, windowHeight);
		
		//By default use red cells
		currentCellType = Entity.REDCELL;
		
		//Play the heartbeat sound
		gameModel.PlayHeartBeat();
	}
	
	public void update(long elapsed)
	{
		line.update();
		if(input.update(elapsed) != null)
			cellVector = input.update(elapsed).colVector();
		
		if(cellVector == null)
		{ cellVector = new Vector2D(3,7);	}
			
		//main update function
		if(heart.update())
		{
			//asking heart if we will add new cell
			this.assignCell();
		}
		if(gameModel.getGhostCount() >= 2)
		{
			OrganGhost newOrgan = gameModel.getGhost();
			gameModel.addOrgan(newOrgan.getCenX(), newOrgan.getCenY(), 15, 330);
		}
		
		//////////////////////////////////////////////////
		//update game objects
		Iterator<Cell> cell_itr = gameModel.cells.iterator();
		Iterator<Organ> organ_itr = gameModel.organs.iterator();
		
		/////////////////////////////////////////////////////////////////
		//First detect if the cells have to be removed
		while(cell_itr.hasNext())
		{
			Cell updateCell = cell_itr.next();
			if(updateCell.update(gameModel.getBeat()))
				
				gameModel.removeCell(cell_itr);
		}
		
		/////////////////////////////////////////////////////////////////
		//Then check cells collision
		for(int i = 0; i < gameModel.cellCount; i++)
		{
			for(int j = i; j < gameModel.cellCount; j++)
			{
				//If there is a collision between 2 cells
				if(CollisionControl.CircleVsCircle(gameModel.cells.get(i), gameModel.cells.get(j)))
				{
					//If only 1 cell in the array, the 2 cells will be the same so we need to check if it's not the same!
					if(gameModel.cells.get(i) != gameModel.cells.get(j))
					{
						gameModel.cells.get(i).Collide(gameModel.cells.get(j));
						gameModel.cells.get(j).Collide(gameModel.cells.get(i));
					}
				}
			}
		}
		
		//////////////////////////////////////////////////////////
		//check fat cells against other fat cells
		//there are better ways to do this
		for(int i=0, endi = gameModel.getFatCount(); i< endi; i++)
		{
			///check for collisions with heart
			if(CollisionControl.InvCircleVsCircle(heart, gameModel.fatties.get(i)))
			{
				boolean endgame = false;
				endgame = true;
			}
			for(int j=i, endj = gameModel.getFatCount(); j < endj; j++)
			{

					if(gameModel.fatties.get(i) != gameModel.fatties.get(j))
					{
						if(CollisionControl.CircleVsCircle(gameModel.fatties.get(i), gameModel.fatties.get(j)))
						{
							gameModel.fatties.get(i).Collide(gameModel.fatties.get(j));
							gameModel.fatties.get(j).Collide(gameModel.fatties.get(i));
						}
					}
				
			}
	}
		
		virusCount = gameModel.getVirusCount();
		
		///////////////////////////////////////////////////////////
		//update the organs
		while(organ_itr.hasNext())
		{
			Organ crtOrgan = organ_itr.next();
			if(crtOrgan.update(gameModel.getBeat()))
			{
				gameModel.removeOrgan(organ_itr);
				if(crtOrgan.getRadius() == GameModel.ORGAN_RADIUS_MIN)
				{
					gameModel.addFat((int)crtOrgan.getPosX(), (int)crtOrgan.getPosY(), heart.getPosition(), 24, 20);
					gameModel.PlaySlop();
					gameModel.slowerHeart();
				}
			}
			
			for(int i=0; i< gameModel.cellCount; i++)
			{
				Cell crtCell = gameModel.cells.get(i);
				//If there is a collision between the organ and the cell
				if(CollisionControl.CircleVsCircle(crtOrgan, crtCell))
				{
					//increment points
					//shorten life of organ, increase radius
					crtOrgan.Collide(crtCell);
					heart.incrRun();
					if(crtCell.getType() == Entity.REDCELL)
					{
						gameModel.PlayPlop();
						score += 1;
					}
				}
			}
		}
		
		Iterator<FatCell> fatties_itr = gameModel.fatties.iterator();
		/////////////////////////////////////
		//update fat cells
		while(fatties_itr.hasNext())
		{
			FatCell crtFat = fatties_itr.next();
			crtFat.Update(gameModel.getBeat());
		}
		hud.update(elapsed);
	}
	
	public void gameRender(long elapsed, Canvas dbImage) 
	{	
		background.draw(dbImage);
		
		////////////////////////////////////////////////
		//////how do you make a single draw list?
		Iterator<Cell> itr = gameModel.cells.iterator();
		Iterator<Organ> organ_itr = gameModel.organs.iterator();
		Iterator<FatCell> fatties_itr = gameModel.fatties.iterator();
		
		//draw fat
		while(fatties_itr.hasNext())
		{
			FatCell crtFat = fatties_itr.next();
			crtFat.Draw(dbImage);
		}
		
		//Draw cells
		while(itr.hasNext())
		{
			Cell drawCell = itr.next();
			drawCell.Draw(dbImage);
		}
		//Draw organs
		while(organ_itr.hasNext())
		{
			Organ drawCell = organ_itr.next();
			drawCell.Draw(dbImage);
		}
		
		input.draw(dbImage);
		heart.draw(dbImage);
		line.draw(dbImage);
		hud.draw(dbImage);
		
		//On top of everything, draw the score
		dbImage.drawText("Score: "+score, 10, 20, ParserView.textColor);
	}
	
	public void slowUpdate( long stepTime )
	{
		scoreCal.calScore(stepTime);
	}
	
	private boolean assignCell()
	{
		//create new cell. will change size depending on how many you have previously hit
		//initial direction should be a unit vector!
		int rndNumberX = (int) (rNum.Random(heart.getCurRad()) - (heart.getCurRad() * 0.5) );
		int rndNumberY = (int) (rNum.Random(heart.getCurRad()) - (heart.getCurRad() * 0.5) );
		
		if(gameModel.addCell(heart.getX() + rndNumberX, heart.getY() + rndNumberY,
				cellVector, currentCellType) != null)
			return true;
		else
			return false;
	}
	
	private void checkVirus()
	{	
		if(virusCount > 0 && score > 0)
			score = score - Math.log(virusCount+1)*1.25;
	}
	
	public boolean onSensorChanged(SensorEvent event)
	{
		input.setValues(event);
		return false;
	}
	public boolean onTouch(MotionEvent event) 
	{
		input.touch(event);
		if(event.getAction() == MotionEvent.ACTION_DOWN)
			hud.interact((int) event.getX(), (int) event.getY());
		return false;
	}
	public void switchCellType(int newCellType)
	{
		currentCellType = newCellType;
	}
}
