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

import android.content.res.Resources;
import android.graphics.Color;
import events.Vector2D;
import game.Entity;
import game.GameModel;
import game.EntityMouvementBehavior.FatCellMovement;
import game.EntityMouvementBehavior.FatCellRoll;
import gameEngine.ParserView;
import game.EntityMouvementBehavior.*;
public class FatCell extends Cell 
{
	private boolean onWall = false;
	Vector2D vfatPair = null;
	FatCell collides = null;
	FatCellMovement normalMove = null;

	public boolean getWall() { return onWall; };
	
	public FatCell (int startX, int startY, Vector2D istart2d, int rad, Resources viewRes, GameModel refModel)
	{
		super(startX, startY, istart2d, rad, viewRes, refModel);
		normalMove = new FatCellMovement((int)istart2d.x, (int)istart2d.y, startX, startY);
		this.setMouvementBehavior( normalMove );
		getEntityColor().setColor(Color.YELLOW);
		this.setType(Entity.FATCELL);
	}
	public void Collide(Entity e1)
	{
		if(e1.getType() == Entity.FATCELL)
		{
			if(collides == null)
			{
				FatCell temp = (FatCell)e1;
				if(this.getMouvementBehavior().getMove() == EntityMouvementBehavior.FATCELLROLL)
						this.HitWall();
				else if(this.onWall == temp.onWall)
				{
					super.Collide(e1);
				}
				else if(!this.onWall)
				{
					//we may need to roll around this object
					collides = (FatCell)e1;
					Vector2D tempV = new Vector2D( ( this.getPosX() - e1.getPosX() ), ( this.getPosY() - e1.getPosY() ) );
					FatCellRoll tempMove = new FatCellRoll( (int)e1.getPosX(), (int)e1.getPosY(), tempV );
					this.setMouvementBehavior(tempMove);
				}
			}
			else if(e1 != collides)
				this.HitWall();
		}
		else
			super.Collide(e1);
	}
	
	public boolean Update(double beat)
	{
		//if(!onWall)
		{
			this.getMouvementBehavior().update(this, beat);
			if(this.getPosX() > (ParserView.windowWidth - this.getRadius()))
			{
				//change state to on wall and change pos to wall plus rad
				//but on the correct axis and with rad point the right way
				HitWall();
				this.setPosX(ParserView.windowWidth - this.getRadius());
			}
			else if(this.getPosX() < (0 + this.getRadius()))
			{
				HitWall();
				this.setPosX(0 + this.getRadius());
			}
			else if(this.getPosY() > (ParserView.windowHeight - this.getRadius()))
			{
				HitWall();
				this.setPosY(ParserView.windowHeight - this.getRadius());
			}
			else if(this.getPosY() < (0 + this.getRadius()))
			{
				HitWall();
				this.setPosY(0 + this.getRadius());
			}
		}
		return onWall;
	}
	
	private void HitWall()
	{
		collides = null;
		onWall = true;
		this.setMouvementBehavior(new FatCellStop());
	}
}
