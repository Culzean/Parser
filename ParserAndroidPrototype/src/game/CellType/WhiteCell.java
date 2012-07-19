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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import events.Vector2D;
import game.Entity;
import game.GameModel;
import game.GameScore;
import game.EntityMouvementBehavior.Wandering;
import game.test.R;
import gameEngine.GameObject;
import gameEngine.ParserView;
import gameEngine.Sprite;

public class WhiteCell extends Cell implements GameObject
{
	public static final int MAX_APPLICATION = 2;
	//might want to alter this number ingame or on creation
	private int life = 2770;
	int virusRemoved;
	
	public WhiteCell (int startX, int startY, Vector2D istart2d, Resources viewRes, GameModel refModel)
	{
		super(startX, startY, istart2d, 16, viewRes, refModel);
		getEntityColor().setColor(Color.WHITE);
		this.setType(Entity.WHITECELL);
		this.setMouvementBehavior(new Wandering());
		virusRemoved = 0;
		Bitmap bitmapSprite = BitmapFactory.decodeResource(viewRes, R.drawable.white_cells);
		sprite = new Sprite(bitmapSprite, 1, 1, 1, 1);
	}

	public boolean Update(double beat, double dt)
	{	
		life -= ( dt );
		if(life < 0 || super.Update(beat, dt))
			this.setRemove(true);
		
		return isRemoved();
	}
	
	protected void OutOfBounds()
	{
		if( this.getPosX() < (0 - radius) )
			this.setPosX(ParserView.windowWidth + radius);
		else if( this.getPosX() > (ParserView.windowWidth + radius) )
			this.setPosX( 0 - radius );
		if( this.getPosY() < (0 - radius) )
			this.setPosY( (ParserView.windowHeight + radius) );
		else if( this.getPosY() > (ParserView.windowHeight + radius) )
			this.setPosY((0 - radius));
	}
	
	public void Collide(Entity e1)
	{
		
		if(e1.getType() == Entity.VIRUS)
		{
			model.disinfect();
			life -= 1000;
			if(life < 0){
				e1.setRemove(true);
				model.scoreCal.regEvent(GameScore.VIRUS_HIT);
			}
		}
		else if(e1.getType() == Entity.FATCELL)
		{
			this.colVec.x = 0;
			this.colVec.y = 0;
		}
		else if(e1.getType() == Entity.ORGAN)
		{
			
		}
		else
			super.Collide(e1);
	}
}
