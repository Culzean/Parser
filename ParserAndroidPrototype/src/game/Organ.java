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

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;

public class Organ extends Entity 
{
	private int dur, life, flash, xFlash;
	private boolean draw;
	
	public Organ(double x, double y, int rad, int idur, Resources viewRes)
	{
		super(x, y, rad, Entity.ORGAN, viewRes); // add constant for the type!!!!!
		setDur(idur); setLife(0);
		draw = true; remove = false;
		xFlash = 29;
		//Organ are blue for now
		getEntityColor().setColor(Color.BLUE);
	}
	
	public boolean update(double beat)
	{
		//none moving object right now

		//do we want to remove this yet?
		if(++life > getDur())
			remove = true;
		
		//create a flash as the object nears removal
		if(draw && (getLife()) > (getDur() / 9 ) && flash == 0)
		{
			draw = false; 
			flash++;
		}
		else if(!draw && (getLife()) > (getDur() / 9 ) && flash > 0)
			++flash;
		else if((getLife()) > (getDur() / 7.8 ))
			--flash;
			
		if(flash > xFlash)
		{
			--xFlash;
			draw = true;
			flash--;
		}

		return isRemoved();
	}
	
	public void Draw(Canvas canvas)
	{
		if(draw)
			super.Draw(canvas);
	}

	public void Collide(Entity c1)
	{
		c1.setRemove(true);
		this.setDur((int) (this.getDur()*0.85));
		this.setRadius((int) (this.getRadius()*1.1));
	}
	
	public int getDur() { return dur; } 	public void setDur(int dur) { this.dur = dur; }
	public int getLife() { return life;	}	public void setLife(int life) {	this.life = life; }
}
