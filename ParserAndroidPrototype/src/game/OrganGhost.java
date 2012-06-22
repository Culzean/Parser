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

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import events.RandomNumGen;
import events.Vector2D;

public class OrganGhost 
{
	private Vector2D position;
	private int width, height;
	final int FULL_POINTS = 20;
	private final int FACTOR = 20;
	private Paint ghostColor = new Paint();
	RandomNumGen r;
	
	private float indent[];

	public OrganGhost(int x, int y)
	{
		setWidth(FULL_POINTS * 4); setHeight(50);
		position = new Vector2D(x,y);
		r = new RandomNumGen();
		r.Randomize();
		ghostColor.setColor(Color.WHITE);
		indent = this.getIndent();
	}
	
	public void draw(Canvas g, int curY)
	{
		int curX = getX();
		for(int i =0; i< FULL_POINTS-1; i++)
		{
			g.drawLine(curX, curY + (int) (indent[i] * FACTOR), curX +4, curY + (int)(indent[i+1] * FACTOR), ghostColor);
			curX = curX + 4;
		}
	}
	
	private float[] getIndent()
	{
		float set[] = new float[FULL_POINTS];
		
		for(int i=0; i<FULL_POINTS; i++)
		{//generate a random number between 1 and -1
			int rand = r.Random(720);
			if(rand > 360)
				set[i] = (float) Math.cos(rand);
			else
				set[i] = (float) Math.sin(rand);
		}
		set[0] = 0; set[FULL_POINTS-1] = 0;
		
		return set;
	}

	public int getX() {	return (int)position.x; } 			public void setX(int xPos) { position.x = xPos; }
	public int getY() { return (int)position.y; } 			public void setY(int yPox) { position.y = yPox; }
	public int getWidth() {	return width; } 				public void setWidth(int width) { this.width = width; }
	public int getHeight() { return height; } 				public void setHeight(int height) {	this.height = height; }
	public int getCenX() { return (int) (getX() + getWidth() * 0.5); }
	public int getCenY() { return (int) (getY() + getHeight() * 0.5); }
}
