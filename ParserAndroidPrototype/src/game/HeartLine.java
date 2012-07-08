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

import events.RandomNumGen;
import events.Vector2D;
import gameEngine.ParserView;

import java.util.Timer;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class HeartLine {

	private int width, height, noLine;
	private int drawY;
	private float velY;
	static Paint lineColor = new Paint();
	
	private int virusAlance = 3;
	
	Timer virusTimer = new Timer();
	
	private int ratio;
	RandomNumGen r;
	GameModel model;
	
	public HeartLine(int w, int h, GameModel _model, int iratio)
	{ 
		width = w; height = h;
		model = _model;
		setRatio(iratio);
		noLine = (int) (height * 0.2);
		velY = (float) ( (height / 1.4) * 0.001);
		drawY = 0;
		lineColor.setColor(Color.GRAY);
		r = new RandomNumGen();
		r.Randomize();
	}
	
	public void update(long dt)
	{
		//drawY += (height / noLine);
		drawY += (velY * dt);
		
		if(drawY >= height)
			drawY = 0 + 1;
		
		//r.Random(noLine * noLine) odd are one per cycle through
		if(r.Random((int)(noLine * noLine * 1.5)) < noLine)
			model.addGhost(r.Random(width), drawY);
		
		if(r.Random(noLine * noLine * virusAlance) < noLine)
			newVirus();
	}
	
	public void draw(Canvas g)
	{
		//if no indent at drawY
		//draw line straight across screen at drawY
		//else
		//draw line upto indentX, draw indent points, draw line from indentX + indentWidth to screen end
		OrganGhost ghost = model.getGhost(g);
		if(ghost != null)
		{
			if(drawY > ghost.getY() && drawY < (ghost.getY() + ghost.getHeight()))
			{
				g.drawLine(0, drawY, ghost.getX(), drawY, lineColor);
				g.drawLine((ghost.getX() + ghost.getWidth()), drawY, width, drawY, lineColor);
				ghost.draw(g,drawY);
			}
			else
				g.drawLine(0, drawY, width, drawY, lineColor);
		}
		else{
			g.drawLine(0, drawY, width, drawY, lineColor);
		}
	}
	
	public void newVirus()
	{
		//////////////////////////////////////
		//method to set new variables for a virus.
		//set it's direction information and decide which side to start on
		
		
		//create a virus. Might need to decide what type
		//find where to add the virus. set virus startx and y.
		//find directions of travel
		int randNum = r.Random(ParserView.windowWidth * 4);
		
		int side = (randNum / ParserView.windowWidth );  //0,1,2,3
		float dx = r.Random(2000) - 1000;
		dx /= 1000;
		float dy = r.Random(2000) - 1000;
		dy /= 1000;
		switch(side)
		{
			case 0:
				//
				model.addCell(r.Random( ParserView.windowWidth ), 0, new Vector2D(dx,dy), Entity.VIRUS);
				break;
			case 1:
				model.addCell(ParserView.windowWidth, r.Random( ParserView.windowHeight ), new Vector2D(dx,dy), Entity.VIRUS);
				break;
			case 2:
				model.addCell(r.Random( ParserView.windowWidth ), ParserView.windowHeight, new Vector2D(dx,dy), Entity.VIRUS);
				break;
			case 3:
				model.addCell(0, r.Random( ParserView.windowHeight ), new Vector2D(dx,dy), Entity.VIRUS);
				break;
			default:
			//System.out.println("Did not add virus! initialization information corrupt! " + side);
			break;
		}
	}

	public int getRatio() { return ratio; } public void setRatio(int ratio) { this.ratio = ratio; }
}
