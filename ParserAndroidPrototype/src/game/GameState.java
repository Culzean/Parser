package game;

import game.CellType.Cell;
import game.CellType.FatCell;
import game.test.R;
import gameEngine.CollisionControl;
import gameEngine.GameObject;
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
import events.CreateCell;
import events.RandomNumGen;
import events.Vector2D;

public class GameState extends State 
{
	private Heart heart;
	private HeartLine line;
	private PlayerUI input;
	private Vector2D cellVector; // to set the direction of the cells
	private GameScore scoreCal;
	private long avTime; //average time per frame in ms
	
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
		
		gameModel.InitGame();
		
		heart = new Heart(gameModel, windowWidth * 0.5, windowHeight * 0.5, windowHeight * 0.18);
		gameModel.setHeartRef(heart);
		line = new HeartLine(windowWidth, windowHeight, gameModel, 2);
		input = new PlayerUI(gameModel, heart);
		scoreCal = gameModel.scoreCal;
		
		rNum = new RandomNumGen(); //initilize RNG
		rNum.Randomize();
		
		hud = new HUD(this, scoreCal);
		
		gameModel.setGameHeight(getWindowHeight());
		gameModel.setGameWidth(getWindowWidth());
		
		gameModel.addOrgan(rNum.Random((int)ParserView.windowWidth), rNum.Random((int)ParserView.windowHeight), 15, 500);
		Bitmap backgroundSprite = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.bg);
		background = new Sprite(backgroundSprite, 1, 1, 1, 1);
		background.resize(windowWidth, windowHeight);
		
		int randStartFat = (rNum.Random(22) + 6);
		for(int i = 0; i< randStartFat; ++i)
			this.addFat();
		//Play the heartbeat sound
		gameModel.PlayHeartBeat( heart.getVol() );//mono?
	}
	
	public boolean update(long dt)
	{
		//convert dt to ms, use avergae over one sec to stablise value
		dt *= 0.000001;
		dt = (long) ( (dt + avTime) * 0.5 );
		line.update(dt);
		if(input.update(dt) != null)
			cellVector = input.update(dt).colVector();
		
		if(cellVector == null)
		{ cellVector = new Vector2D(3,7);	}
			
		//main update function
		if(heart.update(dt))
		{
			//asking heart if we will add new cell
			this.assignCell();
		}
		if(gameModel.getGhostCount() >= 2)
		{
			OrganGhost newOrgan = gameModel.getGhost();
			gameModel.addOrgan(newOrgan.getCenX(), newOrgan.getCenY(), 15, 530);
		}
		scoreCal.update(dt);
		//////////////////////////////////////////////////
		//update game objects
		Iterator<GameObject> cell_itr = gameModel.cells.iterator();
		Iterator<Organ> organ_itr = gameModel.organs.iterator();
		
		/////////////////////////////////////////////////////////////////
		//First detect if the cells have to be removed
		while(cell_itr.hasNext())
		{
			GameObject updateCell = cell_itr.next();
			if(updateCell.Update( gameModel.getBeat(),dt) )
				
				gameModel.removeCell(cell_itr);
		}
		///////////////////////////////////////////////////////////////
		//Check what cells should be created
		int iBuild = -1;
		while( ++iBuild < gameModel.MAX_BUILD_LIST )
		{
			CreateCell crtOrder = gameModel.buildList.get(iBuild);
			if(crtOrder.isActive())
				crtOrder.addCell(cellVector);
			else
				break;
		}
		gameModel.resetBuildCount();
		
		/////////////////////////////////////////////////////////////////
		//Then check cells collision
		for(int i = 0; i < gameModel.cellCount; i++)
		{
			for(int j = i; j < gameModel.cellCount; j++)
			{
				//If there is a collision between 2 cells
				if(CollisionControl.CircleVsCircle((Entity)gameModel.cells.get(i), (Entity)gameModel.cells.get(j)))
				{
					//If only 1 cell in the array, the 2 cells will be the same so we need to check if it's not the same!
					if(gameModel.cells.get(i) != gameModel.cells.get(j))
					{
						gameModel.cells.get(i).Collide((Entity) gameModel.cells.get(j));
						gameModel.cells.get(j).Collide((Entity) gameModel.cells.get(i));
					}
				}
			}
			//need to check fat cells against heart!
			if(((Entity) gameModel.cells.get(i)).getType() == Entity.FATCELL)
			{
				if(CollisionControl.InvCircleVsCircle(heart,(Entity)gameModel.cells.get(i)))
				{
					this.gameView.gameOver();
					return false;
				}
			}
		}
		
		virusCount = gameModel.getVirusCount();
		///////////////////////////////////////////////////////////
		//update the organs
		while(organ_itr.hasNext())
		{
			Organ crtOrgan = organ_itr.next();
			if(crtOrgan.Update(gameModel.getBeat(),dt))
			{
				gameModel.removeOrgan(organ_itr);
			}
			
			for(int i=0; i< gameModel.cellCount; i++)
			{
				GameObject crtCell = gameModel.cells.get(i);
				//If there is a collision between the organ and the cell
				if(CollisionControl.CircleVsCircle((Entity)crtOrgan, (Entity)crtCell))
				{
					//increment points
					//shorten life of organ, increase radius
					crtOrgan.Collide((Entity)crtCell);
					crtCell.Collide((Entity)(crtOrgan));
					if(((Entity) crtCell).getType() == Entity.REDCELL)
					{
						scoreCal.regEvent(GameScore.ORGAN_HIT);
						gameModel.PlayPlop();
						heart.incrRun();
						score += 1;
					}
				}
			}
		}
		

		hud.update(dt);
		return true;
	}
	
	public void gameRender(long elapsed, Canvas dbImage) 
	{	
		background.draw(dbImage);
		
		////////////////////////////////////////////////
		//////how do you make a single draw list?
		Iterator<GameObject> itr = gameModel.cells.iterator();
		Iterator<Organ> organ_itr = gameModel.organs.iterator();
		
		//Draw cells
		while(itr.hasNext())
		{
			GameObject drawCell = itr.next();
			((Entity) drawCell).Draw(dbImage);
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
	
	public void slowUpdate( long stepTime, int frCount )
	{
		avTime = stepTime / frCount;
		avTime *= 0.000001;
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
	
	private void addFat()
	{
		int rndNumberX = (int) (rNum.Random(heart.getCurRad()) - (heart.getCurRad() * 0.5) );
		int rndNumberY = (int) (rNum.Random(heart.getCurRad()) - (heart.getCurRad() * 0.5) );
		
		if(rndNumberX >= 0)
			rndNumberX += 140;
		else
			rndNumberX -= 140;
		if(rndNumberY >= 0)
			rndNumberY += 90;
		else
			rndNumberY -= 90;
		
		gameModel.addFat(heart.getX() + rndNumberX, heart.getY() + rndNumberY,
				heart.getPosition(), 24, 20);
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
	//	gameModel.addCell( (int)event.getX(), (int)event.getY(), new Vector2D(-1,1), Entity.VIRUS);
		return false;
	}
	public void switchCellType(int newCellType)
	{
		currentCellType = newCellType;
	}
	public int getCellType()			{	return currentCellType;	}
}
