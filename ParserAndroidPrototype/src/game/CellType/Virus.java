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
import game.EntityMouvementBehavior.Harmonic;
import game.test.R;
import gameEngine.GameObject;
import gameEngine.ParserView;
import gameEngine.Sprite;

public class Virus extends Cell implements GameObject
{	
	public Virus (int startX, int startY, Vector2D istart2d, Resources viewRes, GameModel refModel)
	{
		super(startX, startY, istart2d, 16, viewRes, refModel);
		getEntityColor().setColor(Color.GREEN);
		this.setType(Entity.VIRUS);
		this.setMouvementBehavior(new Harmonic( istart2d, 0 ));
		Bitmap bitmapSprite = BitmapFactory.decodeResource(viewRes, R.drawable.virus);
		sprite = new Sprite(bitmapSprite, 1, 1, 1, 1);
	}
	
	protected void OutOfBounds()
	{
		if( this.getPosX() < (0 - radius) )
			this.setPosX(ParserView.windowWidth + radius);
		else if( this.getPosX() > (ParserView.windowHeight + radius) )
			this.setPosX( 0 - radius );
		if( this.getPosY() < (0 - radius) )
			this.setPosY( (ParserView.windowHeight + radius) );
		else if( this.getPosY() > (ParserView.windowHeight + radius) )
			this.setPosY((0 - radius));
	}
	
	public void Collide(Entity e1)
	{
		
	}
	
	
}