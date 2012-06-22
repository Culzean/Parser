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
import game.EntityMouvementBehavior.Wandering;
import game.test.R;
import gameEngine.Sprite;

public class RedCell extends Cell 
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
		super.Collide(e1);
		
		if(e1.getType() == Entity.VIRUS)
			this.infect();
	}
}
