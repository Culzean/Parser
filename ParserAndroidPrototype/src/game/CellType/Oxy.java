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

package game.CellType;

import game.Entity;
import game.GameModel;
import game.GameScore;
import game.EntityMouvementBehavior.CellStop;
import game.EntityMouvementBehavior.EntityMouvementBehavior;
import game.EntityMouvementBehavior.OxyFloat;
import game.EntityMouvementBehavior.Wandering;
import gameEngine.GameObject;
import gameEngine.ParserView;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Oxy extends Entity implements GameObject
{
	private int life;
	private float dur, start,flash, flash_max;
	private boolean draw;
	private Paint faint;
	private int trapCol;
	private GameModel model;
	
	public Oxy(float x, float y, int rad, int idur, Resources viewRes, GameModel modelRef)
	{
		super(x, y, rad, Entity.ORGAN, viewRes); // add constant for the type!!!!!
		setDur(idur);
		life = idur;
		start = (float) (idur - idur * 0.1);
		draw = true; remove = false;
		flash = (float) ((2 * idur / 3));
		flash_max = (float) (idur - (idur / 3));
		faint = new Paint(Color.BLUE);
		faint.setAlpha(110);
		model = modelRef;
		trapCol = Color.rgb(183, 180, 255);
		//Organ are blue for now
		getEntityColor().setColor(Color.BLUE);
		this.setType(Entity.OXYGEN);
		this.setMouvementBehavior(new CellStop( EntityMouvementBehavior.CELLSTOP ));
	}
	
	public boolean Update(double beat, double dt)
	{
		//none moving object right now

		getMouvementBehavior().update(this, beat, dt);
		
		//test on edges
		if( this.getPosX() > (ParserView.windowWidth + radius) ||
			this.getPosX() < (0 - radius) ||
			this.getPosY() > (ParserView.windowHeight + radius) ||
			this.getPosY() < (0 - radius) )
		{
			OutOfBounds();
		}
				
		if(lapVec.x > 0 || lapVec.y > 0)
		{
			this.ResolveCol();
			lapVec.x = 0; lapVec.y = 0;
		}
		//do we want to remove this yet?
		this.setDur( (float)(dur - (dt * 0.01) ));
		if(draw)
		{
			flash -= dt * 0.07;
			if(flash < 0)
				draw = false;
		}else
		{
			flash += dt* 0.2;
			if(flash > flash_max)
			{
				draw = true;
				flash_max *= (dur *1.5/ life);
				if(flash_max < life / 15)
					flash_max = life / 15;
			}
		}
		if(dur < 0)
			{
				remove = true;
				if(getRadius() == GameModel.ORGAN_RADIUS_MIN)
				{
					model.orderCell().order(Entity.FATCELL, GameModel.FAT_RADIUS_DEF, (int)getPosX(), (int)getPosY());
					model.PlaySlop();
					model.slowerHeart();
				}
				else{
					model.orderCell().order(Entity.CELLPOP, radius * 2, (int)getPosX(), (int)getPosY());
				}
			}
		
		if(getMouvementBehavior().getMove() == EntityMouvementBehavior.CELLSTOP)
		{
			if(dur < start )
				this.setMouvementBehavior(new OxyFloat(EntityMouvementBehavior.OXYFLOAT));
		}
		

		return isRemoved();
	}
	
	public void Draw(Canvas canvas)
	{
		if(draw)
			super.Draw(canvas);
		else
			canvas.drawCircle((int)this.getPosX(), (int)this.getPosY(), (int)getRadius(), faint);
	}

	public void Collide(Entity c1)
	{
		if(c1.getType() == Entity.REDCELL)
		{
			this.setDur((float) (this.getDur()*0.85));
			this.setRadius((int) (this.getRadius()*1.1));
			this.Transport( c1.getVelX(), c1.getVelY(), EntityMouvementBehavior.WANDERING );
			model.scoreCal.regEvent(GameScore.ORGAN_HIT);
			model.PlayPlop();
			model.heartRef.incrRun();
		}
		else if(c1.getType() == Entity.ORGAN)
		{
			if(this.getMouvementBehavior().getMove() == EntityMouvementBehavior.WANDERING )
			{
				//this.setRadius((int) (this.getRadius() * 0.05));
				//if(this.getRadius() < 2)
					{
						model.orderCell().order(OXYFLARE, getRadius(), (int)posX, (int)posY);
						remove = true;
					}
			}
		}
		else if(c1.getType() == Entity.PLATELET)
		{
			this.setMouvementBehavior(new CellStop( EntityMouvementBehavior.CELLSTOP ));
			start = (float) (dur - dur * 0.2);
		}
		else if(c1.getType() == Entity.FATCELL)
		{
			if(this.getMouvementBehavior().getMove() == EntityMouvementBehavior.WANDERING )
			{
				model.orderCell().order(OXYFLARE, getRadius(), (int)posX, (int)posY);
				remove = true;
			}
		}
	}
	
	private void Transport(float newVx, float newVy, int newMove)
	{
		if( this.getMouvementBehavior().getMove() == EntityMouvementBehavior.OXYFLOAT
				|| this.getMouvementBehavior().getMove() == EntityMouvementBehavior.CELLSTOP)
		{
			//swap for new type
			this.setEntityColor(trapCol);
			this.setMouvementBehavior(new Wandering());
			this.setStartDX(newVx);		this.setStartDY(newVy);
		}
	}
	
	public float getDur() { return dur; } 	public void setDur(float dur) { this.dur = dur; }
	public int getLife() { return life;	}	public void setLife(int life) {	this.life = life; }

	@Override
	public void ResolveCol() {
		// TODO Auto-generated method stub
		
	}

	protected void OutOfBounds() {
		model.orderCell().order(FATCELL, GameModel.FAT_RADIUS_DEF, (int)posX, (int)posY);
		model.PlaySlop();
		remove = true;
		
	}
}
