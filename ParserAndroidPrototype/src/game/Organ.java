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
	private GameModel model;
	
	public Organ(double x, double y, int rad, int idur, Resources viewRes, GameModel modelRef)
	{
		super(x, y, rad, Entity.ORGAN, viewRes); // add constant for the type!!!!!
		setDur(idur); setLife(0);
		draw = true; remove = false;
		xFlash = 29;
		model = modelRef;
		//Organ are blue for now
		getEntityColor().setColor(Color.BLUE);
		this.setType(Entity.ORGAN);
	}
	
	public boolean Update(double beat, double dt)
	{
		//none moving object right now

		//do we want to remove this yet?
		if(++life > getDur())
			{
				remove = true;
				if(getRadius() == GameModel.ORGAN_RADIUS_MIN)
				{
					model.orderCell().order(Entity.FATCELL, model.FAT_RADIUS_DEF, (int)getPosX(), (int)getPosY());
					model.PlaySlop();
					model.slowerHeart();
				}
				else{
					model.orderCell().order(Entity.CELLPOP, radius * 2, (int)getPosX(), (int)getPosY());
				}
			}
		
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
	
	public void draw(Canvas canvas)
	{
		if(draw)
			super.Draw(canvas);
	}

	public void Collide(Entity c1)
	{
		if(c1.getType() == Entity.REDCELL)
		{
			this.setDur((int) (this.getDur()*0.85));
			this.setRadius((int) (this.getRadius()*1.1));
		}
	}
	
	public int getDur() { return dur; } 	public void setDur(int dur) { this.dur = dur; }
	public int getLife() { return life;	}	public void setLife(int life) {	this.life = life; }

	@Override
	public void ResolveCol() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void OutOfBounds() {
		// TODO Auto-generated method stub
		
	}
}
