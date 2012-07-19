package game;

import java.util.Iterator;

import events.RandomNumGen;
import events.Vector2D;
import game.CellType.Cell;
import game.CellType.FatCell;
import gameEngine.GameObject;
import gameEngine.ParserView;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorEvent;
import android.view.MotionEvent;

public class HeartAttack extends State{

	private int countDown;
	private Paint texCol;
	private Heart heart = null;
	private double avTime;
	//Random number generator
	private RandomNumGen rNum;
	private int addCount = 0;
	
	public HeartAttack(int _windowWidth, int _windowHeight, ParserView gView,
			GameModel modelRef) {
		super(_windowWidth, _windowHeight, gView, modelRef);
		init();
	}

	public void init() {
		heart = gameModel.getHeartRef();
		gameModel.setSinus(1);
		rNum = new RandomNumGen();
		rNum.Randomize();
		texCol = new Paint();
		texCol.setTextSize(40);
		texCol.setColor(Color.BLUE);
		countDown = 770;
		for(int i=0;i< 4; ++i)
			gameModel.fasterHeart();
		
		heart.setRadius((int) (windowHeight * 0.25));
		
		gameModel.clearCells();
	}

	@Override
	public boolean update(long dt) {
		dt *= 0.000001;
		dt = (long) ( (dt + avTime) * 0.5 );
		
		heart.update(dt);
		if(--countDown < 0)
			gameView.endGame(this.getWindowWidth(), this.getWindowHeight());
		
		if(++addCount < 320)
		{
			if(rNum.Random(10) > 4)
				assignCell();
			else
				addFat();
		}
		
		//////////////////////////////////////////////////
		//update game objects
		Iterator<GameObject> cell_itr = gameModel.cells.iterator();

		/////////////////////////////////////////////////////////////////
		//First detect if the cells have to be removed
		while(cell_itr.hasNext())
		{
			GameObject updateCell = cell_itr.next();
			if( !(((Entity) updateCell).isRemoved()) )
			updateCell.Update( gameModel.getBeat(),dt);
		}
		return true;
	}

	@Override
	public void slowUpdate(long stepTime, int frCount) {
		avTime = stepTime / frCount;
		avTime *= 0.000001;
	}

	@Override
	public void gameRender(long elapsed, Canvas dbImage) {
		
		dbImage.drawText(""+countDown, 350, 170, texCol);
		
		////////////////////////////////////////////////
		//////how do you make a single draw list?
		Iterator<GameObject> itr = gameModel.cells.iterator();

		//draw cells
		while(itr.hasNext())
		{
			GameObject drawCell = itr.next();
			if( ((Entity) drawCell).getType() == Entity.FATCELL )
			{
				if( !((FatCell) drawCell).getWall() )
					drawCell.Draw(dbImage);
			}		
			else if( !(((Entity) drawCell).isRemoved()) )
					drawCell.Draw(dbImage);
		}
		
		heart.draw(dbImage);
		dbImage.drawText("You Scored..", 270, 180, texCol);
		dbImage.drawText(""+gameModel.scoreCal.score, 310, 380, texCol);
		dbImage.drawText("..in this life", 270, 430, texCol);
	}
	
	private boolean assignCell()
	{
		//create new cell. will change size depending on how many you have previously hit
		//initial direction should be a unit vector!
		int rndNumberX = (int) (rNum.Random(heart.getCurRad()) - (heart.getCurRad() * 0.5) );
		int rndNumberY = (int) (rNum.Random(heart.getCurRad()) - (heart.getCurRad() * 0.5) );
		
		Vector2D vec = new Vector2D( (rNum.Random(2000) * 0.001) - 1, (rNum.Random(2000) * 0.001) - 1 );
		int cellType = Entity.PLATELET;
		if(rNum.Random(10) > 5)
			cellType = rNum.Random(Entity.NUM_ENT_TYPES);
		else
			cellType = Entity.REDCELL;
		
		if((cellType == Entity.REDCELL) ||
				(cellType == Entity.PLATELET) )
			{}
		else
			return false;
		
		Cell cellRef = gameModel.addCell(heart.getX() + rndNumberX, heart.getY() + rndNumberY,
				vec, cellType);
		
		//this is returning null!
		if((cellRef.getType() == Entity.REDCELL))
			cellRef.setRadius(42);
		
			return true;
	}
	
	private void addFat()
	{
		int rndNumberX = (int) (rNum.Random(heart.getCurRad()) - (heart.getCurRad() * 0.5) );
		int rndNumberY = (int) (rNum.Random(heart.getCurRad()) - (heart.getCurRad() * 0.5) );
		
		gameModel.addFat(heart.getX() + rndNumberX,
				heart.getY() + rndNumberY,
				heart.getPosition(), 36, 20);
	}

	public boolean onTouch(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void switchCellType(int newCellType) {
		// TODO Auto-generated method stub
		
	}

}
