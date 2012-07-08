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
import gameEngine.Sprite;

public class RedCell extends Cell implements GameObject
{
	public RedCell(int startX, int startY, Vector2D istart2d, Resources viewRes, GameModel refModel) 
	{
		super(startX, startY, istart2d, 12, viewRes, refModel);
		getEntityColor().setColor(Color.RED);
		this.setType(Entity.REDCELL);
		this.setMouvementBehavior(new Wandering());
		Bitmap bitmapSprite = BitmapFactory.decodeResource(viewRes, R.drawable.red_cells);
		sprite = new Sprite(bitmapSprite, 1, 1, 1, 1);
		sprite.resize(24, 24);
	}
	
	public void Collide(Entity e1)
	{
		this.colVec.x = ( e1.getPosX() - this.getPosX() );
		this.colVec.y = ( e1.getPosY() - this.getPosY() );
		
		float overlap = (float) ( ( this.getRadius() + e1.getRadius() )
				- Math.abs(colVec.getLength()) );

		this.lapVec.x = Math.abs( colVec.x * overlap * 0.5 );
		this.lapVec.y = Math.abs( colVec.y * overlap * 0.5 );

		if(e1.getType() == Entity.VIRUS)
			this.infect( e1.getMouvementBehavior().getStartDir(), e1.getMouvementBehavior().getAngle() );

		else if(e1.getType() == Entity.FATCELL)
			this.setRemove(true);
		else if (e1.getType() == Entity.ORGAN)
			this.setRemove(true);
		
	}
	
	public void ResolveCol()
	{
		//decide how to resolve the collision
		if( colVec.x < 0 )//to left on screen
			this.setPosX( this.getPosX() + lapVec.x );
		else
			this.setPosX( this.getPosX() - lapVec.x );
				
		if( colVec.y < 0 )//above on screen
			this.setPosY( this.getPosY() + lapVec.y );
		else
			this.setPosY( this.getPosY() - lapVec.y );
	}
	
	protected void OutOfBounds()
	{
		model.scoreCal.regEvent(GameScore.RED_BOUNDS);
		super.OutOfBounds();
	}
}
