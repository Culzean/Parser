package hud;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import game.Entity;
import game.GameModel;
import game.GameScore;
import game.GameState;
import game.test.R;
import gameEngine.Sprite;
import hud.ScoreBoard;

public class HUD
{
	private Button redCell;
	private Button whiteCell;
	private Button platelet;
	private GameState gameStateRef;
	private ScoreBoard scoreBoard = null;
	private int SPACER;
	private int INDENT;
	private int BUTTONY;
	private int BUTTON_RAD;
	private int SELECTED_RAD;
	private int BOARD_WIDTH;
	private int SCR_WIDTH;
	private int SCR_HEIGHT;
	
	public HUD(GameState stateRef, GameScore scoreRef)
	{
		gameStateRef = stateRef;
		setPositions();
		CreateButtons();
		scoreBoard = new ScoreBoard( SCR_WIDTH - (INDENT*2), (int)(SCR_HEIGHT * 0.54), BOARD_WIDTH, BOARD_WIDTH , scoreRef);
	}
	
	public void update(long dt){
		
	}
	
	public void slowUpdate(long dt)
	{
		
	}
	
	public void draw(Canvas dbimage)
	{
		redCell.draw(dbimage);
		whiteCell.draw(dbimage);
		platelet.draw(dbimage);
		//highLighter.draw(dbimage);
		scoreBoard.draw(dbimage);
	}
	
	public void interact(int clickX, int clickY)
	{
		if(redCell.clicked(clickX, clickY))
			{
				gameStateRef.switchCellType(Entity.REDCELL);
				//highLighter.setPosX((int) (SCR_WIDTH * 0.5)	);
				redCell.setSelected(true);
				whiteCell.setSelected(false);
				platelet.setSelected(false);
			}
		if(whiteCell.clicked(clickX, clickY))
			{
				gameStateRef.switchCellType(Entity.WHITECELL);
				//highLighter.setPosX((int) (SCR_WIDTH * 0.5)	- SPACER);
				redCell.setSelected(false);
				whiteCell.setSelected(true);
				platelet.setSelected(false);
			}
		if(platelet.clicked(clickX, clickY))
			{
				gameStateRef.switchCellType(Entity.PLATELET);
				//highLighter.setPosX((int) (SCR_WIDTH * 0.5)	+ SPACER);
				redCell.setSelected(false);
				whiteCell.setSelected(false);
				platelet.setSelected(true);//Button should be in an array
			}
	}
	
	private void setPositions()
	{
		//set pos for button elements depending on the screen size.
		//Maybe we want to do a check for the large/medium./small screen?
		SCR_WIDTH = gameStateRef.getWindowWidth();
		SCR_HEIGHT = gameStateRef.getWindowHeight();
		SPACER = (int) (SCR_WIDTH * 0.14);
		BUTTONY = (int) ( SCR_HEIGHT - (SCR_HEIGHT * 0.2));
		BUTTON_RAD = (int) (SCR_WIDTH * 0.11);
		SELECTED_RAD = (int) (BUTTON_RAD * 1.34);
		BOARD_WIDTH = (int) ( SCR_WIDTH * 0.19 );
		INDENT = (int) ( SCR_WIDTH * 0.11 );
	}
	
	private void CreateButtons()
	{
		redCell = new Button( (int) (SCR_WIDTH * 0.5), BUTTONY, BUTTON_RAD, BUTTON_RAD);
		Bitmap bitmapSprite = BitmapFactory.decodeResource(gameStateRef.getGameView().getResources(), R.drawable.red_cells);
		Sprite sprite = new Sprite(bitmapSprite, 1, 1, 1, 1);
		sprite.setAlpha(150);
		redCell.setSprite(sprite);
		sprite = new Sprite(bitmapSprite, 1, 1, 1, 1);
		sprite.setAlpha(220);
		redCell.setLarge(sprite, SELECTED_RAD);
		
		whiteCell = new Button((int) (SCR_WIDTH * 0.5) - SPACER, BUTTONY, BUTTON_RAD, BUTTON_RAD);
		bitmapSprite = BitmapFactory.decodeResource(gameStateRef.getGameView().getResources(), R.drawable.white_cells);
		sprite = new Sprite(bitmapSprite, 1, 1, 1, 1);
		sprite.setAlpha(150);
		whiteCell.setSprite(sprite);
		sprite = new Sprite(bitmapSprite, 1, 1, 1, 1);
		sprite.setAlpha(220);
		whiteCell.setLarge(sprite, SELECTED_RAD);
		
		platelet = new Button((int) (SCR_WIDTH * 0.5) + SPACER, BUTTONY, BUTTON_RAD, BUTTON_RAD);
		bitmapSprite = BitmapFactory.decodeResource(gameStateRef.getGameView().getResources(), R.drawable.platelets);
		sprite = new Sprite(bitmapSprite, 1, 1, 1, 1);
		sprite.setAlpha(150);
		platelet.setSprite(sprite);
		sprite = new Sprite(bitmapSprite, 1, 1, 1, 1);
		sprite.setAlpha(220);
		platelet.setLarge(sprite, SELECTED_RAD);
		
		//highLighter = new HighLight( (int) (SCR_WIDTH * 0.5) - SPACER, BUTTONY, SELECTED_RAD, SELECTED_RAD );
		//highLighter.setPaint(45, 44, 220, 165);
	}
}