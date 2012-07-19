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

public class Platelet extends Cell implements GameObject
{
	public Platelet (int startX, int startY, Vector2D istart2d, Resources viewRes, GameModel refModel)
	{
		super(startX, startY, istart2d, 11, viewRes, refModel);
		getEntityColor().setColor(Color.MAGENTA);
		this.setType(Entity.PLATELET);
		this.setMouvementBehavior(new Wandering());
		Bitmap bitmapSprite = BitmapFactory.decodeResource(viewRes, R.drawable.platelets);
		sprite = new Sprite(bitmapSprite, 1, 1, 1, 1);
		sprite.resize(24, 24);
	}
	
	public void Collide(Entity e1)
	{
		if(e1.getType() == Entity.VIRUS)
		{
			e1.setRadius(e1.getRadius()+1);
			this.setRemove(true);
		}
		else if(e1.getType() == Entity.FATCELL)
			this.setRemove(true);
		else if(e1.getType() == Entity.ORGAN)
			this.setRemove(true);
	}
	
	protected void OutOfBounds()
	{
		model.scoreCal.regEvent(GameScore.PLATELET_BOUNDS);
		super.OutOfBounds();
	}
}
